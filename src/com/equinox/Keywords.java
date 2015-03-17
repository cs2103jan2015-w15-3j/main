package com.equinox;

public enum Keywords {
	// Commands
	ADD,
	MARK,
	DELETE,
	SEARCH,
	EDIT,
	DISPLAY, 
	UNDO,
	REDO,
	EXIT,
	
	// Keywords for parameters in ADD command
	BY,
	FROM,
	ON,
	AT,
	
	// Keywords for parameters in EDIT command
	// NAME,
	START,
	END,
	DONE,
	
	//Keywords for parameters in SEARCH command
	NAME,
	DATE,
	TIME,
	DAY,
	MONTH,
	YEAR;
}