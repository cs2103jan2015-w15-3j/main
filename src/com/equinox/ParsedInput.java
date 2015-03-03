package com.equinox;
import java.util.ArrayList;

public class ParsedInput {
	
	private KEYWORDS type;
	private ArrayList<KeyParamPair> paramPairList;
    
    public ParsedInput(KEYWORDS cType, ArrayList<KeyParamPair> pairList) {
    	type = cType;
    	paramPairList= pairList;
	}
    
    public KEYWORDS getType() {
		return type;
		}

	public void setType(KEYWORDS type) {
		this.type = type;
	}

	public ArrayList<KeyParamPair> getParamPairList() {
		return paramPairList;
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
