/**
 * @Title: MacPortsModel.java
 * @Description: TODO(定义交换机mac-port模型)
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月16日 下午14:03:26
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;

public class MacPortsModel {
	    // 设备名称
		private String agentName;
		// 设备IP
		private String agentIp;
		// 端口
		private String clPort;
		// 共同体
		private String community;
		//设备类型
		private String deviceType;
	    private OidList oidlist;
		public static class OidList implements Serializable{
			private static final long serialVersionUID = 1L;
			private String cascadeoid;
			private String macportoid;
			private String indexoid;
			private String logicaloid;
			public String getCascadeoid() {
				return cascadeoid;
			}
			public void setCascadeoid(String cascadeoid) {
				this.cascadeoid = cascadeoid;
			}
			public String getMacportoid() {
				return macportoid;
			}
			public void setMacportoid(String macportoid) {
				this.macportoid = macportoid;
			}
			public String getIndexoid() {
				return indexoid;
			}
			public void setIndexoid(String indexoid) {
				this.indexoid = indexoid;
			}
			public String getLogicaloid() {
				return logicaloid;
			}
			public void setLogicaloid(String logicaloid) {
				this.logicaloid = logicaloid;
			}
			
		}

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


		public String getDeviceType() {
			return deviceType;
		}


		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}


		public OidList getOidlist() {
			return oidlist;
		}


		public void setOidlist(OidList oidlist) {
			this.oidlist = oidlist;
		}
		@Override
		public String toString() {
			return "MacPortsModel [agentName=" + agentName + ", agentIp="
					+ agentIp + ", clPort=" + clPort + ", community="
					+ community + ", deviceType=" + deviceType + ", oidlist="
					+ oidlist + "]";
		}

}
