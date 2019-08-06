package com.hanweb.dczj.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.service.VisitCountService;

/**
 * 获取访问量
 * @author admin
 *
 */
@Controller
@RequestMapping("front/jsurvey")
public class FrontVisitCountController {

	@Autowired
	private VisitCountService visitCountService;
	
	@RequestMapping("visitCount")
	@ResponseBody
	public JsonResult jsurveyVisitCount(String jsurveyid) {
		JsonResult jsonResult = JsonResult.getInstance();
		Cache cache = CacheManager.getInstance("jsurvey_visitcount");
		String vc = cache.get("jsurveyvc_"+jsurveyid, new Type<String>(){});
		int visitcount = 0;
		if(StringUtil.isNotEmpty(vc)) {
			visitcount = NumberUtil.getInt(vc);
		}else {
			visitcount = visitCountService.findVistCount(jsurveyid);
		}
		visitcount ++;
		cache.put("jsurveyvc_"+jsurveyid, StringUtil.getString(visitcount));
		jsonResult.setSuccess(true);
		return jsonResult;
	}
}
