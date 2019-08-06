package com.hanweb.setup.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.CheckPwdStrong;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.VerifyCode;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.exception.LoginException;
import com.hanweb.complat.interceptor.CsrfDefInterceptor;

/**
 * setup登陆
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("setup")
public class LogInOutController {

	private static final String PASSWORD_PROP_KEY = "adminpw";

	private final static String SESSION_KEY = "setupuser";

	public final static String SESSION_KEY_PWD_LEVEL = "setupuser_pwd_level";

	private final static String ADMIN_NAME = "admin";

	@RequestMapping("login")
	public ModelAndView login(HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("setup/login");
        modelAndView.addObject("username",
            SecurityUtil.base64Decode(StringUtil.decoder(ControllerUtil.getCookieValue(SESSION_KEY))));
		modelAndView.addObject("url", "dologin.do");
		ControllerUtil.addCookie(response, "_pubk", SecurityUtil.getPublicKey(), 60 * 60 * 24 * 30);
		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, "login.do");
		return modelAndView;
	}

	@RequestMapping("dologin")
	@ResponseBody
	public JsonResult doLogin(@RequestParam(value = "username", required = false) String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "randcode", required = false) String randCode, HttpSession session,
			HttpServletResponse response) {
		JsonResult jsonResult = JsonResult.getInstance();
		Properties prop = getSetupProp();
		String rand = (String) session.getAttribute("setup_rand");
		try {
			if (StringUtil.isEmpty(randCode) || !StringUtil.equalsIgnoreCase(rand, randCode)) {
				session.setAttribute("setup_rand", null);
				throw new LoginException("login.randcode.error");
			}
			if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
				throw new LoginException("login.error");
			}
			userName = SecurityUtil.RSADecode(userName);
			password = SecurityUtil.RSADecode(password);
			if (StringUtil.equals(ADMIN_NAME, userName)
					&& Md5Util.isValidatePwd(password, prop.getString(PASSWORD_PROP_KEY))) {
				session.invalidate();
				session = SpringUtil.getRequest().getSession(true);
				session.setAttribute(SESSION_KEY, ADMIN_NAME);
				session.setAttribute(SESSION_KEY_PWD_LEVEL, CheckPwdStrong.check(password));
                ControllerUtil.addCookie(response, SESSION_KEY, SecurityUtil.base64Encode(ADMIN_NAME), 60 * 60 * 24 * 7,
                    true);
				jsonResult.setSuccess(true);
			} else {
				throw new LoginException("login.error");
			}
		} catch (LoginException e) {
			jsonResult.setMessage(e.getMessage());
			// 登录失败后csrftoken需要重新赋值
			CsrfDefInterceptor.addCsrfToken(response, session, "login.do");
		} finally {
			session.removeAttribute("setup_rand");
		}
		return jsonResult;
	}

	@RequestMapping("logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView modelAndView = new ModelAndView();
		RedirectView redirectView = new RedirectView("login.do");
		modelAndView.setView(redirectView);
		return modelAndView;
	}

	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}

	@RequestMapping("verifyCode")
	@ResponseBody
	public String showVerifyCode(HttpSession session, HttpServletResponse response) {
		return VerifyCode.generate(response, "setup_rand");
	}
}
