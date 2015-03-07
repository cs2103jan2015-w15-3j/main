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
		Todo edited;
		try {
			if(input.containsEmptyParams()) {
				return new Signal(Signal.EMPTY_PARAM_EXCEPTION);
			}
			int userIndex = Integer.parseInt(keyParamPairList.get(0).getParam());
			edited = memory.setterGet(userIndex);
			
			for (KeyParamPair keyParamPair : keyParamPairList) {
				String keyword = keyParamPair.getKeyword();
				String param = keyParamPair.getParam();

				switch (keyword) {
				case "title":
					edited.setTitle(param);
					break;
				case "start":
					edited.setStartTime(param);
					break;
				case "end":
					edited.setEndTime(param);
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
				return new Signal(Signal.EDIT_INVALID_TIME);
			}
		} catch (DateUndefinedException e) {
			return new Signal(e.getMessage());
		} catch (NullTodoException e) {
			return new Signal(e.getMessage());
		} catch (NumberFormatException e) {
			return new Signal(Signal.EDIT_INVALID_PARAMS);
		}
		return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, edited));
	}

}
