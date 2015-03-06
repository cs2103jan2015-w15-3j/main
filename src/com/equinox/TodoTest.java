package com.equinox;
import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;


public class TodoTest {
	
	DateTime expectedSingleDate = new DateTime(2015, 8, 26, 12, 59, 0);
	DateTime expectedFirstDate = new DateTime(2015, 9, 29, 00, 00, 0);
	DateTime expectedSecondDate = new DateTime(2015, 11, 10, 18, 59, 0);

	@Test
	public void testOneDate() {
		try {
			DateTime actualSingleDate = DateParser.parseDate("26 August at 1259pm");
			assertEquals("Year", expectedSingleDate.year(), actualSingleDate.year());
			assertEquals("Month", expectedSingleDate.monthOfYear(), actualSingleDate.monthOfYear());
			assertEquals("Day", expectedSingleDate.dayOfMonth(), actualSingleDate.dayOfMonth());
			assertEquals("Hour", expectedSingleDate.hourOfDay(), actualSingleDate.hourOfDay());
			assertEquals("Minute", expectedSingleDate.minuteOfHour(), actualSingleDate.minuteOfHour());
			assertEquals("Second", expectedSingleDate.secondOfMinute(), actualSingleDate.secondOfMinute());
			assertEquals("Object", expectedSingleDate, actualSingleDate);
		} catch (DateUndefinedException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTwoDates() {
		String date = "from midnight 29 September to 6:59pm 10 November";
		List<DateTime> actualDates;
		try {
			actualDates = DateParser.parseDates(date);
			DateTime actualFirstDate = actualDates.get(0);
			
			assertEquals("Year 1", expectedFirstDate.year(), actualFirstDate.year());
			assertEquals("Month 1", expectedFirstDate.monthOfYear(), actualFirstDate.monthOfYear());
			assertEquals("Day 1", expectedFirstDate.dayOfMonth(), actualFirstDate.dayOfMonth());
			assertEquals("Hour 1", expectedFirstDate.hourOfDay(), actualFirstDate.hourOfDay());
			assertEquals("Minute 1", expectedFirstDate.minuteOfHour(), actualFirstDate.minuteOfHour());
			assertEquals("Second 1", expectedFirstDate.secondOfMinute(), actualFirstDate.secondOfMinute());
			assertEquals("Object 1", expectedFirstDate, actualFirstDate);
			
			DateTime actualSecondDate = actualDates.get(1);
			assertEquals("Year 2", expectedSecondDate.year(), actualSecondDate.year());
			assertEquals("Month 2", expectedSecondDate.monthOfYear(), actualSecondDate.monthOfYear());
			assertEquals("Day 2", expectedSecondDate.dayOfMonth(), actualSecondDate.dayOfMonth());
			assertEquals("Hour 2", expectedSecondDate.hourOfDay(), actualSecondDate.hourOfDay());
			assertEquals("Minute 2", expectedSecondDate.minuteOfHour(), actualSecondDate.minuteOfHour());
			assertEquals("Second 2", expectedSecondDate.secondOfMinute(), actualSecondDate.secondOfMinute());
			assertEquals("Object 2", expectedSecondDate, actualSecondDate);
		} catch (DateUndefinedException e) {
			fail(e.getMessage());
		}
	}
	
	@Test (expected = DateUndefinedException.class)
	public void testInvalid() throws DateUndefinedException {
		String date = "not a date";
		DateParser.parseDate(date);
		DateParser.parseDate(date);
	}

	@Test (expected = DateUndefinedException.class)
	public void testEmpty() throws DateUndefinedException {
		String date = "";
		DateParser.parseDate(date);
		DateParser.parseDates(date);
	}
	
	@Test (expected = DateUndefinedException.class)
	public void testNull() throws DateUndefinedException {
		String date = null;
		DateParser.parseDate(date);
		DateParser.parseDate(date);
	}
}
