package com.hanweb.setup.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.interceptor.BaseInterceptor;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.setup.controller.LogInOutController;

/**
 * 
 * @author 李杰
 * 
 */
public class PwdCheckInterceptor extends BaseInterceptor {

	/**
	 * 默认password
	 */
	private static final String PASSWORD = "hanweb";

	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Properties setupProp = getSetupProp();
		int pwdLevel = (Integer) request.getSession().getAttribute(LogInOutController.SESSION_KEY_PWD_LEVEL);
		String encodePwd = setupProp.getString("adminpw");
		if (Md5Util.isValidatePwd(PASSWORD, encodePwd) || pwdLevel < Settings.getSettings().getCheckLevel()) {
			try {
				String url = BaseInfo.getContextPath() + "/setup/main/modify_password_show.do";
				Script script = Script.getInstanceWithJsLib();
				script.addScript("top.location.href='" + url + "'");
				ControllerUtil.showHtml(script.getScript(), response);
			} catch (Exception e) {
				LogWriter.error("sendRedirect error", e);
			}
			return false;
		}
		return true;
	}

	@Override
	public void after(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

	}

	@Override
	public void complete(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	}

	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}
}
