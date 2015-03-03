package com.equinox;
/**
 * The AddHandler class handles all user commands with "add" as the first keyword.
 * 
 * It takes in a ParsedInput object and generates a Todo object with respect to the
 * ParsedInput object. The Todo object can be a floating task, deadline or event.
 * 
 * It returns a Signal object to indicate success or failure (if exception is thrown).
 * 
 * @param input A ParsedInput object that contains the command TYPE and a keyParamPair array
 * @param memory A Memory object that stores Todo objects
 * @return A Signal object whose type is SIGNAL_SUCCESS if AddHandler is successful,
 * SIGNAL_ERROR if unsuccessful.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

import org.joda.time.DateTime;
import java.util.ArrayList;

public class AddHandler {

    public static Signal process(ParsedInput input, Memory memory) {
    	//Check for valid number of keywords
    	int numberOfKeywords = input.getParamPairList().size();
    	if(numberOfKeywords < 1 || numberOfKeywords > 3){
    		return new Signal(Signal.SIGNAL_INVALID_PARAMS);
    	}
    	
    	try{
	        ArrayList<KeyParamPair> keyParamPairList = input.getParamPairList();
	        int numberOfElements = keyParamPairList.size();
	        DateTime currentTime = DateTime.now();
	        
	        switch(numberOfElements){
	        	//Floating task
	        	//Example:
	        	//KeyParamPair 0: add <task>
	        	case 1 :
	        	String floatingTaskName = keyParamPairList.get(0).getParam();
	        	Todo floatingTask = new Todo(currentTime , floatingTaskName);
	        	memory.add(floatingTask);
	        	break;
	        	
	        	//Deadline 
	        	//Example: 
	        	//KeyParamPair 0: add <task>
	        	//KeyParamPair 1: by <date>
	        	case 2 :
	        	String deadlineName = keyParamPairList.get(0).getParam();
	        	DateTime deadlineTime = DateParser.parseDate(keyParamPairList.get(1).getParam());
	        	Todo deadline = new Todo(currentTime, deadlineName, deadlineTime );
	        	memory.add(deadline);
	        	break;
	        	
	        	//Event
	        	//Example:
	        	//KeyParamPair 0: add <event>
	        	//KeyParamPair 1: from <date>
	        	//KeyParamPair 2: to <date>
	        	case 3 :
	        	String eventName = keyParamPairList.get(0).getParam();
	            DateTime eventStartTime = DateParser.parseDate(keyParamPairList.get(1).getParam());
	            DateTime eventEndTime = DateParser.parseDate(keyParamPairList.get(2).getParam());
	            Todo event = new Todo(currentTime, eventName, eventStartTime, eventEndTime);
	        	memory.add(event);
	        	break;
	        }
    	} catch(Exception e) {
    		e.printStackTrace();
    		return new Signal(Signal.SIGNAL_ERROR);
    	}
        return new Signal(Signal.SIGNAL_SUCCESS);
    }

}
