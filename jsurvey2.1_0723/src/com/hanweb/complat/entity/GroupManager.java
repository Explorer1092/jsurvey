package com.hanweb.complat.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 机构管理范围实体
 * 
 * @author ZhangC
 * 
 */
@Table(name = Tables.GROUPMANAGER)
public class GroupManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -377572209753939466L;

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
	 * 机构ID
	 */
	@Column(type = ColumnType.INT)
	private Integer groupId;

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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

}
