package com.equinox;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InputStringParser {

	private static final String STRING_EMPTY = "";
	private static final String REGEX_SPACE = "\\s";

	public static ParsedInput parse(String input) {
		ArrayList<String> wordList = processInput(input);
		KEYWORDS cType = getCommandType(wordList);

		// if command type is error
		if (cType == null) {
			return new ParsedInput(null, null);
		}

		ArrayList<KeyParamPair> pairArray = extractParam(wordList);
		return new ParsedInput(cType, pairArray);
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String array.
	 *
	 * @param input
	 *            Input string from Zeitgeist class
	 * @return A String array where each element is a word from the original
	 *         string
	 */
	public static ArrayList<String> processInput(String input) {
		input = input.trim();
		input = input.toLowerCase();
		String[] inputArray = input.split(REGEX_SPACE);
		ArrayList<String> wordList = new ArrayList<String>();
		for(int i=0; i<inputArray.length; i++){
			wordList.add(inputArray[i]);
		}
		return wordList;
	}

	/**
	 * Processes the inputArray to fill up an ArrayList with KeyParamPair
	 * objects. This method assumes that the first word in user input
	 * is a keyword.
	 * 
	 * @param inputArray
	 *            A string array with the user input split into individual
	 *            words.
	 * @return A ArrayList<KeyParamPair> object with KeyParamPair objects
	 */
	public static ArrayList<KeyParamPair> extractParam(ArrayList<String> wordList) {
		String key = wordList.get(0);
		ArrayList<KeyParamPair> resultList = new ArrayList<KeyParamPair>();
		String tempParam = STRING_EMPTY;
		String currentParam;
		String processedInputArray;
		
		
		/*buggy: infinite loop
		 * // Process the wordList to append [on <date>] to the end of the ArrayList 
		 
		int initialSize = 
		for(int i=0; i< wordList.size(); i++){
			String word = wordList.get(i);
			
			if(InputStringKeyword.getKeyword(word) == KEYWORDS.ON){
				int index = i+1;
				wordList.remove(word);
				wordList.add(word);
				
				while(InputStringKeyword.getKeyword(word) != KEYWORDS.FROM){
					
					String removed = wordList.remove(index);
					wordList.add(removed);
				}
				
				break;
			}
		}
		*/
		for (int i = 1; i < wordList.size(); i++) {
			currentParam = wordList.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// keyword
			// and tempParam and add to ArrayList. Update key and tempParam.
			if (InputStringKeyword.isKeyword(currentParam)) {
				resultList.add(new KeyParamPair(key, tempParam));
				key = currentParam;
				
				if(InputStringKeyword.getKeyword(currentParam) == KEYWORDS.FROM){
					// concatenate all params that come after 'from'
					// and generate a KeyParamPair
					tempParam = STRING_EMPTY;
					for(int j=i+1; j<wordList.size(); j++){
						tempParam += wordList.get(j) + " ";
					}
					resultList.add(new KeyParamPair(key, tempParam.trim()));
					return resultList;
				}
				
				
				tempParam = STRING_EMPTY;

				// wordList.get(i) is not a keyword; concat with tempParam.
			} else {
				tempParam = combineParamString(tempParam, currentParam);
			}
		}
		// last KeyParamPair to be added to ArrayList
		resultList.add(new KeyParamPair(key, tempParam));

		return resultList;
	}

	public static String combineParamString(String tempParam,
			String currentParam) {
		if (tempParam.length() == 0) {
			return currentParam;
		} else {
			return tempParam.concat(" ".concat(currentParam));
		}
	}

	/**
	 * This operation gets the type of command of the user input assuming that
	 * the keyword of command type is input by the user as the first word.
	 * 
	 * @param inputArray
	 * @return
	 */
	public static KEYWORDS getCommandType(ArrayList<String> wordList) {
		String typeString = wordList.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types. Returns the command type or type error if the command type is not
	 * listed.
	 * 
	 * @param typeString
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	public static KEYWORDS determineCommandType(String typeString) {
		KEYWORDS type = null;
		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}
}
