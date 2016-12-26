/**
 * @Title: BackupNetworkModel.java
 * @Description: TODO(定义备份网络信息模型)
 * Copyright: Copyright (c) 2016 * 
 * @author liuzm
 * @date 2016年9月5日 晚上21：33：45
 * @version V1.0
 */
package com.dzg.model;

import java.io.Serializable;

public class BackupNetworkModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userName;   //用户名
	private String password;   //密码
	private String managerIP;   //交换机IP
	private boolean isSuper;   //是否super命令
	private String commamd;   //查看配置命令
	private String screenCmd;  //不分屏命令
	private String exitCmd;    //退出命令
	private int port;         //ssh端口
	
	public String getExitCmd() {
		return exitCmd;
	}
	public void setExitCmd(String exitCmd) {
		this.exitCmd = exitCmd;
	}
	public boolean isSuper() {
		return isSuper;
	}
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}
	public String getCommamd() {
		return commamd;
	}
	public void setCommamd(String commamd) {
		this.commamd = commamd;
	}
	public String getScreenCmd() {
		return screenCmd;
	}
	public void setScreenCmd(String screenCmd) {
		this.screenCmd = screenCmd;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getManagerIP() {
		return managerIP;
	}
	public void setManagerIP(String managerIP) {
		this.managerIP = managerIP;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}