package com.hanweb.complat.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.interceptor.BaseInterceptor;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.support.controller.CurrentUser;

/**
 * 
 * @author 李杰
 * 
 */
public class PwdCheckInterceptor extends BaseInterceptor {
	/**
	 * 不可以等于的字符
	 */
	private static List<String> LoginIdblackList_equals = new ArrayList<String>();
	
	/**
	 * 不可以含有的字符
	 */
	private static List<String> LoginIdblackList_contain = new ArrayList<String>();

	public PwdCheckInterceptor() {
		LoginIdblackList_equals.add("sa");
		LoginIdblackList_equals.add("dba");
		LoginIdblackList_equals.add("sys");
		
		LoginIdblackList_contain.add("admin");
		LoginIdblackList_contain.add("root");
		LoginIdblackList_contain.add("console");
		LoginIdblackList_contain.add("manage");
		LoginIdblackList_contain.add("system");
	}

	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
		CurrentUser user = UserSessionInfo.getCurrentUser();
		Integer pwdLevel = user.getPwdLevel();
		if (pwdLevel >= Settings.getSettings().getCheckLevel()
				&& isLoginIdValid(user.getLoginName())) {
			return true;
		} else {
			try {
				String url = BaseInfo.getContextPath() + "/manager/user/modify_password_show.do";
				Script script = Script.getInstanceWithJsLib();
				script.addScript("top.location.href='" + url + "'");
				ControllerUtil.showHtml(script.getScript(), response);
			} catch (Exception e) {
				LogWriter.error("sendRedirect error", e);
			}
			return false;
		}
	}

	public static boolean isLoginIdValid(String loginId) {
		loginId = loginId.toLowerCase();
		if (LoginIdblackList_equals.contains(loginId)) {
			return false;
		}
		for (String str : LoginIdblackList_contain) {
			if (loginId.contains(str)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void after(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

	}

	@Override
	public void complete(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
	}
}
