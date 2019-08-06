package com.hanweb.dczj.controller.front;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.entity.TotalReco;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.FrontSubmitService;
import com.hanweb.dczj.service.FrontVerificationService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.SentPhoneMessageService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;


@Controller
@RequestMapping("front/dczj")
public class DczjSubController {
	
	@Autowired
	private SettingService settingService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private FrontSubmitService frontSubmitService;
	@Autowired
	private RadioRecoService radioRecoService;
	@Autowired
	private CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	private ContentRecoService contentRecoService;
	@Autowired
	private SentPhoneMessageService sentPhoneMessageService;
	@Autowired
	private FrontVerificationService frontVerificationService;
    @Autowired
    private DisplayConfigService displayConfigService;
    @Autowired
    private TitleInfoService titleInfoService;
    
	/**
	 * 表单提交
	 * 
	 */
	@RequestMapping("dczj_submit")
	@ResponseBody
	public String formSubmit(String dczjid,TotalReco totalReco,
			HttpServletResponse response, HttpSession session,
			@RequestParam(value = "code", required = false) String code) {

		String submitType = SpringUtil.getRequest().getParameter("submitType");//判断提交类型 2是手机还是 1电脑
		Script script = Script.getInstanceOnly();
		dczjid = StringUtil.getSafeString(dczjid);
		Cache cache = CacheManager.getInstance("jsurvey");
		Cache testcache = CacheManager.getInstance("jsurvey_test");
		if(StringUtil.isEmpty(dczjid)){
			script.addScript("parent.showerrormessage('表单提交失败，请稍候再试！');").getScript();
			return script.getScript();
		}
		Dczj_Setting setting = null;
		setting = cache.get("configEnformid"+dczjid, new Type<Dczj_Setting>(){});
		if(setting == null){
			setting =  settingService.getEntityBydczjid(dczjid);
			cache.put("configEnformid"+dczjid, setting);
		}
		if(setting == null){
			setting = new  Dczj_Setting();
		}
		if(setting.getIsend() == 1) {
			if(setting.getEndtime() != null && this.getState(setting.getEndtime()) == 2) {
				script.addScript("parent.showerrormessage('表单提交失败，请稍候再试！');").getScript();
				return script.getScript();
			} 
		}
		DisplayConfig displayConfig = null;
		displayConfig = cache.get("display_config_"+dczjid, new Type<DisplayConfig>(){});
		if(displayConfig == null) {
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
			cache.put("display_config_"+dczjid, displayConfig);
		}
		if(displayConfig == null){
			displayConfig = new  DisplayConfig();
		}
		String errorMessage = null;
		String mobile = "";  //获取手机号码
		String codes = "";   //获取所填验证码
		int isCode = setting.getIscode();//是否开启验证码，0未开启，1开启
		int codetype = setting.getCodes();//0为图形 1为手机
		int limitType = setting.getLimittype();	//限制访问类型 :  0IP限制，1手机限制
		int limitTime = setting.getLimittime();//0只能，1每小时，2每天
		int mostSubmit = setting.getLimitnumber(); //能投票的次数 默认一次，1
		int isjump = setting.getIsjump(); //是否跳转 0感谢信息，1制定页面
		int limitlength = NumberUtil.getInt(displayConfig.getContentsize());
		String jumpurl = setting.getJumpurl();// 跳转地址
		if(isCode == 1 && codetype == 1){  //开启手机短信验证
			mobile = SpringUtil.getRequest().getParameter("iphonenumber");
			codes = SpringUtil.getRequest().getParameter("iponecode");
			//手机号验证
			script = phoneNumCheck(mobile, codes, script);
			if(script.getScript().indexOf("<script type=\"text/javascript\">\n</script>")<0) {
				return script.getScript();
			}
			HttpSession session1 = SpringUtil.getRequest().getSession(false);
			String sessionCode = (String)session1.getAttribute("surveycode");
			if("".equals(codes) || !codes.equals(sessionCode)){
				errorMessage = "您输入的手机验证码不正确，无法提交！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script.getScript();
			}
			
			int isHaveSurvey = 0;
			//提交次数验证
			if(setting.getSubmitlimit()==1) {
				script = phoneLimitCheck(limitType, limitTime, cache, dczjid, mobile, isHaveSurvey,script, mostSubmit);
			}
			if(script.getScript().indexOf("<script type=\"text/javascript\">\n</script>")<0) {
				return script.getScript();
			}
		}
		//验证码
		if(isCode == 1 && codetype == 0){	//开启验证码，需要插入标签
			code = StringUtil.getString(code);
			script = codeCheck(code, script, dczjid, session);
			if(script.getScript().indexOf("<script type=\"text/javascript\">\n</script>")<0) {
				return script.getScript();
			}
		}
		String userIp = IpUtil.getIp();
		String [] ips = userIp.split(",");
		if (ips != null && ips.length > 0){ //防止双IP
		    userIp = StringUtil.getString(ips[0]);
		}
		String uIp = userIp.replace(".", "_").replace(":", "_");
		String address = IpUtil.getCountryByIP(SpringUtil.getRequest(), userIp);
		String sumScore = SpringUtil.getRequest().getParameter("sumscore");
		int sumscore = 0;
		if(StringUtil.isNotEmpty(sumScore)) {
			sumscore = NumberUtil.getInt(sumScore);
		}
		int sameIpCount = 0;
		mostSubmit = setting.getLimitnumber();		//允许提交次数
		//ip提交限制检查
		if(setting.getSubmitlimit()==1) {
			script = ipLimitCheck(limitType, limitTime, cache, dczjid, uIp, sameIpCount, script, mostSubmit);
		}
		if(script.getScript().indexOf("<script type=\"text/javascript\">\n</script>")<0) {
			return script.getScript();
		}
		List<QuesInfo> quesList = null;
		if(cache.get("dczj_ques_list_"+dczjid, new Type<List<QuesInfo>>(){}) != null) {
			quesList = cache.get("dczj_ques_list_"+dczjid, new Type<List<QuesInfo>>(){});
		}else {
			quesList = quesInfoService.findQuesListByDczjid(dczjid);
			cache.put("dczj_ques_list_"+dczjid, quesList);
		}
		
		boolean insertSuccess = false;  //插入是否成功
		boolean isFilledForm = false;   //是否填写了表单，true：填了，false：没填
		String unid = "";
		if(quesList == null){
			String showMessage = "提交失败，请设置调查表调查题目！";
			script.addScript("parent.showerrormessage('"+showMessage+"')");
			return script.getScript();
		}else{
			unid = StringUtil.getUUIDString();
			if(totalReco!=null){
				totalReco.setCreatedate(new Date());
				totalReco.setUnid(unid);
				totalReco.setDczjid(NumberUtil.getInt(dczjid));
				totalReco.setType(NumberUtil.getInt(submitType));        //PC端 1 phone端2 
				totalReco.setCode(codes);
				totalReco.setMobile(mobile);
				totalReco.setIp(userIp);
				totalReco.setIpaddress(address);
				totalReco.setSumscore(sumscore);
				totalReco.setSubmitstate(0); //未提交，只是放置缓存中
			}
			//将提交的数据放入缓存中
			
			//提交单选
			List<RadioReco> radioRecoList = new ArrayList<RadioReco>();
			//提交多选
			List<CheckedBoxReco> checkedBoxRecoList = new ArrayList<CheckedBoxReco>();
			//提交填空
			List<ContentReco> contentRecoList = new ArrayList<ContentReco>();
			
			Iterator<QuesInfo> it = quesList.iterator();  //获取提交值
			int answId = 0;
			while(it.hasNext()){
				QuesInfo quesEn = (QuesInfo)it.next();
				int quesId = quesEn.getIid();
				int quesType = quesEn.getType();
				int mustfill = quesEn.getIsmustfill();
				String answName = "surveyAnsw" + quesId;
				String[] answArray = SpringUtil.getRequest().getParameterValues(answName);
				String answContent = "";
				if(quesType == 0 || quesType ==1 || quesType==6 || quesType==7) {
					script.addScript("parent.setUUID('"+unid+"')");
					testcache.put("testRightAnsw_"+quesId+"_"+unid, answArray);
				}
				if( !(answArray != null && answArray.length >0)) {  //必填未选的时候
					if(mustfill == 1 && (quesType == 0 || quesType == 1 || quesType == 6 || quesType == 7)) {
						script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
						return script.getScript();
					}
				}
				
				if(answArray != null && (quesType == 0 || quesType == 6)){//单选题
					if(answArray.length != 1) {  //单选选了多个的时候
						script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
						return script.getScript();
					}
					
					isFilledForm = true;
					List<String> answList = Arrays.asList(answArray);
					Iterator<String> answIt = answList.iterator();
					while(answIt.hasNext()){
						answId = NumberUtil.getInt((String)answIt.next());
						AnswInfo answEn = null;
						if(cache.get("dczj_answ_entity_"+answId,new Type<AnswInfo>(){}) != null) {
							answEn = cache.get("dczj_answ_entity_"+answId,new Type<AnswInfo>(){});
						}else {
							answEn = answInfoService.getEntity(answId);
							if(answEn != null) {
								cache.put("dczj_answ_entity_"+answEn.getIid(), answEn);
							}
						}
						if(answEn == null) {  //选项为空 直接返回
							script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
							return script.getScript();
						}
						if(answEn != null) { //题目ID不匹配时 或者 已删除的题目 返回
							if( (NumberUtil.getInt(answEn.getQuesid()) != NumberUtil.getInt(quesEn.getIid())) || (answEn.getState() == 1)) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}
						if(NumberUtil.getInt(answEn.getAllowfillinair()) == 1){
							answContent = SpringUtil.getRequest().getParameter("surveyAnsw_"+quesId+"_"+answId+"_fillInAir");
							answContent = StringUtil.getString(answContent).trim();
							if(answContent.length()>limitlength) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}
						RadioReco radioReco = new RadioReco();
						radioReco.setIp(userIp);
						radioReco.setReplyid(StringUtil.getUUIDString());
						radioReco.setSubmittime(new Date());
						radioReco.setUnid(unid);
						radioReco.setAnswid(answId);
						radioReco.setAudi(""+ 0);//未审核
						radioReco.setQuesid(quesId);
						radioReco.setAnswcontent(answContent);
						radioReco.setDczjid(NumberUtil.getInt(dczjid));
						radioRecoList.add(radioReco);
					}
					insertSuccess = true;
					
				}else if(answArray != null && (quesType == 1 || quesType == 7) ){//多选题
					int maxsize = quesEn.getMaxselect();
					int minsize = quesEn.getMinselect();
					int actualsize = answArray.length;
					if(mustfill == 1 && (actualsize < minsize || actualsize > maxsize)) {
						script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
						return script.getScript();
					}
					
					Map<String, String> answMap = new HashMap<String, String>();
					int right = 0;
					isFilledForm = true;
					List<String> answList = Arrays.asList(answArray);
					Iterator<String> answIt = answList.iterator();
					if(quesType == 7) {
						String[][] rightAnswArray = answInfoService.findRightAnswid(quesId+"");
						if(rightAnswArray!=null && rightAnswArray.length>0) {
							if(rightAnswArray.length == answList.size()) {
								String rightStr = "-";
								for(int i = 0;i<rightAnswArray.length;i++) {
									rightStr += rightAnswArray[i][0] + "-";
								}
								for(int j=0;j<answList.size();j++) {
									right = 1;
									answId = NumberUtil.getInt(answList.get(j));
									if(rightStr.indexOf("-"+answId+"-")<0) {
										right = 0;
										break;
									}
								}
							}
						}
					}
					while(answIt.hasNext()){
						answId = NumberUtil.getInt((String)answIt.next());
						if(StringUtil.isEmpty(answMap.get(answId+""))) {
							answMap.put(answId+"", "1");
						}
						
						AnswInfo answEn = null;
						if(cache.get("dczj_answ_entity_"+answId,new Type<AnswInfo>(){}) != null) {
							answEn = cache.get("dczj_answ_entity_"+answId,new Type<AnswInfo>(){});
						}else {
							answEn = answInfoService.getEntity(answId);
							if(answEn != null) {
								cache.put("dczj_answ_entity_"+answEn.getIid(), answEn);
							}
						}
						if(answEn == null) {  //选项为空 直接返回
							script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
							return script.getScript();
						}
						if(answEn != null) { //题目ID不匹配时 或者 已删除的题目 返回
							if( (NumberUtil.getInt(answEn.getQuesid()) != NumberUtil.getInt(quesEn.getIid())) || (answEn.getState() == 1)) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}
						if(NumberUtil.getInt(answEn.getAllowfillinair()) == 1){
							answContent = SpringUtil.getRequest().getParameter("surveyAnsw_"+quesId+"_"+answId+"_fillInAir");
							answContent = StringUtil.getString(answContent).trim();
							if(answContent.length()>limitlength) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}
						CheckedBoxReco checkedBox = new CheckedBoxReco();
						checkedBox.setIp(userIp);
						checkedBox.setReplyid(StringUtil.getUUIDString());
						checkedBox.setSubmittime(new Date());
						checkedBox.setUnid(unid);
						checkedBox.setAnswid(answId);
						checkedBox.setAudi(""+0);//未审核
						checkedBox.setDczjid(NumberUtil.getInt(dczjid));
						checkedBox.setQuesid(quesId);
						checkedBox.setAnswcontent(answContent);
						checkedBox.setIsright(right);
						checkedBoxRecoList.add(checkedBox);
					}
					int mapsize = answMap.size();
					if(mustfill == 1 && (mapsize < minsize || mapsize > maxsize)) {
						script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
						return script.getScript();
					}
					insertSuccess = true;
				}else if(quesType == 2 || quesType == 5){  //单、多行文本
					answContent = StringUtil.getString(SpringUtil.getRequest().getParameter(answName + "_content"));
					answContent = StringUtil.getString(answContent).trim();
					
					if(mustfill == 1 && StringUtil.isEmpty(answContent)) {
						script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
						return script.getScript();
					}
					if(quesType == 5 && mustfill == 1) {
						int validateRules = quesEn.getValidaterules();
						boolean bl = false;
						if(validateRules == 1) {
							bl = frontVerificationService.checkname(answContent);
							if(!bl) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}else if(validateRules == 2) {
							bl = frontVerificationService.checkNumber(answContent);
							if(!bl) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}else if(validateRules == 3) {
							bl = frontVerificationService.checkEmail(answContent);
							if(!bl) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}else if(validateRules == 4) {
							bl = frontVerificationService.checkPhone(answContent);
							if(!bl) {
								script.addScript("parent.showerrormessage('对不起，表单提交错误，请稍候再试！');").getScript();
								return script.getScript();
							}
						}
					}
					if(answContent.length()==0){
						continue;
					}
					ContentReco contentReco = new ContentReco();
					contentReco.setIp(userIp);
					contentReco.setReplyid(StringUtil.getUUIDString());
					contentReco.setSubmittime(new Date());
					contentReco.setUnid(unid);
					contentReco.setAnswid(answId);
					contentReco.setAudi("0");  //设置为未审核
					contentReco.setDczjid(NumberUtil.getInt(dczjid));
					contentReco.setQuesid(quesId);
					contentReco.setAnswcontent(answContent);
					contentRecoList.add(contentReco);
					insertSuccess = true;
					isFilledForm = true;
				}
			}
			if(insertSuccess) {
				frontSubmitService.submitSync(cache, totalReco);
				cache.put("radio"+unid, radioRecoList);
				cache.put("checkedBox"+unid, checkedBoxRecoList);
				cache.put("content"+unid, contentRecoList);
			}
		}
		
		if(insertSuccess){
			//调查表提交成功,记录已经提交过cookie标识
			Cookie submitedCookie = new Cookie("survey"+dczjid+"_submited","1");
			submitedCookie.setMaxAge(-1);
			response.addCookie(submitedCookie);

			//手机记录插入缓存中
			if(isCode == 1 && codetype == 1){
				phoneInCache(limitType, limitTime, cache, dczjid, mobile);
			}
			//ip记录入缓存
			limitInCache(limitType, limitTime, cache, dczjid, uIp);
			
			if(isjump==0){//若为弹出感谢信息
				jumpurl = "";
				script.addScript("parent.alertResultWind('"+jumpurl+"');");
			}else{//若为指定url
				if(StringUtil.isNotEmpty(jumpurl)){
					 script.addScript("parent.alertResultWind('"+jumpurl+"');");
				}
			}
			script.addScript("parent.dczj" + dczjid + ".reset();");
		}else{
			String showMessage = isFilledForm ? "调查表提交失败！" : "请填写调查表！";
			script.addScript("parent.showerrormessage('"+showMessage+"')");
			return script.getScript();
		}
		//验证结束，将session中的数据删除
		session.removeAttribute("surveycode");
		session.removeAttribute("sessionid");
		//存入session信息
		if(StringUtil.isNotEmpty(unid)) {
			session.setAttribute("check_"+unid+"_"+dczjid, "1");
			session.setAttribute("loginunid_"+dczjid, unid);
		}
		return script.getScript();
		
	}
	
