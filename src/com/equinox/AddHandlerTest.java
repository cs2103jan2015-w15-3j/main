/**
 * Unit test class for AddHandler.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

package com.equinox;
import org.joda.time.DateTime;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class AddHandlerTest {
	Memory memory;
	ParsedInput input;
	ParsedInput.TYPE addCommand = ParsedInput.TYPE.ADD;
	
	private static final String addCommandString = "add";
	private static final String userInputFloatingTask1 = "add study CS2105";
	
	public enum TYPE{
		ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
	}
	
	@Before
	public void testSetup(){
		memory = new Memory();
	}
	
	@After
	public void testTeardown(){
		memory = null;
		input = null;
	}
	
	@Test
	public void testLessThanOneKeyword(){
		//Hard-coded parameters for AddHandler.process method
		ArrayList<KeyParamPair> list = new ArrayList<KeyParamPair>();
		input = new ParsedInput(addCommand, list);
		Signal invalidParamSignal = new Signal(Signal.SIGNAL_INVALID_PARAMS);
		
		//Test for equivalence in Signal object
		assertEquals(invalidParamSignal, AddHandler.process(input, memory));
	}
	
	public void testFloatingTask(){
		//Hard-coded parameters for AddHandler.process method
		ParsedInput input = InputStringParser.parse(userInputFloatingTask1);
		String taskTitle = "study CS2105";
		DateTime currentTime = DateTime.now();
		/*		
		ArrayList<KeyParamPair> list = new ArrayList<KeyParamPair>();
		list.add(new KeyParamPair(addCommandString, taskTitle));
	
		input = new ParsedInput(addCommand, list);
		*/
		
		AddHandler.process(input, memory);
		
		//Test for equivalence in Todo object
		Todo testTodo = new Todo(currentTime, taskTitle);
		assertEquals(testTodo, memory.get(memory.getCurrentSize()));
	}
}
