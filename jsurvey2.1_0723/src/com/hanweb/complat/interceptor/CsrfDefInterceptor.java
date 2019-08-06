package com.hanweb.complat.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.BaseInfo;
import com.hanweb.common.interceptor.BaseInterceptor;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.common.util.mvc.ControllerUtil;

/**
 * csrf拦截器
 * 
 * @author 李杰
 *
 */
public class CsrfDefInterceptor extends BaseInterceptor {

	public final static String TOKEN = "csrf_token";

	public final static String REDIRECT_URL = "csrf_redirecturl";

	/**
	 * 增加csrf拦截标志，csrf的请求跳转地址为 应用路径
	 * 
	 * @param response
	 * @param session
	 */
	public static void addCsrfToken(HttpServletResponse response, HttpSession session) {
		addCsrfToken(response, session, null);
	}

	/**
	 * 增加csrf拦截标志
	 * 
	 * @param response
	 * @param session
	 * @param redirectUrl
	 *            csrf的请求跳转地址
	 */
	public static void addCsrfToken(HttpServletResponse response, HttpSession session, String redirectUrl) {
		// 加入标志给dologin判断是否是csrf
		String token = Md5Util.encode(StringUtil.getUUIDString());
		session.setAttribute(CsrfDefInterceptor.TOKEN, token);
		session.setAttribute(CsrfDefInterceptor.REDIRECT_URL, redirectUrl);
		ControllerUtil.removeCookie(response, CsrfDefInterceptor.TOKEN);
		// 5分钟超时
		ControllerUtil.addCookie(response, CsrfDefInterceptor.TOKEN, token, 60 * 5, true);
	}

	@Override
	public void after(HttpServletRequest req, HttpServletResponse resp, Object arg2, ModelAndView arg3) {

	}

	@Override
	public boolean before(HttpServletRequest req, HttpServletResponse resp, Object arg2) {
		String cookieToken = ControllerUtil.getCookieValue(TOKEN);
		String sessionToken = (String) req.getSession().getAttribute(TOKEN);
		String redirectUrl = (String) req.getSession().getAttribute(REDIRECT_URL);
		ControllerUtil.removeCookie(resp, TOKEN);
		req.getSession().removeAttribute(TOKEN);
		if (!StringUtil.equals(cookieToken, sessionToken)) {
			try {
				LogWriter.warn("the request is suspected a Csrf attack");
				resp.sendRedirect(StringUtil.isNotEmpty(redirectUrl) ? redirectUrl : BaseInfo.getContextPath());
			} catch (Exception e) {
				LogWriter.error("Csrf redirect error");
			}
			return false;
		}
		return true;
	}

	@Override
	public void complete(HttpServletRequest req, HttpServletResponse resp, Object arg2, Exception arg3) {
	}
}
