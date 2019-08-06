package com.hanweb.dczj.controller.front;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.interceptor.CsrfDefInterceptor;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.RoleRelationService;
import com.hanweb.complat.service.UserLoginService;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.Dczj_Setting;

import com.hanweb.dczj.entity.LimitLoginUser;
import com.hanweb.dczj.service.LimitLoginUserService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("front/jsurvey")
public class FrontUserLoginController {

	@Autowired
	UserService userService;
	@Autowired
	LimitLoginUserService limitLoginUserService;
	@Autowired
	SettingService settingService;
	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	RoleRelationService roleRelationService;
	
	@RequestMapping("userlogin_pc")
	public ModelAndView userloginpc(String dczjid,HttpServletResponse response, HttpSession session){
		ModelAndView model = new ModelAndView("dczj/front/index_pc");
		model.addObject("dczjid",dczjid);
		model.addObject("url", "login_pc_submit.do");
		ControllerUtil.addCookie(response, "_pubk", SecurityUtil.getPublicKey(), 60 * 60 * 24 * 30);
		model.addObject("appName", BaseInfo.getAppName());
        model.addObject("version", BaseInfo.getVersion());
		ControllerUtil.removeCookie(response, "page");
		ControllerUtil.removeCookie(response, "menu");
		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, "login_pc_submit.do");
		return model;
	}
	
	@RequestMapping("userlogin_phone")
	public ModelAndView userloginphone(String dczjid,HttpServletResponse response, HttpSession session){
		ModelAndView model = new ModelAndView("dczj/front/index_phone");
		model.addObject("dczjid",dczjid);
		model.addObject("url", "login_phone_submit.do");
		ControllerUtil.addCookie(response, "_pubk", SecurityUtil.getPublicKey(), 60 * 60 * 24 * 30);
		model.addObject("appName", BaseInfo.getAppName());
        model.addObject("version", BaseInfo.getVersion());
		ControllerUtil.removeCookie(response, "page");
		ControllerUtil.removeCookie(response, "menu");
		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, "login_phone_submit.do");
		return model;
	}
	
	@RequestMapping("login_pc_submit")
	@ResponseBody
	public JsonResult surveypc_submit(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) 
	String password,String logincode,String dczjid,HttpSession session,HttpServletResponse response){
		JsonResult jsonResult = JsonResult.getInstance();
		String ip = IpUtil.getIp();
		name = SecurityUtil.RSADecode(name);
		password = SecurityUtil.RSADecode(password);
		
//		HttpSession session = SpringUtil.getRequest().getSession();
		User user = userService.findByLoginName(name);
		if(user != null && user.getEnable() == 1){
			LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,null);
		if(limitLoginUser !=null && limitLoginUser.getLimitids()!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			if(limitLoginUser.getLimittype()==0 ) {
				//按机构
				String groupId = StringUtil.getString(user.getGroupId());
				String[] groupIds = limitLoginUser.getLimitids().split(",");
				for(int i=0;i<groupIds.length;i++) {
					if(StringUtil.isEmpty(groupIds[i]) || StringUtil.equals(groupIds[i], groupId)) {
						String storePwd = user.getPwd();
						if(Md5Util.isValidatePwd(password, storePwd)){
							String rand = (String) session.getAttribute("sessionid");
							session.removeAttribute("sessionid");
							if(StringUtil.isEmpty(logincode) || !StringUtil.equalsIgnoreCase(rand, logincode)){
								jsonResult.setMessage("验证码错误！");
								jsonResult.setSuccess(false);
							}else{
								CurrentUser currentUser = userLoginService.getUser(user, password, ip);
								session = UserSessionInfo.setCurrentUser(currentUser);
								ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, SecurityUtil.base64Encode(name),
						                    60 * 60 * 24 * 7, true);
								session.setAttribute("surveyloginname_"+dczjid, name);
								jsonResult.setSuccess(true);
								return jsonResult;
							}
						}else{
							jsonResult.setMessage("用户名或密码错误！");
							jsonResult.setSuccess(false);
						}
					}else {
						jsonResult.setMessage("该机构的用户无访问权限！");
					}
				}
//				jsonResult.setMessage("该用户无访问权限！");
				jsonResult.setSuccess(false);
			}else if (limitLoginUser.getLimittype()==1) {
				//按用户
				String userId = StringUtil.getString(user.getIid());
				String[] userIds = limitLoginUser.getLimitids().split(",");
				for(int i=0;i<userIds.length;i++) {
					if(StringUtil.isEmpty(userIds[i]) || StringUtil.equals(userIds[i], userId)) {
						String storePwd = user.getPwd();
						if(Md5Util.isValidatePwd(password, storePwd)){
							String rand = (String) session.getAttribute("sessionid");
							session.removeAttribute("sessionid");
							if(StringUtil.isEmpty(logincode) || !StringUtil.equalsIgnoreCase(rand, logincode)){
								jsonResult.setMessage("验证码错误！");
								jsonResult.setSuccess(false);
							}else{
								CurrentUser currentUser = userLoginService.getUser(user, password, ip);
								session = UserSessionInfo.setCurrentUser(currentUser);
								ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, SecurityUtil.base64Encode(name),
						                    60 * 60 * 24 * 7, true);
								session.setAttribute("surveyloginname_"+dczjid, name);
								jsonResult.setSuccess(true);
								return jsonResult;
							}
						}else{
							jsonResult.setMessage("用户名或密码错误！");
							jsonResult.setSuccess(false);
						}
					}else {
						jsonResult.setMessage("该用户无访问权限！");
					}
				}
//				jsonResult.setMessage("该用户无访问权限！");
				jsonResult.setSuccess(false);
			}else if (limitLoginUser.getLimittype()==2) {
				//按角色
				int roleID = roleRelationService.findRoleByUserId(user.getIid());
				String[] roleIds = limitLoginUser.getLimitids().split(",");
				for(int i=0;i<roleIds.length;i++) {
					if(StringUtil.isEmpty(roleIds[i]) || StringUtil.equals(roleIds[i], roleID+"")) {
						String storePwd = user.getPwd();
						if(Md5Util.isValidatePwd(password, storePwd)){
							String rand = (String) session.getAttribute("sessionid");
							session.removeAttribute("sessionid");
							if(StringUtil.isEmpty(logincode) || !StringUtil.equalsIgnoreCase(rand, logincode)){
								jsonResult.setMessage("验证码错误！");
								jsonResult.setSuccess(false);
							}else{
								CurrentUser currentUser = userLoginService.getUser(user, password, ip);
								session = UserSessionInfo.setCurrentUser(currentUser);
								ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, SecurityUtil.base64Encode(name),
						                    60 * 60 * 24 * 7, true);
								session.setAttribute("surveyloginname_"+dczjid, name);
								jsonResult.setSuccess(true);
								return jsonResult;
							}
						}else{
							jsonResult.setMessage("用户名或密码错误！");
							jsonResult.setSuccess(false);
						}
					}else {
						jsonResult.setMessage("该角色的用户无访问权限！");
					}
				}
