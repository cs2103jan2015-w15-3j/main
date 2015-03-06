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

		KEYWORDS commandType = userInput.getType();
		if (commandType == null) {
            SignalHandler.printSignal(new Signal(String
                    .format(Signal.INVALID_COMMAND_FORMAT)));
		} else {

            switch (commandType) {
                case ADD :
                    processSignal = AddHandler.process(userInput, memory);
                    break;

                case DELETE :
                    processSignal = DeleteHandler.process(userInput, memory);
                    break;

                case MARK :
                    processSignal = MarkHandler.process(userInput, memory);
                    break;
				
                case REDO :
                    processSignal = RedoHandler.process(userInput, memory);
                    break;

                case UNDO :
                    processSignal = UndoHandler.process(userInput, memory);
                    break;

                case EDIT :
                    processSignal = EditHandler.process(userInput, memory);
                    break;

                case DISPLAY :
                    processSignal = DisplayHandler.process(userInput, memory);
                    break;

                case SEARCH :
                    processSignal = SearchHandler.process(userInput, memory);
                    break;

                case EXIT :
                    processSignal = ExitHandler.process(userInput, memory);
                    break;

                default :
                    processSignal = new Signal(
                            String.format(Signal.INVALID_COMMAND_FORMAT));
                    break;
			}
            SignalHandler.printSignal(processSignal);
		}
	}
}
