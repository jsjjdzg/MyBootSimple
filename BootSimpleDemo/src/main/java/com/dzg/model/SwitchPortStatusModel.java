/**
 * @Title: SwitchPortStatusModel.java
 * @Description: TODO(定义交换机端口状态模型)
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 下午20：01：45
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SwitchPortStatusModel {
	private String agentIp;
	private String oid;
	private List<Poolinfo> poolinfo = new ArrayList<Poolinfo>();
	private int portNumber;
	public static class Poolinfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private String portStatus;
		private String port;
		public String getPortStatus() {
			return portStatus;
		}
		public void setPortStatus(String portStatus) {
			this.portStatus = portStatus;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		@Override
		public String toString() {
			return "Poolinfo [portStatus=" + portStatus + ", port=" + port + "]";
		}
				
	}
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
	public List<Poolinfo> getPoolinfo() {
		return poolinfo;
	}
	public void setPoolinfo(List<Poolinfo> poolinfo) {
		this.poolinfo = poolinfo;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
}