	/**
	 * 手机记录入缓存
	 * @param limitType
	 * @param limitTime
	 * @param cache
	 * @param formid
	 * @param mobile
	 */
	public void phoneInCache(int limitType, int limitTime, Cache cache, String formid, String mobile) {

		if(limitType == 1 && limitTime == 0){  //当为手机限制时，只能提交次数
			int count = 0;
			if(cache.get("only"+formid + mobile,new Type<Integer>() {}) != null){
				count = cache.get("only"+formid + mobile,new Type<Integer>() {});
			}
			cache.put("only"+formid + mobile, count+1);
		}else if(limitType == 1 && limitTime == 1){ //每小时提交次数
			int count = 0;
			if(cache.get("everyhours"+formid + mobile,new Type<Integer>() {}) != null){
				count = cache.get("everyhours"+formid + mobile,new Type<Integer>() {});
			}
			cache.put("everyhours"+formid + mobile, count+1,60*60);
		}else if(limitType == 1 && limitTime == 2){ //每天提交次数
			int count = 0;
			if(cache.get("everyday"+formid + mobile,new Type<Integer>() {}) != null){
				count = cache.get("everyday"+formid + mobile,new Type<Integer>() {});
			}
			cache.put("everyday"+formid + mobile, count+1);
		}
	
	}
	
