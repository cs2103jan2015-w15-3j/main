package com.equinox;

public class SignalHandler {

    private static final String PREFIX = "Zqitgesit: ";

    public static void printSignal(Signal signal) {
        String message = signal.toString();
        if (message != null) {
            System.out.println(PREFIX + message);
        }
	}
}
