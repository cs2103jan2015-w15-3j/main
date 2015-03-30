package com.equinox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.equinox.exceptions.InvalidDateException;
import com.equinox.exceptions.InvalidPeriodException;
import com.joestelmach.natty.DateGroup;

public class Parser {

	private static final String STRING_DAY = "day";
	private static final String STRING_WEEK = "week";
	private static final String STRING_MONTH = "month";
	private static final String STRING_YEAR = "year";
	private static final char CHAR_SPACE = ' ';
	private static final String REGEX_SPACE = "\\s";
	private static final String STRING_MON = "mon";
	private static final String STRING_TUE = "tue";
	private static final String STRING_WED = "wed";
	private static final String STRING_THU = "thu";
	private static final String STRING_FRI = "fri";
	private static final String STRING_SAT = "sat";
	private static final String STRING_SUN = "sun";
	private static final String STRING_MONDAY = "monday";
	private static final String STRING_TUESDAY = "tuesday";
	private static final String STRING_WEDNESDAY = "wednesday";
	private static final String STRING_THURSDAY = "thursday";
	private static final String STRING_FRIDAY = "friday";
	private static final String STRING_SATURDAY = "saturday";
	private static final String STRING_SUNDAY = "sunday";

	/**
	 * Parses the specified String, for the command type, keywords, dates and
	 * other parameters.
	 * 
	 * @param input
	 *            the String read from the user.
	 * @return a ParsedInput object containing the command type,
	 *         keyword-parameter pairs and dates identified.
	 */
	public static ParsedInput parseInput(String input) {
		boolean hasLimit = false;
		boolean isRecurring = false;
		Period period = new Period();
		ArrayList<String> words = tokenize(input);
		Keywords cType = getCommandType(words);
		ArrayList<Integer> dateIndexes = new ArrayList<Integer>();
		DateTime limit = new DateTime(0);

		// if command type is error
		if (cType == null) {
			return new ParsedInput(null, null, null, null, false, false, null);
		}

		List<DateTime> dateTimes = new ArrayList<DateTime>();
		ParsedInput returnInput;

		ArrayList<KeyParamPair> keyParamPairs = extractParam(words);
		if (cType == Keywords.ADD) {
			for (int i = 1; i < keyParamPairs.size(); i++) {
				// ignores the first pair as it is assumed to be the name of the
				// todo
				KeyParamPair currentPair = keyParamPairs.get(i);
				Keywords key = currentPair.getKeyword();

				// assumes that 'every _ until _' is at the end of user input
				if (isRecurring) { // check if there is a recurring limit
					if (key == Keywords.UNTIL) {
						try {
							limit = parseDates(currentPair.getParam()).get(0);
							hasLimit = true;
						} catch (InvalidDateException e) { // no valid date
															// given
							// appends every keyword + its param back to title
							String newName = appendParameters(keyParamPairs, 0,
									i);
							keyParamPairs.get(0).setParam(newName);
						}
					}
				} else if (key == Keywords.EVERY) {
					// tries to detect if there is a period in user input
					try {
						// tries to parse param as date to extract the date
						List<DateTime> parsedDate = parseDates(currentPair
								.getParam());
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
					} catch (InvalidDateException e) {
						// if parameters cannot be parsed as dates, just ignores
					}
					try {
						// tries to parse as period
						period = retrieveRecurringPeriod(currentPair.getParam()
								.toLowerCase());
						isRecurring = true;
					} catch (InvalidPeriodException e) { // no valid period
															// given
						// appends every keyword + its param back to title
						String newName = appendParameters(keyParamPairs, i, 0);
						keyParamPairs.get(0).setParam(newName);
					}
				} else {
					// tries to parse param as date
					try {
						List<DateTime> parsedDate = parseDates(currentPair
								.getParam());
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
					} catch (InvalidDateException e) { // no valid date given
						// appends every keyword + its param back to title
						String newName = appendParameters(keyParamPairs, 0, i);
						keyParamPairs.get(0).setParam(newName);
					}
				}
			}
			for(int i = keyParamPairs.size() - 1; i > 0; i--) {
				keyParamPairs.remove(i);
			}
		}

		// Post-process EDIT command parameters
		if (cType == Keywords.EDIT) {
			for (KeyParamPair keyParamPair : keyParamPairs) {
				Keywords key = keyParamPair.getKeyword();
				if (key == Keywords.START || key == Keywords.END) {
					try {
						DateTime parsedDate = parseDates(
								keyParamPair.getParam()).get(0);
						dateTimes.add(parsedDate);
					} catch (InvalidDateException e) {
						// Ignore empty date list will be returned
					}
				}
			}
		}

		// Post-process SEARCH command parameters
		if (cType == Keywords.SEARCH) {
			for (KeyParamPair keyParamPair : keyParamPairs) {
				Keywords key = keyParamPair.getKeyword();
				if (!(key == Keywords.NAME || key == Keywords.SEARCH)) {
					String dateParam = keyParamPair.getParam();
					if (key == Keywords.YEAR) {
						dateParam = "march ".concat(keyParamPair.getParam());
					}
					try {
						DateTime parsedDate = parseDates(dateParam).get(0);
						dateTimes.add(parsedDate);
					} catch (InvalidDateException e) {
						// Ignore empty date list will be returned
					}
				}
			}
		}

		if (isRecurring) {
			if (!isValidRecurring(dateTimes)) {
				isRecurring = false;

				for (int i = 1; i < keyParamPairs.size(); i++) {
					if (keyParamPairs.get(i).getKeyword() == Keywords.EVERY) {
						String newName = appendParameters(keyParamPairs, 0, i);
						keyParamPairs.get(0).setParam(newName);
					} else if (keyParamPairs.get(i).getKeyword() == Keywords.UNTIL) {
						String newName = appendParameters(keyParamPairs, 0, i);
						keyParamPairs.get(0).setParam(newName);
					}

				}

			} else {
				if (hasLimit) {
					dateTimes.add(limit);
				}
			}
		}
		returnInput = new ParsedInput(cType, keyParamPairs, dateTimes, period,
				isRecurring, hasLimit, limit);
		return returnInput;
	}

