package com.dzg.enums;

/**
 * 
 * SQL关系Enum
 * 
 * @author DZG
 * @since V1.0 2016年8月2日
 */
public enum Relation {
	AND("AND"),OR("OR");//构造
	
	private String relation;
	
	private Relation(String relation){
		this.relation = relation;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
}
