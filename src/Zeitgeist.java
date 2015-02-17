
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
    public static SearchHandler mSearchHandler;
    public static EditHandler mEditHandler;
    public static ErrorHandler mErrorHandler;
    public static DisplayHandler mDisplayHandler;
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
            	processSignal = mMarkHandler.process(c);      
            	printer.printProcessStateMessage(processSignal);
            	break;
            
            case UNDO :
            	mUndoHandler = new UndoHandler();
            	processSignal = mUndoHandler.process(c);      
            	printer.printProcessStateMessage(processSignal);
            	break;
            	
            case EDIT :
            	mSearchHandler = new SearchHandler();
            	processSignal = mSearchHandler.process(c);
            	printer.printProcessStateMessage(processSignal);
            	break;
            
            case DISPLAY : 
            	mDisplayHandler = new DisplayHandler();
            	processSignal = mDisplayHandler.process(c);
            	printer.printProcessStateMessage(processSignal);
            	break;
            	
            case ERROR :
            	mErrorHandler = new ErrorHandler();
            	processSignal = mErrorHandler.process(c);
            	printer.printProcessStateMessage(processSignal);
                break;
        }

    }
}
