package com.equinox;

/**
 * Houses a method which processes the redo request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class RedoCommand extends Command {

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
	@Override
	public Signal execute(ParsedInput userInput, Memory memory) {
		int numberOfKeywords = userInput.getParamPairList().size();
		if(numberOfKeywords != 1){
			return new Signal(Signal.REDO_INVALID_PARAMS);
		}
		
		try{
			memory.restoreFutureState();
		} catch (StateUndefinedException e) {
			return new Signal(e.getMessage());
		}
		
		return new Signal(Signal.REDO_SUCCESS);
	}
}
