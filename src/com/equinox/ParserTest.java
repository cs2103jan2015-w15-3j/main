package com.equinox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class ParserTest {
	String s0 = "";
	String s1 = "add task 1 at computing level 1 on 12 december";
	String s2 = "DELETE TASK 2";
	String s3 = "     delete task 3 on list";
	String s4 = "mark    ";
	String s5 = "test error";

	ArrayList<String> a0 = new ArrayList<String>(Arrays.asList(""));
	ArrayList<String> a1 = new ArrayList<String>(Arrays.asList("add", "task",
			"1", "at", "computing", "level", "1", "on", "12", "december"));
	ArrayList<String> a2 = new ArrayList<String>(Arrays.asList("delete",
			"task", "2"));
	ArrayList<String> a3 = new ArrayList<String>(Arrays.asList("delete",
			"task", "3", "on", "list"));
	ArrayList<String> a4 = new ArrayList<String>(Arrays.asList("mark"));
	ArrayList<String> a5 = new ArrayList<String>(Arrays.asList( "test", "error" ));

	@Test
	public void testProcessInput() {
		assertEquals(a0, Parser.processInput(s0));
		assertEquals(a1, Parser.processInput(s1));
		assertEquals(a2, Parser.processInput(s2));
		assertEquals(a3, Parser.processInput(s3));
		assertEquals(a4, Parser.processInput(s4));
	}

	@Test
	public void testGetCommandType() {
		assertEquals(null, Parser.getCommandType(a0));
		assertEquals(Keywords.ADD, Parser.getCommandType(a1));
		assertEquals(Keywords.DELETE, Parser.getCommandType(a2));
		assertEquals(Keywords.DELETE, Parser.getCommandType(a3));
		assertEquals(Keywords.MARK, Parser.getCommandType(a4));
		assertEquals(null, Parser.getCommandType(a5));
	}

	@Test
	public void testExtractParam() {
		ArrayList<KeyParamPair> p1 = new ArrayList<KeyParamPair>(3);
		p1.add(new KeyParamPair("add", "task 1"));
		p1.add(new KeyParamPair("at", "computing level 1 on 12 december"));
		ArrayList<KeyParamPair> p2 = new ArrayList<KeyParamPair>(1);
		p2.add(new KeyParamPair("delete", "task 2"));
		ArrayList<KeyParamPair> p3 = new ArrayList<KeyParamPair>(2);
		p3.add(new KeyParamPair("delete", "task 3"));
		p3.add(new KeyParamPair("on", "list"));
		ArrayList<KeyParamPair> p4 = new ArrayList<KeyParamPair>(1);
		p4.add(new KeyParamPair("mark", ""));
		assertEquals(p1, Parser.extractParam(a1));
		assertEquals(p2, Parser.extractParam(a2));
		assertEquals(p3, Parser.extractParam(a3));
		assertEquals(p4, Parser.extractParam(a4));
	}

}
