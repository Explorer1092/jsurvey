package com.hanweb.complat.controller.outsideuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.entity.OutsideUser;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.service.OutsideUserService;

/**
 * 外网用户操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "outsideuser")
@RequestMapping("manager/outsideuser")
public class OprOutsideUserController {

	@Autowired
	private OutsideUserService outsideUserService;

	/**
	 * 显示新增外网用户页面
	 * 
	 * @return
	 */
	@Permission(function = "add_show")
	@RequestMapping(value = "add_show")
	public ModelAndView showAdd() {
		ModelAndView modelAndView = new ModelAndView("complat/outsideuser/outsideuser_opr");

		modelAndView.addObject("url", "add_submit.do");
		modelAndView.addObject("outsideuser", new OutsideUser());
		return modelAndView;
	}

	/**
	 * 显示编辑外网用户页面
	 * 
	 * @param iid
	 *            外网用户ID
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModify(int iid) {
		ModelAndView modelAndView = new ModelAndView("complat/outsideuser/outsideuser_opr");

		OutsideUser outsideUser = outsideUserService.findByIid(iid);

		modelAndView.addObject("url", "modify_submit.do");
		modelAndView.addObject("outsideuser", outsideUser);
		return modelAndView;
	}

	/**
	 * 新增外网用户
	 * 
	 * @param outsideuser
	 *            外网用户实体
	 * @return
	 */
	@Permission(function = "add_submit")
	@RequestMapping(value = "add_submit")
	@ResponseBody
	public JsonResult submitAdd(OutsideUser outsideuser) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		outsideuser.setEnable(1);
		try {
			outsideuser.setLoginName(SecurityUtil.RSADecode(outsideuser.getLoginName()));
			outsideuser.setPwd(SecurityUtil.RSADecode(outsideuser.getPwd()));
			isSuccess = outsideUserService.add(outsideuser);
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
	 * 编辑外网用户
	 * 
	 * @param outsideuser
	 *            外网用户实体
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModify(OutsideUser outsideuser) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			if (StringUtil.isNotEmpty(outsideuser.getPwd())) {
				outsideuser.setPwd(SecurityUtil.RSADecode(outsideuser.getPwd()));
			}
			isSuccess = outsideUserService.modify(outsideuser);
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
	 * 删除外网用户
	 * 
	 * @param ids
	 *            外网用户ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(String ids) {
		boolean isSuccess = outsideUserService.removeByIds(ids);
		JsonResult jsonResult = JsonResult.getInstance();
		if (isSuccess) {
			jsonResult.set(ResultState.REMOVE_SUCCESS);
		} else {
			jsonResult.set(ResultState.REMOVE_FAIL);
		}
		return jsonResult;
	}

	/**
	 * 切换外网用户状态
	 * 
	 * @param iid
	 *            外网用户ID
	 * @param enable
	 *            是否有效<br>
	 *            1 有效<br>
	 *            0 失效
	 * @return
	 */
	@Permission(function = "enable_modify")
	@RequestMapping(value = "enable_modify")
	@ResponseBody
	public JsonResult modifyEnable(Integer iid, Integer enable) {
		boolean isSuccess = outsideUserService.modifyEnable(iid, enable);
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
