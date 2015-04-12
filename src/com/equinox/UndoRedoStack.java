package com.equinox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import com.equinox.exceptions.StateUndefinedException;

public class UndoRedoStack<T extends UndoableRedoable<T>> {
	private LinkedList<T> undoStack;
	private LinkedList<T> redoStack;
	private HashMap<Integer, T> memory;
	private IDBuffer<T> idBuffer;
	private int maxStates;
	
	public UndoRedoStack(HashMap<Integer, T> memory, IDBuffer<T> idBuffer, int maxStates) {
		this.undoStack = new LinkedList<T>();
		this.redoStack = new LinkedList<T>();
		this.memory = memory;
		this.idBuffer = idBuffer;
		this.maxStates = maxStates;
	}
		
	/**
	 * Saves the a copy of the state of a Todo into the undo stack. If the Todo
	 * specified is null, a placeholder is used instead.
	 * <p>
	 * The stack never contains null values. <br>
	 * If the maximum stack size is reached, the earliest state is discarded. <br>
	 * If the stack and memory no longer contains a particular Todo, its ID is
	 * returned to the pool of available indices.
	 * 
	 * @param toBeSaved the Todo to be saved.
	 */
	public void save(T toBeSaved) {
		T toBeSavedCopy = toBeSaved.copy();
		undoStack.add(toBeSavedCopy);
		
		// If undo stack has exceeded max size, discard earliest state.
		if (undoStack.size() > maxStates) {
			int id = undoStack.removeFirst().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}
	
	/**
	 * Restores the latest future state of the memory. Also known as the redo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no future states to restore
	 *             to.
	 */
	public T restoreFutureState() throws StateUndefinedException {
		T fromStack;
		try {
			fromStack = redoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_FUTURE_STATES);
		}

		int id = fromStack.getId();
		T inMemory = memory.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		save(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.isPlaceholder()) {
			memory.remove(id);
		} else {
			memory.put(id, fromStack);
		}
		return fromStack;
	}
	
	/**
	 * Restores the latest history state of the memory. Also known as the undo
	 * operation.
	 * 
	 * @throws StateUndefinedException if there are no history states to restore
	 *             to.
	 */
	public T restoreHistoryState() throws StateUndefinedException {
		T fromStack;
		try {
			fromStack = undoStack.removeLast();
		} catch (NoSuchElementException e) {
			throw new StateUndefinedException(
					ExceptionMessages.NO_HISTORY_STATES);
		}

		int id = fromStack.getId();
		T inMemory = memory.get(id);

		// If Todo does not exist in memory, use placeholder.
		if (inMemory == null) {
			inMemory = fromStack.getPlaceholder();
		}

		// Redo stack will not exceed maximum size.
		redoStack.add(inMemory);

		// If Todo from stack is a placeholder, delete Todo indicated by its
		// ID in the memory.
		if (fromStack.isPlaceholder()) {
			memory.remove(id);
		} else {
			memory.put(id, fromStack);
		}
		return fromStack;
	}
	
	public T peekHistoryState() {
		return undoStack.peekLast();
	}
	
	public T peekFutureState() {
		return redoStack.peekLast();
	}
	

	/**
	 * Flushes both undo and redo stacks. For use with exit command.
	 */
	public void flushStacks() {
		flushRedoStack();
		flushUndoStack();
	}

	/**
	 * Flushes the undoStack of all states of Todos.
	 */
	private void flushUndoStack() {
		while (!undoStack.isEmpty()) {
			int id = undoStack.pollLast().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}

	/**
	 * Flushes the redoStack of all states of Todos.
	 */
	public void flushRedoStack() {
		while (!redoStack.isEmpty()) {
			int id = redoStack.pollLast().getId();
			if (!memory.containsKey(id)) {
				idBuffer.put(id);
			}
		}
	}
}