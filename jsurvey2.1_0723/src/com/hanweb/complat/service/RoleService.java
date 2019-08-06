package com.hanweb.complat.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.controller.role.RoleFormBean;
import com.hanweb.complat.dao.RoleDAO;
import com.hanweb.complat.dao.RoleRelationDAO;
import com.hanweb.complat.dao.RoleRightDAO;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.support.controller.CurrentUser;

/**
 * 角色Service
 * 
 * @author ZhangC
 * 
 */
public class RoleService {

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private RoleRightDAO rightDAO;

	@Autowired
	private RoleRelationDAO roleRelationDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRelationService roleRelationService;

	/**
	 * 
	 * @param role
	 *            角色实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 */
	public boolean add(RoleFormBean role) throws OperationException {
		if (role == null) {
			return false;
		}
		int num = roleDAO.findNumBySameName(0, role.getName());

		if (num > 0) {
			throw new OperationException("存在同名角色！");
		}
		role.setPinYin(PinyinUtil.getHeadByString(role.getName()));

		int iid = roleDAO.insert(role);

		return iid > 0 ? true : false;
	}

	/**
	 * 更新角色
	 * 
	 * @param role
	 *            角色实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 */
	public boolean modify(RoleFormBean role) throws OperationException {
		if (role == null || role.getIid() <= 0) {
			return false;
		}
		int num = roleDAO.findNumBySameName(role.getIid(), role.getName());

		if (num > 0) {
			throw new OperationException("存在同名角色！");
		}
		role.setPinYin(PinyinUtil.getHeadByString(role.getName()));

		return roleDAO.update(role);
	}

