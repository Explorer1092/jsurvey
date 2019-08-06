package com.hanweb.dczj.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.TitleInfo;

public class PublishTaskService {

	@Autowired
	private SettingService settingService;
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired 
	private AnswInfoService answInfoService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private DisplayConfigService displayConfigService;
	@Autowired
	private PublishService publishService;
	@Autowired
	private PhonePublishService phonePublishService;
	@Autowired
	private TotalRecoService totalRecoService;
	
	public boolean dopublish(String dczjid) {
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		String loadPath = webPath + "jsurvey_"+ dczjid + ".html";
		String phonePath = webPath + "phonejsurvey_"+ dczjid + ".html";
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		if(StringUtil.isNotEmpty(dczjid)){
			settingEn = settingService.getEntityBydczjid(dczjid);  //formid查询config实体
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			styleEn = styleService.getEntityByDczjid(dczjid);
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
		}
		
		if(styleEn == null){
			styleEn = new Style();
		}
		if(settingEn == null) {
			settingEn = new Dczj_Setting();
		}
		if(displayConfig == null) {
			displayConfig = new DisplayConfig();
		}
		String errorMessage = null;  //错误信息
		if(titleInfo == null){
			errorMessage = "调查表不存在！";
		}
		//解析配置
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		if(titleInfo != null){
			confMap.put("surveytitle",titleInfo.getTitlename());  //调查标题
			confMap.put("createtime",titleInfo.getCreatetime());
			confMap.put("starttime",settingEn.getStarttime());
			confMap.put("endtime",settingEn.getEndtime());
			confMap.put("currentPath",webPath);
			confMap.put("formId",""+dczjid);
		}else{
			errorMessage = "调查表不存在!";
		}
		String strContent = "";
		String phoneStrContent = "";
		int state = 0;
		if(titleInfo != null) {
			state = titleInfo.getState();
		}
		String index = "1";
		switch(state){
			case 0: //进行中
				confMap.put("surveyresulturl",BaseInfo.getContextPath()+"/jsurvey/questionnaire/result_"+dczjid+".html");
				confMap.put("surveyphoneresulturl",BaseInfo.getContextPath()+"/jsurvey/questionnaire/phoneresult_"+dczjid+".html");
				strContent = publishService.startingPage(settingEn,styleEn,dczjid,confMap,displayConfig,index);
				phoneStrContent = phonePublishService.startingPage(settingEn,styleEn,dczjid,confMap,displayConfig);
				break;
			case 1:
				strContent = publishService.getNoStartPage(dczjid);
				phoneStrContent = phonePublishService.getNoStartPage(dczjid);
				break;
			case 2: //已结束
				int isShow = settingEn.getIsopen();
				if(isShow == 0){
					strContent = publishService.getEndPage(dczjid);
					phoneStrContent = phonePublishService.getEndPage(dczjid);
				}else{
					int ishtml =1;
					strContent = publishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml,index);
					phoneStrContent = phonePublishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml);
				}
				break;
		}
		Cache cache = CacheManager.getInstance();
		List<QuesInfo> quesList1 = quesInfoService.findDQuesByDczjId(dczjid);
		String hiddenQuesJson = "";
		if(quesList1 != null && quesList1.size()>0){
			for(QuesInfo quesEn : quesList1 ){
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
		}
		int pageNum = quesInfoService.findpageNumCount(dczjid);
		cache.put("pageNum_"+dczjid, pageNum);
		
		List<QuesInfo> quesList2 = quesInfoService.findQuesListByDczjid(dczjid);
		String validateQuesJson = "";
		if(quesList2 != null && quesList2.size()>0){
			for(QuesInfo textQuesEn : quesList2){
				validateQuesJson += "{\"quesId\":\""+textQuesEn.getIid()+"\",\"mustfill\":\""+textQuesEn.getIsmustfill()+"\",\"min\":\""+textQuesEn.getMinselect()+"\",\"max\":\""+textQuesEn.getMaxselect()+"\",\"type\":\""+textQuesEn.getType()+"\"},";
			}
		}
		if(StringUtil.isNotEmpty(validateQuesJson)){
			validateQuesJson = "[" + validateQuesJson.substring(0,validateQuesJson.length()-1) + "]";
			cache.put("validateQuesJson_"+dczjid, validateQuesJson);
		}
		boolean bl = false;
		if(errorMessage == null){
			if(Settings.getSettings().getEnabledistributed()==0){
				FileUtil.writeStringToFile(loadPath, strContent);
				FileUtil.writeStringToFile(phonePath, phoneStrContent);  //移动端发布静态页面
//			}else{
//				final String surveycontent = strContent;
//				final String surveyloadPath = "/dczj/survey/form_"+ formid + ".html";
//				MqOperator mqOperator = new MqOperator("1");
//				mqOperator.sendTopic("com.hanweb.dczj.survey.topic", new MessageCreator(){
//					@Override
//					public MapMessage createMessage(Session session)
//							throws JMSException {
//						MapMessage msg = session.createMapMessage();
//						msg.setStringProperty("surveycontent", surveycontent);
//						msg.setStringProperty("surveyloadPath", surveyloadPath);
//						return msg;
//					}
//				});
//				final String phoneSurveyContent = phoneStrContent;
//				final String phoneSurveyloadPath = "/dczj/survey/phoneform_"+ formid + ".html";
//				mqOperator.sendTopic("com.hanweb.dczj.phonesurvey.topic", new MessageCreator(){
//					@Override
//					public MapMessage createMessage(Session session)
//							throws JMSException {
//						MapMessage msg = session.createMapMessage();
//						msg.setStringProperty("phoneSurveyContent", phoneSurveyContent);
//						msg.setStringProperty("phoneSurveyloadPath", phoneSurveyloadPath);
//						return msg;
//					}
//					
//				});
			}
			bl = true;
		}else{
			return bl;
		}
		titleInfoService.setUpdateHtml(dczjid,1);
		return bl;
	}

	public boolean doResultPublish(String dczjid) {
		String index = "1";
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		String loadPath = webPath + "result_"+ dczjid + ".html";
		String phonePath = webPath + "phoneresult_"+ dczjid + ".html";
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		if(StringUtil.isNotEmpty(dczjid)){
			settingEn = settingService.getEntityBydczjid(dczjid);  //formid查询config实体
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			styleEn = styleService.getEntityByDczjid(dczjid);
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
		}
		
		if(styleEn == null){
			styleEn = new Style();
		}
		if(settingEn == null) {
			settingEn = new Dczj_Setting();
		}
		if(displayConfig == null) {
			displayConfig = new DisplayConfig();
		}
		
		int count = totalRecoService.findRecoIsHaveSubmitByDczjid(dczjid);
		if(count == 0) {
			return false;
		}
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		if(titleInfo != null){
			confMap.put("surveytitle",titleInfo.getTitlename());  //调查标题
			confMap.put("createtime",titleInfo.getCreatetime());
			confMap.put("starttime",settingEn.getStarttime());
			confMap.put("endtime",settingEn.getEndtime());
			confMap.put("currentPath",webPath);
			confMap.put("dczjid",""+dczjid);
			confMap.put("formId",""+dczjid);
			confMap.put("dczjtype",""+titleInfo.getType());
		}else{
			return false;
		}
		String strContent = "";
		String phoneStrContent = "";
		int ishtml =1;
		strContent = publishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml,index);
		phoneStrContent = phonePublishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml);
		FileUtil.writeStringToFile(loadPath, strContent);
		FileUtil.writeStringToFile(phonePath, phoneStrContent);  //移动端发布静态页面
		return true;
	}
}
