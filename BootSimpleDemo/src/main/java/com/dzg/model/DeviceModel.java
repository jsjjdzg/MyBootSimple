/**
 * @Title: DeviceModel.java
 * @Description: TODO
 * Copyright: Copyright (c) 2016 * 
 * @author ouyangwulin
 * @date 2016年4月13日 下午4:57:26
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author ouyangwulin
 * @date 2016年4月13日 下午4:57:26
 */
@JsonRootName(value = "DeviceModel")
@JsonIgnoreProperties
public class DeviceModel implements Serializable{

	private static final long serialVersionUID = 1L;

	// 设备名称
	private String agentName;
	// 设备IP
	private String agentIp;
	// 端口
	private String clPort;
	// 共同体
	private String community;
	//oidList
	private List<String> oidList;
	private String oid;//基本不用 用list
	//设备类型
	private String deviceType;

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentIp() {
		return agentIp;
	}

	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
	}

	public String getClPort() {
		return clPort;
	}

	public void setClPort(String clPort) {
		this.clPort = clPort;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public List<String> getOidList() {
		return oidList;
	}

	public void setOidList(List<String> oidList) {
		this.oidList = oidList;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
