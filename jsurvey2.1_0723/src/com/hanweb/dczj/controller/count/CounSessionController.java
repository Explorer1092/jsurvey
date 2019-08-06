package com.hanweb.dczj.controller.count;

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
@RequestMapping("manager/count")
public class CounSessionController { 
	
	@RequestMapping("setquessession")
	@ResponseBody
	public JsonResult setquessession(String url, String dczjid ,String selectid) {
		JsonResult jsonResult = JsonResult.getInstance();
		HttpSession session = SpringUtil.getRequest().getSession();
		if(session!=null){
			session.setAttribute("count_"+dczjid+"_session_url", url);
			session.setAttribute("count_"+dczjid+"_session_selectid", selectid);
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
		jsonResult.setSuccess(true);
		HttpSession session = SpringUtil.getRequest().getSession();
		String url = "";
		String selectid = "";
		if(session!=null){
			url = (String)session.getAttribute("count_"+dczjid+"_session_url");
			selectid = (String)session.getAttribute("count_"+dczjid+"_session_selectid");
			jsonResult.addParam("url", url);
			jsonResult.addParam("selectid", selectid);
		}
		return jsonResult;
	}
}
