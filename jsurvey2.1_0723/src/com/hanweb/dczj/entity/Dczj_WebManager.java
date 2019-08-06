package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.WEBMANAGER)
public class Dczj_WebManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -730382884693022297L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 用户ID
	 */
	@Column(type = ColumnType.INT)
	private Integer userId;

	/**
	 * 网站ID
	 */
	@Column(type = ColumnType.INT)
	private Integer webId;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getWebId() {
		return webId;
	}

	public void setWebId(Integer webId) {
		this.webId = webId;
	}
	
}
