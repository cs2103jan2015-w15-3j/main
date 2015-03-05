package com.equinox;

public class SignalHandler {

    private static final String PREFIX = "Zqitgesit: ";

    public static void printSignal(Signal signal) {
        String message = signal.toString();
        if (message != null && !message.isEmpty()) {
            System.out.println(PREFIX + message);
        }
	}
}