	/**
	 * ip记录入缓存
	 * @param limitType
	 * @param limitTime
	 * @param cache
	 * @param formid
	 * @param uIp
	 */
	public void limitInCache(int limitType, int limitTime, Cache cache, String formid, String uIp) {
		if(limitType == 0 && limitTime == 0){    
			int count = 0;
			if(cache.get("onlyforip"+formid + uIp,new Type<Integer>() {}) != null){
				count = cache.get("onlyforip"+formid + uIp,new Type<Integer>() {});
			}
			cache.put("onlyforip"+formid + uIp, count+1);
		}else if(limitType == 0 && limitTime == 1){
			int count = 0;
			if(cache.get("everyhoursforip"+formid + uIp,new Type<Integer>() {}) != null){
				count = cache.get("everyhoursforip"+formid + uIp,new Type<Integer>() {});
			}
			cache.put("everyhoursforip"+formid + uIp, count+1);
		}else if(limitType == 0 && limitTime == 2){
			int count = 0;
			if(cache.get("everydayforip"+formid + uIp,new Type<Integer>() {}) != null){
				count = cache.get("everydayforip"+formid + uIp,new Type<Integer>() {});
			}
			cache.put("everydayforip"+formid + uIp, count+1);
		}
	}
	
	/**
	 * 手机号验证
	 * @param errorMessage
	 * @param mobile
	 * @param codes
	 * @param script
	 * @return
	 */
	public Script phoneNumCheck(String mobile, String codes, Script script) {
		String errorMessage = null;
		if(StringUtil.isEmpty(mobile)){
			errorMessage = "请填写手机号码！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			return script;
		}
		String regEx = "(^(12|13|14|15)[0-9]{9}$)|(^(16|17|18)[0-9]{9}$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(mobile);
		if(!matcher.find()){
			errorMessage = "请填写正确的手机号码！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			return script;
		}
		if(StringUtil.isEmpty(codes)){
			errorMessage = "请填写接收到的手机验证码！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			return script;
		}
		return script;
	}
	
