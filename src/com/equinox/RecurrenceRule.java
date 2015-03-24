package com.equinox;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class RecurrenceRule {
    protected Period recurringInterval;
    protected int recurringId;
    private DateTime recurrenceLimit;

    public Period getRecurringInterval() {
        return recurringInterval;
    }

    public int getRecurringId() {
        return recurringId;
    }

    public DateTime getRecurrenceLimit() {
        return recurrenceLimit;
    }

    public RecurrenceRule(Period recurringInterval, int recurringId,
            DateTime recurrenceLimit) {
        super();
        this.recurringInterval = recurringInterval;
        this.recurringId = recurringId;
        this.recurrenceLimit = recurrenceLimit;
    }

}
