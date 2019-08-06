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
	Cache cache = CacheManager.getInstance("jsurvey_visitcount");
	
	@RequestMapping("visitCount")
	@ResponseBody
	public synchronized JsonResult jsurveyVisitCount(String jsurveyid) {

		JsonResult jsonResult = JsonResult.getInstance();
		int visitcount = 0;
		/*System.out.println("jsurveyid:"+jsurveyid);
		long jsurveyvc = cache.incr("jsurveyvc_"+jsurveyid);
		System.out.println("jsurveyvc:"+jsurveyvc);
		if(jsurveyvc == 1) {
			visitcount = visitCountService.findVistCount(jsurveyid);
			jsurveyvc = cache.incr("jsurveyvc_"+jsurveyid,visitcount);
		}*/
		
		String vc = cache.get("jsurveyvc_"+jsurveyid, new Type<String>(){});
		
		if(StringUtil.isNotEmpty(vc) && vc!=null) {
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
