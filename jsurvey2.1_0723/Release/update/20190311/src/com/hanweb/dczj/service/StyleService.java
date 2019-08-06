package com.hanweb.dczj.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.file.IFileUtil;
import com.hanweb.common.util.xml.XmlDocument;
import com.hanweb.common.util.xml.XmlNode;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.dao.StyleDAO;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.TitleInfo;

import bsh.Interpreter;

public class StyleService {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private StyleDAO styleDAO;
	@Autowired
	private CountService countService;
	@Autowired
	@Qualifier("FileUtil")
	private IFileUtil fileUtil;
	/**
	 *  通过xml路径读取样式代码
	 * @param dauatlpage
	 * @return
	 */
	public String getStyleCodeByPath(String dauatlpage) {
		String styleCode = null;
		XmlDocument xml = new XmlDocument();
		File file = new File(dauatlpage);
		xml.read(file);
		if (xml.isExist("main/stylecode")) {
			XmlNode styleCode1 = xml.getXmlNode("main/stylecode");
			if (styleCode1 != null) {
				styleCode = styleCode1.getValue();
			}
		}
		return styleCode;
	}

	/**
	 *  解析题目区代码
	 * @param strContent
	 * @return
	 */
	public String parseDefaultQues(String strContent) {
		String tmqs = "<!--unitname=题目区-->";
		String tmqe = "<!--/unitname=题目区-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}

	/**
	 *  解析文字说明区代码
	 * @param strContent
	 * @return
	 */
	public String parseXiaoJieQues(String strContent) {
		String tmqs = "<!--unitname=小结区-->";
		String tmqe = "<!--/unitname=小结区-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析分页区代码
	 * @param strContent
	 * @return
	 */
	public String parseFenYeQues(String strContent) {
		String tmqs = "<!--unitname=分页区-->";
		String tmqe = "<!--/unitname=分页区-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析分页区代码
	 * @param strContent
	 * @return
	 */
	public String parseInfoQues(String strContent) {
		String tmqs = "<!--unitname=信息区-->";
		String tmqe = "<!--/unitname=信息区-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析单选题代码
	 * @param strContent
	 * @return
	 */
	public String parseSingleChoice(String strContent){
		String tmqs = "<!--unitname=单选题-->";
		String tmqe = "<!--/unitname=单选题-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析测评单选题代码
	 * @param strContent
	 * @return
	 */
	public String parseEvaluationSingleChoice(String strContent){
		String tmqs = "<!--unitname=测评单选题-->";
		String tmqe = "<!--/unitname=测评单选题-->";
		int begin = strContent.indexOf(tmqs)+21;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析多选题代码
	 * @param strContent
	 * @return
	 */
	public String parseMultipleChoice(String strContent){
		String tmqs = "<!--unitname=多选题-->";
		String tmqe = "<!--/unitname=多选题-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析测评多选题代码
	 * @param strContent
	 * @return
	 */
	public String parseEvaluationMultipleChoice(String strContent){
		String tmqs = "<!--unitname=测评多选题-->";
		String tmqe = "<!--/unitname=测评多选题-->";
		int begin = strContent.indexOf(tmqs)+21;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析多行文本框代码
	 * @param strContent
	 * @return
	 */
	public String parseInputQues(String strContent){
		String tmqs = "<!--unitname=文本题-->";
		String tmqe = "<!--/unitname=文本题-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	/**
	 *  解析单行文本框代码
	 * @param strContent
	 * @return
	 */
	public String parseAnswAndQues(String strContent){
		String tmqs = "<!--unitname=问答题-->";
		String tmqe = "<!--/unitname=问答题-->";
		int begin = strContent.indexOf(tmqs)+19;
		int end = strContent.indexOf(tmqe);
		strContent = strContent.substring(begin, end);
		return strContent;
	}
	
	public String parseQuesFront(String dczjid, HashMap<String, Object> confMap, Dczj_Setting settingEn,
			String content,String index) {
		String strContent = "";
		List<QuesInfo> quesList = quesInfoService.findQuesListByDczjid(dczjid);
		if(quesList !=null && quesList.size() >0){
			TitleInfo infoEn = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
			if(infoEn != null){
				confMap.put("surveytitle",infoEn.getTitlename());  //调查标题
				confMap.put("styleCode",content);
			}
			confMap.put("dczjid", dczjid + "");
			strContent += this.parseFormFront(confMap,quesList,index);
		}
		return strContent;
	}

	
	public String parseFormFront(HashMap<String, Object> confMap, List<QuesInfo> quesList,String index) {
		String strContent = (String)confMap.get("styleCode"); //样式效果代码
		strContent = strContent.replace("\n","");
		strContent = strContent.replace("\r","");
		String regEx1 = "<!--for.*?-->(?s:.*)<!--/for-->";
		Pattern p1 = Pattern.compile(regEx1,Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(strContent);
		if(m1.find()){  
			String temp1 = m1.group();
			String regEx2 = "<!--for.*?-->";
			Pattern p2 = Pattern.compile(regEx2,Pattern.CASE_INSENSITIVE);
			Matcher m2 = p2.matcher(temp1);
			m2.find();
			String temp2 = m2.group();
			temp1 = temp1.substring(temp2.length(),temp1.length()-11);
			String strFor = "";
			String strInner = "";
			String xiaojie = "";
			Iterator<QuesInfo> it = quesList.iterator(); //迭代器遍历调查题目
			HashMap<String,Object> quesConfMap = null;
			int k = 0;
			int quesType = 0;
			while(it.hasNext()){ 
				k += 1;
				quesConfMap = confMap;
				QuesInfo quesEn = (QuesInfo)it.next();
				//调查问题
				int quesId = 0;
				if(quesEn != null){
					quesType = quesEn.getType();
					quesId = quesEn.getIid();   
				}
				List<AnswInfo> answList = new ArrayList<AnswInfo>();
				if(quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7){//不是问答题，则获取答案列表  为单选或者多选时获取答案列表
					answList = answInfoService.getAnswListByQuesId(quesId);
				}
				if((quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7) && (answList == null || answList.size()== 0)) {
					continue;   //没有答案时跳出此次循环
				}
				//小节
				if(quesType == 3){	//文字描述
					xiaojie = StringUtil.getString(quesEn.getContent());

					String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
					String styleCode = "";
					String content = "";
					styleCode = this.getStyleCodeByPath(quespage);
					content = this.parseXiaoJieQues(styleCode);
					content = content.replaceAll("<!--xiaojie-->", xiaojie).replaceAll("<!--divquesId-->", quesId+"");
					strInner += content;  //题目类型为小节时。获取题目内容
					continue;
				}
				
				if(quesType == 4){
					String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
					String styleCode = "";
					String content = "";
					styleCode = this.getStyleCodeByPath(quespage);
					content = this.parseFenYeQues(styleCode);
					content = content.replaceAll("<!--divquesId-->", quesId+"");
					strInner += content;  //题目类型为小节时。获取题目内容
					continue;
				}
				
				if(quesType == 5){
					String quesname = StringUtil.getString(quesEn.getQuesname());
					String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
					String styleCode = "";
					String content = "";
					styleCode = this.getStyleCodeByPath(quespage);
					content = this.parseInfoQues(styleCode);
					content = content.replaceAll("<!--quesname-->", quesname).replaceAll("<!--divquesId-->", quesId+"");
					strInner += content;  //题目类型为小节时。获取题目内容
					continue;
				}
				
				if(quesType == 2){	//文本类型
					quesConfMap.put("inputWidth","" + quesEn.getTextinputwidth());
					quesConfMap.put("inputHeight","" + quesEn.getTextinputheight());
					answList.add(new AnswInfo());  //如果是文本类型加入一个答案实体
				}
				
				quesConfMap.remove("styleCode");
				quesConfMap.put("styleCode",temp1);
				quesConfMap.put("quesType",""+quesType);
				quesConfMap.put("quesId",""+quesId);
				quesConfMap.put("ismustfill",quesEn.getIsmustfill());
				quesConfMap.put("selectmin",quesEn.getMinselect());
				quesConfMap.put("selectmax",quesEn.getMaxselect());
				quesConfMap.put("col",""+quesEn.getCol());  //答案列数
				quesConfMap.put("showpublish",""+quesEn.getShowpublish());  //答案列数
				strFor = this.parseList(2,quesConfMap,answList,index); //解析答案列表
				HashMap<String,Object> tag = this.setTag(1,quesEn,confMap,false, k,index);//解析题目样式中的标签
				strInner += this.parseTag(strFor,tag);
			}
			strContent = m1.replaceFirst(strInner);
		}
		return strContent;
	}

	public String parseList(int iType,HashMap<String, Object> confMap,List<AnswInfo> surveyList,String index){	
		String styleCode = (String)confMap.get("styleCode");
		styleCode = styleCode.replaceAll("\r\n|\n","");
		String beforeContent = "";
		String middleContent = "";
		String afterContent = "";
		
		String regEx1 = "<!--for.*?-->(?s:.*?)<tr.*?>(?s:.*)</tr>(?s:.*?)<!--/for-->|<!--subfor.*?-->(?s:.*?)<tr.*?>(?s:.*)</tr>(?s:.*?)<!--/subfor-->";
		Pattern p1 = Pattern.compile(regEx1,Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(styleCode);

		String regEx2 = "<!--for.*?-->(?s:.*)<!--/for-->|<!--subfor.*?-->(?s:.*)<!--/subfor-->";
		Pattern p2 = Pattern.compile(regEx2,Pattern.CASE_INSENSITIVE);
		Matcher m2 = p2.matcher(styleCode);

		int iCol = NumberUtil.getInt((String)confMap.get("col"),1);
		boolean haveTr = false;
		if(m1.find()){
			String[] temp = p1.split(styleCode,-1);
			beforeContent = temp[0];
			afterContent = temp[1];
			middleContent = m1.group();
			haveTr = true;
		}else if(m2.find()){
			String[] temp = p2.split(styleCode,-1);
			beforeContent = temp[0];
			afterContent = temp[1];
			middleContent = m2.group();
			if(iCol>1){
				beforeContent += "<table width=\"100%\" border=\"0\" cellspadding=\"0\" cellspacing=\"0\">";
				afterContent = "</table>" + afterContent;
				middleContent = "<tr><td>" + middleContent + "</td></tr>";
				haveTr = true;
			}
		}else{
			return styleCode;
		}
		String trTag = "";
		String td = middleContent;
		if(haveTr){
			String regEx3 = "<tr.*?>(?s:.*)</tr>";
			Pattern p3 = Pattern.compile(regEx3,Pattern.CASE_INSENSITIVE);
			Matcher m3 = p3.matcher(middleContent);
			m3.find();
			String tr = m3.group();
			String regEx4 = "<tr.*?>";
			Pattern p4 = Pattern.compile(regEx4,Pattern.CASE_INSENSITIVE);
			Matcher m4 = p4.matcher(tr);
			m4.find();
			trTag = m4.group();
			td = tr;
			td = td.substring(trTag.length(),td.length()-5); 
		}
		middleContent = "";
		String temp;
		Iterator<AnswInfo> it = surveyList.iterator();
		int i = 0;
		
		while(it.hasNext()){
			HashMap<String,Object> tag = setTag(iType,it.next(),confMap,false,i,index);
			temp = this.parseTag(td,tag);
			if(iCol>1){
				if(i%iCol==0)
					middleContent += trTag + temp;
				else if((i+1)%iCol==0)
					middleContent += temp + "</tr>";
				else
					middleContent += temp;
			}else if(haveTr){
				middleContent += trTag + temp + "</tr>";
			}else{
				middleContent += temp;
			}
			i++;
		}
		String outCode = beforeContent + middleContent + afterContent;
		return outCode;
	}

	public HashMap<String, Object> setTag(int iType, Object listItem, HashMap<String, Object> confMap, boolean other,
			int k,String index) {
		HashMap<String, Object> tag = new HashMap<String, Object>();	
		String dczjtype = (String)confMap.get("dczjtype");
		
		switch(iType){
			case 1:		//问题列表
				QuesInfo quesEn = (QuesInfo)listItem;
				if(quesEn.getDczjtype() == 0) {
					tag.put("title",quesEn.getQuesname());
				}else {
					tag.put("title","");
				}
						
				String dczjid = (String)confMap.get("formId");
				
				String answid = answInfoService.findRightAnswIdByQuesid(quesEn.getIid());
			
				if(quesEn.getType() == 6) {
				  int accuracy = 0; //正确率
				  int countx = 0;
				  if(StringUtil.isNotEmpty(answid)) {
					  countx = countService.radioAnsListCount(dczjid,quesEn.getIid()+"", answid);	
				  }		  
				  int county = countService.getRecoConutByTime(quesEn.getIid()+"");
				  if(county > 0) {
					 accuracy = countx*100/county;
				  }
				   tag.put("accuracy","(正确率："+accuracy+"%)");	
				}
				
				if(quesEn.getType() == 7) {	
					int accuracy = 0; //正确率
					int countx = 0;
					if(StringUtil.isNotEmpty(answid)) {
						List<Integer> answList =  StringUtil.toIntegerList(answid);
						answid = StringUtil.getString(answList.get(0));
						countx = countService.checkRightBoxAnswCount(answid);
							
					}
					int county = countService.getCheckConutByTime(quesEn.getIid()+"");
					if(county > 0) {
						accuracy = countx*100/county;
					}
					 tag.put("accuracy","(正确率："+accuracy+"%)");
				}
				
				tag.put("divquesId",quesEn.getIid());
				int istitlenumber = NumberUtil.getInt((String)confMap.get("istitlenumber"));
				if(istitlenumber == 1 && k!=0) {
					tag.put("number",k+"、");
				}else {
					tag.put("number","");
				}
				int isshowscore = NumberUtil.getInt((String)confMap.get("isshowscore"));	
				if(isshowscore == 1 && (quesEn.getType() == 6 || quesEn.getType() == 7)) {
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
				if(quesEn.getType() == 6 || quesEn.getType() == 7) {
					String score = "<span style=\"font-size: 10px;color: orange;\">&nbsp;&nbsp;（分值："+quesEn.getQuesscore()+"分）</span>";
					tag.put("score",score);
				}
				tag.put("qmustfill", qmustfill);
				tag.put("clearoption",clearoption);
				tag.put("messageid","surveyMessage"+quesEn.getIid());
				tag.put("tableid","surveyTable"+quesEn.getIid());
				break;
			case 2:		//答案列表
				int pageType = NumberUtil.getInt((String)confMap.get("pageType"));//0:表单页 1:结果页
				AnswInfo answEn = (AnswInfo)listItem;
				int quesType = NumberUtil.getInt((String)confMap.get("quesType"));
				int quesId = NumberUtil.getInt((String)confMap.get("quesId"));
				int iid = NumberUtil.getInt(answEn.getIid());
				int fillInAir = NumberUtil.getInt(answEn.getAllowfillinair());
				int framestyle = NumberUtil.getInt((String)confMap.get("chooseframe_style"));
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
				if(pageType==0){
					switch(quesType){
						case 0:	//单选
							if(framestyle > 0) {
								option = "<input style='display: none'  id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId +"' type='radio' onclick='otherChange0("+quesId+","+iid+");' value='" + iid + "'>";
								option =option+"<img class='radio_img' id='img"+iid+"' src='"+filepath+"/resources/dczj/images/chooseframe/radio"+framestyle+".png' style='vertical-align:middle;cursor:pointer;padding-bottom: 2px;' onclick='chooseimg(this,"+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>";
							}else if(framestyle == 0) {
								option = "<input id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId +"' type='radio' onclick='otherChange0("+quesId+","+iid+");' value='" + iid + "' />";
							}
							if(StringUtil.isNotEmpty(answimngname)){
								String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
								if(Settings.getSettings().getEnableoss() == 0){
									imgpath = "../../"+imgpath;
								}else if(Settings.getSettings().getEnableoss() == 1){
									imgpath = fileUtil.getURL(imgpath);
								}
								answ_img+="<label style='cursor:pointer;' onclick='chooseimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);' for='surveyAnsw_" + quesId + "_" + iid+"' >" +"<img class='answ_img' style='cursor:pointer;' src='"+imgpath+"'></label>";
							}
							if(fillInAir == 1){
								strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
							}
							break;
						case 1:	//多选
							if(framestyle > 0){
								option = "<input style='display: none' id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId +"'style='width:16px; height:16px;vertical-align: middle;margin-bottom: 2px;' type='checkbox' onclick='otherChange("+quesId+","+iid+");' value='" + iid + "'/>";
										option =option+"<img class='check_img' id='checkimg"+iid+"' src='../../resources/dczj/images/chooseframe/check"+framestyle+".png' style='vertical-align:middle;cursor:pointer;' onclick='choosecheckimg(this,"+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>";
							}else{
								option = "<input id='surveyAnsw_" + quesId + "_" + iid + 
										"' name='surveyAnsw" + quesId +"'style='width:16px; height:16px;vertical-align: middle;margin-bottom: 2px;"+ 
										"' type='checkbox' onclick='otherChange("+quesId+","+iid+");' value='" + iid + "'/>";
							}
							if(StringUtil.isNotEmpty(answimngname)){
								String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
								if(Settings.getSettings().getEnableoss() == 0){
									imgpath = "../../"+imgpath;
								}else if(Settings.getSettings().getEnableoss() == 1){
									imgpath = fileUtil.getURL(imgpath);
								}
								answ_img+="<label style='cursor:pointer;' for='surveyAnsw_" + quesId + "_" + iid+"' onclick='choosecheckimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>" 
								        +"<img class='answ_img'  src='"+imgpath+"'></label> ";
							}
//						    option = "<input id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId +"'style='width:16px; height:16px;vertical-align: middle;margin-bottom: 2px;"+"' type='checkbox' value='" + iid + "' />";
							if(fillInAir == 1){
								strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
							}
							break;
						case 6:	//测评单选
							if(framestyle > 0) {
								option = "<input style='display: none'  id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId +"' type='radio' onclick='otherChange0("+quesId+","+iid+");' value='" + iid + "'>";
								option =option+"<img class='radio_img' id='img"+iid+"' src='"+filepath+"/resources/dczj/images/chooseframe/radio"+framestyle+".png' style='vertical-align:middle;cursor:pointer;padding-bottom: 2px;' onclick='chooseimg(this,"+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>";
							}else if(framestyle == 0) {
								option = "<input id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId +"' type='radio' onclick='otherChange0("+quesId+","+iid+");' value='" + iid + "' />";
							}
							if(answEn.getIsright()!=null) {
							  if(answEn.getIsright().equals("1") && !StringUtil.equals("1", index)) {
								  strOther="<span style=\"font-size: 10px;color: orange;\">&nbsp;&nbsp;(正确答案)</span>";
							   }
							 }
							if(StringUtil.isNotEmpty(answimngname)){
								String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
								if(Settings.getSettings().getEnableoss() == 0){
									imgpath = "../../"+imgpath;
								}else if(Settings.getSettings().getEnableoss() == 1){
									imgpath = fileUtil.getURL(imgpath);
								}
								answ_img+="<label style='cursor:pointer;' onclick='chooseimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);' for='surveyAnsw_" + quesId + "_" + iid+"' >" 
								        +"<img class='answ_img' style='cursor:pointer;' src='"+imgpath+"'></label>";
							}
							if(fillInAir == 1){
								strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
							}
							break;
						case 7:	//测评多选
							if(framestyle > 0){
								option = "<input style='display: none' id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId +"'style='width:16px; height:16px;vertical-align: middle;margin-bottom: 2px;' type='checkbox' onclick='otherChange("+quesId+","+iid+");' value='" + iid + "'/>";
										option =option+"<img class='check_img' id='checkimg"+iid+"' src='../../resources/dczj/images/chooseframe/check"+framestyle+".png' style='vertical-align:middle;cursor:pointer;' onclick='choosecheckimg(this,"+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>";
							}else{
								option = "<input id='surveyAnsw_" + quesId + "_" + iid + 
										"' name='surveyAnsw" + quesId +"'style='width:16px; height:16px;vertical-align: middle;margin-bottom: 2px;"+ 
										"' type='checkbox' onclick='otherChange("+quesId+","+iid+");' value='" + iid + "'/>";
							}
						    if(answEn.getIsright()!=null) {
						    if(answEn.getIsright().equals("1") && !StringUtil.equals("1", index)) {
						    	strOther="<span style=\"font-size: 10px;color: orange;\">&nbsp;&nbsp;(正确答案)</span>";
						    }
						    }
							if(StringUtil.isNotEmpty(answimngname)){
								String imgpath = "answimg/dczjid_"+ answEn.getDczjid()+ "/quesid_"+ quesId+ "/answid_"+ iid + "/" + answimngname;
								if(Settings.getSettings().getEnableoss() == 0){
									imgpath = "../../"+imgpath;
								}else if(Settings.getSettings().getEnableoss() == 1){
									imgpath = fileUtil.getURL(imgpath);
								}
								answ_img+="<label style='cursor:pointer;' for='surveyAnsw_" + quesId + "_" + iid+"' onclick='choosecheckimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>" 
								        +"<img class='answ_img'  src='"+imgpath+"'></label> ";
							}
							if(fillInAir == 1){
								strOther += "<input disabled='disabled' type='text' id='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir" + "' name='surveyAnsw_" + quesId + "_" + iid+ "_fillInAir' maxlength='"+contentsize+"'/>";
							}
							break;
						case 2:	//多行文本题
							int inputWidth = NumberUtil.getInt((String)confMap.get("inputWidth"));
							int inputHeight = NumberUtil.getInt((String)confMap.get("inputHeight"));
							if(inputHeight>0){
								option = "<textarea id='surveyAnsw_" + quesId + "_" + iid +"' name='surveyAnsw" + quesId + "_content' style='width:" + inputWidth + "px;height:" + inputHeight + "px;'></textarea>";
							}else{
								option = "<input id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId + "_content' class='dashed' style='width:" + inputWidth + "px'/>";
							}
							break;
//						case 5: //单行文本题
//							option = "<input id='surveyAnsw_" + quesId + "_" + iid + "' name='surveyAnsw" + quesId + "_content' class='" + textCss + "'/>";
					}
				}else if(pageType == 1){
					
					if("3".equals(dczjtype)) {
						if (quesType == 0 || quesType == 1 || quesType == 6 ||quesType == 7) {
							int basePointSum = NumberUtil.getInt((String) confMap.get("basePointSum"));
							String[][] currentData = null;
							if (answEn.getIid() != null) {
								currentData = countService.getCurrentData(iid,quesId, answEn.getBasepoint(), basePointSum);
								tag.put("selectcount", "("+currentData[0][0]+"次)");
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
						}
			
						
					}else {
						if (quesType == 0 || quesType == 1) {
							int basePointSum = NumberUtil.getInt((String) confMap.get("basePointSum"));
							String[][] currentData = null;
							if (answEn.getIid() != null) {
								currentData = countService.getCurrentData(iid,quesId, answEn.getBasepoint(), basePointSum);
								tag.put("selectcount", "("+currentData[0][0]+"票)");
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
						} 	
					}
					
					
					
					if(quesType == 2){
						answTitle = "<input type='button'  value='查看答案' onclick='window.open(\""+ BaseInfo.getContextPath()+ "/front/dczj/show_otheransw.do?quesid="+ quesId + "&quesType=" + quesType + "\")'/>";
					}
				
				}
				String answnote = answEn.getAnswnote();
				if(framestyle > 0 && (quesType == 0||quesType==6)) {
					answTitle = "<label style='cursor:pointer;width:16px;height:16px;' title='' for='surveyAnsw_" + quesId + "_" + iid+"' onclick='chooseimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>" + answTitle + "</label>";
				}else if(framestyle > 0 && (quesType == 1||quesType==7)){
					answTitle = "<label style='cursor:pointer;width:16px;height:16px;' title='' for='surveyAnsw_" + quesId + "_" + iid+"' onclick='choosecheckimgword("+framestyle+","+quesId+","+iid+",&quot;surveyAnsw_" + quesId + "_" + iid +"&quot;,&quot;"+filepath+"&quot;);'>" + answTitle + "</label>";
				}else {
					answTitle = "<label style='cursor:pointer;width:16px;height:16px;' title='' for='surveyAnsw_" + quesId + "_" + iid+"'>" + answTitle + "</label>";
				}
				answTitle = answTitle +"&nbsp;"+ strOther;
				tag.put("title",answTitle);
				tag.put("option",option);
				tag.put("answ_img",answ_img);
				if(k!=0){
					tag.put("answnumber",k+"");
				}
				break;
		}
		return tag;
	}
	
	public String parseTag(String strContent, HashMap<String, Object> tag) {
		if(strContent==null||strContent.trim().length()==0||tag==null||tag.size()==0)
			return "";

		Random rd = new Random();
		int x = rd.nextInt(100);  //从0-100生成一个随机整数
		String variableName = "temp" + x;
		
		String regEx0 = "<!--if test=\".*?\"-->|<!--if test='.*?'-->|<!--/if-->|<!--else-->|<!--/else-->";
		Pattern p0 = Pattern.compile(regEx0); 
		Matcher m0 = p0.matcher(strContent);
		while(m0.find()){
			String temp = m0.group();
			String key = temp.substring(1,temp.length()-1);
			strContent = strContent.replaceFirst(temp,"@" + key + "@");
		}
		
		String regEx = "<!--.*?-->";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(strContent);
		String[] aside = p.split(strContent);
		int i = 0;
		String tagKey;
		String rs = "";
		
		while(m.find()){
			if(aside.length>0)
				rs += aside[i++];
			tagKey = m.group().replaceFirst("<!--", "").replaceFirst("-->", "").trim();
			
			//时间格式化
			Pattern p1 = Pattern.compile(" format=\\\\\".*?\\\\\"");
			Matcher m1 = p1.matcher(tagKey);
			Pattern p2 = Pattern.compile(" format='.*?'");
			Matcher m2 = p2.matcher(tagKey);
			if(m1.find()){
				String strParam = m1.group();
				String format = strParam.replaceFirst(" format=\\\\\"","").replaceFirst("\\\\\"","");
				tagKey = tagKey.substring(0,m1.start());
				if(tag.get(tagKey)!=null){
					String temp = (String)tag.get(tagKey);
					rs += this.parseTime(temp,format);
				}
			}else if(m2.find()){
				String strParam = m2.group();
				String format = strParam.replaceFirst(" format='","").replaceFirst("'","");
				tagKey = tagKey.substring(0,m2.start());
				if(tag.get(tagKey)!=null){
					String temp = (String)tag.get(tagKey);
					rs += this.parseTime(temp,format);
				}
			}else if(tag.get(tagKey)!=null)
				rs += tag.get(tagKey);
		}
		if(aside.length>0)
			rs += aside[aside.length-1];
		//解析if、else
		rs = "String " + variableName + ";" + this.parseCond(rs,variableName);
		
		Interpreter in = new Interpreter();
		try{
			rs = (String)in.eval(rs);
		}catch(Exception e){
		}
		return rs;
	}

	public String parseTime(String date, String format) {
		Date dt = DateUtil.stringtoDate(date,"yyyy-MM-dd HH:mm:ss");
		String strDate = "";
		try{
			strDate = new SimpleDateFormat(format).format(dt);
		}catch(Exception e){
		}
		return strDate;
	}

	public String parseCond(String strContent, String variableName) {
		if(strContent==null||strContent.trim().length()==0)
			return "";
		strContent = strContent.replaceAll("\r\n|\n","");
		String regEx = "@!--if .*?--@|@!--/if--@@!--else--@|@!--/if--@|@!--/else--@";
		Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(strContent);
		String[] aside = p.split(strContent,-1);
		int i = 0;
		String rs = "";
		rs += variableName + " = \"" + aside[i].replaceAll("\"","\\\\\"") + "\";";
		String item;
		while(m.find()){
			item = m.group();
			boolean isIf = item.indexOf("@!--if")>-1;
			if(isIf){
				boolean condition = false;
				String regEx1 = "(test=\".*?\")|(test='.*?')";
				p = Pattern.compile(regEx1,Pattern.CASE_INSENSITIVE);
				Matcher m1 = p.matcher(item);
				while(m1.find()){
					String temp = m1.group();
					temp = temp.substring(6,temp.length()-1);
					if(temp.indexOf("==")!=-1){
						String[] val = temp.split("==",2);
						if(val[0].equals(val[1]))
							condition = true;
					}else if(temp.trim().equals("true"))
						condition = true;
				}
				rs += "if(" + condition + "){";
			}else if(item.indexOf("@!--/if--@@!--else--@")>-1){
				rs += "}else{";
			}else{
				rs += "}";
			}

			rs += variableName + " += \"" + aside[++i].replaceAll("\"","\\\\\"") + "\";";
		}
		if(rs.length()==0)
			return strContent;
		return rs;
	}

	public String parseQues(String content, int quesId) {
		content = content.replace("\n","");
		content = content.replace("\r","");
		String regEx1 = "<!--for.*?-->(?s:.*)<!--/for-->";
		Pattern p1 = Pattern.compile(regEx1,Pattern.CASE_INSENSITIVE);
		Matcher m1 = p1.matcher(content);
		List<AnswInfo> answList = answInfoService.getAnswListByQuesId(quesId);
		String strContent = "";
		if(m1.find()){  
			String text = "";
			String temp1 = m1.group();
			String regEx2 = "<!--for.*?-->";
			Pattern p2 = Pattern.compile(regEx2,Pattern.CASE_INSENSITIVE);
			Matcher m2 = p2.matcher(temp1);
			m2.find();
			String temp2 = m2.group();
			temp1 = temp1.substring(temp2.length(),temp1.length()-11);
			int i = 0;
			for(AnswInfo en : answList){
				i++;
				String allowFillInAir = "";
				if(en.getAllowfillinair() == null){
					allowFillInAir = "0";
				}else{
					allowFillInAir = en.getAllowfillinair() + "";
				}
				String answimgname = "";
				String answdisplay = "none";
				if(StringUtil.isNotEmpty(en.getAnswimgname())){
					answimgname = en.getAnswimgname();
					answdisplay = "";
				}
				text = temp1.replaceAll("<!--trAnswId-->", en.getIid()+"").replaceAll("<!--tdNumber-->", i+"")
				            .replaceAll("<!--tdAnswName-->", StringUtil.getString(en.getAnswname())).replaceAll("<!--allowFillInAir-->", allowFillInAir)
				            .replaceAll("<!--basepoint-->", en.getBasepoint()+"").replaceAll("<!--answ_imgname-->", answimgname)
				            .replaceAll("<!--answ_display-->", answdisplay).replaceAll("<!--tdAnswNote-->", StringUtil.getString(en.getAnswnote()));
				strContent += text;
			}
		}
		return strContent;
	}

	/**
	 * 通过dczjId获取style实体
	 * @param dczjid
	 * @return
	 */
	public Style getEntityByDczjid(String dczjid) {
		return styleDAO.getEntityByDczjid(dczjid);
	}
}
