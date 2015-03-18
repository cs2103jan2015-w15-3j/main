package com.equinox;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Stores parameters of a Todo using org.joda.time.DateTime objects. A Todo can
 * be subdivided into 3 different subtypes namely Task, Deadline, or Event,
 * which is uniquely determined at construction by the availability of
 * parameters. Todos are uniquely specified identifier known as ID until
 * their deletion, upon which the ID may be recycled.
 * 
 * @author Ho Wei Li || IkarusWill
 *
 */
/**
 * @author Ikarus
 *
 */
public class Todo{
	
	public enum TYPE {
		TASK, DEADLINE, EVENT;
	}

	private final int id;
	private final Integer recurringId;
	private String name;
	private final DateTime createdOn;
	private DateTime modifiedOn, startTime, endTime;
	private boolean isDone;
	private TYPE type;

    private static final DateTimeFormatter DateFormatter = DateTimeFormat
            .forPattern("dd MMM");
    private static final DateTimeFormatter TimeFormatter = DateTimeFormat
            .forPattern("HH:mm");
    private static final String DateTimeStringFormat = "%1$s at %2$s";

    private static final String EventStringFormat = "Event \"%1$s\" from %2$s to %3$s";

    private static final String DeadlineStringFormat = "Deadline \"%1$s\" by %2$s";

    private static final String FloatingTaskStringFormat = "Floating task \"%1$s\"";
	
	/**
	 * Constructs a Todo of type: TASK.
	 * 
	 * @param id the ID of the Todo.
	 * @param name name of the task.
	 */
	public Todo(int id, String name) {
		this.id = id;
		this.recurringId = null;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = TYPE.TASK;
	}
	
	/**
	 * Constructs a Todo of type: DEADLINE or EVENT.
	 * 
	 * ASSUMPTION: dateTimeList is not empty or null and has only either 1 or 2
	 * dates.
	 * 
	 * @param id the ID of the Todo.
	 * @param name name of the task.
	 * @param dateTimes a List of DateTimes specifying the end and/or start times.
	 */
	public Todo(int id, String name, List<DateTime> dateTimes) {
		this.id = id;
		this.recurringId = null;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.isDone = false;
		if(dateTimes.size() == 1) {
			this.startTime = null;
			this.endTime = dateTimes.get(0);
			this.type = TYPE.DEADLINE;
		} else if(dateTimes.size() == 2) {
			this.startTime = dateTimes.get(0);
			this.endTime = dateTimes.get(1);
			this.type = TYPE.EVENT;
		}
	}
	
	/**
	 * Constructs a Recurring Todo of type: DEADLINE or EVENT
	 * 
	 * @param id the ID of the Todo.
	 * @param recurringId the RecurringID of the Todo.
	 * @param name name of the task.
	 * @param dateTimes a List of DateTimes specifying the end and/or start times.
	 */
	public Todo(int id, int recurringId, String name, List<DateTime> dateTimes) {
		this.id = id;
		this.recurringId = recurringId;
		this.name = name;
		this.createdOn = new DateTime();
		this.modifiedOn = this.createdOn;
		this.isDone = false;
		if(dateTimes.size() == 1) {
			this.startTime = null;
			this.endTime = dateTimes.get(0);
			this.type = TYPE.DEADLINE;
		} else if(dateTimes.size() == 2) {
			this.startTime = dateTimes.get(0);
			this.endTime = dateTimes.get(1);
			this.type = TYPE.EVENT;
		}
	}

	/**
	 * Makes an exact copy of another Todo.
	 * 
	 * @param todo the Todo to be copied.
	 */
	protected Todo(Todo todo) {
		this.id = todo.id;
		this.recurringId = todo.recurringId;
		this.name = todo.name;
		this.createdOn = todo.createdOn;
		this.modifiedOn = todo.modifiedOn;
		this.startTime = todo.startTime;
		this.endTime = todo.endTime;
		this.isDone = todo.isDone;
		this.type = todo.type;
	}
	
	/**
	 * Constructs a placeholder Todo with null fields except the ID. To be
	 * used by Memory class in its stacks for undo/redo operations.
	 * 
	 * @param id the ID of the Todo that was removed from Memory.
	 */
	private Todo(int id) {
		this.id = id;
		this.recurringId = null;
		this.name = null;
		this.createdOn = null;
		this.modifiedOn = null;
		this.startTime = null;
		this.endTime = null;
		this.isDone = false;
		this.type = null;
	}

	/**
	 * Returns the ID of the Todo.
	 * 
	 * @return the ID of the Todo.
	 */
	public int getId() {
		return id;
	}
	

