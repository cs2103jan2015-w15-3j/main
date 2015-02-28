package com.equinox;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;

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
	 * @return The immutable first date encountered in the string.
	 */
	public static DateTime parseDate(String dateString) {
		DateTime returnDateTime = null;
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			Date parsedDate = parser.parse(dateString).get(0).getDates()
					.get(0);
			returnDateTime = new DateTime(parsedDate);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return returnDateTime;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and returns the nth date.
	 * 
	 * @param dateNumber Specify which date to return in the event string
	 *            contains multiple dates.
	 * @return The immutable nth date processed in the string.
	 */
	public static DateTime parseDate(String dateString, int dateNumber) {
		DateTime returnDateTime = null;
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			Date parsedDate = parser.parse(dateString).get(0).getDates()
					.get(dateNumber - 1);
			returnDateTime = new DateTime(parsedDate);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Referenced date number exceeds number of date entries detected.");
			e.printStackTrace();
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return returnDateTime;
	}

}
