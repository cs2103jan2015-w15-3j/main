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

	private static void dispatchCommand(ParsedInput userInput, Memory memory) {
		Signal processSignal;
		Command c;

		KEYWORDS commandType = userInput.getType();
		if (commandType == null) {
            SignalHandler.printSignal(new Signal(String
                    .format(Signal.INVALID_COMMAND_FORMAT, ""))); // TODO
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
                    processSignal = new Signal(
                            String.format(Signal.INVALID_COMMAND_FORMAT,"")); // TODO
                    break;
			}
            SignalHandler.printSignal(processSignal);
		}
	}
}
