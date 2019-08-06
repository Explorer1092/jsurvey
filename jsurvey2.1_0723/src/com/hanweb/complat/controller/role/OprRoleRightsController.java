package com.hanweb.complat.controller.role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.entity.Right;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.RightService;
import com.hanweb.complat.service.RoleRightService;

/**
 * 角色权限关系控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "role_rights")
@RequestMapping("manager/role/rights")
public class OprRoleRightsController {

	@Autowired
	private RightService rightService;

	@Autowired
	private RoleRightService roleRightService;

	/**
	 * 显示角色权限关系界面
	 * 
	 * @param iid
	 *            角色ID
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModifyRights(String iid) {
		ModelAndView modelAndView = new ModelAndView("complat/role/rights_opr");
		Integer roleId = NumberUtil.getInt(iid);

		List<Right> rightList = rightService.findByRoleId(roleId);
		List<Right> allRightList = rightService.findAllRights();

		List<Integer> selectedRightIds = new ArrayList<Integer>();

		for (Right right : rightList) {
			if (right == null || right.getIid() <= 0) {
				continue;
			}
			selectedRightIds.add(right.getIid());
		}

		modelAndView.addObject("selectedRightIds", selectedRightIds);
		modelAndView.addObject("allRightList", allRightList);
		modelAndView.addObject("iid", iid);
		modelAndView.addObject("url", "modify_submit.do");
		return modelAndView;
	}

	/**
	 * 提交角色权限关系
	 * 
	 * @param iid
	 *            角色ID
	 * @param rights
	 *            权限ID集 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModifyMembers(String iid, String rights) {
		JsonResult jsonResult = JsonResult.getInstance();
		List<Integer> rightIdList = StringUtil.toIntegerList(rights, ",");
		boolean isSuccess = false;
		try {
			isSuccess = roleRightService.modifyRoleRight(NumberUtil.getInt(iid), rightIdList);
			if (isSuccess) {
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			} else {
				jsonResult.set(ResultState.MODIFY_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}
}