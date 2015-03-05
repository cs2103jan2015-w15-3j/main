package com.equinox;
/**
 * Restores the last stored state in memory.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 *
 */
public class UndoHandler {

	public static Signal process(ParsedInput input, Memory memory) {
		//check if the number of parameters is correct
		int numberOfKeywords = input.getParamPairList().size();
		if(numberOfKeywords != 1){
			return new Signal(Signal.UNDO_INVALID_PARAMS);
		}
		
		try{
			memory.restoreHistoryState();
		} catch (StateUndefinedException e) {
			return new Signal(e.getMessage());
		}
		
		return new Signal(Signal.UNDO_SUCCESS_FORMAT);
	}
}