//				jsonResult.setMessage("该用户无访问权限！");
				jsonResult.setSuccess(false);
			}else {
				jsonResult.setMessage("该限制方式不存在！");
				jsonResult.setSuccess(false);
			}
		 }else {
			 jsonResult.setMessage("该用户不存在！");
			 jsonResult.setSuccess(false);
		 }
		}else {
			jsonResult.setMessage("该用户不存在！");
			jsonResult.setSuccess(false);
		}
		
		return jsonResult;
	}
	
	@RequestMapping("login_phone_submit")
	@ResponseBody
	public JsonResult surveyphone_submit(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) 
	String password,String logincode,String dczjid,HttpSession session,HttpServletResponse response){
		JsonResult jsonResult = JsonResult.getInstance();
		String ip = IpUtil.getIp();
		name = SecurityUtil.RSADecode(name);
		password = SecurityUtil.RSADecode(password);
		
//		HttpSession session = SpringUtil.getRequest().getSession();
		User user = userService.findByLoginName(name);
		if(user != null && user.getEnable() == 1){
			LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,null);
		if(limitLoginUser !=null && limitLoginUser.getLimitids()!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			if(limitLoginUser.getLimittype()==0) {
				//按机构
				String groupId = StringUtil.getString(user.getGroupId());
				String[] groupIds = limitLoginUser.getLimitids().split(",");
				for(int i=0;i<groupIds.length;i++) {
					if(StringUtil.equals(groupIds[i], groupId)) {
						String storePwd = user.getPwd();
						if(Md5Util.isValidatePwd(password, storePwd)){	
							String rand = (String) session.getAttribute("sessionid");
							session.removeAttribute("sessionid");
							if(StringUtil.isEmpty(logincode) || !StringUtil.equalsIgnoreCase(rand, logincode)){
								jsonResult.setMessage("验证码错误！");
								jsonResult.setSuccess(false);
							}else{
								CurrentUser currentUser = userLoginService.getUser(user, password, ip);
								session = UserSessionInfo.setCurrentUser(currentUser);
								ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, SecurityUtil.base64Encode(name),
						                    60 * 60 * 24 * 7, true);
								session.setAttribute("surveyloginname_"+dczjid, name);
								jsonResult.setSuccess(true);
								return jsonResult;
							}
						
						}else{
							jsonResult.setMessage("用户名或密码错误！");
							jsonResult.setSuccess(false);
						}
					}else {
						jsonResult.setMessage("该用户无访问权限！");
					}
				}
//				jsonResult.setMessage("该用户无访问权限！");
				jsonResult.setSuccess(false);
			}else if (limitLoginUser.getLimittype()==1) {
				//按用户
				String userId = StringUtil.getString(user.getIid());
				String[] userIds = limitLoginUser.getLimitids().split(",");
				for(int i=0;i<userIds.length;i++) {
					if(StringUtil.equals(userIds[i], userId)) {
						String storePwd = user.getPwd();
						if(Md5Util.isValidatePwd(password, storePwd)){	
							String rand = (String) session.getAttribute("sessionid");
							session.removeAttribute("sessionid");
							if(StringUtil.isEmpty(logincode) || !StringUtil.equalsIgnoreCase(rand, logincode)){
								jsonResult.setMessage("验证码错误！");
								jsonResult.setSuccess(false);
							}else{
								CurrentUser currentUser = userLoginService.getUser(user, password, ip);
								session = UserSessionInfo.setCurrentUser(currentUser);
								ControllerUtil.addCookie(response, StaticValues.LOGIN_COOKIE, SecurityUtil.base64Encode(name),
						                    60 * 60 * 24 * 7, true);
								session.setAttribute("surveyloginname_"+dczjid, name);
								jsonResult.setSuccess(true);
								return jsonResult;
							}
						
						}else{
							jsonResult.setMessage("用户名或密码错误！");
							jsonResult.setSuccess(false);
						}
					}else {
						jsonResult.setMessage("该用户无访问权限！");
					}
				}
//				jsonResult.setMessage("该用户无访问权限！");
				jsonResult.setSuccess(false);
			}else {
				jsonResult.setMessage("该限制方式不存在！");
				jsonResult.setSuccess(false);
			}
		}else {
			 jsonResult.setMessage("该用户不存在！");
			 jsonResult.setSuccess(false);
		}
		}else {
			jsonResult.setMessage("该用户不存在！");
			jsonResult.setSuccess(false);
		}
		
		return jsonResult;
	}
	
	
	@RequestMapping("checkOpenLimit")
	@ResponseBody
	public JsonResult checkOpenLimit(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		
		dczjid = StringUtil.getSafeString(dczjid);
		Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
		
		HttpSession session = SpringUtil.getRequest().getSession();
		String sessionCode =null;
		if(session!=null){
			 sessionCode = (String)session.getAttribute("surveyloginname_"+dczjid);
		}
		
		if(setting != null && setting.getIslimituser() == 1 && StringUtil.isEmpty(sessionCode)){
			jsonResult.setSuccess(true);
		}else{
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}
}
