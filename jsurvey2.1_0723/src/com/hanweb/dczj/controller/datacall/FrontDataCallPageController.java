package com.hanweb.dczj.controller.datacall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.service.DataCallPublishService;

@Controller
@RequestMapping("front/datacallpage")
public class FrontDataCallPageController {

	@Autowired
	private DataCallPublishService dataCallPublishService;
	
	@RequestMapping("gopageanswer")
	@ResponseBody
	public JsonResult goAnswerPage(String jactid,int pageno,int pageNum) {
		JsonResult jsonResult = JsonResult.getInstance();
		jactid = StringUtil.getSafeString(jactid);
		String pageHtml = dataCallPublishService.getPageHtml(jactid, pageno,pageNum);
		jsonResult.setMessage(pageHtml);
		return jsonResult;
	}
}
