package com.hanweb.sso.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.TemplateParser;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.jis.expansion.webservice.SSoAuthApp;
import com.hanweb.sso.bean.App;
import com.hanweb.sso.bean.AppsResult;

/**
 * sso
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("manager/sso")
public class OprSsoAppsController {

	private final static String USER_APPS_SEESION_KEY = "userAppMap";

	private final static String DEFAULTIMAGEURL = BaseInfo.getContextPath() + "/resources/complat/images/default.png";

	private final static String SSO_CURRAPPS_STYLE = "<div><a class=\"app_current\" title=\"${app.name}\">"
			+ "<img src=\"${app.imageUrl }\"></a></div>";

	private final static String SSO_APPS_STYLE = "<div><a title=\"${app.name}\" onclick=\"ssoLogin('${app.uuid}')\">"
			+ "<img src=\"${app.imageUrl }\"></a></div>";
	
	private String stateHeadKey = "state";

	@RequestMapping("apps_show")
	@ResponseBody
	public String showApps(HttpSession httpSession) {
		String result = null;
		if (BaseInfo.isSso()) {
			try {
				String userName = UserSessionInfo.getCurrentUser().getLoginName();
				String appsString = SSoAuthApp.findUserApps(userName, "5", 1);
				AppsResult appsResult = JsonUtil.StringToObject(appsString, AppsResult.class);
				if (appsResult == null) {
					return result;
				}
				String stateHead = "2";
				if (StringUtil.equals(appsResult.getHead().get(stateHeadKey), stateHead)) {
					// result = appsResult.getHead().get("errormsg");
					return result;
				}
				List<App> apps = appsResult.getApps();
				if (CollectionUtils.isNotEmpty(apps)) {
					StringBuffer buffer = new StringBuffer();
					Map<String, App> userAppMap = new HashMap<String, App>(6);
					for (App app : apps) {
						if (app != null) {
							if (StringUtil.isEmpty(app.getImageUrl())) {
								app.setImageUrl(DEFAULTIMAGEURL);
							}
							if (app.getCurrent()) {
								buffer.insert(0, TemplateParser.parserTemplate(SSO_CURRAPPS_STYLE, "app", app));
							} else {
								buffer.append(TemplateParser.parserTemplate(SSO_APPS_STYLE, "app", app));
							}
							userAppMap.put(app.getUuid(), app);
						}
					}
					httpSession.setAttribute(USER_APPS_SEESION_KEY, userAppMap);
					result = buffer.toString();
				}
			} catch (Exception e) {
				LogWriter.error("showApps error", e);
			}
		}
		return result;
	}

	/**
	 * 开始单点登录
	 * 
	 * @param uuid
	 * @param httpSession
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("sso_login")
	@ResponseBody
	public String ssoLogin(String uuid, HttpSession httpSession, HttpServletResponse response) {
		String result = null;
		String userName = UserSessionInfo.getCurrentUser().getLoginName();
		String resultStr = SSoAuthApp.getTicket(uuid, userName);
		if (StringUtil.isNotEmpty(resultStr)) {
			Map<String, String> resultMap = JsonUtil.StringToObject(resultStr, Map.class);
			if (resultMap != null) {
				String errKey = "errormsg";
				if (StringUtil.isNotEmpty(resultMap.get(errKey))) {
					result = resultMap.get(errKey);
				} else {
					userName = resultMap.get("username");
					String ticket = resultMap.get("ticket");
					Map<String, App> apps = (Map<String, App>) httpSession.getAttribute(USER_APPS_SEESION_KEY);
					App app = apps.get(uuid);
					try {
						String url = app.getUrl() + "?ticket=" + ticket;
						response.sendRedirect(url);
					} catch (Exception e) {
					}
				}
			}
		}
		return result;
	}
}
