/**
 * @Title: SwtichCascadeManager.java
 * @Description: TODO（SNMPWALK得到交换机级联端口）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月16日 上午11：07：45
 * @version V1.0
 */
package com.dzg.snmp;

import java.io.IOException;
import java.util.ArrayList;

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

import com.dzg.model.SwtichCascadeModel;

public class SwtichCascadeManager extends SnmpUtil{
	// 记录日志类
    private final static Logger log = Logger.getLogger(SwtichCascadeManager.class);
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
					vb = response.get(0);  //oid返回值
				}
				// check finish or not
				finished = checkWalkFinished(targetOID, pdu, vb);
				if (!finished) {		
					String source = vb.getOid().toString(); 
					String out[] = StringUtils.split(source, ".");	
					String port = out[out.length-2];
					if(!flag){
						portList.add(port);
						flag = true;
					}else{
						for(int i=0,count=1; i<portList.size(); i++,count++){
							if(port.equals(portList.get(i))){
								break;
							}else{
								if(count == portList.size()){
									portList.add(port);
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
			//edited by liuzhiming 2016-08-08,oid不存在时返回null
			if(null != portList && portList.size() !=0){
				switchModel.setPortList(portList);
			}else{
				log.warn("====SwtichCascadeModel snmpWalkMapping=======no cascade port====");
				return null;
			}
		} catch (Exception e) {
			log.error("SwtichCascadeManager"+e.getMessage());
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
}
