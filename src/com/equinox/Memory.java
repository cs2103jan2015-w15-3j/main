package com.equinox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Memory {
	private static int STACK_MAX_SIZE = 5;
	private ArrayList<Todo> currentState;
	private LinkedList<ArrayList<Todo>> undoStack;
	private LinkedList<ArrayList<Todo>> redoStack;

	public Memory() {
		this.currentState = new ArrayList<Todo>();
		this.undoStack = new LinkedList<ArrayList<Todo>>();
		this.redoStack = new LinkedList<ArrayList<Todo>>();
	}

	public void add(Todo todo) {
		currentState.add(todo);
	}

	public Todo get(int userIndex) {
		return currentState.get(--userIndex);
	}

	public int size() {
		return currentState.size();
	}

	public int stackSize() {
		return undoStack.size();
	}

	public void remove(int userIndex) {
		currentState.remove(--userIndex);
	}

	public void saveCurrentState() {
		ArrayList<Todo> currentStateCopy = deepCopyCurrentState();
		if (undoStack.size() > STACK_MAX_SIZE) {
			undoStack.removeFirst();
		}
		undoStack.add(currentStateCopy);
	}

	/**
	 * @return
	 */
	private ArrayList<Todo> deepCopyCurrentState() {
		ArrayList<Todo> currentStateCopy = new ArrayList<Todo>();
		for (Todo todo : currentState) {
			currentStateCopy.add(new Todo(todo));
		}
		return currentStateCopy;
	}

	public void restoreHistoryState() throws StateUndefinedException {
		ArrayList<Todo> currentStateCopy = deepCopyCurrentState();
		if (redoStack.size() > STACK_MAX_SIZE) {
			redoStack.removeFirst();
		}
		redoStack.add(currentStateCopy);

		try {
			currentState = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No history states exist.");
		}
	}

	public void restoreFutureState() throws StateUndefinedException {
		saveCurrentState();
		try {
			currentState = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException("No future states exist.");
		}
	}

	/**
	 * Method to get all the todos for displaying purposes
	 * 
	 * @return all todos as ArrayList<Todo>
	 */
	public ArrayList<Todo> getAllTodos() {
		return currentState;
	}
}
