
/**
 * This is where it all begins.
 * 
 * @author paradite
 *
 */
import java.util.Scanner;

public class Zeitgeist {
    public static AddHandler mAddHandler;
    public static DeleteHandler mDeleteHandler;
    public static MarkHandler mMarkHandler;
    public static UndoHandler mUndoHandler;
    public static MessagePrinter printer;
    public static Scanner scn;
    
    public static void main(String[] args) {
        scn = new Scanner(System.in);
    	
        while(scn.hasNextLine()){
	        String input = scn.nextLine();
	        Command c = CommandParser.parse(input);
	        dispatchCommand(c);
    	}
    }

    private static void dispatchCommand(Command c) {
    	Signal processSignal = null;
    	
    	printer = new MessagePrinter();
        switch (c.type) {
            case ADD :
                mAddHandler = new AddHandler();
                processSignal = mAddHandler.process(c);
                printer.printProcessStateMessage(processSignal);
                break;
            case DELETE :
            	mDeleteHandler = new DeleteHandler();
            	processSignal = mDeleteHandler.process(c);
            	printer.printProcessStateMessage(processSignal);
                break;
                
            case MARK :
            	mMarkHandler = new MarkHandler();
            	mMarkHandler.process(c);      
            	printer.printProcessStateMessage(processSignal);
            	break;
            
            case UNDO :
            	mUndoHandler = new UndoHandler();
            	mUndoHandler.process(c);      
            	printer.printProcessStateMessage(processSignal);
            	break;
            	
            default :
            	//make new Signal and pass to messagePrinter;
                break;
        }

    }
}
