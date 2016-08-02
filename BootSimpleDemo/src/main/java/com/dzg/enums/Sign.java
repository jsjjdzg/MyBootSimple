package com.dzg.enums;

/**
 * 
 * SQL条件连接符
 * 
 * @author DZG
 * @since V1.0 2016年8月2日
 */
public enum Sign {
	EQ("="), // 等于
	UEQ("<>"), // 不等于
	GT(">"), // 大于
	LT("<"), // 小于
	GET(">="), // 大于等于
	LET("<="), // 小于等于
	LK("LIKE"), // like
	NUL("IS NULL");// 为空

	private String sign;
	
	private Sign(String sign){
		this.sign = sign;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
