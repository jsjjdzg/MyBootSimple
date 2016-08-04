package com.bocloud.emaas.database.exception;

public class BoCloudDBException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exceptionCode="00_00_00";
	
	
	/**
	 * @return the exceptionCode
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}
	public BoCloudDBException() {
		super();
	}
	public BoCloudDBException(String message) {
		super(message);
	}
}
