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
import org.springframework.beans.factory.annotation.Qualifier;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.file.IFileUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.MessageContent;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;

public class PhonePublishService {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private MessageContentService messageContentService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private CountService countService;
	@Autowired
	private PublishService publishService;
	
	@Autowired
	@Qualifier("FileUtil")
	private IFileUtil fileUtil;
	
	public String startingPage(Dczj_Setting settingEn, Style styleEn, String dczjid, HashMap<String, Object> confMap,
			DisplayConfig displayConfig) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,2); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			temp = FileUtil.readFileToString(temp);
		}
		String style = null;
		if (styleEn != null) {
			style = styleEn.getPhonestyle();
		}
		if (style == null || style.length() == 0) {
			// 手机端样式设置为默认样式
			String dauatlpage = BaseInfo.getRealPath() + "/resources/dczj/template/phoneform.xml";
			style = styleService.getStyleCodeByPath(dauatlpage);
		}
		style = style.substring(1, style.length() - 1);
		String cssstyle = displayConfig.getCssstyle();
		String thanksmessage = settingService.initPhoneThanksDiv(dczjid);
		style =cssstyle+style+thanksmessage;
		int isprize = settingEn.getIsprize();
		if(isprize == 1) {
			style = style + publishService.getPhoneDrawDiv(dczjid);
		}
		
		String strContent = this.parsePhoneFront(dczjid,confMap,settingEn,style,displayConfig);
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

	public String parsePhoneFront(String dczjid, HashMap<String, Object> confMap, Dczj_Setting settingEn, String style,
			DisplayConfig displayConfig) {
		String strContent = null;
		// 设置题目列表，查询该问卷的题目
		List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
		// 加载相关js
		strContent = "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>" 
				   + "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/front/js/phonedczj.js'></script>" 
				   + "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/jquery.validator.min.js?local=en'></script>" 
				   + "<link rel='stylesheet' type='text/css' href='"+BaseInfo.getContextPath()+"/resources/dczj/nicevalidator/validatoriput.css'/>"
		           + "<link rel='stylesheet' href='"+ BaseInfo.getContextPath()+ "/resources/dczj/mui/css/dczj.css'/>"
		           + "<script src=\" "+ BaseInfo.getContextPath()+"/resources/dczj/mui/js/mui.min.js\"></script>";
		
		if (quesList == null) {
			strContent += "<div style=\"text-align:center;\">该调查不存在！</div>";
		} else {
			// 基本设置
			TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			// 配置
			String formName = null;
			if (titleInfo != null) {
				confMap.put("state",""+ titleInfo.getState());
				confMap.put("styleCode", style);
				formName = "dczj" + dczjid;
			} else {
				strContent += "<div style='text-align:center;'>该调查不存在！</div>";
			}
			// 验证码
			int isCode = settingEn.getIscode();
			if(isCode == 1) {
				int codes = settingEn.getCodes();
				// 手机短信验证
				if(codes == 0){	//开启验证码，需要插入标签，并且手机验证码为空时
					Random random = new Random();
					strContent += "<script type='text/javascript'>function reshcode(){document.getElementById('valicode"+dczjid+"').src='"+BaseInfo.getContextPath()+"/front/dczj/que_code.do?sessionid=surveyform"+dczjid+"&random="+ Math.random()+"'}</script>";
					confMap.put("validatecode","<input id='code' name='code' placeholder='请输入验证码' class='text-normal' type='text' size='4' maxlength='4' " +
							"style='width:60%;'  data-rule='required' data-msg-required='请填写验证码' data-target='#validcode' /> " +
							"<img id='valicode"+dczjid+"' src='"+BaseInfo.getContextPath()+"/front/dczj/que_code.do?sessionid=surveyform"+dczjid+"&random=" + random.nextFloat() + "'" + 
							"onclick='changecode("+dczjid+")' title='如果看不清数字，请点图片刷新验证码' style='cursor:pointer' height='30' width='30%' align='absmiddle'>");
					confMap.put("validatecodename", "验证码：");
				}else if(codes == 1) {
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
					String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/phonecode2.xml";
					String setPhoneCode = styleService.getStyleCodeByPath(dauatlpage);
					HashMap<String, Object> tag = new HashMap<String, Object>();
					tag.put("dczjid", dczjid);
					tag.put("phoneMessage", phoneMessage);
					setPhoneCode = setPhoneCode.substring(1,setPhoneCode.length() - 1);
					setPhoneCode = setPhoneCode.replaceAll("\r\n|\n", "\\\\n");
					setPhoneCode = styleService.parseTag(setPhoneCode, tag);// 解析样式中的标签
					confMap.put("phonecode", setPhoneCode);
				}
			}
			
			// 重置表单
			String reset = formName + ".reset();\\\\$('#" + formName+ "').validator('cleanUp');";
			confMap.put("reset", reset);
			// 查看结果
			confMap.put("dczjid", dczjid + "");
			confMap.put("istitlenumber",displayConfig.getIstitlenumber()+"");
			confMap.put("isshowscore",displayConfig.getIsshowscore()+"");
			confMap.put("chooseframe_style",displayConfig.getChooseframe_style()+"");
			confMap.put("isopencontent",displayConfig.getIsopencontent()+"");
			confMap.put("contentsize",displayConfig.getContentsize());
			confMap.put("filepath",BaseInfo.getContextPath());
			// 解析列表题目
			strContent += parseFormFront(confMap,quesList,settingEn);
		}
		return strContent;
	}

	private String parseFormFront(HashMap<String, Object> confMap, List<QuesInfo> quesList, Dczj_Setting settingEn) {
		String strContent = (String) confMap.get("styleCode"); // 样式效果代码
		// 拆开替换，防止出现单个\r或者\n替换不掉
		strContent = strContent.replace("\n", "");
		strContent = strContent.replace("\r", "");
		String regEx1 = "<!--for.*?-->(?s:.*)<!--/for-->"; // 解析for循环
		Pattern p1 = Pattern.compile(regEx1, Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(strContent);
		// String validateScript = ""; //验证js
		String hideTd = "";
		String dczjid = StringUtil.getString((String) confMap.get("dczjid")); // 获取formid
		String formName = "dczj" + dczjid;
		int pageType = NumberUtil.getInt((String) confMap.get("pageType")); // 页面类型
																			// 0:表单页;
																			// 1:结果页;
		if (m1.find()) {
			String temp1 = m1.group();
			String regEx2 = "<!--for.*?-->";
			Pattern p2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
			Matcher m2 = p2.matcher(temp1);
			m2.find();
			String temp2 = m2.group();
			temp1 = temp1.substring(temp2.length(), temp1.length() - 11); // 去掉for
																			// temp1为标签for循环里面的内容
			String strFor = "";
			String strInner = "";
			String xiaojie = "";
			Iterator<QuesInfo> it = quesList.iterator(); // 迭代器遍历调查题目
			HashMap<String, Object> quesConfMap = null;
			int k = 0;
			while (it.hasNext()) {
				quesConfMap = confMap;
				QuesInfo quesEn = (QuesInfo) it.next();
				// 调查问题
				int quesType = 0;
				int quesId = 0;
				if (quesEn != null) {
					quesType = quesEn.getType();
					quesId = quesEn.getIid();   
				}
				if(quesType != 4){
					k += 1;
				}
				List<AnswInfo> answList = new ArrayList<AnswInfo>();
				if (quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7) {// 不是问答题，则获取答案列表
														// 为单选或者多选时获取答案列表
					answList = answInfoService.getAnswListByQuesId(quesId);
				}
				if ((quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7)&& (answList == null || answList.size() == 0))
					continue; // 没有答案时跳出此次循环
				if (pageType == 0) { // 0表单页
					quesConfMap.put("col", "1"); // 答案列数 移动端答案控制一列
					if (pageType == 0 && quesType == 3) { // 小节
						xiaojie = StringUtil.getString(quesEn.getContent());
						if (xiaojie != null && xiaojie.length() > 0) {
							xiaojie = "<table width=\"100%\" style=\"margin-top:15px;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
									+ "<tr>"
									+ "<td style=\"word-break:break-all;width:100%; margin:0px auto;padding:8px 10px 8px 20px;background-color:#FFF;color:#DF2A3F;font-size:16px; font-family:微软雅黑;\"><span style=\"color:black;font-size:16px; font-family:微软雅黑; line-height:22px;\">"
									+ xiaojie
									+ "</span></td>"
									+ "</tr></table>";
						}

						strInner += xiaojie; // 题目类型为小节时。获取题目内容
						continue;
					}
					if(pageType == 0 && quesType == 4){
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
						
						String fenye =  "<input type=\"hidden\" id=\"surveyText"+textNum+"\" value='"+textJson+"'></div><div id=\"surveyPaging"+pageNum+"\">";
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
					quesConfMap.put("pageType", "" + pageType);
				} else { // 1结果页
					if (quesType == 3) { // 小节
						xiaojie = StringUtil.getString(quesEn.getContent());
						if (xiaojie != null && xiaojie.length() > 0) {
							xiaojie = "<table width=\"100%\" style=\"margin-top:15px;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
									+ "<tr>"
									+ "<td style=\"word-break:break-all;width:100%; margin:0px auto;padding:8px 10px 8px 20px;background-color:#FFF;color:#DF2A3F;font-size:16px; font-family:微软雅黑;\"><span style=\"color:black;font-size:16px; font-family:微软雅黑; line-height:22px;\">"
									+ xiaojie
									+ "</span></td>"
									+ "</tr></table>";
						}

						strInner += xiaojie; // 题目类型为小节时。获取题目内容
						continue;
					}
//					if (quesType == 2) {
//						hideTd += "<script language='javascript'>\\$(function(){\\$('#surveyMessage"+ quesId+ "pre').html('');\\$('#surveyMessage"+ quesId + "count').html('')});</script>";
//					}
					if(quesType == 4 || quesType == 5){
						strInner += ""; // 题目类型为分页时。为空
						continue;
					}
					quesConfMap.put("pageType", "" + pageType);
				}
				// 文本类型是自定义限制
				if(pageType == 0 && quesType !=0 && quesType !=1 && quesType!=3 && quesType != 4 && quesType != 6 && quesType != 7){	//文本类型
					quesConfMap.put("inputWidth","" + quesEn.getTextinputwidth());
					quesConfMap.put("inputHeight","" + quesEn.getTextinputheight());
					answList.add(new AnswInfo());  //如果是文本类型加入一个答案实体
				}
				
				if(pageType == 1 && quesType != 0 && quesType !=1 && quesType != 3 && quesType != 6 && quesType != 7){//答案页面时，添加查看答案按钮
					AnswInfo otherAnsw = new AnswInfo();
					answList.add(otherAnsw);
					quesConfMap.remove("other");
				}
//				int basePointSum = surveyAnswService.getBasePointSum(quesId);
//				confMap.put("basePointSum", "" + basePointSum);
				/**
				 * if(pageType == 1){ //当为结果页面时，对答案列表进行排序 for(Survey_Answ
				 * answ:answList){ String [][] currentData =
				 * surveyFormanalService
				 * .getCurrentData(answ.getI_id(),quesId,answ
				 * .getI_basepoint(),basePointSum);
				 * answ.setNum(NumberUtil.getInt(currentData[0][0])); }
				 * Collections.sort(answList, new Comparator<Survey_Answ>(){
				 * public int compare(Survey_Answ s1, Survey_Answ s2) { return
				 * s2.getNum()-s1.getNum(); } }); }
				 */
				quesConfMap.remove("styleCode");
				quesConfMap.put("styleCode", temp1);
				quesConfMap.put("quesType", "" + quesType);
				quesConfMap.put("quesId", "" + quesId);
				quesConfMap.put("ismustfill", quesEn.getIsmustfill());
				quesConfMap.put("selectmin", quesEn.getMinselect());
				quesConfMap.put("selectmax", quesEn.getMaxselect());

				strFor = this.parseList(2, quesConfMap, answList); // 解析答案列表
				HashMap<String, Object> tag = this.setTag(1, quesEn, confMap,false, k);// 解析题目样式中的标签
				strInner += styleService.parseTag(strFor, tag);
			}// 迭代题目结束
			strContent = m1.replaceFirst(strInner);
		}
//		String userbasemessage = null;
//		userbasemessage = parseService.userBaseMessageParse(formId);// 解析用户表单
//		confMap.put("userbasemessage", userbasemessage);
		if (pageType == 0) {// 表单页
			String filepath = BaseInfo.getContextPath();
			strContent =  "<input id='dczjid' name='dczjid' type='hidden' value="+dczjid+">"+ "<script type='text/javascript' src='"+BaseInfo.getContextPath()+"/resources/front/js/phoneprogress.js'></script>"
			            + "<form id=\""+ formName+ "\" name=\""+ formName+ "\" method=\"post\" action=\""+ BaseInfo.getContextPath()
					    + "/front/dczj/dczj_submit.do?dczjid="+ dczjid+ "\" style=\"margin:0;\" target=\"hiddenFrame"+ dczjid+ "\">"+ strContent
					    + "<input type='hidden' name='submitType' value='2'><input type='hidden' id='isShowResult' name='isShowResult' value='"+ settingEn.getIsopen()+ "'><input type='hidden' id='isDraw' name='isDraw' value='"+ settingEn.getIsprize()+ "'><input type='hidden' id='filepath' value='"+filepath+"'><input type='hidden' name='sumscore' id='sumscore' val=''><input type='hidden' name='rightnum' id='rightnum' val=''>";
//			if(isDraw == 1){
//				strContent += "<script type='text/javascript' src='"+filePathSelectUtil.getBasePath(configEn)+"/resources/dczj/draw/js/draw.js'></script>";
//			};            
		    strContent +="</form><iframe frameborder='0'  name=\"hiddenFrame"+ dczjid + "\" width=\"0\" height=\"0\"></iframe>";
			String temp = "";
			int showResult = NumberUtil.getInt((String) confMap.get("showResult")); // 是否显示结果
			int showAnswer = NumberUtil.getInt((String) confMap.get("showAnswer")); // 是否显示答案
			int state = NumberUtil.getInt((String) confMap.get("state"));
			if (state != 1) // 非进行中，隐藏提交和重置按钮
				temp += "\\$(\"#" + formName+ " .surveySubmit\").hide();\\$(\"#" + formName+ " .surveyReset\").hide();";
			if (showResult == 0)
				if (showAnswer == 0)
					if (temp.length() > 0) {
						temp = "<script language=\"javascript\">\\$(function(){"+ temp + "})</script>";
						temp = temp.replaceAll("\\$", "\\\\\\$");
					}
			strContent = temp + strContent;
		} else if (pageType == 1) {// 结果页
			String scriptCode = "<script language=\"javascript\" src=\""+ BaseInfo.getContextPath()+ "/resources/dczj/nicevalidator/jquery-1.12.3.min.js\"></script>"
					+ "<script language=\"javascript\">function openWindow(quesId)"
					+ "{window.open('"+ BaseInfo.getContextPath()+ "/front/survey/show_otheransw.do?quesid='+quesId);}"
					+ "function columnChart(quesId){\\$('#pie'+quesId).hide();\\$('#column2'+quesId).hide();\\$('#column'+quesId).show();}"
					+ "function pieChart(quesId){\\$('#column'+quesId).hide();\\$('#column2'+quesId).hide();\\$('#pie'+quesId).show();}"
					+ "function columnChart2(quesId){\\$('#pie'+quesId).hide();\\$('#column'+quesId).hide();\\$('#column2'+quesId).show();}</script>";
			scriptCode = scriptCode.replaceAll("\\$", "\\\\\\$");
			hideTd = hideTd.replaceAll("\\$", "\\\\\\$");
			strContent = scriptCode + hideTd + strContent;
		}
		strContent = styleService.parseTag(strContent, confMap);// 解析表单标签
		return strContent;
	}

	/**
	 * 解析答案列表
	 * 
	 * @param confMap
	 *            :标签map
	 * @param formList
	 *            :循环数据，调查表实体
	 */
	public String parseList(int iType, HashMap<String, Object> confMap,
			List<AnswInfo> surveyList) {
		String styleCode = (String) confMap.get("styleCode");
		styleCode = styleCode.replaceAll("\r\n|\n", "");
		String beforeContent = "";
		String middleContent = "";
		String afterContent = "";

		String regEx1 = "<!--for.*?-->(?s:.*?)<tr.*?>(?s:.*)</tr>(?s:.*?)<!--/for-->|<!--subfor.*?-->(?s:.*?)<tr.*?>(?s:.*)</tr>(?s:.*?)<!--/subfor-->";
		Pattern p1 = Pattern.compile(regEx1, Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(styleCode);

		String regEx2 = "<!--for.*?-->(?s:.*)<!--/for-->|<!--subfor.*?-->(?s:.*)<!--/subfor-->";
		Pattern p2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
		Matcher m2 = p2.matcher(styleCode);

		int iCol = NumberUtil.getInt((String) confMap.get("col"), 1);
		boolean haveTr = false;
		if (m1.find()) {
			String[] temp = p1.split(styleCode, -1);
			beforeContent = temp[0];
			afterContent = temp[1];
			middleContent = m1.group();
			haveTr = true;
		} else if (m2.find()) {
			String[] temp = p2.split(styleCode, -1);
			beforeContent = temp[0];
			afterContent = temp[1];
			middleContent = m2.group();
			if (iCol > 1) {
				beforeContent += "<table width=\"100%\" border=\"0\" cellspadding=\"0\" cellspacing=\"0\">";
				afterContent = "</table>" + afterContent;
				middleContent = "<tr><td>" + middleContent + "</td></tr>";
				haveTr = true;
			}
		} else {
			return styleCode;
		}
		String trTag = "";
		String td = middleContent;
		if (haveTr) {
			String regEx3 = "<tr.*?>(?s:.*)</tr>";
			Pattern p3 = Pattern.compile(regEx3, Pattern.CASE_INSENSITIVE);
			Matcher m3 = p3.matcher(middleContent);
			m3.find();
			String tr = m3.group();
			String regEx4 = "<tr.*?>";
			Pattern p4 = Pattern.compile(regEx4, Pattern.CASE_INSENSITIVE);
			Matcher m4 = p4.matcher(tr);
			m4.find();
			trTag = m4.group();
			td = tr;
			td = td.substring(trTag.length(), td.length() - 5);
		}
		middleContent = "";
		String temp;
		Iterator<AnswInfo> it = surveyList.iterator();
		int i = 0;
		int quesType = NumberUtil.getInt((String) confMap.get("quesType"));
		int bOther = NumberUtil.getInt((String) confMap.get("other"));
		while (it.hasNext()) {
			if (bOther == 1 && (quesType == 0 || quesType == 1) && (i == surveyList.size() - 1)) { // 其他答案时
				HashMap<String, Object> tag = setTag(iType, it.next(), confMap,true, i);
				temp = styleService.parseTag(td, tag);
				if (iCol > 1) {
					temp = trTag + temp.replaceFirst("<td", "<td colspan=\"" + iCol + "\"") + "</tr>";
				}
				middleContent += temp;
				continue;
			} else {
				HashMap<String, Object> tag = setTag(iType, it.next(), confMap,false, i);
				temp = styleService.parseTag(td, tag);
			}
			if (iCol > 1) {
				if (i % iCol == 0)
					middleContent += trTag + temp;
				else if ((i + 1) % iCol == 0)
					middleContent += temp + "</tr>";
				else
					middleContent += temp;
			} else if (haveTr) {
				middleContent += trTag + temp + "</tr>";
			} else {
				middleContent += temp;
			}
			i++;
		}
		String outCode = beforeContent + middleContent + afterContent;
		return outCode;
	}
	
	
	/**
	 * 设置标签值
	 * 
	 * @param iType
	 *            :列表类型 0:调查表列表;1:问题列表;2:答案列表
	 * @param formList
	 *            :循环数据，调查表实体
	 */
	public HashMap<String, Object> setTag(int iType, Object listItem,HashMap<String, Object> confMap, boolean other, int k) {
		HashMap<String, Object> tag = new HashMap<String, Object>();
		switch (iType) {
		case 1: // 问题列表
			QuesInfo quesEn = (QuesInfo)listItem;
			if(quesEn.getDczjtype() == 0) {
				tag.put("title",quesEn.getQuesname());
			}else {
				tag.put("title","");
			}
			tag.put("divquesId",quesEn.getIid());
			int istitlenumber = NumberUtil.getInt((String)confMap.get("istitlenumber"));
			if(istitlenumber == 1 && k!=0) {
				tag.put("number",k+"、");
			}else {
				tag.put("number","");
			}
			int isshowscore = NumberUtil.getInt((String)confMap.get("isshowscore"));
			if(isshowscore == 1) {
				tag.put("qustscore","（"+quesEn.getQuesscore()+"分）");
			}
			String quesNote = "";
			if(StringUtil.isNotEmpty(quesEn.getNote())) {
				quesNote = "<tr><td class=\"jsurvey-td-ques-note\"><span class=\"jsurvey-span-ques-note\">"+quesEn.getNote()+"</span></td></tr>";
			}
			tag.put("jsurveyquesnote",quesNote);
			int chooseframe_style = NumberUtil.getInt((String)confMap.get("chooseframe_style"));
			String clearoption ="";
			String qmustfill = "";
			if(quesEn.getIsmustfill() == 0) {
				clearoption = "<a id=\"clearoption"+quesEn.getIid()+"\" class=\"clearoption\" title=\"将本题所有选项设为未选中状态\" onclick=\"clearoption("+chooseframe_style+","+quesEn.getIid()+");\">[清除选择]</a>";
			}else if(quesEn.getIsmustfill() == 1){
				qmustfill = "（必填）";
			}
			tag.put("qmustfill", qmustfill);
			tag.put("clearoption",clearoption);
			tag.put("messageid","surveyMessage"+quesEn.getIid());
			tag.put("tableid","surveyTable"+quesEn.getIid());
			break;
		case 2: // 答案列表
			int pageType = NumberUtil.getInt((String) confMap.get("pageType"));// 0:表单页
																				// 1:结果页
			AnswInfo answEn = (AnswInfo)listItem;
			int quesType = NumberUtil.getInt((String)confMap.get("quesType"));
			int quesId = NumberUtil.getInt((String)confMap.get("quesId"));
			int iid = NumberUtil.getInt(answEn.getIid());
			int fillInAir = NumberUtil.getInt(answEn.getAllowfillinair());
			int framestyle = NumberUtil.getInt((String)confMap.get("chooseframe_style"));
			int ismustfill = NumberUtil.getInt(confMap.get("ismustfill"));
			int selectmin = NumberUtil.getInt(confMap.get("selectmin"));
			int selectmax = NumberUtil.getInt(confMap.get("selectmax"));
			int isopencontent = NumberUtil.getInt((String)confMap.get("isopencontent"));
			String contentsize = (String)confMap.get("contentsize");
			if(StringUtil.isEmpty(contentsize)) {
				contentsize = "30";
			}
			String filepath = (String)confMap.get("filepath");
			String option = "";
			String strOther = "";
			String answ_img = "";//备选答案图片
			String answTitle = StringUtil.getString(answEn.getAnswname());
			String answimngname = answEn.getAnswimgname();
			if (pageType == 0 || pageType == 2 || pageType == 3) {
				switch (quesType) {
				case 0: // 单选
					option = "<input  class='magic-radio' id='surveyAnsw_"+ quesId + "_" + iid + "' name='surveyAnsw"+ quesId + "' type='radio' style='vertical-align: middle;margin-bottom: 4px;' onclick='otherChange0("+ quesId + "," + iid + ");' value='" + iid+ "'/>";
					if (StringUtil.isNotEmpty(answimngname)) {
						String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
						if(Settings.getSettings().getEnableoss() == 0){
							imgpath = "../../"+imgpath;
						}else if(Settings.getSettings().getEnableoss() == 1){
							imgpath = fileUtil.getURL(imgpath);
						}
						answ_img+="<label style='cursor:pointer;' onclick='chooseimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);' for='surveyAnsw_" + quesId + "_" + iid+"' >" +"<img class='phoneansw_img' style='cursor:pointer;' src='"+imgpath+"'></label>";
					}
					if(fillInAir == 1){
						strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"' />";
					}
					break;
				case 1: // 多选
					option = "<input id='surveyAnsw_"+ quesId+ "_"+ iid+ "' name='surveyAnsw"+ quesId+ "'style='vertical-align: middle;margin-bottom: 4px;"+ "' type='checkbox' onclick='otherChange("+ quesId + "," + iid + ");' value='" + iid+ "' />";
					if(ismustfill == 1){
						option +="<input id='surveyAnswMin_"+iid+"' type='hidden' value='"+selectmin+"'><input id='surveyAnswMax_"+iid+"' type='hidden' value='"+selectmax+"'>";
					}
					if (StringUtil.isNotEmpty(answimngname)) {
						String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
						if(Settings.getSettings().getEnableoss() == 0){
							imgpath = "../../"+imgpath;
						}else if(Settings.getSettings().getEnableoss() == 1){
							imgpath = fileUtil.getURL(imgpath);
						}
						answ_img += "<label style='cursor:pointer;' for='surveyAnsw_"+ quesId+ "_"+ iid+ "' onclick='choosecheckimgword("+framestyle+","+ quesId+ ","+ iid+ ",&quot;surveyAnsw_"+ quesId+ "_"+ iid+ "&quot;,&quot;"+filepath+"&quot;);'><img class='phoneansw_img'  src='"+imgpath+"'></label> ";
					}
					if(fillInAir == 1){
						strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
					}
					break;
				case 2: // 问答
					String inputWidth = "100%";
					int inputHeight = NumberUtil.getInt((String)confMap.get("inputHeight"));
					if(inputHeight>0){
						option = "<textarea id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId + "_content' style='width:" + inputWidth + ";height:" + inputHeight + "px;'></textarea>";
					}else{
						option = "<input id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId + "_content' class='dashed' style='width:" + inputWidth + "px'/>";
					}
					break;
				case 6: // 测评单选
					option = "<input  class='magic-radio' id='surveyAnsw_"+ quesId + "_" + iid + "' name='surveyAnsw"+ quesId + "' type='radio' style='vertical-align: middle;margin-bottom: 4px;' onclick='otherChange0("+ quesId + "," + iid + ");' value='" + iid+ "'/>";
					if (StringUtil.isNotEmpty(answimngname)) {
						String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
						if(Settings.getSettings().getEnableoss() == 0){
							imgpath = "../../"+imgpath;
						}else if(Settings.getSettings().getEnableoss() == 1){
							imgpath = fileUtil.getURL(imgpath);
						}
						answ_img+="<label style='cursor:pointer;' onclick='chooseimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);' for='surveyAnsw_" + quesId + "_" + iid+"' >" +"<img class='phoneansw_img' style='cursor:pointer;' src='"+imgpath+"'></label>";
					}
					if(fillInAir == 1){
						strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"' />";
					}
					break;
				case 7: // 多选
					option = "<input id='surveyAnsw_"+ quesId+ "_"+ iid+ "' name='surveyAnsw"+ quesId+ "'style='vertical-align: middle;margin-bottom: 4px;"+ "' type='checkbox' onclick='otherChange("+ quesId + "," + iid + ");' value='" + iid+ "' />";
					if(ismustfill == 1){
						option +="<input id='surveyAnswMin_"+iid+"' type='hidden' value='"+selectmin+"'><input id='surveyAnswMax_"+iid+"' type='hidden' value='"+selectmax+"'>";
					}
					if (StringUtil.isNotEmpty(answimngname)) {
						String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
						if(Settings.getSettings().getEnableoss() == 0){
							imgpath = "../../"+imgpath;
						}else if(Settings.getSettings().getEnableoss() == 1){
							imgpath = fileUtil.getURL(imgpath);
						}
						answ_img += "<label style='cursor:pointer;' for='surveyAnsw_"+ quesId+ "_"+ iid+ "' onclick='choosecheckimgword("+framestyle+","+ quesId+ ","+ iid+ ",&quot;surveyAnsw_"+ quesId+ "_"+ iid+ "&quot;,&quot;"+filepath+"&quot;);'><img class='phoneansw_img'  src='"+imgpath+"'></label> ";
					}
					if(fillInAir == 1){
						strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
					}
					break;
				}
			}else if(pageType == 1) {
				if (quesType == 0 || quesType == 1) {
					int basePointSum = NumberUtil.getInt((String) confMap.get("basePointSum"));
					String[][] currentData = null;
					if (answEn.getIid() != null) {
						currentData = countService.getCurrentData(iid,quesId, answEn.getBasepoint(), basePointSum);
						tag.put("selectcount", "（"+currentData[0][0]+"票）");
						tag.put("percent", currentData[0][1] + "%");
					}
					if (other) {
						answTitle = "<a href='"+ BaseInfo.getContextPath()+ "/front/dczj/show_otheransw.do?quesid="
								+ quesId+ "&quesType="+ quesType+ "' target='_blank' style='color:blue;text-decoration:underline;'>"+ answTitle + "</a>";
					}
					if(StringUtil.isNotEmpty(answEn.getAllowfillinair()+"") && StringUtil.equals(answEn.getAllowfillinair()+"", "1") && isopencontent == 1){
						answTitle = "<a href='"+BaseInfo.getContextPath()+"/front/dczj/show_otheransw.do?quesid=" + quesId +"&quesType="+quesType+ "&answid="+answEn.getIid()+
						"' target='_blank' style='color:blue;text-decoration:underline;'>" + answTitle + "</a>";
					}
				} else {
					answTitle = "<input type='button'  value='查看答案' onclick='window.open(\""+ BaseInfo.getContextPath()+ "/front/dczj/show_otheransw.do?quesid="+ quesId + "&quesType=" + quesType + "\")'/>";
				}
			}
			String answnote = answEn.getAnswnote();
				if (framestyle > 0 && quesType == 0) {
					answTitle = "<label style='cursor:pointer;' title='"+answnote+"' onclick='chooseimgword("+framestyle+","+ quesId+ ","+ iid+ ",&quot;surveyAnsw_"+ quesId+ "_"+ iid+ "&quot;,&quot;"+filepath+"&quot;);' for='surveyAnsw_"+ quesId+ "_"+ iid+ "'>"+ answTitle+ "</label>";
				} else if (framestyle > 0 && quesType == 1) {
					answTitle = "<label style='cursor:pointer;' title='"+answnote+"' for='surveyAnsw_"+ quesId+ "_"+ iid+ "' onclick='choosecheckimgword("+framestyle+","+ quesId+ ","+ iid+ ",&quot;surveyAnsw_"+ quesId+ "_"+ iid+ "&quot;,&quot;"+filepath+"&quot;);' >" + answTitle + "</label>";
				} else {
					answTitle = "<label style='cursor:pointer;' title='"+answnote+"' for='surveyAnsw_"+ quesId+ "_"+ iid+ "'>"+ answTitle+ "</label>";
				}
			answTitle = answTitle + "&nbsp;" + strOther;
			tag.put("title", answTitle);
			tag.put("option", option);
			tag.put("answ_img", answ_img);
			if (k != 0) {
				tag.put("answnumber", k + "");
			}
			break;
		}
		return tag;
	}

	public String getResultPage(Dczj_Setting settingEn, Style styleEn, TitleInfo titleInfo,
			HashMap<String, Object> confMap, DisplayConfig displayConfig, int ishtml) {
		String temp = null;
		String dczjid = titleInfo.getIid()+"";
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,2); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			temp = FileUtil.readFileToString(temp);
		}
		String style = null;
		if(styleEn != null ){
			style = styleEn.getPhonestyle();
		}
		if(style == null || style.length() == 0){ //如果没设置样式时，，设置默认样式
			String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/phoneresult.xml";
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
			confMap.put("istitlenumber",displayConfig.getIstitlenumber()+"");
			confMap.put("chooseframe_style",displayConfig.getChooseframe_style()+"");
			confMap.put("isopencontent",displayConfig.getIsopencontent()+"");
			confMap.put("contentsize",displayConfig.getContentsize());
			confMap.put("filepath",BaseInfo.getContextPath());
			if(ishtml == 1){
				confMap.put("surveyurl", BaseInfo.getContextPath()+"/jsurvey/questionnaire/phonejsurvey_"+titleInfo.getIid()+".html");
			}else{
				confMap.put("surveyurl",BaseInfo.getContextPath()+"/manager/dczj/dopreview.do?dczjid="+titleInfo.getIid()+"&pcstyle=1");
			}
			String AfterAffiContent = "调查已结束！";
			if(StringUtil.isEmpty(AfterAffiContent)){//没有设置默认
				AfterAffiContent = "调查已结束！";
			}
//			int sumcount = totalRecoService.findRecoIsHaveSubmitByDczjid(dczjid);
//			confMap.put("sumcount",sumcount);
		}
		strContent += this.parseFormFront(confMap,quesList,settingEn);
		strContent = this.parseTemplate(strContent,temp);
		return strContent;
	}

	public String getEndPage(String dczjid) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,2); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			temp = FileUtil.readFileToString(temp);
		}
		String content = "<div style='font-weight:bold;text-align:center;font-size:16px;padding-top:20px;padding-bottom:20px;'>问卷已经结束，谢谢参与！</div>";
		String strContent = this.parseTemplate(content, temp);
		return strContent;
	}

	public String getNoStartPage(String dczjid) {
		String temp = null;
		Template tpen = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid,2); //根据fomid查询列表页模板文件
		if(tpen != null ){ //如果不为空时
			temp = tpen.getContent();
			if(StringUtil.isEmpty(temp)){
				temp = tpen.getPath() + tpen.getName(); //获取到文件位置
				//判断是否有文件
				File file = new File(temp);
				if(!file.exists()){
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
				}
				temp = FileUtil.readFileToString(temp);
			}
		}else{
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			temp = FileUtil.readFileToString(temp);
		}
		String content = "<div style='font-weight:bold;text-align:center;font-size:16px;padding-top:20px;padding-bottom:20px;'>问卷尚未开始，敬请期待！</div>";
		String strContent = this.parseTemplate(content, temp);
		return strContent;
	}

}
