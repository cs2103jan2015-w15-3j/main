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
		memoryMap.put(todo.getIndex(), todo);
	}

	public Todo get(int index) {
		return memoryMap.get(index);
	}

	public int size() {
		return memoryMap.size();
	}

	public void remove(int index) {
		memoryMap.remove(index);
	}

	public void save(Todo toBeSaved) {
		Todo toBeSavedCopy = new Todo(toBeSaved);
		if (undoStack.size() > STACK_MAX_SIZE) {
			undoStack.removeFirst();
		}
		undoStack.add(toBeSavedCopy);
	}

	public void restoreHistoryState() throws StateUndefinedException {
		Todo toReplace;
		try {
			toReplace = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No history states exist.");
		}
		
		int index = toReplace.getIndex();
		Todo toBeReplaced = memoryMap.get(index);
		
		if (redoStack.size() > STACK_MAX_SIZE) {
			redoStack.removeFirst();
		}
		redoStack.add(toBeReplaced);
		memoryMap.put(index, toReplace);
	}

	public void restoreFutureState() throws StateUndefinedException {
		Todo toReplace;
		try {
			toReplace = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No future states exist.");
		}
		
		int index = toReplace.getIndex();
		Todo toBeReplaced = memoryMap.get(index);
		
		if (undoStack.size() > STACK_MAX_SIZE) {
			undoStack.removeFirst();
		}
		undoStack.add(toBeReplaced);
		memoryMap.put(index, toReplace);
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
