package com.equinox;

//@author A0115983X
public class KeyParamPair {
	private Keywords keyword;
	private String keyString;
	private String param;

	KeyParamPair(Keywords inputKeyword, String inputKeyString, String inputParam) {
		keyword = inputKeyword;
		keyString = inputKeyString;
		param = inputParam;
	}

	public Keywords getKeyword() {
		return keyword;
	}

	public String getParam() {
		return param;
	}

	public String getKeyString() {
		return keyString;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public boolean equals(Object other) {
		if (other.getClass() == this.getClass()) {
			Keywords xKeyword = this.getKeyword();
			Keywords yKeyword = ((KeyParamPair) other).getKeyword();
			String xKeyString = this.getKeyString();
			String yKeyString = ((KeyParamPair) other).getKeyString();
			String xParam = this.getParam();
			String yParam = ((KeyParamPair) other).getParam();
			return xKeyword.equals(yKeyword) && xParam.equals(yParam)
					&& xKeyString.equals(yKeyString);
		} else {
			return false;
		}
	}
}
