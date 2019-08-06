package com.hanweb.dczj.controller.ques;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("manager/dczj")
public class QuesSessionController {

	@Autowired
	private TitleInfoService titleInfoService;
	
	@RequestMapping("setquessession")
	@ResponseBody
	public JsonResult setquessession(String url, String dczjid ,String type) {
		JsonResult jsonResult = JsonResult.getInstance();
//		url = StringUtil.getSafeString(url);
		dczjid = StringUtil.getSafeString(dczjid);
		type = StringUtil.getSafeString(type);
		
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			session.setAttribute("ques_"+dczjid+"_session_url", url);
			session.setAttribute("ques_"+dczjid+"_session_type", type);
			jsonResult.setSuccess(true);
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("操作失败，请联系管理员！");
		}
		return jsonResult;
	}
	
	@RequestMapping("getquessession")
	@ResponseBody
	public JsonResult getquessession(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		jsonResult.setSuccess(true);
		HttpSession session = SpringUtil.getRequest().getSession();
		String url = "";
		String type = "";
		if(session!=null){
			url = (String)session.getAttribute("ques_"+dczjid+"_session_url");
			type = (String)session.getAttribute("ques_"+dczjid+"_session_type");
			jsonResult.addParam("url", url);
			jsonResult.addParam("type", type);
		}
		return jsonResult;
	}
	
	@RequestMapping("backhomepage")
	@ResponseBody
	public JsonResult backhomepage(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		jsonResult.setSuccess(true);
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			session.removeAttribute("ques_"+dczjid+"_session_url");
			session.removeAttribute("ques_"+dczjid+"_session_type");
			session.removeAttribute("title_session_dczjid");
			TitleInfo infoEn = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
			int webid = infoEn.getWebid();
			jsonResult.addParam("webid", webid);
		}
		return jsonResult;
	}
}
