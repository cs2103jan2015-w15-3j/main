// @author A0115983X
package com.equinox;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import com.equinox.exceptions.InvalidRecurringException;
import com.equinox.exceptions.InvalidTodoNameException;
import com.equinox.exceptions.ParsingFailureException;

public class ParserTest {

	@Test
	public void testAddFloating() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {

		// floating task without any other keywords
		String add1 = "add test 1";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	public void testAddFloatingWithKeyword() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		// floating task with 'one keyword + non-datetime'
		String add2 = "add study for test on algorithms";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "study for test on algorithms"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));
	}

	@Test
	public void testAddDeadlineWithDateOnly() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
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
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddDeadlineWithTime() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday 3pm
		List<Date> dates0 = parser.parse("Friday 3pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// deadline task with 'KEYWORD + <datetime>'
		String add0 = "add test 0 by Friday 3pm";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddDeadlineWithNondateAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("at 3pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			// dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}
		// deadline task 'KEYWORD <invalid datetime> + KEYWORD <datetime>'
		String add5 = "add test 5 in sr1 at 3pm";
		ParsedInput parsed5 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 5 in sr1"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed5, Parser.parseInput(add5));
	}

	@Test
	public void testAddDeadlineWithDateAndNondate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// deadline task 'KEYWORD <datetime> + KEYWORD <invalid datetime>'
		String add6 = "add test 6 by Friday at Computing";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 at Computing"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithTimeAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 2359 on 15 March
		List<Date> dates0 = parser.parse("2359 on 15 March").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// deadline task 'KEYWORD <datetime> + KEYWORD <invalid datetime>'
		String add6 = "add test 6 by 2359 on 15 March";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithNonPeriod()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period>'
		String add6 = "add test 6 by Friday every body";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every body"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithLimit() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
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

		// deadline task 'KEYWORD <datetime> + UNTIL <datetime>'
		String add6 = "add test 6 by Friday until 1 June";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 until 1 June"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddDeadlineWithNonPeriodAndLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period> + UNTIL
		// <datetime>'
		String add6 = "add test 6 by Friday every one until 1 June";
		ParsedInput parsed6 = new ParsedInput(
				Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every one until 1 June"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	public void testAddDeadlineWithNonPeriodAndNonLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// deadline task 'KEYWORD <datetime> + EVERY <invalid period> + UNTIL
		// <invalid datetime>'
		String add6 = "add test 6 by Friday every one until we die";
		ParsedInput parsed6 = new ParsedInput(
				Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6 every one until we die"))),
				dateTimes0, new Period(), false, false, new DateTime(0));

		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddEventWithTwoTimes() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm
		List<Date> dates0 = parser.parse("3pm to 4pm").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// event task default 'one keyword + 2 datetime'
		String add0 = "add test 0 from 3pm to 4pm";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddEventWithDateAndTwoTimes()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates1 = parser.parse("3pm to 4pm on Sunday").get(0)
				.getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
		}
		// event task default 'keyword + day + keyword + 2 datetime'
		String add1 = "add test 1 on Sunday from 3pm to 4pm";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddEventWithTwoTimesAndDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates1 = parser.parse("3pm to 4pm on Sunday").get(0)
				.getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
		}
		// event task default '+ keyword + 2 datetime + keyword + day'
		String add1 = "add test 1 from 3pm to 4pm on Sunday";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddEventWithTwoDateAndTime()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates2 = parser.parse("3 March at 10am to 3 March at 12pm")
				.get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
		}

		String add2 = "add CIP event from 3 March at 10am to 3 March at 12pm";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "CIP event"))), dateTimes2,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));
	}

	@Test
	public void testAddEventWithTwoDates() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {

		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: 3pm to 4pm on Sunday
		List<Date> dates2 = parser.parse("Sunday to Monday").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		String add0 = "add test 0 from Sunday to Monday";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes2,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testStringProcessing() throws InvalidTodoNameException,
			InvalidRecurringException, ParsingFailureException {
		String add0 = "       add";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", ""))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));

		String add1 = "add         ";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", ""))), new ArrayList<DateTime>(),
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));

		String add2 = "add         something";
		ParsedInput parsed2 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "something"))),
				new ArrayList<DateTime>(), new Period(), false, false,
				new DateTime(0));

		assertEquals(parsed2, Parser.parseInput(add2));

	}

	@Test
	public void testAddRecurringDeadlineWithDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		String add0 = "add test 0 on Friday every week";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));

	}

	@Test
	public void testAddRecurringDeadlineWithDayOfWeek()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// recurring deadline task 'EVERY <valid day of week>'
		String add6 = "add test 6 every Friday";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddRecurringDeadlineWithTimeEveryDayOfWeek() throws InvalidRecurringException, InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());
		// date: Friday
		List<Date> dates0 = parser.parse("3pm on Friday").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
		}

		// recurring deadline task 'EVERY <valid day of week>'
		String add6 = "add test 6 at 3pm every Friday";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withWeeks(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddRecurringDeadlineEveryDay()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("today").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'EVERY day'
		String add6 = "add test 6 every day";
		ParsedInput parsed6 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 6"))), dateTimes0,
				new Period().withDays(1), true, false, new DateTime(0));
		assertEquals(parsed6, Parser.parseInput(add6));
	}

	@Test
	public void testAddRecurringEvent() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// dates: Friday to Sunday
		List<Date> dates1 = parser.parse("Friday to Sunday").get(0).getDates();
		List<DateTime> dateTimes1 = new ArrayList<DateTime>();
		for (int i = 0; i < dates1.size(); i++) {
			Date date = dates1.get(i);
			dateTimes1.add(new DateTime(date));
			dateTimes1.set(i, dateTimes1.get(i).withTime(23, 59, 0, 0));
		}

		// recurring event task
		String add1 = "add test 1 from Friday to Sunday every month";
		ParsedInput parsed1 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 1"))), dateTimes1,
				new Period().withMonths(1), true, false, new DateTime(0));

		assertEquals(parsed1, Parser.parseInput(add1));
	}

	@Test
	public void testAddRecurringDeadlineWithLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		List<Date> dates2 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes2 = new ArrayList<DateTime>();
		for (int i = 0; i < dates2.size(); i++) {
			Date date = dates2.get(i);
			dateTimes2.add(new DateTime(date));
			dateTimes2.set(i, dateTimes2.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task with limit
		String add3 = "add test 3 on Friday every week until 4 Dec 2015";
		ParsedInput parsed3 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 3"))), dateTimes0,
				new Period().withWeeks(1), true, true, dateTimes2.get(0));
		assertEquals(parsed3, Parser.parseInput(add3));
	}

	@Test
	public void testAddRecurringDeadlineWithNonLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
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

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		// + UNTIL <invalid limit>'
		String add0 = "add test 0 on Friday every week until forever";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0 until forever"))),
				dateTimes0, new Period().withWeeks(1), true, false,
				new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test
	public void testAddRecurringEventWithLimit()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

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

		// recurring event task with limit
		String add4 = "add test 4 from Friday to Sunday every month until 4 Dec 2015";
		ParsedInput parsed4 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 4"))), dateTimes1,
				new Period().withMonths(1), true, true, dateTimes2.get(0));

		assertEquals(parsed4, Parser.parseInput(add4));
	}

	@Test
	public void testAddRecurringDeadlineEveryDate()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: Friday
		List<Date> dates0 = parser.parse("4 Jan").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		// recurring deadline task 'KEYWORD <valid date> + EVERY <valid period>
		String add0 = "add test 0 every 4 Jan";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 0"))), dateTimes0,
				new Period().withYears(1), true, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test(expected = InvalidRecurringException.class)
	public void testAddInvalidRecurring() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}
		// invalid recurring command: name + every <valid period> + <valid
		// limit>
		String add0 = "add test 5 every month until 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.ADD,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.ADD, "add", "test 5"))),
				new ArrayList<DateTime>(), new Period().withMonths(1), false,
				true, dateTimes0.get(0));
		assertEquals(parsed0, Parser.parseInput(add0));
	}

	@Test(expected = ParsingFailureException.class)
	public void testAddTaskWithMoreThanTwoDateTimes()
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		String add0 = "add event from 1 Jan to 2 Jan in 2 days";
		Parser.parseInput(add0);
	}

	@Test
	public void testEditTo() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		// date: 4 Dec 2015
		List<Date> dates0 = parser.parse("4 Dec 2015").get(0).getDates();
		List<DateTime> dateTimes0 = new ArrayList<DateTime>();
		for (int i = 0; i < dates0.size(); i++) {
			Date date = dates0.get(i);
			dateTimes0.add(new DateTime(date));
			dateTimes0.set(i, dateTimes0.get(i).withTime(23, 59, 0, 0));
		}

		String edit0 = "edit 1 to 4 Dec 2015";
		ParsedInput parsed0 = new ParsedInput(Keywords.EDIT,
				new ArrayList<KeyParamPair>(Arrays.asList(new KeyParamPair(
						Keywords.EDIT, "edit", "1"), new KeyParamPair(
						Keywords.TO, "to", "4 Dec 2015"))), dateTimes0,
				new Period(), false, false, new DateTime(0));
		assertEquals(parsed0, Parser.parseInput(edit0));
	}

	@Test
	public void testSearchYear() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {
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
						Keywords.SEARCH, "search", ""), new KeyParamPair(
						Keywords.YEAR, "-y", "march 2016"))), dateTimes0,
				new Period(), false, false, new DateTime(0));

		assertEquals(parsed0, Parser.parseInput(search0));

	}

	@Test(expected = InvalidTodoNameException.class)
	public void testAddFlagName() throws InvalidRecurringException,
			InvalidTodoNameException, ParsingFailureException {

		String add0 = "add -y";
		Parser.parseInput(add0);
	}
}
