package com.equinox;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

	private static final String TASK_1 = "Read book";
	private static final String TASK_2 = "Do laundry";
	private static final String TASK_3 = "Do homework";
	Memory memory;

	@Before
	public void setUp() {
		memory = new Memory();
		Todo todo1 = new Todo(new DateTime(), TASK_1);
		memory.add(todo1);
		Todo todo2 = new Todo(new DateTime(), TASK_2);
		memory.add(todo2);
		Todo todo3 = new Todo(new DateTime(), TASK_3);
		memory.add(todo3);
	}

	@Test
	public void testAddGet() {
		assertEquals("Task 1 Add (Title)", TASK_1, memory.get(1).getTitle());
		assertEquals("Task 2 Add (Title)", TASK_2, memory.get(2).getTitle());
		assertEquals("Task 3 Add (Title)", TASK_3, memory.get(3).getTitle());
	}

	@Test
	public void testSetGet() {
		memory.saveCurrentState();
		Todo todo1 = memory.get(1);
		todo1.setDone(true);
		assertEquals("Task 1 Mark (isDone)", true, memory.get(1).isDone());
		assertEquals("Task 2 (isDone)", false, memory.get(2).isDone());
		assertEquals("Task 3 (isDone)", false, memory.get(3).isDone());
	}

	@Test
	public void testRemove() {
		memory.saveCurrentState();
		memory.remove(2);
		assertEquals("Task 1 (Title)", TASK_1, memory.get(1).getTitle());
		assertEquals("Task 2, was Task 3 (Title)", TASK_3, memory.get(2)
				.getTitle());
	}

	@Test
	public void testSaveRestore() throws StateUndefinedException {
		memory.saveCurrentState();
		memory.remove(2);
		assertEquals("Task 1 (Title)", TASK_1, memory.get(1).getTitle());
		assertEquals("Task 2, was Task 3 (Title)", TASK_3, memory.get(2)
				.getTitle());
		memory.restoreHistoryState();
		assertEquals("Task 1 (Title)", TASK_1, memory.get(1).getTitle());
		assertEquals("Task 2 Restored (Title)", TASK_2, memory.get(2)
				.getTitle());
		assertEquals("Task 3 (Title)", TASK_3, memory.get(3).getTitle());
	}

	@Test
	public void testUndoRedo() throws StateUndefinedException {
		memory.saveCurrentState();
		memory.remove(2);
		memory.saveCurrentState();
		memory.remove(1);
		memory.restoreHistoryState();
		memory.restoreFutureState();
		assertEquals("Task 3 (Title)", TASK_3, memory.get(1).getTitle());
	}
}
