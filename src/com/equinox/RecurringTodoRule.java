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
    private String originalName;

    private List<DateTime> dateTimes;

    private ArrayList<Todo> recurringTodos = new ArrayList<Todo>();

    private String RECURRING_TODO_PREIX = "(Recurring) ";

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
        this.originalName = name;
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
     * @return the number of new Todos created due to the update
     */
    public int updateTodoList(Memory memory) {
        int currentID;
        int newTodoCount = 0;
        if (recurringTodos.isEmpty()) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes, recurringId);
            addRecurringTodo(memory, newTodo);
            newTodoCount++;
        }

        DateTime now = new DateTime();
        DateTime nextOccurrence = now.plus(getRecurringInterval());
        // Update until next occurrence or the limit, whichever is earlier
        DateTime updateLimit = nextOccurrence;
        if (nextOccurrence.compareTo(getRecurrenceLimit()) > 0) {
            updateLimit = getRecurrenceLimit();
        }

        // updateDateTime();
        while (getDateTime().plus(recurringInterval)
                .compareTo(updateLimit) <= 0) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes, recurringId);
            addRecurringTodo(memory, newTodo);
            newTodoCount++;
            updateDateTime();
        }

        return newTodoCount;
    }

    public void setRecurringInterval(Period recurringInterval) {
        this.recurringInterval = recurringInterval;
    }

    public void setDateTimes(List<DateTime> dateTimes) {
        this.dateTimes = dateTimes;
    }

    private void addRecurringTodo(Memory memory, Todo newTodo) {
        recurringTodos.add(newTodo);
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
        return String.format(recurringTodoStringFormat, originalName);
    }

    /**
     * Method to return a DateTime of the Recurring rule's last occurrence. The
     * order of preference: start time > end time > null
     * 
     * @author paradite
     * 
     * @return start time for events; end time for deadlines; null for tasks.
     */
    public DateTime getDateTime() {
        if (dateTimes.get(0) != null) {
            return dateTimes.get(0);
        } else if (dateTimes.get(1) != null) {
            return dateTimes.get(1);
        } else {
            return null;
        }
    }
}
