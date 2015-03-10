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
    public static Memory memory = new Memory();

	public static void main(String[] args) {
        SignalHandler.printSignal(new Signal(Signal.WELCOME_SIGNAL, true));
		String input = scn.nextLine();
		while (true) {
            Signal signal = handleInput(input);
            SignalHandler.printSignal(signal);
			input = scn.nextLine();
		}
	}

    public static Signal handleInput(String input) {
        return handleInput(memory, input);
    }

    private static Signal handleInput(Memory memory, String input) { // Propose removal as Zeitgeist class now has static memory. Reference via Zeitgeist.memory
        ParsedInput c = Parser.parseInput(input);
        return execute(c, memory);
    }

	private static Signal execute(ParsedInput userInput, Memory memory) {
		Signal processSignal;
		Command c;

		KEYWORDS commandType = userInput.getType();
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
