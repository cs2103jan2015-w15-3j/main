package com.equinox;

import java.util.ArrayList;

/**
 * @author Ikarus
 *
 */
public class DeleteHandler {

	public static Signal process(ParsedInput input, Memory memory) {
		
		ArrayList<KeyParamPair> paramPairList = input.getParamPairList();
		//Check for valid number of keywords
    	int numberOfKeywords = input.getParamPairList().size();
    	if(numberOfKeywords > 1){
    		return new Signal(Signal.SIGNAL_INVALID_PARAMS);
    	}
    	
    	int deleteIndex = Integer.parseInt(paramPairList.get(0).getParam());
    	try {
    		memory.remove(deleteIndex);
    	} catch (IndexOutOfBoundsException e) {
    		return new Signal(Signal.SIGNAL_NOT_FOUND);
    	}
		
		return new Signal(Signal.SIGNAL_SUCCESS);
	}

}
