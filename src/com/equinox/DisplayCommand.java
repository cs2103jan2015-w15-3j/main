package com.equinox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayCommand extends Command {
    private static Logger logger = LoggerFactory
            .getLogger(DisplayCommand.class);

    private static final boolean LOGGING = false;

	/**
	 * Creates a DisplayCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public DisplayCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	// Signals for whether to display pending or completed todos
	private static final int showPending = 0;
	private static final int showCompleted = 1;
	private static final int showAll = 2;

	// Messages for different categories based on completeness
    private static final String MESSAGE_PENDING = "Showing pending todos:";
    private static final String MESSAGE_COMPLETED = "Showing completed todos:";
    private static final String MESSAGE_ALL = "Showing all todos:";

    // Heading for dates and floating tasks
    private static final String FLOATING_TASK_HEADING = "Anytime";
    private static final Object HEADING_PREFIX = System.lineSeparator();

    // Symbols for different types of todos (event, deadline, floating)
    private static final String SYMBOL_EVENT = " E  ";
    private static final String SYMBOL_DEADLINE = " D  ";
    private static final String SYMBOL_FLOATING = " F  ";

	// Max length for the title of todo to be displayed
    private static final int MAX_CHAR = 30;
    private static final int TOTAL_LENGTH = 20;

    private static final String HEADING_ID = "ID";
    private static final String HEADING_NAME = "Name";
    private static final String HEADING_TIME = "Time";

    private static final String DATE_DECO = ".";
    private static final String EMPTY_FIELD = "NIL";

    private static final String eventFormat = "%1$-2s | %2$-30s | %3$s - %4$s";
    private static final String deadLineFormat = "%1$-2s | %2$-30s | %3$s";
    private static final String floatingFormat = "%1$-2s | %2$-30s | %3$s";

    private static final String headingFormat = "%1$-2s | %2$-30s | %3$s";

    private static DateTime inOneDay = new DateTime().plusDays(1);

	private static final DateTimeFormatter DateFormatter = DateTimeFormat
            .forPattern("EEE dd MMM yyyy");
	private static final DateTimeFormatter TimeFormatter = DateTimeFormat
			.forPattern("HH:mm");

    private static PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendPrefix(" in ").appendHours().appendSuffix("h ")
            .appendMinutes().appendSuffix("min ")
            .printZeroNever().toFormatter();

	@Override
	public Signal execute() {
		String displayString;
		Collection<Todo> todos = memory.getAllTodos();
		if (todos.size() == 0) {
            return new Signal(Signal.DISPLAY_EMPTY_SIGNAL, true);
		}
		String param = keyParamPairs.get(0).getParam();
		if (param.equals("completed") || param.equals("complete")
				|| param.equals("c")) {
			displayString = getDisplayChrono(todos, showCompleted);
			System.out.println(displayString);
		} else if (param.equals("all") || param.equals("a")) {
			displayString = getDisplayChrono(todos, showAll);
			System.out.println(displayString);
        } else if (param.isEmpty()) {
			// By default we show pending tasks, in chronological order
            displayDefault(memory);
        } else {
            return new Signal(
                    String.format(Signal.DISPLAY_INVALID_PARAM, param), false);
        }
        return new Signal(Signal.DISPLAY_SUCCESS_SIGNAL, true);
	}

    public static void displayDefault(Memory memory) {
        Collection<Todo> todos = memory.getAllTodos();
        String displayString;
        displayString = getDisplayChrono(todos, showPending);
        System.out.println(displayString);
    }

	public static String getDisplayChrono(Collection<Todo> todos, int signal) {
        ArrayList<Todo> clonedTodos = cloneTodos(todos);
        // By default, we order the todos in chronological order
        Collections.sort(clonedTodos, new ChronoComparator());
		return getDisplay(clonedTodos, signal);
	}

	private static ArrayList<Todo> cloneTodos(Collection<Todo> todos) {
		ArrayList<Todo> clonedTodos = new ArrayList<Todo>(todos.size());
		for (Todo todo : todos) {
			clonedTodos.add(new Todo(todo));
		}
		return clonedTodos;
	}

    public static String getDisplay(Collection<Todo> todos, int signal) {
		Iterator<Todo> iterator = todos.iterator();
		StringBuilder sBuilder = new StringBuilder();
        DateTime currentDate = new DateTime(0);
		appendHeading(signal, sBuilder);

		while (iterator.hasNext()) {
			Todo todo = iterator.next();
            assert (todo != null);
            assert (todo.getName() != null);
            if (LOGGING) {
                logger.info("adding todo with title {} into display",
                        todo.getName());
            }

            // Show pending, skip the completed tasks
            if (signal == showPending && todo.isDone()) {
                continue;
            }
            // Show completed, skip the pending tasks
            if (signal == showCompleted && !todo.isDone()) {
                continue;
            }
            DateTime todoDateTime = todo.getDateTime();
            if (!dateAlreadyDisplayed(currentDate, todoDateTime)) {
                // Date not displayed yet, update currentDate
                currentDate = todoDateTime;
                appendDate(sBuilder, todoDateTime);
            }
            appendTodo(sBuilder, todo);
		}
		return sBuilder.toString();
	}

    /**
     * Check if the date is already displayed
     * 
     * currentDate will be set to null for the first floating task, and a
     * heading for floating task will be displayed.
     * 
     * Subsequent floating tasks will not have the heading displayed when the
     * currentDate is null.
     * 
     * @param currentDate
     * @param dateTime
     * @return	true if date has already been displayed, false otherwise.
     */
    private static boolean dateAlreadyDisplayed(DateTime currentDate,
            DateTime dateTime) {
        if (currentDate == null) {
            // null currentDate indicates that floating task heading has
            // already been displayed
            return true;
        }
        if (dateTime == null) {
            // null dateTime indicates that this is a floating task
            return false;
        }
        if (currentDate.getDayOfYear() == dateTime.getDayOfYear()
                && currentDate.getYear() == dateTime.getYear()) {
            // currentDate equals to the DateTime of todo, meaning that the date
            // has been displayed
            return true;
        }
        return false;
    }

    /**
     * Append the date before the task details if the date is not already
     * appended.
     * 
     * Each date will be displayed only once for all tasks on that date.
     * 
     * Floating tasks will have a heading in place of the date.
     * 
     * @param sBuilder
     * @param todo
     */
    private static void appendDate(StringBuilder sBuilder, DateTime dateTime) {
        // Add empty spaces in front
        sBuilder.append(HEADING_PREFIX);
        if (dateTime == null) {
            // Floating task, add heading
            sBuilder.append(addDecoForDate(FLOATING_TASK_HEADING)
                    + System.lineSeparator());
        } else {
            sBuilder.append(addDecoForDate(formatDateForDisplay(dateTime))
                    + System.lineSeparator());
        }
    }

    private static String addDecoForDate(String s) {
        return StringUtils.center(s, TOTAL_LENGTH, DATE_DECO);
    }

    private static void appendHeading(int signal, StringBuilder sBuilder) {
        if (signal == showAll) {
			sBuilder.append(MESSAGE_ALL + System.lineSeparator());
		} else if (signal == showCompleted) {
			sBuilder.append(MESSAGE_COMPLETED + System.lineSeparator());
		} else if (signal == showPending) {
			sBuilder.append(MESSAGE_PENDING + System.lineSeparator());
		}
        sBuilder.append(String.format(headingFormat, HEADING_ID,
                HEADING_NAME, HEADING_TIME)
                + System.lineSeparator());
    }

    private static void appendTodo(StringBuilder sBuilder, Todo todo) {
		if (todo.getStartTime() != null && todo.getEndTime() != null) {
			sBuilder.append(formatEvent(todo));
		} else if (todo.getEndTime() != null) {
			sBuilder.append(formatDeadline(todo));
		} else if (todo.getStartTime() == null && todo.getEndTime() == null) {
			sBuilder.append(formatFloatingTask(todo));
		}
	}

	private static String formatFloatingTask(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
        return String.format(floatingFormat, id, title, EMPTY_FIELD)
				+ System.lineSeparator();
	}

	private static String formatDeadline(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime endTime = todo.getEndTime();
        String endDateString = formatDateForDisplay(endTime);
        String endTimeString = formatTimeForDisplay(endTime);
        return String.format(deadLineFormat, id, title,
				endTimeString) + System.lineSeparator();
	}

	private static String formatEvent(Todo todo) {
		String title = todo.getName();
        title = shortenTitle(title);
		String id = String.valueOf(todo.getId());
		DateTime startTime = todo.getStartTime();
		DateTime endTime = todo.getEndTime();
        String startDateString = formatDateForDisplay(startTime);
        String startTimeString = formatTimeForDisplay(startTime);
        String endTimeString = formatTimeForDisplay(endTime);
        return String.format(eventFormat, id, title,
				startTimeString, endTimeString) + System.lineSeparator();
	}

	static class ChronoComparator implements Comparator<Todo> {

		@Override
		public int compare(Todo o1, Todo o2) {
			// Floating tasks with no time will be sorted in lexicographical
			// order
			if (o1.getDateTime() == null && o2.getDateTime() == null) {
				return o1.getName().compareTo(o2.getName());
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

    private static String formatTimeForDisplay(DateTime time) {
        DateTime now = new DateTime();
        String periodString = "";
        if (time.isAfter(now) && time.isBefore(inOneDay)) {
            Period period = new Period(now, time);
            periodString = formatter.print(period);
        }

        String timeString = TimeFormatter.print(time).concat(periodString);
        return timeString;
    }

    private static String formatDateForDisplay(DateTime time) {
        String dateString = DateFormatter.print(time);
        return dateString;
    }

    static class StringUtils {

        public static String center(String s, int size) {
            return center(s, size, " ");
        }

        public static String center(String s, int size, String pad) {
            if (pad == null)
                throw new NullPointerException("pad cannot be null");
            if (pad.length() <= 0)
                throw new IllegalArgumentException("pad cannot be empty");
            if (s == null || size <= s.length())
                return s;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (size - s.length()) / 2; i++) {
                sb.append(pad);
            }
            sb.append(s);
            while (sb.length() < size) {
                sb.append(pad);
            }
            return sb.toString();
        }
    }

	public static void main(String[] args) {

	}
}
