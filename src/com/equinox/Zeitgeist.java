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
			SignalHandler.printProcessStateMessage(new Signal(
					Signal.SIGNAL_INVALID_COMMAND));
		}

		switch (commandType) {
		case ADD:
			processSignal = AddHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;
		case DELETE:
			processSignal = DeleteHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;

		case MARK:
			processSignal = MarkHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;

		case UNDO:
			processSignal = UndoHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;

		case EDIT:
			processSignal = SearchHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;

		case DISPLAY:
			processSignal = DisplayHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;

		case SEARCH:
			processSignal = SearchHandler.process(userInput, memory);
			SignalHandler.printProcessStateMessage(processSignal);
			break;
		default:
			SignalHandler.printProcessStateMessage(new Signal(
					Signal.SIGNAL_INVALID_COMMAND));
			break;
		}

	}
}
