package com.bocloud.emaas.common.model;

public enum Relation {
	AND("AND"), // 与
	OR("OR");// 或

	private String relation;

	private Relation(String relation) {
		this.relation = relation;
	}

	/**
	 * @return the relation
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * @param relation
	 *            the relation to set
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

}
