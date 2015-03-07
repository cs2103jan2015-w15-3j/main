package com.equinox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DisplayCommand extends Command {
	
	public DisplayCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	// Signals for whether to display pending or completed todos
	private static final int showPending = 0;
	private static final int showCompleted = 1;
	private static final int showAll = 2;

	// Messages for different categories based on completeness
	private static final String messagePending = "Pending:";
	private static final String messageCompleted = "Completed:";
	private static final String messageAll = "All:";

	// Messages for different types of todos (event, deadline, floating)
	private static final String messageEvent = "E ";
	private static final String messageDeadline = "D ";
	private static final String messageFloating = "F ";

	// Max length for the title of todo to be displayed
	private static final int MAX_CHAR = 15;

	private static final String eventFormat = "%1$-2s %2$-7s %3$-25s %4$s - %5$s";
	private static final String deadLineFormat = "%1$-2s %2$-7s %3$-25s %4$s";
	private static final String floatingFormat = "%1$-2s %2$-7s %3$-25s";

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
			.forPattern("dd MMM");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

	@Override
	public Signal execute() {
		String displayString;
		Collection<Todo> todos = memory.getAllTodos();
		if (todos.size() == 0) {
			return new Signal(Signal.DISPLAY_EMPTY_SIGNAL);
		}
		String param = keyParamPairList.get(0).getParam();
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
		return new Signal(Signal.DISPLAY_SUCCESS_SIGNAL);
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
			appendTodo(signal, sBuilder, todo);
		}
		return sBuilder.toString();
	}

	private static void appendTodo(int signal, StringBuilder sBuilder, Todo todo) {
		// Show pending, skip the completed tasks
		if (signal == showPending && todo.isDone()) {
			return;
		}

		// Show completed, skip the pending tasks
		if (signal == showCompleted && !todo.isDone()) {
			return;
		}

		if (todo.getStartTime() != null && todo.getEndTime() != null) {
			sBuilder.append(formatEvent(todo));
		} else if (todo.getEndTime() != null) {
			sBuilder.append(formatDeadline(todo));
		} else if (todo.getStartTime() == null && todo.getEndTime() == null) {
			sBuilder.append(formatFloatingTask(todo));
		}
	}

	private static String formatFloatingTask(Todo todo) {
		String title = todo.getTitle();
		title = messageFloating + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		return String.format(floatingFormat, id, "", title)
				+ System.lineSeparator();
	}

	private static String formatDeadline(Todo todo) {
		String title = todo.getTitle();
		title = messageDeadline + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime endTime = todo.getEndTime();
		String endDateString = DateFormatter.print(endTime);
		String endTimeString = TimeFormatter.print(endTime);
		return String.format(deadLineFormat, id, endDateString, title,
				endTimeString) + System.lineSeparator();
	}

	private static String formatEvent(Todo todo) {
		String title = todo.getTitle();
		title = messageEvent + shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime startTime = todo.getStartTime();
		DateTime endTime = todo.getEndTime();
		// TODO: Handle start and end on different dates
		String startDateString = DateFormatter.print(startTime);
		String startTimeString = TimeFormatter.print(startTime);
		String endTimeString = TimeFormatter.print(endTime);
		return String.format(eventFormat, id, startDateString, title,
				startTimeString, endTimeString) + System.lineSeparator();
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

			todos.add(new Todo("CS3243 project deadline", DateParser
					.parseDate("7 March at 9am")));
		} catch (DateUndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(DisplayCommand.getDisplayChrono(todos, showAll));

		System.out.println(DisplayCommand.getDisplayChrono(todos, showPending));

		System.out.println(DisplayCommand
				.getDisplayChrono(todos, showCompleted));

	}
}
