package com.dzg.snmp;

import com.alibaba.fastjson.JSONObject;

public class Interface {
	public int ifIndex;

	// interface name
	public String ifDescr;

	public IfType ifType;

	public IfType getIfType() {
		return ifType;
	}

	public void setIfType(IfType ifType) {
		this.ifType = ifType;
	}

	public enum IfType {

		other(1), ethernetCsmacd(6), ppp(23), softwareLoopback(24), propMultiplexor(54), tunnel(131), vmwareVirtualNic(
				258);

		IfType(int type) {
			this.type = type;
		}

		public int type;

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public static IfType getIfType(int type) {
			switch (type) {
			case 1:
				return IfType.other;
			case 6:
				return IfType.ethernetCsmacd;
			case 23:
				return IfType.ppp;
			case 24:
				return IfType.softwareLoopback;
			case 54:
				return IfType.propMultiplexor;
			case 131:
				return IfType.tunnel;
			case 258:
				return IfType.vmwareVirtualNic;
			default:
				return IfType.other;
			}
		}
	}

	public int ifMtu;

	public int ifSpeed;

	// mac address
	public String ifPhysAddress;

	public IfAdminStatus ifAdminStatus;

	public IfAdminStatus getIfAdminStatus() {
		return ifAdminStatus;
	}

	public void setIfAdminStatus(IfAdminStatus ifAdminStatus) {
		this.ifAdminStatus = ifAdminStatus;
	}

	public enum IfAdminStatus {
		up(1), down(2), testing(3);

		IfAdminStatus(int adminStatus) {
			this.adminStatus = adminStatus;
		}

		public int adminStatus;

		public int getAdminStatus() {
			return adminStatus;
		}

		public void setAdminStatus(int adminStatus) {
			this.adminStatus = adminStatus;
		}

		public static IfAdminStatus getifAdminStatus(int adminStatus) {
			switch (adminStatus) {
			case 1:
				return IfAdminStatus.up;
			case 2:
				return IfAdminStatus.down;
			case 3:
				return IfAdminStatus.testing;
			default:
				return IfAdminStatus.testing;
			}
		}
	}

	public int ifInOctets;

	public int ifInErrors;

	public int ifOutOctets;

	public int ifOutErrors;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
