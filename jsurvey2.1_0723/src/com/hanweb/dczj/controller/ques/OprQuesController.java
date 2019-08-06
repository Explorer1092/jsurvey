package com.hanweb.dczj.controller.ques;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.file.IFileUtil;
import com.hanweb.common.util.file.LocalFileUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.LimitLoginUser;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.StyleService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class OprQuesController {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private StyleService styleService;
	
	@Autowired
	@Qualifier("FileUtil")
	private IFileUtil fileUtil;
	@Autowired
	@Qualifier("LocalFileUtil")
	private LocalFileUtil localFileUtil;
	
	/**
	 * 新增题目
	 * @param dczjid
	 * @param type
	 * @return
	 */
	@RequestMapping("addques")
	@ResponseBody
	public JsonResult submitAddQuesAndAnsw(int dczjid, int type) {
		JsonResult jsonResult = JsonResult.getInstance();
		int newquesid = quesInfoService.addEn(dczjid, type);
		if(newquesid > 0) {
			jsonResult.set(ResultState.ADD_SUCCESS);
			jsonResult.addParam("quesid", newquesid);
			if(type == 0 || type == 1||type==6||type==7||type==8) {
				AnswInfo answEn = new AnswInfo();
				answEn.setOrderid(0);
				answEn.setQuesid(newquesid);
				answEn.setDczjid(dczjid);
				answEn.setBasepoint(0);
				answEn.setState(0);
				answEn.setAnswimgname("");;
				answEn.setAllowfillinair(0);
				answEn.setAnswnote("");
				int forsize = 2;
				if (type==8) {
					forsize = 3;
				}
				for(int i = 0; i < forsize; i++) {
					if (i == 0) {
						answEn.setAnswname("选项1");
					} else if (i == 1) {
						answEn.setAnswname("选项2");
					}
					if (type==8&&i == 2) {
						answEn.setAnswname("选项3");
					}
					int answid = answInfoService.add(answEn);
					if(answid > 0) {
						jsonResult.addParam("answid" + i, answid);
					}
				} 
			}else {
				jsonResult.addParam("answid0", 0);
				jsonResult.addParam("answid1", 0);
			}
		}else {
			jsonResult.set(ResultState.ADD_FAIL);
		}
		int num = quesInfoService.findNum(NumberUtil.getInt(dczjid));
		int sumscore = quesInfoService.findScore(NumberUtil.getInt(dczjid));
		jsonResult.addParam("num", num);
		jsonResult.addParam("sumscore", sumscore);
		titleInfoService.setUpdateHtml(dczjid+"", 0);
		return jsonResult;
	}
	
	/**
	 * 获取新增题目的HTML样式
	 * @param quesid
	 * @return
	 */
	@RequestMapping("addqueshtml")
	@ResponseBody
	public JsonResult addqueshtml(int quesid) {
		JsonResult jsonResult = JsonResult.getInstance();
		String strContent = "";
		String index = "0";
		String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
		String content = styleService.getStyleCodeByPath(quespage);
		content = styleService.parseDefaultQues(content);
		List<QuesInfo> quesList = quesInfoService.findQuesListByIid(quesid);
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		if(quesList !=null && quesList.size() >0){
			confMap.put("styleCode",content);
			strContent = styleService.parseFormFront(confMap,quesList,index);
		}
		jsonResult.setMessage(strContent);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	
	/**
	 * 问卷编辑页面
	 * @param dczjid
	 * @param quesid
	 * @return
	 */
	@RequestMapping("editques")
	@ResponseBody
	public JsonResult edit(Integer dczjid,Integer quesid) {
		JsonResult jsonResult = JsonResult.getInstance();
		QuesInfo quesInfoEn = quesInfoService.findQuesEntityByIid(quesid);
		if(quesInfoEn == null){
			quesInfoEn = new QuesInfo();
		}
		int type = quesInfoEn.getType();
		
		String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/surveyansw.xml";
		String content = styleService.getStyleCodeByPath(quespage);
		jsonResult.addParam("mustfill", quesInfoEn.getIsmustfill());
		jsonResult.addParam("showpublish", quesInfoEn.getShowpublish());
		jsonResult.addParam("dczjtype", quesInfoEn.getDczjtype());
		jsonResult.addParam("type", type);
		if(type == 0){   //单选题
			content = styleService.parseSingleChoice(content);
			String strContent = "";
			String beforeContent = "";
			String middleContent = "";
			String afterContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->");
			int afterIndex = content.indexOf("<!--/for-->") + 11;
			beforeContent = content.substring(0,beforeIndex);
			afterContent = content.substring(afterIndex);
			middleContent = styleService.parseQues(content,quesid);
			
			strContent = beforeContent + middleContent + afterContent;
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->",quesInfoEn.getQuesname()).replaceAll("<!--quesCol-->", quesInfoEn.getCol()+"").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--divQuesNote-->", StringUtil.getString(quesInfoEn.getNote()));
			jsonResult.setMessage(strContent);
			List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesid);
			if(answIdList != null && answIdList.size()>0){
				String json = "";
				for(Integer answid : answIdList ){
					json += "{\"answId\":\""+answid+"\"}," ;
				}
				if(StringUtil.isNotEmpty(json)){
					json = json.substring(0,json.length()-1);
					json = "[" +json+"]" ;
				}
				jsonResult.addParam("num",answIdList.size());
				jsonResult.addParam("answJson",json);
			}
			
		}else if(type == 1){  //多选题
			content = styleService.parseMultipleChoice(content);
			String strContent = "";
			String beforeContent = "";
			String middleContent = "";
			String afterContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->");
			int afterIndex = content.indexOf("<!--/for-->") + 11;
			beforeContent = content.substring(0,beforeIndex);
			afterContent = content.substring(afterIndex);
			middleContent = styleService.parseQues(content,quesid);
			
			strContent = beforeContent + middleContent + afterContent;
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname()).replaceAll("<!--quesCol-->", quesInfoEn.getCol()+"").replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--quesMin-->", quesInfoEn.getMinselect()+"").replaceAll("<!--quesMax-->", quesInfoEn.getMaxselect()+"").replaceAll("<!--divQuesNote-->", StringUtil.getString(quesInfoEn.getNote()));
			jsonResult.setMessage(strContent);
			List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesid);
			if(answIdList != null && answIdList.size()>0){
				String json = "";
				for(Integer answid : answIdList ){
					json += "{\"answId\":\""+answid+"\"}," ;
				}
				if(StringUtil.isNotEmpty(json)){
					json = json.substring(0,json.length()-1);
					json = "[" +json+"]" ;
				}
				jsonResult.addParam("num",answIdList.size());
				jsonResult.addParam("answJson",json);
			}
		}else if(type == 2){ //多行文本题
			content = styleService.parseAnswAndQues(content);
			String strContent = content;
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname())
			                       .replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--quesMin-->", quesInfoEn.getMinselect()+"")
			                       .replaceAll("<!--quesMax-->", quesInfoEn.getMaxselect()+"").replaceAll("<!--textinputwidth-->", quesInfoEn.getTextinputwidth()+"")
			                       .replaceAll("<!--textinputheight-->", quesInfoEn.getTextinputheight()+"").replaceAll("<!--divQuesNote-->", StringUtil.getString(quesInfoEn.getNote()));
			
			jsonResult.setMessage(strContent);
		}else if(type == 3){ //文字说明
			content = styleService.parseXiaoJieQues(content);
			content = content.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divContent-->", quesInfoEn.getContent());
			jsonResult.setMessage(content);
		}else if(type == 5) {
			content = styleService.parseInputQues(content);
			String strContent = content;
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname())
			                       .replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--quesMin-->", quesInfoEn.getMinselect()+"")
			                       .replaceAll("<!--quesMax-->", quesInfoEn.getMaxselect()+"").replaceAll("<!--textinputwidth-->", quesInfoEn.getTextinputwidth()+"")
			                       .replaceAll("<!--textinputheight-->", quesInfoEn.getTextinputheight()+"").replaceAll("<!--divQuesNote-->", StringUtil.getString(quesInfoEn.getNote()));
			jsonResult.addParam("validaterules",quesInfoEn.getValidaterules());
			jsonResult.setMessage(strContent);
		}else if(type == 8) {
			content = styleService.selectInputQues(content);
			String strContent = "";
			String beforeContent = "";
			String middleContent = "";
			String afterContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->");
			int afterIndex = content.indexOf("<!--/for-->") + 11;
			beforeContent = content.substring(0,beforeIndex);
			afterContent = content.substring(afterIndex);
			middleContent = styleService.parseQues(content,quesid);
			
			strContent = beforeContent + middleContent + afterContent;
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"")
					.replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname())
					.replaceAll("<!--quesCol-->", quesInfoEn.getCol()+"")
					.replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"")
					.replaceAll("<!--quesMin-->", quesInfoEn.getMinselect()+"")
					.replaceAll("<!--quesMax-->", quesInfoEn.getMaxselect()+"")
					.replaceAll("<!--divQuesNote-->", StringUtil.getString(quesInfoEn.getNote()));
			jsonResult.setMessage(strContent);
			List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesid);
			if(answIdList != null && answIdList.size()>0){
				String json = "";
				for(Integer answid : answIdList ){
					json += "{\"answId\":\""+answid+"\"}," ;
				}
				if(StringUtil.isNotEmpty(json)){
					json = json.substring(0,json.length()-1);
					json = "[" +json+"]" ;
				}
				jsonResult.addParam("num",answIdList.size());
				jsonResult.addParam("answJson",json);
			}
		}else if(type == 6) {//测评单选题
			content = styleService.parseEvaluationSingleChoice(content);
			String strContent = "";
			String beforeContent = "";
			String middleContent = "";
			String afterContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->");
			int afterIndex = content.indexOf("<!--/for-->") + 11;
			beforeContent = content.substring(0,beforeIndex);
			afterContent = content.substring(afterIndex);
			middleContent = styleService.parseQues(content,quesid);
			
			strContent = beforeContent + middleContent + afterContent;
			
			String quesscore = "5";
			if(NumberUtil.getInt(quesInfoEn.getQuesscore()) != 0) {
				quesscore = StringUtil.getString(quesInfoEn.getQuesscore());
			}
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname()).replaceAll("<!--quesCol-->", quesInfoEn.getCol()+"").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--quesscore-->", quesscore);
			jsonResult.setMessage(strContent);
			List<AnswInfo> answList = answInfoService.getAnswListByQuesId(quesid);
			if(answList != null && answList.size()>0){
				String rightansw= "";
				String json = "";
				String isright="";
				for(AnswInfo answEn : answList ){
					isright = answEn.getIsright();
					json += "{\"answId\":\""+answEn.getIid()+"\"}," ;
					if(StringUtil.isNotEmpty(answEn.getIsright()) && StringUtil.equals(answEn.getIsright(), "1")) {
						rightansw = StringUtil.getString(answEn.getIid());
					}
				}
				if(StringUtil.isNotEmpty(json)){
					json = json.substring(0,json.length()-1);
					json = "[" +json+"]" ;
				}
				jsonResult.addParam("rightansw",rightansw);
				jsonResult.addParam("isright", isright);
				jsonResult.addParam("num",answList.size());
				jsonResult.addParam("answJson",json);
			}
			
		}else if(type == 7) {//测评多选题
			content = styleService.parseEvaluationMultipleChoice(content);
			String strContent = "";
			String beforeContent = "";
			String middleContent = "";
			String afterContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->");
			int afterIndex = content.indexOf("<!--/for-->") + 11;
			beforeContent = content.substring(0,beforeIndex);
			afterContent = content.substring(afterIndex);
			middleContent = styleService.parseQues(content,quesid);
			
			strContent = beforeContent + middleContent + afterContent;
			String quesscore = "5";
			if(NumberUtil.getInt(quesInfoEn.getQuesscore()) != 0) {
				quesscore = StringUtil.getString(quesInfoEn.getQuesscore());
			}
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--divQuesName-->", quesInfoEn.getQuesname()).replaceAll("<!--quesCol-->", quesInfoEn.getCol()+"").replaceAll("<!--mustfill-->", quesInfoEn.getIsmustfill()+"").replaceAll("<!--quesMin-->", quesInfoEn.getMinselect()+"").replaceAll("<!--quesMax-->", quesInfoEn.getMaxselect()+"").replaceAll("<!--quesscore-->", quesscore);
			jsonResult.setMessage(strContent);
			List<AnswInfo> answList = answInfoService.getAnswListByQuesId(quesid);
			if(answList != null && answList.size()>0){
				String json = "";
				String rightanswjson = "";
				for(AnswInfo answEn : answList ){
					json += "{\"answId\":\""+answEn.getIid()+"\"},";
					if(StringUtil.isNotEmpty(answEn.getIsright()) && StringUtil.equals(answEn.getIsright(), "1")) {
						rightanswjson += "{\"answId\":\""+answEn.getIid()+"\"},";
					}
				}
				if(StringUtil.isNotEmpty(json)){
					json = json.substring(0,json.length()-1);
					json = "[" +json+"]" ;
				}
				if(StringUtil.isNotEmpty(rightanswjson)){
					rightanswjson = rightanswjson.substring(0,rightanswjson.length()-1);
					rightanswjson = "[" +rightanswjson+"]" ;
				}
				jsonResult.addParam("num",answList.size());
				jsonResult.addParam("answJson",json);
				jsonResult.addParam("rightanswjson",rightanswjson);
			}
		
		}
		titleInfoService.setUpdateHtml(dczjid + "", 0);
		
		String userIp = IpUtil.getIp();
		String [] ips = userIp.split(",");
		if (ips != null && ips.length > 0){ //防止双IP
		    userIp = StringUtil.getString(ips[0]);
		}
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		String quesname = quesInfoEn.getQuesname();
		if(quesname.indexOf("<img") != -1) {
			quesname = StringUtil.removeHTML(quesname)+"[图片]";
		}
		quesname = StringUtil.removeHTML(quesname).replace("&nbsp;"," ");
		LogRecorder.record(LogEntity.getInstance().setModelName("在线调查问题").setFunctionName("编辑")
				.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("编辑调查表问题，调查表ID："+quesInfoEn.getDczjid()+"，问题："+quesname));
		return jsonResult.setSuccess(true);
	}
	
	/**
	 * 问题删除
	 * @param quesid
	 * @return
	 */
	@RequestMapping("removeques")
	@ResponseBody
	public JsonResult removeques(String dczjid,String quesid) {
		JsonResult jsonResult = JsonResult.getInstance();
		if(StringUtil.isEmpty(quesid)) {
			return jsonResult.set(ResultState.REMOVE_FAIL);
		}
		quesid = StringUtil.getSafeString(quesid);
		boolean bl = quesInfoService.delete(quesid);// 删除题目
		int num = quesInfoService.findNum(NumberUtil.getInt(dczjid));
		int sumscore = quesInfoService.findScore(NumberUtil.getInt(dczjid));
		jsonResult.addParam("num", num);
		jsonResult.addParam("sumscore", sumscore);
		titleInfoService.setUpdateHtml(dczjid, 0);
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
	}
	
	
	@RequestMapping("addquessubmit")
	@ResponseBody
	public String submitAdd(String rightAnsw) {
		Script script = Script.getInstanceOnly();
		String quesName = SpringUtil.getRequest().getParameter("quesName");
		String col = SpringUtil.getRequest().getParameter("col");
		String mustfill = SpringUtil.getRequest().getParameter("mustfill");
		String quesid = SpringUtil.getRequest().getParameter("quesId");
		String i_min = SpringUtil.getRequest().getParameter("i_min");
		String i_max = SpringUtil.getRequest().getParameter("i_max");
		String textinputwidth = SpringUtil.getRequest().getParameter("textinputwidth");
		String textinputheight = SpringUtil.getRequest().getParameter("textinputheight");
		String quesContent = SpringUtil.getRequest().getParameter("quesContent");
		String quesNote = SpringUtil.getRequest().getParameter("quesNote");
		String showpublish = SpringUtil.getRequest().getParameter("showpublish");
		String dczjtype = SpringUtil.getRequest().getParameter("dczjtype");
		String validaterules = SpringUtil.getRequest().getParameter("validaterules");
		String quesscore = SpringUtil.getRequest().getParameter("quesscore");
		int quesId = NumberUtil.getInt(quesid);
		QuesInfo surveyQues = quesInfoService.findQuesEntityByIid(quesId);
		
		
		Cache cache = CacheManager.getInstance("dczj_score");

		if(surveyQues != null) {
			cache.put("dczj_score_"+surveyQues.getDczjid(), "");
			cache.put("dczj_scorenum_"+surveyQues.getDczjid(), "");
				
		}
		
		
		boolean bl = true;
		if(surveyQues != null){
			if(StringUtil.isNotEmpty(col)){
				surveyQues.setCol(NumberUtil.getInt(col));
			}
			if(StringUtil.isNotEmpty(quesName)){
				quesName = quesName.replaceAll("<p>", "").replaceAll("</p>", "");
				surveyQues.setQuesname(quesName);
			}
			if(StringUtil.isNotEmpty(mustfill) && StringUtil.equals(mustfill, "1")){
				surveyQues.setIsmustfill(1);
			}else {
				surveyQues.setIsmustfill(0);
			}
			if(StringUtil.isNotEmpty(i_min)){
				surveyQues.setMinselect(NumberUtil.getInt(i_min));
			}
            if(StringUtil.isNotEmpty(i_max)){
            	surveyQues.setMaxselect(NumberUtil.getInt(i_max));
			}
            if(StringUtil.isNotEmpty(textinputwidth)){
            	surveyQues.setTextinputwidth(NumberUtil.getInt(textinputwidth));
            }
            if(StringUtil.isNotEmpty(textinputheight)){
            	surveyQues.setTextinputheight(NumberUtil.getInt(textinputheight));
            }
            if(StringUtil.isNotEmpty(quesContent)){
            	surveyQues.setContent(quesContent);
            }
            if(StringUtil.isNotEmpty(quesNote)) {
            	surveyQues.setNote(quesNote);
            }
            if(StringUtil.isNotEmpty(quesscore)){
            	surveyQues.setQuesscore(NumberUtil.getInt(quesscore));
            }
            if(StringUtil.isNotEmpty(showpublish) && StringUtil.equals(showpublish, "1")) {
            	surveyQues.setShowpublish(1);;
            }else {
            	surveyQues.setShowpublish(0);;
            }
            if(StringUtil.isNotEmpty(dczjtype) && StringUtil.equals(dczjtype, "1")) {
            	surveyQues.setDczjtype(1);
            }else {
            	surveyQues.setDczjtype(0);
            }
            if(StringUtil.isNotEmpty(validaterules)) {
            	surveyQues.setValidaterules(NumberUtil.getInt(validaterules));
            }
			bl = quesInfoService.modify(surveyQues);
			if(bl){
				List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesId);
				if(answIdList != null && answIdList.size()>0){
					int type = surveyQues.getType();
					if(type == 7) {
						rightAnsw = ","+rightAnsw+",";
					}
					for(Integer answid : answIdList){
						AnswInfo surveyAnsw = answInfoService.getEntity(answid);
						String answName =  SpringUtil.getRequest().getParameter("tdAnswName_"+answid);
						String allowFillInAir = SpringUtil.getRequest().getParameter("allowFillInAir_"+answid);
						String basepoint = SpringUtil.getRequest().getParameter("basepoint_"+answid);
						String answImgName = SpringUtil.getRequest().getParameter("answ_imgname_"+answid);
						String answNote = SpringUtil.getRequest().getParameter("tdAnswNote_"+answid);
						String defaultvalue = SpringUtil.getRequest().getParameter("defaultvalue");
						if(StringUtil.isNotEmpty(answName)) {
							answName = answName.replaceAll("<p>", "").replaceAll("</p>", "");
						}
						surveyAnsw.setAnswname(answName);
						surveyAnsw.setAnswimgname(answImgName);
						surveyAnsw.setAnswnote(answNote);
						if(StringUtil.isNotEmpty(allowFillInAir)){
							surveyAnsw.setAllowfillinair(NumberUtil.getInt(allowFillInAir));
				        }
						if(StringUtil.isNotEmpty(basepoint)){
							surveyAnsw.setBasepoint(NumberUtil.getInt(basepoint));
				        }
						if(type == 6 && answid == NumberUtil.getInt(rightAnsw)) {
							surveyAnsw.setIsright("1");
						}else if(type == 6 && answid != NumberUtil.getInt(rightAnsw)){
							surveyAnsw.setIsright("0");
						}if(type == 7) {
                            String newanswid = ","+answid+",";
                            if(rightAnsw.indexOf(newanswid) >= 0) {
                            	surveyAnsw.setIsright("1");
                            }else {
                            	surveyAnsw.setIsright("0");
                            }
						}
						Integer defaultvalueint = NumberUtil.getInt(defaultvalue);
						if (type == 8&&answid.equals(defaultvalueint)) {
							surveyAnsw.setDefaultvalue(1);
						}else {
							surveyAnsw.setDefaultvalue(0);
						}
						bl = answInfoService.modify(surveyAnsw);
						if(bl){
							titleInfoService.setUpdateHtml(surveyAnsw.getDczjid()+"",0);
							String userIp = IpUtil.getIp();
							String [] ips = userIp.split(",");
							if (ips != null && ips.length > 0){ //防止双IP
							    userIp = StringUtil.getString(ips[0]);
							}
							CurrentUser currentUser = UserSessionInfo.getCurrentUser();
							String answname = surveyAnsw.getAnswname();
							if(answname.indexOf("<img") != -1) {
								answname = StringUtil.removeHTML(answname)+"[图片]";
							}
							answname = StringUtil.removeHTML(answname).replace("&nbsp;"," ");
							LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("保存")
									.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("保存调查问题的选项，调查表ID："+surveyAnsw.getDczjid()+"，题目ID："+surveyAnsw.getQuesid()+"，答案ID："+surveyAnsw.getIid()+"，答案："+answname));
						}
					}
				}
			}
			
		}
		script.addScript("parent.location.reload();");
		return script.getScript();
	}
	
	/**
	 * 导出
	 * @param ids
	 * @return
	 */
	@RequestMapping("quesexport")
	@ResponseBody
	public FileResource export(int dczjid) {
		File file = null;
		FileResource fileResouce = null;
		try{
			String filePath = quesInfoService.exportDczjQues(dczjid);
			file = new File(filePath);
			fileResouce = ControllerUtil.getFileResource(file, "调查征集题目列表导出.xls");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null && file.exists()) {
				file.delete();
			}
		}
		return fileResouce;
	}
	
	/**
	 * 显示导入页面
	 * @param webId
	 * @param formId
	 * @return
	 */
	@RequestMapping("quesimportshow")
	public ModelAndView showImport(int dczjid) {
		ModelAndView model = new ModelAndView("dczj/ques/ques_import");
		model.addObject("dczjid", dczjid);
		model.addObject("url", "quesimportsubmit.do");
		return model;
	}
	
	/**
	 * 导入提交
	 * @param file
	 * @param webId
	 * @param formId
	 * @return
	 */
	@RequestMapping(value = "quesimportsubmit")
	@ResponseBody
	public String submitImport(MultipartFile file, int dczjid) {
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		if (file.isEmpty()) {
			message = SpringUtil.getMessage("import.nofile");
		} else {
			try {
				MultipartFileInfo info = MultipartFileInfo.getInstance(file);
				if (ArrayUtils.contains(FileUtil.EXCEL_FILE, info.getFileType())) {
					File filePath = new File(Settings.getSettings().getFileTmp() + StringUtil.getUUIDString() + "." + info.getFileType());
					ControllerUtil.writeMultipartFileToFile(filePath, file);
					message = quesInfoService.importQues(filePath,dczjid);
				} else {
					throw new OperationException("文件类型不正确");
				}
			} catch (OperationException e) {
				message = e.getMessage();
			}
		}
		if (StringUtil.isNotEmpty(message)) {
			script.addAlert(message);
			script.addScript("parent.refreshParentWindow();");
		} else {
			script.addScript("parent.refreshParentWindow();parent.closeDialog();");
		}
		return script.getScript();
	}
	
	/**
     * 题目复制
     * @param formId
     * @param type
     * @return
     */
	@RequestMapping("copyques")
	@ResponseBody
	public JsonResult submitCopyQuesAndAnsw(int quesId) {
		JsonResult jsonResult = JsonResult.getInstance();
		QuesInfo quesInfo = quesInfoService.findQuesEntityByIid(quesId);
		String index = "0";
		int newquesId = 0;
		if(quesInfo != null){
			newquesId = quesInfoService.addQues(quesInfo);
		}
		if(newquesId > 0){
			QuesInfo newSurveyQuesEn = quesInfoService.findQuesEntityByIid(newquesId);
			boolean bl = true;
			if(bl){
				if(newSurveyQuesEn.getType() == 0 || newSurveyQuesEn.getType() == 1 || newSurveyQuesEn.getType() == 6 || newSurveyQuesEn.getType() == 7 ){
					List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesId);
					if(answIdList != null && answIdList.size() > 0){
						for(Integer answid : answIdList){
							AnswInfo surveyAnsw = answInfoService.getEntity(answid);
							if(surveyAnsw != null){
								surveyAnsw.setQuesid(newquesId);
								int newanswid = answInfoService.add(surveyAnsw);
								if(newanswid > 0 && StringUtil.isNotEmpty(surveyAnsw.getAnswimgname())){
									String oldpath = BaseInfo.getRealPath() + "/"+ "answimg" + "/dczjid_"+surveyAnsw.getDczjid()+"/quesid_"+quesId+"/answid_"+answid+"/";
									String newpath = BaseInfo.getRealPath() + "/"+ "answimg" + "/dczjid_"+surveyAnsw.getDczjid()+"/quesid_"+surveyAnsw.getQuesid()+"/answid_"+newanswid+"/";
									FileUtil.createDir(newpath);
									File oldFile = new File(oldpath + surveyAnsw.getAnswimgname());
									if(oldFile.exists()){
										localFileUtil.copyFile(oldpath + surveyAnsw.getAnswimgname(), newpath + surveyAnsw.getAnswimgname());
										if(Settings.getSettings().getEnableoss() == 1){
											String imagepath ="answimg" + "/dczjid_"+surveyAnsw.getDczjid()+"/quesid_"+surveyAnsw.getQuesid()+"/answid_"+answid+"/";
											String ossFilePath = fileUtil.getAbsolutePath(imagepath);
					                        
					                        File tempfile = new File(Settings.getSettings().getFileTmp() + surveyAnsw.getAnswimgname());
											localFileUtil.copyFile(oldFile, tempfile);
											
											fileUtil.deleteDirectory(ossFilePath);
											fileUtil.createDir(ossFilePath);
											fileUtil.moveFile(tempfile, ossFilePath +surveyAnsw.getAnswimgname());
										}
									}
								}
							}
						}
					}
				}
			}
			if(bl){
				String strContent = "";
				String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/quespage.xml";
				String content = styleService.getStyleCodeByPath(quespage);
				content = styleService.parseDefaultQues(content);
				List<QuesInfo> quesList = quesInfoService.findQuesListByIid(newquesId);
				HashMap<String, Object> confMap = new HashMap<String, Object>();
				if(quesList !=null && quesList.size() >0){
					confMap.put("styleCode",content);
					strContent = styleService.parseFormFront(confMap,quesList,index);
				}
				jsonResult.setSuccess(true);
				jsonResult.addParam("newquesId", newquesId);
				jsonResult.addParam("newQuesHtml", strContent);
			}else{
				jsonResult.setSuccess(false);
				jsonResult.setMessage("操作失败，请联系管理员");
			}
		}
		if(quesInfo != null) {
			int num = quesInfoService.findNum(NumberUtil.getInt(quesInfo.getDczjid()));
			int sumscore = quesInfoService.findScore(NumberUtil.getInt(quesInfo.getDczjid()));
			jsonResult.addParam("num", num);
			jsonResult.addParam("sumscore", sumscore);
			titleInfoService.setUpdateHtml(quesInfo.getDczjid()+"", 0);
		}
		return jsonResult;
	}
	
	/**
	 * 排序
	 * @param quesId
	 * @param answId
	 * @param type
	 * @return
	 */
	@RequestMapping("sortques")
	@ResponseBody
	public JsonResult sortques(int dczjid,int quesid,int type) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(false);
		List<Integer> quesIdList = quesInfoService.findQuesIdByDczjId(dczjid);
		if(quesIdList != null && quesIdList.size()>0){
			for(int i = 0; i<= quesIdList.size() - 1; i++){
				if(quesIdList.get(i) == quesid){
					if(type == 1){
						if(i > 0){
							int sortQuesId = quesIdList.get(i-1);
							quesIdList.set(i, sortQuesId);
							quesIdList.set(i-1, quesid);
							jsonResult.setSuccess(true);
							jsonResult.addParam("sortQuesId", sortQuesId);
							break;
						}else{
							return jsonResult.setMessage("当前题目已排在第一位！");
						}
					}else if(type == 2){
						if(i < quesIdList.size() - 1){
							int sortQuesId = quesIdList.get(i+1);
							quesIdList.set(i, sortQuesId);
							quesIdList.set(i+1, quesid);
							jsonResult.setSuccess(true);
							jsonResult.addParam("sortQuesId", sortQuesId);
							break;
						}else{
							return jsonResult.setMessage("当前题目已排在最后一位！");
						}
					}
				}
			}
		}
		String[][] strData = quesInfoService.findMinOrder(quesIdList);
		int nOrderID = 0;
		if (strData != null && strData.length > 0)
			nOrderID = NumberUtil.getInt(strData[0][0]);
		
		int nLen = quesIdList.size();
		for (int j = 0; j < nLen; j++) {
			boolean bl = quesInfoService.modifyOrder(quesIdList.get(j),nOrderID + nLen -1 -j);
			if(!bl){
				return jsonResult.setMessage("操作失败，请联系管理员！");
			}
		}
		titleInfoService.setUpdateHtml(dczjid+"", 0);
		return jsonResult.setSuccess(true);
	}
	
	/**
	 * 批量添加
	 * @param dczjid
	 * @return
	 */
	@RequestMapping("all_add")
	public ModelAndView allAdd(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/ques/alladd");
		return modelAndView;
	}
}
