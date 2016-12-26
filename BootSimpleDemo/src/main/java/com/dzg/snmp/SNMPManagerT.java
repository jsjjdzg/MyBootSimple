/**
 * @Title: SNMPManager.java
 * @Description: TODO
 * @author lkx
 * @date 2016年11月28日 下午2:26:23
 * @version V1.0
 */

package com.dzg.snmp;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPManagerT {

	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	public static final int DEFAULT_RETRY = 3;

	// 记录日志类
	private final static Logger log = Logger.getLogger(SNMPManagerT.class);

	/**
	 * 创建对象communityTarget
	 * 
	 * @param targetAddress
	 * @param community
	 * @param version
	 * @param timeOut
	 * @param retry
	 * @return CommunityTarget
	 */
	public static CommunityTarget createDefault(String ip, String community) {
		Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip + "/" + DEFAULT_PORT);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(address);
		target.setVersion(DEFAULT_VERSION);
		target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
		target.setRetries(DEFAULT_RETRY);
		return target;
	}

	public static Vector<VariableBinding> snmpWalk(String ip, String community, String targetOid) {
		Vector<VariableBinding> variableBindings = new Vector<VariableBinding>();
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();

			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}
				// check finish or not
				finished = checkWalkFinished(targetOID, pdu, vb);
				if (!finished) {
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
					variableBindings.add(response.getVariableBindings().get(0));
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
		} catch (Exception e) {
			log.error("[SNMPManager][snmpWalk]=========" + e.getStackTrace());
			log.warn("SNMP walk Exception: " + e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}
		}
		return variableBindings;
	}

	/*
	 * 信息遍历校验
	 */
	private static boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			finished = true;
		} else if (vb.getOid() == null) {
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			finished = true;
		}
		return finished;
	}

	public static void main(String[] args) {
		// get ipRouteType oid 1.3.6.1.2.1.4.21.1.8
		String ipRouteType_oid = "1.3.6.1.2.1.4.21.1.8";
		String ipRouteNextHop_oid = "1.3.6.1.2.1.4.21.1.7";
		String ipRouteIfIndex_oid = "1.3.6.1.2.1.4.21.1.2";
		String ipRouteMask_oid = "1.3.6.1.2.1.4.21.1.11";
//		Vector<VariableBinding> variableBindings = SNMPManager.snmpWalk("192.168.1.1", "bocloud",
//				// ipRouteMask_oid);
//				// ipRouteType_oid);
//				ipRouteIfIndex_oid);

		String linux_oid = "1.3.6.1.2";
		//Vector<VariableBinding> variableBindings = SNMPManagerT.snmpWalk("192.168.1.1", "bocloud", ".1.3.6.1.2.1.4.1");
		Vector<VariableBinding> variableBindings = SNMPManagerT.snmpWalk("192.168.1.165", "public", ".1.3.6.1.2.1.1.1");
		//System.out.println(variableBindings.size());
		System.out.println(variableBindings.get(0).getVariable().toString());
		for (VariableBinding variableBinding : variableBindings) {
			System.out.println(variableBinding.toString());
		}
	}

}
