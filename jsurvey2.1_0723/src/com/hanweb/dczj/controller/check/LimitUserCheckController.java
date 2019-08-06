package com.hanweb.dczj.controller.check;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.service.SettingService;

@Controller
@RequestMapping("front/jsurvey")
public class LimitUserCheckController {
	
	@Autowired
	UserService userService;
	@Autowired
	SettingService settingService;

	@RequestMapping("checkUserLimit")
	@ResponseBody
	public JsonResult checkUserLimit(String dczjid){
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
	
	@RequestMapping("getUserInfo")
	@ResponseBody
	public JsonResult getUserInfo(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(false);
		dczjid = StringUtil.getSafeString(dczjid);
		
		HttpSession session = SpringUtil.getRequest().getSession();
		String sessionCode =null;
		if(session!=null){
			 sessionCode = (String)session.getAttribute("surveyloginname_"+dczjid);
		}
		if(StringUtil.isNotEmpty(sessionCode)){
			User user = userService.findByLoginName(sessionCode);
			if(user != null){
				jsonResult.addParam("username", user.getName());
				jsonResult.addParam("email", user.getEmail());
				jsonResult.addParam("mobile", user.getMobile());
				jsonResult.addParam("address", user.getAddress());
				jsonResult.setSuccess(true);
			}
		}
		return jsonResult;
	}
	
}
