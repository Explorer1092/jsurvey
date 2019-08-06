package com.hanweb.complat.controller.interfaces;

import com.hanweb.common.BaseInfo;

/**
 * JIS formBean
 * 
 * @author wt
 * 
 */
public class ReceiveFormBean {

	/**
	 * EMAIL
	 */
	private String email = "";

	/**
	 * 手机号码
	 */
	private String mobile = "";

	/**
	 * 用户名
	 */
	private String t_name = "";

	/**
	 * 地址
	 */
	private String address = "";

	/**
	 * 性别
	 */
	private String sex = "";

	/**
	 * 应用名
	 */
	private String appname = "";

	/**
	 * 密钥
	 */
	private String enckey = "";

	/**
	 * ldap地址
	 */
	private String ldapurl = "";

	/**
	 * 加密方式
	 */
	private String encrypttype = "1";

	/**
	 * 状态位
	 */
	private String state = "";

	/**
	 * 状态位
	 */
	private String result = "";

	/**
	 * 登录名
	 */
	private String loginuser = "";

	/**
	 * 密码
	 */
	private String loginpass = "";

	/**
	 * 机构名
	 */
	private String domainname = "";

	/**
	 * 数据库ID
	 */
	private String appid = "";

	/**
	 * 内外网
	 */
	private String web = "0";

	/**
	 * 登录后跳转的页面
	 */
	private String url = "";

	/**
	 * 是否需要修改本地用户
	 */
	private String modifyuser = "T";

	/**
	 * sso调用地址
	 */
	private String ssourl = "";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getT_name() {
		return t_name;
	}

	public void setT_name(String tName) {
		t_name = tName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getEnckey() {
		return enckey;
	}

	public void setEnckey(String enckey) {
		this.enckey = enckey;
	}

	public String getLdapurl() {
		return ldapurl;
	}

	public void setLdapurl(String ldapurl) {
		this.ldapurl = ldapurl;
	}

	public String getEncrypttype() {
		encrypttype = encrypttype == null || "".equals(encrypttype) ? "1" : encrypttype;
		return encrypttype;
	}

	public void setEncrypttype(String encrypttype) {
		this.encrypttype = encrypttype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public String getLoginpass() {
		return loginpass;
	}

	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	public String getDomainname() {
		return domainname;
	}

	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}

	public String getAppid() {
		appid = appid == null || "".equals(appid) ? BaseInfo.getAppName() : appid;
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getWeb() {
		web = web == null || "".equals(web) ? "0" : web;
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getModifyuser() {
		modifyuser = modifyuser == null || "".equals(modifyuser) ? "T" : modifyuser;
		return modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	public String getSsourl() {
		return ssourl;
	}

	public void setSsourl(String ssourl) {
		this.ssourl = ssourl;
	}
}
