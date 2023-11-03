package com.increff.commons.sheet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParquetFileTest {

    public static String PARQUET_TEST_FILE = "src/test/resources/com/increff/commons/sheet/parquet_test.parquet";
    public static String PARQUET_TEST_FILE_ERROR_SCHEMA_MISMATCH = "src/test/resources/com/increff/commons/sheet/parquet_test1.parquet";
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public static InputStream getInputStream(String resource) throws FileNotFoundException {
        return new FileInputStream(resource);
    }

    public static OutputStream getOutputStream(String resource) throws IOException {
        File file = new File(resource);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return new FileOutputStream(file);
    }

    @Test
    public void testWrite() throws IOException {
        DemoParquetFile file = new DemoParquetFile();
        file.setData(new ArrayList<>(getExampleRowList()));

        OutputStream os = getOutputStream(PARQUET_TEST_FILE);
        file.write(os);
    }

    @Test
    public void testRead() throws Exception {
        DemoParquetFile file = new DemoParquetFile();
        InputStream is = getInputStream(PARQUET_TEST_FILE);
        file.read(is);
        List<DemoParquetRow> fileRow = file.getData();
        List<DemoParquetRow> expectedRows = getExampleRowList();
        assertEquals(fileRow.get(0).channel, expectedRows.get(0).channel);
        assertEquals(fileRow.get(0).sku, expectedRows.get(0).sku);
        assertEquals(fileRow.get(0).mrp, expectedRows.get(0).mrp);
        assertEquals(fileRow.get(0).discount, expectedRows.get(0).discount);
        assertEquals(fileRow.get(0).price_bucket, expectedRows.get(0).price_bucket);
        assertEquals(fileRow.get(0).revenue, expectedRows.get(0).revenue, 0.01);
        assertEquals(fileRow.get(0).style_code, expectedRows.get(0).style_code);
        assertEquals(fileRow.get(0).store_codes, expectedRows.get(0).store_codes);
        assertEquals(fileRow.get(0).day, expectedRows.get(0).day);
        assertEquals(fileRow.get(0).enabled, expectedRows.get(0).enabled);
        assertEquals(fileRow.get(0).is_online, expectedRows.get(0).is_online);
        assertEquals(fileRow.get(0).seasons, expectedRows.get(0).seasons);

        assertEquals(fileRow.get(1).sku, expectedRows.get(1).sku);
        assertEquals(fileRow.get(1).mrp, expectedRows.get(1).mrp);
        assertEquals(fileRow.get(1).channel, expectedRows.get(1).channel);
        assertEquals(fileRow.get(1).discount, expectedRows.get(1).discount);
        assertEquals(fileRow.get(1).price_bucket, expectedRows.get(1).price_bucket);
        assertEquals(fileRow.get(1).revenue, expectedRows.get(1).revenue, 0.01);
        assertEquals(fileRow.get(1).style_code, expectedRows.get(1).style_code == null ? "": expectedRows.get(1).style_code);
        assertEquals(fileRow.get(1).store_codes, expectedRows.get(1).store_codes == null ? "": expectedRows.get(1).store_codes );
        assertEquals(fileRow.get(1).day, expectedRows.get(1).day);
        assertEquals(fileRow.get(1).enabled, expectedRows.get(1).enabled);
        assertEquals(fileRow.get(1).is_online, expectedRows.get(1).is_online);
        assertEquals(fileRow.get(1).seasons, expectedRows.get(1).seasons);

    }

    @Test
    public void  testReadSchemaMismatch() throws Exception {

        expectedEx.expect(SheetException.class);
        expectedEx.expectMessage("Expected Columns seasons,store_codes,price_bucket,discount,style_code,mrp,is_online,enabled but not present");
        DemoParquetFile file = new DemoParquetFile();
        InputStream is = getInputStream(PARQUET_TEST_FILE_ERROR_SCHEMA_MISMATCH);
        file.read(is);
        List<DemoParquetRow> fileRow = file.getData();
        for(DemoParquetRow demoParquetRow: fileRow){
            System.out.println(demoParquetRow.toString());
        }
    }


    private List<DemoParquetRow> getExampleRowList(){
        List<DemoParquetRow> rowList = new ArrayList<>();
        DemoParquetRow row = new DemoParquetRow();
        row.day = LocalDate.now();
        row.channel = 1;
        row.sku = 25;
        row.revenue = 25.55;
        row.discount = 32.33;
        row.mrp = 53343;
        row.price_bucket = 123354L;
        row.enabled = false;
        row.is_online = true;
        row.style_code = "Abc";
        row.seasons = new HashSet<>(Arrays.asList("abc", "def"));
        row.store_codes = Arrays.asList("wxy", "xys");

        rowList.add(row);

        DemoParquetRow row1 = new DemoParquetRow();
        row1.day = null;
        row1.channel = 1;
        row1.revenue = 25.55;
        row1.mrp = 53343;
        row1.enabled = false;
        row1.seasons = new HashSet<>(Arrays.asList("abc", "def"));
        row1.store_codes = Arrays.asList("wxy", "xys");

        row1.sku = null;
        row1.discount = null;
        row1.price_bucket = null;
        row1.is_online = null;
        row1.style_code = null;

        rowList.add(row1);

        return rowList;
    }

}
