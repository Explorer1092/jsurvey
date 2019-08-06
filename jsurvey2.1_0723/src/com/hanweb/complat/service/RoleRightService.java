package com.hanweb.complat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.dao.RoleRightDAO;
import com.hanweb.complat.entity.RoleRight;
import com.hanweb.complat.exception.OperationException;

/**
 * 角色权限关系Service
 * 
 * @author ZhangC
 * 
 */
public class RoleRightService {

	@Autowired
	private RoleRightDAO roleRightDAO;

	/**
	 * 修改角色权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @param rightIdList
	 *            权限ID集
	 * @return
	 * @throws OperationException
	 *             错误提示信息
	 */
	public boolean modifyRoleRight(int roleId, List<Integer> rightIdList) throws OperationException {
		if (roleId <= 0) {
			return false;
		}
		List<Integer> roleIds = StringUtil.toIntegerList(roleId + "", ",");

		boolean isSuccess = roleRightDAO.deleteByRoleIds(roleIds);
		if (!isSuccess) {
			throw new OperationException("删除角色原有权限失败");
		}

		RoleRight roleRight = null;
		Integer iid = null;
		for (Integer rightId : rightIdList) {
			if (NumberUtil.getInt(rightId) <= 0) {
				continue;
			}
			roleRight = new RoleRight();
			roleRight.setRoleId(roleId);
			roleRight.setRightId(rightId);
			iid = roleRightDAO.insert(roleRight);
			if (NumberUtil.getInt(iid) <= 0) {
				throw new OperationException("添加角色权限失败");
			}
		}
		return true;
	}

}
