package com.equinox;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemsTestZ {

    Collection<Todo> todos;
    String storageFileDirectory = StorageUtils.readSettingsFile();
    Zeitgeist logic;

    @Before
    public void setUp() {
    	String fileDirectory = Zeitgeist.getStorageFileDirFromSettings();
        logic = new Zeitgeist(fileDirectory);
        logic.reloadMemory();
        
    }

    @After
    public void tearDown() {
    	logic.deleteStorageFile();
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

        logic.handleInput("add go to NUS hackers on 3 apr every friday until 4 apr");

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

        logic.handleInput("edit 3 last new year");

        logic.handleInput("search new");

        logic.handleInput("delete 6");

        logic.handleInput("display");

        todos = logic.memory.getAllTodos();

        String expected = "Showing pending todos:\nID | Name                           | Time\n\n..Mon 09 Mar 2015...\n1  | CS3230 deadline                | 21:00\n\n..Thu 01 Jan 2015...\n3  | last new year                  | 23:59\n\n..Fri 01 Jan 2016...\n4  | (Recurring) new year           | 23:59\n\n..Tue 03 Feb 2015...\n5  | CS1010 deadline                | 22:00\n\n..Sat 07 Mar 2015...\n7  | CS3243 project deadline        | 09:00\n\n..Tue 07 Apr 2015...\n8  | CS3333 project 2               | 10:00\n\n..Fri 03 Apr 2015...\n9  | (Recurring) go to NUS hackers  | 23:59\n";
        assertEquals(expected, DisplayCommand.getDisplay(todos, 0));

    }

}
