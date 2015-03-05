package com.equinox;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Stores all Todos and keeps state information allowing Undo and Redo
 * operations. Maximum number of states that can be stored by Memory is
 * {@value #STACK_MAX_SIZE}.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
public class Memory {
	private static int STACK_MAX_SIZE = 5;
	private HashMap<Integer, Todo> memoryMap;
	private LinkedList<Todo> undoStack;
	private LinkedList<Todo> redoStack;

	/**
	 * Constructs an empty Memory object.
	 */
	public Memory() {
		this.memoryMap = new HashMap<Integer, Todo>();
		this.undoStack = new LinkedList<Todo>();
		this.redoStack = new LinkedList<Todo>();
	}

	/**
	 * Adds the specified Todo to memory. The current state is saved prior to any operation.
	 * 
	 * @param todo	the Todo to be added.
	 */
	public void add(Todo todo) {
		save(todo.getPlaceholder());
		memoryMap.put(todo.getIndex(), todo);
	}

	/**
	 * Retrieves the Todo identified by the specified index from the memory.
	 * 
	 * @param index	the index of the Todo to be retrieved.
	 * @return	the Todo object identified by the specified index.
	 */
	public Todo get(int index) { // TODO Handle case where todo indicated by index does not exist.
		return memoryMap.get(index);
	}

	/**
	 * Retrieves the Todo identified by the specified index from the memory for
	 * editing. The current state is saved prior to any operation.
	 * 
	 * @param index the index of the Todo to be retrieved.
	 * @return the Todo object identified by the specified index.
	 */
	public Todo setterGet(int index) { // TODO Handle case where todo indicated by index does not exist.
		save(memoryMap.get(index));
		return memoryMap.get(index);
	}

	/**
	 * Returns the current size of the memory in terms of number of Todos.
	 * 
	 * @return the current number of Todos in the memory.
	 */
	public int size() {
		return memoryMap.size();
	}

	/**
	 * Removes the Todo identified by the specified index from the memory. The
	 * current state is saved prior to any operation.
	 * 
	 * @param index the index of the Todo to be removed.
	 */
	public void remove(int index) { // TODO Handle case where todo indicated by index does not exist.
		save(memoryMap.get(index));
		memoryMap.remove(index);
	}

	/**
	 * Saves the state of a Todo into the undo stack.
	 * 
	 * @param toBeSaved the Todo to be saved.
	 */
	private void save(Todo toBeSaved) {
		if(toBeSaved == null) {
			undoStack.add(null);
		} else {
			Todo toBeSavedCopy = new Todo(toBeSaved);
			if (undoStack.size() > STACK_MAX_SIZE) {
				undoStack.removeFirst();
			}
			undoStack.add(toBeSavedCopy);
		}
	}

	/**
	 * Restores the latest history state of the memory. Also known as the undo operation.
	 * 
	 * @throws StateUndefinedException	if there are no history states to restore to.
	 */
	public void restoreHistoryState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No history states exist.");
		}
		
		int index = fromStack.getIndex();
		Todo inMemory = memoryMap.get(index);
		
		if(inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}
		
		if (redoStack.size() > STACK_MAX_SIZE) {
			redoStack.removeFirst();
		}
		redoStack.add(inMemory);
		if(fromStack.getCreatedOn() == null) {
			memoryMap.put(index, null);
		} else {
			memoryMap.put(index, fromStack);
		}
	}

	/**
	 * Restores the latest future state of the memory. Also known as the redo operation.
	 * 
	 * @throws StateUndefinedException if there are no future states to restore to.
	 */
	public void restoreFutureState() throws StateUndefinedException {
		Todo fromStack;
		try {
			fromStack = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No future states exist.");
		}
		
		int index = fromStack.getIndex();
		Todo inMemory = memoryMap.get(index);
		
		if(inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		} 
		
		if (undoStack.size() > STACK_MAX_SIZE) {
			undoStack.removeFirst();
		}
		undoStack.add(inMemory);
		
		if (fromStack.getCreatedOn() == null) {
			memoryMap.put(index, null);
		} else {
			memoryMap.put(index, fromStack);
		}
	}

	/**
	 * Method to get all the Todos for displaying purposes
	 * 
	 * @return all Todos as Collection
	 */
	public Collection<Todo> getAllTodos() {
		return memoryMap.values();
	}
}
