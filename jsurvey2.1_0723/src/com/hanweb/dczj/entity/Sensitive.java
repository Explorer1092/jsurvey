package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 征集调查敏感词表实体
 * 
 * @author zhounj
 * 
 */
@Table(name = Tables.SENSITIVE)
public class Sensitive implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3000546104583385486L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer i_id;

	/**
	 * 创建用户名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String vc_sensitiveword;

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getVc_sensitiveword() {
		return vc_sensitiveword;
	}

	public void setVc_sensitiveword(String vc_sensitiveword) {
		this.vc_sensitiveword = vc_sensitiveword;
	}
	
}
