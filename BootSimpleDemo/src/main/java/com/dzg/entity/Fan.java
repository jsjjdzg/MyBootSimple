package com.dzg.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.bocloud.emaas.database.core.annotations.Column;
import com.bocloud.emaas.database.core.annotations.PK;
import com.bocloud.emaas.database.core.annotations.Table;
import com.bocloud.emaas.database.core.entity.GenericEntity;
import com.bocloud.emaas.database.core.meta.GenerateStrategy;
import com.bocloud.emaas.database.utils.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 测试Fan实体类-JDBC测试
 * 类描述
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@Table("weixin_fan")
public class Fan extends GenericEntity implements Serializable{
	
	private static final long serialVersionUID = 2120869894112984147L;
	
	@PK(value = GenerateStrategy.AUTO_INCREMENT)
	@Column("id")
	private String id;
	@Column("city")
	private String city;
	@Column("sex")
	private String sex;
	@Column("province")
	private String province;
	@Column("subscribe_time")
	@JsonSerialize(using = DateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
	private Date subscribeTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
		return "Fan [id=" + id + ", city=" + city + ", sex=" + sex + ", province=" + province
				+ "]";
	}
	
	
}
