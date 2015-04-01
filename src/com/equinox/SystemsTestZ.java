package com.equinox;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemsTestZ {

    Collection<Todo> todos;

    Zeitgeist logic;

    @Before
    public void setUp() {

        Zeitgeist.readSettingsFile();
        logic = Zeitgeist.getInstance();
    }

    @After
    public void tearDown() {
        logic.storage.storageFile.delete();
    }

    @Test
    public void testAll() {

        logic.handleInput("add floating task");

        logic.handleInput("add CS3230 deadline on 9 March 9pm");

        logic.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        // logic.handleInput("add new year from 1 January at 10am to 1 January at 11am every year");

        logic.handleInput("add new year on 1 January every year");

        logic.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        logic.handleInput("add read floating books");

        logic.handleInput("add CS3243 project deadline by 7 March at 9am");

        logic.handleInput("add CS3333 project 2 on 7 Apr 10am");

        logic.handleInput("mark 0");

        logic.handleInput("mark 2");

        logic.handleInput("search read");

        logic.handleInput("add go to NUS hackers every friday");

        logic.handleInput("add learn something every sunday");

        logic.handleInput("search learn");

        logic.handleInput("undo");

        logic.handleInput("undo");

        logic.handleInput("redo");

        logic.handleInput("mark 1");

        logic.handleInput("undo");

        logic.handleInput("mark 2");

        logic.handleInput("search new");

        logic.handleInput("search NUS");

        logic.handleInput("edit 3 name last new year");

        logic.handleInput("search new");

        logic.handleInput("display");

        todos = logic.memory.getAllTodos();

        String expected = "Showing pending todos:\nID | Name                           "
                + "| Time\n\n.....Mon 09 Mar.....\n1  | CS3230 deadline                "
                + "| 21:00\n\n.....Thu 01 Jan.....\n3  | last new year                  "
                + "| 23:59\n\n.....Fri 01 Jan.....\n4  | (Recurring) new year           "
                + "| 23:59\n\n.....Tue 03 Feb.....\n5  | CS1010 deadline                "
                + "| 22:00\n\n......Anytime.......\n6  | read floating books            "
                + "| NIL\n7  | CS3243 project deadline        | 09:00\n8  "
                + "| CS3333 project 2               | 10:00\n9  "
                + "| (Recurring) go to NUS hackers  | 23:59\n";
        assertEquals(expected, DisplayCommand.getDisplay(todos, 0));

    }

}