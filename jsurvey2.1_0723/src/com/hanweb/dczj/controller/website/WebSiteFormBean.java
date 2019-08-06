package com.hanweb.dczj.controller.website;

import com.hanweb.dczj.entity.DCZJ_WebSite;

public class WebSiteFormBean extends DCZJ_WebSite implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4272141735547112507L;

	/**
	 * 网站的上一个父网站ID（修改网站所属网站时使用）
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
