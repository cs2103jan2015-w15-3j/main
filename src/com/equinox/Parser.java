package com.equinox;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;

public class Parser {

	private static final String STRING_EMPTY = "";
	private static final String REGEX_SPACE = "\\s";

	public static ParsedInput parseInput(String input) {
		ArrayList<String> wordList = tokenize(input);
		KEYWORDS cType = getCommandType(wordList);

		// if command type is error
		if (cType == null) {
			return new ParsedInput(null, null, null, null);
		}
		
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		ParsedInput returnInput;
		try {
			int lastDateKeywordIndex = findLastDateKeyword(wordList);
			String dateString = getDateString(lastDateKeywordIndex, wordList);
			dateTimeList = parseDates(dateString);
			removeDates(lastDateKeywordIndex, wordList);
		} catch (NoDateKeywordException e) {
			// Ignore as not all commands need dates
		} catch (DateUndefinedException e) {
			// Ignore as keywords identified without dates might be part of title
		} finally {
			ArrayList<KeyParamPair> pairArray = extractParam(wordList);
			returnInput = new ParsedInput(input, cType, pairArray, dateTimeList);
		}
		return returnInput;
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String array.
	 *
	 * @param input Input string from Zeitgeist class
	 * @return An ArrayList<String> where each element is a word from the
	 *         original string
	 */
	public static ArrayList<String> tokenize(String input) {
		input = input.trim();
		input = input.toLowerCase();
		String[] inputArray = input.split(REGEX_SPACE);
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < inputArray.length; i++) {
			wordList.add(inputArray[i]);
		}
		return wordList;
	}
	
	private static String getDateString(int lastDateKeywordIndex, ArrayList<String> wordList) {
		// Append 'on' keyword and following parameters at the end of the
		// ArrayList
		appendOnParamsAtEnd(wordList);
		StringBuilder dateString = new StringBuilder();
		
		// Build dateString
		for(int i = lastDateKeywordIndex; i < wordList.size(); i++) {
			String word = wordList.get(i);
			dateString.append(word + " ");
		}
		return dateString.toString().trim();
	}

	private static void removeDates(int lastDateKeywordIndex,
			ArrayList<String> wordList) {
		// Remove dateString from wordList
		for (int i = wordList.size() - 1; i >= lastDateKeywordIndex; i--) {
			wordList.remove(i);
		}
	}

	private static int findLastDateKeyword(ArrayList<String> wordList) throws NoDateKeywordException {
		LinkedList<Integer> onIndices = new LinkedList<Integer>();
		
		// Find index of from, at, by, on keywords whichever is earlier
		for(int i = wordList.size() - 1; i >= 0; i--) {
			String word = wordList.get(i);
			if(InputStringKeyword.isDateKeyword(word)) {
				KEYWORDS dateKeyword = InputStringKeyword.getDateKeyword(word);
				if(dateKeyword == KEYWORDS.ON) {
					onIndices.offer(i);
				} else {
					return i;
				}
			}
		}
		// If there are no instances of from, at or by, take the last on
		if(!onIndices.isEmpty()) {
			return onIndices.poll();
		}
		throw new NoDateKeywordException(ExceptionMessages.NO_DATE_KEYWORD_EXCEPTION);
	}

	/**
	 * Processes the inputArray to fill up an ArrayList with KeyParamPair
	 * objects. This method assumes that the first word in user input is a
	 * keyword.
	 * 
	 * @param wordList
	 *            An ArrayList<String> with the user input split into individual
	 *            words.
	 * @return An ArrayList<KeyParamPair> object with KeyParamPair objects
	 */
	public static ArrayList<KeyParamPair> extractParam(
			ArrayList<String> wordList) {
		String key = wordList.get(0);
		String tempParam = STRING_EMPTY;
		ArrayList<KeyParamPair> resultList = new ArrayList<KeyParamPair>();

		for (int i = 1; i < wordList.size(); i++) {
			String currentParam = wordList.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// param
			// and tempParam and add to ArrayList. Update key and tempParam.
			// If currentParam is a keyword:
			if (InputStringKeyword.isKeyword(currentParam)) {
				resultList.add(new KeyParamPair(key, tempParam));
				key = currentParam;
				tempParam = STRING_EMPTY;

				// wordList.get(i) is not a keyword; concat with tempParam.
			} else {
				tempParam = combineParamString(tempParam, currentParam); // TODO: Suggest using StringBuilder for simplicity
			}
		}
		// last KeyParamPair to be added to ArrayList
		resultList.add(new KeyParamPair(key, tempParam));
		return resultList;
	}

	private static void appendOnParamsAtEnd(ArrayList<String> wordList) {
		// Process the wordList to append [on <date>] to the end of the
		// ArrayList
		for (int i = 0; i < wordList.size(); i++) {
			String word = wordList.get(i);

			// Append to the end if 'on' appears and is not the last keyword
			if (InputStringKeyword.getKeyword(word) == KEYWORDS.ON
					&& !isLastKeyword(wordList, i + 1)) {

				do {
					String removed = wordList.remove(i);
					wordList.add(removed);
					word = wordList.get(i);
				} while (!InputStringKeyword.isKeyword(word));

				break;
			}
		}
	}

	private static boolean isLastKeyword(ArrayList<String> wordList, int index) {
		for (int i = index; i < wordList.size(); i++) {
			String word = wordList.get(i);
			if (InputStringKeyword.isKeyword(word)) {
				return false;
			}
		}
		return true;
	}

	public static String combineParamString(String tempParam,
			String currentParam) {
		if (tempParam.length() == 0) {
			return currentParam;
		} else {
			return tempParam.concat(" ".concat(currentParam));
		}
	}

	/**
	 * This operation gets the type of command of the user input assuming that
	 * the keyword of command type is input by the user as the first word.
	 * 
	 * @param wordList
	 * @return
	 */
	public static KEYWORDS getCommandType(ArrayList<String> wordList) {
		String typeString = wordList.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types. Returns the command type or type error if the command type is not
	 * listed.
	 * 
	 * @param typeString
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	private static KEYWORDS determineCommandType(String typeString) {
		KEYWORDS type = null;
		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}
	

	/**
	 * Parses a String with multiple dates provided to the DateParser, and returns a DateTime ArrayList.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates processed in the string.
	 * @throws DateUndefinedException if dateString does not contain a valid date, is empty, or null
	 */
	public static List<DateTime> parseDates(String dateString) throws DateUndefinedException {
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		DateGroup parsedDate;
		try {
			parsedDate = parser.parse(dateString).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new DateUndefinedException(ExceptionMessages.UNDEFINED_DATE_STRING_EXCEPTION);
		}
		List<Date> dateList = parsedDate.getDates();
		for (Date date : dateList) {
			dateTimeList.add(new DateTime(date));
		}
		return dateTimeList;
	}
}