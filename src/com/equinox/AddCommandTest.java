/**
 * Unit test class for AddCommand.
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

public class AddCommandTest {
	Memory memory;
	ParsedInput input;
	KEYWORDS addCommand = KEYWORDS.ADD;
	
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
		//Hard-coded parameters for AddCommand.process method
		ArrayList<KeyParamPair> list = new ArrayList<KeyParamPair>();
		input = new ParsedInput(addCommand, list);
		Signal invalidParamSignal = new Signal(Signal.SIGNAL_INVALID_PARAMS);
		
		//Test for equivalence in Signal object
		assertEquals(invalidParamSignal, AddCommand.process(input, memory));
	}
	
	@Test
	public void testTodoPrint(){
		Todo td1 = new Todo("do CS2103T TextBuddy CE2");
		System.out.println(td1.toString());
	}
	
	@Test
	public void testFloatingTask(){
		//Hard-coded parameters for AddCommand.process method
		ParsedInput input = InputStringParser.parse(userInputFloatingTask1);
		String taskTitle = "study CS2105";
		
		//Add hard-coded input into memory using AddCommand's process method
		AddCommand.process(input, memory);

		//Test for equivalence between Todo object in memory and testTodo
		Todo testTodo = new Todo(taskTitle);
		Todo fromMemory = memory.get(1);

		if(testTodo.equals(fromMemory)){
			System.out.println("Success");
		} else {
			System.out.println("Failure");
		}
		
		assertEquals(testTodo, fromMemory);

		/*
		ParsedInput input2 = InputStringParser.parse("add one");
		testTodo = new Todo(currentTime, "two");
		AddCommand.process(input2, memory);
		assertEquals(testTodo, memory.get(memory.getCurrentSize()));
		 */
	}
	
	
}
