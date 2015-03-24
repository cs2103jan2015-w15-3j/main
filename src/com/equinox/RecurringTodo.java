package com.equinox;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.equinox.exceptions.DateUndefinedException;

public class RecurringTodo extends Todo {
    private Period recurringPeriod;
    protected final int recurringId;

    /**
     * Constructs a Recurring Todo of type: DEADLINE or EVENT
     * 
     * @param id the ID of the Todo.
     * @param recurringId the RecurringID of the Todo.
     * @param name name of the task.
     * @param dateTimes a List of DateTimes specifying the end and/or start times.
     */
    public RecurringTodo(int id, int recurringId, String name,
            List<DateTime> dateTimes, Period period) {
        // Pass in the constructor for normal event and deadline
        super(id, name, dateTimes);
        this.recurringId = recurringId;
        this.recurringPeriod = period;

    }
    
    /**
     * Makes an exact copy of another Todo.
     * 
     * @param todo the Todo to be copied.
     */
    protected RecurringTodo(RecurringTodo todo) {
        super(todo);
        this.recurringId = todo.recurringId;
        this.recurringPeriod = todo.recurringPeriod;
    }

    @Override
    public String toString() {
        return this.recurringId + " " + this.recurringPeriod.toString();
    }

    /**
     * Returns the name of the Todo.
     * 
     * @return the name of the Todo.
     */
    @Override
    public String getName() {
        return "Recurring: " + name;
    }

    /**
     * Returns the RecurringID of the Todo.
     * 
     * @return the RecurringID of the Todo.
     */
    public int getRecurringId() {
        return recurringId;
    }

    public static void main(String[] args) throws DateUndefinedException {
        Todo recurringTodo = new RecurringTodo(2, 0, "first Recur",
                Parser.parseDates("tuesday"), new Period(0).withDays(2));

        System.out.println(recurringTodo.getName());
        System.out.println(recurringTodo.toString());

    }

}
