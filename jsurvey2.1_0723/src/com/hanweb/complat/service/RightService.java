package com.hanweb.complat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.dao.RightDAO;
import com.hanweb.complat.entity.Right;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.support.controller.CurrentUser;

/**
 * 权限Service
 * 
 * @author ZhangC
 * 
 */
public class RightService {

	@Autowired
	RightDAO rightDAO;

	@Autowired
	private UserService userService;

	/**
	 * 检查用户是否拥有某权限
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param rightId
	 *            权限ID
	 * @return true - 有权限<br/>
	 *         false - 无权限
	 */
	public static boolean haveThisRight(String rightId) {
		/* 取得用户的权限 */
		CurrentUser user = UserSessionInfo.getCurrentUser();
		if (user == null) {
			return false;
		}
		List<Right> rightList = user.getRightList();
		/* 判断用户是否拥有roleId 的权限 */
		for (Right right : rightList) {
			if (right != null && right.getModuleId().equals(rightId)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获得用户有权限的模块
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<Right> findUserRights(int userId) {
		if (NumberUtil.getInt(userId) <= 0) {
			return null;
		}

		Integer groupId = userService.findGroupIdByIid(userId);
		List<Right> rightList = rightDAO.findByUserIdOrGroupId(userId, groupId);
		return rightList;
	}

	/**
	 * 获得所有权限集合
	 * 
	 * @return 权限实体集合
	 */
	public List<Right> findAllRights() {
		List<Right> rightList = rightDAO.findAll();
		return rightList;
	}

	/**
	 * 获得角色的权限集合
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 权限实体集合
	 */
	public List<Right> findByRoleId(int roleId) {
		if (roleId <= 0) {
			return null;
		}
		List<Right> rightList = rightDAO.findByRoleId(roleId);

		return rightList;
	}

}
