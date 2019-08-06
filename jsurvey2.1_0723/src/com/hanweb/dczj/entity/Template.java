package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJTEMPLATE)
public class Template implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 323099871908789604L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 类型
	 */
	@Column(type = ColumnType.INT)
	private Integer type;
	
	/**
	 * 页面类型
	 */
	@Column(type = ColumnType.INT)
	private Integer pagetype;
	
	/**
	 * 所属调查征集ID
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 模板内容
	 */
	@Column(type = ColumnType.TEXT)
	private String content;
	
	/**
	 * 路径
	 */
	@Column(type = ColumnType.VARCHAR)
	private String path;
	
	/**
	 * 模板名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String name;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPagetype() {
		return pagetype;
	}

	public void setPagetype(Integer pagetype) {
		this.pagetype = pagetype;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

