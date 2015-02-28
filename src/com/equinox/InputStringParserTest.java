package com.equinox;
import static org.junit.Assert.*;

import org.junit.Test;

public class InputStringParserTest {
	String s0 = "";
	String s1 = "add task 1 at computing level 1 on 12 december";
	String s2 = "DELETE TASK 2";
	String s3 = "     delete task 3 on list";
	String s4 = "mark    ";
	String s5 = "test error";

	String[] a0 = {""};
	String[] a1 = {"add", "task", "1", "at", "computing", "level", "1",
			"on", "12", "december"};
	String[] a2 = {"delete", "task", "2"};
	String[] a3 = {"delete", "task", "3", "on", "list"};
	String[] a4 = {"mark"};
	String[] a5 = {"test", "error"};
	
	KeyParamPair[] p1 = {new KeyParamPair("add", "task 1"), 
			new KeyParamPair("at", "computing level 1"), 
			new KeyParamPair("on", "12 december")};
	KeyParamPair[] p2 = {new KeyParamPair("delete", "task 2")};
	KeyParamPair[] p3 = {new KeyParamPair("delete", "task 3"), 
			new KeyParamPair("on", "list")};
	KeyParamPair[] p4 = {new KeyParamPair("mark", "")};
	
	@Test
	public void testProcessInput() {
		assertArrayEquals(a0, InputStringParser.processInput(s0));
		assertArrayEquals(a1, InputStringParser.processInput(s1));
		assertArrayEquals(a2, InputStringParser.processInput(s2));
		assertArrayEquals(a3, InputStringParser.processInput(s3));
		assertArrayEquals(a4, InputStringParser.processInput(s4));
	}
	
	@Test
	public void testGetCommandType() {
		assertEquals(ParsedInput.TYPE.ERROR, InputStringParser.getCommandType(a0));
		assertEquals(ParsedInput.TYPE.ADD, InputStringParser.getCommandType(a1));
		assertEquals(ParsedInput.TYPE.DELETE, InputStringParser.getCommandType(a2));
		assertEquals(ParsedInput.TYPE.DELETE, InputStringParser.getCommandType(a3));
		assertEquals(ParsedInput.TYPE.MARK, InputStringParser.getCommandType(a4));
		assertEquals(ParsedInput.TYPE.ERROR, InputStringParser.getCommandType(a5));
	}
	
	@Test
	public void testGetNoOfKeywords() {
		assertEquals(3, InputStringParser.getNoOfKeywords(a1));
		assertEquals(1, InputStringParser.getNoOfKeywords(a2));
		assertEquals(2, InputStringParser.getNoOfKeywords(a3));
		assertEquals(1, InputStringParser.getNoOfKeywords(a4));
	}
	
	@Test
	public void testExtractParam() {
		assertArrayEquals(p1, InputStringParser.extractParam(a1));
		assertArrayEquals(p2, InputStringParser.extractParam(a2));
		assertArrayEquals(p3, InputStringParser.extractParam(a3));
		assertArrayEquals(p4, InputStringParser.extractParam(a4));
	}
}
