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
			int id;
			boolean containsNewName = false;
			String title = new String(); // Stub initialization
			// Check if first param has any text appended to it intended as Todo name
			if(keyParamPairs.get(0).getParam().length() > 1) {
				String[] combinedParam = keyParamPairs.get(0).getParam().split("\\s", 2);
				
				// Try to parse id as int. If fail send invalidParams Signal.
				id = Integer.parseInt(combinedParam[0].trim());
				title = combinedParam[1];
				containsNewName = true;
			} else {
				// Check if input contains more than 1 keyword (keyParamPairs.size() > 1)
				if (input.containsOnlyCommand()) {
					return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
				// Check if input is missing parameters
				if (input.containsEmptyParams()) {
					return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
				}
				id = Integer.parseInt(keyParamPairs.get(0).getParam());
			}
			Todo todo = memory.get(id);

			if (input.isRecurring()) {
				int numberOfKeywords = keyParamPairs.size() + dateTimes.size();
				// Check for valid number of keywords
				if (numberOfKeywords > 8) {
					return new Signal(Signal.ADD_INVALID_PARAMS, false);
				}

				RecurringTodoRule rule = memory.getRule(todo.getRecurringId());
				RecurringTodoRule ruleOld = new RecurringTodoRule(rule);
				
				// If input contains new title
				if(containsNewName) {
					rule.setOriginalName(title);
				}
				
				// If recurrence rule has a limit
				if (input.hasLimit()) {
					rule.setRecurrenceLimit(input.getLimit());
				}
				
				if (input.hasPeriod()) {
					rule.setRecurringInterval(input.getPeriod());
					rule.setDateTimes(dateTimes);
				}
				
				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, ruleOld, rule), true);	
			} else {
				// TODO
				
				return null;
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
