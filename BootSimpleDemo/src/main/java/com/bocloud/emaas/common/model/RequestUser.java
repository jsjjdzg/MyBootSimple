package com.bocloud.emaas.common.model;

/**
 * 请求用户对象类
 * @author dmw
 *
 */
public class RequestUser {

	private Long id;

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
	 * @param id
	 */
	public RequestUser(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 */
	public RequestUser() {
		super();
		// TODO Auto-generated constructor stub
	}

}
