package com.hanweb.sso.bean;

import java.util.List;
import java.util.Map;

/**
 * jis app 结果
 * 
 * @author 李杰
 *
 */
public class AppsResult {

	private Map<String, String> head = null;

	private List<App> apps = null;

	public Map<String, String> getHead() {
		return head;
	}

	public void setHead(Map<String, String> head) {
		this.head = head;
	}

	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}
}
