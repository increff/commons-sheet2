package com.increff.commons.sheet;

import java.util.Arrays;
import java.util.Set;

/**
 * Implementation of the AbstractDataFile to handle .tsv files
 */
public class TsvFile extends AbstractDataFile<DataRow> {

    private String[] headers = {};

    @Override
    protected DataRow read(DataRow rec) {
        DataRow dataRow = new DataRow(rec.getColumns());
        dataRow.setTokens(rec.getTokens());
        dataRow.setNumber(rec.getNumber());
        return dataRow;
    }

    @Override
    protected void write(DataRow rec, DataRow t) {
        throw new UnsupportedOperationException("Write method is not supported");
    }

    @Override
    protected String[] getHeaders() {
        return headers;
    }

    public void setHeaders(Set<String> headerSet) {
        headers = Arrays.copyOf(headerSet.toArray(), headerSet.size(), String[].class);
    }

}
