package com.hanweb.complat.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.complat.dao.AccessLogDAO;
import com.hanweb.complat.entity.AccessLog;

/**
 * 访问日志业务
 * 
 * @author 李杰
 *
 */
public class AccessLogService {
	@Autowired
	private AccessLogDAO accessLogDAO;
	/**
	 * 保留最后几次的日志记录
	 */
	private int keepNumber = 3; 
	
	/**
	 * 记录日志
	 * @param log
	 * @return
	 */
	public boolean record(AccessLog log) {
		boolean isSuccess = false;
		this.deleteExpiredLogs(log.getLoginName());
		int iid = accessLogDAO.insert(log);
		if (iid > 0) {
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 删除过期日志
	 * @param loginName
	 */
	private void deleteExpiredLogs(String loginName) {
		int count = accessLogDAO.findCount(loginName);
		if (count >= keepNumber) {
			accessLogDAO.deleteByMinId(loginName);
		}
	}
}
