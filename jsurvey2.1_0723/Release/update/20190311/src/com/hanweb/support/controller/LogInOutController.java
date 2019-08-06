package com.hanweb.support.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.Constants;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.VerifyCode;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.util.security.QrcodeMaker;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.cache.QrcodeCache;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.complat.entity.AccessLog;
import com.hanweb.complat.entity.BanList;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.LoginException;
import com.hanweb.complat.interceptor.CsrfDefInterceptor;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.AccessLogService;
import com.hanweb.complat.service.BanListService;
import com.hanweb.complat.service.UserLoginService;
import com.hanweb.complat.service.UserService;

/**
 * 登陆
 * 
 * @author 李杰
 *
 */
@Controller("adminlogin")
@RequestMapping
public class LogInOutController {

	private static final String LOGIN_ERROR = "login.error";

	@Autowired
	private UserService userService;

	@Autowired
	private UserLoginService userLoginService;

	@Autowired
	private BanListService banListService;

	@Autowired
	private AccessLogService accessLogService;

	/**
	 * 登录后第一个页面
	 */
	private static final String FIRSTPAGE = "manager/index.do";

	@RequestMapping("login")
	public ModelAndView login(HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("support/login");
		modelAndView.addObject("username",
				StringUtil.decoder(ControllerUtil.getCookieValue(StaticValues.LOGIN_COOKIE)));
		ControllerUtil.addCookie(response, "_pubk", SecurityUtil.getPublicKey(), 60 * 60 * 24 * 30);
		modelAndView.addObject("appName", BaseInfo.getAppName());
		modelAndView.addObject("version", BaseInfo.getVersion());
		ControllerUtil.removeCookie(response, "page");
		ControllerUtil.removeCookie(response, "menu");
		modelAndView.addObject("url", "dologin.do");
		modelAndView.addObject("isVerifyCode", Settings.getSettings().isEnableVerifyCode());
		modelAndView.addObject("dynamicCodeLogin", Settings.getSettings().isDynamicCodeLogin());
		modelAndView.addObject("qrcodeLogin", Settings.getSettings().isQrcodeLogin());
		if (Settings.getSettings().isDynamicCodeLogin()) {
			modelAndView.addObject("qrcodePath", StaticValues.QRCODE_URL);
		}

		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, "login.do");

