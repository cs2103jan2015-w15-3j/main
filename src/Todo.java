import java.util.GregorianCalendar;

/**
 * @author Ikarus
 *
 */
public class Todo {

	public String title;
	public GregorianCalendar createdOn, modifiedOn, startTime, endTime;
	public boolean isDone;
	
	// Floating task
	public Todo(GregorianCalendar currentTime, String userTitle) {
		title = userTitle;
		createdOn = currentTime;
		modifiedOn = null;
		startTime = null;
		endTime = null;
		isDone = false;
	}
	
	// Deadline
	public Todo(GregorianCalendar currentTime, String userTitle, GregorianCalendar deadline) {
		title = userTitle;
		createdOn = currentTime;
		modifiedOn = null;
		startTime = null;
		endTime = deadline;
		isDone = false;
	}
	
	// Event
	public Todo(GregorianCalendar currentTime, String userTitle, GregorianCalendar start, GregorianCalendar end) {
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
