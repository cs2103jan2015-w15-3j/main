//@author A0115983X
package com.equinox.exceptions;

public class InvalidTodoNameException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidTodoNameException() {

	}

	public InvalidTodoNameException(String message) {
		super(message);
	}
}