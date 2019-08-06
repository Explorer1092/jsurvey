package com.hanweb.complat.listener;

import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.support.controller.CurrentUser;

/**
 * 在线用户
 * 
 * @author 李杰
 *
 */
public class UserSessionInfo {

	private static final Log LOGGER = LogFactory.getLog(UserSessionInfo.class);

	/**
	 * 获得当前用户--从session中.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 用户
	 */
	public static CurrentUser getCurrentUser() {
		HttpSession session = SpringUtil.getRequest().getSession(false);
		CurrentUser currentUser = null;
		if (session != null && session.getAttribute(StaticValues.USERINFO) != null) {
			currentUser = (CurrentUser) session.getAttribute(StaticValues.USERINFO);
		}
		return currentUser;
	}

	/**
	 * 保存用户信息到session和在线用户.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param user
	 *            用户
	 */
	public static HttpSession setCurrentUser(CurrentUser user) {
		HttpSession session = null;
		try {
			if (user != null) {
				SpringUtil.getRequest().getSession().invalidate();
				session = SpringUtil.getRequest().getSession(true);
				// 设置用户session超时时间为半个小时
				session.setMaxInactiveInterval(60 * Settings.getSettings().getSessionTime());
				session.setAttribute(StaticValues.USERINFO, user);
			}
		} catch (Exception e) {
			LOGGER.error("setCurrentUser Error:", e);
		}
		return session;
	}

	/**
	 * 删除用户信息 ---从session和在线用户中.
	 * 
	 * @param request
	 *            HttpServletRequest.
	 */
	public static void removeCurrentUser() {
		SpringUtil.getRequest().getSession().invalidate();
	}
}
