package com.equinox;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EditHandlerTest {
	Memory memory = new Memory();
	
	DateTime startDate = new DateTime(2015, 9, 29, 00, 00, 0);
	DateTime endDate = new DateTime(2015, 11, 10, 18, 59, 0);
	String title = "Meet parents for dinner";
	String start = "7pm 6 May";
	String end = "2100h 6 May";
	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		memory.add(new Todo(new DateTime(), "Project meeting", startDate, endDate));
		ArrayList<KeyParamPair> paramList = new ArrayList<KeyParamPair>();
		paramList.add(new KeyParamPair("", "1"));
		paramList.add(new KeyParamPair("title", title));
		paramList.add(new KeyParamPair("start", start));
		paramList.add(new KeyParamPair("end", end));
		ParsedInput input = new ParsedInput(null, paramList);
		EditHandler.process(input, memory);
		
		Todo expectedTodo;
		try {
			expectedTodo = new Todo(new DateTime(), title, DateParser.parseDate(start), DateParser.parseDate(end));
			assertEquals(memory.get(1), expectedTodo);
		} catch (DateUndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
