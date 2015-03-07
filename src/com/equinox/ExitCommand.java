package com.equinox;

public class ExitCommand extends Command {
	public ExitCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
    public Signal execute() {
        return new Signal(Signal.EXIT_SUCCESS);
    }
}
