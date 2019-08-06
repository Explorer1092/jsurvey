package com.hanweb.setup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.constant.Settings;

/**
 * 用户自身动作
 * 
 * @author 李杰
 * 
 */
@Controller("com.hanweb.setup.controller.OprUserSelfController")
@RequestMapping("setup/main")
public class OprUserSelfController {

	/**
	 * 显示修改个人密码页面
	 * 
	 * @return
	 */
	@RequestMapping("modify_password_show")
	public ModelAndView showForceModifyPassord() {
		ModelAndView modelAndView = new ModelAndView("/setup/user_modify_password");
		modelAndView.addObject("checkPasswordLevel", Settings.getSettings().getCheckLevel());
		modelAndView.addObject("url", "modify_password_submit.do");
		return modelAndView;
	}

	/**
	 * 修改个人密码
	 * 
	 * @param password
	 *            用户密码
	 * @return
	 */
	@RequestMapping("modify_password_submit")
	@ResponseBody
	public JsonResult submitModifyPassword(String password) {
		JsonResult jsonResult = JsonResult.getInstance();
		if (StringUtil.isNotEmpty(password)) {
			Properties setupProp = getSetupProp();
			setupProp.setProperty("adminpw", Md5Util.encodePwd(password));
			setupProp.save();
			jsonResult.set(ResultState.MODIFY_SUCCESS);
		} else {
			jsonResult.setMessage("密码不能为空");
		}
		return jsonResult;
	}
	
	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}
}
