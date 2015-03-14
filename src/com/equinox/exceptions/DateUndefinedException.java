package com.equinox.exceptions;

public class DateUndefinedException extends Exception {
	private static final long serialVersionUID = 1L;

	public DateUndefinedException() {
	}
	
	public DateUndefinedException(String message) {
		super(message);
	}
}