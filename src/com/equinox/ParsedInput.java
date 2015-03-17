package com.equinox;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class ParsedInput {
	
	private String userInput;
	private Keywords type;
	private ArrayList<KeyParamPair> keyParamPairs;
	private List<DateTime> dateTimes;
    
    /**
	 * Creates a ParsedInput object with the type of command, a list of
	 * KeyParamPair objects and a list of DateTime objects derived from the user
	 * input String.
	 * 
	 * @param userInput the original String read from the user.
	 * @param type KEYWORDS specifying the type of the command.
	 * @param keyParamPairList the list of KeyParamPair objects parsed from the input.
	 * @param dateTimeList the list of DateTime objects parsed from the input.
	 */
    public ParsedInput(String userInput, Keywords type, ArrayList<KeyParamPair> keyParamPairList, List<DateTime> dateTimeList) {
    	this.userInput = userInput;
    	this.type = type;
    	this.keyParamPairs = keyParamPairList;
    	this.dateTimes = dateTimeList;
	}
    
    /**
     * Retrieves the type of command specified by the input.
     * 
     * @return KEYWORDS specifying the type of the command.
     */
    public Keywords getType() {
		return type;
		}

	/**
	 * Retrieves the list of KeyParamPair objects parsed from the input if any.
	 * 
	 * @return the list of KeyParamPair objects parsed from the input.
	 */
	public ArrayList<KeyParamPair> getParamPairs() {
		return keyParamPairs;
	}
	
	/**
	 * Retrieves the list of DateTime objects parsed from the input if any.
	 * 
	 * @return the list of DateTime objects parsed from the input.
	 */
	public List<DateTime> getDateTimes() {
		return dateTimes;
	}
	
	/**
	 * Checks if only the command keyword and its parameter is present.
	 * 
	 * @return true if there only the command keyword and its parameter is present.
	 */
	public boolean containsOnlyCommand() {
		if(keyParamPairs.size() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Iterates through the keyParamPair ArrayList and checks if any parameter is an empty string.
	 * 
	 * @return boolean If there is at least one empty string parameter, return true. Else, return false.
	 */
	public boolean containsEmptyParams() {
		for(KeyParamPair pair : keyParamPairs){
			if(pair.getParam().equals("")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if any dates are parsed.
	 * 
	 * @return true if at least one date has been parsed.
	 */
	public boolean containDates() {
		if(dateTimes.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o.getClass().equals(this.getClass())) {
			return this.getType().equals(((ParsedInput) o).getType()) &&
					this.getParamPairs().equals(((ParsedInput) o).getParamPairs());
		}
		return false;
	}
}
