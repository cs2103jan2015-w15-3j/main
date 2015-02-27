import org.joda.time.DateTime;

/**
 * @author Ikarus
 *
 */
public class Todo {

	public String title;
	public DateTime createdOn, modifiedOn, startTime, endTime;
	public boolean isDone;
	
	// Floating task
	public Todo(DateTime currentTime, String userTitle) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
	}
	
	// Deadline
	public Todo(DateTime currentTime, String userTitle, DateTime deadline) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = deadline;
		this.isDone = false;
	}
	
	// Event
	public Todo(DateTime currentTime, String userTitle, DateTime start, DateTime end) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = start;
		this.endTime = end;
		this.isDone = false;
	}
	
	// Copy of todo
	public Todo(Todo todo) {
		this.title = todo.title;
		this.createdOn = todo.createdOn;
		this.modifiedOn = todo.modifiedOn;
		this.startTime = todo.startTime;
		this.endTime = todo.endTime;
		this.isDone = todo.isDone;
	}

	public String toString() {
		return (modifiedOn.toString() + " " + createdOn.toString() + " " + title + " " + startTime.toString() + " " + endTime.toString() + " " + isDone);
	}
}
