package com.hanweb.dczj.controller.title;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.VerifyCode;
import com.hanweb.common.util.json.Type;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.ThanksSetting;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.ThanksSettingService;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("front/dczj")
public class FrontTitlePublish {
	
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
    @Autowired
    private TitleInfoService titleInfoService;
    @Autowired
    private ThanksSettingService thanksSettingService;

	@RequestMapping("que_code")
	@ResponseBody
	public String showSurveyCode(String sessionid,HttpServletResponse response) {
		if(sessionid==null)
			sessionid = "";
		return VerifyCode.generate(response,"sessionid" );
	}
	
	/**
	 * 传json数据
	 * @param formid
	 * @return
	 */
	@RequestMapping("findQuesAndAnswJson")
	@ResponseBody
	public JsonResult findQuesAndAnswJson(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		String hiddenQuesJson = "";
		Cache cache = CacheManager.getInstance("jsurvey_front");
		if(cache.get("surveyHiddenQuesJson_"+dczjid,new Type<String>() {}) != null){
			hiddenQuesJson = cache.get("surveyHiddenQuesJson_"+dczjid,new Type<String>() {});
			jsonResult.setSuccess(true);
			jsonResult.addParam("hiddenQuesJson", hiddenQuesJson);
		}else{
			List<QuesInfo> quesList = quesInfoService.findDQuesByDczjId(dczjid);
			if(quesList != null && quesList.size()>0){
				for(QuesInfo quesEn : quesList ){
					int dAnswId = NumberUtil.getInt(quesEn.getRelyanswid());
					if(dAnswId > 0){
						AnswInfo answEn = answInfoService.getEntity(dAnswId);
						QuesInfo dquesEn = quesInfoService.findQuesEntityByIid(answEn.getQuesid());
						hiddenQuesJson += "{\"hiddenQuesId\":\""+quesEn.getIid()+"\",\"type\":\""+dquesEn.getType()+"\",\"dQuesId\":\""+answEn.getQuesid()+"\",\"dAnswID\":\""+answEn.getIid()+"\",\"mustfill\":\""+quesEn.getIsmustfill()+"\"},";
					}
				}
			}
			if(StringUtil.isNotEmpty(hiddenQuesJson)){
				hiddenQuesJson = hiddenQuesJson.substring(0, hiddenQuesJson.length()-1);
				hiddenQuesJson = "["+hiddenQuesJson+"]";
				cache.put("surveyHiddenQuesJson_"+dczjid, hiddenQuesJson);
				jsonResult.setSuccess(true);
				jsonResult.addParam("hiddenQuesJson", hiddenQuesJson);
			}
		}
		return jsonResult;
	}
	
	/**
	 * 查询分页总页数
	 * @param formid
	 * @return
	 */
	@RequestMapping("findPageNum")
	@ResponseBody
	public JsonResult findPageNum(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		Cache cache = CacheManager.getInstance("jsurvey_front");
		int pageNum = 0;
		if(cache.get("pageNum_"+dczjid,new Type<Integer>() {}) != null){
			pageNum = cache.get("pageNum_"+dczjid,new Type<Integer>() {});
			if(pageNum > 0){
				jsonResult.setSuccess(true);
				jsonResult.addParam("pageNum", pageNum);
			}
		}else{
			pageNum = quesInfoService.findpageNumCount(dczjid);
			if(pageNum > 0){
				jsonResult.setSuccess(true);
				jsonResult.addParam("pageNum", pageNum);
			}
		}
		return jsonResult;
	}
	
