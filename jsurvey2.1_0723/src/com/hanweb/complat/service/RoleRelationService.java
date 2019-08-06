package com.hanweb.complat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.dao.RoleRelationDAO;
import com.hanweb.complat.entity.RoleRelation;

/**
 * 角色成员关系Service
 * 
 * @author ZhangC
 * 
 */
public class RoleRelationService {

	@Autowired
	private RoleRelationDAO roleRelationDAO;

	/**
	 * 添加机构对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groupId
	 *            机构ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean addGroup(Integer roleId, Integer groupId) {
		RoleRelation roleRelation = new RoleRelation();
		roleRelation.setRoleId(roleId);
		roleRelation.setGroupId(groupId);

		int iid = roleRelationDAO.insert(roleRelation);

		return iid > 0 ? true : false;
	}

	/**
	 * 添加机构对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param userId
	 *            用户ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean addUser(Integer roleId, Integer userId) {
		RoleRelation roleRelation = new RoleRelation();
		roleRelation.setRoleId(roleId);
		roleRelation.setUserId(userId);

		int iid = roleRelationDAO.insert(roleRelation);

		return iid > 0 ? true : false;
	}

	/**
	 * 删除机构对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groupIdsList
	 *            机构ID 集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean removeGroupsByRoleId(Integer roleId, List<Integer> groupIdList) {

		boolean isSuccess = roleRelationDAO.deleteGroups(roleId, groupIdList);

		return isSuccess;
	}

	/**
	 * 删除用户对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groupIdsList
	 *            机构ID 集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean removeUsersByRoleId(Integer roleId, List<Integer> userIdList) {

		boolean isSuccess = roleRelationDAO.deleteUsers(roleId, userIdList);

		return isSuccess;
	}

	/**
	 * 删除机构对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean clean(Integer roleId) {
		if (NumberUtil.getInt(roleId) == 0) {
			return false;
		}
		List<Integer> roleIdList = new ArrayList<Integer>();
		roleIdList.add(roleId);

		boolean isSuccess = roleRelationDAO.deleteByroleId(roleIdList);

		return isSuccess;
	}

	/**
	 * 获得机构型成员的数量
	 * 
	 * @param roleId
	 * @param groupId
	 * @return
	 */
	public int findGroupMemberSize(Integer roleId, Integer groupId) {
		if (NumberUtil.getInt(roleId) == 0 || groupId == null) {
			return 0;
		}

		return roleRelationDAO.findGroupMemberSize(roleId, groupId);
	}

	/**
	 * 获得用户型成员的数量
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public int findUserMemberSize(Integer roleId, Integer userId) {
		if (NumberUtil.getInt(roleId) == 0 || NumberUtil.getInt(userId) == 0) {
			return 0;
		}

		return roleRelationDAO.findUserMemberSize(roleId, userId);
	}
	/**
	 * 根据用户ID获取角色ID
	 * @param userId
	 * @return
	 */
	public int findRoleByUserId(Integer userId) {
		return roleRelationDAO.findRoleByUserId(userId);
	}

}
