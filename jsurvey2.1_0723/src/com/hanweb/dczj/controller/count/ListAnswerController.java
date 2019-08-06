package com.hanweb.dczj.controller.count;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.RadioRecoService;

@Controller
@RequestMapping("manager/count")
public class ListAnswerController {
	
	@Autowired
	RadioRecoService radioRecoService;
	@Autowired
	CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	ContentRecoService contentRecoService;

	@RequestMapping(value = "answlist")
	public ModelAndView listAnswer(String dczjid,String answid,String type,String quesid,String starttime,String endtime) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/answerlist");
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("answid", answid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("quesid", quesid);
		modelAndView.addObject("starttime", starttime);
		modelAndView.addObject("endtime", endtime);
		return modelAndView;
	}
	
	@RequestMapping(value = "examineanswlist")
	public ModelAndView listExamineAnswer(String dczjid,String answid,String type,String quesid,String starttime,String endtime) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/examineanswlist");
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("answid", answid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("quesid", quesid);
		modelAndView.addObject("starttime", starttime);
		modelAndView.addObject("endtime", endtime);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "examineanswertable")
	public String examineAnswerTable(String page,String limit,String dczjid,String answid,String quesid,String starttime,String endtime) {
		String data = "";
		String audistate = "";
		String replystate = "";
		int count = contentRecoService.findEntityCountByTime(dczjid, quesid, answid, starttime, endtime);
		List<ContentReco> contentRecos = contentRecoService.findEntityListByTime(page, limit,dczjid, quesid, answid, starttime, endtime);
		for(ContentReco contentReco : contentRecos) {
			String answcontent = "";		
			if(StringUtil.equals("0", contentReco.getAudi())) {
				audistate = "未审核";
			}else if(StringUtil.equals("1", contentReco.getAudi())) {
				audistate = "已审核";
			}else if(StringUtil.equals("2", contentReco.getAudi())) {
				audistate = "已公开";
			}else {
				audistate = "未通过";
			}
			if(StringUtil.isNotEmpty(contentReco.getReplycontent())) {
				replystate = "已回复";
			}else {
				replystate = "未回复";
			}
			answcontent = "<a style=\\\"cursor:pointer;\\\" onclick=\\\"openExaminePage('"+contentReco.getIid()+"')\\\">"+StringUtil.getSafeString(contentReco.getAnswcontent())+"</a>";
			data +="{\"contentid\":\""+contentReco.getIid()+"\",\"answcontent\":\""+answcontent+"\",\"audistate\":\""+audistate+"\",\"replystate\":\""+replystate+"\"},";
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		String json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		return json;
	}
	
}
