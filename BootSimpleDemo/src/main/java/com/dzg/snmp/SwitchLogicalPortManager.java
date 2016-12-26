/**
 * @Title: SwtichBundleManager.java
 * @Description: TODO（SNMPWALK得到交换机逻辑端口及其端口索引）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年8月10日 上午10：45：45
 * @version V1.0
 */
package com.dzg.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.dzg.model.SwitchLogicalPortModel;
import com.dzg.model.SwitchLogicalPortModel.Poolinfo;

public class SwitchLogicalPortManager extends SnmpUtil{
    private final static Logger log = Logger.getLogger(SwitchLogicalPortManager.class);

	public SwitchLogicalPortModel snmpWalkMapping(String ip, String community, String targetOid) {
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwitchLogicalPortModel portModel = new SwitchLogicalPortModel();
		portModel.setAgentIp(ip);
		portModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			List<Poolinfo> poolinfolist = new ArrayList<Poolinfo>();
			boolean finished = false;
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);  //oid返回值
				}
				finished = checkWalkFinished(targetOID, pdu, vb);

				if (!finished) {		
					
					String source = vb.getOid().toString();     
					String indexPort = vb.getVariable().toString();  //端口索引
					String out[] = StringUtils.split(source, ".");					
					String logicalPort = out[out.length-1];    //逻辑端口
					Poolinfo poolinfo = new Poolinfo();					
					poolinfo.setIndexPort(indexPort);
					poolinfo.setLogicalPort(logicalPort);
					poolinfolist.add(poolinfo);
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					snmp.close();
				}
			}
			if(poolinfolist.size() !=0 && null != poolinfolist){
				portModel.setPoolinfo(poolinfolist);
			}else{
				log.warn("====SwitchLogicalPortModel snmpWalkMapping=======no logical port====");
				return null;
			}
		} catch (Exception e) {
			log.error("[NetworkTopologyManager]" , e);
			log.warn("SNMP walk Exception: " , e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}
		}
		return portModel;
	}
}