	/**
	 * 提交验证传参
	 * @param formid
	 * @return
	 */
	@RequestMapping("findValidateQues")
	@ResponseBody
	public JsonResult findValidateQues(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		String validateQuesJson = "";
		Cache cache = CacheManager.getInstance("jsurvey_front");
		if(cache.get("validateQuesJson_"+dczjid,new Type<String>() {}) != null){
			validateQuesJson = cache.get("validateQuesJson_"+dczjid,new Type<String>() {});
			jsonResult.setSuccess(true);
			jsonResult.addParam("validateQuesJson", validateQuesJson);
		}else{
			List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
			if(quesList != null && quesList.size()>0){
				for(QuesInfo textQuesEn : quesList){
					validateQuesJson += "{\"quesId\":\""+textQuesEn.getIid()+"\",\"mustfill\":\""+textQuesEn.getIsmustfill()+"\",\"min\":\""+textQuesEn.getMinselect()+"\",\"max\":\""+textQuesEn.getMaxselect()+"\",\"type\":\""+textQuesEn.getType()+"\",\"validaterules\":\""+textQuesEn.getValidaterules()+"\"},";
				}
			}
			if(StringUtil.isNotEmpty(validateQuesJson)){
				validateQuesJson = "[" + validateQuesJson.substring(0,validateQuesJson.length()-1) + "]";
				jsonResult.setSuccess(true);
				jsonResult.addParam("validateQuesJson", validateQuesJson);
			}
		}
		return jsonResult;
	}
	
	/**
	 * 提交验证传参
	 * @param formid
	 * @return
	 */
	@RequestMapping("findRightSocre")
	@ResponseBody
	public JsonResult findRightSocre(String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		TitleInfo infoEn = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		if(infoEn.getType() != 3) {
			return jsonResult.setSuccess(false);
		}
		ThanksSetting thanksSetting = thanksSettingService.getSettingBydczjid(dczjid);
		Cache cache = CacheManager.getInstance("dczj_score");
		String json = "";
		int allScore = 0;
		if(StringUtil.isNotEmpty((String) cache.get("dczj_score_"+dczjid, new Type<String>() {})) && StringUtil.isNotEmpty(StringUtil.getString(cache.get("dczj_scorenum_"+dczjid, new Type<Integer>() {})))) {
			json = cache.get("dczj_score_"+dczjid, new Type<String>() {});
			allScore = cache.get("dczj_scorenum_"+dczjid, new Type<Integer>() {});
			jsonResult.setSuccess(true);
		}else {
			List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
			if(quesList != null && quesList.size() > 0) {
				for(QuesInfo quesEn : quesList) {
					if(quesEn != null && (quesEn.getType() == 6 || quesEn.getType() == 7)) {
						String rightansw = "";
						allScore += NumberUtil.getInt(quesEn.getQuesscore());
						List<AnswInfo> answList = answInfoService.getAnswListByQuesId(quesEn.getIid());
						if(answList != null && answList.size() > 0) {
							for(AnswInfo answEn : answList) {
								if(StringUtil.isNotEmpty(answEn.getIsright()) && StringUtil.equals(answEn.getIsright(), "1")) {
									rightansw += answEn.getIid() +",";
								}
							}
						}
						if(StringUtil.isNotEmpty(rightansw) && rightansw.length() > 0) {
							rightansw = rightansw.substring(0, rightansw.length()-1);
						}
						json +="{\"type\":\""+quesEn.getType()+"\",\"quesid\":\""+quesEn.getIid()+"\",\"rightansw\":\""+rightansw+"\",\"quesscore\":\""+quesEn.getQuesscore()+"\"},";
					}
				}
				if(StringUtil.isNotEmpty(json) && json.length() > 0) {
					json = "["+json.substring(0, json.length()-1)+"]";
				}
				cache.put("dczj_score_"+dczjid, json);
				cache.put("dczj_scorenum_"+dczjid, allScore);
				jsonResult.setSuccess(true);
			}else {
				jsonResult.setSuccess(false);
			}
		}

		if(thanksSetting!=null) {
		  if(thanksSetting.getIsdetail() == null) {
			  jsonResult.addParam("detail","null");	 
		  }else {
			 jsonResult.addParam("detail", thanksSetting.getIsdetail());	  
		  }
		 
		}else {
		  jsonResult.addParam("detail","null");	
		}
		
		jsonResult.addParam("json", json);
		jsonResult.addParam("scorenum", allScore);
		return jsonResult;
	}
}
