import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.joestelmach.natty.*;

/**
 * DateParser parses the last string into a GregorianCalendar object using parseDate() and returns a date according to the host Time Zone.
 * Multiple dates may be processed per string, in which case the parser requires a parameter to determine which date to return, else the first date is returned.
 * 
 */

/**
 * @author Ikarus
 *
 */
public class DateParser {

	Parser parser;
	List<DateGroup> dateGroupList;

	public DateParser (){
		parser = new Parser(TimeZone.getDefault());
	}
	
	/**
	 * @param input Initial String to be parsed
	 */
	public DateParser(String input) {
		// Creates a parser object according to host timezone
		parser = new Parser(TimeZone.getDefault());
		dateGroupList = parser.parse(input);
	}

	public void acceptInput(String input) {
		dateGroupList = parser.parse(input);
	}

	/**
	 * Parses the latest String provided to the DateParser.
	 * 
	 * @return The first date encountered in the string.
	 */
	public GregorianCalendar parseDate() {
		GregorianCalendar gregDate = new GregorianCalendar();
		try {
			Date date = dateGroupList.get(dateGroupList.size() - 1).getDates()
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
	public GregorianCalendar parseDate(int dateNumber) {
		GregorianCalendar gregDate = new GregorianCalendar();
		try {
			Date date = dateGroupList.get(dateGroupList.size() - 1).getDates()
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
