package com.equinox;

final class ExceptionMessages {
	/**
	 * Exception messages. Used to construct new exceptions.
	 */
	static final String NULL_TODO_EXCEPTION = "Specified Todo does not exist.";
	static final String NO_HISTORY_STATES_EXCEPTION = "No undoable states exist";
	static final String NO_FUTURE_STATES_EXCEPTION = "No redoable states exist";
	static final String DATE_UNDEFINED_EXCEPTION = "Date String is empty or does not contain dates.";
	static final String NO_DATE_KEYWORD_EXCEPTION = "Date keywords cannot be found in input string";
}