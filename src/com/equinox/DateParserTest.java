package com.equinox;
import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.junit.Test;


public class DateParserTest {
	
	DateTime expectedSingleDate = new DateTime(2015, 8, 26, 12, 59, 0);
	DateTime expectedFirstDate = new DateTime(2015, 11, 10, 18, 59, 0);
	DateTime expectedSecondDate = new DateTime(2015, 9, 29, 00, 00, 0);

	@Test
	public void testOneDate() {
		DateTime actualSingleDate = DateParser.parseDate("26 August at 1259pm");
		assertEquals("Year", expectedSingleDate.year(), actualSingleDate.year());
		assertEquals("Month", expectedSingleDate.monthOfYear(), actualSingleDate.monthOfYear());
		assertEquals("Day", expectedSingleDate.dayOfMonth(), actualSingleDate.dayOfMonth());
		assertEquals("Hour", expectedSingleDate.hourOfDay(), actualSingleDate.hourOfDay());
		assertEquals("Minute", expectedSingleDate.minuteOfHour(), actualSingleDate.minuteOfHour());
		assertEquals("Second", expectedSingleDate.secondOfMinute(), actualSingleDate.secondOfMinute());
		assertEquals("Object", expectedSingleDate, actualSingleDate);
	}
	
	@Test
	public void testTwoDates() {
		
		String date = "10 November at 0659pm to 29 September midnight";
		DateTime actualFirstDate = DateParser.parseDate(date,1);
		assertEquals("Year 1", expectedFirstDate.year(), actualFirstDate.year());
		assertEquals("Month 1", expectedFirstDate.monthOfYear(), actualFirstDate.monthOfYear());
		assertEquals("Day 1", expectedFirstDate.dayOfMonth(), actualFirstDate.dayOfMonth());
		assertEquals("Hour 1", expectedFirstDate.hourOfDay(), actualFirstDate.hourOfDay());
		assertEquals("Minute 1", expectedFirstDate.minuteOfHour(), actualFirstDate.minuteOfHour());
		assertEquals("Second 1", expectedFirstDate.secondOfMinute(), actualFirstDate.secondOfMinute());
		assertEquals("Object 1", expectedFirstDate, actualFirstDate);
		
		DateTime actualSecondDate = DateParser.parseDate(date, 2);
		assertEquals("Year 2", expectedSecondDate.year(), actualSecondDate.year());
		assertEquals("Month 2", expectedSecondDate.monthOfYear(), actualSecondDate.monthOfYear());
		assertEquals("Day 2", expectedSecondDate.dayOfMonth(), actualSecondDate.dayOfMonth());
		assertEquals("Hour 2", expectedSecondDate.hourOfDay(), actualSecondDate.hourOfDay());
		assertEquals("Minute 2", expectedSecondDate.minuteOfHour(), actualSecondDate.minuteOfHour());
		assertEquals("Second 2", expectedSecondDate.secondOfMinute(), actualSecondDate.secondOfMinute());
		assertEquals("Object 2", expectedSecondDate, actualSecondDate);
		
	}

}
