import java.util.GregorianCalendar;

public class AddHandler {

    public static Signal process(ParsedInput c, Memory memory) {
        KeyParamPair[] commandArray = c.getParamPairArray();
        int numberOfElements = commandArray.length;
        
        switch(numberOfElements){
        	//Floating task
        	case 1 :
        	String floatingTaskName = commandArray[0].getParam();
        	Todo floatingTask = new Todo(new GregorianCalendar(), floatingTaskName);
        	//Memory.add(floatingTask);
        	break;
        	
        	//Deadline
        	case 2 :
        	String deadlineName = commandArray[0].getParam();
        	GregorianCalendar deadlineTime =  dateParser(commandArray[1].getParam());
        	Todo deadline = new Todo(new GregorianCalendar(), deadlineName, deadlineTime );
        	//Memory.add(deadline);
        	break;
        	
        	//Event
        	case 3 :
        	String eventName = commandArray[0].getParam();
            GregorianCalendar eventStartTime =  dateParser(commandArray[1].getParam());
            GregorianCalendar eventEndTime = dateParser(commandArray[2].getParam());
            Todo event = new Todo(new GregorianCalendar(), eventName, eventStartTime, eventEndTime);
        	//Memory.add(todo);
        	break;
        }
        return null;
    }

}
