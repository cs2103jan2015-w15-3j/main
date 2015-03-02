package com.equinox;
import java.util.ArrayList;

public class ParsedInput {
	
	private TYPE type;
	private ArrayList<KeyParamPair> paramPairList;
	
	public enum TYPE {
        ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;

    }
    
    public ParsedInput(TYPE cType, ArrayList<KeyParamPair> pairList) {
    	type = cType;
    	paramPairList= pairList;
	}
    
    public TYPE getType() {
		return type;
		}

	public void setType(TYPE type) {
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
	public void execute() {
        // TODO Auto-generated method stub

    }
}
