/**
 * @Title: SwtichBundleManager.java
 * @Description: TODO（SNMPWALK得到交换机聚合端口）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年8月10日 上午9：17：45
 * @version V1.0
 */

package com.dzg.snmp;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.dzg.model.SwtichCascadeModel;

public class SwtichBundleManager extends SnmpUtil {
	private final static Logger log = Logger.getLogger(SwtichBundleManager.class);
	private static final String CONSTANT_ZERO = "0";//

	public SwtichCascadeModel snmpWalkMapping(String ip, String community, String targetOid) {
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwtichCascadeModel switchModel = new SwtichCascadeModel();
		switchModel.setAgentIp(ip);
		switchModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			boolean flag = false;
			ArrayList<String> portList = new ArrayList<String>();
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0); // oid返回值
				}
				// check finish or not
				finished = checkWalkFinished(targetOID, pdu, vb);
				if (!finished) {
					String port = vb.getVariable().toString(); // 聚合口
					if (!port.equals(CONSTANT_ZERO)) {
						if (!flag) {
							portList.add(port); // 得到第一个聚合口
							flag = true;
						} else {
							for (int i = 0, count = 1; i < portList.size(); i++, count++) {
								if (port.equals(portList.get(i))) {
									break;
								} else {
									if (count == portList.size()) {
										portList.add(port); // 得到第n个聚合口
									}
								}
							}
						}
					}
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					snmp.close();
				}
			}
			if (null != portList && portList.size() != 0) {
				switchModel.setPortList(portList);
			} else {
				log.warn("====SwtichBundleManager snmpWalkMapping=======no blund port====");
				return null;
			}
		} catch (Exception e) {
			log.error("SwtichCascadeManager" + e.getMessage());
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
		return switchModel;
	}

	@Test
	public void main() {
		snmpWalkMapping("192.168.1.164", "public", "1").getPortList().parallelStream()
				.forEach(t -> System.out.println(t.toString()));
	}
}
