package com.bocloud.emaas.common.model;

/**
 * 条件连接符
 * 
 * @author dmw
 *
 */
public enum Sign {

	EQ("="), // 等于
	UEQ("!="), // 不等于
	GT(">"), // 大于
	LT("<"), // 小于
	GET(">="), // 大于等于
	LET("<="), // 小于等于
	LK("like"), // like
	NUL("IS NULL");// 为空

	private String sign;

	private Sign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
}
