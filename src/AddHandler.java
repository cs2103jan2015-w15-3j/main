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
 * @return A Signal object whose type is 1 if AddHandler is successful, -1 if unsuccessful.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 */

import org.joda.time.DateTime;

public class AddHandler {

    public static Signal process(ParsedInput input, Memory memory) {
    	
    	try{
	        KeyParamPair[] keyParamPairArray = input.getParamPairArray();
	        int numberOfElements = keyParamPairArray.length;
	        DateTime currentTime = DateTime.now();
	        
	        switch(numberOfElements){
	        	//Floating task
	        	//Example:
	        	//KeyParamPair 0: add <task>
	        	case 1 :
	        	String floatingTaskName = keyParamPairArray[0].getParam();
	        	Todo floatingTask = new Todo(currentTime , floatingTaskName);
	        	memory.add(floatingTask);
	        	break;
	        	
	        	//Deadline 
	        	//Example: 
	        	//KeyParamPair 0: add <task>
	        	//KeyParamPair 1: by <date>
	        	case 2 :
	        	String deadlineName = keyParamPairArray[0].getParam();
	        	DateTime deadlineTime = DateParser.parseDate(keyParamPairArray[1].getParam());
	        	Todo deadline = new Todo(currentTime, deadlineName, deadlineTime );
	        	memory.add(deadline);
	        	break;
	        	
	        	//Event
	        	//Example:
	        	//KeyParamPair 0: add <event>
	        	//KeyParamPair 1: from <date>
	        	//KeyParamPair 2: to <date>
	        	case 3 :
	        	String eventName = keyParamPairArray[0].getParam();
	            DateTime eventStartTime = DateParser.parseDate(keyParamPairArray[1].getParam());
	            DateTime eventEndTime = DateParser.parseDate(keyParamPairArray[2].getParam());
	            Todo event = new Todo(currentTime, eventName, eventStartTime, eventEndTime);
	        	memory.add(event);
	        	break;
	        }
    	} catch(Exception e) {
    		return new Signal(-1);
    	}
        return new Signal(1);
    }

}
