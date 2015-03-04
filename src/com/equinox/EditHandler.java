package com.equinox;

import java.util.ArrayList;

/**
 * Houses a method which processes the edit request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class EditHandler {

	/**
	 * Processes a ParsedInput object containing the edit command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * Reverts the Todo to its original state if the changes are invalid.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	public static Signal process(ParsedInput input, Memory memory) {
		ArrayList<KeyParamPair> paramPairList = input.getParamPairList();
		int userIndex = Integer.parseInt(paramPairList.get(0).getParam());
		Todo editing = memory.setterGet(userIndex);
		
		try {
			for (int i = 1; i < paramPairList.size(); i++) {
				String keyword = paramPairList.get(i).getKeyword();
				String param = paramPairList.get(i).getParam();

				switch (keyword) {
				case "title":
					editing.setTitle(param);
					break;
				case "start":
					editing.setStartTime(DateParser.parseDate(param));
					break;
				case "end":
					editing.setEndTime(DateParser.parseDate(param));
					break;
				case "done":
					editing.setDone(Boolean.parseBoolean(param));
					break;
				}
			}
			if(!editing.isValid()) {
				try {
					memory.restoreHistoryState();
				} catch (StateUndefinedException e) {
					e.printStackTrace();
				}
				return new Signal(Signal.SIGNAL_INVALID_PARAMS);
			}
		} catch (DateUndefinedException e) {
			return new Signal(Signal.SIGNAL_INVALID_PARAMS);
		}
		return new Signal(Signal.SIGNAL_SUCCESS);
	}

}
