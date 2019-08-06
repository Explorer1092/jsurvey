package com.hanweb.dczj.controller.count;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.dczj.service.TotalRecoService;
import com.hanweb.dczj.service.VisitCountService;

@Controller
@RequestMapping("manager/count")
public class ListJoinGeneralizationController {
    
	@Autowired
	private VisitCountService visitCountService;
	@Autowired
	private TotalRecoService totalRecoService;
	@Autowired
	private TitleInfoService titleInfoService;
	
	
	@RequestMapping("generalizationlist")
	public ModelAndView showPage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/generalizationlist");
		Cache cache = CacheManager.getInstance("jsurvey_visitcount");
		String vc = cache.get("jsurveyvc_"+dczjid, new Type<String>(){});
		int visitcount = 0;
		if(StringUtil.isNotEmpty(vc)) {
			visitcount = NumberUtil.getInt(vc);
		}else {
			visitcount = visitCountService.findVistCount(dczjid);
		} //获取访问量
		int totalCount = totalRecoService.findRecoIsHaveSubmitByDczjid(dczjid); //问卷回收量
		int percentage = 0;
		if(visitcount != 0 ) {
			percentage = totalCount*100/visitcount; //百分比
		}
		
		int pccount = totalRecoService.findResourceCount(dczjid,1);
		int phonecount = totalRecoService.findResourceCount(dczjid,2);
		
		String[][] ipAddressCount = totalRecoService.findAddressCount(dczjid);
		String[][] ipCount = totalRecoService.findIpCount(dczjid);
		
		TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		if(titleInfo.getType() == 3) {
			int grade0 = totalRecoService.findNumByGrade(0,60,dczjid);
			int grade1 = totalRecoService.findNumByGrade(60,75,dczjid);
			int grade2 = totalRecoService.findNumByGrade(75,85,dczjid);
			int grade3 = totalRecoService.findNumByGrade(85,1000000,dczjid);
			modelAndView.addObject("grade0", grade0);
			modelAndView.addObject("grade1", grade1);
			modelAndView.addObject("grade2", grade2);
			modelAndView.addObject("grade3", grade3);
		}
		modelAndView.addObject("titleInfo", titleInfo);
		modelAndView.addObject("ipAddressCount", ipAddressCount);
		modelAndView.addObject("ipCount", ipCount);
		modelAndView.addObject("pccount", pccount);
		modelAndView.addObject("phonecount", phonecount);
		modelAndView.addObject("vc", visitcount);
		modelAndView.addObject("totalCount", totalCount);
		modelAndView.addObject("percentage", percentage);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("starttime", "");
		modelAndView.addObject("endtime", "");
		return modelAndView;
	}
	
	@RequestMapping("totalCountEcharts")
	@ResponseBody
	public JsonResult totalCountEcharts(String dczjid,String date) {
		JsonResult jsonResult = JsonResult.getInstance();
		if(StringUtil.isEmpty(date)) {
			date = DateUtil.getCurrDateTime();
		}
		Date datetime = DateUtil.stringtoDate(date,"yyyy-MM-dd");
		String timedata = "";
		String countdata = "";
		for(int i=6;i>=0;i--) {
			Date newtime = DateUtil.nextDay(datetime, -i);
			String timestr = DateUtil.dateToString(newtime, "MM-dd");
			timedata += "'"+timestr + "',";
			String starttime = DateUtil.dateToString(newtime, "yyyy-MM-dd") +" 00:00:00";
			String endtime = DateUtil.dateToString(newtime, "yyyy-MM-dd") +" 23:59:59";
			int count = totalRecoService.getTotalSurveyNumByFormid(dczjid, starttime, endtime);
			countdata += "'"+count + "',";
		}
		if(StringUtil.isNotEmpty(timedata)) {
			timedata = timedata.substring(0,timedata.length()-1);
		}
		if(StringUtil.isNotEmpty(countdata)) {
			countdata = countdata.substring(0,countdata.length()-1);
		}
		jsonResult.addParam("timedata", timedata);
		jsonResult.addParam("countdata", countdata);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	@RequestMapping("testCountEcharts")
	@ResponseBody
	public JsonResult testCountEcharts(String dczjid,String gradescore0,String gradescore1,String gradescore2,String gradescore3) {
		JsonResult jsonResult = JsonResult.getInstance();
		int grade0 = totalRecoService.findNumByGrade(NumberUtil.getInt(gradescore0),NumberUtil.getInt(gradescore1),dczjid);
		int grade1 = totalRecoService.findNumByGrade(NumberUtil.getInt(gradescore1),NumberUtil.getInt(gradescore2),dczjid);
		int grade2 = totalRecoService.findNumByGrade(NumberUtil.getInt(gradescore2),NumberUtil.getInt(gradescore3),dczjid);
		int grade3 = totalRecoService.findNumByGrade(NumberUtil.getInt(gradescore3),1000000,dczjid);
		jsonResult.addParam("grade0", grade0);
		jsonResult.addParam("grade1", grade1);
		jsonResult.addParam("grade2", grade2);
		jsonResult.addParam("grade3", grade3);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
}
