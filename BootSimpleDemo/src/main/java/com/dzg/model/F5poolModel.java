/**
 * @Title: F5poolModel.java
 * @Description: TODO
 * Copyright: Copyright (c) 2016 * 
 * @author ouyangwulin
 * @date 2016年4月18日 下午4:36:09
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ouyangwulin
 * @date 2016年4月18日 下午4:36:09
 */
public class F5poolModel {
	private String agentIp;
	private String oid;
	private List<Poolinfo> poolinfo = new ArrayList<Poolinfo>();
	public static class Poolinfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private String ipaddr;
		private String port;
		private String poolname;
		private String macAddr;
		
		public String getMacAddr() {
			return macAddr;
		}
		public void setMacAddr(String macAddr) {
			this.macAddr = macAddr;
		}
		public String getIpaddr() {
			return ipaddr;
		}
		public void setIpaddr(String ipaddr) {
			this.ipaddr = ipaddr;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		public String getPoolname() {
			return poolname;
		}
		public void setPoolname(String poolname) {
			this.poolname = poolname;
		}
		@Override
		public String toString() {
			return "Poolinfo [ipaddr=" + ipaddr + ", port=" + port + ", poolname=" + poolname + ", macAddr=" + macAddr
					+ "]";
		}
		
	}
	public List<Poolinfo> getPoolinfo() {
		return poolinfo;
	}
	public void setPoolinfo(List<Poolinfo> poolinfo) {
		this.poolinfo = poolinfo;
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

}
