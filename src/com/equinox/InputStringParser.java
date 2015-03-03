package com.equinox;
import java.util.ArrayList;

public class InputStringParser {

    private static final String STRING_EMPTY = "";
	private static final String REGEX_SPACE = "\\s";

	
	public static ParsedInput parse(String input) {
    	String[] inputArray = processInput(input);
    	KEYWORDS cType = getCommandType(inputArray);
    	
    	//if command type is error
    	if(cType == null) {
    		return new ParsedInput(null, null);
    	}
    	
    	ArrayList<KeyParamPair> pairArray = extractParam(inputArray);
        return new ParsedInput(cType, pairArray);
    }

	/**
	 * Takes in a user input string and puts individual words into elements in a String array. 
	 *
	 * @param input Input string from Zeitgeist class
	 * @return A String array where each element is a word from the original string
	 */
	public static String[] processInput(String input) {
		input = input.trim();
		input = input.toLowerCase();
		return input.split(REGEX_SPACE);
	}

	public static ArrayList<KeyParamPair> extractParam(String[] inputArray) {
		String key = inputArray[0];
		ArrayList<KeyParamPair> resultList = fillUpPairArray(inputArray, key);
		return resultList;
	}
	
	/**
	 * Processes the inputArray to fill up the ArrayList with KeyParamPair objects
	 * 
	 * @param inputArray A string array with the user input split into individual words.
	 * @param key The command key (first keyword) in the user input string.
	 * @return A ArrayList<KeyParamPair> object with KeyParamPair objects
	 */
	public static ArrayList<KeyParamPair> fillUpPairArray(String[] inputArray, String key) {
		ArrayList<KeyParamPair> resultList = new ArrayList<KeyParamPair>();
		String tempParam = STRING_EMPTY;
		String currentParam;
		
		for(int i = 1; i < inputArray.length; i++) {
			currentParam = inputArray[i];
			
			//inputArray[i] is a keyword. Create a KeyParamPair with previous keyword 
			//and tempParam and add to ArrayList. Update key and tempParam.
			if(InputStringKeyword.isKeyword(currentParam)) {
				resultList.add(new KeyParamPair(key, tempParam));
				key = currentParam;
				tempParam = STRING_EMPTY;
			
				//inputArray[i] is not a keyword; concat with tempParam.
			} else {
				tempParam = combineParamString(tempParam, currentParam);
			}
		}
		//last KeyParamPair to be added to ArrayList
		resultList.add(new KeyParamPair(key, tempParam));
		
		return resultList;
	}

	public static String combineParamString(String tempParam,
			String currentParam) {
		if(tempParam.length() == 0) {
			return currentParam;
		} else {
			return tempParam.concat(" ".concat(currentParam));
		}
	}

	/**
	 * This operation gets the type of command of the user input assuming
	 * that the keyword of command type is input by the user as the 
	 * first word.
	 * @param inputArray
	 * @return
	 */
	public static KEYWORDS getCommandType(String[] inputArray) {
		String typeString = inputArray[0];
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed 
	 * command types. Returns the command type or type error if 
	 * the command type is not listed. 
	 * @param typeString
	 * @return KEYWORDS specifying the type, null if typeString does not contain command.
	 */
	public static KEYWORDS determineCommandType(String typeString) {
		KEYWORDS type = null;
		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}
}