	/**
	 * 手机提交限制检查
	 * @param limitType
	 * @param limitTime
	 * @param cache
	 * @param formid
	 * @param mobile
	 * @param isHaveSurvey
	 * @param errorMessage
	 * @param script
	 * @param mostSubmit
	 * @return
	 */
	public Script phoneLimitCheck(int limitType, int limitTime, Cache cache, String formid, String mobile, int isHaveSurvey
			, Script script, int mostSubmit) {
		String errorMessage = null;
		if(limitType == 2 && limitTime == 0){  //当为手机限制时，只能 提交次数
			if(cache.get("only"+formid + mobile, new Type<Integer>() {}) != null){
				isHaveSurvey = cache.get("only"+formid + mobile, new Type<Integer>() {});
			}
			if(isHaveSurvey >= mostSubmit){
				errorMessage = "对不起，相同手机号码只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;
			}
		}else if(limitType == 2 && limitTime == 1){ // 手机  每小时 提交次数
			if(cache.get("everyhours"+formid + mobile, new Type<Integer>() {}) != null){
				isHaveSurvey = cache.get("everyhours"+formid + mobile, new Type<Integer>() {});
			}
			if(isHaveSurvey >= mostSubmit){
				errorMessage = "对不起，相同手机号码每小时只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;
			}
		}else if(limitType == 2 && limitTime == 2){ // 手机 每天 提交次数
			if(cache.get("everyday"+formid + mobile, new Type<Integer>() {}) != null){
				isHaveSurvey = cache.get("everyday"+formid + mobile, new Type<Integer>() {});
			}
			if(isHaveSurvey >= mostSubmit){
				errorMessage = "对不起，相同手机号码每天只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;
			}
		}
		return script;
	}
	
