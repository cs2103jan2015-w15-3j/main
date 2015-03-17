package com.equinox;

public class SignalHandler {

    private static final String PREFIX = "Zeitgeist: ";

    public static void printSignal(Signal signal) {
        assert (signal != null);
        String message = signal.toString();
        if (message.equals(Signal.EXIT_SUCCESS)) {
            System.out.println(PREFIX + message);
            System.exit(0);
        }
        if (!message.isEmpty()) {
            System.out.println(PREFIX + message);
        }
	}
}
