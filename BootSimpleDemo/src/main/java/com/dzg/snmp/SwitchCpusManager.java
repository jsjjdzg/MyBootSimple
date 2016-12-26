/**
 * @Title: NetworkTopologyManager.java
 * @Description:
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月12日 上午11：09：45
 * @version V1.0
 */
package com.dzg.snmp;

import java.io.IOException;

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

import com.dzg.model.SwitchCpusModel;


public class SwitchCpusManager extends SnmpUtil{
    private final static Logger log = Logger.getLogger(SwitchCpusManager.class);

    
    /**
     * 获取cpu等信息
     * @param ip
     * @param community
     * @param targetOid
     * @return
     */
	public SwitchCpusModel getcpu(String ip, String community, String targetOid) {
		log.debug("===========getcpu==========start==================");
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwitchCpusModel cpusModel = new SwitchCpusModel();
		cpusModel.setAgentIp(ip);
		cpusModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			int count = 0;  //cpu计数
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
				if (!finished) {		
					if(!vb.getVariable().toString().equals("0")) count++;
					log.info("vb.getVariable().toString()====="+vb.getVariable().toString()+", vb.toValueString()====="+vb.toValueString());
					log.info("[CPU:] :\t"+count);
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
			cpusModel.setValue(String.valueOf(count));
			
		} catch (Exception e) {
			log.error("[SwitchCpusManager]"+e.getMessage());
			log.error("SNMP walk Exception: " , e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}
		}
		
		return cpusModel;
	}
	
	
	
	/**
	 * 获取内存等信息
	 * @param ip
	 * @param community
	 * @param targetOid
	 * @return
	 */
	public SwitchCpusModel getMemory(String ip, String community, String targetOid) {
		log.debug("===========getMemory==========start==================");
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwitchCpusModel cpusModel = new SwitchCpusModel();
		cpusModel.setAgentIp(ip);
		cpusModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			String memory = "";  
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
				if (!finished) {		
					memory=vb.getVariable().toString();
//					log.info("vb.getVariable().toString()====="+vb.getVariable().toString()+", vb.toValueString()====="+vb.toValueString());
					
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
			cpusModel.setValue(memory);
			
		} catch (Exception e) {
			log.error("[SwitchCpusManager]"+e.getMessage());;
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

		return cpusModel;
	}
	
	@Test
	public void main() {
		System.out.println(getcpu("192.168.1.164","public","1").getValue());
	}
}
