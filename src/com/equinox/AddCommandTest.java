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
		final DateTime baseTime = new DateTime();
		DateTime changedTime;
		String formattedTime;
		String formattedDate;
		/*
		 * Test for a single-worded, lower-case floating task, with absolute date and time, long-format
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
		 * Test for a single-worded, lower-case floating task, absolute date and time, short-format
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
		 * Test for a single-worded, lower-case floating task, relative time (day), long-format
		 */
		changedTime = baseTime.plusDays(2);
		formattedDate = formatDate(changedTime);
		deadlineCommand = "add interview in 2 days";
		deadlineString = "Deadline \"interview\" by " + formattedDate + " at 23:59";
		// Mock Signal object
		addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(deadlineCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
		
		/*
		 * Test for a multiple-worded, mixed-case floating task, absolute date and time, long-format
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
		 * Test for a multiple-worded, mixed-case floating task, absolute date and time, long-format
		 */
		deadlineCommand = "add hand in CS2103T developers guide by 1 April at 6pm";
		deadlineString = "Deadline \"hand in CS2103T developers guide\" by Wed 01 Apr 2015 at 18:00";
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
		/*
		 * Test for a single-worded, lower-case event
		 */
		String eventCommand = "add canoeing from 3pm to 4pm on 6 April";
		String eventString = "Event \"canoeing\" from Mon 06 Apr 2015 at 15:00 to Mon 06 Apr 2015 at 16:00";
		// Mock Signal object
		Signal addSuccess = new Signal(String.format(
				Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		try{
		assertEquals(addSuccess, logic.handleInput(eventCommand));
		} catch(InvalidRecurringException | InvalidTodoNameException e){}
	}
	
}
