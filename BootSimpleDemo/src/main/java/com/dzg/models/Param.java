package com.dzg.models;

import java.util.Map;

import com.dzg.enums.Relation;
import com.dzg.enums.Sign;

/**
 * 
 * SQL参数类
 * 
 * @author DZG
 * @since V1.0 2016年8月2日
 */
public class Param {
	//SQL关系 eg:and或者or
	private Relation relation;
	//对应参数值
	private Map<String, Object> param;
	//SQL连接符 eg:<> LIKE IS NULL
	private Sign sign;

	public Param() {
		super();
	}

	public Param(Relation relation, Map<String, Object> param, Sign sign) {
		super();
		this.relation = relation;
		this.param = param;
		this.sign = sign;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public Sign getSign() {
		return sign;
	}

	public void setSign(Sign sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "Param [relation=" + relation + ", param=" + param + ", sign=" + sign + "]";
	}

}
