package com.hanweb.support.controller.resetpwd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hanweb.common.util.StringUtil;

/**
 * 缓存密码重置操作
 * @author 李杰
 *
 */
public class ResetPwdCache {
	private static final Log LOGGER = LogFactory.getLog(ResetPwdCache.class);
	
	private static final List<ResetPwdCache> RESET_PWD_CACHES = new ArrayList<ResetPwdCache>();
	
	/**
	 * 新增一个，如果登录名称存在则覆盖
	 * @param resetPwdCache
	 */
	public static void add(ResetPwdCache resetPwdCache){
		try{
			for (Iterator<ResetPwdCache> iterator = RESET_PWD_CACHES.iterator(); iterator.hasNext();) {
				ResetPwdCache resetPwdCached = iterator.next();
				if(StringUtil.equals(resetPwdCached.getName(), resetPwdCache.getName())){
					iterator.remove();
				}
			}
			RESET_PWD_CACHES.add(resetPwdCache);
		}catch(Exception e){
			LOGGER.error("add error", e);
		}
	}
	
	/**
	 * 获得一个缓存
	 * @param token
	 * @return
	 */
	public static ResetPwdCache get(String token){
		ResetPwdCache resetPwdCache = null;
		if(StringUtil.isEmpty(token)){
			return resetPwdCache;
		}
		for (ResetPwdCache resetPwdCached : RESET_PWD_CACHES) {
			if(StringUtil.equals(resetPwdCached.getToken(), token)){
				resetPwdCache = resetPwdCached;
				break;
			}
		}
		return resetPwdCache;
	}
	
	/**
	 * 按照令牌删除缓存
	 * @param token
	 */
	public static void remove(String token){
		try{
			for (Iterator<ResetPwdCache> iterator = RESET_PWD_CACHES.iterator(); iterator.hasNext();) {
				ResetPwdCache resetPwdCache = iterator.next();
				if(StringUtil.equals(resetPwdCache.token, token)){
					iterator.remove();
				}
			}
		}catch(Exception e){
			LOGGER.error("remove error", e);
		}
	}
	
	/**
	 * 删除过期缓存
	 */
	public static void removeExpiratoin(){
		try{
			Date date = new Date();
			for (Iterator<ResetPwdCache> iterator = RESET_PWD_CACHES.iterator(); iterator.hasNext();) {
				ResetPwdCache resetPwdCache = iterator.next();
				if(resetPwdCache.getIndate().compareTo(date) < 1){
					iterator.remove();
				}
			}
		}catch(Exception e){
			LOGGER.error("removeExpiratoin error", e);
		}
	}
	
	/**
	 * 口令
	 */
	private String token;
	
	/**
	 * 用户名
	 */
	private String name;
	
	/**
	 * 有效时间
	 */
	private Date indate;

	public ResetPwdCache(String token, String name, Date indate) {
		super();
		this.token = token;
		this.name = name;
		this.indate = indate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIndate() {
		return indate;
	}

	public void setIndate(Date indate) {
		this.indate = indate;
	}
}
