package com.equinox;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Memory {
	private static int STACK_MAX_SIZE = 5;
	private HashMap<Integer, Todo> memoryMap;
	private LinkedList<Todo> undoStack;
	private LinkedList<Todo> redoStack;

	public Memory() {
		this.memoryMap = new HashMap<Integer, Todo>();
		this.undoStack = new LinkedList<Todo>();
		this.redoStack = new LinkedList<Todo>();
	}

	public void add(Todo todo) {
		save(todo.getPlaceholder());
		memoryMap.put(todo.getIndex(), todo);
	}

	public Todo get(int index) { // TODO Handle case where todo indicated by index does not exist.
		return memoryMap.get(index);
	}
	
	public Todo setterGet(int index) { // TODO Handle case where todo indicated by index does not exist.
		save(memoryMap.get(index));
		return memoryMap.get(index);
	}

	public int size() {
		return memoryMap.size();
	}

	public void remove(int index) { // TODO Handle case where todo indicated by index does not exist.
		save(memoryMap.get(index));
		memoryMap.remove(index);
	}

	public void save(Todo toBeSaved) {
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
	 * Method to get all the todos for displaying purposes
	 * 
	 * @return all todos as ArrayList<Todo>
	 */
	public Collection<Todo> getAllTodos() {
		return memoryMap.values();
	}
}
