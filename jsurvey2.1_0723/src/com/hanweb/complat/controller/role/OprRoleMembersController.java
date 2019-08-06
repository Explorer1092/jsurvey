package com.hanweb.complat.controller.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.service.RoleRelationService;

/**
 * 角色成员操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "role_members")
@RequestMapping("manager/role/members")
public class OprRoleMembersController {

	@Autowired
	private RoleRelationService relationService;

	/**
	 * 保存角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groups
	 *            机构ID串 如:1,2,3,4
	 * @param users
	 *            用户ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModifyMembers(Integer roleId, String groups, String users) {
		List<Integer> groupIdList = StringUtil.toIntegerList(groups, ",");
		List<Integer> userIdList = StringUtil.toIntegerList(users, ",");

		boolean isSuccess = true;

		for (Integer groupId : groupIdList) {
			if (relationService.findGroupMemberSize(roleId, groupId) == 0) {
				isSuccess = relationService.addGroup(roleId, groupId);
				if (!isSuccess) {
					break;
				}
			}
		}

		if (isSuccess) {
			for (Integer userId : userIdList) {
				if (relationService.findUserMemberSize(roleId, userId) == 0) {
					isSuccess = relationService.addUser(roleId, userId);
					if (!isSuccess) {
						break;
					}
				}
			}
		}
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(isSuccess);
		if (isSuccess) {
			jsonResult.setMessage("opr.success");
		} else {
			jsonResult.setMessage("opr.fail");
		}
		return jsonResult;
	}

	/**
	 * 移除对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groups
	 *            机构ID串 如:1,2,3,4
	 * @param users
	 *            用户ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(Integer roleId, String userIds, String groupIds) {
		boolean isSuccess = true;

		List<Integer> userIdList = StringUtil.toIntegerList(userIds);
		if (userIdList.size() > 0) {
			isSuccess = relationService.removeUsersByRoleId(roleId, userIdList);
		}

		if (isSuccess) {
			List<Integer> groupIdList = StringUtil.toIntegerList(groupIds);
			if (groupIdList.size() > 0) {
				isSuccess = relationService.removeGroupsByRoleId(roleId, groupIdList);
			}
		}
		JsonResult jsonResult = JsonResult.getInstance();
		if (isSuccess) {
			jsonResult.set(ResultState.REMOVE_SUCCESS);
		} else {
			jsonResult.set(ResultState.REMOVE_FAIL);
		}
		return jsonResult;
	}

	/**
	 * 清空角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	@Permission(function = "clean")
	@RequestMapping(value = "clean")
	@ResponseBody
	public JsonResult clean(Integer roleId) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean isSuccess = relationService.clean(roleId);
		jsonResult.setSuccess(isSuccess);
		if (isSuccess) {
			jsonResult.setMessage("opr.success");
		} else {
			jsonResult.setMessage("opr.fail");
		}
		return jsonResult;
	}
}