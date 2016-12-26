package com.dzg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SwitchLogicalPortModel {
	private String agentIp;
	private String oid;
	private List<Poolinfo> poolinfo = new ArrayList<Poolinfo>();
	public static class Poolinfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private String logicalPort;
		private String indexPort;
		public String getLogicalPort() {
			return logicalPort;
		}
		public void setLogicalPort(String logicalPort) {
			this.logicalPort = logicalPort;
		}
		public String getIndexPort() {
			return indexPort;
		}
		public void setIndexPort(String indexPort) {
			this.indexPort = indexPort;
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
