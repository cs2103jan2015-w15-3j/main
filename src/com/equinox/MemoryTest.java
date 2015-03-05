package com.equinox;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

	private static final String TASK_1 = "Read book";
	private static final String TASK_2 = "Do laundry";
	private static final String TASK_3 = "Do homework";
	Memory memory;
	Todo todo1, todo2, todo3;

	@Before
	public void setUp() {
		memory = new Memory();
		todo1 = new Todo(TASK_1);
		memory.add(todo1);
		todo2 = new Todo(TASK_2);
		memory.add(todo2);
		todo3 = new Todo(TASK_3);
		memory.add(todo3);
	}

	@Test
	public void testAddGet() {
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		assertEquals("Todo2", todo2, memory.get(todo2.getIndex()));
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
	}

	@Test
	public void testSetterGetUndo() throws StateUndefinedException {
		memory.setterGet(todo1.getIndex());
		Todo todo1Copy = new Todo(todo1);
		todo1.setDone(true);
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		assertEquals("Todo2", todo2, memory.get(todo2.getIndex()));
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
		memory.restoreHistoryState();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.get(todo1.getIndex()));
	}
	
	@Test
	public void testSetterGetUndoRedo() throws StateUndefinedException {
		memory.setterGet(todo1.getIndex());
		Todo todo1Copy = new Todo(todo1);
		todo1.setDone(true);
		Todo todo1MarkCopy = new Todo(todo1);
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		assertEquals("Todo2", todo2, memory.get(todo2.getIndex()));
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
		memory.restoreHistoryState();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.get(todo1.getIndex()));
		memory.restoreFutureState();
		assertEquals("Todo1 Redo Mark", todo1MarkCopy, memory.get(todo1.getIndex()));
	}

	@Test (expected = NullPointerException.class)
	public void testRemoveUndo() throws StateUndefinedException {
		memory.remove(todo2.getIndex());
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		memory.get(todo2.getIndex()); // Exception
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
		memory.restoreHistoryState();
		assertEquals("Todo2", todo2, memory.get(todo2.getIndex()));
	}

	@Test
	public void testRemoveUndoRedo() throws StateUndefinedException {
		memory.remove(todo2.getIndex());
		memory.remove(todo1.getIndex());
		memory.restoreHistoryState();
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
		memory.restoreFutureState();
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
	}
	
	@Test (expected = NullPointerException.class)
	public void testAddUndo() throws StateUndefinedException {
		memory.restoreHistoryState();
		memory.get(todo3.getIndex()); // Exception
	}
	
	@Test
	public void testAddUndoRedo() throws StateUndefinedException {
		memory.restoreHistoryState();
		assertEquals("Todo1", todo1, memory.get(todo1.getIndex()));
		assertEquals("Todo2", todo2, memory.get(todo2.getIndex()));
		memory.restoreFutureState();
		assertEquals("Todo3", todo3, memory.get(todo3.getIndex()));
	}
}
