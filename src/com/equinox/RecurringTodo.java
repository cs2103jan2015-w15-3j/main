package com.equinox;

import org.joda.time.Period;

import com.equinox.exceptions.DateUndefinedException;

public class RecurringTodo extends Todo {
    private RecurringTodoRule recurrenceRule;
    private Period DEFAULT_RECURRENCE_LIMIT_PERIOD = new Period(0).withYears(1);

    /**
     * Constructs a Recurring Todo of type: DEADLINE or EVENT
     * 
     * @param id the ID of the Todo.
     * @param recurringId the RecurringID of the Todo.
     * @param name name of the task.
     * @param dateTimes a List of DateTimes specifying the end and/or start times.
     */
    public RecurringTodo(int id, RecurringTodoRule rule) {
        // Pass in the constructor for normal event and deadline
        super(id, rule.getName(), rule.getDateTimes());
        this.recurrenceRule = rule;
    }
    
    /**
     * Makes an exact copy of another Todo.
     * 
     * @param todo the Todo to be copied.
     */
    protected RecurringTodo(RecurringTodo todo) {
        super(todo);
        this.recurrenceRule = todo.recurrenceRule;
    }

    @Override
    public String toString() {
        return this.getName() + this.recurrenceRule.getRecurringId() + " "
                + this.recurrenceRule.getRecurringInterval().toString();
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
        return recurrenceRule.getRecurringId();
    }

    public static void main(String[] args) throws DateUndefinedException {
        Period period = new Period().withDays(2);
        Todo recurringTodo = new RecurringTodo(2, new RecurringTodoRule(0, 2,
                "recurring", Parser.parseDates("tuesday"), period));

        System.out.println(recurringTodo.getName());
        System.out.println(recurringTodo.toString());
        System.out.println();

        Todo another = new Todo(recurringTodo);
        System.out.println(another.getName());
        System.out.println(another.toString());
        System.out.println();
        
        Todo anotherrecurring = new RecurringTodo((RecurringTodo) recurringTodo);
        System.out.println(anotherrecurring.getName());
        System.out.println(anotherrecurring.toString());

    }

}
