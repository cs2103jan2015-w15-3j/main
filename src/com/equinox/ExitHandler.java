package com.equinox;

public class ExitHandler {
    public static Signal process(ParsedInput userInput, Memory memory) {
        return new Signal(Signal.EXIT_SUCCESS);
    }
}
