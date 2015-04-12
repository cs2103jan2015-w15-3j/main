/**
 * Unit test class for AddCommand.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

package com.equinox;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import com.equinox.exceptions.InvalidRecurringException;
import com.equinox.exceptions.InvalidTodoNameException;

//@author A0110839H
public class AddCommandTest {
	Memory memory;
	ParsedInput input;
	Keywords addCommand = Keywords.ADD;
	Zeitgeist logic;
	
	private static final String userInputFloatingTask1 = "add study CS2105";

	public enum TYPE {
		ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
	}

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
            .forPattern("EEE dd MMM yyyy");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");
    private static String formatTime(DateTime time) {
    	String timeString = TimeFormatter.print(time);
        return timeString;
    }

    private static String formatDate(DateTime time) {
        String dateString = DateFormatter.print(time);

        return dateString;
    }
	
	@Before
	public void setUp() {
		String fileDirectory = Zeitgeist.getStorageFileDirFromSettings();
		logic = new Zeitgeist(fileDirectory);
		logic.reloadMemory();

	}

	@After
	public void tearDown() {
		logic.deleteStorageFile();
	}

	@Test
	public void testLessThanOneKeyword() {
		// Mock Signal object
		Signal invalidParamSignal = new Signal(Signal.GENERIC_EMPTY_PARAM,
				false);
		try{
		// Test for equivalence in Signal object
		assertEquals(invalidParamSignal, logic.handleInput("add"));
		
		//Should not happen
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
	}

	/*
	 * Tests adding of floating tasks
	 */
	@Test
	public void testFloatingTask() {
		String floatingTaskCommand;
		String floatingTaskString;
		Signal addSuccess;
		
		/*
		 * Test for a single-worded, lower-case floating task
		 */
		floatingTaskCommand = "add running";
		floatingTaskString = "Floating task \"running\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTaskString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running";
		floatingTaskString = "Floating task \"Running\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTaskString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running for StandChart marathon";
		floatingTaskString = "Floating task \"Running for StandChart marathon\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTaskString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case floating task
		 */
		floatingTaskCommand = "add Running for 42km StandChart marathon";
		floatingTaskString = "Floating task \"Running for 42km StandChart marathon\"";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTaskString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(floatingTaskCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
	}
	
	/*
	 * Test adding of deadlines
	 */
	@Test
	public void testDeadline() {
		String deadlineCommand;
		String deadlineString;
		Signal addSuccess;
		DateTime changedTime;
		String formattedTime;
		String formattedDate;
		final DateTime baseTime = new DateTime();
		/*
		 * Absolute deadlines 
		 */
		
		/*
		 * Test for a single-worded, lower-case title, with absolute date and time, long-format
		 */
		deadlineCommand = "add interview by 0800 on 13 Apr";
		deadlineString = "Deadline \"interview\" by Mon 13 Apr 2015 at 08:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, absolute date and time, short-format with upper-case
		 */
		deadlineCommand = "add interview by 8am on 15 Mar";
		deadlineString = "Deadline \"interview\" by Sun 15 Mar 2015 at 08:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (day), long-format
		 */
		changedTime = baseTime.plusDays(2);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add interview in two days";
		deadlineString = "Deadline \"interview\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (day), short-format
		 */
		deadlineCommand = "add interview in 2 days";
		deadlineString = "Deadline \"interview\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		/*
		 * Test for a multiple-worded, mixed-case title, absolute date and time, long-format
		 */
		deadlineCommand = "add interview with Google by 10am on 17 June";
		deadlineString = "Deadline \"interview with Google\" by Wed 17 Jun 2015 at 10:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case title, absolute date and time, long-format
		 */
		deadlineCommand = "add hand in CS2103T developers guide by 1 April at 6pm";
		deadlineString = "Deadline \"hand in CS2103T developers guide\" by Wed 01 Apr 2015 at 18:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Relative deadlines
		 */
		
		/*
		 * Test for a single-worded, lower-case title, relative time (months), long-format in lower-case
		 */
		changedTime = baseTime.plusMonths(2);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add NOC in three months";
		deadlineString = "Deadline \"NOC\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (months), short-format in lower-case
		 */
		deadlineCommand = "add NOC in 3 months";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		
		/*
		 * Test for a single-worded, lower-case title, relative time (months), short-format in lower-case
		 */
		deadlineCommand = "add NOC in 3 month";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (months), short-format in mixed-case
		 */
		deadlineCommand = "add NOC in 3 Months";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}

		/*
		 * Test for a multiple-worded, mixed-case title, relative time (months), short-format in mixed-case
		 */
		changedTime = baseTime.plusMonths(2);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add NOC in Silicon Valley in 3 months";
		deadlineString = "Deadline \"NOC in Silicon Valley\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), long-format in lower-case
		 */
		changedTime = baseTime.plusHours(2);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add exam in two hours";
		deadlineString = "Deadline \"exam\" by " + formattedDate + " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), long-format in mixed-case
		 */
		deadlineCommand = "add exam in Two Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), short-format in lower-case
		 */
		deadlineCommand = "add exam in 2 hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), short-format in lower-case
		 */
		deadlineCommand = "add exam in 2 hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), short-format in mixed-case
		 */
		deadlineCommand = "add exam in 2 Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (hours), short-format in mixed-case
		 */
		deadlineCommand = "add exam in 2 Hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours), long-format in lower-case
		 */
		changedTime = baseTime.plusHours(6);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add CS2103T exam in SR1 in six hours";
		deadlineString = "Deadline \"CS2103T exam in SR1\" by " + formattedDate + " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours), long-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in Six Hours ";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case title, relative time (hours), short-format in lower-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours), short-format in lower-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours), short-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 Hours";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, lower-case title, relative time (hours), short-format in mixed-case
		 */
		deadlineCommand = "add CS2103T exam in SR1 in 6 Hrs";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
	
		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), long-format in lower-case
		 */
		changedTime = baseTime.plusMinutes(30);
		formattedDate = formatDate(changedTime);
		formattedTime = formatTime(changedTime);
		deadlineCommand = "add lunch in thirty minutes";
		deadlineString = "Deadline \"lunch\" by " + formattedDate + " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		

		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), long-format in mixed-case
		 */
		deadlineCommand = "add lunch in Thirty Minutes";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), short-format in lower-case
		 */
		
		deadlineCommand = "add lunch in 30 min";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), short-format in lower-case
		 */
		
		deadlineCommand = "add lunch in 30 mins";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), short-format in mixed-case
		 */
		
		deadlineCommand = "add lunch in 30 Min";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		
		/*
		 * Test for a single-worded, lower-case title, relative time (minutes), short-format in mixed-case
		 */
		
		deadlineCommand = "add lunch in 30 Mins";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case title, relative time (minutes), long-format in lower-case
		 */
		deadlineCommand = "add lunch at McDonalds in 30 min";
		deadlineString = "Deadline \"lunch at McDonalds\" by " + formattedDate + " at " + formattedTime;
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		
	} 
	/*
	 * Test adding of events
	 */
	@Test
	public void testEvent() {
		final DateTime baseTime = new DateTime();
		DateTime changedTime;
		String formattedDate;
		String formattedTime;
		String eventCommand;
		String eventString;
		Signal addSuccess;
		
		/*
		 * Absolute time
		 */
		/*
		 * Test for a single-worded, lower-case event, long-format
		 */
		eventCommand = "add canoeing from 3pm to 4pm on six april";
		eventString = "Event \"canoeing\" from Mon 06 Apr 2015 at 15:00 to Mon 06 Apr 2015 at 16:00 ";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a single-worded, lower-case event within the same day, short format
		 */
		eventCommand = "add canoeing from 3pm to 4pm on 6 apr";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case event within the same day, short format
		 */
		
		eventCommand = "add climb Mount Everest in Nepal from 3pm to 4pm on 12 Jun";
		eventString = "Event \"climb Mount Everest in Nepal\" from Fri 12 Jun 2015 at 15:00 to Fri 12 Jun 2015 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case event straddling different days, short format
		 */
		
		eventCommand = "add climb Mount Everest in Nepal from 5 jun 3pm to 20 jun 4pm";
		eventString = "Event \"climb Mount Everest in Nepal\" from Fri 05 Jun 2015 at 15:00 to Sat 20 Jun 2015 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case event straddling different days, short format
		 */
		
		eventCommand = "add climb Mount Everest in Nepal from 3pm on 5 jun to 4pm on 20 jun";
		eventString = "Event \"climb Mount Everest in Nepal\" from Fri 05 Jun 2015 at 15:00 to Sat 20 Jun 2015 at 16:00";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
	
		/*
		 * Relative time
		 */
		
	}
	
}
