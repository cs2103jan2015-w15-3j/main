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
    public void setUp() throws Exception{
        // Add different types of todos
        todos.add(new Todo(DateTime.now(), "floating task"));

        // Completed tasks
        todos.add(new Todo(DateTime.now(), "CS3230 deadline", DateParser
                .parseDate("6 March at 9pm")));

        Todo todo_done_0 = new Todo(DateTime.now(), "eat more");
        todo_done_0.setDone(true);
        todos.add(todo_done_0);

        Todo todo_done = new Todo(DateTime.now(), "CIP event",
                DateParser.parseDate("3 March at 10am"),
                DateParser.parseDate("3 March at 12pm"));
        todo_done.setDone(true);
        todos.add(todo_done);
        
        Todo todo_done_1 = new Todo(DateTime.now(), "new year",
                DateParser.parseDate("1 January at 10am"),
                DateParser.parseDate("1 January at 11am"));
        todo_done_1.setDone(true);
        todos.add(todo_done_1);

        Todo todo_done_2 = new Todo(DateTime.now(), "CS1010 deadline",
                DateParser.parseDate("3 Feb at 10pm"));
        todo_done_2.setDone(true);
        todos.add(todo_done_2);

        todos.add(new Todo(DateTime.now(), "read floating books"));

        todos.add(new Todo(DateTime.now(), "CS3243 project deadline",
                DateParser.parseDate("7 March at 9am")));

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDisplayDefaultPending() {
        String expected = "         floating task            "
                + "\n06 Mar   CS3230 deadline           21:00"
                + "\n         read floating b          "
                + "\n07 Mar   CS3243 project            09:00" + "\n";
        assertEquals(expected, DisplayHandler.getDisplayDefault(todos, 0));
    }

    @Test
    public void testDisplayChronoPending() {
        String expected = "06 Mar   CS3230 deadline           21:00"
                + "\n07 Mar   CS3243 project            09:00"
                + "\n         floating task            "
                + "\n         read floating b          " + "\n";
        assertEquals(expected, DisplayHandler.getDisplayChrono(todos, 0));
    }

    @Test
    public void testDisplayDefaultCompleted() {
        String expected = "         eat more                 "
                + "\n03 Mar   CIP event                 10:00 - 12:00"
                + "\n01 Jan   new year                  10:00 - 11:00"
                + "\n03 Feb   CS1010 deadline           22:00"
                + "\n";
        assertEquals(expected, DisplayHandler.getDisplayDefault(todos, 1));
    }

    @Test
    public void testDisplayChronoCompleted() {
        String expected = "01 Jan   new year                  10:00 - 11:00"
                + "\n03 Feb   CS1010 deadline           22:00"
                + "\n03 Mar   CIP event                 10:00 - 12:00"
                + "\n         eat more                 " + "\n";

        assertEquals(expected, DisplayHandler.getDisplayChrono(todos, 1));
    }

}
