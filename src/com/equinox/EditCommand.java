//@author A0094679H

package com.equinox;

import com.equinox.exceptions.NotRecurringException;
import com.equinox.exceptions.NullRuleException;
import com.equinox.exceptions.NullTodoException;
import com.equinox.exceptions.StateUndefinedException;

/**
 * Houses a method which processes the edit request from the user.
 * 
 * @author Ikarus
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
			int id;
			boolean containsNewName = false;
			boolean isRule = false;
			String title = new String(); // Stub initialization
			
			// Check if first param has any text appended to it intended as Todo name
			String[] firstKeywordParams = keyParamPairs.get(0).getParam().trim().split("\\s", 2);
			if (firstKeywordParams.length > 1) {
				// Try to parse first sub-param as int. If fail send invalidParams Signal.
				id = Integer.parseInt(firstKeywordParams[0].trim());
				title = firstKeywordParams[1];
				containsNewName = true;
			} else {
				// Check if input contains only 1 keyword (keyParamPairs.size() == 1)
				if (input.containsOnlyCommand()) {
					return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
				// Check if input is missing parameters
				if (input.containsEmptyParams()) {
					return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
				}
				id = Integer.parseInt(keyParamPairs.get(0).getParam());
			}

			// Check for presence of -r flag
			isRule = input.containsFlag(Keywords.RULE);
			
			if (input.isRecurring() || isRule) {
				RecurringTodoRule rule = memory.getToModifyRule(memory.getTodo(id).getRecurringId());
				RecurringTodoRule ruleOld = rule.copy();
				
				// If input contains new title
				if(containsNewName) {
					rule.setOriginalName(title);
				}
				
				// If recurrence rule has a limit
				if (input.hasLimit()) {
					rule.setRecurrenceLimit(input.getLimit());
				}
				
				if (input.hasPeriod()) {
					if(dateTimes.isEmpty()) {	// TODO: Catch dateTimes empty. Undo
						return new Signal(Signal.EDIT_NO_RECURRING_TIME, false);
					} else {
						rule.setRecurringInterval(input.getPeriod());
						rule.setDateTimes(dateTimes);
					}
				}
				
				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, ruleOld, rule), true);	
			} else {
				
				Todo todo = memory.getToModifyTodo(id);
				Todo oldTodo = todo.copy();
				
				// If input contains new title
				if(containsNewName) {
					memory.updateMaps(id, title, todo.getName());
					todo.setName(title);
				}
				if(dateTimes.size() == 2) {
					memory.updateMaps(id, dateTimes.get(0), todo.getStartTime());
					memory.updateMaps(id, dateTimes.get(1), todo.getEndTime());
					todo.setStartTime(dateTimes.get(0));
					todo.setEndTime(dateTimes.get(1));
				} else if(dateTimes.size() == 1) {
					for(int i = 1; i < keyParamPairs.size(); i++) {
						Keywords keyword = keyParamPairs.get(i).getKeyword();
						
						switch (keyword) {
						case FROM:
							memory.updateMaps(id, dateTimes.get(0), todo.getStartTime());
							todo.setStartTime(dateTimes.get(0));
							break;
						case BY:
						case ON:
						case AT:
							memory.updateMaps(id, dateTimes.get(0), todo.getEndTime());
							memory.updateMaps(id, null, todo.getStartTime());
							todo.setEndTime(dateTimes.get(0));
							todo.setStartTime(null);
							break;
						case TO:
							memory.updateMaps(id, dateTimes.get(0), todo.getEndTime());
							todo.setEndTime(dateTimes.get(0));
							break;
						default:
							return new Signal(Signal.EDIT_INVALID_PARAMS, false);
						}
					}
				}
				if(!todo.isValid()) {
					try {
						memory.undo();
					} catch (StateUndefinedException e) {
						e.printStackTrace();
					}
					return new Signal(Signal.EDIT_END_BEFORE_START, false);
				}
				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, oldTodo, todo), true);
			}
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		} catch (NullRuleException e) {
			return new Signal(Signal.EDIT_NO_LONGER_RECURS, false);
		} catch (NotRecurringException e) {
			return new Signal(e.getMessage(), false);
		}
	}

}
