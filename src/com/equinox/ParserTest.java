package com.equinox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

public class ParserTest {
	
	//@author A0115983X
	
	@Test
	public void testAddFloatingCommands() {

		// floating task without any other keywords
		String add1 = "add test 1";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed1, Parser.parseInput(add1));

		// floating task with 'one keyword + invalid datetime'
		String add2 = "add study for test on algorithms";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "study for test on algorithms"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));
		assertEquals(parsed2, Parser.parseInput(add2));

	}

	@Test
	public void testAddDeadlineCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task with 'KEYWORD + <datetime>'
		String add0 = "add test 0 by Friday";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"))), dateTimes0, new Period(),
				false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));

		// deadline task 'KEYWORD <invalid datetime> + KEYWORD <datetime>'
		String add5 = "add test 5 at NTU on Friday";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 5 at NTU"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed5, Parser.parseInput(add5));

		// deadline task 'KEYWORD <datetime> + KEYWORD <invalid datetime>'
		String add6 = "add test 6 by Friday at Computing";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 6 at Computing"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testEventCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm
		List<Date> dates0 = parser.parse("3pm to 4pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withMillisOfSecond(0));
			dateTimes0.set(i, dateTimes0.get(i).withSecondOfMinute(0));
		}

		// dates: 3pm to 4pm on Sunday
		List<Date> dates1 = parser.parse("3pm to 4pm on Sunday").get(0)
				.getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withMillisOfSecond(0));
			dateTimes1.set(i, dateTimes1.get(i).withSecondOfMinute(0));
		}

		// dates: 3pm to 4pm on Sunday
		List<Date> dates2 = parser.parse("3 March at 10am to 3 March at 12pm")
				.get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withMillisOfSecond(0));
			dateTimes2.set(i, dateTimes2.get(i).withSecondOfMinute(0));
		}

		// event task default 'one keyword + 2 datetime'
		String add0 = "add test 0 from 3pm to 4pm";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"))), dateTimes0, new Period(),
				false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));

		// event task default 'keyword + day + keyword + 2 datetime'
		String add1 = "add test 1 on Sunday from 3pm to 4pm";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"))), dateTimes1, new Period(),
				false, false, new DateTime(0));
		assertEquals(parsed1, Parser.parseInput(add1));

		String add2 = "add CIP event from 3 March at 10am to 3 March at 12pm";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "CIP event"))), dateTimes2, new Period(),
				false, false, new DateTime(0));
		assertEquals(parsed2, Parser.parseInput(add2));
	}

	@Test
	public void testEndCases() {
		String add0 = "add";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, ""))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));

	}

	@Test
	public void testAddRecurringCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// dates: Friday to Sunday
		List<Date> dates1 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		// dates: limit 4 December 2015
		List<Date> dates2 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		String add0 = "add test 0 on Friday every week";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));

		// recurring deadline task 'EVERY <valid day of week>'
		String add6 = "add test 6 every Friday";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 6"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));

		// recurring event task
		String add1 = "add test 1 from Friday to Sunday every month";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"))), dateTimes1,
				new Period().withMonths(1), true, false, new DateTime(0));
		assertEquals(parsed1, Parser.parseInput(add1));

		// recurring deadline task with limit
		String add3 = "add test 3 on Friday every week until 4 Dec 2015";
		ParsedInput parsed3 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 3"))), dateTimes0,
				new Period().withWeeks(1), true, true, dateTimes2.get(0));
		assertEquals(parsed3, Parser.parseInput(add3));

		// recurring event task with limit
		String add4 = "add test 4 from Friday to Sunday every month until 4 Dec 2015";
		ParsedInput parsed4 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 4"))), dateTimes1,
				new Period().withMonths(1), true, true, dateTimes2.get(0));
		assertEquals(parsed4, Parser.parseInput(add4));

		// invalid recurring command: name + every <valid period> + <valid
		// limit>
		String add5 = "add test 5 every month until 4 Dec 2015";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 5"))),
				new ArrayList<DateTime>(), new Period().withMonths(1), false,
				true, dateTimes2.get(0));
		assertEquals(parsed5, Parser.parseInput(add5));
	}

	@Test
	public void testSearchYear() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 2016
		List<Date> dates0 = parser.parse("march 2016").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String search0 = "search -y 2016";
		ParsedInput parsed0 = new ParsedInput(Keywords.SEARCH,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.SEARCH, ""), new KeyParamPair(Keywords.YEAR,
						"2016"))), dateTimes0, new Period(), false, false,
				new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(search0));
	}
}
