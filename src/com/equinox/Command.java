package com.equinox;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public abstract class Command {
	ParsedInput input;
	Memory memory;
	ArrayList<KeyParamPair> keyParamPairList;
	List<DateTime> dateTimeList;
	
	public Command(ParsedInput input, Memory memory) {
		this.keyParamPairList = input.getParamPairList();
		this.dateTimeList = input.getDateTimeList();
		this.input = input;
		this.memory = memory;
	}

	public abstract Signal execute();

}
