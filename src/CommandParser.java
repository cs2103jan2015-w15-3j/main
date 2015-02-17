
public class CommandParser {

    private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String REGEX_SPACE = " ";

	public static Command parse(String input) {
    	String[] commandArray = processInput(input);
    	Command.TYPE cType = getCommandType(commandArray);
    	KeyParamPair[] pairArray = extractParam(commandArray);
        return new Command(cType, pairArray);
    }

	private static String[] processInput(String input) {
		input = input.toLowerCase();
		return input.split(REGEX_SPACE);
	}

	private static KeyParamPair[] extractParam(String[] commandArray) {
		
		return null;
	}

	/**
	 * This operation gets the type of command of the user input assuming
	 * that the keyword of command type is input by the user as the 
	 * first word.
	 * @param commandArray
	 * @return
	 */
	private static Command.TYPE getCommandType(String[] commandArray) {
		String typeString = commandArray[0];
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed 
	 * command types. Returns the command type or type error if 
	 * the command type is not listed. 
	 * @param typeString
	 * @return
	 */
	private static Command.TYPE determineCommandType(String typeString) {
		if(typeString.equals(COMMAND_ADD)) {
			return Command.TYPE.ADD;
		} else if(typeString.equals(COMMAND_DELETE)) {
			return Command.TYPE.DELETE;
		} else if (typeString.equals(COMMAND_UNDO)) {
			return Command.TYPE.UNDO;
		} else if (typeString.equals(COMMAND_EDIT)) {
			return Command.TYPE.EDIT;
		} else if (typeString.equals(COMMAND_MARK)) {
			return Command.TYPE.MARK;
		} else if (typeString.equals(COMMAND_SEARCH)) {
			return Command.TYPE.SEARCH;
		} else if (typeString.equals(COMMAND_DISPLAY)) {
			return Command.TYPE.DISPLAY;
		} else {
			return Command.TYPE.ERROR;
		}
	}

//	public static void main(String[] args) {
//		CommandParser cp = new CommandParser();
//		Command c = cp.parse("add helloworld at COM1");
//		System.out.println(c.getType());
//	}
}
