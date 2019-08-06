package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJDATATEMP)
public class DataCallTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224985159865105273L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 所属datacallid
	 */
	@Column(type = ColumnType.INT)
	private Integer datacallid;
	
	/**
	 * 类别
	 */
	@Column(type = ColumnType.INT)
	private Integer type;
	
	/**
	 * 模板样式
	 */
	@Column(type = ColumnType.TEXT)
	private String template;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getDatacallid() {
		return datacallid;
	}

	public void setDatacallid(Integer datacallid) {
		this.datacallid = datacallid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
}
