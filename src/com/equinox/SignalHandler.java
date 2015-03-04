package com.equinox;

public class SignalHandler {

    private static final String MESSAGE_SUCCESS = "%1$s operation is successful for %2$s!";

    private static final String ERROR_UNRECOGNIZED_SINGAL = "Error: message not specified, please add a message for the signal!";

    private static final String ERROR_INVALID_COMMAND = "Error: %1$s command is invalid!";

    public static void printProcessStateMessage(Signal signal) {
        String message = processSignal(signal);
        if (message != null) {
            System.out.println(message);
        }
	}

    public static String processSignal(Signal signal) {
        switch (signal.getType()) {
            case Signal.SIGNAL_DISPLAY_SUCCESS :
                return null;

            case Signal.SIGNAL_SUCCESS :
                return String.format(MESSAGE_SUCCESS, signal.getFirstParam(),
                        signal.getSecondParam());

            case Signal.SIGNAL_INVALID_COMMAND :
                return String.format(ERROR_INVALID_COMMAND,
                        signal.getFirstParam());

            default :
                return ERROR_UNRECOGNIZED_SINGAL;
        }
    }
}
