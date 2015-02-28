import java.util.ArrayList;

public class ParsedInput {
	
	public TYPE type;
	public ArrayList<KeyParamPair> paramPairList;
	
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
	
	public void execute() {
        // TODO Auto-generated method stub

    }

}
