package com.equinox;
import java.util.ArrayList;

public class Memory {
	// Accessible by members of the same package
	private ArrayList<Todo> currentState;
	private ArrayList<ArrayList<Todo>> stateHistory;

	// For Redo
	// private ArrayList<ArrayList<Todo>> stateFuture;

	public Memory() {
		currentState = new ArrayList<Todo>();
		stateHistory = new ArrayList<ArrayList<Todo>>();
	}

	public void add(Todo todo) {
		saveCurrentState();
		currentState.add(todo);
	}

	public Todo get(int userIndex) {
		return currentState.get(--userIndex);
	}
	
	public int getCurrentSize() {
		return currentState.size();
	}

	public void set(int userIndex, Todo todo) {
		saveCurrentState();
		currentState.set(--userIndex, todo);
	}
	
	public void remove(int userIndex) {
		saveCurrentState();
		currentState.remove(--userIndex);
	}

	public void saveCurrentState() {
		ArrayList<Todo> currentStateCopy = new ArrayList<Todo>();
		for(Todo todo: currentState) {
			currentStateCopy.add(new Todo(todo));
		}
		stateHistory.add(currentStateCopy);
	}
	
	public int getStateHistorySize(){
		return stateHistory.size();
	}

	public void restoreLastState() {
		// Discard previous state
		currentState = stateHistory.remove(stateHistory.size() - 1);
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