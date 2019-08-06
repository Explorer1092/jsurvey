package com.hanweb.dczj.controller.count;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.PrizeSetting;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.DrawWinnersInfoService;
import com.hanweb.dczj.service.PrizeSettingService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.dczj.service.TotalRecoService;


@Controller
@RequestMapping("manager/count")
public class CountController {
	
	@Autowired
	SettingService settingService;
	@Autowired
	TitleInfoService titleInfoService;
	@Autowired
	QuesInfoService quesInfoService;
	@Autowired
	AnswInfoService answInfoService;
	@Autowired
	ContentRecoService contentRecoService;
	@Autowired
	TotalRecoService totalRecoService;
	@Autowired
	DrawWinnersInfoService drawWinnersInfoService;
	@Autowired
	PrizeSettingService prizeSettingService;
	@Autowired
	RadioRecoService radioRecoService;
	@Autowired
	CheckedBoxRecoService checkedBoxRecoService;

	@RequestMapping("page_show")
	public ModelAndView showPage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/countindex");
		
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@RequestMapping("report")
	public ModelAndView reportPage(String dczjid ,String starttime, String endtime) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/report");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//先更新统计信息
		radioRecoService.countByDczjid(dczjid);
		checkedBoxRecoService.countByDczjid(dczjid);
		
		if(StringUtil.isEmpty(starttime)) {
			Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
			if(setting.getIsstart()==1) {
				starttime = sdf.format(setting.getStarttime());
			}else {
				TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
				starttime = sdf.format(titleInfo.getCreatetime());
			}
		}
		if(StringUtil.isEmpty(endtime)) {
			endtime = sdf.format(new Date());
		}
		List<QuesInfo> quesInfos = quesInfoService.findQuesListForExcel(dczjid);
		int answCount = 0;
		for(QuesInfo quesInfo : quesInfos) {
			int accuracy = 0; //正确率
			//单行文本和多行文本
			if(quesInfo.getType() == 2 || quesInfo.getType() == 5) {
				answCount = contentRecoService.findContentSumByTime(quesInfo.getDczjid(), quesInfo.getIid(), starttime, endtime);
				quesInfo.setAnswcount(answCount);
			}
			if(quesInfo.getType() == 6) {
				String answid = answInfoService.findRightAnswIdByQuesid(quesInfo.getIid());
				if(StringUtil.isNotEmpty(answid)) {
					int countx = radioRecoService.radioAnsListCount(dczjid,quesInfo.getIid()+"", answid,starttime, endtime);
					int county = radioRecoService.getRecoConutByTime(quesInfo.getIid()+"", starttime, endtime);
					if(county > 0) {
						accuracy = countx*100/county;
					}
				}
				quesInfo.setAnswcount(accuracy);
			}
			if(quesInfo.getType() == 7) {
				String answids = answInfoService.findRightAnswIdByQuesid(quesInfo.getIid());
				if(StringUtil.isNotEmpty(answids)) {
					List<Integer> answList =  StringUtil.toIntegerList(answids);
					Integer answid = answList.get(0);
					int countx = checkedBoxRecoService.checkRightBoxAnswCount(answid+"",starttime, endtime);
					int county = checkedBoxRecoService.getCheckConutByTime(quesInfo.getIid()+"", starttime, endtime);
					if(county > 0) {
						accuracy = countx*100/county;
					}
				}
				
				quesInfo.setAnswcount(accuracy);
			}
		}
		modelAndView.addObject("quesInfos", quesInfos);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("starttime", starttime);
		modelAndView.addObject("endtime", endtime);
		return modelAndView;
	}
	
	@RequestMapping("showdraw")
	public ModelAndView showdraw(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/draw");
		Integer joinQuesNum = 0;//参与问卷人数
		Integer joinDrawNum = 0;//参与抽奖人数
		
		joinQuesNum = totalRecoService.getTotalSurveyNumByFormid(dczjid, "", "");
		joinDrawNum = drawWinnersInfoService.findDrawNumByDczjid(dczjid);
		List<PrizeSetting> prizeSettings = prizeSettingService.getSettingBydczjid(dczjid);
		
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("joinQuesNum", joinQuesNum);
		modelAndView.addObject("joinDrawNum", joinDrawNum);
		modelAndView.addObject("prizeSettings", prizeSettings);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("check_isprize")
	public JsonResult checkIsPrize(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		
		Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
		if(setting!=null && setting.getIsprize()==1) {
			result.setSuccess(true);
		}else {
			result.setSuccess(false).setMessage("您尚未开启抽奖");
		}
		
		return result;
	}
	
}
