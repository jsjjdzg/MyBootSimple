package com.dzg.entity;

import java.io.Serializable;

/**
 * 测试Fan实体类-JDBC测试
 * 类描述
 * @author DZG
 * @since V1.0 2016年7月29日
 */
public class Fan implements Serializable{
	
	private static final long serialVersionUID = 2120869894112984147L;
	
	private String id;
	private String nickname;
	private String city;
	private String sex;
	private String province;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Override
	public String toString() {
		return "Fan [id=" + id + ", nickname=" + nickname + ", city=" + city + ", sex=" + sex + ", province=" + province
				+ "]";
	}
	
	
}
