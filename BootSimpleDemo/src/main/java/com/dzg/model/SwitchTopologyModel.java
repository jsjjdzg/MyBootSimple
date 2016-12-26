/**
 * @Title: SwitchTopologyModel.java
 * @Description: TODO（定义交换机网络拓扑模型）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 上午11：00：45
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dzg.model.F5poolModel.Poolinfo;

public class SwitchTopologyModel {
	private String agentIp;
	private String oid;
	private List<Poolinfo> poolinfo = new ArrayList<Poolinfo>();
	public static class Poolinfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private String macAddr;
		private String port;
		
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getMacAddr() {
			return macAddr;
		}
		public void setMacAddr(String macAddr) {
			this.macAddr = macAddr;
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
}
