import java.util.ArrayList;

public class Memory {
	// Accessible by members of the same package
	private ArrayList<Todo> currentState;
	private ArrayList<ArrayList<Todo>> stateHistory;
	// For Redo
	// private ArrayList<ArrayList<Todo>> stateFuture;
	
	public Memory() {
		currentState = new ArrayList<Todo>();
	}
	
	public void add(Todo todo) {
		// Create a copy of currentState
		currentState = new ArrayList<Todo>(currentState);
		currentState.add(todo);
		stateHistory.add(currentState);
	}
	
	public Todo get(int userIndex) {
		return currentState.get(--userIndex);
	}
	
	public void saveCurrentState() {
		stateHistory.add(currentState);
	}
	
	public void restoreLastState() {
		// Discard previous state
		currentState = stateHistory.remove(stateHistory.size() - 1);
	}

}
