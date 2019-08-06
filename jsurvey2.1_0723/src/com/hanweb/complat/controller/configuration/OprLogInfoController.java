package com.hanweb.complat.controller.configuration;

import java.util.Map;

import org.apache.log4j.Level;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.LogUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;

/**
 * 详细日志操作类
 * 
 * @author 李杰
 * 
 */
@Controller
@RequestMapping("manager/configuration/log")
public class OprLogInfoController {

	@RequestMapping("info")
	public ModelAndView info(String filter) {
		String[] filterArray = null;
		if(StringUtil.isNotEmpty(filter)){
			filterArray = StringUtil.split(filter, ",");
		}else{
			filterArray = new String[]{"com.hanweb", "jdbc.sqlonly", "jdbc.audit"};
		}
		Map<String, Level> levelTree = LogUtil
				.getAllLog(filterArray);
		ModelAndView modelAndView = new ModelAndView("/complat/configuration/log_opr");
		modelAndView.addObject("level_url", "change_level.do");
		modelAndView.addObject("logdetail_url", "change_logdetail.do");
		modelAndView.addObject("filter", StringUtil.join(filterArray, ","));
		modelAndView.addObject("levelTree", levelTree);
		modelAndView.addObject("logdetail", BaseInfo.isDetailErrorLog());
		return modelAndView;
	}

	@RequestMapping("change_level")
	@ResponseBody
	public JsonResult changeLevel(String loggerName, String level) {
		JsonResult jsonResult = JsonResult.getInstance();
		LogUtil.setLogLevel(loggerName, level);
		jsonResult.setSuccess(true);
		return jsonResult;
	}

	@RequestMapping("change_logdetail")
	@ResponseBody
	public JsonResult changeLogDetail(boolean logdetail) {
		System.out.println(logdetail);
		JsonResult jsonResult = JsonResult.getInstance();
		BaseInfo.setDetailErrorLog(logdetail);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
}
