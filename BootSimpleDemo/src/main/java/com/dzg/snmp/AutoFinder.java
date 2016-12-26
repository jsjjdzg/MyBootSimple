package com.dzg.snmp;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import com.dzg.snmp.Interface.IfAdminStatus;
import com.dzg.snmp.Interface.IfType;

public class AutoFinder {

	public void routerFind(String routerIp, String community) {

		Vector<VariableBinding> variableBindings = SNMPManagerT.snmpWalk(routerIp, community, ".1.3.6.1.2.1.2");
		System.out.println(variableBindings.size());
		Map<String, Interface> ifs = new HashMap<>();

		OID targetOID = new OID("1.3.6.1.2.1.2.2.1.1");
		for (VariableBinding vb : variableBindings) {
			Interface per_if = new Interface();
			if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) == 0) {
				per_if.ifIndex = vb.getVariable().toInt();
				ifs.put(vb.getVariable().toString(), per_if);
			}
		}

		for (VariableBinding vb : variableBindings) {
			for (int mibIndex = 1; mibIndex < 22; mibIndex++) {
				for (String s : ifs.keySet()) {
					Interface per_if = ifs.get(s);
					OID nwdescOID = new OID("1.3.6.1.2.1.2.2.1" + "." + mibIndex + "." + s);
					if (nwdescOID.leftMostCompare(nwdescOID.size(), vb.getOid()) == 0) {
						switch (mibIndex) {
						case 2:
							per_if.ifDescr = vb.getVariable().toString();
							break;
						case 3:
							if (per_if.ifType == null) {
								per_if.setIfType(IfType.getIfType(vb.getVariable().toInt()));
							}
							break;
						case 4:
							per_if.ifMtu = vb.getVariable().toInt();
							break;
						case 5:
							per_if.ifSpeed = vb.getVariable().toInt();
							break;
						case 6:
							per_if.ifPhysAddress = vb.getVariable().toString();
							break;
						case 7:
							if (per_if.ifType == null) {
								per_if.setIfAdminStatus(IfAdminStatus.getifAdminStatus(vb.getVariable().toInt()));
							}
							break;
						case 14:
							per_if.ifInErrors = vb.getVariable().toInt();
							break;
						default:
							break;
						}
						ifs.put(s, per_if);
					}
				}
			}
		}

		for (Entry<String, Interface> set : ifs.entrySet()) {
			System.out.println(set);
		}
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/log4j.properties");

		AutoFinder aa = new AutoFinder();
		aa.routerFind("192.168.1.1", "bocloud");
	}
}
