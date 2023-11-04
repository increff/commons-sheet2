package com.increff.commons.sheet;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.avro.AvroSchemaConverter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

public abstract class AbstractParquetFile<T> implements IDataFile<T> {
    private String newlineValue = "\n";
    private String delimValue = "\t";
    private String nullValue = "null";
    private Integer maxRows = 1_00_000;
    private String fileExtension = ".parquet";
    private int count;
    private ParquetReader<GenericRecord> reader;
    private ParquetWriter<GenericRecord> writer;
    private Schema schema;
    private Set<String> binaryFields;
    private Set<String> dateTimeFields;
    private IProgressMonitor progress;
    private PagedList<RowError> errors;
    private ArrayList<T> data;

    /**
     * @param s This function takes a string 's' as a delimeter.
     *          By default it is '\t'
     *          To use any different delimeter, please override this function
     */
    public void setDelimiterValue(String s) {
        this.delimValue = s;
    }
    public String getFileExtension(){
        return this.fileExtension;
    }
    public void setFileExtension(String extension){
        this.fileExtension = extension;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public void setNullValue(String s) {
        this.nullValue = s;
    }

    public void setNewlineValue(String s) {
        this.newlineValue = s;
    }

    public List<RowError> getErrors() {
        return errors.getAll();
    }

    // Public methods
    @Override
    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public ArrayList<T> getData() {
        return data;
    }

    /**
     * Read and parse lines of data from an input stream and store them in a PagedList
     *
     * @param is InputStream from which data is to be read
     */
    @Override
    public void read(InputStream is) throws Exception {

        PagedList<T> pagedList = new PagedList<>();
        Schema expectedSchema = computeSchema();
        errors = new PagedList<>();
        count = 0;

        ParquetStreamReader parquetStream = new ParquetStreamReader(is);

        ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(parquetStream)
                .disableCompatibility()
                .build();

        GenericRecord record;

        while ((record = reader.read()) != null) {
            if(count == 0){
                readSchema(record);
                validateHeaders(schema,expectedSchema);
            }

            count++;
            if (count > maxRows) {
                throw new SheetException("Maximum rows allowed is : " + maxRows);
            }
            if (progress != null) {
                progress.process(count);
            }

            try {
                readBinaryToString(record);
                readEpochToDatetime(record);
                T t = read(record);
                pagedList.add(t);
            } catch (Exception e) {
                errors.add(new RowError(count, e.getMessage()));
            }
        }
        data = pagedList.getAll();
        reader.close();

    }


    /**
     * Write contents of the stored 'data' PagedList onto an OutputStream
     *
     * @param os OutputStream on which to write data
     */
    @Override
    public void write(OutputStream os) throws IOException {

        Schema schema = computeSchema();

        ParquetStreamWriter out = new ParquetStreamWriter(os);
        writer = AvroParquetWriter.<GenericRecord>builder(out)
                .withSchema(schema)
                .withDataModel(ReflectData.get())
                .withConf(new Configuration())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                .build();

        count = 0;
        for (T t : data) {
            count++;
            GenericData.Record record = new GenericData.Record(schema);
            write(record, t);
            convertBinaryToUTF8(record);
            convertDateTimeToEpoch(record);
            writer.write(record);
            if (progress != null) {
                progress.process(count);
            }
        }
        writer.close();
    }

    @Override
    public void setProgressMonitor(IProgressMonitor progress) {
        this.progress = progress;
    }

    // Subclasses must override these methods
    protected abstract T read(GenericRecord rec) throws Exception;
    protected abstract void write(GenericRecord rec, T t);
    protected abstract Type[] getSchema();

    protected static void validateHeaders(Schema incomingSchema, Schema expectedSchema) throws SheetException {

        Map<String, Schema> fieldInfo1 = extractFieldInfo(incomingSchema);
        Map<String, Schema> fieldInfo2 = extractFieldInfo(expectedSchema);

        List<String> schemaValidations = new ArrayList<>();

        Set<String> missingColumns = fieldInfo2.keySet();
        missingColumns.removeAll(fieldInfo1.keySet());

        if(!missingColumns.isEmpty())
            schemaValidations.add("Expected Columns " + String.join(",", missingColumns)+ " but not present");

        for (Map.Entry<String, Schema> entry : fieldInfo1.entrySet()) {
            String fieldName = entry.getKey();
            Schema fieldType1 = entry.getValue();
            Schema fieldType2 = fieldInfo2.get(fieldName);

            if (fieldType2 != null && !fieldType1.equals(fieldType2)) {
                schemaValidations.add("Column '" + fieldName + "' has a different type than expected.");
            }
        }

        if(!schemaValidations.isEmpty())
            throw new SheetException(String.join(";", schemaValidations));
    }

    public static Map<String, Schema> extractFieldInfo(Schema schema) {
        Map<String, Schema> fieldInfo = new HashMap<>();
        for (Schema.Field field : schema.getFields()) {
            fieldInfo.put(field.name(), field.schema());
        }
        return fieldInfo;
    }


    private Schema computeSchema(){
        MessageType parquetSchema = new MessageType(getParameterClass().getSimpleName(), getSchema());
        Schema avroSchema = new AvroSchemaConverter().convert(parquetSchema);

        binaryFields = new HashSet<>();
        dateTimeFields = new HashSet<>();

        for(Type field: getSchema()){
            if(field.asPrimitiveType().getPrimitiveTypeName().equals(PrimitiveType.PrimitiveTypeName.BINARY)){
                binaryFields.add(field.getName());
            } else if (field.getOriginalType()!=null &&
                    field.getOriginalType().equals(OriginalType.DATE)) {
                dateTimeFields.add(field.getName());
            }
        }

        return avroSchema;
    }


    private void readSchema(GenericRecord record) {
        schema = record.getSchema();
    }

    private void convertBinaryToUTF8(GenericRecord record){
        for(String fieldName: binaryFields){
            if(record.get(fieldName) != null){
                record.put(fieldName, StandardCharsets.UTF_8.encode((String) record.get(fieldName)));
            }
        }
    }

    private void readBinaryToString(GenericRecord record){
        for(String fieldName: binaryFields){

            if(record.get(fieldName) == null){
                record.put(fieldName, "");
            }
            else {
                record.put(fieldName, StandardCharsets.UTF_8.decode((ByteBuffer) record.get(fieldName)).toString());
            }
        }
    }

    private void convertDateTimeToEpoch(GenericRecord record) {
        for(String fieldName: dateTimeFields){
            if(record.get(fieldName) != null){
                record.put(fieldName, ((LocalDate)record.get(fieldName)).toEpochDay());
            }
        }
    }

    private void readEpochToDatetime(GenericRecord record) {
        for(String fieldName: dateTimeFields){
            if(record.get(fieldName) != null){
                record.put(fieldName, LocalDate.ofEpochDay((int) record.get(fieldName)));
            }
        }
    }

    private Class<T> getParameterClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
