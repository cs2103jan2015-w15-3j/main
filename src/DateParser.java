import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.joestelmach.natty.*;

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
	 * @return The first date encountered in the string.
	 */
	public static GregorianCalendar parseDate(String dateString) {
		GregorianCalendar gregDate = new GregorianCalendar();
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			Date date = parser.parse(dateString).get(0).getDates()
					.get(0);
			gregDate.setTime(date);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return gregDate;
	}

	/**
	 * Parses the latest String provided to the DateParser.
	 * 
	 * @param dateNumber Specify which date to return in the event string
	 *            contains multiple dates.
	 * @return The nth date processed in the string.
	 */
	public GregorianCalendar parseDate(String dateString, int dateNumber) {
		GregorianCalendar gregDate = new GregorianCalendar();
		Parser parser = new Parser(TimeZone.getDefault());
		try {
			Date date = parser.parse(dateString).get(0).getDates()
					.get(dateNumber - 1);
			gregDate.setTime(date);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Referenced date number exceeds number of date entries detected.");
			e.printStackTrace();
			System.exit(-1);
		} catch (NullPointerException e) {
			System.out.println("Input string is not defined.");
			e.printStackTrace();
			System.exit(-1);
		}
		return gregDate;
	}

}
