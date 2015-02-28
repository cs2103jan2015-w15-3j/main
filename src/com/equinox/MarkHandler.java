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

public class MarkHandler {

	public static Signal process(ParsedInput input, Memory memory) {
		ArrayList<KeyParamPair> inputList = input.getParamPairList();

		// Ensure that there is only one KeyParamPair in inputList
		if (inputList.size() > 1) {
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}

		try {

			// -1 discrepancy between user input index and index in memory is
			// handled in Memory class
			int index = Integer.parseInt(inputList.get(0).getParam());

			Todo todoToMark = memory.get(index);
			todoToMark.isDone = true;

			// replace Todo in memory with marked Todo
			memory.set(index, todoToMark);

		} catch (Exception e) {
			e.printStackTrace();
			return new Signal(Signal.SIGNAL_ERROR);
		}

		return new Signal(Signal.SIGNAL_SUCCESS);
	}
}
