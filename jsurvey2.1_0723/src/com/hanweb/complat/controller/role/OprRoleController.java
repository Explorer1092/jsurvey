package com.hanweb.complat.controller.role;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.RoleService;

/**
 * 角色操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "role")
@RequestMapping("manager/role")
public class OprRoleController {

	@Autowired
	private RoleService roleService;

	/**
	 * 打开新增页面
	 * 
	 * @return
	 */
	@Permission(function = "add_show")
	@RequestMapping(value = "add_show")
	public ModelAndView showAdd() {
		ModelAndView modelAndView = new ModelAndView("complat/role/role_opr");

		modelAndView.addObject("role", new Role());
		modelAndView.addObject("url", "add_submit.do");
		return modelAndView;
	}

	/**
	 * 打开编辑页面
	 * 
	 * @param iid
	 *            角色ID
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModify(int iid) {
		ModelAndView modelAndView = new ModelAndView("complat/role/role_opr");

		Role role = roleService.findByIid(iid);

		modelAndView.addObject("url", "modify_submit.do");
		modelAndView.addObject("role", role);
		return modelAndView;
	}

	/**
	 * 提交新增
	 * 
	 * @param roleFormBean
	 *            角色FormBean
	 * @return
	 */
	@Permission(function = "add_submit")
	@RequestMapping(value = "add_submit")
	@ResponseBody
	public JsonResult submitAdd(RoleFormBean roleFormBean) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			roleFormBean.setType(6);
			isSuccess = roleService.add(roleFormBean);
			if (isSuccess) {
				jsonResult.set(ResultState.ADD_SUCCESS);
			} else {
				jsonResult.set(ResultState.ADD_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 提交修改
	 * 
	 * @param roleFormBean
	 *            角色FormBean
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModify(RoleFormBean roleFormBean) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = roleService.modify(roleFormBean);
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

	/**
	 * 角色删除
	 * 
	 * @param ids
	 *            角色ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(String ids) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = roleService.removeByIds(ids);
			if (isSuccess) {
				jsonResult.set(ResultState.REMOVE_SUCCESS);
			} else {
				jsonResult.set(ResultState.REMOVE_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 切换默认角色开关
	 * 
	 * @param iid
	 *            角色ID
	 * @param isDefault
	 *            需要切换的状态 <br>
	 *            1 默认角色<br>
	 *            0 非默认角色
	 * @return
	 */
	@Permission(function = "isdefault_modify")
	@RequestMapping(value = "isdefault_modify")
	@ResponseBody
	public JsonResult modifyIsDefault(String iid, String isDefault) {
		boolean isSuccess = roleService.modifyeIsDefault(NumberUtils.toInt(iid),
				NumberUtils.toInt(isDefault));
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(isSuccess);
		if (isSuccess) {
			jsonResult.setMessage("opr.success");
		} else {
			jsonResult.setMessage("opr.fail");
		}
		return jsonResult;
	}

}
