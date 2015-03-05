package com.equinox;

import java.util.ArrayList;

/**
 * Houses a method which processes the delete request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class DeleteHandler {

	/**
	 * Processes a ParsedInput object containing the delete command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	public static Signal process(ParsedInput input, Memory memory) {
		ArrayList<KeyParamPair> paramPairList = input.getParamPairList();
		
		//Check for valid number of keywords
    	int numberOfKeywords = paramPairList.size();
    	if(numberOfKeywords > 1){
    		return new Signal(Signal.DELETE_INVALID_PARAMS);
    	}
    	
    	int deleteIndex;
    	Todo deleted;
		try {
			deleteIndex = Integer.parseInt(paramPairList.get(0).getParam());
			deleted = memory.remove(deleteIndex);
		} catch (NumberFormatException e) {
			return new Signal(Signal.DELETE_INVALID_PARAMS);
		} catch (NullTodoException e) {
			return new Signal(e.getMessage());
		}
		
		return new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deleted));
	}

}
