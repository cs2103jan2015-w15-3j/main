package com.equinox;

public abstract class Command {
	
	public abstract Signal execute(ParsedInput input, Memory memory);

}
