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

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class AddHandler {

    public static Signal process(ParsedInput input, Memory memory) {
    	//Check for valid number of keywords
    	int numberOfKeywords = input.getParamPairList().size();
    	if(numberOfKeywords > 3){
    		return new Signal(Signal.invalidParamsForAddHandler);
    	}
    	
    	try{
	        ArrayList<KeyParamPair> keyParamPairList = input.getParamPairList();
	        int numberOfElements = keyParamPairList.size();
	        
	        switch(numberOfElements){
	        	//Floating task
	        	//Example:
	        	//KeyParamPair 0: add <task>
	        	case 1 :
	        	String floatingTaskName = keyParamPairList.get(0).getParam();
	        	Todo floatingTask = new Todo(floatingTaskName);
	        	memory.add(floatingTask);
                    return new Signal(String.format(Signal.addSuccessSignalFormat, floatingTask));
	        	
	        	//Deadline 
	        	//Example: 
                //KeyParamPair 0: add <task>, KeyParamPair 1: by <date>
                    
                //Event
    	        //Example:
    	        //KeyParamPair 0: add <event>, KeyParamPair 1: from <startDate to endDate>
                    
	        	case 2 :
	        	
	        	String todoName = keyParamPairList.get(0).getParam();
	        	String secondKeyword = keyParamPairList.get(1).getParam();
	        	
	        	//Deadline
	        	if(secondKeyword.equals("by") || secondKeyword.equals("on") || secondKeyword.equals("at")){
	        		DateTime deadlineTime = DateParser.parseDate(keyParamPairList.get(1).getParam());
		        	Todo deadline = new Todo(todoName, deadlineTime );
		        	memory.add(deadline);
	                    return new Signal(String.format(Signal.addSuccessSignalFormat, deadline));
	        	}
	        	
	        	//Event
	        	else if(secondKeyword.equals("from")){
	        		List<DateTime> dateTimeList = DateParser.parseDates(keyParamPairList.get(1).getParam());
		            DateTime eventStartTime = dateTimeList.get(0);
		            DateTime eventEndTime = dateTimeList.get(1);
	        		Todo event = new Todo(todoName, eventStartTime, eventEndTime);
		        	memory.add(event);
	                    return new Signal(String.format(Signal.addSuccessSignalFormat, event));
	        	}
	        }
    	} catch(DateUndefinedException e) {
    		e.printStackTrace();
    		String exceptionMessage = e.getMessage();
    		
    		return new Signal(Signal.SIGNAL_ERROR);
    	}
        return new Signal(Signal.SIGNAL_SUCCESS);
    }

}
