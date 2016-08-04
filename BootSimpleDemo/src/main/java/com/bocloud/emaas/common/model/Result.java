package com.bocloud.emaas.common.model;

public class Result {

	private boolean success;// 操作是否成功 True|False
	private String message;// 操作返回的消息

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 */
	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param success
	 * @param message
	 */
	public Result(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message + "]";
	}

}
