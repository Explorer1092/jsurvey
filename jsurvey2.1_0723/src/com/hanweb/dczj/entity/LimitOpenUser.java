package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name=Tables.JUSURVEYLIMITOPEN)
public class LimitOpenUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9018795914974606592L;

	@Id
	@Column(type=ColumnType.INT)
	private Integer iid;//主键
	
	@Column(type=ColumnType.INT)
	private Integer dczjid;//关联dczjid
	
	@Column(type=ColumnType.INT)
	private Integer limittype;//限制类型（0：按机构 1：按用户 2：按角色）
	
	@Column(type=ColumnType.VARCHAR)
	private String limitids;//限制关联id(若按机构此为机构id 若按用户此为用户id 若按角色此为角色id)，多个以逗号隔开
	
	private String groups;

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

	public Integer getLimittype() {
		return limittype;
	}

	public void setLimittype(Integer limittype) {
		this.limittype = limittype;
	}

	public String getLimitids() {
		return limitids;
	}

	public void setLimitids(String limitids) {
		this.limitids = limitids;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	
}
