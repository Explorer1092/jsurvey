package com.hanweb.dczj.controller.ques;

import java.io.File;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.StyleService;
import com.hanweb.dczj.service.TemplateService;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("manager/dczj")
public class QuesEditController {

	@Autowired
	private StyleService styleService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private DisplayConfigService displayConfigService;
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private TemplateService templateService;
	
	/**
	 * 进入问卷编辑的title页面
	 * @param dczjid
	 * @return
	 */
	@RequestMapping("quesindex")
	public ModelAndView showQuesIndex(Integer dczjid) {
		ModelAndView modelAndView = new ModelAndView("/dczj/ques/ques_index");
		Template template = templateService.findTemplatByDczjidAndTypeAndPagetype(StringUtil.getString(dczjid), 0);
		int istemplate = 0;
		if(template !=null) {
			istemplate = 1;
		}
		TitleInfo titleInfo= titleInfoService.getEntity(dczjid);
		modelAndView.addObject("istemplate", istemplate);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("type", titleInfo.getType());
		return modelAndView;
	}
	
	/**
	 * 组织编辑问卷页面
	 * @param dczjid
	 * @return
	 */
	@RequestMapping("quesedit")
	public ModelAndView showQuesEdit(Integer dczjid) {
     		ModelAndView modelAndView = new ModelAndView("/dczj/ques/ques_edit");
		
		//获取显示设置
		DisplayConfig configEn = displayConfigService.findEntityByDczjid(dczjid);
		if(configEn == null) {
			configEn = displayConfigService.addNewConfig(dczjid);
		}
		
		String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
		String styleCode = "";
		String content = "";
		Dczj_Setting settingEn = settingService.getEntityBydczjid(dczjid+"");
		
		styleCode = styleService.getStyleCodeByPath(quespage);
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		confMap.put("pageType", "0");
		confMap.put("dczjid", dczjid + "");
		content = styleService.parseDefaultQues(styleCode);
		content = styleService.parseQuesFront(dczjid + "",confMap, settingEn, content);
		
		String cssstyle = "";
		String stylepath = BaseInfo.getRealPath()+"/resources/dczj/template/jsurvey.css";
		File file = new File(stylepath);
		cssstyle = FileUtil.readFileToString(file);
		if(StringUtil.isNotEmpty(cssstyle)) {
			cssstyle = "<style type=\"text/css\">"+cssstyle+"</style >";
		}
		Integer type = (titleInfoService.getEntity(dczjid)).getType();
		modelAndView.addObject("content", content);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("configEn", configEn);
		modelAndView.addObject("defaultcssstyle", cssstyle);
		modelAndView.addObject("type", type);
		modelAndView.addObject("url", "displaySubmit.do");
		return modelAndView;
	}
	
	
	@RequestMapping("displaySubmit")
	@ResponseBody
	public JsonResult displaySubmit(DisplayConfig configEn) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(false);
		boolean bl =  displayConfigService.update(configEn);
		if(bl) {
			titleInfoService.setUpdateHtml(configEn.getDczjid()+"", 0);
			Cache cache = CacheManager.getInstance("jsurvey");
			cache.put("display_config_"+configEn.getDczjid(), configEn);
			jsonResult.setSuccess(true);
		}
		return jsonResult.setSuccess(true);
	}
	
	/**
	 * 重新加载页面
	 * @param formId
	 * @param quesId
	 * @return
	 */
	@RequestMapping("updatequesdiv")
	@ResponseBody
	public JsonResult updateQuesDiv(int dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
		String styleCode = "";
		String content = "";
		Dczj_Setting settingEn = settingService.getEntityBydczjid(dczjid+"");
		
		styleCode = styleService.getStyleCodeByPath(quespage);
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		confMap.put("pageType", "0");
		confMap.put("dczjid", dczjid + "");
		content = styleService.parseDefaultQues(styleCode);
		content = styleService.parseQuesFront(dczjid + "",confMap, settingEn, content);
		jsonResult.setMessage(content);
		return jsonResult.setSuccess(true);
	}
}
