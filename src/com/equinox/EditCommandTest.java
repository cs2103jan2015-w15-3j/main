package com.equinox;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import com.equinox.exceptions.NullTodoException;

public class EditCommandTest {
	Memory memory = new Memory();
	
	String startTime = "midnight 29 Sep 2015 ";
	String endTime = "1859h 10 Nov 2015 ";
	String title = "Meet parents for dinner ";
	String editedStart = "7pm 6 May ";
	String editedEnd = "2100h 6 May ";
	
	DateTime newStartDate = new DateTime(2015, 5, 6, 19, 00, 0);
	DateTime newEndDate = new DateTime(2015, 5, 6, 21, 00, 0);
	ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
	
	@Before
	public void setup() {
		dateTimes.add(newStartDate);
		dateTimes.add(newEndDate);
	}
	
	@Test
	public void test() throws NullTodoException {
		Zeitgeist.handleInput("add " + title + "from " + startTime + "to " + endTime);
		System.out.println(Zeitgeist.handleInput("edit " + "0 " + "start " + editedStart + "end " + editedEnd));
		assertEquals(new Todo(0, title.toLowerCase().trim(), dateTimes), Zeitgeist.memory.get(0));
	}
}
