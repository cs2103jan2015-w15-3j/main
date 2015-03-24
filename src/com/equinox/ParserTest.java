package com.equinox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.junit.Test;

public class ParserTest {

	@Test
	public void testAddFloatingCommands() {

		// floating task without any other keywords
		String add1 = "add test 1";
		ParsedInput parsed1 = new ParsedInput(add1, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 1"))), new ArrayList<DateTime>());
		assertEquals(parsed1, Parser.parseInput(add1));

		// floating task with 'one keyword + invalid datetime'
		String add2 = "add study for test on algorithms";
		ParsedInput parsed2 = new ParsedInput(add2, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "study for test on algorithms"))),
				new ArrayList<DateTime>());
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

		ParsedInput parsed0 = new ParsedInput(add0, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"))), dateTimes0);
		assertEquals(parsed0, Parser.parseInput(add0));

		// deadline task 'at x on date'
		String add5 = "add test 5 at NTU on Friday";
		ParsedInput parsed5 = new ParsedInput(add5, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 5 at NTU"))), dateTimes0);
		assertEquals(parsed5, Parser.parseInput(add5));

		// deadline task 'at x by date'
		String add7 = "add test 7 at Computing by Friday";
		ParsedInput parsed7 = new ParsedInput(add7, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 7 at Computing"))), dateTimes0);
		assertEquals(parsed7, Parser.parseInput(add7));

		// Failed cases:
		// deadline task 'from x on date'
		String add8 = "add test 8 from Malaysia on Friday";
		ParsedInput parsed8 = new ParsedInput(add8, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 8 from Malaysia"))), dateTimes0);
		System.out.println(Parser.parseInput(add8).getParamPairs().get(0)
				.getParam());
		assertEquals(parsed8, Parser.parseInput(add8));

		// deadline task 'by date at x'
		String add6 = "add test 6 by Friday at Computing";
		ParsedInput parsed6 = new ParsedInput(add6, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 6 at Computing"))), dateTimes0);
		System.out.println(Parser.parseInput(add6).getParamPairs().get(0)
				.getParam());
		assertEquals(parsed6, Parser.parseInput(add6));

		// deadline task 'on date at x'
		String add4 = "add test 4 on Friday at NTU";
		ParsedInput parsed4 = new ParsedInput(add4, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 4 at NTU"))), dateTimes0);
		System.out.println(Parser.parseInput(add4).getParamPairs().get(0)
				.getParam());
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
		ParsedInput parsed0 = new ParsedInput(add0, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "test 0"))), dateTimes0);
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testEndCases() {
		String add0 = "add";
		ParsedInput parsed0 = new ParsedInput(add0, Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, ""))), new ArrayList<DateTime>());
		assertEquals(parsed0, Parser.parseInput(add0));

	}
}
