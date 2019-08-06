package com.hanweb.dczj.controller.count;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;

@Controller
@RequestMapping("manager/count")
public class CrossAnalysisController {

	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private RadioRecoService radioRecoService;
	
	@RequestMapping("crossanalysis")
	public ModelAndView showCrossAnalysisPage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/crossAnalysis");
		
		List<QuesInfo> quesList = quesInfoService.findRadioQuesByDczjId(dczjid);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("quesList", quesList);
		return modelAndView;
	}
	
	@RequestMapping("oprcrossanalysis")
	@ResponseBody
	public JsonResult oprCrossAnalysis(String xquesid, String yquesid) {
		JsonResult jsonResult = JsonResult.getInstance();
		xquesid = StringUtil.getSafeString(xquesid);
		yquesid = StringUtil.getSafeString(yquesid);
		
		List<AnswInfo> xAnswInfoList = answInfoService.getAnswListByQuesId(NumberUtil.getInt(xquesid));
		List<AnswInfo> yAnswInfoList = answInfoService.getAnswListByQuesId(NumberUtil.getInt(yquesid));
		
		List<List<String>> rows = new ArrayList<List<String>>();
		List<String> headList = new ArrayList<String>();/* 表头 */
		List<String> valueList = null; /*数据列 */
		String legend_data = "";
		String yAxis_data = "";
		
		headList.add("");
		if(xAnswInfoList != null && xAnswInfoList.size() > 0) {
			for(AnswInfo answInfoEn : xAnswInfoList) {
				String answnames = answInfoEn.getAnswname();
				if(StringUtil.equals(answInfoEn.getIsright(), "1")) {
					answnames = "<span style = 'color: blue;'>" + answnames + "</span>";
				}
				headList.add(answnames);
				legend_data += "'"+answInfoEn.getAnswname() + "',";
			}
		}
		rows.add(headList);
		if(yAnswInfoList != null && yAnswInfoList.size() > 0) {
			for(AnswInfo yanswInfoEn : yAnswInfoList) {
				int yanswid = yanswInfoEn.getIid();  //y轴
				valueList = new ArrayList<String>();
				String answnames = yanswInfoEn.getAnswname();
				if(StringUtil.equals(yanswInfoEn.getIsright(), "1")) {
					answnames = "<span style = 'color: blue;'>" + answnames + "</span>";
				}
				valueList.add(answnames);
				yAxis_data += "'" + yanswInfoEn.getAnswname() + "',";
				if(xAnswInfoList != null && xAnswInfoList.size() > 0) {
					for(AnswInfo xanswInfoEn : xAnswInfoList) {
						int xanswid = xanswInfoEn.getIid(); //x轴
						int count = radioRecoService.findCountByXAndY(xanswid,yanswid);
						valueList.add(StringUtil.getString(count));
					}
				}
				rows.add(valueList);
			}
		}
		
		String html = "<table class=\"table\" style=\"width: 100%;border-top: 1px solid #ccc\">";
		if(rows != null && rows.size() > 0) {
			for(List<String> strList : rows) {
				html += "<tr style=\"border-bottom: 1px solid #ccc\">";
				if(strList != null && strList.size() > 0) {
					for(String values : strList) {
						html +="<td>"+values+"</td>";
					}
				}
				html +="</tr>";
			}
		}
		html += "</table>";
		
		String seriesData = "";
		if(xAnswInfoList != null && xAnswInfoList.size() > 0) {
			for(AnswInfo xanswInfoEn : xAnswInfoList) {
				int xanswid = xanswInfoEn.getIid();  //x轴
				String seriesDatas ="";
				seriesData += " { name: '"+xanswInfoEn.getAnswname()+"',type: 'bar', barMaxWidth: 30,stack: '总量',label: {normal: { show: false,position: 'insideRight' }},data: [";
				if(yAnswInfoList != null && yAnswInfoList.size() > 0) {
					for(AnswInfo yanswInfoEn : yAnswInfoList) {
						int yanswid = yanswInfoEn.getIid(); //x轴
						int count = radioRecoService.findCountByXAndY(xanswid,yanswid);
						seriesDatas += count+",";
					}
				}
				if(StringUtil.isNotEmpty(seriesDatas)) {
					seriesDatas = seriesDatas.substring(0, seriesDatas.length()-1);
				}
				seriesData += seriesDatas + "]},";
			}
		}
		if(StringUtil.isNotEmpty(seriesData)) {
			seriesData = "[" + seriesData.substring(0, seriesData.length()-1) + "]";
		}
		if(StringUtil.isNotEmpty(legend_data)) {
			legend_data = "[" + legend_data.substring(0, legend_data.length()-1) + "]";
		}
		if(StringUtil.isNotEmpty(yAxis_data)) {
			yAxis_data = "[" + yAxis_data.substring(0, yAxis_data.length()-1) + "]";
		}
		jsonResult.setSuccess(true);
		jsonResult.addParam("html", html);
		jsonResult.addParam("legend_data", legend_data);
		jsonResult.addParam("yAxis_data", yAxis_data);
		jsonResult.addParam("seriesData", seriesData);
		return jsonResult;
	}
}
