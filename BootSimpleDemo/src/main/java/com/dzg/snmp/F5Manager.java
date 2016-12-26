/**
 * @Title: F5Manager.java
 * @Description: TODO
 * @author Testing
 * @date 2016年4月14日 下午2:26:23
 * @version V1.0
 */

package com.dzg.snmp;

/**
 * @author Testing
 *
 */
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
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.dzg.model.F5poolModel;
import com.dzg.model.F5poolModel.Poolinfo;

/**
 * @author Testing
 *
 */
public class F5Manager 
{
	
	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	public static final int DEFAULT_RETRY = 3;
	
    // 记录日志类
    private final static Logger log = Logger.getLogger(F5Manager.class);

	/**
	 * 创建对象communityTarget
	 * @param targetAddress
	 * @param community
	 * @param version
	 * @param timeOut
	 * @param retry
	 * @return CommunityTarget
	 */
	public static CommunityTarget createDefault(String ip, String community) {
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
	
	/*获取表格*/
	public static F5poolModel snmpWalkMapping(String ip, String community, String targetOid)
	{
		log.debug("==========F5poolModel snmpWalkMapping===Oid="+targetOid+",ip=="+ip+",community=="+community);
		CommunityTarget target = createDefault(ip, community);
		TransportMapping transport = null;
		Snmp snmp = null;
		F5poolModel f5poolModel = new F5poolModel();
		f5poolModel.setAgentIp(ip);
		f5poolModel.setOid(targetOid);
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			transport.listen();

			PDU pdu = new PDU();
			OID targetOID = new OID(targetOid);
			pdu.add(new VariableBinding(targetOID));

			boolean finished = false;
			List<Poolinfo> poolinfolist = new ArrayList<Poolinfo>();
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
					
					String source = vb.getOid().toString();
					String poolName = vb.getVariable().toString();
					
					String out[] = StringUtils.split(source, ".");
					
					int length = out.length;
					StringBuffer format = new StringBuffer();
					for (int i=length-5;i<length;i++)
					{
						format.append(out[i]);
						
						if(i==length-2)
						{
							format.append(":");
						}
						else if (i==length-1)
						{
							format=format;
						}
						else
						{
							format.append(".");
						}
					}
					String[] info = format.toString().split(":");
					Poolinfo poolinfo = new Poolinfo();
					poolinfo.setIpaddr(info[0]);
					poolinfo.setPort(info[1]);
					poolinfo.setPoolname(poolName);
					poolinfolist.add(poolinfo);
//					log.info("[IP] :\t"+info[0] + " \t[Port] :\t"+info[1]+" \t[Pool Name]:\t"+poolName);
					
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
				
			}
			f5poolModel.setPoolinfo(poolinfolist);
		} catch (Exception e) {
			log.error("[F5Manager]========="+e.getStackTrace());
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
		
		return f5poolModel;
	}
	
	/*
	 * Get the pool Name (Only For Getting Pool Name)
	 * */
	
	public static void snmpWalkPoolName(String ip, String community, String targetOid)
	{
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
					
					String poolName = vb.getVariable().toString();
					
//					log.info(" \t[Pool Name]:\t"+poolName);
					
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
		} catch (Exception e) {
			log.error("[F5Manager]========="+e.getStackTrace());
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
	}
	
	/*
	 * Get The F5 Used Port (Only For Getting F5 Port)
	 * */
	
	public static void snmpWalkUsedPort(String ip, String community, String targetOid)
	{
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
					
					String poolName = vb.getVariable().toString();
					
//					log.info(" \t[Used Port Number]:\t"+poolName);
					
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
		} catch (Exception e) {
			log.error("[F5Manager][snmpWalkUsedPort]========="+e.getStackTrace());
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
	}
	
	
	/*
	 * 信息遍历校验
	 * */
	private static boolean checkWalkFinished(OID targetOID, PDU pdu,
			VariableBinding vb) {
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
	
	@Test
	public void main() {
		snmpWalkMapping("192.168.1.1", "bocloud", ".1.3.6.1.2.1.4.21.1.2").getPoolinfo().parallelStream()
				.forEach(t -> System.out.println(t.toString()));
	}
}
