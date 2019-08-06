package com.hanweb.dczj.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.MessageContent;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;


public class PublishService {
	
	@Autowired
	private TemplateService templateService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private MessageContentService messageContentService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private TotalRecoService totalRecoService;
	
	/**
	 * 获取进行中的页面
	 * @param settingEn
	 * @param styleEn
	 * @param dczjid
	 * @param confMap
	 * @param displayConfig 
	 * @return
	 */
	public String startingPage(Dczj_Setting settingEn, Style styleEn, String dczjid,
			HashMap<String, Object> confMap, DisplayConfig displayConfig) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,0); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			temp = FileUtil.readFileToString(temp);
		}
		
		String style = null;
		if(styleEn != null ){
			style = styleEn.getPcstyle();
		}
		if(style == null || style.length() == 0){ //如果没设置样式时，，设置默认样式
			String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/htmlform.xml";
			style = styleService.getStyleCodeByPath(dauatlpage);
		}
		style = style.substring(1,style.length()-1);
		String cssstyle = displayConfig.getCssstyle();
		String thanksmessage = settingService.initPCThanksDiv(dczjid);
		style = cssstyle + style + thanksmessage;
		if(displayConfig.getIsprogress() == 1) {
			temp = temp.replace("<!--progress-->", "<div id=\"progressbar\"><div id=\"progress\"></div><p></p></div>");
		}
		int isprize = settingEn.getIsprize();
		if(isprize == 1) {
			style = style + this.getDrawDiv(dczjid);
		}
		String strContent = this.parseDczjFront(dczjid,confMap,settingEn,style,displayConfig);
		strContent  = "<script type='text/javascript' language='javascript'>" +
				"if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {" +
				"window.location.href='"+BaseInfo.getContextPath()+"/jsurvey/questionnaire/phonejsurvey_"+dczjid+".html';}</script>"+strContent;
		strContent = strContent.replaceAll("../../resources", BaseInfo.getContextPath()+"/resources");
		strContent = this.parseTemplate(strContent, temp);
		return strContent;
	}
	
	/**
	 * 在线解析模板标签
	 */
	public String parseTemplate(String strContent,String temp){
		String regEx = "<!--beginunit .*?在线调查.*?-->(?s:.*?)<!--endunit.*?-->";
		Pattern p1 = Pattern.compile(regEx);
		Matcher m1 = p1.matcher(temp);
		String tag1 = "";
		if(m1.find()){
			tag1 = m1.group();	
		}
		if(StringUtil.isNotEmpty(tag1)){
			strContent = temp.replaceFirst(tag1,strContent);
		}
		return strContent;
	}
	
	/**
	 * 解析问卷
	 * @param dczjid
	 * @param confMap
	 * @param settingEn
	 * @param style
	 * @param displayConfig 
	 * @return
	 */
	public String parseDczjFront(String dczjid, HashMap<String, Object> confMap, Dczj_Setting settingEn,
			String style, DisplayConfig displayConfig) {
		String strContent = null;
		//设置题目列表，查询该问卷的题目
		List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
		//加载相关js
		strContent = "<input type='hidden' name='contextpath' id='contextpath' value='"+BaseInfo.getContextPath()+"'>"
				   + "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>" 
				   + "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/front/js/dczj.js'></script>" 
				   + "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/jquery.validator.min.js?local=en'></script>" 
				   + "<link rel='stylesheet' type='text/css' href='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/validatoriput.css'/>";
		if(quesList == null){
			strContent += "<div style=\"text-align:center;\">该调查不存在！</div>";
		}else{
			//基本设置
			TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			//配置
			if(titleInfo != null){
				confMap.put("styleCode",style);
			}else{
				strContent += "<div style='text-align:center;'>该调查不存在！</div>";
			}
			//验证码
			int isCode = settingEn.getIscode();
			if(isCode == 1) {
				int codes = settingEn.getCodes();
				if(codes == 0) {  //图形验证码
					Random random = new Random();
					strContent += "<script type='text/javascript'>function reshcode(){document.getElementById('valicode"+dczjid+"').src='"+BaseInfo.getContextPath()+"/front/dczj/que_code.do?sessionid=surveyform"+dczjid+"&random="+ Math.random()+"'}</script>";
					confMap.put("validatecode","<input id='code' name='code' type='text' size='4' maxlength='4' " +
							"style='width:55%;height:32px;'  data-rule='required' data-msg-required='请填写验证码' data-target='#validcode' /> " +
							"<img id='valicode"+dczjid+"' src='"+BaseInfo.getContextPath()+"/front/dczj/que_code.do?sessionid=surveyform"+dczjid+"&random=" + random.nextFloat() + "'" + 
							"onclick='changecode("+dczjid+")' title='如果看不清数字，请点图片刷新验证码' style='cursor:pointer' height='40' align='absmiddle'>");
					confMap.put("validatecodename", "图形验证码：");
					confMap.put("validatecodemust", "*");
				}else if(codes == 1) { //短信验证码
					MessageContent messageContent = messageContentService.getEntityBydczjid(dczjid);
					if(messageContent == null) {
						messageContent = new MessageContent();
						messageContent.setContent("你的短信验证码为：{codenumber}，谢谢您的参与");
					}
					String phoneMessage = messageContent.getContent();
					if("codenumber".equals(phoneMessage)){
						phoneMessage = "在线调查验证码："+phoneMessage;
					}else if(phoneMessage.indexOf("codenumber") < 0){
						phoneMessage = "在线调查验证码：codenumber";
					}
					String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/phonecode.xml";
					String setPhoneCode = styleService.getStyleCodeByPath(dauatlpage);
					HashMap<String, Object> tag = new HashMap<String, Object>();
					tag.put("dczjid", dczjid);
					tag.put("phoneMessage",phoneMessage);
					setPhoneCode = setPhoneCode.substring(1,setPhoneCode.length()-1);
					setPhoneCode = setPhoneCode.replaceAll("\r\n|\n","\\\\n");
					setPhoneCode = styleService.parseTag(setPhoneCode,tag);//解析样式中的标签
					confMap.put("phonecode", setPhoneCode); 
				}
			}
			//查看结果
			confMap.put("dczjid", dczjid + "");
			confMap.put("istitlenumber",displayConfig.getIstitlenumber()+"");
			confMap.put("isshowscore",displayConfig.getIsshowscore()+"");
			confMap.put("chooseframe_style",displayConfig.getChooseframe_style()+"");
			confMap.put("isopencontent",displayConfig.getIsopencontent()+"");
			confMap.put("contentsize",displayConfig.getContentsize());
			confMap.put("filepath",BaseInfo.getContextPath());
			//解析列表题目
			strContent += parseFormFront(confMap,quesList,settingEn);
		}
		return strContent;
	}

    /**
     * 解析题目
     * @param confMap
     * @param quesList
     * @param settingEn
     * @return
     */
	private String parseFormFront(HashMap<String, Object> confMap, List<QuesInfo> quesList, Dczj_Setting settingEn) {
		String strContent = (String)confMap.get("styleCode"); //样式效果代码
		//拆开替换，防止出现单个\r或者\n替换不掉
		strContent = strContent.replace("\n","");
		strContent = strContent.replace("\r","");
		String regEx1 = "<!--for.*?-->(?s:.*)<!--/for-->";  //解析for循环
		Pattern p1 = Pattern.compile(regEx1,Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(strContent);
		String hideTd = "";
		String dczjid = StringUtil.getString((String)confMap.get("dczjid"));  //获取formid
		String formName = "dczj" + dczjid;
//		TitleInfo titleInfoEn = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		int pageType = NumberUtil.getInt((String)confMap.get("pageType"));	//页面类型 0:表单页; 1:结果页; 
		if(m1.find()){  
			String temp1 = m1.group();
			String regEx2 = "<!--for.*?-->";
			Pattern p2 = Pattern.compile(regEx2,Pattern.CASE_INSENSITIVE);
			Matcher m2 = p2.matcher(temp1);
			m2.find();
			String temp2 = m2.group();
			temp1 = temp1.substring(temp2.length(),temp1.length()-11);	//去掉for temp1为标签for循环里面的内容
			String strFor = "";
			String strInner = "";
			String xiaojie = "";
			String fenye = "";
			Iterator<QuesInfo> it = quesList.iterator(); //迭代器遍历调查题目
			HashMap<String,Object> quesConfMap = null;
			int k = 0;
			while(it.hasNext()){ 
				quesConfMap = confMap;
				QuesInfo quesEn = (QuesInfo)it.next();
				
				//调查问题
				int quesType = 0;
				int quesId = 0;
				if(quesEn != null){
					quesType = quesEn.getType();
					quesId = quesEn.getIid();   
				}
				if(quesType != 4){
	                	k += 1;
				}
				List<AnswInfo> answList = new ArrayList<AnswInfo>();
				if(quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7){//不是问答题，则获取答案列表  为单选或者多选时获取答案列表
					answList = answInfoService.getAnswListByQuesId(quesId);
				}
				if((quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7) && (answList == null || answList.size()== 0))
					continue;   //没有答案时跳出此次循环
				if(pageType == 0 ){	//0表单页 
					quesConfMap.put("col",""+quesEn.getCol());  //答案列数
					if(pageType ==0 && quesType == 3){	//文字描述
						xiaojie = StringUtil.getString(quesEn.getContent());
						if(xiaojie != null && xiaojie.length()>0){
							xiaojie = "<table width=\"100%\" style=\"margin-top:15px;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"+
						             "<tr>"+
									"<td class=\"jsurvey-td-xj\"><span class=\"jsurvey-td-xj-span\">"+xiaojie+"</span></td>"+
						             "</tr></table>";
						}
						strInner += xiaojie;  //题目类型为小节时。获取题目内容
						continue;
					}
					if(pageType ==0 && quesType == 4){  // 分页
						int pageNum = 0;
						pageNum = quesInfoService.findPageNum(dczjid,quesId+"");
						List<QuesInfo> textQuesList = quesInfoService.findTxetQuesBeforeThisQuesid(dczjid,quesId+"");
						String textJson = "";
						if(textQuesList != null && textQuesList.size()>0){
							for(QuesInfo textQuesEn : textQuesList){
								textJson += "{\"quesId\":\""+textQuesEn.getIid()+"\",\"mustfill\":\""+textQuesEn.getIsmustfill()+"\",\"min\":\""+textQuesEn.getMinselect()+"\",\"max\":\""+textQuesEn.getMaxselect()+"\",\"type\":\""+textQuesEn.getType()+"\",\"validaterules\":\""+textQuesEn.getValidaterules()+"\"},";
							}
						}
						if(StringUtil.isNotEmpty(textJson)){
							textJson = "[" + textJson.substring(0,textJson.length()-1) + "]";
						}
						int textNum = 0;
						if(pageNum > 0){
							textNum = pageNum-1;
						}
						fenye =  "<input type=\"hidden\" id=\"surveyText"+textNum+"\" value='"+textJson+"'></div><div id=\"surveyPaging"+pageNum+"\" style=\"display:none\">";
						strInner += fenye;  
						continue;
					}
					
					if(pageType ==0 && quesType == 5){	//单行文本
						String userinfo = StringUtil.getString(quesEn.getQuesname());
						if(userinfo != null && userinfo.length()>0){
							userinfo = "<table id=\"surveyTable"+quesId+"\" class=\"jsurvey-table\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr>"
						             + "<td class=\"jsurvey-td-info\"><span class=\"jsurvey-td-info-span\">"+userinfo+"</span></td>"
						             + "<td class=\"jsurvey-td-input\"><input type=\"text\" id='surveyAnsw_" + quesId + "_" + 0 + "' name='surveyAnsw" + quesId + "_content' class='jsurvey-td-info-input'/><span id=\"surveyMessage"+quesId+"\" class=\"jsurvey-messagebox\"></span></td><td></td></tr></table>";
						}
						strInner += userinfo;  //题目类型为小节时。获取题目内容
						continue;
					}
					quesConfMap.put("pageType",""+pageType);
				}else{		//1结果页  
					if(quesEn.getShowpublish() == 0) {
						strInner += "";
						continue;
					}
					if(quesType==3){	//小节
						strInner += xiaojie;  //题目类型为小节时。获取题目内容
						continue;
					}
					if(quesType == 4 || quesType == 5){
						strInner += "";
						continue;
					}
					quesConfMap.put("pageType",""+pageType);
				}
				//文本类型是自定义限制
				if(pageType == 0 && quesType !=0 && quesType !=1 && quesType!=3 && quesType != 4 && quesType!=6 && quesType != 7){	//文本类型
					quesConfMap.put("inputWidth","" + quesEn.getTextinputwidth());
					quesConfMap.put("inputHeight","" + quesEn.getTextinputheight());
					answList.add(new AnswInfo());  //如果是文本类型加入一个答案实体
				}
				
				if(pageType == 1 && quesType != 0 && quesType !=1 && quesType != 3 && quesType!=6 && quesType != 7){//答案页面时，添加查看答案按钮
					AnswInfo otherAnsw = new AnswInfo();
					answList.add(otherAnsw);
					quesConfMap.remove("other");
				}
				int basePointSum = answInfoService.getBasePointSum(quesId);
				confMap.put("basePointSum",""+basePointSum);
				/**
				if(pageType == 1){   //当为结果页面时，对答案列表进行排序 只针对多选和单选
					
					if(quesType == 0||quesType == 1){
						for(Survey_Answ answ:answList){
							String [][] currentData = surveyFormanalService.getCurrentData(answ.getI_id(),quesId,answ.getI_basepoint(),basePointSum);
							answ.setNum(NumberUtil.getInt(currentData[0][0]));
						}
						Collections.sort(answList, new Comparator<Survey_Answ>(){
							public int compare(Survey_Answ s1, Survey_Answ s2) {
								      return s2.getNum()-s1.getNum();
								}
						});
					}
				}
*/
				quesConfMap.remove("styleCode");
				quesConfMap.put("styleCode",temp1);
				quesConfMap.put("quesType",""+quesType);
				quesConfMap.put("quesId",""+quesId);
				quesConfMap.put("ismustfill",quesEn.getIsmustfill());
				quesConfMap.put("selectmin",quesEn.getMinselect());
				quesConfMap.put("selectmax",quesEn.getMaxselect());
				
				strFor = styleService.parseList(2,quesConfMap,answList); //解析答案列表
				HashMap<String,Object> tag = styleService.setTag(1,quesEn,confMap,false, k);//解析题目样式中的标签
				strInner += styleService.parseTag(strFor,tag);
			}//迭代题目结束
			strContent = m1.replaceFirst(strInner);
		}
//		String userbasemessage = null;
//		userbasemessage = parseService.userBaseMessageParse(formId);//解析用户表单 设参中控制
//		confMap.put("userbasemessage", userbasemessage);
		if(pageType==0){//表单页
			String json = "";
			String proContent = "";
			for(int i=0;i<quesList.size();i++){
				if(quesList.get(i).getType() != 3 && quesList.get(i).getType() != 4 && (StringUtil.equals(quesList.get(i).getRelyanswid()+"", "0"))){
					json += "{\"quesid\":\""+quesList.get(i).getIid()+"\",\"type\":\""+quesList.get(i).getType()+"\"},";
				}
			}
			if(StringUtil.isNotEmpty(json) && json.length()>0){
				json = json.substring(0,json.length()-1);
			}
			
			proContent += "<input id='jsonquesid' name='jsonquesid' type='hidden' value="+json+">";
			proContent += "<input id='jsurveytype' name='jsurveytype' type='hidden' value="+json+">";
			proContent += "<input id='jsurveyuuid' name='jsurveyuuid' type='hidden' value=''>";
			proContent += "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/front/js/progress.js'></script>";
			proContent += "<link rel='stylesheet' type='text/css' href='"+BaseInfo.getContextPath()+"/resources/front/css/progress.css'/>";
			proContent += "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/front/js/frontCheck.js'></script>";
			strContent = "<input type='hidden' name='dczjid' id='dczjid' value='"+dczjid+"'>"+proContent+"<form id=\"" + formName + "\" name=\"" + formName + 
						"\" method=\"post\" action=\""+BaseInfo.getContextPath()+"/front/dczj/dczj_submit.do?dczjid=" + dczjid  +
						"\" style=\"margin:0;\" target=\"hiddenFrame" + dczjid + 
						"\">" + strContent + "<input type='hidden' name='submitType' value='1'><input type='hidden' name='isprize' id='isprize' value='"+settingEn.getIsprize()+"'><input type='hidden' name='sumscore' id='sumscore' val=''><input type='hidden' name='rightnum' id='rightnum' val=''></form>";
			strContent += "<iframe frameborder='0'  name=\"hiddenFrame" + dczjid +"\" width=\"0\" height=\"0\"></iframe>";
			
		}else if(pageType==1){//结果页
			String scriptCode = "<script language=\"javascript\" src=\""+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/jquery-1.12.3.min.js\"></script>";
			scriptCode = scriptCode.replaceAll("\\$","\\\\\\$");
			hideTd = hideTd.replaceAll("\\$", "\\\\\\$");
			strContent  = scriptCode + hideTd + strContent;
		}
		strContent = styleService.parseTag(strContent,confMap);//解析表单标签
		return strContent;
	}
	
	/**
	 * 获取抽奖DIV
	 * 
	 * @param dczjid
	 * @return
	 */
	public String getDrawDiv(String dczjid) {
		String drawpath = BaseInfo.getRealPath() + "/resources/dczj/draw/drawpage.xml";
		String drawContent = styleService.getStyleCodeByPath(drawpath);
		Properties pro = new Properties(BaseInfo.getRealPath()
				+ "/WEB-INF/config/setup.properties");
		String basePath = pro.getString("domain");
		if (StringUtil.isEmpty(basePath)) {
			basePath = "../..";
		}
		String drawSrc = basePath+"/front/draw/godrawpage.do?dczjid="+dczjid;
		drawContent = drawContent.replaceAll("<!--drawsrc-->", drawSrc);
		return drawContent;
	}

	public String getResultPage(Dczj_Setting settingEn, Style styleEn, TitleInfo titleInfo, HashMap<String, Object> confMap,
			DisplayConfig displayConfig, int ishtml) {
		String temp = null;
		String dczjid = titleInfo.getIid()+"";
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,0); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			temp = FileUtil.readFileToString(temp);
		}
		String style = null;
		if(styleEn != null ){
			style = styleEn.getPcstyle();
		}
		if(style == null || style.length() == 0){ //如果没设置样式时，，设置默认样式
			String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/result.xml";
			style = styleService.getStyleCodeByPath(dauatlpage);
		}
		style = style.substring(1,style.length()-1);
		String cssstyle = displayConfig.getCssstyle();
		style = cssstyle + style;
		
		String strContent = "";
		List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
		if(quesList==null){
			strContent = "<div style=\"text-align:center;\">暂无记录！</div>";
		}else{
			//配置
			confMap.put("pageType","1");							//页面类型 1:结果页
			confMap.put("styleCode",style);
			confMap.put("page","result");//表明是查看结果页面
			confMap.put("buildSpacetime","1");
			if(ishtml == 1){
				confMap.put("surveyurl", BaseInfo.getContextPath()+"/jsurvey/questionnaire/jsurvey_"+titleInfo.getIid()+".html");
			}else{
				confMap.put("surveyurl",BaseInfo.getContextPath()+"/manager/dczj/dopreview.do?dczjid="+titleInfo.getIid()+"&pcstyle=1");
			}
			String AfterAffiContent = "调查已结束！";
			if(StringUtil.isEmpty(AfterAffiContent)){//没有设置默认
				AfterAffiContent = "调查已结束！";
			}
			int sumcount = totalRecoService.findRecoIsHaveSubmitByDczjid(dczjid);
			confMap.put("sumcount",sumcount);
			confMap.put("chooseframe_style",displayConfig.getChooseframe_style()+"");
			confMap.put("isopencontent",displayConfig.getIsopencontent()+"");
			confMap.put("contentsize",displayConfig.getContentsize());
			confMap.put("filepath",BaseInfo.getContextPath());
		}
		strContent += this.parseFormFront(confMap,quesList,settingEn);
		if(titleInfo.getState() == 0){
			strContent  = "<script type='text/javascript' language='javascript'>" +
					"if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {" +
					"window.location.href='"+BaseInfo.getContextPath()+"/jsurvey/questionnaire/phonejsurvey_"+dczjid+".html';}</script>"
					+strContent;
		}else if(titleInfo.getState() == 2){
			strContent  ="<script type='text/javascript' language='javascript'>" +
					"if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {" +
					"window.location.href='"+BaseInfo.getContextPath()+"/dczj/survey/phoneresult_"+dczjid+".html';}</script>"
					+strContent;
		}
		strContent = this.parseTemplate(strContent,temp);
		return strContent;
	}
	
	/**
	 * 获取手机抽奖DIV
	 * 
	 * @param dczjid
	 * @return
	 */
	public String getPhoneDrawDiv(String dczjid) {
		String drawpath = BaseInfo.getRealPath() + "/resources/dczj/draw/drawphonepage.xml";
		String drawContent = styleService.getStyleCodeByPath(drawpath);
		Properties pro = new Properties(BaseInfo.getRealPath()
				+ "/WEB-INF/config/setup.properties");
		String basePath = pro.getString("domain");
		if (StringUtil.isEmpty(basePath)) {
			basePath = "../..";
		}
		String drawSrc = basePath+"/front/draw/gophonedrawpage.do?formId="+dczjid;
		drawContent = drawContent.replaceAll("<!--drawsrc-->", drawSrc);
		return drawContent;
	}

	public String getEndPage(String dczjid) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,0); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			temp = FileUtil.readFileToString(temp);
		}
		String content = "<div style='font-weight:bold;text-align:center;font-size:16px;padding-top:20px;padding-bottom:20px;'>问卷已经结束，谢谢参与！</div>";
		String strContent = this.parseTemplate(content, temp);
		return strContent;
	}

	public String getNoStartPage(String dczjid) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,0); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			temp = FileUtil.readFileToString(temp);
		}
		String content = "<div style='font-weight:bold;text-align:center;font-size:16px;padding-top:20px;padding-bottom:20px;'>问卷尚未开始，敬请期待！</div>";
		String strContent = this.parseTemplate(content, temp);
		return strContent;
	}

	/**
	 * 测评页面
	 * @param titleInfo
	 * @return
	 */
	public String getTestPage(TitleInfo titleInfo) {
		String testContent = "";
		String temp =  BaseInfo.getRealPath() + "/resources/dczj/template/testContent.html";
		temp = FileUtil.readFileToString(temp);
		int jsurveyid = titleInfo.getIid();
		List<QuesInfo> quesinfoList = quesInfoService.findQuesListByDczjid(StringUtil.getString(jsurveyid));
		if(quesinfoList != null && quesinfoList.size() >0) {
			for(QuesInfo quesEn : quesinfoList) {
				String quesname = quesEn.getQuesname();
				String quesscore = StringUtil.getString(quesEn.getQuesscore());
				Integer type = quesEn.getType();
				Integer quesid = quesEn.getIid();
                if(type == 0 || type == 1 ||type == 6 || type == 7) {
                	String content = temp.replaceAll("<!--quesid-->", StringUtil.getString(quesid)).replaceAll("<!--jsurveyquesname-->", quesname);
    				String rightAnsw = answInfoService.findRightAnswByQuesid(quesEn.getIid());
    				if(type == 6 || type == 7) {
    					rightAnsw = "<tr><td>正确答案："+rightAnsw+"; 分值："+quesscore+"分</td></tr>";
    					content = content.replaceAll("<!--rightansw-->", rightAnsw);
    				}
    				testContent += content;
				}
			}
		}
		return testContent;
	}
}
