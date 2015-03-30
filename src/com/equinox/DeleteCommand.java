package com.equinox;

import com.equinox.exceptions.NullRuleException;
import com.equinox.exceptions.NullTodoException;

/**
 * Houses a method which processes the delete request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class DeleteCommand extends Command {

	/**
	 * Creates a DeleteCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public DeleteCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the delete command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		boolean isRecurringRule = false;
		
		// Ensure that there is only one KeyParamPair in inputList
		if (!input.containsOnlyCommand()) {
			return new Signal(Signal.DELETE_INVALID_PARAMS, false);
		}

		// Check 2nd Keyword for -r flag
		if (keyParamPairs.get(1).getKeyword() == Keywords.RULE) {
			keyParamPairs.remove(1);
			isRecurringRule = true;
		}
		
		if(input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}
    	
    	int deleteIndex;
    	Signal returnSignal;
    	Todo deleted;
    	RecurringTodoRule deletedRule;
		try {
			deleteIndex = Integer.parseInt(keyParamPairs.get(0).getParam());
			if(isRecurringRule) {
				deletedRule = memory.removeRecurringRule(deleteIndex);
				returnSignal = new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deletedRule), true);
			} else {
				deleted = memory.remove(deleteIndex);
				returnSignal = new Signal(String.format(Signal.DELETE_SUCCESS_FORMAT, deleted),
		                true);
			}
			memory.saveToFile();
		} catch (NumberFormatException e) {
            return new Signal(Signal.DELETE_INVALID_PARAMS, false);
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		} catch (NullRuleException e) {
			return new Signal(e.getMessage(), false);
		}
		
        return returnSignal;
	}

}
