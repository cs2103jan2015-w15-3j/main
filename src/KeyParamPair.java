
public class KeyParamPair {
	String keyword;
	String param;
	
	KeyParamPair(String commandKeyword, String commandParam) {
		keyword = commandKeyword;
		param = commandParam;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
