
public class MarkHandler implements CommandHandler {

	@Override
	public Signal process(Command c) {
        KeyParamPair[] pairs = c.getParamPairArray();

        // Ensure that there is only pair of key and params
        if (pairs.length > 1) {
            return new Signal(Signal.SIGNAL_INVALID_PARAMS);
        } else {
            // Real index equals to the index in command minus 1
            int indexToMark = Integer.parseInt(pairs[0].getParam()) + 1;
            // Todo todoToMark =
            return null;
        }
	}
}
