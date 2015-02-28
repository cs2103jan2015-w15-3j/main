package com.equinox;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayHandlerTest {
    ArrayList<Todo> todos = new ArrayList<Todo>();

    @Before
    public void setUp() {
        // Add different types of todos
        todos.add(new Todo(DateTime.now(), "floating task"));

        // Completed tasks
        todos.add(new Todo(DateTime.now(), "CS3230 deadline", DateParser
                .parseDate("6 March at 9pm")));

        Todo todo_done_0 = new Todo(DateTime.now(), "eat more");
        todo_done_0.isDone = true;
        todos.add(todo_done_0);

        Todo todo_done = new Todo(DateTime.now(), "CIP event",
                DateParser.parseDate("3 March at 10am"),
                DateParser.parseDate("3 March at 12pm"));
        todo_done.isDone = true;
        todos.add(todo_done);
        
        Todo todo_done_1 = new Todo(DateTime.now(), "new year",
                DateParser.parseDate("1 January at 10am"),
                DateParser.parseDate("1 January at 11am"));
        todo_done_1.isDone = true;
        todos.add(todo_done_1);

        todos.add(new Todo(DateTime.now(), "read floating books"));

        todos.add(new Todo(DateTime.now(), "CS3243 project deadline",
                DateParser.parseDate("7 March at 9am")));

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDisplayDefaultPending() {
        String expected = "floating task"
                + "\nCS3230 deadline by 06 March 21:00"
                + "\nread floating books"
                + "\nCS3243 project deadline by 07 March 09:00" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayDefault(todos, 0));
    }

    @Test
    public void testDisplayChronoPending() {
        String expected = "CS3230 deadline by 06 March 21:00"
                + "\nCS3243 project deadline by 07 March 09:00"
                + "\nfloating task" + "\nread floating books" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayChrono(todos, 0));
    }

    @Test
    public void testDisplayDefaultCompleted() {
        String expected = "eat more" + "\nCIP event 03 March 10:00 - 12:00"
                + "\nnew year 01 January 10:00 - 11:00"
                + "\n";
        assertEquals(expected, DisplayHandler.getDisplayDefault(todos, 1));
    }

    @Test
    public void testDisplayChronoCompleted() {
        String expected = "new year 01 January 10:00 - 11:00"
                + "\nCIP event 03 March 10:00 - 12:00" + "\neat more" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayChrono(todos, 1));
    }

}
