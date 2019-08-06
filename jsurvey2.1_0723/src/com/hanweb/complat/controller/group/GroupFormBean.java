package com.hanweb.complat.controller.group;

import com.hanweb.complat.entity.Group;

/**
 * 机构FormBean
 * 
 * @author ZhangC
 * 
 */
public class GroupFormBean extends Group implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1176122037470241423L;

	/**
	 * 机构的上一个父机构ID（修改机构所属机构时使用）
	 */
	private Integer prevPid = 0;

	/**
	 * 选中的角色ID集
	 */
	private String roleIds = "";

	public Integer getPrevPid() {
		return prevPid;
	}

	public void setPrevPid(Integer prevPid) {
		this.prevPid = prevPid;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

}
