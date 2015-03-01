package com.equinox;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;

/**
 * @author Ikarus
 *
 */
public class Todo {

	protected String title;
	protected DateTime createdOn, modifiedOn, startTime, endTime;
	protected boolean isDone;
	
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
	protected Todo(Todo todo) {
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
