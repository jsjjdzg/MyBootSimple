package com.bocloud.emaas.database.core.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.bocloud.emaas.database.core.annotations.Column;
import com.bocloud.emaas.database.core.annotations.IgnoreUpdate;
import com.bocloud.emaas.database.utils.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 基础实体类
 * 
 * @author dmw
 *
 */
public class GenericEntity extends Generic {

	@Column("gmt_create")
	@IgnoreUpdate
	@JsonSerialize(using = DateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date gmtCreate;// 创建时间
	@Column("gmt_modify")
	@JsonSerialize(using = DateSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date gmtModify;// 修改时间
	@Column("creater_id")
	@IgnoreUpdate
	private Long createrId;// 创建者ID
	@Column("owner_id")
	private Long ownerId;// 所有者ID
	@Column("mender_id")
	private Long menderId;// 最后修改者ID
	@Column("name")
	private String name; // 名称
	@Column("status")
	private String status; // 状态
	@Column("is_locked")
	private Boolean isLocked; // 操作状态
	@Column("is_deleted")
	private Boolean isDeleted; // 是否被删除
	@Column("more_prop")
	private String moreProp; // 其他属性
	@Column("remark")
	private String remark; // 描述

	/**
	 * @return the gmtCreate
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * @param gmtCreate
	 *            the gmtCreate to set
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * @return the gmtModify
	 */
	public Date getGmtModify() {
		return gmtModify;
	}

	/**
	 * @param gmtModify
	 *            the gmtModify to set
	 */
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	/**
	 * @return the createrId
	 */
	public Long getCreaterId() {
		return createrId;
	}

	/**
	 * @param createrId
	 *            the createrId to set
	 */
	public void setCreaterId(Long createrId) {
		this.createrId = createrId;
	}

	/**
	 * @return the ownerId
	 */
	public Long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            the ownerId to set
	 */
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the menderId
	 */
	public Long getMenderId() {
		return menderId;
	}

	/**
	 * @param menderId
	 *            the menderId to set
	 */
	public void setMenderId(Long menderId) {
		this.menderId = menderId;
	}

	public GenericEntity(Long createrId, Long ownerId, Long menderId) {
		super();
		this.createrId = createrId;
		this.ownerId = ownerId;
		this.menderId = menderId;
	}

	public GenericEntity() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the isLocked
	 */
	public Boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked
	 *            the isLocked to set
	 */
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the moreProp
	 */
	public String getMoreProp() {
		return moreProp;
	}

	/**
	 * @param moreProp
	 *            the moreProp to set
	 */
	public void setMoreProp(String moreProp) {
		this.moreProp = moreProp;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
