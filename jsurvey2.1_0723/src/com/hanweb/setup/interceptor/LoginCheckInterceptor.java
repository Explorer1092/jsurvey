package com.hanweb.setup.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.interceptor.BaseInterceptor;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.log.LogWriter;

/**
 * setup登陆拦截器
 * 
 * @author 李杰
 *
 */
public class LoginCheckInterceptor extends BaseInterceptor {

	/**
	 * 默认password
	 */
	private static final String PASSWORD = "admin";

	@Override
	public void after(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
		HttpSession session = request.getSession();
		String name = (String) session.getAttribute("setupuser");
		if (name == null || !StringUtil.equals(name, PASSWORD)) {
			try {
				response.sendRedirect(BaseInfo.getContextPath() + "/setup/login.do");
			} catch (Exception e) {
				LogWriter.error("sendRedirect error", e);
			}
			return false;
		}
		return true;
	}

	@Override
	public void complete(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	}

}
