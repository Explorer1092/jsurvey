package com.hanweb.support.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.hanweb.complat.entity.Right;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;

/**
 * 
 * @author cj
 *
 */
public class CurrentUser extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 废弃，请使用iid
	 */
	private Integer id;

	private String post;
	
	private String ip;
	
	private Date accessTime;
	
	private Integer rangeId;
	
	private String rangeName;

	private String sessionId;
	
	private Integer pwdLevel;

	/**
	 * 是否为系统管理员
	 */
	private boolean sysAdmin = false;

	/**
	 * 是否为机构管理员
	 */
	private boolean groupAdmin = false;

	/**
	 * 用户的角色集合
	 */
	private List<Role> roleList;

	/**
	 * 用户的权限集合
	 */
	private List<Right> rightList;

	/**
	 * 具有的权限字符串
	 */
	private Set<String> permissions = new HashSet<String>();
	
	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public Integer getRangeId() {
		return rangeId;
	}

	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public boolean isSysAdmin() {
		return sysAdmin;
	}

	public void setSysAdmin(boolean sysAdmin) {
		this.sysAdmin = sysAdmin;
	}

	public boolean isGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(boolean groupAdmin) {
		this.groupAdmin = groupAdmin;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Right> getRightList() {
		return rightList;
	}

	public void setRightList(List<Right> rightList) {
		this.rightList = rightList;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getPwdLevel() {
		return pwdLevel;
	}

	public void setPwdLevel(Integer pwdLevel) {
		this.pwdLevel = pwdLevel;
	}
	
	/**
	 * 废弃，请使用iid
	 * @return
	 */
	@Deprecated
	public Integer getId() {
		return id;
	}
	
	/**
	 * 废弃，请使用iid
	 * @return
	 */
	@Deprecated
	public void setId(Integer id) {
		this.id = id;
	}
}
