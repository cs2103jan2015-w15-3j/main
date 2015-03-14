package com.equinox;

import com.equinox.exceptions.StateUndefinedException;

/**
 * Restores the last stored state in memory.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 *
 */
public class UndoCommand extends Command{
	
	/**
	 * Creates an UndoCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */	
	public UndoCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
	public Signal execute() {
		//check if the number of parameters is correct
		int numberOfKeywords = keyParamPairList.size();
		if(numberOfKeywords != 1){
            return new Signal(Signal.UNDO_INVALID_PARAMS, false);
		}
		
		try{
			memory.restoreHistoryState();
		} catch (StateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		}
		
        return new Signal(Signal.UNDO_SUCCESS, true);
	}
}
