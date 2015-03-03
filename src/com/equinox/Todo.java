package com.equinox;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

/**
 * @author Ikarus
 *
 */
public class Todo {
	
	private enum TYPE {
		TASK, DEADLINE, EVENT;
	}

	private String title;
	private DateTime createdOn, modifiedOn, startTime, endTime;
	private boolean isDone;
	private TYPE type;

	// Floating task
	public Todo(DateTime currentTime, String userTitle) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = TYPE.TASK;
	}
	
	// Deadline
	public Todo(DateTime currentTime, String userTitle, DateTime deadline) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = deadline;
		this.isDone = false;
		this.type = TYPE.DEADLINE;
	}
	
	// Event
	public Todo(DateTime currentTime, String userTitle, DateTime start, DateTime end) {
		this.title = userTitle;
		this.createdOn = currentTime;
		this.modifiedOn = null;
		this.startTime = start;
		this.endTime = end;
		this.isDone = false;
		this.type = TYPE.EVENT;
	}
	
	// Copy of todo
	protected Todo(Todo todo) {
		this.title = todo.title;
		this.createdOn = todo.createdOn;
		this.modifiedOn = todo.modifiedOn;
		this.startTime = todo.startTime;
		this.endTime = todo.endTime;
		this.isDone = todo.isDone;
		this.type = todo.type;
	}
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		modifiedOn = new DateTime();
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
		modifiedOn = new DateTime();
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
		modifiedOn = new DateTime();
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
		modifiedOn = new DateTime();
	}

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public DateTime getModifiedOn() {
		return modifiedOn;
	}

	public TYPE getType() {
		return type;
	}

	public String toString() {
		StringBuilder taskStringBuilder = new StringBuilder(modifiedOn.toString() + " " + createdOn.toString() + " " + title);
		
		switch(type) {
		case TASK:
			return taskStringBuilder.toString();
		case DEADLINE:
			taskStringBuilder.append(" " + endTime);
			return taskStringBuilder.toString();
		case EVENT:
			taskStringBuilder.append(" " + startTime + " " + endTime);
			return taskStringBuilder.toString();
		}
		return (modifiedOn.toString() + " " + createdOn.toString() + " " + title + " " + startTime.toString() + " " + endTime.toString() + " " + isDone);
	}

    /**
     * Method to return a DateTime of the todo for ordering them chronologically
     * 
     * The order of preference: start time > end time > null
     * 
     * For events, start time will be returned
     * 
     * For deadlines, end time will be returned
     * 
     * For floating todos, null will be returned
     * 
     * @author paradite
     * 
     * @return DateTime object
     */
    public DateTime getDateTime() {
        if (this.startTime != null) {
            return this.startTime;
        } else if (this.endTime != null) {
            return this.endTime;
        } else {
            return null;
        }

    }
	
	/**
	 * Overriding the equals method. 
	 * Compares the title, startTime, endTime and isDone parameters between this Todo object
	 * and the other Todo object being compared to.
	 * 
	 * Returns true if the parameters being compared to are equal, false otherwise.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(this.getClass() != obj.getClass()){
			return false;
		}
		final Todo other = (Todo) obj;
		
		//Construct a DateTimeComparator comparing DateTime objects at the minute resolution.
		//If the argument passed into the compare method is null, it will be treated as DateTime.now
		//Thus null checks must be done beforehand
		DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfDay());
		
		//Comparing startTime. If it is null in both objects, treat as equal.
		if((this.startTime == null) && (other.startTime != null)){
			return false;
		}
		else if((this.startTime != null) && (other.startTime == null)){
			return false;
		}
		else if((this.startTime != null) && (other.startTime != null)){
			if(comparator.compare(this.startTime, other.startTime) != 0){
				return false;
			}
		}
				
		//Comparing endTime. If it is null in both objects, treat as equal.
		if((this.endTime == null) && (other.endTime != null)){
			return false;
		}
		else if((this.endTime != null) && (other.endTime == null)){
			return false;
		}
		else if((this.endTime != null) && (other.endTime != null)){
			if(comparator.compare(this.endTime, other.endTime) != 0){
				return false;
			}
		}
		
		//Comparing title
				if(!this.title.equals(other.title)){
					return false;
				}
				
		//Comparing isDone
				if(this.isDone != other.isDone){
					return false;
				}
				
		return true;
	}
}
