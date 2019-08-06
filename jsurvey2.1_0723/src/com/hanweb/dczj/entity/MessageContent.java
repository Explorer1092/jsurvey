package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.MESSAGECONTENT)
public class MessageContent implements Serializable{

	private static final long serialVersionUID = -282150414193276993L;

	@Id
	@Column(type=ColumnType.INT)
	private Integer iid;
	
	@Column(type=ColumnType.INT)
	private Integer dczjid;
	
	@Column(type=ColumnType.TEXT)
	private String content;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
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
}
