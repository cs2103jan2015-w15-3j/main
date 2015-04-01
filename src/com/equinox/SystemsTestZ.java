package com.equinox;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

public class SystemsTestZ {

    Collection<Todo> todos;

    @Test
    public void testAll() {
        Zeitgeist logic = Zeitgeist.getInstance();

        logic.handleInput("add floating task");

        logic.handleInput("add CS3230 deadline on 9 March 9pm");

        logic.handleInput("add CIP event from 3 March at 10am to 3 March at 12pm");

        logic.handleInput("add new year from 1 January at 10am to 1 January at 11am");

        logic.handleInput("add CS1010 deadline by 3 Feb at 10pm");

        logic.handleInput("add read floating books");

        logic.handleInput("add CS3243 project deadline by 7 March at 9am");

        logic.handleInput("add CS3333 project 2 on 7 Apr 10am");

        logic.handleInput("add meet june at on the table from malaysia from 9pm on 9 march to 10pm on 10 march");

        logic.handleInput("mark 0");

        logic.handleInput("mark 2");

        logic.handleInput("search read");

        logic.handleInput("add go to NUS hackers every friday");

        logic.handleInput("add learn something every sunday until 20 apr");

        logic.handleInput("search learn");

        logic.handleInput("undo");

        logic.handleInput("undo");

        logic.handleInput("redo");

        todos = logic.memory.getAllTodos();

        String expected = "Pending:\n" + "         floating task            "
                + "\n06 Mar   CS3230 deadline           21:00"
                + "\n         read floating b          "
                + "\n07 Mar   CS3243 project            09:00" + "\n";
        assertEquals(expected, DisplayCommand.getDisplay(todos, 0));

    }

}
