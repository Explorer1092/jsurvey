package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJSTYLE)
public class Style implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2099899120951300936L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 所属调查征集ID
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * PC端样式
	 */
	@Column(type = ColumnType.TEXT)
	private String pcstyle;
	
	/**
	 * PC端结果页样式
	 */
	@Column(type = ColumnType.TEXT)
	private String pcresultstyle;
	
	/**
	 * 手机端样式
	 */
	@Column(type = ColumnType.TEXT)
	private String phonestyle;
	
	/**
	 * 手机端结果样式
	 */
	@Column(type = ColumnType.TEXT)
	private String phoneresultstyle;

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

	public String getPcstyle() {
		return pcstyle;
	}

	public void setPcstyle(String pcstyle) {
		this.pcstyle = pcstyle;
	}

	public String getPcresultstyle() {
		return pcresultstyle;
	}

	public void setPcresultstyle(String pcresultstyle) {
		this.pcresultstyle = pcresultstyle;
	}

	public String getPhonestyle() {
		return phonestyle;
	}

	public void setPhonestyle(String phonestyle) {
		this.phonestyle = phonestyle;
	}

	public String getPhoneresultstyle() {
		return phoneresultstyle;
	}

	public void setPhoneresultstyle(String phoneresultstyle) {
		this.phoneresultstyle = phoneresultstyle;
	}
	
}
