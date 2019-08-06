package com.hanweb.complat.webservice.jis;

import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.UserWsService;

/**
 * jis内网用户同步
 * @author 李杰
 *
 */
public class JisUserSync {

	private String msg;

	private UserWsService userWsService;

	public JisUserSync() {
		super();
		userWsService = SpringUtil.getBean("complat_UserWsService", UserWsService.class);
	}

	public boolean addUser(User user) {
		boolean isSuccess = false;
		try {
			isSuccess = userWsService.addUser(user);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean modifyUser(User user) {
		boolean isSuccess = false;
		try {
			isSuccess = userWsService.modifyUser(user);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean removeUser(String loginId) {
		boolean isSuccess = false;
		try {
			isSuccess = userWsService.removeUser(loginId);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean enableUser(String loginId) {
		return userWsService.modifyEnableUser(loginId);
	}

	public boolean disableUser(String loginId) {
		return userWsService.modifyDisableUser(loginId);
	}

	public User findUser(String loginId) {
		return userWsService.findUser(loginId);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
