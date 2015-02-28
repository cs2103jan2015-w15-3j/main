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

        todos.add(new Todo(DateTime.now(), "CS3230 deadline", DateParser
                .parseDate("6 March at 9pm")));

        Todo todo_done = new Todo(DateTime.now(), "CIP event",
                DateParser.parseDate("3 March at 10am"),
                DateParser.parseDate("3 March at 12am"));
        todo_done.isDone = true;
        todos.add(todo_done);

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
        String expected = "CIP event 03 March 10:00 - 00:00" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayDefault(todos, 1));
    }

    @Test
    public void testDisplayChronoCompleted() {
        String expected = "CIP event 03 March 10:00 - 00:00" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayChrono(todos, 1));
    }

}
