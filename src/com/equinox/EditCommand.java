package com.equinox;

import org.joda.time.Period;

import com.equinox.exceptions.NullRuleException;
import com.equinox.exceptions.NullTodoException;
import com.equinox.exceptions.StateUndefinedException;

/**
 * Houses a method which processes the edit request from the user.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class EditCommand extends Command {

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
		try {
			boolean isRecurring = input.isRecurring();
			// Check if input contains more than 1 keyword (keyParamPairs.size() > 1)
			if (input.containsOnlyCommand()) {
				return new Signal(Signal.EDIT_INVALID_PARAMS, false);
			}
			
			if (keyParamPairs.get(1).getKeyword() == Keywords.RULE) {
				keyParamPairs.remove(1);
				if(!isRecurring) {
					isRecurring = true;
				}
			}

			if (input.containsEmptyParams()) {
				return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
			}

			int id = Integer.parseInt(keyParamPairs.get(0).getParam());
			Todo todo = memory.get(id);

			if (isRecurring) {
				// Check if todo is recurring
				if(!todo.isRecurring()) {
					return new Signal(Signal.EDIT_NOT_RECURRING, false);
				}
				int recurringId = todo.getRecurringId();
				RecurringTodoRule rule = memory.getRule(recurringId);
				
				for(int i = 1; i < keyParamPairs.size(); i++) {
					Keywords keyword = keyParamPairs.get(i).getKeyword();
					String param = keyParamPairs.get(i).getParam();
					
					// TODO: Save to file on each modifying action
					if(keyword == Keywords.NAME) {
						rule.setOriginalName(param);
						memory.saveToFile();
					}
					// return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
				
				Period period = input.getPeriod();
				
				if(input.isRecurring()) {
					rule.setRecurringInterval(period);
					memory.saveToFile();
				}
				
				if(input.hasLimit()) {
					rule.setRecurrenceLimit(input.getLimit());
					memory.saveToFile();
				}
					
				return new Signal("Recurring Todo rule successfully edited", true);
			} else {
				Todo preEdit, postEdit;
				preEdit = new Todo(todo);
				postEdit = memory.setterGet(id);

				for (int i = 1; i < keyParamPairs.size(); i++) {
					Keywords keyword = keyParamPairs.get(i).getKeyword();
					String param = keyParamPairs.get(i).getParam();

					switch (keyword) {
					case NAME:
						postEdit.setName(param);
						memory.updateMaps(id, param, preEdit.getName());
						memory.saveToFile();
						break;
					case START:
						if (param.equals("null")) {
							postEdit.setStartTime(null);
						} else if (!input.containDates()) {
							return new Signal(Signal.EDIT_INVALID_DATE, false);
						} else {
							postEdit.setStartTime(dateTimes.get(0));
							memory.updateMaps(id, dateTimes.get(0),
									preEdit.getStartTime());
							dateTimes.remove(0);
						}
						memory.saveToFile();
						break;
					case END:
						if (param.equals("null")) {
							postEdit.setEndTime(null);
						} else if (!input.containDates()) {
							return new Signal(Signal.EDIT_INVALID_DATE, false);
						} else {
							postEdit.setEndTime(dateTimes.get(0));
							memory.updateMaps(id, dateTimes.get(0),
									preEdit.getEndTime());
							dateTimes.remove(0);
						}
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
				if (!postEdit.isValid()) {
					try {
						memory.restoreHistoryState();
					} catch (StateUndefinedException e) {
						e.printStackTrace();
					}
					return new Signal(Signal.EDIT_END_BEFORE_START, false);
				}
				return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, preEdit,
						postEdit), true);
			}
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		} catch (NullRuleException e) {
			return new Signal(Signal.EDIT_NO_LONGER_RECURS, false);
		}
	}

}
