/**
 * @Title: NetworkTopologyManager.java
 * @Description: TODO（SNMPWALK得到交换机端口及其状态的映射关系）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 上午11：14：45
 * @version V1.0
 */
package com.dzg.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import com.dzg.model.SwitchPortStatusModel;
import com.dzg.model.SwitchPortStatusModel.Poolinfo;

public class SwitchPortStatusManager extends SnmpUtil{
	private final static Logger log = Logger.getLogger(SwitchPortStatusManager.class);

	public SwitchPortStatusModel snmpWalkMapping(String ip, String community, String targetOid) {
		log.debug("===========SwitchPortStatusManager==========start==================");
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwitchPortStatusModel portStatusModel = new SwitchPortStatusModel();
		portStatusModel.setAgentIp(ip);
		portStatusModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			List<Poolinfo> poolinfolist = new ArrayList<Poolinfo>();
			boolean finished = false;
			int count = 0; //统计端口数
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
//					log.info("responsePDU == null");
					finished = true;
					break;
				} else {
					vb = response.get(0);  //oid返回值
				}
				// check finish or not
				finished = checkWalkFinished(targetOID, pdu, vb);
//				log.info("=======================finished=="+finished);
				if (!finished) {	
					count++;
					String source = vb.getOid().toString();     
					System.err.println(source);
					String portStatus = vb.getVariable().toString();  //交换机端口号
					String out[] = StringUtils.split(source, ".");					
					Poolinfo poolinfo = new Poolinfo();					
					poolinfo.setPort(out[out.length-1]);
					poolinfo.setPortStatus(portStatus);
					poolinfolist.add(poolinfo);
//					log.info("[port:] :\t"+out[out.length-1] +" \t[portStatus]:\t"+portStatus);
					
					
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
			portStatusModel.setPoolinfo(poolinfolist);
			portStatusModel.setPortNumber(count);
		} catch (Exception e) {
			log.error("SwitchPortStatusManager"+e.getMessage());
			log.warn("【SwitchPortStatusManager】SNMP walk Exception: " + e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}
		}
		
		return portStatusModel;
	}
	
	@Test
	public void main() {
		snmpWalkMapping("192.168.1.1", "bocloud", ".1.3.6.1.2.1.4.21.1.2").getPoolinfo().parallelStream()
				.forEach(t -> System.out.println(t.toString()));
	}
}
