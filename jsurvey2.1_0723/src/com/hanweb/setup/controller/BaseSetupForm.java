package com.hanweb.setup.controller;

import org.springframework.web.multipart.MultipartFile;
import com.hanweb.jis.expansion.entity.JisAppEntity;

/**
 * setup的form
 * 
 * @author 李杰
 *
 */
public class BaseSetupForm {

	private String appName;

	private MultipartFile licence;

	private int kick = 0;

	private String password;

	private int sso = 0;

	private int sync = 0;

	private JisAppEntity jisConfig;

	private int code = 0;

	private int debug = 4;

	private String domain;

	public MultipartFile getLicence() {
		return licence;
	}

	public void setLicence(MultipartFile licence) {
		this.licence = licence;
	}

	public int getKick() {
		return kick;
	}

	public void setKick(int kick) {
		this.kick = kick;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSso() {
		return sso;
	}

	public void setSso(int sso) {
		this.sso = sso;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getDebug() {
		return debug;
	}

	public void setDebug(int debug) {
		this.debug = debug;
	}

	public int getSync() {
		return sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public JisAppEntity getJisConfig() {
		return jisConfig;
	}

	public void setJisConfig(JisAppEntity jisConfig) {
		this.jisConfig = jisConfig;
	}
}
