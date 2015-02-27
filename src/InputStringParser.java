
public class InputStringParser {

    private static final String STRING_EMPTY = "";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String REGEX_SPACE = " ";

	
	public static ParsedInput parse(String input) {
    	String[] commandArray = processInput(input);
    	ParsedInput.TYPE cType = getCommandType(commandArray);
    	
    	//if command type is error
    	if(cType == ParsedInput.TYPE.ERROR) {
    		return new ParsedInput(cType, null);
    	}
    	
    	KeyParamPair[] pairArray = extractParam(commandArray);
        return new ParsedInput(cType, pairArray);
    }

	public static String[] processInput(String input) {
		input = input.trim();
		input = input.toLowerCase();
		return input.split(REGEX_SPACE);
	}

	public static KeyParamPair[] extractParam(String[] commandArray) {
		String key = commandArray[0];
		int length = getNoOfKeywords(commandArray);
		KeyParamPair[] resultTable = new KeyParamPair[length];
		resultTable = fillUpPairArray(commandArray, key, length, resultTable);
		return resultTable;
	}

	public static KeyParamPair[] fillUpPairArray(String[] commandArray, String key,
			int length, KeyParamPair[] resultTable) {
		int tableIndex = 0;
		String tempParam = STRING_EMPTY;
		String currentParam;
		
		//traverses the commandArray. tempParam concats until a keyword is recognised
		//Then, a KeyParamPair is created with the previous keyword and the tempParam.
		for(int i = 1; i < commandArray.length; i++) {
			currentParam = commandArray[i];
			if(InputStringKeyword.isKeyword(currentParam)) {
				resultTable[tableIndex] = new KeyParamPair(key, tempParam);
				tableIndex++;
				key = currentParam;
				tempParam = STRING_EMPTY;
			} else {
				tempParam = combineParamString(tempParam, currentParam);
			}
		}
		resultTable[length -1] = new KeyParamPair(key, tempParam);
		return resultTable;
	}

	public static String combineParamString(String tempParam,
			String currentParam) {
		if(tempParam.length() == 0) {
			return currentParam;
		} else {
			return tempParam.concat(REGEX_SPACE.concat(currentParam));
		}
	}

	public static int getNoOfKeywords(String[] commandArray) {
		int count = 0;
		String param;
		for(int i = 0; i < commandArray.length; i++) {
			param = commandArray[i];
			if(InputStringKeyword.isKeyword(param)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * This operation gets the type of command of the user input assuming
	 * that the keyword of command type is input by the user as the 
	 * first word.
	 * @param commandArray
	 * @return
	 */
	public static ParsedInput.TYPE getCommandType(String[] commandArray) {
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
	public static ParsedInput.TYPE determineCommandType(String typeString) {
		if(typeString.equals(COMMAND_ADD)) {
			return ParsedInput.TYPE.ADD;
		} else if(typeString.equals(COMMAND_DELETE)) {
			return ParsedInput.TYPE.DELETE;
		} else if (typeString.equals(COMMAND_UNDO)) {
			return ParsedInput.TYPE.UNDO;
		} else if (typeString.equals(COMMAND_EDIT)) {
			return ParsedInput.TYPE.EDIT;
		} else if (typeString.equals(COMMAND_MARK)) {
			return ParsedInput.TYPE.MARK;
		} else if (typeString.equals(COMMAND_SEARCH)) {
			return ParsedInput.TYPE.SEARCH;
		} else if (typeString.equals(COMMAND_DISPLAY)) {
			return ParsedInput.TYPE.DISPLAY;
		} else {
			return ParsedInput.TYPE.ERROR;
		}
	}
}
