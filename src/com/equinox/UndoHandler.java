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
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}
		
		//check if stateHistory has at least one stored state
		if(memory.stackSize() < 1){
			return new Signal(Signal.SIGNAL_NO_PREVIOUS_STATE);
		}
		
		try{
			memory.restoreHistoryState();
			
		} catch (Exception e) {
			return new Signal(Signal.SIGNAL_ERROR);
		}
		
		return new Signal(Signal.SIGNAL_SUCCESS);
	}
}
