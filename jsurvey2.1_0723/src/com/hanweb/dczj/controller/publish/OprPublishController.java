package com.hanweb.dczj.controller.publish;

import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.mq.MessageCreator;
import com.hanweb.common.mq.MqOperator;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.PhonePublishService;
import com.hanweb.dczj.service.PublishService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.StyleService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class OprPublishController {

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
	private PublishService publishService;
	@Autowired
	private DisplayConfigService displayConfigService;
	@Autowired
	private PhonePublishService phonePublishService;
	
	/**
	 * 
	 * @param webId
	 * @param formid 
	 * @param preview 预览
	 * @return
	 */
	@RequestMapping("dopreview")
	public ModelAndView doPreview(String dczjid,int pcstyle,int webid) {
		ModelAndView model = new ModelAndView("dczj/title/titlepublish");
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		String index = "1";
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
		confMap.put("surveyresulturl",BaseInfo.getContextPath()+"/jsurvey/questionnaire/result_"+dczjid+".html");
		confMap.put("surveyphoneresulturl",BaseInfo.getContextPath()+"/jsurvey/questionnaire/phoneresult_"+dczjid+".html");
		if(pcstyle == 1) {
			strContent = publishService.startingPage(settingEn,styleEn,dczjid,confMap,displayConfig,index);
			if(StringUtil.isNotEmpty(strContent)) {
				strContent = strContent.replaceAll("surveysubmitvalidate", "jsurveysubmitvalidate");
			}
		}else if(pcstyle == 0) {
			phoneStrContent = phonePublishService.startingPage(settingEn,styleEn,dczjid,confMap,displayConfig);
			if(StringUtil.isNotEmpty(phoneStrContent)) {
				phoneStrContent = phoneStrContent.replaceAll("phonesurveysubmitvalidate", "phonejsurveysubmitvalidate");
			}
		}
		model.addObject("strContent",strContent);
		model.addObject("phoneStrContent",phoneStrContent);
		model.addObject("errorMessage",errorMessage);
		model.addObject("pcstyle",pcstyle);
		model.addObject("dczjid",dczjid);
		model.addObject("webid",webid);
		return model;
	}
	
	/**
	 * 检查是否发布
	 * @param dczjid
	 * @return
	 */
	@RequestMapping("checkpublish")
	@ResponseBody
	public JsonResult checkpublish(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		if(titleInfo !=null ) {
			if(titleInfo.getIspublish() == 1) {
				jsonResult.setSuccess(false);
				jsonResult.setMessage("该问卷已经发布！");
			}else {
				jsonResult.setSuccess(true);
			}
		}else {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("操作失败，请联系管理员！");
		}
		return jsonResult;
	}
	
	/**
	 * 发布
	 * @param en
	 * @return
	 */
	@RequestMapping("dopublish")
	@ResponseBody
	public JsonResult doPublish(String dczjid,String preview) {
		JsonResult jsonResult = JsonResult.getInstance();
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		String loadPath = webPath + "jsurvey_"+ dczjid + ".html";
		String phonePath = webPath + "phonejsurvey_"+ dczjid + ".html";
		String testPath =  webPath + "testjsurvey_"+ dczjid + ".html";
		
	    String index = "1";
		
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		Cache cache1 = CacheManager.getInstance("dczj_score");
		cache1.put("dczj_score_"+dczjid, "");
		cache1.put("dczj_scorenum_"+dczjid, "");
		if(StringUtil.isNotEmpty(dczjid)){
			settingEn = settingService.getEntityBydczjid(dczjid);  //formid查询config实体
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			styleEn = styleService.getEntityByDczjid(dczjid);
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
		}
		String errorMessage = null;  //错误信息
		if(styleEn == null){
			styleEn = new Style();
		}
		if(settingEn == null) {
			settingEn = new Dczj_Setting();
		}
		if(displayConfig == null) {
			errorMessage = "请先编辑问卷信息！";
			return jsonResult.setMessage(errorMessage);
		}
		if(titleInfo == null){
			errorMessage = "调查表不存在！";
			return jsonResult.setMessage(errorMessage);
		}
		if(titleInfo.getType() == 3) {
			int count = quesInfoService.findNum(NumberUtil.getInt(dczjid));
			if(count == 0) {
				errorMessage = "未选择测评题！";
				return jsonResult.setMessage(errorMessage);	
			}
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
			confMap.put("dczjtype",""+titleInfo.getType());
		}
		String strContent = "";
		String phoneStrContent = "";
		int state = 0;
		if(titleInfo != null) {
			state = titleInfo.getState();
		}	
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
					index = "0";
					strContent = publishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml,index);
					phoneStrContent = phonePublishService.getResultPage(settingEn,styleEn,titleInfo,confMap,displayConfig,ishtml);
				}
				break;
		}
		String testContent = "";
		if(titleInfo.getType() == 3) {
			testContent = publishService.getTestPage(titleInfo);
		}
		Cache cache = CacheManager.getInstance("jsurvey_front");
		Cache jsurveycache = CacheManager.getInstance("jsurvey");
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
		jsurveycache.put("dczj_title_"+dczjid, titleInfo);
		List<QuesInfo> quesList2 = quesInfoService.findQuesListByDczjid(dczjid);
		String validateQuesJson = "";
		if(quesList2 != null && quesList2.size()>0){
			for(QuesInfo textQuesEn : quesList2){
				validateQuesJson += "{\"quesId\":\""+textQuesEn.getIid()+"\",\"mustfill\":\""+textQuesEn.getIsmustfill()+"\",\"min\":\""+textQuesEn.getMinselect()+"\",\"max\":\""+textQuesEn.getMaxselect()+"\",\"type\":\""+textQuesEn.getType()+"\",\"validaterules\":\""+textQuesEn.getValidaterules()+"\"},";
				List<AnswInfo> answList = answInfoService.getAnswListByQuesId(textQuesEn.getIid());
				if(answList != null && answList.size() > 0) {
					for(AnswInfo answEn : answList) {
						jsurveycache.put("dczj_answ_entity_"+answEn.getIid(), answEn);
					}
					jsurveycache.put("dczj_answ_list_"+textQuesEn.getIid(), answList);
				}
			}
			jsurveycache.put("dczj_ques_list_"+dczjid, quesList2);
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
				if(StringUtil.isNotEmpty(testContent)) {
					FileUtil.writeStringToFile(testPath, testContent); 
				}
			}else{
				final String surveycontent = strContent;
				final String surveyloadPath = "/jsurvey/questionnaire/jsurvey_"+ dczjid + ".html";
				MqOperator mqOperator = new MqOperator("1");
				mqOperator.sendTopic("com.hanweb.dczj.survey.topic", new MessageCreator(){
					@Override
					public MapMessage createMessage(Session session)
							throws JMSException {
						MapMessage msg = session.createMapMessage();
						msg.setStringProperty("surveycontent", surveycontent);
						msg.setStringProperty("surveyloadPath", surveyloadPath);
						return msg;
					}
				});
				final String phoneSurveyContent = phoneStrContent;
				final String phoneSurveyloadPath = "/jsurvey/questionnaire/phonejsurvey_"+ dczjid + ".html";
				mqOperator.sendTopic("com.hanweb.dczj.phonesurvey.topic", new MessageCreator(){
					@Override
					public MapMessage createMessage(Session session)
							throws JMSException {
						MapMessage msg = session.createMapMessage();
						msg.setStringProperty("phoneSurveyContent", phoneSurveyContent);
						msg.setStringProperty("phoneSurveyloadPath", phoneSurveyloadPath);
						return msg;
					}
				});
			}
			bl = true;
		}
		if(bl){
			titleInfoService.setUpdateHtml(dczjid,1);
			String userIp = IpUtil.getIp();
			String [] ips = userIp.split(",");
			if (ips != null && ips.length > 0){ //防止双IP
			    userIp = StringUtil.getString(ips[0]);
			}
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			String userName = currentUser.getName();
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("发布")
					.setIpAddr(userIp).setLogUser(userName+"("+currentUser.getLoginName()+")").setDescription("发布jsurveyid为"+dczjid+"的调查表"));
			jsonResult.setSuccess(true);
		}
		return jsonResult;
	}
	
}
