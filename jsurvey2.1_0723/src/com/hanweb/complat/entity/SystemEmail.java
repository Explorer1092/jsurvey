package com.hanweb.complat.entity;

/**
 * email实体
 * @author 李杰
 *
 */
public class SystemEmail {
	
	private String host;

	private String user;

	private String pwd;

	private String subject;

	private String form;

	private String formNickname;

	private String content;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getFormNickname() {
		return formNickname;
	}

	public void setFormNickname(String formNickname) {
		this.formNickname = formNickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
