package com.hanweb.dczj.controller.count;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.entity.TotalReco;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.dczj.service.TotalRecoService;

@Controller
@RequestMapping("manager/count")
public class ListJoinDetailController {

	@Autowired
	private TotalRecoService totalRecoService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private TitleInfoService titleInfoService;
	
	@RequestMapping("detaillist")
	public ModelAndView showPage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/detailList");
		int maxscore=totalRecoService.findMaxScore(dczjid);
		int minscore=totalRecoService.findMinScore(dczjid);
		int avgscore=totalRecoService.findAvgScore(dczjid);
		int sumscore=quesInfoService.findSumScore(dczjid);
		TitleInfo titleInfo= titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		modelAndView.addObject("type", titleInfo.getType());
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("starttime", "");
		modelAndView.addObject("endtime", "");
		modelAndView.addObject("maxscore", maxscore);
		modelAndView.addObject("minscore", minscore);
		modelAndView.addObject("avgscore", avgscore);
		modelAndView.addObject("sumscore", sumscore);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("list")
	public String showList(String page,String limit,String dczjid,String starttime,String endtime,String check,String sorttype) {
		String data = "";
		//String delete = "";
		String type = "";
		String uuid = "";
		float earnscore = 0;
		
		List<TotalReco> totalRecos = totalRecoService.findSubmitRecoListsByFormid(dczjid, NumberUtil.getInt(page), NumberUtil.getInt(limit), starttime, endtime,check,sorttype);
		int count = totalRecoService.getTotalSurveyNumByFormid(dczjid, starttime, endtime);
		if(totalRecos!=null && totalRecos.size()>0) {
			for(TotalReco totalReco : totalRecos) {
				//delete = "<img style=\\\"width:25px;cursor: pointer;\\\" src=\\\"../../resources/dczj/images/u5492.png\\\" onclick='remove("+totalReco.getIid()+");' />";
				uuid = "<a style=\\\"cursor: pointer;\\\" onclick=\\\"showdetail('"+totalReco.getIid()+"')\\\">"+totalReco.getUnid()+"</a>";
				if(totalReco.getType()==1) {
					type = "pc端";
				}else if(totalReco.getType()==2) {
					type = "手机端";
				}else {
					type = "微信";
				}
				if(totalReco.getSumscore()!=null) {
				  earnscore=totalReco.getSumscore();	
				}else {
				  earnscore = 5 ;
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String createDate = sdf.format(totalReco.getCreatedate());
				data +="{\"iid\":\""+totalReco.getIid()+"\",\"unid\":\""+uuid+"\","
						+ "\"ip\":\""+totalReco.getIp()+"\",\"type\":\""+type+"\",\"createdate\":\""+createDate+"\",\"earnscore\":\""+earnscore+"\"},";
			}
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		String json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		return json;
	}
	
}
