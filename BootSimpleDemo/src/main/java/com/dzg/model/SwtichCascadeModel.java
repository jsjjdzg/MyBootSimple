/**
 * @Title: SwtichCascadeModel.java
 * @Description: TODO（定义交换机级联端口模型）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月16日 上午11：11：45
 * @version V1.0
 */
package com.dzg.model;

import java.util.List;

public class SwtichCascadeModel {
	private String agentIp;
	private String oid;
	private List<String> portList;

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

	public List<String> getPortList() {
		return portList;
	}

	public void setPortList(List<String> portList) {
		this.portList = portList;
	}

	@Override
	public String toString() {
		return "SwtichCascadeModel [agentIp=" + agentIp + ", oid=" + oid + ", portList=" + portList + "]";
	}

}
