package com.equinox;

import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DisplayHandler {
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

        displayString = getDisplayString(todos);
        System.out.println(displayString);
        return new Signal(Signal.SIGNAL_SUCCESS);
	}

    public static String getDisplayString(ArrayList<Todo> todos) {
        Iterator<Todo> iterator = todos.iterator();
        StringBuilder sBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Todo todo = iterator.next();
            // Skip the finished tasks
            if (todo.isDone) {
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

}
