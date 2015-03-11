package com.equinox;

/**
 * Houses a method which processes the edit request from the user. 
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class EditCommand extends Command{

	public EditCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

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
	@Override
	public Signal execute() {
		Todo preEdit, postEdit;
		try {
			if(input.containsEmptyParams()) {
				return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
			}
			int userIndex = Integer.parseInt(keyParamPairList.get(0).getParam());
			preEdit = new Todo(memory.get(userIndex));
			postEdit = memory.setterGet(userIndex);
			
			for (int i = 1; i < keyParamPairList.size(); i++) {
				KEYWORDS keyword = keyParamPairList.get(i).getKeyword();
				String param = keyParamPairList.get(i).getParam();

				switch (keyword) {
				case NAME:
					postEdit.setName(param);
					break;
				case START:
					postEdit.setStartTime(dateTimeList.remove(0));
					break;
				case END:
					postEdit.setEndTime(dateTimeList.remove(0));
					break;
				case DONE:
					postEdit.setDone(Boolean.parseBoolean(param));
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
                return new Signal(Signal.EDIT_INVALID_TIME, false);
			}
		} catch (DateUndefinedException e) {
            return new Signal(e.getMessage(), false);
		} catch (NullTodoException e) {
            return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
            return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		}
        return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, preEdit,
                postEdit), true);
	}

}
