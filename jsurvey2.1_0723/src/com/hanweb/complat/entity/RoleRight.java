package com.hanweb.complat.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 角色权限关系实体
 * 
 * @author ZhangC
 * 
 */

@Table(name = Tables.ROLERIGHT)
public class RoleRight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384115949800751843L;

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
	 * 权限id
	 */
	@Column(type = ColumnType.INT)
	private Integer rightId;

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

	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

}