	/**
	 * Check if given dateTimes has enough elements for todo to be a valid
	 * recurring todo
	 * 
	 * @param dateTimes
	 * @return isValidRecurring
	 */
	private static boolean isValidRecurring(List<DateTime> dateTimes) {
		return dateTimes.size() > 0;
	}

	/**
	 * Adds parsedDate to dateTimes depending on number of elements in
	 * dateTimes. If dateTimes already contains some elements, method tries to
	 * combine the parameters and re-parse them as dates to check if resulting
	 * dateTime is different
	 * 
	 * @param parsedDate
	 * @param dateTimes
	 * @param keyParamPairs
	 * @param dateIndexes
	 * @param currentIndex
	 */
	private static void addToDateTimes(List<DateTime> parsedDate,
			List<DateTime> dateTimes, ArrayList<KeyParamPair> keyParamPairs,
			ArrayList<Integer> dateIndexes, int currentIndex) {

		if (dateTimes.size() > 0) {
			int appendedPairIndex = dateIndexes.get(0);
			List<DateTime> newDateTimes = new ArrayList<DateTime>();

			assert (dateTimes.size() < 3); // There should be at the most 2
											// dates only
			assert (dateIndexes.size() == 1); // There should be at the most 1
												// date param

			String newDateParam = appendParameters(keyParamPairs,
					appendedPairIndex, currentIndex);
			try {
				newDateTimes = parseDates(newDateParam);

			} catch (InvalidDateException e) {
				// should never enter this catch block as old date parameters
				// were parse-able
				e.printStackTrace(); // TODO: handle this exception
			}
			System.out.println(newDateTimes.get(0).toString());
			System.out.println(dateTimes.get(0).toString());
			System.out.println(newDateTimes.equals(dateTimes));
			if (!newDateTimes.isEmpty() && newDateTimes.equals(dateTimes)) {

				// natty could not parse in the first order, try appending the
				// other way
				appendedPairIndex = currentIndex;
				currentIndex = dateIndexes.get(0);
				newDateParam = appendParameters(keyParamPairs,
						appendedPairIndex, currentIndex);
				try {
					newDateTimes = parseDates(newDateParam);

				} catch (InvalidDateException e) {
					// should never enter this catch block as old date
					// parameters were parse-able
					e.printStackTrace(); // TODO: handle this exception
				}
			}
			// dateTime generated were different
			assert (!newDateTimes.isEmpty()); // shouldn't be empty because
												// parameters should be
												// parse-able
			dateTimes.clear(); // removes all elements in dateTimes
			dateTimes.addAll(newDateTimes);
			keyParamPairs.get(appendedPairIndex).setParam(newDateParam);
			dateIndexes.remove(0);
			dateIndexes.add(appendedPairIndex);
		} else {
			dateTimes.addAll(parsedDate);
			dateIndexes.add(currentIndex);
		}
	}

