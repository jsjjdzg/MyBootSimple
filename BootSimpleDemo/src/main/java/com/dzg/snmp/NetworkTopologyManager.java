/**
 * @Title: NetworkTopologyManager.java
 * @Description: TODO（SNMPWALK得到交换机mac地址与端口映射关系）
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年6月16日 上午11：13：45
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

import com.dzg.model.SwitchTopologyModel;
import com.dzg.model.SwitchTopologyModel.Poolinfo;


public class NetworkTopologyManager extends SnmpUtil{
	// 记录日志类
    private final static Logger log = Logger.getLogger(NetworkTopologyManager.class);

	public SwitchTopologyModel snmpWalkMapping(String ip, String community, String targetOid) {
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		SwitchTopologyModel topologyModel = new SwitchTopologyModel();
		topologyModel.setAgentIp(ip);
		topologyModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();
			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));
			List<Poolinfo> poolinfolist = new ArrayList<Poolinfo>();
			boolean finished = false;
			boolean flag = true;
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
					String port = vb.getVariable().toString();  //交换机端口号
					String out[] = StringUtils.split(source, ".");					
					int length = out.length;
					StringBuffer macAddr = new StringBuffer();	
					//edited by liuzhiming 2016-08-08,得到10进制mac地址，中间去除'.'
					/*************拼接mac地址************/
					for (int i=length-6;i<length;i++)
					{
						macAddr.append(out[i]);
					}

					if(flag){
						
						Poolinfo poolinfo = new Poolinfo();					
						poolinfo.setMacAddr(macAddr.toString());
						poolinfo.setPort(port);
						poolinfolist.add(poolinfo);
						flag = false;
					}else{
						 for(int i=0,count=1; i<poolinfolist.size(); i++,count++){
							if(macAddr.toString().equals(poolinfolist.get(i).getMacAddr())&& port.equals(poolinfolist.get(i).getPort())){
							    break;	//遍历过程中有重复macAddr及port值，退出循环						
							}else{
								if(count==poolinfolist.size()){//遍历完无重复值，插入新值
									Poolinfo poolinfo = new Poolinfo();					
									poolinfo.setMacAddr(macAddr.toString());
									poolinfo.setPort(port);
									poolinfolist.add(poolinfo);
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
			if(poolinfolist.size() !=0 && null != poolinfolist){
				topologyModel.setPoolinfo(poolinfolist);
			}else{
				log.warn("====SwitchTopologyModel snmpWalkMapping=======no mac-port====");
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
		return topologyModel;
	}
}
