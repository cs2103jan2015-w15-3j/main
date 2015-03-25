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

	@Test
	public void testAddFloatingCommands() {

		// floating task without any other keywords
		String add1 = "add test 1";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"))), new ArrayList<DateTime>(),
				new Period(), false);
		assertEquals(parsed1, Parser.parseInput(add1));

		// floating task with 'one keyword + invalid datetime'
		String add2 = "add study for test on algorithms";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "study for test on algorithms"),
						new KeyParamPair(Keywords.ON, "algorithms"))),
				new ArrayList<DateTime>(), new Period(), false);
		assertEquals(parsed2, Parser.parseInput(add2));

	}

	@Test
	public void testAddDeadlineCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (Date d : dates0) {
			dateTimes0.add(new DateTime(d));
		}

		// deadline task with 'one keyword + one datetime'
		String add0 = "add test 0 by Friday";

		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"), new KeyParamPair(Keywords.BY,
						"Friday"))), dateTimes0, new Period(), false);
		// ParsedInput parsed = Parser.parseInput(add0);
		// System.out.println(parsed.getType().equals(parsed0.getType()));
		// System.out.println(parsed.getParamPairs().equals(
		// parsed0.getParamPairs()));
		// System.out.println(parsed.getDateTimes());
		// System.out.println(parsed0.getDateTimes());
		// System.out
		// .println(parsed.getDateTimes().equals(parsed0.getDateTimes()));
		// System.out.println(parsed.getPeriod().equals(parsed0.getPeriod()));
		// System.out.println(parsed.isRecurring() == parsed0.isRecurring());
		assertEquals(parsed0, Parser.parseInput(add0));

		// deadline task 'at x on date'
		String add5 = "add test 5 at NTU on Friday";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 5 at NTU"), new KeyParamPair(
						Keywords.AT, "NTU"), new KeyParamPair(Keywords.ON,
						"Friday"))), dateTimes0, new Period(), false);
		assertEquals(parsed5, Parser.parseInput(add5));

		// deadline task 'at x by date'
		String add7 = "add test 7 at Computing by Friday";
		ParsedInput parsed7 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 7 at Computing"), new KeyParamPair(
						Keywords.AT, "Computing"), new KeyParamPair(
						Keywords.BY, "Friday"))), dateTimes0, new Period(),
				false);
		assertEquals(parsed7, Parser.parseInput(add7));

		// deadline task 'from x on date'
		String add8 = "add test 8 from Malaysia on Friday";
		ParsedInput parsed8 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 8 from Malaysia"),
						new KeyParamPair(Keywords.FROM, "Malaysia"),
						new KeyParamPair(Keywords.ON, "Friday"))), dateTimes0,
				new Period(), false);
		assertEquals(parsed8, Parser.parseInput(add8));

		// deadline task 'by date at x'
		String add6 = "add test 6 by Friday at Computing";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 6 at Computing"), new KeyParamPair(
						Keywords.BY, "Friday"), new KeyParamPair(Keywords.AT,
						"Computing"))), dateTimes0, new Period(), false);
		assertEquals(parsed6, Parser.parseInput(add6));

		// deadline task 'on date at x'
		String add4 = "add test 4 on Friday at NTU";
		ParsedInput parsed4 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 4 at NTU"), new KeyParamPair(
						Keywords.ON, "Friday"), new KeyParamPair(Keywords.AT,
						"NTU"))), dateTimes0, new Period(), false);
		assertEquals(parsed4, Parser.parseInput(add4));
	}

	@Test
	public void testEventCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: Friday to Sunday
		List<Date> dates0 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (Date d : dates0) {
			dateTimes0.add(new DateTime(d));
		}

		// event task default 'one keyword + 2 datetime'
		String add0 = "add test 0 from Friday to Sunday";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"), new KeyParamPair(
						Keywords.FROM, "Friday to Sunday"))), dateTimes0,
				new Period(), false);
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testEndCases() {
		String add0 = "add";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, ""))), new ArrayList<DateTime>(),
				new Period(), false);
		assertEquals(parsed0, Parser.parseInput(add0));

	}

	@Test
	public void testAddRecurringCommands() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (Date d : dates0) {
			dateTimes0.add(new DateTime(d));
		}

		// dates: Friday to Sunday
		List<Date> dates1 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (Date d : dates1) {
			dateTimes1.add(new DateTime(d));
		}

		// dates: limit 4 December 2015
		List<Date> dates2 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (Date d : dates2) {
			dateTimes2.add(new DateTime(d));
		}

		// recurring deadline task
		String add0 = "add test 0 on Friday every week";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"), new KeyParamPair(Keywords.ON,
						"Friday"), new KeyParamPair(Keywords.EVERY, "week"))),
				dateTimes0, new Period().withWeeks(1), true);
		assertEquals(parsed0, Parser.parseInput(add0));

		// recurring event task
		String add1 = "add test 1 from Friday to Sunday every month";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"), new KeyParamPair(
						Keywords.FROM, "Friday to Sunday"), new KeyParamPair(
						Keywords.EVERY, "month"))), dateTimes1,
				new Period().withMonths(1), true);
		assertEquals(parsed1, Parser.parseInput(add1));

		// recurring deadline task with limit
		String add3 = "add test 3 on Friday every week until 4 Dec 2015";
		dateTimes0.addAll(dateTimes2);
		ParsedInput parsed3 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 3"), new KeyParamPair(Keywords.ON,
						"Friday"), new KeyParamPair(Keywords.EVERY, "week"),
						new KeyParamPair(Keywords.UNTIL, "4 Dec 2015"))),
				dateTimes0, new Period().withWeeks(1), true);
		assertEquals(parsed3, Parser.parseInput(add3));

		// recurring event task with limit
		String add4 = "add test 4 from Friday to Sunday every month until 4 Dec 2015";
		dateTimes1.addAll(dateTimes2);
		ParsedInput parsed4 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 4"), new KeyParamPair(
						Keywords.FROM, "Friday to Sunday"), new KeyParamPair(
						Keywords.EVERY, "month"), new KeyParamPair(
						Keywords.UNTIL, "4 Dec 2015"))), dateTimes1,
				new Period().withMonths(1), true);
		assertEquals(parsed4, Parser.parseInput(add4));

		// invalid recurring command: name + every <valid period> 
		String add5 = "add test 5 every month until 4 Dec 2015";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 5 every month until 4 Dec 2015"), new KeyParamPair(
						Keywords.EVERY, "month"), new KeyParamPair(
						Keywords.UNTIL, "4 Dec 2015"))), new ArrayList<DateTime>(),
				new Period().withMonths(1), false);
		assertEquals(parsed5, Parser.parseInput(add5));
	}
}
