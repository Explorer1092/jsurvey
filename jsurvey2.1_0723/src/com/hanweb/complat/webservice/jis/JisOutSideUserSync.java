package com.hanweb.complat.webservice.jis;

import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.entity.OutsideUser;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.OutSideUserWsService;

/**
 * jis外网用户同步
 * 
 * @author 李杰
 *
 */
public class JisOutSideUserSync {

	private String msg;

	private OutSideUserWsService outSideUserWsService;

	public JisOutSideUserSync() {
		super();
		outSideUserWsService = SpringUtil.getBean("complat_OutSideUserWsService", OutSideUserWsService.class);
	}

	public boolean addUser(OutsideUser outsideUser) {
		boolean isSuccess = false;
		try {
			isSuccess = outSideUserWsService.addUser(outsideUser);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean modifyUser(OutsideUser outsideUser) {
		boolean isSuccess = false;
		try {
			isSuccess = outSideUserWsService.modify(outsideUser);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean removeUser(String loginName) {
		boolean isSuccess = false;
		try {
			isSuccess = outSideUserWsService.removeUser(loginName);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	public boolean enableUser(String loginName) {
		return outSideUserWsService.modifyEnableUser(loginName);
	}

	public boolean disableUser(String loginName) {
		return outSideUserWsService.modifyDisableUser(loginName);
	}

	public OutsideUser findUser(String loginName) {
		return outSideUserWsService.findUser(loginName);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
