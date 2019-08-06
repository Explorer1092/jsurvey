package com.hanweb.complat.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.complat.dao.GroupDAO;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.GroupService;

/**
 * jis 机构 webservice 的业务
 * 
 * @author 李杰
 * 
 */
public class GroupWsService {

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupDAO groupDAO;

	/**
	 * 新增机构
	 * 
	 * @param group
	 * @return
	 */
	public boolean addGroup(Group group) throws OperationException {
		if (group == null) {
			return false;
		}
		if(group != null && group.getPid() == null){
			group.setPid(0);
		}
		int num = groupService.findNumOfSameName(group.getName(), group.getPid());
		if (num > 0) {
			throw new OperationException("机构名称已存在,请重新设置！");
		}
		group.setPinYin(PinyinUtil.getHeadByString(group.getName()));
		if(group.getPid()==null){
			group.setPid(0);
		}
		int iid = groupDAO.insert(group);
		return iid > 0 ? true : false;
	}

	/**
	 * 修改机构
	 * 
	 * @param group
	 * @return
	 */
	public boolean modifyGroup(Group group) throws OperationException {
		if (group == null) {
			return false;
		}
		if(group != null && group.getPid() == null){
			group.setPid(0);
		}
		boolean isSuccess = false;
//		int num = groupService.findNumOfSameName(group.getName(), group.getPid());
//		if (num > 0) {
//			throw new OperationException("机构名称已存在,请重新设置！");
//		}
		group.setPinYin(PinyinUtil.getHeadByString(group.getName()));
		if (group.getPid() == null) {
			group.setPid(0);
		}
		Group innerGroup = groupService.findByCodeId(group.getCodeId());
		if (innerGroup == null) {
			// isSuccess = addGroup(group);
			throw new OperationException("无法修改机构，原因：无此机构，group code:" + group.getCodeId());
		} else {
			group.setIid(innerGroup.getIid());
			isSuccess = groupDAO.update(group);
		}
		return isSuccess;
	}

	/**
	 * 通过机构编码删除机构
	 * 
	 * @param codeId
	 *            机构编码
	 * @return
	 * @throws OperationException
	 */
	public boolean removeGroup(String codeId) throws OperationException {
		boolean isSuccess = false;
		isSuccess = groupService.removeByCodeId(codeId);
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
		return groupService.findByCodeId(codeId);
	}
}
