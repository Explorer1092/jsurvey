package com.hanweb.complat.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.interceptor.BaseInterceptor;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.complat.listener.UserSessionInfo;

/**
 * 登陆拦截器
 * 
 * @author 李杰
 *
 */
public class LoginCheckInterceptor extends BaseInterceptor {

	@Override
	public void after(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	@Override
	public boolean before(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (UserSessionInfo.getCurrentUser() == null) {
			try {
				response.sendRedirect(BaseInfo.getContextPath() + "/error/300.do");
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
