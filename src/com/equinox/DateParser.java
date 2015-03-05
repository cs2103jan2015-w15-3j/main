package com.equinox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Houses methods which parse a String containing dates and time into their
 * corresponding DateTime objects.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class DateParser {

	/**
	 * Parses a single date String provided to the DateParser.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return The immutable DateTime object representing the first date encountered in the string.
	 * @throws DateUndefinedException if dateString does not contain a valid date, is empty, or null
	 */
	public static DateTime parseDate(String dateString) throws DateUndefinedException {
		DateTime returnDateTime = null;
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			DateGroup parsedDate = parser.parse(dateString).get(0);
			List<Date> dateList = parsedDate.getDates();
			Date firstDate = dateList.get(0);
			returnDateTime = new DateTime(firstDate);
		} catch (IndexOutOfBoundsException e) {
			throw new DateUndefinedException(Signal.UNDEFINED_DATE_STRING_EXCEPTION);
		} catch (NullPointerException e) {
			throw new DateUndefinedException(Signal.NULL_DATE_STRING_EXCEPTION);
		}
		return returnDateTime;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and returns a DateTime array.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates processed in the string.
	 * @throws DateUndefinedException if dateString does not contain a valid date, is empty, or null
	 */
	public static List<DateTime> parseDates(String dateString) throws DateUndefinedException {
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			DateGroup parsedDate = parser.parse(dateString).get(0);
			List<Date> dateList = parsedDate.getDates();
			for(Date date : dateList) {
				dateTimeList.add(new DateTime(date));
			}
		} catch (IndexOutOfBoundsException e) {
			throw new DateUndefinedException(Signal.UNDEFINED_DATE_STRING_EXCEPTION);
		} catch (NullPointerException e) {
			throw new DateUndefinedException(Signal.NULL_DATE_STRING_EXCEPTION);
		}
		return dateTimeList;
	}
}
