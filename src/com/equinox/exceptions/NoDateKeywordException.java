package com.equinox.exceptions;

public class NoDateKeywordException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDateKeywordException() {
	}

	public NoDateKeywordException(String message) {
		super(message);
	}
}
