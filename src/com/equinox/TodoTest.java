package com.equinox;

import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.junit.Test;


public class TodoTest {
	
	String title1 = "Todo One";
	String title2 = "Todo Two";
	String deadlineString = "26 August at 1259pm";
	String periodString = "from midnight 29 September to 6:59pm 10 November";
	String startTimeString = "midnight 29 September";
	String endTimeString = "6:59pm 10 November";
	String notDate = "not a date";
	String empty = "";
	DateTime expectedDeadline = new DateTime(2015, 8, 26, 12, 59, 0);
	DateTime expectedStartDate = new DateTime(2015, 9, 29, 00, 00, 0);
	DateTime expectedEndDate = new DateTime(2015, 11, 10, 18, 59, 0);

	@Test
	public void testTask() {
		try {
			Todo task = new Todo(title1);
			assertEquals(Todo.TYPE.TASK, task.getType());
			assertEquals(title1, task.getTitle());
		} catch (DateUndefinedException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDeadline() {
		try {
			Todo deadline = new Todo(title1, deadlineString);
			assertEquals(Todo.TYPE.DEADLINE, deadline.getType());
			assertEquals(title1, deadline.getTitle());
			assertEquals(expectedDeadline, deadline.getEndTime());
		} catch (DateUndefinedException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testEvent() {
		try {
			Todo event = new Todo(title2, periodString);
			assertEquals(title2, event.getTitle());
			assertEquals(Todo.TYPE.EVENT, event.getType());
			assertEquals(expectedStartDate, event.getStartTime());
			assertEquals(expectedEndDate, event.getEndTime());
		} catch (DateUndefinedException e) {
			fail(e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	@Test (expected = DateUndefinedException.class)
	public void testInvalid() throws DateUndefinedException {
		Todo todo = new Todo(title1, notDate);
	}
	
	@SuppressWarnings("unused")
	@Test (expected = DateUndefinedException.class)
	public void testEmpty() throws DateUndefinedException {
		Todo todo = new Todo(title2, empty);
	}
	
	@SuppressWarnings("unused")
	@Test (expected = DateUndefinedException.class)
	public void testNull() throws DateUndefinedException {
		Todo todo = new Todo(title1, null);
	}
	
	@Test
	public void testSetStartEndTime() throws DateUndefinedException {
		Todo todo = new Todo(title1, deadlineString);
		todo.setName(title2);
		todo.setStartTime(startTimeString);
		todo.setEndTime(endTimeString);
		todo.isValid();
		assertEquals(title2, todo.getTitle());
		assertEquals(Todo.TYPE.EVENT, todo.getType());
		assertEquals(expectedStartDate, todo.getStartTime());
		assertEquals(expectedEndDate, todo.getEndTime());
	}
}
