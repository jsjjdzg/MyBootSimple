/**
 * @Title: SnmpUtil.java
 * @Description: TODO（定义SnmpUtil类，包含常用变量及方法）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 上午11：06：45
 * @version V1.0
 */
package com.dzg.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

public abstract class SnmpUtil {
	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	public static final int DEFAULT_RETRY = 3;

	protected boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
		// TODO Auto-generated method stub
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			finished = true;
		} else if (vb.getOid() == null) {
			finished = true;
		} 
			else if (vb.getOid().size() < targetOID.size()) {
			finished = true;
		}else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			finished = true;
		}
		return finished;
	}

	protected CommunityTarget createDefault(String ip, String community) {
		// TODO Auto-generated method stub
		Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip
				+ "/" + DEFAULT_PORT);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(address);
		target.setVersion(DEFAULT_VERSION);
		target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
		target.setRetries(DEFAULT_RETRY);
		return target;
	}
}
