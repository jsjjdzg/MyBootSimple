package com.dzg.snmp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.dzg.snmp.model.SnmpBackModel.Snmpinfo;

/**
 * 
 * SNMP信息处理类
 * 
 * @author DZG
 * @since V1.0 2016年11月28日
 */
public class SnmpManager extends SnmpUtil{

	private final static Logger log = Logger.getLogger(SnmpManager.class);

	// 错误消息
	private String errorMassage;

	// SNMP的协议版本
	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	// 访问协议
	public static final String DEFAULT_PROTOCOL = "udp";
	// 默认端口
	public static final int DEFAULT_PORT = 161;
	// 超时时间
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	// 重试次数
	public static final int DEFAULT_RETRY = 3;

	/**
	 * @param targetAddress
	 * @param community
	 * @param version
	 * @param timeOut
	 * @param retry
	 * @return CommunityTarget
	 * 
	 *         update 2016-04-14 Testing
	 */
	public CommunityTarget createDefault(String ip, String community) {
		Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip + "/" + DEFAULT_PORT);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(address);
		target.setVersion(DEFAULT_VERSION);
		// Set the timeout (milliseconds)
		target.setTimeout(DEFAULT_TIMEOUT);
		target.setRetries(DEFAULT_RETRY);
		return target;
	}

	/**
	 * 根据IP,community,oid获取SNMP信息
	 * 
	 * @since 2016年11月28日
	 */
	public Vector<VariableBinding> snmpWalk(String ip, String community, String targetOid) {
		Vector<VariableBinding> variableBindings = new Vector<VariableBinding>();
		CommunityTarget target = createDefault(ip, community);
		TransportMapping<?> transport = null;
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
					//log.debug("SNMP walk OID has finished.");
					snmp.close();
				}
			}
		} catch (Exception e) {
			//log.error("[SNMPManager][snmpWalk]=========" + e.getStackTrace());
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
	
	/**
	 * SNMP主动采集设备性能
	 * 
	 * @param ip
	 * @param community
	 * @param oid
	 * @throws IOException
	 */
	public String snmpGet(DeviceModel deviceBean, String oid) {

		String ip = deviceBean.getAgentIp();
		String prot = deviceBean.getClPort();
		String community = deviceBean.getCommunity();
		String deviceName = deviceBean.getAgentName();

		if (!verification(deviceName, ip, prot, oid, community)) {
			return null;
		}

		// agent对象(Testing update)
		CommunityTarget target = createDefault(ip, community);

		Snmp snmp = null;
		ResponseEvent response = null;
		DefaultUdpTransportMapping udpTransportMapping = null;

		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oid)));
		pdu.setType(PDU.GET);

		try {
			udpTransportMapping = new DefaultUdpTransportMapping();
			udpTransportMapping.listen();
			snmp = new Snmp(udpTransportMapping);
			// 发送同步消息
			long oidStartTime = System.currentTimeMillis();
			response = snmp.send(pdu, target);
			log.debug("[1]设备采集(单节点):IP[" + deviceBean.getAgentIp() + "]采集时间["
					+ (System.currentTimeMillis() - oidStartTime) + "]oid[" + oid + "]采集状态[]");
			PDU responsePdu = response.getResponse();
			if (responsePdu == null) {
				this.errorMassage = "IP(" + ip + "),community(" + community + "),oid(" + oid + ")采集超时！";
				log.warn(errorMassage);
				return "OVER_TIME";
			} else {
				Vector<?> vbVect = responsePdu.getVariableBindings();
				log.debug(ip + "vb size:" + vbVect.size());
				if (vbVect.size() == 0) {
					this.errorMassage = "IP(" + ip + "),community(" + community + "),oid(" + oid + ")没有采集到数据！";
					log.warn(errorMassage);
					return "NULL_DATA";
				} else {
					Object obj = vbVect.firstElement();
					VariableBinding vb = (VariableBinding) obj;
					log.debug("IP(" + ip + "),community(" + community + "),oid(" + oid + ")采集到数据："
							+ vb.getVariable().toString());
					return vb.getVariable().toString();
				}
			}
		} catch (Exception e) {
			log.error("IP(" + ip + "),community(" + community + "),oid(" + oid + ")采集数据出现异常！", e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}

			if (udpTransportMapping != null) {
				try {
					udpTransportMapping.close();
				} catch (IOException ex2) {
					udpTransportMapping = null;
				}
			}
		}
		return null;
	}

	/*
	 * 同步获取信息值
	 */
	public SnmpBackModel snmpGet(DeviceModel deviceBean, List<String> oidList) {

		String ip = deviceBean.getAgentIp();
		String prot = deviceBean.getClPort();
		String community = deviceBean.getCommunity();
		String deviceName = deviceBean.getAgentName();
		String deviceType = deviceBean.getDeviceType();
		// set back model
		SnmpBackModel snmpbackmodel = new SnmpBackModel();
		snmpbackmodel.setAgentIp(ip);
		snmpbackmodel.setAgentName(deviceName);
		snmpbackmodel.setDeviceType(deviceType);

		if (!verification(deviceName, ip, prot, oidList.get(0), community)) {
			return null;
		}

		// agent对象(Testing update)
		CommunityTarget target = createDefault(ip, community);

		Snmp snmp = null;
		ResponseEvent response = null;
		DefaultUdpTransportMapping udpTransportMapping = null;

		PDU pdu = new PDU();
		for (String oid : oidList) {
			pdu.add(new VariableBinding(new OID(oid)));
		}
		pdu.setType(PDU.GET);

		try {
			udpTransportMapping = new DefaultUdpTransportMapping();
			udpTransportMapping.listen();
			snmp = new Snmp(udpTransportMapping);
			// 发送同步消息
			long oidStartTime = System.currentTimeMillis();
			response = snmp.send(pdu, target);
			log.debug("[1]设备采集(单节点):IP[" + deviceBean.getAgentIp() + "]采集时间["
					+ (System.currentTimeMillis() - oidStartTime) + "]oid[" + oidList + "]采集状态[]");
			PDU responsePdu = response.getResponse();
			if (responsePdu == null) {
				this.errorMassage = "IP(" + ip + "),community(" + community + "),oid(" + oidList + ")采集超时！";
				log.warn(errorMassage);
				snmpbackmodel.setCollectStatus(false);
				return snmpbackmodel;
			} else {
				Vector<?> vbVect = responsePdu.getVariableBindings();
				log.info(ip + "vb size:" + vbVect.size());
				if (vbVect.size() == 0) {
					this.errorMassage = "IP(" + ip + "),community(" + community + "),oid(" + oidList + ")没有采集到数据！";
					log.warn(errorMassage);
					snmpbackmodel.setCollectStatus(false);
					return snmpbackmodel;
				} else {

					@SuppressWarnings("unchecked")
					Vector<VariableBinding> recVBs = (Vector<VariableBinding>) responsePdu.getVariableBindings();
					/*
					 * Object obj = vbVect.firstElement(); VariableBinding vb = (VariableBinding) obj;
					 */
					log.debug("IP(" + ip + "),community(" + community + "),oid(" + oidList + ")采集到数据："
							+ recVBs.toString());

					List<Snmpinfo> snmpinfoList = new ArrayList<Snmpinfo>();
					/* Iterator iter = ((List) vb).iterator(); */
					for (int i = 0; i < recVBs.size(); i++) {
						Snmpinfo snmpinfo = new Snmpinfo();
						VariableBinding recVB = recVBs.elementAt(i);
						snmpinfo.setOid('.' + recVB.getOid().toString());
						snmpinfo.setOidvalue(recVB.getVariable().toString());
						snmpinfoList.add(snmpinfo);
					}

					snmpbackmodel.setCollectStatus(true);
					snmpbackmodel.setSnmpinfo(snmpinfoList);
					return snmpbackmodel;
				}
			}
		} catch (Exception e) {
			log.warn("IP(" + ip + "),community(" + community + "),oid(" + oidList + ")采集数据出现异常！" + e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}

			if (udpTransportMapping != null) {
				try {
					udpTransportMapping.close();
				} catch (IOException ex2) {
					udpTransportMapping = null;
				}
			}
		}
		return null;
	}

	/*
	 * Testing (chenye add)
	 * 
	 * 异步获取信息列表
	 * 
	 */
	public void snmpAsynGetList(DeviceModel deviceBean, List<String> oidList) {
		String ip = deviceBean.getAgentIp();
		String prot = deviceBean.getClPort();
		String community = deviceBean.getCommunity();
		String deviceName = deviceBean.getAgentName();

		if (!verification(deviceName, ip, prot, oidList.get(0), community)) {
			log.error("设备信息校验失败");
			// exit();
		}

		CommunityTarget target = createDefault(ip, community);
		Snmp snmp = null;
		try {
			PDU pdu = new PDU();

			for (String oid : oidList) {
				pdu.add(new VariableBinding(new OID(oid)));
			}

			DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			snmp.listen();

			// 设置异步作用
			/* final CountDownLatch latch = new CountDownLatch(1); */
			ResponseListener listener = new ResponseListener() {
				public void onResponse(ResponseEvent event) {
					readResponse(event);
				}
			};

			pdu.setType(PDU.GET);
			snmp.send(pdu, target, null, listener);

			log.info("asyn send pdu wait for response...");

			snmp.close();

			log.info("SNMP GET one OID value finished !");
			log.info("IP(" + ip + "),community(" + community + "),oid(" + oidList + ")采集到数据");

		} catch (Exception e) {
			log.error("[SnmpManager]" + e.getMessage());
			log.warn("IP(" + ip + "),community(" + community + "),oid(" + oidList + ")采集数据出现异常！" + e);
		} finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
					log.warn("SNMP 【关闭采集数据】 出现异常！" + ex1);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void readResponse(ResponseEvent respEvnt) {
		// 解析Response
		log.info("------------>解析Response<-------------");
		if (respEvnt != null && respEvnt.getResponse() != null) {
			Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getResponse().getVariableBindings();
			for (int i = 0; i < recVBs.size(); i++) {
				VariableBinding recVB = recVBs.elementAt(i);
				log.info(recVB.getOid() + " : " + recVB.getVariable().toString());
			}
		}
	}

	/**
	 * 验证传入参数是否合法
	 * 
	 * @return
	 */
	public boolean verification(String deviceName, String ip, String prot, String oid, String community) {
		boolean flag = true;
		if (ip == null) {
			log.warn(deviceName + "的IP地址为空，不能采集设备性能");
			flag = false;
		} else if (!ip.matches("((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))")) {
			log.warn(deviceName + "的IP地址格式不正确，不能采集设备性能，IP地址为" + ip);
			flag = false;
		} else if (prot == null) {
			log.warn(deviceName + "的端口号为空，不能采集设备性能");
			flag = false;
		} else if (!prot.matches("^[0-9]*[1-9][0-9]*$")) {
			log.warn(deviceName + "的端口号格式不正确，不能采集设备性能，端口号为" + prot);
			flag = false;
		} else if (oid == null) {
			log.warn(deviceName + "的OID为空，不能采集设备性能");
			flag = false;
		} else if (community == null) {
			log.warn(deviceName + "的共同体为空，不能采集设备性能");
			flag = false;
		}
		String[] oidArr = oid.split("\\.");
		for (String oidnum : oidArr) {

			if (!oidnum.matches("^[0-9]*$")) {
				log.warn("oid格式不正确：" + oid);
				flag = false;
			}
		}

		return flag;
	}

	@Test
	public void test() {
		String ipRouteType_oid = "1.3.6.1.2.1.4.21.1.8";
		String ipRouteNextHop_oid = "1.3.6.1.2.1.4.21.1.7";
		String ipRouteIfIndex_oid = "1.3.6.1.2.1.4.21.1.2";
		String ipRouteMask_oid = "1.3.6.1.2.1.4.21.1.11";
		String dotldFdpTable_oid = "1.3.6.1.2.1.4.21.1.11";
		String linux_oid = "1.3.6.1.2";
		//Vector<VariableBinding> variableBindings = SNMPManagerT.snmpWalk("192.168.1.1", "bocloud", ".1.3.6.1.2.1.4.1");
		Vector<VariableBinding> variableBindings = snmpWalk("192.168.1.164", "public", dotldFdpTable_oid);
		System.out.println(variableBindings.size());	
		if(variableBindings.size() > 0){
			System.out.println(variableBindings.get(0).getVariable().toString());
			for (VariableBinding variableBinding : variableBindings) {
				System.out.println(variableBinding.toString());
			}
		}	
	}

}
