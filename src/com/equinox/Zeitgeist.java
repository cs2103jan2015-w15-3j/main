package com.equinox;

/**
 * This is where it all begins.
 * 
 * @author paradite
 *
 */
import java.util.Scanner;

public class Zeitgeist {
    public static Scanner scn;
    
    public static void main(String[] args) {
        scn = new Scanner(System.in);
        Memory memory = new Memory();
        String input = scn.nextLine();
        while (true) {
	        ParsedInput c = InputStringParser.parse(input);
	        dispatchCommand(c, memory);
            input = scn.nextLine();
    	}
    }

    private static void dispatchCommand(ParsedInput userCommand, Memory memory) {
    	Signal processSignal = null;
    	
        switch (userCommand.getType()) {
            case ADD :
                processSignal = AddHandler.process(userCommand, memory);
                SignalHandler.printProcessStateMessage(processSignal);
                break;
            case DELETE :
            	processSignal = DeleteHandler.process(userCommand, memory);
            	SignalHandler.printProcessStateMessage(processSignal);
                break;
                
            case MARK :
            	processSignal = MarkHandler.process(userCommand, memory);      
            	SignalHandler.printProcessStateMessage(processSignal);
            	break;
            
            case UNDO :
            	processSignal = UndoHandler.process(userCommand, memory);      
            	SignalHandler.printProcessStateMessage(processSignal);
            	break;
            	
            case EDIT :
            	processSignal = SearchHandler.process(userCommand, memory);
            	SignalHandler.printProcessStateMessage(processSignal);
            	break;
            
            case DISPLAY :
            	processSignal = DisplayHandler.process(userCommand, memory);
            	SignalHandler.printProcessStateMessage(processSignal);
            	break;
            	
            case SEARCH:
            	processSignal = SearchHandler.process(userCommand, memory);
            	SignalHandler.printProcessStateMessage(processSignal);
            	break;	
            	
            case ERROR :
            	processSignal = ErrorHandler.process(userCommand, memory);
            	SignalHandler.printProcessStateMessage(processSignal);
                break;
            
        }

    }
}