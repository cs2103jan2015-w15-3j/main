package com.equinox;
import java.util.ArrayList;

public class InputStringParser {

    private static final String STRING_EMPTY = "";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String REGEX_SPACE = "\\s";

	
	public static ParsedInput parse(String input) {
    	String[] inputArray = processInput(input);
    	ParsedInput.TYPE cType = getCommandType(inputArray);
    	
    	//if command type is error
    	if(cType == ParsedInput.TYPE.ERROR) {
    		return new ParsedInput(cType, null);
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
		int length = getNoOfKeywords(inputArray);
		ArrayList<KeyParamPair> resultList = fillUpPairArray(inputArray, key, length);
		return resultList;
	}
	
	/**
	 * Processes the inputArray to fill up the ArrayList with KeyParamPair objects
	 * 
	 * @param inputArray A string array with the user input split into individual words.
	 * @param key The command key (first keyword) in the user input string.
	 * @param length The total number of keywords in the user input string.
	 * @return A ArrayList<KeyParamPair> object with KeyParamPair objects
	 */
	public static ArrayList<KeyParamPair> fillUpPairArray(String[] inputArray, String key,
			int length) {
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
			return tempParam.concat(REGEX_SPACE.concat(currentParam));
		}
	}

	public static int getNoOfKeywords(String[] inputArray) {
		int count = 0;
		String param;
		for(int i = 0; i < inputArray.length; i++) {
			param = inputArray[i];
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
	 * @param inputArray
	 * @return
	 */
	public static ParsedInput.TYPE getCommandType(String[] inputArray) {
		String typeString = inputArray[0];
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
