package com.equinox;

public class ErrorHandler {

	public static Signal process(ParsedInput c, Memory memory) {
		
		return new Signal(Signal.SIGNAL_INVALID_COMMAND);
	}

}
