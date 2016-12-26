/**
 * @Title: SwitchCpusModel.java
 * @Description: TODO(定义交换机cpu模型)
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 上午11：11：45
 * @version V1.0
 */
package com.dzg.model;

public class SwitchCpusModel {
	private String agentIp;
	private String oid;
//	private int cpus;
//	private String memory;
	private String value;
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
	
//	/**
//	 * @return the memory
//	 */
//	public String getMemory() {
//		return memory;
//	}
//	/**
//	 * @param memory the memory to set
//	 */
//	public void setMemory(String memory) {
//		this.memory = memory;
//	}
//
//	
//	public int getCpus() {
//		return cpus;
//	}
//	public void setCpus(int cpus) {
//		this.cpus = cpus;
//	}
	public String getAgentIp() {
		return agentIp;
	}
	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
}
