package com.hanweb.sso.bean;

/**
 * app
 * 
 * @author 李杰
 *
 */
public class App {

	private String name;

	private String url;

	private String uuid;

	private String imageUrl;

	private String appMark;

	private Boolean current;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAppMark() {
		return appMark;
	}

	public void setAppMark(String appMark) {
		this.appMark = appMark;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
