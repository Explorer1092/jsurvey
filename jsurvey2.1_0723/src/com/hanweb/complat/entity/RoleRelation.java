package com.hanweb.complat.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 角色关系实体
 * @author 李杰
 */
@Table(name = Tables.ROLERELATION)
public class RoleRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8545498907473926480L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 角色id
	 */
	@Column(type = ColumnType.INT)
	private Integer roleId;

	/**
	 * 用户id
	 */
	@Column(type = ColumnType.INT)
	private Integer userId;

	/**
	 * 机构id
	 */
	@Column(type = ColumnType.INT)
	private Integer groupId;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