	/**
	 * 验证码检查
	 * @param code
	 * @param errorMessage
	 * @param script
	 * @param formid
	 * @param session
	 * @return
	 */
	public Script codeCheck(String code, Script script,String formid, HttpSession session) {
		String errorMessage = null;
		if(code.trim().length() == 0){
			errorMessage = "请输入验证码！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			return script;
		}
		String rand = (String) session.getAttribute("sessionid");
		if(StringUtil.isEmpty(rand)){
			errorMessage = "图形验证码已失效，请刷新！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			return script;
		}
		if (StringUtil.isEmpty(code) || !StringUtil.equals(rand.toLowerCase(), code.toLowerCase())) {
			errorMessage = "验证码填写错误！";
			script.addScript("parent.showerrormessage('"+errorMessage+"')");
			script.addScript("parent.changecode('"+formid+"');");//刷新验证码
			return script;
		}
		return script;
	}
	
	/**
	 * ip限制检查
	 * @param limitType
	 * @param limitTime
	 * @param cache
	 * @param formid
	 * @param uIp
	 * @param sameIpCount
	 * @param errorMessage
	 * @param script
	 * @param mostSubmit
	 * @return
	 */
	public Script ipLimitCheck(int limitType, int limitTime, Cache cache, String formid, String uIp, int sameIpCount
			,  Script script, int mostSubmit) {
		String errorMessage = null;
		if(limitType == 0 && limitTime == 0){ // ip 只能 提交次数
			if(cache.get("onlyforip"+formid + uIp, new Type<Integer>() {}) != null){
				sameIpCount = cache.get("onlyforip"+formid + uIp, new Type<Integer>() {});
			}
			if(sameIpCount >= mostSubmit){ //IP限制
				errorMessage = "对不起，同一IP只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;		
			}
		}else if(limitType == 0 && limitTime == 1){//ip 每小时  提交次数
			if(cache.get("everyhoursforip"+formid + uIp, new Type<Integer>() {}) != null){
				sameIpCount = cache.get("everyhoursforip"+formid + uIp, new Type<Integer>() {});
			}
			if(sameIpCount >= mostSubmit){ //IP限制
				errorMessage = "对不起，同一IP每小时只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;		
			}
		}else if(limitType == 0 && limitTime == 2){ //ip 每天  提交次数
			if(cache.get("everydayforip"+formid + uIp, new Type<Integer>() {}) != null){
				sameIpCount = cache.get("everydayforip"+formid + uIp, new Type<Integer>() {});
			}
			if(sameIpCount >= mostSubmit){ //IP限制
				errorMessage = "对不起，同一IP每天只能投票"+mostSubmit+"次，感谢您的参与！";
				script.addScript("parent.showerrormessage('"+errorMessage+"')");
				return script;		
			}
		}
		return script;
	}
	
