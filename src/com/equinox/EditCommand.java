package com.equinox;

import com.equinox.exceptions.NullTodoException;
import com.equinox.exceptions.StateUndefinedException;

/**
 * Houses a method which processes the edit request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class EditCommand extends Command{

	/**
	 * Creates an EditCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public EditCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the edit command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * Reverts the Todo to its original state if the changes are invalid.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		Todo preEdit, postEdit;
		try {
			if(input.containsEmptyParams() || input.containsOnlyCommand()) {
				return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
			}
			int userIndex = Integer.parseInt(keyParamPairs.get(0).getParam());
			preEdit = new Todo(memory.get(userIndex));
			postEdit = memory.setterGet(userIndex);
			
			for (int i = 1; i < keyParamPairs.size(); i++) {
				Keywords keyword = keyParamPairs.get(i).getKeyword();
				String param = keyParamPairs.get(i).getParam();

				switch (keyword) {
				case NAME:
					postEdit.setName(param);
					memory.saveToFile();
					break;
				case START:
					if(param.equals("null")) {
						postEdit.setStartTime(null);
					} else if(!input.containDates()) {
						return new Signal(Signal.EDIT_INVALID_DATE, false);
					} else {
						postEdit.setStartTime(dateTimes.remove(0));
					}

					postEdit.setStartTime(dateTimes.remove(0));
					memory.saveToFile();
					break;
				case END:
					if(param.equals("null")) {
						postEdit.setEndTime(null);
					} else if(!input.containDates()) {
						return new Signal(Signal.EDIT_INVALID_DATE, false);
					} else {
						postEdit.setEndTime(dateTimes.remove(0));
					}

					postEdit.setEndTime(dateTimes.remove(0));
					memory.saveToFile();
					break;
				case DONE:
					postEdit.setDone(Boolean.parseBoolean(param));
					memory.saveToFile();
					break;
				default:
					// Invalid Params
					return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
			}
			if(!postEdit.isValid()) {
				try {
					memory.restoreHistoryState();
				} catch (StateUndefinedException e) {
					e.printStackTrace();
				}
                return new Signal(Signal.EDIT_END_BEFORE_START, false);
			}
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
            return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		}
        return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, preEdit,
                postEdit), true);
	}

}
