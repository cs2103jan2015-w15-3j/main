import java.util.ArrayList;

public class Memory {
	// Accessible by members of the same package
	ArrayList<Todo> currentState;
	private ArrayList<ArrayList<Todo>> stateHistory;
	// For Redo
	// private ArrayList<ArrayList<Todo>> stateFuture;
	
	public Memory() {
		currentState = new ArrayList<Todo>();
	}
	
	public ArrayList<Todo> getCurrentState() {
		return currentState;
	}
	
	public void saveCurrentState() {
		stateHistory.add(currentState);
	}
	
	public void restoreLastState() {
		// Discard previous state
		currentState = stateHistory.remove(stateHistory.size() - 1);
	}

}