	/**
	 * Returns the RecurringID of the Todo.
	 * 
	 * @return the RecurringID of the Todo.
	 */
	public Integer getRecurringId() {
		return recurringId;
	}
	
	/**
	 * Checks if the Todo recurs.
	 * 
	 * @return true if the Todo recurs.
	 */
	public boolean isRecurring() {
		if(recurringId == null) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the name of the Todo.
	 * 
	 * @return the name of the Todo.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Replaces the title with the specified String and updates the last
	 * modified time.
	 * 
	 * @param title the new title of the Todo.
	 */
	public void setName(String title) {
		this.name = title;
		modifiedOn = new DateTime();
	}

	/**
	 * Returns the start time of the Todo.
	 * 
	 * @return the start time of the Todo.
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * Replaces the start time with the date encoded in the specified
	 * startTimeString and updates the last modified time.
	 * 
	 * @param startTime DateTime of the new startTime
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
		modifiedOn = new DateTime();
	}

	/**
	 * Returns the end time of the Todo.
	 * 
	 * @return the end time of the Todo.
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Replaces the end time with the specified DateTime and updates the last
	 * modified field of the Todo.
	 * 
	 * @param endTime String containing the new end time of the Todo.
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
		modifiedOn = new DateTime();
	}

	/**
	 * Checks if the Todo is marked as done.
	 * 
	 * @return true if the Todo has been marked as done, false otherwise.
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * Marks the Todo as done or undone and updates the last modified time.
	 * 
	 * @param isDone the new status of the Todo.
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
		modifiedOn = new DateTime();
	}

	/**
	 * Returns the time at which the Todo was created.
	 * 
	 * @return the time at which the Todo was created.
	 */
	public DateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * Returns the time at which the Todo was last modified.
	 * 
	 * @return the time at which the Todo was last modified.
	 */
	public DateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * Returns the type of the Todo as specified by Todo.TYPE.
	 * 
	 * @return returns the type of the Todo.
	 */
	public TYPE getType() {
		return type;
	}
	
	/**
	 * Returns the placeholder Todo constructed from the ID of this Todo. 
	 * For use in Undo and Redo stacks in Memory.
	 * 
	 */
	protected Todo getPlaceholder() {
		return new Todo(id);
	}
	
	/**
	 * Checks if the Todo has valid parameters and type. Specifically, checks
	 * for the presence of startTime and endTime and sets the type accordingly.
	 * If both are present, startTime is checked to determine if it elapses
	 * before endTime. Used in AddCommand and EditCommand operations.
	 * 
	 * @return true if Todo has valid parameters and type, false otherwise.
	 */
	protected boolean isValid() {
		if(startTime != null && endTime != null) {
			type = TYPE.EVENT;
			if(endTime.isBefore(startTime)) {
				return false;
			}
		} else if (startTime != null && endTime == null) {
			endTime = startTime;
			startTime = null;
			type = TYPE.DEADLINE;
		} else if(startTime == null && endTime != null) {
			type = TYPE.DEADLINE;
		} else if(startTime == null && endTime == null) {
			type = TYPE.TASK;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
        String endDateTimeString = formatDateTime(endTime);
        String startDateTimeString = formatDateTime(startTime);

        switch (type) {
            case TASK :
                return String.format(FloatingTaskStringFormat, name);
            case DEADLINE :
                return String.format(DeadlineStringFormat, name,
                        endDateTimeString);
            case EVENT :
                return String.format(EventStringFormat, name,
                        startDateTimeString, endDateTimeString);
            default :
                return "";
        }
    }

	// TODO: Refactor out of Todo.
    private String formatDateTime(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String dateString = DateFormatter.print(dateTime);
        String timeString = TimeFormatter.print(dateTime);
        return String.format(DateTimeStringFormat, dateString, timeString);
    }

    /**
     * Method to return a DateTime of the Todo for ordering them chronologically
     * The order of preference: start time > end time > null
     * 
     * @author paradite
     * 
     * @return start time for events; end time for deadlines; null for tasks.
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
     * Method to compare two DateTime objects at the minute resolution
     * 
     * @author Jonathan Lim Siu Chi || ign3sc3nc3
     * 
     * @return -1 if the first object is smaller (earlier), 0 if the two objects are equal, 1 if the first object is larger(later).
     */
    public int compareDateTime(DateTime first, DateTime second){
    	DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfDay());
    	return comparator.compare(first, second);
    }
	
	/**
	 * Overriding the equals method. 
	 * Compares the title, startTime, endTime and isDone parameters between this Todo object
	 * and the other Todo object being compared to.
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
				if(!this.name.equals(other.name)){
					return false;
				}
				
		//Comparing isDone
				if(this.isDone != other.isDone){
					return false;
				}
				
		return true;
	}
}
