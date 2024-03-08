/*
 * Copyright (c) 2021. Increff
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.increff.commons.sheet;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.*;

public abstract class AbstractTSVFile<T> implements IDataFile<T> {

    private String newlineValue = "\n";
    private String delimValue = "\t";
    private String nullValue = "null";
    private Integer maxRows = 1_00_000;
    private String fileExtension = ".tsv";
    private int count;
    private CSVReader reader;
    private BufferedWriter writer;
    private HashMap<String, Integer> headerMap;
    private IProgressMonitor progress;
    private PagedList<RowError> errors;
    private ArrayList<T> data;

    /**
     * @params This function takes a string 's' as a delimeter.
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
        Set<String> expectedHeaderSet = new HashSet<>(Arrays.asList(getHeaders()));
        errors = new PagedList<>();
        count = 0;
        reader = new CSVReader(new InputStreamReader(is), delimValue.charAt(0));
        String[] tokens;

        // Read headers. If no headers, don't read further
        Set<String> headers = readHeaders();
        if (!(expectedHeaderSet.isEmpty())) {
            validateHeaders(headers, expectedHeaderSet);
        }
        DataRow dataRow = new DataRow(headerMap);
        while ((tokens = reader.readNext()) != null) {
            count++;
            if (count > maxRows) {
                throw new SheetException("Maximum rows allowed is : " + maxRows);
            }
            if (progress != null) {
                progress.process(count);
            }
            try {
                parse(tokens, dataRow);
                T t = read(dataRow);
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
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(os), delimValue.charAt(0));
        count = 0;
        // Write headers
        headerMap = new HashMap<>();
        String[] headers = getHeaders();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i], i);
        }
        csvWriter.writeNext(headers);

        // Write rows
        DataRow r = new DataRow(headerMap);
        r.setTokens(new String[headerMap.size()]);
        for (T t : data) {
            count++;
            write(r, t);
            csvWriter.writeNext(r.getTokens());
            if (progress != null) {
                progress.process(count);
            }
        }
        csvWriter.close();
    }

    @Override
    public void setProgressMonitor(IProgressMonitor progress) {
        this.progress = progress;
    }

    // Subclasses must override these methods
    protected abstract T read(DataRow rec) throws Exception;

    protected abstract void write(DataRow rec, T t);

    protected abstract String[] getHeaders();

    protected static void validateHeaders(Set<String> headers, Set<String> expectedHeaders) throws SheetException {
        Set<String> missingHeaders = new HashSet<>(expectedHeaders);
        missingHeaders.removeAll(headers);
        if (!missingHeaders.isEmpty())
            throw new SheetException(String.join(",", missingHeaders) + " are not found in the file");
    }


    /**
     * Reads and Maps the first line of the input file as header. Separates tokens based on delimValues
     * Creates a Mapping, stored in "headerMap", from the header token to an integer index
     *
     * @return true if header is non-null, false otherwise
     */
    private Set<String> readHeaders() throws SheetException, IOException {
        headerMap = new HashMap<>();
        String[] tokens = reader.readNext();
        if (tokens == null || tokens.length == 0) {
            throw new SheetException("Unable to read file");
        }

        for (int i = 0; i < tokens.length; i++) {
            if (headerMap.keySet().contains(tokens[i]))
                throw new SheetException(tokens[i] + " correspond to multiple columns");
            headerMap.put(tokens[i], i);
        }
        return headerMap.keySet();
    }

    /**
     * Parse input line based on delimValue. Replaces strings matching nullValue with 'null'
     * Saves the tokenized list and count of lines into data members of DataRow object
     *
     * @param tokens Input line to be parse
     * @param r    DataRow to be used for parsing
     * @return true if parsed without errors, false otherwise
     */
    private void parse(String[] tokens, DataRow r) throws SheetException {
        for (int i = 0; i < tokens.length; i++) {
            if (nullValue.equalsIgnoreCase(tokens[i])) {
                tokens[i] = null;
            }
        }
        if (tokens.length != headerMap.size()) {
            throw new SheetException("Record length does not match that of the header");
        }

        r.setTokens(tokens);
        r.setNumber(count);
    }

}
