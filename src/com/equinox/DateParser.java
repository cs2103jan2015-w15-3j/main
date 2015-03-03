package com.equinox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * DateParser.parseDate(dateString) parses the last string into a GregorianCalendar object using parseDate() and returns a date according to the host Time Zone.
 * Multiple dates may be processed per string, in which case the parser can accept an index to determine which date to return, else the first date is returned.
 * 
 */

/**
 * @author Ikarus
 *
 */
public class DateParser {

	/**
	 * Parses a single date String provided to the DateParser.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return The immutable DateTime object representing the first date encountered in the string.
	 */
	public static DateTime parseDate(String dateString) {
		DateTime returnDateTime = null;
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			DateGroup parsedDate = parser.parse(dateString).get(0);
			List<Date> dateList = parsedDate.getDates();
			Date firstDate = dateList.get(0);
			returnDateTime = new DateTime(firstDate);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return returnDateTime;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and returns a DateTime array.
	 * 
	 * @param dateString String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates processed in the string.
	 */
	public static List<DateTime> parseDates(String dateString) {
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			DateGroup parsedDate = parser.parse(dateString).get(0);
			List<Date> dateList = parsedDate.getDates();
			for(Date date : dateList) {
				dateTimeList.add(new DateTime(date));
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Referenced date number exceeds number of date entries detected.");
			e.printStackTrace();
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return dateTimeList;
	}

}
