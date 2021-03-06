package com.equinox;

public class ExitCommand extends Command {
    // @author A0093910H
	/**
	 * Creates an ExitCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public ExitCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	@Override
    public Signal execute() {
        String param = keyParamPairs.get(0).getParam();
        if (!param.isEmpty()) {
            return new Signal(Signal.EXIT_INVALLID_PARAMS, false);
        }
        
        memory.onDestroy();
        memory.saveToFile();
        return new Signal(Signal.EXIT_SUCCESS, true);
    }
}
