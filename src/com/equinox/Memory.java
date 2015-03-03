package com.equinox;
import java.util.ArrayList;

public class Memory {
	private static int STACK_MAX_SIZE = 5;
	private ArrayList<Todo> currentState;
	private ArrayList<ArrayList<Todo>> undoStack;
	private ArrayList<ArrayList<Todo>> redoStack;
	private int undoStackIndex;
	private int redoStackIndex;

	public Memory() {
		this.currentState = new ArrayList<Todo>();
		this.undoStack = new ArrayList<ArrayList<Todo>>();
		this.redoStack = new ArrayList<ArrayList<Todo>>();
		this.undoStackIndex = 0;
		this.redoStackIndex = 0;
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
	
	public int stackSize(){
		return undoStack.size();
	}
	
	public void remove(int userIndex) {
		currentState.remove(--userIndex);
	}

	public void saveCurrentState() {
		ArrayList<Todo> currentStateCopy = deepCopyCurrentState();
		if(undoStack.size() <= STACK_MAX_SIZE) {
			undoStack.add(currentStateCopy);
		} else {
			undoStack.set(undoStackIndex, currentStateCopy);
		}	
		undoStackIndex = incrementModulo(undoStackIndex);
	}

	/**
	 * @return
	 */
	private ArrayList<Todo> deepCopyCurrentState() {
		ArrayList<Todo> currentStateCopy = new ArrayList<Todo>();
		for(Todo todo: currentState) {
			currentStateCopy.add(new Todo(todo));
		}
		return currentStateCopy;
	}

	public void restoreHistoryState() {
		ArrayList<Todo> currentStateCopy = deepCopyCurrentState();
		if(redoStack.size() <= STACK_MAX_SIZE) {
			redoStack.add(currentStateCopy);
		} else {
			redoStack.set(redoStackIndex, currentStateCopy);
		}
		redoStackIndex = incrementModulo(redoStackIndex);
		undoStackIndex = decrementModulo(undoStackIndex);
		
		currentState = undoStack.get(undoStackIndex);
	}
	
	public void restoreFutureState() {
		saveCurrentState();
		redoStackIndex = decrementModulo(redoStackIndex);
		currentState = redoStack.get(redoStackIndex);
	}

    /**
     * Method to get all the todos for displaying purposes
     * 
     * @return all todos as ArrayList<Todo>
     */
    public ArrayList<Todo> getAllTodos() {
        return currentState;
    }
    
    private static int incrementModulo(int value) {
    	value++;
    	value %= STACK_MAX_SIZE;
    	return value;
    }
    
    private static int decrementModulo(int value) {
    	value--;
    	if(value < 0) {
    		value += STACK_MAX_SIZE;
    	}
    	return value;
    }
}
