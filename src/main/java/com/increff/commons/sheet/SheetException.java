package com.increff.commons.sheet;

public class SheetException extends Exception {

	private static final long serialVersionUID = 1L;

	public SheetException(String message) {
		super(message);
	}

	public SheetException(String message, Throwable t) {
		super(message, t);
	}
}
