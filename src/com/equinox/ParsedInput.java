package com.equinox;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class ParsedInput {
	
	private String userInput;
	private KEYWORDS type;
	private ArrayList<KeyParamPair> keyParamPairList;
	private List<DateTime> dateTimeList;
    
    public ParsedInput(String userInput, KEYWORDS type, ArrayList<KeyParamPair> keyParamPairList, List<DateTime> dateTimeList) {
    	this.userInput = userInput;
    	this.type = type;
    	this.keyParamPairList = keyParamPairList;
    	this.dateTimeList = dateTimeList;
	}
    
    public KEYWORDS getType() {
		return type;
		}

	public ArrayList<KeyParamPair> getParamPairList() {
		return keyParamPairList;
	}
	
	public List<DateTime> getDateTimeList() {
		return dateTimeList;
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
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass().equals(this.getClass())) {
			return this.getType().equals(((ParsedInput) o).getType()) &&
					this.getParamPairList().equals(((ParsedInput) o).getParamPairList());
		}
		return false;
	}
}
