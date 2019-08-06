package com.hanweb.complat.controller.user;

import com.hanweb.complat.entity.User;

/**
 * 用户FormBean
 * 
 * @author ZhangC
 * 
 */
public class UserFormBean {
	
	private User user = new User();

	/**
	 * 选中的角色ID集
	 */
	private String roleIds = "";
	
	/**
	 * 管理机构ID（机构管理员）
	 */
	private Integer rangeId;
	
	private String rangeName;
	
	private String groupName;
	
	/**
	 * 管理网站ID（网站管理员）
	 */
	private String rangeWebIds;
	
	private String rangeWebNames;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getRangeId() {
		return rangeId;
	}

	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}

	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRangeWebIds() {
		return rangeWebIds;
	}

	public void setRangeWebIds(String rangeWebIds) {
		this.rangeWebIds = rangeWebIds;
	}

	public String getRangeWebNames() {
		return rangeWebNames;
	}

	public void setRangeWebNames(String rangeWebNames) {
		this.rangeWebNames = rangeWebNames;
	}

}
