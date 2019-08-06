package com.hanweb.complat.listener;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hanweb.common.BaseInfo;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.support.controller.CurrentUser;

/**
 * 在线用户监听
 * 
 * @author 李杰
 * 
 */
public class OnlineUserListener implements HttpSessionAttributeListener, HttpSessionListener {
	/**
     * 
     */
	protected static final Log LOGGER = LogFactory.getLog(OnlineUserListener.class);

	/**
	 * 存储用户，以便剔除
	 */
	public static final Vector<CurrentUser> ONLINE_ENTITIES = new Vector<CurrentUser>(0);
	
	/**
	 * 存储用户session
	 */
	public static final Map<String, HttpSession> USERSESSIONS = new ConcurrentHashMap<String, HttpSession>();

	/**
	 * 剔除用户
	 * 
	 * @param sessionId
	 *            sessionid
	 * @param loginName
	 *            用户登录名
	 */
	private void kickUser(String sessionId, String loginName) {
		if (BaseInfo.isKick()) {
			for (int i = 0; i < ONLINE_ENTITIES.size(); i++) {
				if (!sessionId.equals(ONLINE_ENTITIES.get(i).getSessionId())) {
					if (ONLINE_ENTITIES.get(i).getLoginName().equals(loginName)) {
						HttpSession httpSession = USERSESSIONS.get(ONLINE_ENTITIES.get(i).getSessionId());
						if(httpSession != null){
							httpSession.invalidate();
							USERSESSIONS.remove(sessionId);
							i--;
						}
					}
				}
			}
		}
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		if (StaticValues.USERINFO.equals(se.getName()) && se.getValue() != null) {
			CurrentUser currentUser = (CurrentUser) se.getValue();
			kickUser(se.getSession().getId(), currentUser.getLoginName());
			currentUser.setSessionId(se.getSession().getId());
			USERSESSIONS.put(se.getSession().getId(), se.getSession());
			ONLINE_ENTITIES.add(currentUser);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se) {
		if (StaticValues.USERINFO.equals(se.getName()) && se.getValue() != null) {
			String sessionId = se.getSession().getId();
			for (CurrentUser onlineUser : ONLINE_ENTITIES) {
				if (sessionId.equals(onlineUser.getSessionId())) {
					CurrentUser user = (CurrentUser) se.getSession().getAttribute(StaticValues.USERINFO);
					user.setSessionId(sessionId);
					USERSESSIONS.put(sessionId, se.getSession());
					break;
				}
			}
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		String sessionId = se.getSession().getId();
		for (int i = 0; i < ONLINE_ENTITIES.size(); i++) {
			if (ONLINE_ENTITIES.get(i).getSessionId().equals(sessionId)) {
				ONLINE_ENTITIES.remove(i);
				break;
			}
		}
	}
}
