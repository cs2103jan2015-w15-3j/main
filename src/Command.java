
public class Command {
	
	public TYPE type;
	public KeyParamPair[] paramPairArray;
	
	public enum TYPE {
        ADD, MARK, DELETE, SEARCH, EDIT, DISPLAY, UNDO, ERROR;
    }
    
    public Command(TYPE cType, KeyParamPair[] pairArray) {
    	type = cType;
    	paramPairArray = pairArray;
	}
    
    public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public KeyParamPair[] getParamPairArray() {
		return paramPairArray;
	}
	
	public void execute() {
        // TODO Auto-generated method stub

    }

}
