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
		Todo edited;
		try { // TODO Check for empty params.
			ArrayList<KeyParamPair> paramPairList = input.getParamPairList();
			int userIndex = Integer.parseInt(paramPairList.get(0).getParam());
			edited = memory.setterGet(userIndex);
			
			for (int i = 1; i < paramPairList.size(); i++) {
				String keyword = paramPairList.get(i).getKeyword();
				String param = paramPairList.get(i).getParam();

				switch (keyword) {
				case "title":
					edited.setTitle(param);
					break;
				case "start":
					edited.setStartTime(DateParser.parseDate(param));
					break;
				case "end":
					edited.setEndTime(DateParser.parseDate(param));
					break;
				case "done":
					edited.setDone(Boolean.parseBoolean(param));
					break;
				}
			}
			if(!edited.isValid()) {
				try {
					memory.restoreHistoryState();
				} catch (StateUndefinedException e) {
					e.printStackTrace();
				}
                return new Signal(Signal.EDIT_INVALID_TIME, false);
			}
		} catch (DateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
            return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		}
        return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, edited),
                true);
	}

}