	/**
	 * 删除角色
	 * 
	 * @param ids
	 *            角色ID串 如:1,2
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 */
	public boolean removeByIds(String ids) throws OperationException {
		List<Integer> idsList = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idsList)) {
			return false;
		}

		boolean isSuccess = false;
		roleDAO.deleteByIds(idsList);
		// 删除角色
		isSuccess = roleDAO.deleteByIds(idsList);
		if (!isSuccess) {
			throw new OperationException("删除角色失败！");
		}
		// 删除角色权限关系
		isSuccess = rightDAO.deleteByRoleIds(idsList);
		if (!isSuccess) {
			throw new OperationException("删除角色权限失败！");
		}
		isSuccess = roleRelationDAO.deleteByroleId(idsList);
		if (!isSuccess) {
			throw new OperationException("删除角色关系失败！");
		}
		return isSuccess;
	}

	/**
	 * 通用角色id获得角色信息
	 * 
	 * @param iid
	 *            角色id
	 * @return
	 */
	public Role findByIid(int iid) {
		return roleDAO.findByIid(iid);
	}

	
	public List<Role> findByIds(String ids) {
		List<Integer> idsLsit = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idsLsit)) {
			return null;
		}
		List<Role> roleList = new ArrayList<Role>();
		for (Integer integer : idsLsit) {
			Role role =roleDAO.findByIid(NumberUtil.getInt(integer));
			roleList.add(role);
		}		
		return roleList;
	}
	/**
	 * 获得用户的所有角色 (包括其所在机构的角色)
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<Role> findUserRoles(int userId) {
		if (userId <= 0) {
			return new ArrayList<Role>();
		}
		int groupId = userService.findGroupIdByIid(userId);
		List<Role> roleList = roleDAO.findRolesByUserIdOrGroupId(userId, groupId);
		return roleList;
	}

	/**
	 * 切换缺省状态
	 * 
	 * @param iid
	 *            角色ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean modifyeIsDefault(int iid, int isDefault) {
		if (iid <= 0) {
			return false;
		}
		return roleDAO.updateIsDefault(iid, isDefault);
	}

	/**
	 * 获得所有角色列表
	 * 
	 * @return
	 */
	public List<Role> findAllRoles() {
		List<Role> rolesList = roleDAO.findAllRoles();
		return rolesList;
	}

	/**
	 * 获得 用户缺省状态的角色 (包括缺省角色及其所在机构的角色)
	 * 
	 * @param groupId
	 *            机构ID
	 * @return 角色实体集合
	 */
	public List<Role> findDefaultUserRoles(Integer groupId) {
		List<Role> roleList = roleDAO.findDefaultUserRoles(groupId);
		return roleList;
	}

	/**
	 * 获得对应机构的角色
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Role> findGroupRoles(Integer groupId) {
		List<Role> roleList = roleDAO.findRolesByUserIdOrGroupId(null, groupId);
		return roleList;
	}

	/**
	 * 获得缺省角色集合
	 * 
	 * @return
	 */
	public List<Role> findDefaultRoles() {
		List<Role> roleList = roleDAO.findDefaultRoles();
		return roleList;
	}

	/**
	 * 取得指定用户的角色
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> findRolesByUserId(Integer userId) {
		if (NumberUtil.getInt(userId) == 0) {
			return null;
		}
		return roleDAO.findRolesByUserIdOrGroupId(userId, null);
	}

	/**
	 * 更新机构对应的角色权限
	 * 
	 * @param roleIdList
	 *            角色ID集
	 * @param groupId
	 *            机构ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean modifyGroupMembers(List<Integer> roleIdList, Integer groupId) {
		boolean isSuccess = false;
		if (CollectionUtils.isNotEmpty(roleIdList)) {
			for (Integer roleId : roleIdList) {
				isSuccess = roleRelationService.addGroup(roleId, groupId);
				if (!isSuccess) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 更新用户对应的角色权限
	 * 
	 * @param roleIdList
	 *            角色ID集
	 * @param groupId
	 *            机构ID
	 * @param userId
	 *            用户ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean modifyUserMembers(List<Integer> roleIdList, Integer groupId, Integer userId) {
		List<String> groupRoleIdList = roleRelationDAO.findRoleIdsByGroupId(groupId);
		boolean isSuccess = false;
		if (CollectionUtils.isNotEmpty(groupRoleIdList)) {
			for (Integer roleId : roleIdList) {
				if (NumberUtil.getInt(roleId) > 0) {
					// 新添加的用户角色ID
					if (!groupRoleIdList.remove(roleId + "")) {
						isSuccess = roleRelationService.addUser(roleId, userId);
						if (!isSuccess) {
							return false;
						}
					} else {
						// 已存在的机构角色 跳过
						continue;
					}
				}
			}
		} else {
			for (Integer roleId : roleIdList) {
				if (NumberUtil.getInt(roleId) > 0) {
					isSuccess = roleRelationService.addUser(roleId, userId);
					if (!isSuccess) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 检查用户是否为系统管理员
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public static boolean isSysAdmin() {
		CurrentUser user = UserSessionInfo.getCurrentUser();
		return isSysAdmin(user);
	}

	/**
	 * 检查用户是否系统管理员
	 * 
	 * @param user
	 *            登录用户实体
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public static boolean isSysAdmin(CurrentUser currentUser) {
		if (currentUser == null) {
			return false;
		}
		if (currentUser.isSysAdmin()) {
			return true;
		}
		List<Role> roleList = currentUser.getRoleList();
		for (Role role : roleList) {
			// 判断是否为系统管理员
			if (role != null && role.getType() != null && role.getType() == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检查用户是否系统管理员
	 * 
	 * @param userId
	 *            用户ID
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public boolean isSysAdminUser(Integer userId) {
		if (NumberUtil.getInt(userId) <= 0) {
			return false;
		}
		List<Role> roleList = this.findUserRoles(userId);
		for (Role role : roleList) {
			// 判断是否系统管理员
			if (role != null && role.getType() != null && role.getType() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查用户是否机构管理员
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public static boolean isGroupAdmin() {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		return isGroupAdmin(currentUser);

	}

	/**
	 * 检查用户是否机构管理员
	 * 
	 * @param userInfoEn
	 *            登录信息实体
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public static boolean isGroupAdmin(CurrentUser currentUser) {
		try {
			if (currentUser == null) {
				return false;
			}
			List<Role> roleList = currentUser.getRoleList();
			for (Role role : roleList) {
				// 判断是否为机构管理员
				if (role != null && role.getType() != null && role.getType() == 1) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 检查用户是否机构管理员
	 * 
	 * @param userId
	 *            用户ID
	 * @return true - 是<br/>
	 *         false - 不是
	 */
	public boolean isGroupAdminUser(Integer userId) {
		if (NumberUtil.getInt(userId) <= 0) {
			return false;
		}
		List<Role> roleList = this.findUserRoles(userId);
		for (Role role : roleList) {
			// 判断是否为机构管理员
			if (role != null && role.getType() != null && role.getType() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 按名称或名称首字母查询
	 * 
	 * @param keyword
	 * @return
	 */
	public List<Role> findByNameOrPinYin(String keyword) {
		List<Role> roleList = roleDAO.findByNameOrPinYin(keyword);
		return roleList;
	}

}
