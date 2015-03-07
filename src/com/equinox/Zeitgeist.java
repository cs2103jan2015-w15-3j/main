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
                	c = new AddCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case DELETE :
                	c = new DeleteCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case MARK :
                	c = new MarkCommand();
                	processSignal = c.execute(userInput, memory);
                    break;
				
                case REDO :
                	c = new RedoCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case UNDO :
                	c = new UndoCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case EDIT :
                	c = new EditCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case DISPLAY :
                	c = new DisplayCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case SEARCH :
                	c = new SearchCommand();
                	processSignal = c.execute(userInput, memory);
                    break;

                case EXIT :
                	c = new ExitCommand();
                	processSignal = c.execute(userInput, memory);
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
