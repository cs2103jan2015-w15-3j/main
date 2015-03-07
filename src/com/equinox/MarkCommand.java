package com.equinox;

/**
 * This class handles all user input with "mark" as the first keyword with the format of mark <index>.
 * It retrieves a Todo object from memory at the given index, marks the Todo as done and replaces the 
 * existing copy in memory.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 * 
 */
import java.util.ArrayList;

public class MarkCommand extends Command {
	/**
	 * Retrieves the Todo object specified by index in ParsedInput from Memory
	 * and marks it as done.
	 * 
	 * @param input
	 *            A ParsedInput object that contains a KEYWORDS type and an
	 *            ArrayList<KeyParamPair>
	 * @param memory
	 *            A memory object that stores Todo objects
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute(ParsedInput input, Memory memory) {
		if(input.containsEmptyParams()) {
			return new Signal(Signal.EMPTY_PARAM_EXCEPTION);
		}
		
		ArrayList<KeyParamPair> inputList = input.getParamPairList();

		// Ensure that there is only one KeyParamPair in inputList
		if (inputList.size() > 1) {
			return new Signal(Signal.INVALID_PARAMS_FOR_MARK_HANDLER);
		}

		try {

			// -1 discrepancy between user input index and index in memory is
			// handled in Memory class
			int index = Integer.parseInt(inputList.get(0).getParam());
			Todo todoToMark = memory.setterGet(index);
			todoToMark.setDone(true);
			return new Signal(String.format(Signal.MARK_SUCCESS_SIGNAL_FORMAT, todoToMark));
		} catch (NullTodoException e) {
			return new Signal(String.format(Signal.EXCEPTIONS_FORMAT, e.getMessage()));
		} catch (NumberFormatException e) {
			return new Signal(String.format(Signal.INVALID_PARAMS_FOR_MARK_HANDLER));
		}
	}
}
