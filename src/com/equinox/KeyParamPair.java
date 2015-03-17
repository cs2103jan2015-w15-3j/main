package com.equinox;

public class KeyParamPair {
	private Keywords keyword;
	private String param;
	
	KeyParamPair(Keywords commandKeyword, String commandParam) {
		keyword = commandKeyword; // TODO: Propose using KEYOWRDS keyword = InputStringKeyword.getKeyword(commandKeyword); 
		param = commandParam;
	}

	public Keywords getKeyword() {
		return keyword;
	}

	public void setKeyword(Keywords keyword) { // TODO: Propose removal if unnecessary due to security of private fields
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
			Keywords xKeyword = this.getKeyword();
			Keywords yKeyword = ((KeyParamPair) other).getKeyword();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam);
		} else {
			return false;
		}
	}
}
