package com.hanweb.dczj.controller.title;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;

@RequestMapping("manager/dczj")
@Controller
public class TitleSessionController {
	
	@Autowired
	private WebSiteService webSiteService;
	
	@RequestMapping("settitlesession")
	@ResponseBody
	public JsonResult settitlesession(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			session.setAttribute("title_session_dczjid", dczjid);
			jsonResult.setSuccess(true);
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("操作失败，请联系管理员！");
		}
		return jsonResult;
	}
	
	@RequestMapping("gettitlesession")
	@ResponseBody
	public JsonResult gettitlesession() {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			String dczjid = (String)session.getAttribute("title_session_dczjid");
			jsonResult.addParam("dczjid", dczjid);
		}
		return jsonResult;
	}
	
	@RequestMapping("setwebidsession")
	@ResponseBody
	public JsonResult setwebidsession(String webid) {
		JsonResult jsonResult = JsonResult.getInstance();
		webid = StringUtil.getSafeString(webid);
		
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			session.setAttribute("dczj_session_webid", webid);
			jsonResult.setSuccess(true);
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("操作失败，请联系管理员！");
		}
		return jsonResult;
	}
	
	@RequestMapping("getwebidsession")
	@ResponseBody
	public JsonResult getwebidsession() {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			String webid = (String)session.getAttribute("dczj_session_webid");
			String webname = "";
			int webId = NumberUtil.getInt(webid);
			if(webId > 0) {
				DCZJ_WebSite website = webSiteService.findByIid(webId);
				if(website != null) {
					webname = website.getName();
				}
			}
			jsonResult.addParam("webid", webId);
			jsonResult.addParam("webname", webname);
		}
		return jsonResult;
	}
}
