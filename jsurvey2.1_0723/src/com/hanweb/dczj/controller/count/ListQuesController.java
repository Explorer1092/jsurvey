package com.hanweb.dczj.controller.count;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.CountService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;

@Controller
@RequestMapping("manager/count")
public class ListQuesController {
	
	@Autowired
	RadioRecoService radioRecoService;
	@Autowired
	CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	AnswInfoService answInfoService;
	@Autowired
	CountService countService;
	@Autowired
	QuesInfoService quesInfoService;

	@ResponseBody
	@RequestMapping(value = "quescount")
	public String quescount(String quesid,String starttime,String endtime) {
		int count = 0;
		int sum = 0;
		String data = "";
		double percent = 0.0;
		QuesInfo quesInfo = quesInfoService.findQuesEntityByIid(NumberUtil.getInt(quesid));
		List<AnswInfo> answInfos = answInfoService.getAnswListByQuesId(NumberUtil.getInt(quesid));
		if(answInfos != null && answInfos.size() > 0) {
			for(AnswInfo answInfo : answInfos) {
				count = countService.getCountResult(starttime, endtime, answInfo.getDczjid(), answInfo.getQuesid(), answInfo.getIid());
				answInfo.setCount(count);
				sum += count;
			}
			BigDecimal sumdecimal = new BigDecimal(sum);
			BigDecimal countdecimal = null;
			for(AnswInfo answInfo : answInfos) {
				String answname  = "";
				if(answInfo.getCount()==0) {
					percent = 0.0;
				}else {
					countdecimal = new BigDecimal(answInfo.getCount());
					percent = countdecimal.multiply(new BigDecimal(100)).divide(sumdecimal, 2, RoundingMode.HALF_UP).doubleValue();
				}
				if(StringUtil.equals(answInfo.getIsright(), "1")) {
					answname = answInfo.getAnswname();
					if(answname.indexOf("<img") != -1) {
						answname = StringUtil.removeHTML(answname)+"[图片]";
					}
					answname = StringUtil.removeHTML(answname).replace("&nbsp;"," ");
					answname = answname + "<span style = 'color:blue;font-size:12px;'>（正确答案）</span>";
				}else {
					answname = answInfo.getAnswname();
					if(answname.indexOf("<img") != -1) {
						answname = StringUtil.removeHTML(answname)+"[图片]";
					}
					answname = StringUtil.removeHTML(answname).replace("&nbsp;"," ");
				}
			
				if(answInfo.getAllowfillinair()==0) {
					//若不允许填空
					data +="{\"option\":\""+answname+"\",\"count\":\""+answInfo.getCount()+"\",\"percent\":\""
							+percent+"%\"},";
				}else {
					//若允许填空
					String href = "<a onclick=\\\"otheranswer("+answInfo.getIid()+","+quesInfo.getType()+","+quesInfo.getIid()+");\\\" "
							+ "style=\\\"color: #01AAED;cursor: pointer;\\\">"+answname+"</a>";
					data +="{\"option\":\""+href+"\",\"count\":\""+answInfo.getCount()+"\",\"percent\":\""
							+percent+"%\"},";
				}
				
			}
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		String json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		return json;
	}
}
