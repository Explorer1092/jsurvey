package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJDATACALL)
public class DataCall implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4878572144703904678L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 所属网站ID
	 */
	@Column(type = ColumnType.INT)
	private Integer webid;
	
	/**
	 * 创建时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date createdate;
	
	/**
	 * 创建人
	 */
	@Column(type = ColumnType.VARCHAR)
	private String createname;
	
	/**
	 * 数据调用名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String datacall_name;

	/**
	 * 调用网站ID集合
	 */
	@Column(type = ColumnType.TEXT)
	private String datacall_webids;
	
	/**
	 * 调用网站名集合
	 */
	@Column(type = ColumnType.TEXT)
	private String datacall_webnames;

	/**
	 * 调用dczj类型
	 */
	@Column(type = ColumnType.VARCHAR)
	private String datacall_types;
	
	/**
	 * 调用jact类型集合
	 */
	@Column(type = ColumnType.INT)
	private Integer datacall_type;
	
	/**
	 * 页面长度
	 */
	@Column(type = ColumnType.INT)
	private Integer datacall_number;
	
	/**
	 * 页面排序类型0创建时间，1开始时间，2结束时间
	 */
	@Column(type = ColumnType.INT)
	private Integer datacall_sorttype;
	
	/**
	 * 页面排序顺序
	 */
	@Column(type = ColumnType.INT)
	private Integer datacall_sort;
	
	/**
	 * 是否删除
	 */
	@Column(type = ColumnType.INT)
	private Integer state;

	/**
	 * 是否发布
	 */
	@Column(type = ColumnType.INT)
	private Integer updatehtml;
	
	/**
	 * 发布条件
	 */
	@Column(type = ColumnType.INT)
	private Integer filtercondition;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getWebid() {
		return webid;
	}

	public void setWebid(Integer webid) {
		this.webid = webid;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getCreatename() {
		return createname;
	}

	public void setCreatename(String createname) {
		this.createname = createname;
	}

	public String getDatacall_name() {
		return datacall_name;
	}

	public void setDatacall_name(String datacall_name) {
		this.datacall_name = datacall_name;
	}

	public String getDatacall_webids() {
		return datacall_webids;
	}

	public void setDatacall_webids(String datacall_webids) {
		this.datacall_webids = datacall_webids;
	}

	public String getDatacall_webnames() {
		return datacall_webnames;
	}

	public void setDatacall_webnames(String datacall_webnames) {
		this.datacall_webnames = datacall_webnames;
	}

	public String getDatacall_types() {
		return datacall_types;
	}

	public void setDatacall_types(String datacall_types) {
		this.datacall_types = datacall_types;
	}

	public Integer getDatacall_type() {
		return datacall_type;
	}

	public void setDatacall_type(Integer datacall_type) {
		this.datacall_type = datacall_type;
	}

	public Integer getDatacall_number() {
		return datacall_number;
	}

	public void setDatacall_number(Integer datacall_number) {
		this.datacall_number = datacall_number;
	}

	public Integer getDatacall_sorttype() {
		return datacall_sorttype;
	}

	public void setDatacall_sorttype(Integer datacall_sorttype) {
		this.datacall_sorttype = datacall_sorttype;
	}

	public Integer getDatacall_sort() {
		return datacall_sort;
	}

	public void setDatacall_sort(Integer datacall_sort) {
		this.datacall_sort = datacall_sort;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getUpdatehtml() {
		return updatehtml;
	}

	public void setUpdatehtml(Integer updatehtml) {
		this.updatehtml = updatehtml;
	}

	public Integer getFiltercondition() {
		return filtercondition;
	}

	public void setFiltercondition(Integer filtercondition) {
		this.filtercondition = filtercondition;
	}
	
	
}
