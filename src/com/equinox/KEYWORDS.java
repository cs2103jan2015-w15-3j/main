package com.equinox;

public enum KEYWORDS {
	// Commands
	ADD,
	MARK,
	DELETE,
	SEARCH,
	EDIT,
	DISPLAY, 
	UNDO,
 REDO, EXIT,
	
	// Keywords for parameters in ADD command
	BY,
	FROM,
	ON,
	AT,
	
	// Keywords for parameters in EDIT command
	TITLE,
	START,
	END,
	
	//Keywords for parameters in SEARCH command
	NAME,
	DATE,
	TIME,
	DAY,
	MONTH,
	YEAR;
}