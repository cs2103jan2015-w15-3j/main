package com.equinox;

public class KeyParamPair {
	private Keywords keyword;
	private String param;
	
	KeyParamPair(Keywords commandKeyword, String commandParam) {
		keyword = commandKeyword;
		param = commandParam;
	}

	public Keywords getKeyword() {
		return keyword;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
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
