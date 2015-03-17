package com.equinox;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class ParsedInput {
	
	private String userInput;
	private KEYWORDS type;
	private ArrayList<KeyParamPair> keyParamPairList;
	private List<DateTime> dateTimeList;
    
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
    public ParsedInput(String userInput, KEYWORDS type, ArrayList<KeyParamPair> keyParamPairList, List<DateTime> dateTimeList) {
    	this.userInput = userInput;
    	this.type = type;
    	this.keyParamPairList = keyParamPairList;
    	this.dateTimeList = dateTimeList;
	}
    
    /**
     * Retrieves the type of command specified by the input.
     * 
     * @return KEYWORDS specifying the type of the command.
     */
    public KEYWORDS getType() {
		return type;
		}

	/**
	 * Retrieves the list of KeyParamPair objects parsed from the input if any.
	 * 
	 * @return the list of KeyParamPair objects parsed from the input.
	 */
	public ArrayList<KeyParamPair> getParamPairList() {
		return keyParamPairList;
	}
	
	/**
	 * Retrieves the list of DateTime objects parsed from the input if any.
	 * 
	 * @return the list of DateTime objects parsed from the input.
	 */
	public List<DateTime> getDateTimeList() {
		return dateTimeList;
	}
	
	/**
	 * Checks if there are no keywords other than one specifying the command type.
	 * 
	 * @return true if there are no keywords other than one specifying the command type.
	 */
	public boolean containsNoOtherKeywords() {
		if(keyParamPairList.size() == 1) {
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
		for(KeyParamPair pair : keyParamPairList){
			if(pair.getParam().equals("")){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if no dates are parsed.
	 * 
	 * @return true if no dates are parsed.
	 */
	public boolean containsNoDates() {
		if(dateTimeList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o.getClass().equals(this.getClass())) {
			return this.getType().equals(((ParsedInput) o).getType()) &&
					this.getParamPairList().equals(((ParsedInput) o).getParamPairList());
		}
		return false;
	}
}
