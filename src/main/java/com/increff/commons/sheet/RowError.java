package com.increff.commons.sheet;

/**
 * @author gautham-nextscm
 */
public class RowError {
	
    private int row;
    private String error;

    public RowError(int row, String error) {
        this.error = error;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
