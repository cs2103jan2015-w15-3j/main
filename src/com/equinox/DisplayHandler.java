package com.equinox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DisplayHandler {
    // Signals for whether to display pending or completed todos
    private static final int showPending = 0;
    private static final int showCompleted = 1;
    private static final int showAll = 2;

    private static final String eventFormat = "%1$s %2$s %3$s - %4$s";
    private static final String deadLineFormat = "%1$s by %2$s";
    private static final String floatingFormat = "%1$s";

    private static final DateTimeFormatter deadlineTimeFormatter = DateTimeFormat
            .forPattern("dd MMMM HH:mm");
    private static final DateTimeFormatter floatingTimeFormatter = DateTimeFormat
            .forPattern("yyyyMMdd");
    private static final DateTimeFormatter eventDateFormatter = DateTimeFormat
            .forPattern("dd MMMM");
    private static final DateTimeFormatter eventTimeFormatter = DateTimeFormat
            .forPattern("HH:mm");

	public static Signal process(ParsedInput c, Memory memory) {
        String displayString;
        ArrayList<Todo> todos = memory.getAllTodos();
        if (todos.size() == 0) {
            return new Signal(Signal.SIGNAL_EMPTY_TODO);
        }

        // By default we show pending tasks, in chronological order
        displayString = getDisplayChrono(todos, showPending);
        System.out.println(displayString);
        return new Signal(Signal.SIGNAL_SUCCESS);
    }

    public static String getDisplayChrono(ArrayList<Todo> todos, int signal) {
        ArrayList<Todo> clonedTodos = cloneSortChrono(todos);
        return getDisplayDefault(clonedTodos, signal);
    }

    private static ArrayList<Todo> cloneSortChrono(ArrayList<Todo> todos) {
        ArrayList<Todo> clonedTodos = new ArrayList<Todo>(todos.size());
        for (Todo todo : todos) {
            clonedTodos.add(new Todo(todo));
        }
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
        return clonedTodos;
    }

    public static String getDisplayDefault(ArrayList<Todo> todos, int signal) {
        Iterator<Todo> iterator = todos.iterator();
        StringBuilder sBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Todo todo = iterator.next();
            // Show pending, skip the completed tasks
            if (signal == showPending && todo.isDone) {
                continue;
            }

            // Show completed, skip the pending tasks
            if (signal == showCompleted && !todo.isDone) {
                continue;
            }

            if (todo.startTime != null && todo.endTime != null) {
                sBuilder.append(formatEvent(todo));
            } else if (todo.endTime != null) {
                sBuilder.append(formatDeadline(todo));
            } else if (todo.startTime == null && todo.endTime == null) {
                sBuilder.append(formatFloatingTask(todo));
            }
        }
        return sBuilder.toString();
    }

    private static String formatFloatingTask(Todo todo) {
        String title = todo.title;
        return String.format(floatingFormat, title) + System.lineSeparator();
    }

    private static String formatDeadline(Todo todo) {
        String title = todo.title;
        DateTime endTime = todo.endTime;
        String endTimeString = deadlineTimeFormatter.print(endTime);
        return String.format(deadLineFormat, title, endTimeString)
                + System.lineSeparator();
    }

    private static String formatEvent(Todo todo) {
        String title = todo.title;
        DateTime startTime = todo.startTime;
        DateTime endTime = todo.endTime;
        // TODO: Handle start and end on different dates
        String startDateString = eventDateFormatter.print(startTime);
        String startTimeString = eventTimeFormatter.print(startTime);
        String endTimeString = eventTimeFormatter.print(endTime);
        return String.format(eventFormat, title, startDateString,
                startTimeString, endTimeString)
                + System.lineSeparator();
    }

    static class ChronoComparator implements Comparator<Todo> {

        @Override
        public int compare(Todo o1, Todo o2) {
            // Floating tasks with no time will be sorted in lexicographical
            // order
            if (o1.getDateTime() == null && o2.getDateTime() == null) {
                return o1.title.compareTo(o2.title);
            }
            // If only one todo has time, the other with no time will be sorted
            // to the back
            if (o1.getDateTime() == null) {
                return 1;
            } else if (o2.getDateTime() == null) {
                return -1;
            } else {
                // Both have time, compare directly
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        }

    }

}
