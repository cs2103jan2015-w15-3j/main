package com.equinox;

public class ExitCommand extends Command {
	@Override
    public Signal execute(ParsedInput userInput, Memory memory) {
        return new Signal(Signal.EXIT_SUCCESS);
    }
}
