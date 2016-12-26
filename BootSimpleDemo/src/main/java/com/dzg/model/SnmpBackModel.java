/**
 * @Title: SnmpBackModel.java
 * @Description: return pack
 * Copyright: Copyright (c) 2016 * 
 * @author ouyangwulin
 * @date 2016年4月15日 下午4:59:35
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangwulin
 * @date 2016年4月15日 下午4:59:35
 */
public class SnmpBackModel implements Serializable {

	private static final long serialVersionUID = 4018703935345290005L;
	// 设备名称
	private String agentName;
	// 设备IP
	private String agentIp;
	private List<Snmpinfo> snmpinfo = new ArrayList<Snmpinfo>();
	private Boolean collectStatus;
	public static class Snmpinfo implements Serializable {

		private static final long serialVersionUID = 1L;
		private String oid;
		private String oidvalue;
		public String getOid() {
			return oid;
		}
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getOidvalue() {
			return oidvalue;
		}
		public void setOidvalue(String oidvalue) {
			this.oidvalue = oidvalue;
		}
		
	}

/*	// oidList
	private List<String> oidList;
	private String oid;*/
	// 设备类型
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public List<Snmpinfo> getSnmpinfo() {
		return snmpinfo;
	}

	public void setSnmpinfo(List<Snmpinfo> snmpinfo) {
		this.snmpinfo = snmpinfo;
	}

	public Boolean getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Boolean collectStatus) {
		this.collectStatus = collectStatus;
	}



}
