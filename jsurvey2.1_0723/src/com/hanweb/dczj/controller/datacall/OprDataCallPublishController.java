package com.hanweb.dczj.controller.datacall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.DataCall;
import com.hanweb.dczj.entity.DataCallTemplate;
import com.hanweb.dczj.service.DataCallPublishService;
import com.hanweb.dczj.service.DataCallService;
import com.hanweb.dczj.service.DataCallTempService;

@Controller
@RequestMapping("manager/datacallpublish")
public class OprDataCallPublishController {

	@Autowired
	private DataCallService dataCallService;
	@Autowired
	private DataCallTempService dataCallTempService;
	@Autowired
	private DataCallPublishService dataCallPublishService;
	
	@RequestMapping("dopublish")
	@ResponseBody
	public JsonResult dopublish(String jactid){
		JsonResult jsonResult = JsonResult.getInstance();
		jactid = StringUtil.getSafeString(jactid);
		//发布的路径
		String loadPath = BaseInfo.getRealPath() + "/jsurvey/list/index_"+ jactid + ".html";
		String jacttemp = "";
		DataCallTemplate templateEn = dataCallTempService.findTemplate(jactid,"");
		DataCall jactEn = dataCallService.findJactByIid(NumberUtil.getInt(jactid));
		
		if(templateEn == null){
			String path = BaseInfo.getRealPath()+ "/resources/dczj/datacall/";
			if(StringUtil.equals("0", jactEn.getDatacall_type()+"")){  //固定长度的模板
				path += "datacall.html";  
			}else if(StringUtil.equals("1", jactEn.getDatacall_type()+"")){ //列表页的模板
				path += "datacalllist.html";
			}
			jacttemp = FileUtil.readFileToString(path);
		}else{
			jacttemp = templateEn.getTemplate();
		}
		// 组织静态HTML
		String content = dataCallPublishService.getJactPage(jacttemp,jactEn); 
		boolean bl = FileUtil.writeStringToFile(loadPath, content);
		if(bl){
			jsonResult.setSuccess(true);
			dataCallService.setUpdateHtml(NumberUtil.getInt(jactid), 1);
		}else{
			jsonResult.setSuccess(false);
			jsonResult.setMessage("操作失败，请联系管理员！");
		}
		return jsonResult;
	}
}
