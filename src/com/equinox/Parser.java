package com.equinox;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.equinox.exceptions.DateUndefinedException;
import com.equinox.exceptions.NoDateKeywordException;
import com.joestelmach.natty.DateGroup;

public class Parser {

	private static final String STRING_EMPTY = "";
	private static final String REGEX_SPACE = "\\s";

	/**
	 * Parses the specified String, for the command type, keywords, dates and
	 * other parameters.
	 * 
	 * @param input the String read from the user.
	 * @return a ParsedInput object containing the command type,
	 *         keyword-parameter pairs and dates identified.
	 */
	public static ParsedInput parseInput(String input) {
		ArrayList<String> wordList = tokenize(input);
		KEYWORDS cType = getCommandType(wordList);

		// if command type is error
		if (cType == null) {
			return new ParsedInput(null, null, null, null);
		}
		
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		ParsedInput returnInput;
		// Pre-process ADD command parameters for date
		if(cType == KEYWORDS.ADD) {
			try {
				int lastDateKeywordIndex = findLastDateKeyword(wordList);
				String dateString = getDateString(lastDateKeywordIndex,
						wordList);
				dateTimeList = parseDates(dateString);
				removeDates(lastDateKeywordIndex, wordList);
			} catch (NoDateKeywordException e) {
				// Ignore empty date list will be returned
			} catch (DateUndefinedException e) {
				// Ignore as keywords identified without dates might be part of
				// title, empty date list will still be returned
			} 
		}
		ArrayList<KeyParamPair> pairArray = extractParam(wordList);
		
		// Post-process EDIT command parameters
		if(cType == KEYWORDS.EDIT) {
			for(KeyParamPair keyparam : pairArray) {
				KEYWORDS key = keyparam.getKeyword();
				if(key == KEYWORDS.START || key == KEYWORDS.END) {
					try {
						DateTime parsedDate = parseDates(keyparam.getParam()).get(0);
						dateTimeList.add(parsedDate);
					} catch (DateUndefinedException e) {
						// Ignore for now
					}
				}
			}
		} 
		
		returnInput = new ParsedInput(input, cType, pairArray, dateTimeList);	
		return returnInput;
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String ArrayList.
	 *
	 * @param input the String read from the user.
	 * @return an ArrayList of words from the input String.
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
	
	/**
	 * Retrieves the String representing the date(s) entered by the user using
	 * the index of the first occurrence of the date keyword.
	 * 
	 * @param lastDateKeywordIndex the index of the first occurrence of the date keyword.
	 * @param wordList the ArrayList of words from the input String.
	 * @return the String containing only the dates specified by the user.
	 */
	private static String getDateString(int lastDateKeywordIndex, ArrayList<String> wordList) {
		// Append 'on' keyword and following parameters at the end of the
		// ArrayList
		// appendOnParamsAtEnd(wordList); 
		// TODO: No longer works. Suggest removal. 
		StringBuilder dateString = new StringBuilder();
		
		// Build dateString
		for(int i = lastDateKeywordIndex; i < wordList.size(); i++) {
			String word = wordList.get(i);
			dateString.append(word + " ");
		}
		return dateString.toString().trim();
	}

	/**
	 * Remove the 
	 * 
	 * @param lastDateKeywordIndex
	 * @param wordList
	 */
	private static void removeDates(int lastDateKeywordIndex,
			ArrayList<String> wordList) {
		// Remove dateString from wordList
		for (int i = wordList.size() - 1; i >= lastDateKeywordIndex; i--) {
			wordList.remove(i);
		}
	}

	/**
	 * Locates the last date keyword in the tokenized input stored in wordList
	 * by iterating through the entire ArrayList once. This method selects
	 * "from" and "by" preferentially over "on" and at" which are ignored if the
	 * former are present. In the event the former are absent, the latter is
	 * chosen, whichever comes later to ensure that part of the title is not
	 * mistakenly parsed as date.
	 * 
	 * @param wordList the ArrayList of words from the input String.
	 * @return the index of the first date keyword.
	 * @throws NoDateKeywordException if no date keywords are found in the
	 *             tokenized input.
	 */
	private static int findLastDateKeyword(ArrayList<String> wordList) throws NoDateKeywordException {
		LinkedList<Integer> onIndices = new LinkedList<Integer>();
		LinkedList<Integer> atIndices = new LinkedList<Integer>();
		
		// Find index of from, at, by, on keywords whichever is earlier
		for(int i = wordList.size() - 1; i >= 0; i--) {
			String word = wordList.get(i);
			if(InputStringKeyword.isAddKeyword(word)) {
				KEYWORDS dateKeyword = InputStringKeyword.getAddKeyword(word);
				if(dateKeyword == KEYWORDS.ON) {
					onIndices.offer(i);
				} else if(dateKeyword == KEYWORDS.AT) {
					atIndices.offer(i);
				} else {
					return i;
				}
			}
		}
		// FLAW: "add look for max at the park on 9 March"
		// FIXED: Look for the later keyword in absence of from or by
		// If there are no instances of from, at or by, take the last on or last at whichever is earlier.
		if(!onIndices.isEmpty() && !atIndices.isEmpty()) {
			return Math.max(onIndices.poll(), atIndices.poll());
		} else if (!onIndices.isEmpty()) {
			return onIndices.poll();
		} else if (!atIndices.isEmpty()){
			return atIndices.poll();
		}
		
		throw new NoDateKeywordException(ExceptionMessages.NO_DATE_KEYWORD_EXCEPTION);
	}

	/**
	 * Parses the tokenized input, wordList for keywords and their associated
	 * parameters, stores them in KeyParamPair objects and adds all KeyParamPair
	 * objects to an ArrayList which is returned. 
	 * ASSUMPTION: The first word in user input is a keyword.
	 * 
	 * @param wordList the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	public static ArrayList<KeyParamPair> extractParam(
			ArrayList<String> wordList) {
		String key = wordList.get(0);
		String tempParam = STRING_EMPTY;
		ArrayList<KeyParamPair> resultList = new ArrayList<KeyParamPair>();
		KEYWORDS keyword;
		EnumSet<KEYWORDS> keywordOccurrence = EnumSet.of(InputStringKeyword.getCommand(key));

		for (int i = 1; i < wordList.size(); i++) {
			String currentParam = wordList.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// param
			// and tempParam and add to ArrayList. Update key and tempParam.
			// If currentParam is a keyword:
			if (InputStringKeyword.isKeyword(currentParam)) {
				keyword = InputStringKeyword.getKeyword(key);
				// Ignore and append keyword if it has occurred before
				if(!keywordOccurrence.contains(keyword)){
					keywordOccurrence.add(keyword);
					resultList.add(new KeyParamPair(keyword, tempParam));
					key = currentParam;
					tempParam = STRING_EMPTY;
				} else { // wordList.get(i) is a repeated keyword; concat with tempParam.
					tempParam = combineParamString(tempParam, currentParam); // TODO: Suggest using StringBuilder for simplicity
				}
			} else { // wordList.get(i) is not a keyword; concat with tempParam.
				tempParam = combineParamString(tempParam, currentParam); // TODO: Suggest using StringBuilder for simplicity
			}
		}
		// last KeyParamPair to be added to ArrayList
		keyword = InputStringKeyword.getKeyword(key);
		resultList.add(new KeyParamPair(keyword, tempParam));
		return resultList;
	}
/*
	private static void appendOnParamsAtEnd(ArrayList<String> wordList) {
		// Process the wordList to append [on <date>] to the end of the
		// ArrayList
		for (int i = 0; i < wordList.size(); i++) {
			String word = wordList.get(i);

			// Append to the end if 'on' appears and is not the last keyword
			if (InputStringKeyword.getAddKeyword(word) == KEYWORDS.ON
					&& !isLastKeyword(wordList, i + 1)) {

				do {
					String removed = wordList.remove(i);
					wordList.add(removed);
					word = wordList.get(i);
				} while (!InputStringKeyword.isAddKeyword(word));

				break;
			}
		}
	}

	private static boolean isLastKeyword(ArrayList<String> wordList, int index) {
		for (int i = index; i < wordList.size(); i++) {
			String word = wordList.get(i);
			if (InputStringKeyword.isAddKeyword(word)) {
				return false;
			}
		}
		return true;
	}
*/
	public static String combineParamString(String tempParam,
			String currentParam) {
		if (tempParam.length() == 0) {
			return currentParam;
		} else {
			return tempParam.concat(" ".concat(currentParam));
		}
	}

	/**
	 * This operation gets the type of command of the user input.
	 * 
	 * ASSUMPTION: the first word in user input is the command type keyword.
	 * 
	 * @param wordList the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	public static KEYWORDS getCommandType(ArrayList<String> wordList) {
		String typeString = wordList.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types. 
	 * 
	 * @param typeString String specifying the type of command.
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	private static KEYWORDS determineCommandType(String typeString) {
		KEYWORDS type = null; // TODO: Suggest throwing exception instead of returning null.
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
