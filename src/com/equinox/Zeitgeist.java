package com.equinox;

/**
 * This is where it all begins.
 * 
 * @author paradite
 *
 */
import java.util.Scanner;

public class Zeitgeist {

    public static Scanner scn = new Scanner(System.in);
    public static StorageHandler storage = new StorageHandler();
    public static Memory memory = storage.retrieveMemoryFromFile();

	/**
	 * The main logic unit of Zeitgeist. Reads the input from Zeitgeist and
	 * passes it to the Parser, the first element in the flow of component calls
	 * present in all operations.
	 * 
	 * @param args contains arguments from the command line at launch. (Not used)
	 */
	public static void main(String[] args) {
        SignalHandler.printSignal(new Signal(Signal.WELCOME_SIGNAL, true));
        String input;
		while (true) {
            SignalHandler.printCommandPrefix();
            input = scn.nextLine();
            Signal signal = handleInput(input);
            SignalHandler.printSignal(signal);
		}
	}

    public static Signal handleInput(String input) {
        ParsedInput c = Parser.parseInput(input);
        return execute(c);
    }

	/**
	 * Creates a Command object with the given ParsedInput and executes it.
	 * 
	 * @param userInput input from user, parsed by the Parser.
	 * @return a Signal containing a message to be printed, denoting success or
	 *         failure of the execution.
	 */
	private static Signal execute(ParsedInput userInput) {
		Signal processSignal;
		Command c;

		Keywords commandType = userInput.getType();
		if (commandType == null) {
            return new Signal(String.format(Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false);
		} else {

            switch (commandType) {
                case ADD :
                	c = new AddCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DELETE :
                	c = new DeleteCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case MARK :
                	c = new MarkCommand(userInput, memory);
                	processSignal = c.execute();
                    break;
				
                case REDO :
                	c = new RedoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case UNDO :
                	c = new UndoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EDIT :
                	c = new EditCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DISPLAY :
                	c = new DisplayCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case SEARCH :
                	c = new SearchCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EXIT :
                	c = new ExitCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                default :
                	// NOTE: This case should never happen
                    processSignal = new Signal(Signal.GENERIC_FATAL_ERROR,
                            false);
                    System.exit(-1);
                    break;
			}
            
            return processSignal;

		}
		
	}
}
