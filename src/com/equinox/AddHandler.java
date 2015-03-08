package com.equinox;

/**
 * The AddHandler class handles all user commands with "add" as the first keyword and processes
 * ParsedInput to generate Todo objects and adds them into memory. * 
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class AddHandler {

	/**
	 * It takes in a ParsedInput object and generates a Todo object with respect
	 * to the ParsedInput object. The Todo object can be a floating task,
	 * deadline or event.
	 * 
	 * It returns a Signal object to indicate success or failure (if exception
	 * is thrown).
	 * 
	 * @param input
	 *            A ParsedInput object that contains a KEYWORDS type and an
	 *            ArrayList<KeyParamPair>
	 * @param memory
	 *            A Memory object that stores Todo objects
	 * @return It returns a Signal object to indicate success or failure.
	 */
	public static Signal process(ParsedInput input, Memory memory) {
		// Check for valid number of keywords
		int numberOfKeywords = input.getParamPairList().size();
		if (numberOfKeywords > 3) {
            return new Signal(Signal.INVALID_PARAMS_FOR_ADD_HANDLER, false);
		}
		
		ArrayList<KeyParamPair> keyParamPairList = input.getParamPairList();
		String todoName = keyParamPairList.get(0).getParam();
		
		// Check for empty string params
		if(isListParamEmptyString(keyParamPairList)){
            return new Signal(Signal.EMPTY_PARAM_EXCEPTION, false);
		}
		
		try {
			
			int numberOfElements = keyParamPairList.size();

			switch (numberOfElements) {
			// Floating task
			// Example:
			// KeyParamPair 0: add <task>
				case 1:
					Todo floatingTask = new Todo(todoName);
					memory.add(floatingTask);
                    return new Signal(String.format(
                            Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTask),
                            true);

					// Deadline
					// Example:
					// KeyParamPair 0: add <task>, KeyParamPair 1: by <date>

					// Event
					// Example:
					// KeyParamPair 0: add <event>, KeyParamPair 1: from
					// <startDate to endDate>

				case 2:
					String secondKeyword = keyParamPairList.get(1).getKeyword();

					// Deadline
					if (secondKeyword.equals("by") || secondKeyword.equals("on")
							|| secondKeyword.equals("at")) {
						DateTime deadlineTime = DateParser.parseDate(keyParamPairList.get(1)
								.getParam());
						Todo deadline = new Todo(todoName, deadlineTime);
						memory.add(deadline);
                        return new Signal(String.format(
                                Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadline),
                                true);
					}

					// Event
					else if (secondKeyword.equals("from")) {
						List<DateTime> dateTimeList = DateParser.parseDates(keyParamPairList.get(1)
								.getParam());
						DateTime eventStartTime = dateTimeList.get(0);
						DateTime eventEndTime = dateTimeList.get(1);
						Todo event = new Todo(todoName, eventStartTime, eventEndTime);
						memory.add(event);
                        return new Signal(String.format(
                                Signal.ADD_SUCCESS_SIGNAL_FORMAT, event), true);
					}
			}

		} catch (DateUndefinedException e) {
			e.printStackTrace();
			String exceptionMessage = e.getMessage();
            return new Signal(String.format(Signal.DATE_UNDEFINED_EXCEPTION,
                    exceptionMessage), true);
		}

        return new Signal(Signal.UNKNOWN_ADD_ERROR, false);
	}
	/**
	 * Iterates through the keyParamPair ArrayList and checks if any parameter is an empty string.
	 * 
	 * @param keyParamPairList 
	 * @return boolean If there is at least one empty string parameter, return true. Else, return false.
	 */
	private static boolean isListParamEmptyString(ArrayList<KeyParamPair> keyParamPairList) {
		for(KeyParamPair pair : keyParamPairList){
			if(pair.isParamEmptyString()){
				return true;
			}
		}
		return false;
	}
}
