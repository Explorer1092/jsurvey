package com.hanweb.dczj.controller.count;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CountService;

@Controller
@RequestMapping("manager/count")
public class OprEchartController {

	@Autowired
	AnswInfoService answInfoService;
	@Autowired
	CountService countService;
	
	@ResponseBody
	@RequestMapping("datasearch")
	public String dataSearch(String quesid ,String starttime, String endtime) {
		int count = 0;
		List<AnswInfo> answInfos = answInfoService.getAnswListByQuesId(NumberUtil.getInt(quesid));
		String data = "";
		if(answInfos != null && answInfos.size() > 0) {
			data = "{\"status\":0,\"data\":{\"datacount\":[";
			for(AnswInfo answInfo : answInfos) {
				count = countService.getCountResult(starttime, endtime, answInfo.getDczjid(), answInfo.getQuesid(), answInfo.getIid());
				if(answInfo.getAllowfillinair()==0) {
					//若不允许填空
					String answname = answInfo.getAnswname();
	
					if(answname.indexOf("<img") != -1) {
						answname = StringUtil.removeHTML(answname)+"[图片]";
					}
					data +="{\"name\":\""+StringUtil.removeHTML(answname).replace("&nbsp;"," ")+"\",\"count\":\""+count+"\"},";
				}else {
					//若允许填空
					data +="{\"name\":\"其他答案\",\"count\":\""+count+"\"},";
				}
			}
			if(StringUtil.isNotEmpty(data)) {
				data = data.substring(0,data.length()-1);
			}
			data = data +"]}}";
		}else {
			data = "{\"status\":1}";
		}
		return data;
	}
}
