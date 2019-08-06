package com.hanweb.complat.webservice.jis;

import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.GroupWsService;

/**
 * jis机构同步
 * @author 李杰
 *
 */
public class JisGroupSync {

	private String msg;

	private GroupWsService groupWsService;

	public JisGroupSync() {
		super();
		groupWsService = SpringUtil.getBean("complat_GroupWsService", GroupWsService.class);
	}

	/**
	 * 新增机构
	 * 
	 * @param group
	 * @return
	 */
	public boolean addGroup(Group group) {
		boolean isSuccess = false;
		try {
			isSuccess = groupWsService.addGroup(group);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	/**
	 * 修改机构
	 * 
	 * @param group
	 * @return
	 */
	public boolean modifyGroup(Group group) {
		boolean isSuccess = false;
		try {
			isSuccess = groupWsService.modifyGroup(group);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	/**
	 * 通过机构编码删除机构
	 * 
	 * @param codeId
	 *            机构编码
	 * @return
	 */
	public boolean removeGroup(String codeId) {
		boolean isSuccess = false;
		try {
			isSuccess = groupWsService.removeGroup(codeId);
		} catch (OperationException e) {
			msg = e.getMessage();
		}
		return isSuccess;
	}

	/**
	 * 通过机构编码获得机构
	 * 
	 * @param codeId
	 *            机构编码
	 * @return
	 */
	public Group findGroup(String codeId) {
		return groupWsService.findGroup(codeId);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
