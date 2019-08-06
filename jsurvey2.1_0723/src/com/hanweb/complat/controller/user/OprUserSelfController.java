package com.hanweb.complat.controller.user;

import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.common.validation.validate.Validate;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.interceptor.CsrfDefInterceptor;
import com.hanweb.complat.interceptor.PwdCheckInterceptor;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.complat.service.UserService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 用户自身动作
 * 
 * @author 李杰
 * 
 */
@Controller
@RequestMapping("manager/user")
public class OprUserSelfController {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RoleService roleService;

	/**
	 * 显示编辑个人信息页面
	 * 
	 * @return
	 */
	@RequestMapping("modify_self_show")
	public ModelAndView showModifySelf() {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		ModelAndView modelAndView = new ModelAndView("complat/user/user_modify_self");

		User user = userService.findByIid(currentUser.getIid());

		// 机构
		String groupName = groupService.findNameByIid(user.getGroupId());

		// 角色
		LinkedHashMap<Integer, String> allRoleOptions = this.findAllRoleOptions();

		modelAndView.addObject("user", user);
		modelAndView.addObject("groupName", groupName);
		modelAndView.addObject("allRoleOptions", JsonUtil.objectToString(allRoleOptions));

		boolean isSuperAdmin = "bdf699cf97284db0a9524332f0648f22".equals(currentUser.getUuid()) ? true : false;
		modelAndView.addObject("isSuperAdmin", isSuperAdmin);
		modelAndView.addObject("url", "modify_self_submit.do");
		return modelAndView;
	}

	/**
	 * 获得可选角色，即所有选项
	 * 
	 * @return
	 */
	private LinkedHashMap<Integer, String> findAllRoleOptions() {
		List<Role> roleList = null;
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		LinkedHashMap<Integer, String> allRoleOptions = new LinkedHashMap<Integer, String>();
		if (currentUser.isSysAdmin()) {
			allRoleOptions.put(1, "系统管理员");
		} else {
			roleList = roleService.findUserRoles(currentUser.getIid());
			for (Role role : roleList) {
				allRoleOptions.put(role.getIid(), role.getName());
			}
		}
		return allRoleOptions;
	}

	/**
	 * 编辑个人信息
	 * 
	 * @param user
	 *            用户实体
	 * @return
	 */
	@RequestMapping("modify_self_submit")
	@ResponseBody
	public JsonResult submitModifySelf(User user) {
		JsonResult jsonResult = JsonResult.getInstance();
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		boolean success = false;
		if (StringUtil.isNotEmpty(user.getPwd())) {
			user.setPwd(SecurityUtil.RSADecode(user.getPwd()));
		}
		user.setIid(currentUser.getIid());
		success = userService.modifySelf(user);
		jsonResult.setSuccess(success);
		if (success) {
			jsonResult.set(ResultState.MODIFY_SUCCESS);
		} else {
			jsonResult.set(ResultState.MODIFY_FAIL);
		}
		return jsonResult;
	}

	/**
	 * 显示修改个人密码页面
	 * 
	 * @return
	 */
	@RequestMapping("modify_password_show")
	public ModelAndView showForceModifyPassord(HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/complat/user/user_modify_password");
		modelAndView.addObject("checkPasswordLevel", Settings.getSettings().getCheckLevel());
		modelAndView.addObject("loginname", UserSessionInfo.getCurrentUser().getLoginName());
		modelAndView.addObject("url", "modify_password_submit.do");
		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, null);
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
	public JsonResult submitModifyPassword(ModifyPasswordForm modifyPassword, HttpSession session,
			HttpServletResponse response) {
		CurrentUser user = UserSessionInfo.getCurrentUser();
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			String loginname = modifyPassword.getLoginname();
			String opassword = modifyPassword.getOpassword();
			String password = modifyPassword.getPassword();
			String repassword = modifyPassword.getRepassword();
			Validate.getInstance(loginname).require("用户名不能为空")
					.match(Validate.Regex.USERNAME1, "登录名只能由字母、数字、下划线、中文组成，不能以下划线开头和结尾").maxLength(33, "登录名不能超过33个字");
			Validate.getInstance(opassword).require("原密码必须填写");
			Validate.getInstance(password).require("密码必须填写").minLength(6, "密码最少要6个字").maxLength(18, "密码最多18个字");
			Validate.getInstance(repassword).require("重复密码必须填写");

			if (!StringUtil.equals(password, repassword)) {
				throw new OperationException("两次密码必须一致");
			}

			if (!PwdCheckInterceptor.isLoginIdValid(loginname)) {
				throw new OperationException("用户名带有敏感字符");
			}

			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			String currentPwd = currentUser.getPwd();
			if (!Md5Util.isValidatePwd(opassword, currentPwd)) {
				throw new OperationException("原密码不正确");
			}

			boolean isSuccess = userService.modifyLoginIdAndPassword(user.getIid(), loginname, password);
			if (isSuccess) {
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			} else {
				jsonResult.set(ResultState.MODIFY_FAIL);
			}
		} catch (Exception e) {
			jsonResult.setMessage(e.getMessage());
		} finally {
			// 加入标志给dologin判断是否是csrf
			CsrfDefInterceptor.addCsrfToken(response, session, null);
		}
		return jsonResult;
	}

	/**
	 * 保持在线状态
	 */
	@RequestMapping("keep_online")
	@ResponseBody
	public void keepOnline() {
	}
}
