package com.bocloud.emaas.common.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 简单实体，用来实现下拉列表数据
 * 
 * @author dmw
 *
 */
public class SimpleBean {

	private Long id;
	private String name;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param id
	 * @param name
	 */
	public SimpleBean(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public SimpleBean() {

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
