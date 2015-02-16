
/**
 * This is where it all begins.
 * 
 * @author paradite
 *
 */
public class Zeitgeist {
    public static AddHandler mAddHandler;
    public static void main(String[] args) {
        String input = null;
        Command c = CommandParser.parse(input);
        dispatchCommand(c);
    }

    private static void dispatchCommand(Command c) {
        switch (c.type) {
            case ADD :
                mAddHandler = new AddHandler();
                mAddHandler.process(c);
                break;
            case DELETE :

                break;
            default :
                break;
        }

    }
}