	/**
	 * 查看其它答案index
	 * @param quesId
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping("show_otheransw")
	public ModelAndView showOtherAnswIndex(String quesid,Integer pageNo,Integer quesType,Integer answid){
		quesid = StringUtil.getSafeString(quesid);
		ModelAndView model = new ModelAndView("dczj/front/showotheranswerindex");
		model.addObject("quesid", quesid);
		model.addObject("quesType", quesType);
		model.addObject("answid", answid);
		return model;
	}
	/**
	 * 查看其它答案
	 * @param quesId
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping("show_otheranswlist")
	public ModelAndView showOtherAnsw(String quesid,Integer pageNo,Integer quesType,String answid){
		Cache cache = CacheManager.getInstance("jsurvey_openAnswer");
		if(pageNo == null){
			pageNo = 0;
		}
		quesid = StringUtil.getSafeString(quesid);
		answid = StringUtil.getSafeString(answid);
		ModelAndView model = new ModelAndView("dczj/front/showotheranswer");
		if(StringUtil.isEmpty(quesid)){
			ModelAndView modelAndView = new ModelAndView("support/error/602");
			return modelAndView;
		}
		//QuesInfo quesEn = quesInfoService.findQuesEntityByIid(NumberUtil.getInt(quesid));
		if(quesType == 0) {
			List<RadioReco> radioRecos = cache.get("jsurvey_"+quesid+"_"+answid, new Type<List<RadioReco>>() {});
			if(radioRecos!=null) {
				int a = radioRecos.size() % 10;
				int b = radioRecos.size() / 10;
				int pageNum;
				if (a == 0) {
					pageNum = b;
				} else {
					pageNum = b + 1;
				}
				model.addObject("recoList",radioRecos.subList((pageNo-1)*10, pageNo*10-1));
				model.addObject("pageNum", pageNum); // 页码总数
				model.addObject("pageNo", pageNo); // 所在页
				model.addObject("total", radioRecos.size()); // 总数量
				model.addObject("quesid", quesid);
				model.addObject("answid", answid);
				model.addObject("quesType", quesType);
				return model;
			}
		}else if(quesType == 1) {
			List<CheckedBoxReco> checkedBoxRecos = cache.get("jsurvey_"+quesid+"_"+answid, new Type<List<CheckedBoxReco>>() {});
			if(checkedBoxRecos!=null) {
				int a = checkedBoxRecos.size() % 10;
				int b = checkedBoxRecos.size() / 10;
				int pageNum;
				if (a == 0) {
					pageNum = b;
				} else {
					pageNum = b + 1;
				}
				model.addObject("recoList",checkedBoxRecos.subList((pageNo-1)*10, pageNo*10-1));
				model.addObject("pageNum", pageNum); // 页码总数
				model.addObject("pageNo", pageNo); // 所在页
				model.addObject("total", checkedBoxRecos.size()); // 总数量
				model.addObject("quesid", quesid);
				model.addObject("answid", answid);
				model.addObject("quesType", quesType);
				return model;
			}
		}else if(quesType == 2 || quesType == 5) {
			List<ContentReco> contentRecos = cache.get("jsurvey_"+quesid+"_"+answid, new Type<List<ContentReco>>() {});
			if(contentRecos!=null) {
				int a = contentRecos.size() % 10;
				int b = contentRecos.size() / 10;
				int pageNum;
				if (a == 0) {
					pageNum = b;
				} else {
					pageNum = b + 1;
				}
				model.addObject("recoList",contentRecos.subList((pageNo-1)*10, pageNo*10-1));
				model.addObject("pageNum", pageNum); // 页码总数
				model.addObject("pageNo", pageNo); // 所在页
				model.addObject("total", contentRecos.size()); // 总数量
				model.addObject("quesid", quesid);
				model.addObject("answid", answid);
				model.addObject("quesType", quesType);
				return model;
			}
		}
		String  i_audi = "1"; //只显示已经审核的数据
		//查询总的答案数量
		int total = 0;
		int pagesize = 10;
		//quesType = quesEn.getType();
		if(quesType == 0){
			//单选
			total = radioRecoService.getRecoConut(quesid,answid);
		}else if(quesType == 1){
			//多选
			total = checkedBoxRecoService.getRecoConut(quesid,answid);
		}else if(quesType == 2){
			//多行文本(需要审核)
			total = contentRecoService.getRecoConut(quesid,i_audi);
		}else if(quesType == 5) {
			//单行文本
			total = contentRecoService.getRecoConut(quesid);
		}
		
		int a = total % pagesize;
		int b = total / pagesize;
		int pageNum;
		if (a == 0) {
			pageNum = b;
		} else {
			pageNum = b + 1;
		}
		if (pageNo > pageNum) {
			pageNo = pageNum;
		} else if (pageNo <= 0) {
			pageNo = 1;
		}
		
		if(quesType == 0){
			List<RadioReco> radioRecoList = radioRecoService.findOtherAnswerList(pageNo,pagesize,quesid,answid);
			if(radioRecoList == null || radioRecoList.size() <= 0 ){
				model.addObject("errorMessage","没有提交的答案，或者所提交答案没有通过审核！");
			}
			model.addObject("recoList",radioRecoList);
		}else if(quesType == 1){
			List<CheckedBoxReco> checkedBoxRecoList = checkedBoxRecoService.findOtherAnswerList(pageNo, pagesize, quesid, answid);
			if(checkedBoxRecoList == null || checkedBoxRecoList.size() <= 0 ){
				model.addObject("errorMessage","没有提交的答案，或者所提交答案没有通过审核！");
			}
			model.addObject("recoList",checkedBoxRecoList);
		}else if(quesType == 2){
			List<ContentReco> contentRecoList = contentRecoService.findOtherAnswerList(pageNo,pagesize,quesid
					,NumberUtil.getInt(i_audi));
			if(contentRecoList == null || contentRecoList.size() <= 0 ){
				model.addObject("errorMessage","没有提交的答案，或者所提交答案没有通过审核！");
			}
			model.addObject("recoList",contentRecoList);
		}else{
			List<ContentReco> contentRecoList = contentRecoService.findOtherAnswerList(pageNo,pagesize,quesid);
			if(contentRecoList == null || contentRecoList.size() <= 0 ){
				model.addObject("errorMessage","没有提交的答案，或者所提交答案没有通过审核！");
			}
			model.addObject("recoList",contentRecoList);
		}
		
		//model.addObject("start", start); // 开始页码
		model.addObject("pageNum", pageNum); // 页码总数
		model.addObject("pageNo", pageNo); // 所在页
		model.addObject("total", total); // 总数量
		model.addObject("quesid", quesid);
		model.addObject("answid", answid);
		model.addObject("quesType", quesType);
		return model;
	}
	
	/**
	 * 获取手机验证码
	 * @param randomCode 
	 * @param mobile
	 * @param formid
	 * @return
	 */
	@RequestMapping("ajax_sendcodes")
	@ResponseBody
	public JsonResult sendPhoneCodes(String phoneMessage,
			String mobile,String dczjid){
		JsonResult jsonResult = JsonResult.getInstance();
		if(StringUtil.isEmpty(dczjid) || StringUtil.isEmpty(mobile) || StringUtil.isEmpty(phoneMessage) ){
			jsonResult.setSuccess(false);
			jsonResult.setMessage("参数错误！");
			return jsonResult;
		}
		//查询调查结束时间
		Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
		if(setting != null && setting.getIsend()==1) {
			Date endtime = setting.getEndtime();
			if(endtime != null){
				Date endData = endtime;
				Date curDate = DateUtil.stringtoDate(DateUtil.getCurrDate("yyyy-MM-dd"), "yyyy-MM-dd");
				if(curDate.after(endData)){
					jsonResult.setSuccess(false);
					jsonResult.setMessage("调查已结束！");
					return jsonResult;
				}
			}
		}
		if("".equals(mobile)){
			jsonResult.setSuccess(false);
			jsonResult.setMessage("请填写手机号!");
			return jsonResult;
		}
		String regEx = "(^(12|13|14|15)[0-9]{9}$)|(^(16|17|18)[0-9]{9}$)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(mobile);
		if(!matcher.find()){
			jsonResult.setSuccess(false);
			jsonResult.setMessage("请填写正确的手机号码！"); ;
			return jsonResult;
		}
		/*int isHaveSurvey = 0;
		isHaveSurvey = frontSurveyService.getPhone(formid,mobile,"","","");
		if(isHaveSurvey > 0){
			jsonResult.setMessage("对不起，您的手机号码已经投过票，不能重复投票。感谢您的参与！");
			return jsonResult;
		}*/
		long[] date = sentPhoneMessageService.dateDiff(mobile, dczjid);
		if(date != null){
			if(0 <= date[0] && date[0] <= 2){
				String message = "没有收到验证码？"+date[0]+"分钟"+date[1]+"秒后可再次点击获取!";
				jsonResult.setSuccess(false);
				jsonResult.setMessage(message);
				return jsonResult;
			}else if(date[0] == -1){
				String message = "您今天获取验证码次数已经超过5次，请明天再试！";
				jsonResult.setSuccess(false);
				jsonResult.setMessage(message);
				return jsonResult;
			}
		}
		String ip = IpUtil.getIp();
		int code = (int)(Math.random()*9000+1000); // 验证码
		HttpSession session = SpringUtil.getRequest().getSession();
		session.setAttribute("surveycode", ""+code);
		try {
			phoneMessage = URLDecoder.decode(phoneMessage, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			System.out.println("解密失败：" + e.getMessage());
		}
		String msg  = phoneMessage.replace("codenumber", ""+code);
//		System.out.println("you send phone number survey====="+code);
		String result = null;
		try{
			result = sentPhoneMessageService.sendMessageCode(mobile, msg,dczjid,"","",ip,code);
		}catch(Exception e){
			e.printStackTrace();
		}
		String sentResult = null;
		if("0".equals(result)){
			sentResult = "验证码已发送到您的手机，请注意查收!";
			jsonResult.setSuccess(true);
		}else{
			sentResult = "短信发送失败!";
			jsonResult.setSuccess(false);
		}
		return jsonResult.setMessage(sentResult);
	}
	
	
	public int getState(Date endDate){
		if(endDate ==null){
			return -1;
		}
		int state = -1;
		Date currentDate = new Date();
		try{
			if(currentDate.getTime()>endDate.getTime()){//已结束
				state = 2;
			}
		}catch(Exception e){
			return -1;
		}
		return state;
	}
	
	
	/**
	 * 查看其它答案index
	 * @param quesId
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	@RequestMapping("show_testansw")
	public ModelAndView showTestAnsw(String uuid,String dczjid,String rightnum,String sumscore){
		ModelAndView model = new ModelAndView("dczj/front/showtestanswer");
		Cache jsurveycache = CacheManager.getInstance("jsurvey");
		Cache testcache = CacheManager.getInstance("jsurvey_test");
		
		TitleInfo titleInfo = jsurveycache.get("dczj_title_"+dczjid, new Type<TitleInfo>(){});
		if(titleInfo == null) {
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
			jsurveycache.put("dczj_title_"+dczjid, titleInfo);
		}
		List<QuesInfo> quesList = null;
		if(jsurveycache.get("dczj_ques_list_"+dczjid, new Type<List<QuesInfo>>(){}) != null) {
			quesList = jsurveycache.get("dczj_ques_list_"+dczjid, new Type<List<QuesInfo>>(){});
		}else {
			quesList = quesInfoService.findQuesListByDczjid(dczjid);
			jsurveycache.put("dczj_ques_list_"+dczjid, quesList);
		}
		String testContent = "";
		int quesallnum = 0;
		int quesallscore = 0;
		String temp =  BaseInfo.getRealPath() + "/resources/dczj/template/testContent.html";
		temp = FileUtil.readFileToString(temp);
		if(quesList != null && quesList.size() >0) {
			for(QuesInfo quesEn : quesList) {
				String quesname = quesEn.getQuesname();
				String quesscore = StringUtil.getString(quesEn.getQuesscore());
				Integer type = quesEn.getType();
				Integer quesid = quesEn.getIid();
                if(type == 0 || type == 1 ||type == 6 || type == 7) {
                	List<AnswInfo> answList = null;
            		if(jsurveycache.get("dczj_answ_list_"+quesid, new Type<List<AnswInfo>>(){}) != null) {
            			answList = jsurveycache.get("dczj_answ_list_"+quesid, new Type<List<AnswInfo>>(){});
            		}else {
            			answList = answInfoService.getAnswListByQuesId(quesid);
            			jsurveycache.put("dczj_answ_list_"+dczjid, quesList);
            		}
            		String rightAnsw = "";
            		String userAnsw = "";
            		String newRightAnsw = "";
            		if(answList != null && answList.size() >0) {
            			for(AnswInfo answEn : answList) {
            				if(StringUtil.equals(answEn.getIsright(), "1")) {
            					rightAnsw += answEn.getAnswname()+",";
            				}
            			}
            		}
            		if(StringUtil.isNotEmpty(rightAnsw)) {
            			rightAnsw = rightAnsw.substring(0,rightAnsw.length()-1);
            		}
            	
            		String[] answArray = testcache.get("testRightAnsw_"+quesid+"_"+uuid, new Type<String[]>(){});
            		if(answArray != null && answArray.length > 0) {
            			List<String> userAnswList = Arrays.asList(answArray);
            			for(String userAnswId : userAnswList) {
            				AnswInfo userAnswEn = jsurveycache.get("dczj_answ_entity_"+userAnswId, new Type<AnswInfo>(){});
            				if(userAnswEn == null) {
            					userAnswEn = answInfoService.getEntity(NumberUtil.getInt(userAnswId));
            					jsurveycache.put("dczj_answ_entity_"+userAnswId, userAnswEn);
            				}
            				String answName = userAnswEn.getAnswname();
            				userAnsw += answName+",";
            			}
            		}
            		if(StringUtil.isNotEmpty(userAnsw)) {
            			userAnsw = userAnsw.substring(0,userAnsw.length()-1);
            		}
            		
    				if(type == 6 || type == 7) {
    					quesallnum ++;
    					quesallscore += NumberUtil.getInt(quesscore);
    					newRightAnsw = "<tr><td style=\"height: 22px;line-height: 22px\">正确答案："+rightAnsw+"</td></tr>";
    					quesname += "<span style='color:#999'> [分值："+quesscore+"分]</span>";
    					if(StringUtil.equals(rightAnsw, userAnsw)) {
    						userAnsw +="<img src='../../resources/front/images/3.png' style='height: 14px;'> （得分："+quesscore+"分）";
    					}else {
    						userAnsw +="<img src='../../resources/front/images/4.png' style='height: 14px;'> （得分：0分）";
    					}
    				}
    				String content = temp.replaceAll("<!--quesid-->", StringUtil.getString(quesid)).replaceAll("<!--jsurveyquesname-->", quesname).replaceAll("<!--useransw-->", userAnsw).replaceAll("<!--rightansw-->", newRightAnsw);
    				testContent += content;
				}
			}
		}
		model.addObject("titlename", titleInfo.getTitlename());
		model.addObject("quesallnum", quesallnum);
		model.addObject("quesallscore", quesallscore);
		model.addObject("userrightscore", sumscore);
		model.addObject("userrightnum", rightnum);
		model.addObject("testContent", testContent);
		return model;
	}
}
