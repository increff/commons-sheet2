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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a row of tabular data along with corresponding column headings
 */
public class DataRow {

    private String[] tokens;
    private HashMap<String, Integer> columns;
    int rowIndex;

    public DataRow(HashMap<String, Integer> index) {
        this.columns = index;
        this.tokens = null;
    }

    /* LOW LEVEL SETTER AND GETTER */

    protected int getIndex(String col) {
        Integer i = columns.get(col);
        if (i == null) {
            throw new RuntimeException("Invalid column:" + col);
        }
        return i;
    }

    /**
     * Fetches list of all tokens stored in the current DataRow object
     * @return List of Strings which are the tokens
     */
    public String[] getTokens() {
        return tokens;
    }

    /**
     * Get the value in a given column of the DataRow based on its column index
     * @param index Index of column to fetch value from
     * @return Fetched value
     */
    public String getValue(int index) {
        return tokens[index];
    }

    /**
     * Get the value in a given column of the DataRow based on its column name
     * @param col Name of column
     * @return Fetched value
     */
    public String getValue(String col) {
        return getValue(getIndex(col));
    }

    /**
     * Set the token variable to a list of Strings representing the row
     * @param tokens List of String row values
     */
    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    /**
     * Set the token variable to a list of Objects representing the row
     * @param tokens List of Object row values
     */
    public void setTokens(Object[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            setToken(i, tokens[i]);
        }
    }

    /**
     * Set the token value at a specified index
     * @param index Index at which to set token
     * @param token Value of token
     */
    public void setToken(int index, Object token) {
        tokens[index] = token == null ? null : token.toString();
    }

    /**
     * Set the token value at a specified column based on column name
     * @param col Column name for which to set token
     * @param token Value of token
     */
    public void setToken(String col, Object token) {
        setToken(getIndex(col), token);
    }

    /**
     * Set the "count" variable which represents the row number/index of the current row
     * @param i Row number
     */
    public void setNumber(int i) {
        this.rowIndex = i;
    }

    /**
     * @return Returns the row number for the current row
     */
    public int getNumber() {
        return rowIndex;
    }

    /**
     * Return a value from DataRow after parsing as a Date type
     * @param col Column name from which to fetch value
     * @param dateFormat Format of Date to be used for parsing
     * @return Parsed Date object
     */
    public Date getDate(String col, DateFormat dateFormat) throws ParseException {
        String s = getValue(col);
        return s == null ? null : dateFormat.parse(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as Double
     */
    public Double getDouble(String col) {
        String s = getValue(col);
        return s == null ? null : Double.parseDouble(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as BigInteger
     */
    public BigInteger getBigInt(String col) {
        String s = getValue(col);
        return s == null ? null : new BigInteger(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as BigDecimal
     */
    public BigDecimal getBigDecimal(String col) {
        String s = getValue(col);
        return s == null ? null : new BigDecimal(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as Integer
     */
    public Integer getInteger(String col) {
        String s = getValue(col);
        return s == null ? null : Integer.parseInt(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as Boolean
     */
    public Boolean getBoolean(String col) {
        String s = getValue(col);
        return s == null ? null : Boolean.parseBoolean(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched integer value from chosen column as boolean
     */
    public Boolean getBooleanFromInt(String col) {
        String s = getValue(col);
        return s == null ? null : s.equals("1");
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as LocalDate object
     */
    public LocalDate getLocalDate(String col) {
        String s = getValue(col);
        return s == null ? null : LocalDate.parse(s);
    }

    /**
     * @param col Name of column to fetch value from
     * @return Returns fetched value from chosen column as YearMonth
     */
    public YearMonth getYearMonth(String col) {
        String s = getValue(col);
        return s == null ? null : YearMonth.parse(s);
    }

    /* INTEGER BASED GETS */
    public String getStringIntern(int col) {
        String s = getValue(col);
        return s == null ? null : s.intern();
    }

    public String getString(int col) {
        return getValue(col);
    }

    public Double getDouble(int col) {
        String s = getValue(col);
        return s == null ? null : Double.parseDouble(s);
    }

    public BigInteger getBigInteger(int col) {
        String s = getValue(col);
        return s == null ? null : new BigInteger(s);
    }

    public BigDecimal getBigDecimal(int col) {
        String s = getValue(col);
        return s == null ? null : new BigDecimal(s);
    }

    public Integer getInteger(int col) {
        String s = getValue(col);
        return s == null ? null : Integer.parseInt(s);
    }

    public Boolean getBoolean(int col) {
        String s = getValue(col);
        return s == null ? null : Boolean.parseBoolean(s);
    }

    public LocalDate getLocalDate(int col) {
        String s = getValue(col);
        return s == null ? null : LocalDate.parse(s);
    }

    public YearMonth getYearMonth(int col) {
        String s = getValue(col);
        return s == null ? null : YearMonth.parse(s);
    }

    /**
     * @return HashMap representing mapping of column names to indices
     */
    public HashMap<String, Integer> getColumns() {
        return columns;
    }

}
