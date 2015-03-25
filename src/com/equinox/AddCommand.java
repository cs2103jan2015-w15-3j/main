package com.equinox;

/**
 * The AddCommand class handles all user commands with "add" as the first
 * keyword and processes ParsedInput to generate Todo objects and adds them into
 * memory. *
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

public class AddCommand extends Command {

	/**
	 * Creates an AddCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public AddCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * It takes in a ParsedInput object and generates a Todo object with respect
	 * to the ParsedInput object. The Todo object can be a floating task,
	 * deadline or event.
	 * 
	 * It returns a Signal object to indicate success or failure (if exception
	 * is thrown).
	 * 
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		// Check for empty string params
		if (input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		// Check for valid number of keywords
		int numberOfKeywords = keyParamPairs.size();
		if (numberOfKeywords > 3) {
            return new Signal(Signal.ADD_INVALID_PARAMS, false);
		}
		
		String todoName = keyParamPairs.get(0).getParam();

		int numberOfDates = dateTimes.size();

		switch (numberOfDates) {
		// Floating task
		// Example:
		// KeyParamPair 0: add <task>
			case 0:
				Todo floatingTask = new Todo(memory.obtainFreshId(), todoName);
				memory.add(floatingTask);
				memory.saveToFile();
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

			case 1: case 2:
				Todo timedTodo = new Todo(memory.obtainFreshId(), todoName,
					dateTimes);
				if (timedTodo.isValid()) {
					memory.add(timedTodo);
					memory.saveToFile();
					return new Signal(String.format(
						Signal.ADD_SUCCESS_SIGNAL_FORMAT, timedTodo), true);
				} else {
					memory.saveToFile();
					return new Signal(Signal.ADD_END_BEFORE_START_ERROR, false);
				}	
		}
		memory.saveToFile();
        return new Signal(Signal.ADD_UNKNOWN_ERROR, false);
	}
}
