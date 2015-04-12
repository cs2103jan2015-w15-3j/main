package com.equinox;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


/**
 * The RecurringTodoRule class contains the rules and methods for creating and
 * storing individual Todos
 *
 */
public class RecurringTodoRule implements UndoableRedoable<RecurringTodoRule> {
    // @author A0093910H
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

    protected static final String recurringTodoWithLimitStringFormat = "Recurrence Rule: \"%1$s\" every %2$s until %3$s";
    protected static final String recurringTodoStringFormat = "Recurrence Rule: \"%1$s\" every %2$s";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd MMM yyyy");
    private static final PeriodFormatter PERIOD_FORMATTER;
    static {
    	PeriodFormatterBuilder pfb = new PeriodFormatterBuilder();
    	PERIOD_FORMATTER = pfb
    			.printZeroNever().appendYears().appendSuffix(" year(s)")
    			.printZeroNever().appendMonths().appendSuffix(" month(s)")
    			.printZeroNever().appendWeeks().appendSuffix(" week(s)")
    			.printZeroNever().appendDays().appendSuffix(" day(s)")
    			.toFormatter();
    }
    private boolean hasLimit = false;
    

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
        this.originalName = name;
        this.name = RECURRING_TODO_PREIX + name;
        this.dateTimes = dateTimes;
        this.recurringInterval = period;
        this.recurringId = recurringId;
        this.recurrenceLimit = limit;
        this.hasLimit = true;
    }
    
    /**
     * Create a copy of a rule. For use in EditCommand.
     * 
     * @param rule
     */
    private RecurringTodoRule(RecurringTodoRule rule) {
    	this.originalName = rule.originalName;
        this.name = rule.name;
    	this.dateTimes = rule.dateTimes;
    	this.recurringInterval = rule.recurringInterval;
    	this.recurringId = rule.recurringId;
    	this.recurrenceLimit = rule.recurrenceLimit;
    }
    
    private RecurringTodoRule(int recurringId) {
    	this.recurringId = recurringId;
    }
    
    public RecurringTodoRule copy() {
    	return new RecurringTodoRule(this);
    }
    
    public RecurringTodoRule getPlaceholder() {
    	return new RecurringTodoRule(recurringId);
    }
    
    public boolean isPlaceholder() {
    	return recurringInterval == null;
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

    public int getId() {
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
            addFirstRecurringTodo(memory, newTodo);
            newTodoCount++;
            updateDateTime();
        }

        DateTime now = new DateTime();
        DateTime nextOccurrence = now.plus(getRecurringInterval());
        // Update until next occurrence or the limit, whichever is earlier
        DateTime updateLimit = nextOccurrence;
        if (nextOccurrence.compareTo(getRecurrenceLimit()) > 0) {
            updateLimit = getRecurrenceLimit();
        }

        while (getDateTime().compareTo(updateLimit) <= 0) {
            currentID = memory.obtainFreshId();
            Todo newTodo = new Todo(currentID, name, dateTimes, recurringId);
            addRecurringTodo(memory, newTodo);
            newTodoCount++;
            updateDateTime();
        }

        return newTodoCount;
    }

    public void setRecurrenceLimit(DateTime recurrenceLimit) {
        if (recurrenceLimit == null) {
            throw new IllegalArgumentException(
                    "Recurring limit of recurring rule cannot be empty");
        } else {
            this.recurrenceLimit = recurrenceLimit;
            this.hasLimit = true;
        }
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
        this.name = RECURRING_TODO_PREIX + originalName;
    }

    public void setRecurringInterval(Period recurringInterval) {
        if (recurringInterval == null) {
            throw new IllegalArgumentException(
                    "Recurring interval cannot be empty");
        } else {
            this.recurringInterval = recurringInterval;
        }
    }

    public void setDateTimes(List<DateTime> dateTimes)
            throws IllegalArgumentException {
        if (dateTimes == null || dateTimes.size() == 0) {
            throw new IllegalArgumentException(
                    "DateTime field of recurring rule cannot be empty");
        } else {
            this.dateTimes = dateTimes;
        }
    }

    /**
     * Add the first occurrence of the rule
     * 
     * This is considered to be added together with the rule by the user
     * 
     * @param memory
     * @param newTodo
     */
    private void addFirstRecurringTodo(Memory memory, Todo newTodo) {
        recurringTodos.add(newTodo);
        memory.userAdd(newTodo);
    }

    /**
     * Add the subsequent occurrences of the rule
     * 
     * This is considered to be added automatically by the system, not the user
     * 
     * @param memory
     * @param newTodo
     */
    private void addRecurringTodo(Memory memory, Todo newTodo) {
        recurringTodos.add(newTodo);
        memory.systemAdd(newTodo);
    }

    private void updateDateTime() {
        for (int i = 0; i < dateTimes.size(); i++) {
            if (dateTimes.get(i) != null) {
                dateTimes.set(i, dateTimes.get(i).plus(recurringInterval));
            }
        }
    }

    public String toString() {
    	if(hasLimit) {
    		return String.format(recurringTodoWithLimitStringFormat, originalName, recurringInterval.toString(PERIOD_FORMATTER), recurrenceLimit.toString(DATE_TIME_FORMATTER));
    	} else {
    		return String.format(recurringTodoStringFormat, originalName, recurringInterval.toString(PERIOD_FORMATTER));
    	}
        
    }

    /**
     * Method to return a DateTime of the Recurring rule's last occurrence. The
     * order of preference: start time > end time > null
     * 
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
