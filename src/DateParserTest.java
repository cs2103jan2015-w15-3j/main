import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Test;


public class DateParserTest {

	@Test
	public void testOneDate() {
		GregorianCalendar expectedFirstDate = new GregorianCalendar(2015, 7, 26, 12, 59, 0);
		GregorianCalendar actualFirstDate = DateParser.parseDate("26 August at 1259pm");
		assertEquals("Year", expectedFirstDate.get(GregorianCalendar.YEAR), actualFirstDate.get(GregorianCalendar.YEAR));
		assertEquals("Month", expectedFirstDate.get(GregorianCalendar.MONTH), actualFirstDate.get(GregorianCalendar.MONTH));
		assertEquals("Date", expectedFirstDate.get(GregorianCalendar.DATE), actualFirstDate.get(GregorianCalendar.DATE));
		assertEquals("Hour", expectedFirstDate.get(GregorianCalendar.HOUR), actualFirstDate.get(GregorianCalendar.HOUR));
		assertEquals("Minute", expectedFirstDate.get(GregorianCalendar.MINUTE), actualFirstDate.get(GregorianCalendar.MINUTE));
		assertEquals("Second", expectedFirstDate.get(GregorianCalendar.SECOND), actualFirstDate.get(GregorianCalendar.SECOND));
		assertEquals("Second", expectedFirstDate.get(GregorianCalendar.PM), actualFirstDate.get(GregorianCalendar.PM));
		assertEquals("Object", expectedFirstDate, actualFirstDate);
	}
	
	@Test
	public void testTwoDates() {
		
		GregorianCalendar expectedFirstDate = new GregorianCalendar(2015, 10, 10, 18, 59, 0);
		String date = "10 November at 0659pm to 29 September midnight";
		GregorianCalendar actualFirstDate = DateParser.parseDate(date,1);
		assertEquals("Year 1", expectedFirstDate.get(GregorianCalendar.YEAR), actualFirstDate.get(GregorianCalendar.YEAR));
		assertEquals("Month 1", expectedFirstDate.get(GregorianCalendar.MONTH), actualFirstDate.get(GregorianCalendar.MONTH));
		assertEquals("Date 1", expectedFirstDate.get(GregorianCalendar.DATE), actualFirstDate.get(GregorianCalendar.DATE));
		assertEquals("Hour 1", expectedFirstDate.get(GregorianCalendar.HOUR), actualFirstDate.get(GregorianCalendar.HOUR));
		assertEquals("Minute 1", expectedFirstDate.get(GregorianCalendar.MINUTE), actualFirstDate.get(GregorianCalendar.MINUTE));
		assertEquals("Second 1", expectedFirstDate.get(GregorianCalendar.SECOND), actualFirstDate.get(GregorianCalendar.SECOND));
		assertEquals("AM/PM 1", expectedFirstDate.get(GregorianCalendar.PM), actualFirstDate.get(GregorianCalendar.PM));
		assertEquals("Object 1", expectedFirstDate, actualFirstDate);
		
		GregorianCalendar expectedSecondDate = new GregorianCalendar(2015, 8, 29, 00, 00, 0);
		GregorianCalendar actualSecondDate = DateParser.parseDate(date, 2);
		assertEquals("Year 2", expectedSecondDate.get(GregorianCalendar.YEAR), actualSecondDate.get(GregorianCalendar.YEAR));
		assertEquals("Month 2", expectedSecondDate.get(GregorianCalendar.MONTH), actualSecondDate.get(GregorianCalendar.MONTH));
		assertEquals("Date 2", expectedSecondDate.get(GregorianCalendar.DATE), actualSecondDate.get(GregorianCalendar.DATE));
		assertEquals("Hour 2", expectedSecondDate.get(GregorianCalendar.HOUR), actualSecondDate.get(GregorianCalendar.HOUR));
		assertEquals("Minute 2", expectedSecondDate.get(GregorianCalendar.MINUTE), actualSecondDate.get(GregorianCalendar.MINUTE));
		assertEquals("Second 2", expectedSecondDate.get(GregorianCalendar.SECOND), actualSecondDate.get(GregorianCalendar.SECOND));
		assertEquals("AM/PM 2", expectedSecondDate.get(GregorianCalendar.PM), actualSecondDate.get(GregorianCalendar.PM));
		assertEquals("Object 2", expectedSecondDate, actualSecondDate);
		
	}

}
