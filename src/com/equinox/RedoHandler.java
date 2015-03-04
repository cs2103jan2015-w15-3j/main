package com.equinox;

public class RedoHandler {

	public static Signal process(ParsedInput userInput, Memory memory) {
		int numberOfKeywords = userInput.getParamPairList().size();
		if(numberOfKeywords != 1){
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}
		
		try{
			memory.restoreFutureState();
		} catch (StateUndefinedException e) {
			return new Signal(Signal.SIGNAL_NO_PREVIOUS_STATE);
		}
		
		return new Signal(Signal.SIGNAL_SUCCESS);
	}
}
