package com.hanweb.dczj.controller.datacall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.dczj.entity.DataCall;
import com.hanweb.dczj.entity.DataCallTemplate;
import com.hanweb.dczj.service.DataCallService;
import com.hanweb.dczj.service.DataCallTempService;

@Controller
@RequestMapping("manager/datacalltempstyle")
public class OprDataCallTemplateController {

	@Autowired
	private DataCallTempService dataCallTempService;
	@Autowired
	private DataCallService dataCallService;
	
	@RequestMapping("showtempstyle")
	public ModelAndView showJactTemplate(String jactid,String type){
		jactid = StringUtil.getSafeString(jactid);
		type = StringUtil.getSafeString(type);
		
		ModelAndView modelAndView = new ModelAndView("/dczj/datacall/datacalltemplate_opr");
		DataCallTemplate templateEn = dataCallTempService.findTemplate(jactid,type);
		DataCall jactEn = dataCallService.findJactByIid(NumberUtil.getInt(jactid));
		if(templateEn == null){
			templateEn = new DataCallTemplate();
			String path = BaseInfo.getRealPath()+ "/resources/dczj/datacall/";
			if(StringUtil.equals("0", type)){
				path += "datacall.html";
			}else if(StringUtil.equals("1", type)){
				path += "datacalllist.html";
			}
			String jacttemp = FileUtil.readFileToString(path);
			templateEn.setDatacallid(NumberUtil.getInt(jactid));
			templateEn.setType(NumberUtil.getInt(type));
			templateEn.setTemplate(jacttemp);
		}
		
		modelAndView.addObject("en", templateEn);
		modelAndView.addObject("webId", jactEn.getWebid());
		modelAndView.addObject("url", "showtempstyle_submit.do");
		return modelAndView;
	}
	
	@RequestMapping("showtempstyle_submit")
	@ResponseBody
	public JsonResult submitAdd(DataCallTemplate en) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean bl = false;
		DataCallTemplate templateEn = dataCallTempService.findTemplate(en.getDatacallid()+"","");
		if(templateEn != null ){
			en.setIid(templateEn.getIid());
			bl = dataCallTempService.modify(en);
		}else{
			bl = dataCallTempService.add(en);
		}
		if(bl){
            bl = dataCallService.setUpdateHtml(en.getDatacallid(),0);
		}
		return bl ? jsonResult.set(ResultState.MODIFY_SUCCESS) : jsonResult.set(ResultState.MODIFY_FAIL);
	}
}
