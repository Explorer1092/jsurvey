package com.hanweb.complat.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.dao.GroupManagerDAO;
import com.hanweb.complat.entity.GroupManager;

/**
 * 机构管理范围Service
 * 
 * @author ZhangC
 * 
 */
public class GroupManagerService {

	@Autowired
	private GroupManagerDAO groupManagerDAO;

	/**
	 * 通过用户ID获得其管理的机构ID
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public Integer findRangeIdByUserId(Integer userId) {
		if (NumberUtil.getInt(userId) == 0) {
			return null;
		}
		return groupManagerDAO.findRangeIdByUserId(userId);
	}

	/**
	 * 新增用户管理的机构关系
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean add(Integer userId, Integer groupId) {

		GroupManager groupManager = new GroupManager();
		groupManager.setUserId(userId);
		groupManager.setGroupId(groupId);

		int iid = groupManagerDAO.insert(groupManager);

		return iid > 0 ? true : false;
	}

}