		return modelAndView;
	}

	@RequestMapping("dologin")
	@ResponseBody
	public JsonResult doLogin(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "randcode", required = false) String randCode,
			@RequestParam(value = "dynamiccode", required = false) String dynamicCode, HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		JsonResult jsonResult = JsonResult.getInstance();
		String ip = IpUtil.getIp();
		BanList banList = null;
		try {
			// 判断动态码
			if (Settings.getSettings().isDynamicCodeLogin()) {
				if (StringUtil.isEmpty(dynamicCode) || NumberUtil.isNotNumeric(dynamicCode)) {
					throw new LoginException("login.dynamiccode.error");
				}
			} else if (Settings.getSettings().isEnableVerifyCode()) {
				String rand = (String) session.getAttribute("rand");
				session.removeAttribute("rand");
				if (StringUtil.isEmpty(randCode) || !StringUtil.equalsIgnoreCase(rand, randCode)) {
					throw new LoginException("login.randcode.error");
				}
			}
			if (StringUtil.isEmpty(name) || StringUtil.isEmpty(password)) {
				throw new LoginException(LOGIN_ERROR);
			}
			name = SecurityUtil.RSADecode(name);
			password = SecurityUtil.RSADecode(password);
			banList = banListService.checkLoginTimes(name, ip);
			if (!banList.isCanLogin()) {
				throw new LoginException(LOGIN_ERROR);
			}
			CurrentUser user = userLoginService.checkUserLogin(name, password, dynamicCode, ip);
			if (user != null) {
				if(user.getRoleList() == null || user.getRoleList().size() == 0) {
					throw new LoginException("users have no privileges");
				}
				UserSessionInfo.setCurrentUser(user);
				ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, name, 60 * 60 * 24 * 7, true);
				banList.setLoginTimes(0);
				if (banList != null && banList.getIid() != null) {
					banListService.removeById(banList.getIid());
				}
				AccessLog accessLog = new AccessLog();
				accessLog.setIp(ip);
				accessLog.setLoginName(name);
				accessLog.setAccesstime(user.getAccessTime());
				accessLogService.record(accessLog);
				jsonResult.setSuccess(true);
			} else {
				throw new LoginException(LOGIN_ERROR);
			}
		} catch (LoginException e) {
			if (banList != null && LOGIN_ERROR.equals(e.getMessage())) {
				int last = Settings.getSettings().getLoginError() - banList.getLoginTimes() - 1;
				String message = null;
				if (last > 0) {
					message = SpringUtil.getMessage(e.getMessage());
					message += "<br/>" + SpringUtil.getMessage("login.error.limit", last);
					banList.setLoginTimes(banList.getLoginTimes() + 1);
					banListService.modify(banList);
				} else {
					message = SpringUtil.getMessage("user.login.times", Settings.getSettings().getBanTimes());
					banList.setLoginTimes(banList.getLoginTimes() + 1);
					banListService.modify(banList);
				}
				jsonResult.setMessage(message);
			} else {
				jsonResult.setMessage(e.getMessage());
			}
			// 登录失败后csrftoken需要重新赋值
			CsrfDefInterceptor.addCsrfToken(response, session, "login.do");
		}
		return jsonResult;
	}

	/**
	 * 获得登陆二维码
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("get_login_qrcode")
	@ResponseBody
	public void getLoginQrcode(HttpSession session, HttpServletResponse response) {
		String qrcodeId = (String) session.getAttribute("qrcodeid");
		// 清除掉缓存中的uuid
		if (StringUtil.isNotEmpty(qrcodeId)) {
			QrcodeCache.removeQrcodeId(qrcodeId);
		}
		qrcodeId = StringUtil.getUUIDString();
		// 缓存qrcodeId，值为DEFAULT_CACHEVALUE
		QrcodeCache.addQrcodeId(qrcodeId);
		session.setAttribute("qrcodeid", qrcodeId);
		QrcodeMaker maker = new QrcodeMaker(200);
		// des加密
		Map<String, String> result = new HashMap<String, String>(Constants.MAP_INIT_SIZE);
		result.put("code", SecurityUtil.DESEncode(qrcodeId, QrcodeCache.QRCODE_CACHE_NAME));
		result.put("app", BaseInfo.getAppName());
		maker.drawLogoQRCode(JsonUtil.objectToString(result), response);
	}

	/**
	 * 二维码登陆
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("qrcode_login")
	@ResponseBody
	public JsonResult qrcodeLogin(String qrcodeid, String name, HttpSession session) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			if (StringUtil.isEmpty(qrcodeid)) {
				throw new LoginException("二维码为空");
			}
			if (StringUtil.isEmpty(name)) {
				throw new LoginException("登陆名为空");
			}
			// 解密
			qrcodeid = SecurityUtil.DESDecode(qrcodeid, QrcodeCache.QRCODE_CACHE_NAME);
			if (!QrcodeCache.validQrcodeId(qrcodeid)) {
				throw new LoginException("二维码错误");
			}
			User user = userService.findByLoginName(name);
			if (user == null) {
				throw new LoginException("登陆名错误");
			}
			// 验证成功，缓存qrcodeid与登录名
			QrcodeCache.addQrcodeId(qrcodeid, name);
			jsonResult.set(ResultState.OPR_SUCCESS, "二维码登陆验证成功");
		} catch (LoginException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 轮询检测二维码登陆，state的-1：二维码失效，0：待登陆，1：登陆成功
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("check_qrcode_login")
	@ResponseBody
	public JsonResult checkQrcodeLogin(HttpServletRequest request, HttpSession session) {
		JsonResult jsonResult = JsonResult.getInstance();
		String qrcodeId = (String) session.getAttribute("qrcodeid");
		String name = QrcodeCache.getQrcodeValue(qrcodeId);
		if (StringUtil.isEmpty(name)) {
			jsonResult.addParam("state", -1);
			return jsonResult;
		}
		if (StringUtil.equals(name, QrcodeCache.DEFAULT_CACHEVALUE)) {
			jsonResult.addParam("state", 0);
			return jsonResult;
		}
		String ip = IpUtil.getIp();
		try {
			CurrentUser user = userLoginService.checkUserLogin(name, ip);
			if (user != null) {
				UserSessionInfo.setCurrentUser(user);
				AccessLog accessLog = new AccessLog();
				accessLog.setIp(ip);
				accessLog.setLoginName(name);
				accessLog.setAccesstime(user.getAccessTime());
				accessLogService.record(accessLog);
				jsonResult.setSuccess(true);
			} else {
				throw new LoginException(LOGIN_ERROR);
			}
		} catch (LoginException e) {
			jsonResult.setMessage(e.getMessage());
		} finally {
			if (jsonResult.isSuccess()) {
				QrcodeCache.removeQrcodeId(qrcodeId);
			}
		}
		return jsonResult;
	}

	@RequestMapping("jislogin")
	@ResponseBody
	public String jisLogin(@RequestParam(value = "loginuser", required = false) String userName,
			@RequestParam(value = "loginpass", required = false) String password,
			@RequestParam(value = "randcode", required = false) String randCode, HttpServletResponse response,
			HttpSession session) {
		Script script = interfaceDoLogin(userName, password, true, response);
		return script.getScript();
	}

	@RequestMapping("ssologin")
	@ResponseBody
	public String ssoLogin(@RequestParam(value = "username", required = false) String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "randcode", required = false) String randCode, HttpServletResponse response,
			HttpSession session) {
		Script script = interfaceDoLogin(userName, password, false, response);
		return script.getScript();
	}

	/**
	 * 接口用户登录统一处理
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param isJis
	 *            是否是jis
	 * @param request
	 * @param response
	 * @return
	 */
	private Script interfaceDoLogin(String userName, String password, boolean isJis, HttpServletResponse response) {
		Script script = Script.getInstanceWithJsLib();
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			script.addAlert(SpringUtil.getMessage(LOGIN_ERROR));
			script.addScript("top.location.href = 'login.do'");
			return script;
		}
		if (isJis) {
			// 调用jar包中解密方式解密
			com.hanweb.jis.expansion.webservice.UserService service = new com.hanweb.jis.expansion.webservice.UserService();
			// type:表示该用户是前台用户或者后台用户
			String type = "inner";
			userName = service.getUserInfo(userName, 1, type);
			password = service.getUserInfo(password, 2, type);
		}
		try {
			String ip = IpUtil.getIp();
			CurrentUser user = userLoginService.checkUserLogin(userName, password, ip);
			if (user != null) {
				UserSessionInfo.setCurrentUser(user);
				script.addScript("top.location.href = '" + FIRSTPAGE + "'");
				ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, userName, 60 * 60 * 24 * 365);
			} else {
				throw new LoginException(LOGIN_ERROR);
			}
		} catch (LoginException e) {
			script.addAlert(SpringUtil.getMessage(e.getMessage()));
			script.addScript("top.location.href = 'login.do'");
		}
		return script;
	}

	@RequestMapping("logout")
	public ModelAndView logout() {
		UserSessionInfo.removeCurrentUser();
		ModelAndView modelAndView = new ModelAndView();
		RedirectView redirectView = new RedirectView("login.do");
		modelAndView.setView(redirectView);
		return modelAndView;
	}

	@RequestMapping("verifyCode")
	@ResponseBody
	public String showVerifyCode(HttpServletResponse response) {
		return VerifyCode.generate(response, "rand");
	}
}
