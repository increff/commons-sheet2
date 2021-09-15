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
