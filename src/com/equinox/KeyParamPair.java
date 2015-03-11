package com.equinox;

public class KeyParamPair {
	private KEYWORDS keyword;
	private String param;
	
	KeyParamPair(KEYWORDS commandKeyword, String commandParam) {
		keyword = commandKeyword; // TODO: Propose using KEYOWRDS keyword = InputStringKeyword.getKeyword(commandKeyword); 
		param = commandParam;
	}

	public KEYWORDS getKeyword() {
		return keyword;
	}

	public void setKeyword(KEYWORDS keyword) { // TODO: Propose removal if unnecessary due to security of private fields
		this.keyword = keyword;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) { // TODO: Propose removal if unnecessary due to security of private fields
		this.param = param;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other.getClass() == this.getClass()) {
			KEYWORDS xKeyword = this.getKeyword();
			KEYWORDS yKeyword = ((KeyParamPair) other).getKeyword();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam);
		} else {
			return false;
		}
	}
}
