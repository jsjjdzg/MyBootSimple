package com.bocloud.emaas.common.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Param {

	private Relation relation = Relation.AND;
	private Map<String, Object> param;
	private Sign sign;

	/**
	 * @return the relation
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * @param relation
	 *            the relation to set
	 */
	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	/**
	 * @return the param
	 */
	public Map<String, Object> getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	/**
	 * @return the sign
	 */
	public Sign getSign() {
		return sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(Sign sign) {
		this.sign = sign;
	}

	/**
	 * 
	 */
	public Param() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param param
	 * @param sign
	 */
	public Param(Map<String, Object> param, Sign sign) {
		super();
		this.relation = Relation.AND;
		this.param = param;
		this.sign = sign;
	}

	/**
	 * @param relation
	 * @param param
	 * @param sign
	 */
	public Param(Relation relation, Map<String, Object> param, Sign sign) {
		super();
		this.relation = relation;
		this.param = param;
		this.sign = sign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
