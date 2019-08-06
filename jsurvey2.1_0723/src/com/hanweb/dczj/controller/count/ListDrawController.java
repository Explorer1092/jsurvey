package com.hanweb.dczj.controller.count;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.dczj.entity.Draw_WinnersInfo;
import com.hanweb.dczj.entity.LayuiTableEntity;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.DrawWinnersInfoService;
import com.hanweb.dczj.service.QuesInfoService;

@Controller
@RequestMapping("manager/count")
public class ListDrawController {
	
	@Autowired
	DrawWinnersInfoService drawWinnersInfoService;
	@Autowired
	ContentRecoService contentRecoService;
	@Autowired
	QuesInfoService quesInfoService;
	
	@ResponseBody
	@RequestMapping(value = "drawtable")
	public String examineAnswerTable(String page,String limit,String dczjid,String prizename) {
		String data = "";
		String wintime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtil.equals("全部", prizename)) {
			prizename = "";
		}
		int count = drawWinnersInfoService.findDrawNumByDczjidAndPrizename(dczjid, prizename);
		List<Draw_WinnersInfo> winnersInfos = drawWinnersInfoService.findDrawsByDczjidAndPrizename(page, limit, dczjid, prizename);
		//获取单行文本题
		List<QuesInfo> quesList = quesInfoService.findQuesListByDczjidAndType(dczjid,"5");
		for(Draw_WinnersInfo winnersInfo : winnersInfos) {
			wintime = sdf.format(winnersInfo.getWintime());
			data +="{";
			for(QuesInfo quesInfo : quesList) {
				String answcontent = contentRecoService.getValueByDczjidAndQuesid(dczjid, quesInfo.getIid()+"", winnersInfo.getLoginname());
				data += "\""+quesInfo.getIid()+"\":\""+answcontent+"\",";
			}
			data +="\"prizename\":\""+winnersInfo.getPrizename()
			+"\",\"wintime\":\""+wintime+"\"},";
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		String json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		return json;
	}
	
	/**
	 * 获取表头
	 * @param dczjid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tablehead")
	public JsonResult getTableHead(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		try {
			List<QuesInfo> quesList = quesInfoService.findQuesListByDczjidAndType(dczjid,"5");
			List<LayuiTableEntity> list = new ArrayList<LayuiTableEntity>();
//			String col = "[[";
			for(QuesInfo quesInfo : quesList) {
				LayuiTableEntity entity = new LayuiTableEntity();
				entity.setField(quesInfo.getIid()+"");
				entity.setTitle(quesInfo.getQuesname());
				entity.setAlign("center");
				entity.setStyle("background-color:#ffffff;");
				list.add(entity);
//				col += "{field: '"+quesInfo.getIid()+"', title: '"+quesInfo.getQuesname()+"', width:'37.2%', align: 'center',style:\"background-color:#ffffff;\"},";
			} 
			list.add(new LayuiTableEntity("prizename", "中奖情况", "center", "background-color:#ffffff;"));
			list.add(new LayuiTableEntity("wintime", "中奖时间", "center", "background-color:#ffffff;"));
//			col += "{field: 'prizename', title: '中奖情况', width:'33%', align: 'center',style:\"background-color:#ffffff;\"}" + 
//					",{field: 'wintime', title: '中奖时间', width:'30%', align: 'center',style:\"background-color:#ffffff;\"}]]";
			result.setSuccess(true);
			List<List<LayuiTableEntity>> listAll = new ArrayList<List<LayuiTableEntity>>();
			listAll.add(list);
			result.addParam("col", listAll);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		result.set(ResultState.ADD_SUCCESS);
		return result;
	}
	
}
