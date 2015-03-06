package com.equinox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.joda.time.DateTime;
import org.junit.Test;

public class EditHandlerTest {
	Memory memory = new Memory();
	
	DateTime startDate = new DateTime(2015, 9, 29, 00, 00, 0);
	DateTime endDate = new DateTime(2015, 11, 10, 18, 59, 0);
	Todo actual = new Todo("Project meeting", startDate, endDate);
	String title = "Meet parents for dinner";
	String start = "7pm 6 May";
	String end = "2100h 6 May";

	@Test
	public void test() throws NullTodoException, DateUndefinedException {
		memory.add(actual);
		ArrayList<KeyParamPair> paramList = new ArrayList<KeyParamPair>();
		paramList.add(new KeyParamPair("", Integer.toString(actual.getIndex())));
		paramList.add(new KeyParamPair("title", title));
		paramList.add(new KeyParamPair("start", start));
		paramList.add(new KeyParamPair("end", end));
		ParsedInput input = new ParsedInput(null, paramList);
		EditHandler.process(input, memory);
		
		Todo expectedTodo = new Todo(title, DateParser.parseDate(start),
				DateParser.parseDate(end));
		assertEquals(expectedTodo, memory.get(actual.getIndex()));
		
	}

}
