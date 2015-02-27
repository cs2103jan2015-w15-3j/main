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
		title = userTitle;
		createdOn = currentTime;
		modifiedOn = null;
		startTime = null;
		endTime = null;
		isDone = false;
	}
	
	// Deadline
	public Todo(DateTime currentTime, String userTitle, DateTime deadline) {
		title = userTitle;
		createdOn = currentTime;
		modifiedOn = null;
		startTime = null;
		endTime = deadline;
		isDone = false;
	}
	
	// Event
	public Todo(DateTime currentTime, String userTitle, DateTime start, DateTime end) {
		title = userTitle;
		createdOn = currentTime;
		modifiedOn = null;
		startTime = start;
		endTime = end;
		isDone = false;
	}

	public String toString() {
		return (modifiedOn.toString() + " " + createdOn.toString() + " " + title + " " + startTime.toString() + " " + endTime.toString() + " " + isDone);
	}
}
