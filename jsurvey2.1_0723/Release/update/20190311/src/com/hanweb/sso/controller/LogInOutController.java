package com.hanweb.sso.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.complat.exception.LoginException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.UserLoginService;
import com.hanweb.jis.expansion.webservice.SSoAuthApp;
import com.hanweb.support.controller.CurrentUser;

/**
 * 验证jis的sso登陆
 * 
 * @author 李杰
 *
 */
@Controller("jis_login")
@RequestMapping("jis")
public class LogInOutController {

	private static final String ERROR_MSG = "errormsg";

	@Autowired
	private UserLoginService userLoginService;

	/**
	 * 验证jis的sso登陆
	 * 
	 * @param ticket
	 * @param response
	 * @return
	 */
	@RequestMapping("ssologin")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public String ssoLogin(String ticket, HttpServletResponse response) {
		Script script = Script.getInstanceWithJsLib();
		try {
			if (StringUtil.isEmpty(ticket)) {
				throw new LoginException("ticket is none");
			}
			String resultStr = SSoAuthApp.ticketValidate(ticket);
			if (StringUtil.isEmpty(resultStr)) {
				throw new LoginException("sso login error");
			}
			Map<String, String> resultMap = JsonUtil.StringToObject(resultStr, Map.class);
			if (StringUtil.isNotEmpty(resultMap.get(ERROR_MSG))) {
				throw new LoginException(resultMap.get(ERROR_MSG));
			}
			String userName = resultMap.get("loginname");
			String ip = IpUtil.getIp();
			CurrentUser user = userLoginService.checkUserLogin(userName, ip);
			if (user != null) {
				if(user.getRoleList() == null || user.getRoleList().size() == 0) {
					throw new LoginException("users have no privileges");
				}
				UserSessionInfo.setCurrentUser(user);
				script.addScript("top.location.href = '../manager/index.do'");
				ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, userName, 60 * 60 * 24 * 365);
			} else {
				throw new LoginException("login.error");
			}
		} catch (LoginException e) {
			script.addAlert(SpringUtil.getMessage(e.getMessage()));
			script.addScript("top.location.href = '../login.do'");
		}
		return script.getScript();
	}
}