	/**
	 * Appends the keyword and parameter of the second keyParamPair to the
	 * parameters of the first keyParamPair.
	 * 
	 * @param keyParamPairs
	 * @param indexOfFirstPair
	 * @param indexOfSecondPair
	 * @return appended string
	 */
	private static String appendParameters(
			ArrayList<KeyParamPair> keyParamPairs, int indexOfFirstPair,
			int indexOfSecondPair) {
		KeyParamPair firstPair = keyParamPairs.get(indexOfFirstPair);
		KeyParamPair secondPair = keyParamPairs.get(indexOfSecondPair);
		Keywords key = secondPair.getKeyword();

		StringBuilder sBuilder = new StringBuilder(firstPair.getParam());
		sBuilder.append(CHAR_SPACE);
		sBuilder.append(key.toString().toLowerCase());
		sBuilder.append(CHAR_SPACE);
		sBuilder.append(secondPair.getParam());
		return sBuilder.toString();
	}

	/**
	 * Retrieves period given string
	 * 
	 * @param param
	 * @return period for recurrence
	 * @throws InvalidPeriodException
	 */
	private static Period retrieveRecurringPeriod(String param)
			throws InvalidPeriodException {
		param.toLowerCase();
		Period period = new Period();
		switch (param) {
			case STRING_YEAR:
				return period.withYears(1);
			case STRING_MONTH:
				return period.withMonths(1);
			case STRING_WEEK:
				return period.withWeeks(1);
			case STRING_DAY:
				return period.withDays(1);
			case STRING_MON:
				return period.withWeeks(1);
			case STRING_TUE:
				return period.withWeeks(1);
			case STRING_WED:
				return period.withWeeks(1);
			case STRING_THU:
				return period.withWeeks(1);
			case STRING_FRI:
				return period.withWeeks(1);
			case STRING_SAT:
				return period.withWeeks(1);
			case STRING_SUN:
				return period.withWeeks(1);
			case STRING_MONDAY:
				return period.withWeeks(1);
			case STRING_TUESDAY:
				return period.withWeeks(1);
			case STRING_WEDNESDAY:
				return period.withWeeks(1);
			case STRING_THURSDAY:
				return period.withWeeks(1);
			case STRING_FRIDAY:
				return period.withWeeks(1);
			case STRING_SATURDAY:
				return period.withWeeks(1);
			case STRING_SUNDAY:
				return period.withWeeks(1);
			default:
				throw new InvalidPeriodException();
		}
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String ArrayList.
	 *
	 * @param input
	 *            the String read from the user.
	 * @return an ArrayList of words from the input String.
	 */
	private static ArrayList<String> tokenize(String input) {
		input = input.trim();
		String[] inputArray = input.split(REGEX_SPACE);
		ArrayList<String> words = new ArrayList<String>();
		for (int i = 0; i < inputArray.length; i++) {
			words.add(inputArray[i]);
		}
		return words;
	}

	/**
	 * Parses the tokenized input, wordList for keywords and their associated
	 * parameters, stores them in KeyParamPair objects and adds all KeyParamPair
	 * objects to an ArrayList which is returned. ASSUMPTION: The first word in
	 * user input is a keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static ArrayList<KeyParamPair> extractParam(ArrayList<String> words) {
		String key = words.get(0);
		ArrayList<KeyParamPair> results = new ArrayList<KeyParamPair>();
		Keywords keyword;
		EnumSet<Keywords> keywordOccurrence = EnumSet.noneOf(Keywords.class);
		StringBuilder paramBuilder = new StringBuilder();
		for (int i = 1; i < words.size(); i++) {
			String currentParam = words.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// param
			// and paramStringBuilder and add to ArrayList. Update key and
			// paramBuilder.
			// If currentParam is a keyword:
			if (InputStringKeyword.isKeyword(currentParam)) {
				keyword = InputStringKeyword.getKeyword(key);
				// Ignore and append keyword if it has occurred before
				if (!keywordOccurrence.contains(keyword)) {
					keywordOccurrence.add(keyword);
					results.add(new KeyParamPair(keyword, paramBuilder
							.toString()));
					key = currentParam;
					paramBuilder = new StringBuilder();
				} else { // wordList.get(i) is a repeated keyword; append to
							// paramString
					buildParam(paramBuilder, currentParam);

				}
			} else { // wordList.get(i) is not a keyword; append to paramString
				buildParam(paramBuilder, currentParam);
			}
		}
		// last KeyParamPair to be added to ArrayList
		keyword = InputStringKeyword.getKeyword(key);
		results.add(new KeyParamPair(keyword, paramBuilder.toString()));
		return results;
	}

	private static void buildParam(StringBuilder paramBuilder,
			String currentParam) {
		if (paramBuilder.length() != 0) {
			paramBuilder.append(CHAR_SPACE);
		}
		paramBuilder.append(currentParam);
	}

	/**
	 * This operation gets the type of command of the user input.
	 * 
	 * ASSUMPTION: the first word in user input is the command type keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static Keywords getCommandType(ArrayList<String> words) {
		String typeString = words.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types.
	 * 
	 * @param typeString
	 *            String specifying the type of command.
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	private static Keywords determineCommandType(String typeString) {
		Keywords type = null; // TODO: Suggest throwing exception instead of
								// returning null.
		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and
	 * returns a DateTime ArrayList.
	 * 
	 * @param dateString
	 *            String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates
	 *         processed in the string.
	 * @throws InvalidDateException
	 *             if dateString does not contain a valid date, is empty, or
	 *             null
	 */
	public static List<DateTime> parseDates(String dateString)
			throws InvalidDateException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		DateGroup parsedDate;
		DateGroup dateNow;
		try {
			parsedDate = parser.parse(dateString).get(0);
			dateNow = parser.parse(dateString).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidDateException(
					ExceptionMessages.DATE_UNDEFINED_EXCEPTION);
		}

		List<Date> dateList = parsedDate.getDates();
		List<Date> nowList = dateNow.getDates();
		for (int i = 0; i < dateList.size(); i++) {
			Date date = dateList.get(i);
			DateTime dateTime = new DateTime(date);
			if (!date.equals(nowList.get(i))) {
				dateTime = dateTime.withTime(23, 59, 0, 0);
			} else {
				dateTime = dateTime.withSecondOfMinute(0).withMillisOfSecond(0);
			}
			dateTimes.add(dateTime);
		}
		return dateTimes;
	}
}
