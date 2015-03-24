package com.equinox;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

/**
 * The RecurringTodoRule class contains the rules and methods for creating and
 * storing individual Todos
 * 
 * @author paradite
 *
 */
public class RecurringTodoRule {
    private Period DEFAULT_RECURRENCE_LIMIT_PERIOD = new Period(0).withYears(1);

    private int MAX_FUTURE_OCCURRENCE = 1;

    protected Period recurringInterval;
    protected int recurringId;
    private DateTime recurrenceLimit;

    private String name;
    private List<DateTime> dateTimes;

    private ArrayList<Todo> recurringTodos = new ArrayList<Todo>();

    /**
     * Constructor for the RecurringTodoRule without specifying limit
     * 
     * @param id
     * @param recurringId
     * @param name
     * @param dateTimes
     * @param period
     */
    public RecurringTodoRule(int id, int recurringId, String name,
            List<DateTime> dateTimes, Period period) {
        super();
        this.name = name;
        this.dateTimes = dateTimes;
        this.recurringInterval = period;
        this.recurringId = recurringId;
        this.recurrenceLimit = new DateTime()
                .plus(DEFAULT_RECURRENCE_LIMIT_PERIOD);
    }

    /**
     * Constructor for the RecurringTodoRule with limit
     * 
     * @param id
     * @param recurringId
     * @param name
     * @param dateTimes
     * @param period
     * @param limit
     */
    public RecurringTodoRule(int id, int recurringId, String name,
            List<DateTime> dateTimes, Period period, DateTime limit) {
        super();
        this.name = name;
        this.dateTimes = dateTimes;
        this.recurringInterval = period;
        this.recurringId = recurringId;
        this.recurrenceLimit = limit;
    }

    public String getName() {
        return name;
    }

    public List<DateTime> getDateTimes() {
        return dateTimes;
    }

    public ArrayList<Todo> getRecurringTodos() {
        return recurringTodos;
    }

    public Period getRecurringInterval() {
        return recurringInterval;
    }

    public int getRecurringId() {
        return recurringId;
    }

    public DateTime getRecurrenceLimit() {
        return recurrenceLimit;
    }

    /**
     * Update the list of Todos stored in the rule
     * 
     * @return
     */
    public int updateTodoList(int currentID) {
        if (recurringTodos.isEmpty()) {
            recurringTodos.add(new Todo(currentID, name, dateTimes));
        }

        Todo lastTodo = recurringTodos.get(recurringTodos.size() - 1);
        DateTime now = new DateTime();
        while (lastTodo.getStartTime().compareTo(now) < 0) {
            updateDateTime();

            Todo newTodo = new Todo(currentID, name, dateTimes);
            recurringTodos.add(newTodo);
            lastTodo = recurringTodos.get(recurringTodos.size() - 1);
        }

        return recurringTodos.size();
    }

    private void updateDateTime() {
        for (DateTime dateTime : dateTimes) {
            if (dateTime != null) {
                dateTime = dateTime.plus(recurringInterval);
            }
        }

    }

}
