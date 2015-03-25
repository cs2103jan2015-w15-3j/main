package com.equinox;

import java.util.HashMap;
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

    private HashMap<Integer, Todo> recurringTodos = new HashMap<Integer, Todo>();

    private String RECURRING_TODO_PREIX = "*R*";

    protected static final String recurringTodoStringFormat = "Recurring todos \"%1$s\"";

    /**
     * Constructor for the RecurringTodoRule without specifying limit
     * 
     * @param id
     * @param recurringId
     * @param name
     * @param dateTimes
     * @param period
     */
    public RecurringTodoRule(int recurringId, String name,
            List<DateTime> dateTimes, Period period) {
        super();
        this.name = RECURRING_TODO_PREIX + name;
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
    public RecurringTodoRule(int recurringId, String name,
            List<DateTime> dateTimes, Period period, DateTime limit) {
        super();
        this.name = RECURRING_TODO_PREIX + name;
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

    public HashMap<Integer, Todo> getRecurringTodos() {
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
     * @return the number of new Todos created due to the update
     */
    public int updateTodoList(Memory memory) {
        int currentID;
        int newTodoCount = 0;
        if (recurringTodos.isEmpty()) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes);
            addRecurringTodo(memory, currentID, newTodo);
            newTodoCount++;
        }

        Todo lastTodo = recurringTodos.get(recurringTodos.size() - 1);
        DateTime now = new DateTime();
        DateTime nextOccurrence = now.plus(getRecurringInterval());
        // Update until next occurrence or the limit, whichever is earlier
        DateTime updateLimit = nextOccurrence;
        if (nextOccurrence.compareTo(getRecurrenceLimit()) > 0) {
            updateLimit = getRecurrenceLimit();
        }

        updateDateTime();
        while (lastTodo.getDateTime().plus(recurringInterval)
                .compareTo(updateLimit) <= 0) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes);
            addRecurringTodo(memory, currentID, newTodo);
            newTodoCount++;
            lastTodo = recurringTodos.get(recurringTodos.size() - 1);
            updateDateTime();
        }

        return newTodoCount;
    }

    private void addRecurringTodo(Memory memory, int currentID, Todo newTodo) {
        recurringTodos.put(currentID, newTodo);
        memory.add(newTodo);
    }

    private void updateDateTime() {
        for (int i = 0; i < dateTimes.size(); i++) {
            if (dateTimes.get(i) != null) {
                dateTimes.set(i, dateTimes.get(i).plus(recurringInterval));
            }
        }
    }

    public String toString() {
        return String.format(recurringTodoStringFormat, name);
    }

}
