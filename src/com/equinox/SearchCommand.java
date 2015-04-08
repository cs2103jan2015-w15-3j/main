package com.equinox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.equinox.exceptions.InvalidDateException;
import com.equinox.exceptions.InvalidParamException;
import com.equinox.exceptions.NullTodoException;

/**
 * The SearchCommand class handles user input with search commands.
 * 
 * @author peanut11
 *
 */
public class SearchCommand extends Command {
	//@author A0115983X
	
	private static final String REGEX_SPACE = "\\s";

	/**
	 * Creates a SearchCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public SearchCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
	public Signal execute() {
		ArrayList<KeyParamPair> inputList = input.getParamPairs();
		Set<Integer> resultSet = new HashSet<Integer>();
		KeyParamPair pair;
		Keywords typeKey;
		String param;
		List<DateTime> dateTimes = input.getDateTimes();

		// Iterates through every KeyParamPair
		for (int i = 0; i < inputList.size(); i++) {
			pair = inputList.get(i);
			typeKey = pair.getKeyword();
			param = pair.getParam();

			// assumes that if no flag input, assume that user is searching in
			// Todo name
			if (i == 0) {
				typeKey = Keywords.NAME;
			}

			// checks if param behind a flag is an empty string
			if (i != 0 && param.isEmpty()) {
				return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
			}

			try {
				searchIndex(resultSet, typeKey, param, dateTimes);
			} catch (InvalidDateException e) {
				return new Signal(Signal.SEARCH_INVALID_PARAMS, false);
			} catch (InvalidParamException e) {
				return new Signal(Signal.SEARCH_INVALID_PARAMS, false);
			}

		}

		// checks if resultSet is empty
		if (resultSet.isEmpty()) {
			return new Signal(Signal.SEARCH_EMPTY_SIGNAL, false);
		}

		// displays the list of todos that were found
		ArrayList<Todo> todos = getTodos(resultSet);
		String displayString = DisplayCommand.getDisplayChrono(todos, 2);
		System.out.println(displayString);

		return new Signal(Signal.SEARCH_SUCCESS_SIGNAL, true);
	}

	/**
	 * Operation generates Collection of Todo Objects based on their ids
	 * 
	 * @param resultSet
	 * @return Collection of Todo objects
	 */
	private ArrayList<Todo> getTodos(Set<Integer> resultSet) {
		ArrayList<Todo> todos = new ArrayList<Todo>();
		Todo current = null;

		for (int x : resultSet) {
			try {
				current = memory.get(x);
			} catch (NullTodoException e) {

			}
			if (current != null) {
				todos.add(current);
			}
		}
		return todos;
	}

	/**
	 * Search memory for each string in param array based on the typeKey and
	 * updates the resultSet with the ids of the Todos found
	 * 
	 * @param resultSet
	 * @param key
	 * @param paramArray
	 * @throws InvalidDateException
	 * @throws InvalidParamException 
	 */
	private void searchIndex(Set<Integer> resultSet, Keywords typeKey,
			String param, List<DateTime> dateTimes) throws InvalidDateException, InvalidParamException {
		ArrayList<Integer> tempResult;
		
		if(typeKey == Keywords.NAME) {
			String[] paramArray = param.split(REGEX_SPACE);
			for (String searchKey : paramArray) {
				tempResult = memory.search(typeKey, searchKey);
				addToSet(tempResult, resultSet);
			}
		} else { //assumes if typeKey != NAME, user wants to search for dateTime
			assert (dateTimes.size() == 1);
			tempResult = memory.search(typeKey, dateTimes.remove(0));
			addToSet(tempResult, resultSet);
		}
		
	}

	/**
	 * Operation inserts integer from tempResult into the resultSet
	 * 
	 * @param tempResult
	 * @param resultSet
	 */
	private void addToSet(ArrayList<Integer> tempResult, Set<Integer> resultSet) {
		int current;
		for (int i = 0; i < tempResult.size(); i++) {
			current = tempResult.get(i);
			resultSet.add(current);
		}
	}

	public static void main(String[] args) {
//		Zeitgeist.handleInput("add floating task");

//		Zeitgeist.handleInput("add CS3230 deadline on 9 March 9pm");

//		Zeitgeist
//				.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

//		Zeitgeist
//				.handleInput("add new year from 1 January at 10am to 1 January at 11am");

//		Zeitgeist.handleInput("add CS1010 deadline by 3 Feb at 10pm");

//		Zeitgeist.handleInput("add read floating books");

//		Zeitgeist.handleInput("add CS3243 project deadline by 7 March at 9am");

//		Zeitgeist.handleInput("add CS3333 project 2 on 7 Apr 10am");

//		Zeitgeist.handleInput("display");
		
//		Zeitgeist.handleInput("mark 0");

//		Zeitgeist.handleInput("mark 2");

//		Zeitgeist.handleInput("display");
//		Zeitgeist.handleInput("search -n floating deadline");

//		Zeitgeist.handleInput("search deadline");

//		Zeitgeist.handleInput("search -dt 3 march -dt 1/1");
		
//		Zeitgeist.handleInput("search -t 10am");
		
//		Zeitgeist.handleInput("search -m march");
		
//		Zeitgeist.handleInput("search -d thu");

	}
}
