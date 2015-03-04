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
    	int numberOfKeywords = paramPairList.size();
    	if(numberOfKeywords > 1){
    		return new Signal(Signal.SIGNAL_INVALID_PARAMS);
    	}
    	
    	int deleteIndex;
		try {
			deleteIndex = Integer.parseInt(paramPairList.get(0).getParam());
		} catch (NumberFormatException e1) {
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}
		
    	try {
    		memory.save();
    		memory.remove(deleteIndex);
    	} catch (IndexOutOfBoundsException e) {
    		return new Signal(Signal.SIGNAL_NOT_FOUND);
    	}
		
		return new Signal(Signal.SIGNAL_SUCCESS);
	}

}
