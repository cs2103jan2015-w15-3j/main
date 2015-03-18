/**
 * Unit test class for AddCommand.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

package com.equinox;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class AddCommandTest {
	Memory memory;
	ParsedInput input;
	Keywords addCommand = Keywords.ADD;
	
	private static final String userInputFloatingTask1 = "add study CS2105";
	
	public enum TYPE{
		ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
	}
	
	@Test
	public void testLessThanOneKeyword(){
		// Mock Signal object
        Signal invalidParamSignal = new Signal(Signal.GENERIC_EMPTY_PARAM,
                false);
		
		//Test for equivalence in Signal object
		assertEquals(invalidParamSignal, Zeitgeist.handleInput("add"));
	}
	
	@Test
	public void testFloatingTask(){
		/*
		 * Test for a single-worded, lower-case floating task
		 */
		String floatingTaskCommand = "add running";
		String floatingTaskString = "Floating task \"running\"";
		// Mock Signal object
		Signal addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTaskString), true);
		assertEquals(addSuccess, Zeitgeist.handleInput(floatingTaskCommand));
	}

	@Test
	public void testDeadline(){
		/*
		 * Test for a single-worded, lower-case floating task
		 */
		String deadlineCommand = "add ce2 by 2359pm on 15 March";
		String deadlineString = "Deadline \"ce2\" by 15 Mar at 23:59";
		//Mock Signal object
		Signal addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadlineString), true);
		assertEquals(addSuccess, Zeitgeist.handleInput(deadlineCommand));
		
	}
	
	@Test
	public void testEvent(){
		/*
		 * Test for a single-worded, lower-case event
		 */
		String eventCommand = "add canoeing from 3pm to 4pm on 6 April";
		String eventString = "Event \"canoeing\" from 06 Apr at 15:00 to 06 Apr at 16:00";
		//Mock Signal object
		Signal addSuccess = new Signal(String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, eventString), true);
		assertEquals(addSuccess, Zeitgeist.handleInput(eventCommand));
	}
}
