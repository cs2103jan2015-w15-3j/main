package com.equinox;

import java.util.ArrayList;
import java.util.Collection;
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

    // Messages for different categories based on completeness
    private static final String messagePending = "Pending:";
    private static final String messageCompleted = "Completed:";
    private static final String messageAll = "All:";

    // Max length for the title of todo to be displayed
    private static final int MAX_CHAR = 15;

    private static final String eventFormat = "%1$-8s %2$-25s %3$s - %4$s";
    private static final String deadLineFormat = "%1$-8s %2$-25s %3$s";
    private static final String floatingFormat = "%1$-8s %2$-25s";

    private static final DateTimeFormatter deadlineDateFormatter = DateTimeFormat
            .forPattern("dd MMM");
    private static final DateTimeFormatter deadlineTimeFormatter = DateTimeFormat
            .forPattern("HH:mm");
    private static final DateTimeFormatter floatingTimeFormatter = DateTimeFormat
            .forPattern("yyyyMMdd");
    private static final DateTimeFormatter eventDateFormatter = DateTimeFormat
            .forPattern("dd MMM");
    private static final DateTimeFormatter eventTimeFormatter = DateTimeFormat
            .forPattern("HH:mm");

	public static Signal process(ParsedInput c, Memory memory) {
        String displayString;
        Collection<Todo> todos = memory.getAllTodos();
        if (todos.size() == 0) {
            return new Signal(Signal.SIGNAL_EMPTY_TODO);
        }
        String param = c.getParamPairList().get(0).getParam();
        if (param.equals("completed") || param.equals("complete")
                || param.equals("c")) {
            displayString = getDisplayChrono(todos, showCompleted);
            System.out.println(displayString);
        } else if (param.equals("all") || param.equals("a")) {
            displayString = getDisplayChrono(todos, showAll);
            System.out.println(displayString);
        } else {
            // By default we show pending tasks, in chronological order
            displayString = getDisplayChrono(todos, showPending);
            System.out.println(displayString);
        }
        return new Signal(Signal.SIGNAL_DISPLAY_SUCCESS);
    }

    public static String getDisplayChrono(Collection<Todo> todos, int signal) {
        ArrayList<Todo> clonedTodos = cloneSortChrono(todos);
        return getDisplayDefault(clonedTodos, signal);
    }

    private static ArrayList<Todo> cloneSortChrono(Collection<Todo> todos) {
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
        
        if (signal == showAll) {
            sBuilder.append(messageAll + System.lineSeparator());
        } else if (signal == showCompleted) {
            sBuilder.append(messageCompleted + System.lineSeparator());
        } else if (signal == showPending) {
            sBuilder.append(messagePending + System.lineSeparator());
        }
        
        while (iterator.hasNext()) {
            Todo todo = iterator.next();
            // Show pending, skip the completed tasks
            if (signal == showPending && todo.isDone()) {
                continue;
            }

            // Show completed, skip the pending tasks
            if (signal == showCompleted && !todo.isDone()) {
                continue;
            }

            if (todo.getStartTime() != null && todo.getEndTime() != null) {
                sBuilder.append(formatEvent(todo));
            } else if (todo.getEndTime() != null) {
                sBuilder.append(formatDeadline(todo));
            } else if (todo.getStartTime() == null && todo.getEndTime() == null) {
                sBuilder.append(formatFloatingTask(todo));
            }
        }
        return sBuilder.toString();
    }

    private static String formatFloatingTask(Todo todo) {
        String title = todo.getTitle();
        title = shortenTitle(title);
        return String.format(floatingFormat, "", title)
                + System.lineSeparator();
    }

    private static String formatDeadline(Todo todo) {
        String title = todo.getTitle();
        title = shortenTitle(title);
        int maxLength = (title.length() < MAX_CHAR) ? title.length() : MAX_CHAR;
        DateTime endTime = todo.getEndTime();
        String endDateString = deadlineDateFormatter.print(endTime);
        String endTimeString = deadlineTimeFormatter.print(endTime);
        return String.format(deadLineFormat, endDateString, title,
                endTimeString)
                + System.lineSeparator();
    }

    private static String formatEvent(Todo todo) {
        String title = todo.getTitle();
        title = shortenTitle(title);
        int maxLength = (title.length() < MAX_CHAR) ? title.length() : MAX_CHAR;
        DateTime startTime = todo.getStartTime();
        DateTime endTime = todo.getEndTime();
        // TODO: Handle start and end on different dates
        String startDateString = eventDateFormatter.print(startTime);
        String startTimeString = eventTimeFormatter.print(startTime);
        String endTimeString = eventTimeFormatter.print(endTime);
        return String.format(eventFormat, startDateString, title,
                startTimeString, endTimeString)
                + System.lineSeparator();
    }

    static class ChronoComparator implements Comparator<Todo> {

        @Override
        public int compare(Todo o1, Todo o2) {
            // Floating tasks with no time will be sorted in lexicographical
            // order
            if (o1.getDateTime() == null && o2.getDateTime() == null) {
                return o1.getTitle().compareTo(o2.getTitle());
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

    private static String shortenTitle(String title) {
        int maxLength = (title.length() < MAX_CHAR) ? title.length() : MAX_CHAR;
        title = title.substring(0, maxLength);
        return title;
    }

    public static void main(String[] args) {
        ArrayList<Todo> todos = new ArrayList<Todo>();

        try {
			// Add different types of todos
			todos.add(new Todo("floating task"));

			// Completed tasks
			todos.add(new Todo("CS3230 deadline", DateParser
			        .parseDate("6 March at 9pm")));

			Todo todo_done_0 = new Todo("eat more");
			todo_done_0.setDone(true);
			todos.add(todo_done_0);

			Todo todo_done = new Todo("CIP event",
			        DateParser.parseDate("3 March at 10am"),
			        DateParser.parseDate("3 March at 12pm"));
			todo_done.setDone(true);
			todos.add(todo_done);

			Todo todo_done_1 = new Todo("new year",
			        DateParser.parseDate("1 January at 10am"),
			        DateParser.parseDate("1 January at 11am"));
			todo_done_1.setDone(true);
			todos.add(todo_done_1);

			Todo todo_done_2 = new Todo("CS1010 deadline",
			        DateParser.parseDate("3 Feb at 10pm"));
			todo_done_2.setDone(true);
			todos.add(todo_done_2);

			todos.add(new Todo("read floating books"));

			todos.add(new Todo("CS3243 project deadline",
			        DateParser.parseDate("7 March at 9am")));
		} catch (DateUndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // System.out.println("pending:");
        System.out.println(DisplayHandler.getDisplayChrono(todos, 0));
        // System.out.println("completed:");
        System.out.println(DisplayHandler.getDisplayChrono(todos, 1));

    }
}
