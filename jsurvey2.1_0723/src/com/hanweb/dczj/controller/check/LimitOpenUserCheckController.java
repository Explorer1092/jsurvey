package com.hanweb.dczj.controller.check;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.LimitOpenUser;
import com.hanweb.dczj.service.LimitOpenUserService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("check/open")
public class LimitOpenUserCheckController {
	
	@Autowired
	SettingService settingService;
	@Autowired
	LimitOpenUserService limitOpenUserService;
	@Autowired
	UserService userService;

	@ResponseBody
	@RequestMapping("user")
	public JsonResult checkOpenUser(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		
		Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
		if(setting!=null) {
			if(setting.getIsopen()==1) {
				LimitOpenUser limitOpenUser = limitOpenUserService.findEntityBydczjid(dczjid);
				if(limitOpenUser!=null) {
					if(StringUtil.isEmpty(limitOpenUser.getLimitids())) {
						//开启了公开结果，但未指定公开用户，默认全部公开
						result.setSuccess(true);
						return result;
					}else {
						HttpSession session = SpringUtil.getRequest().getSession();
						String sessionCode =null;
						if(session!=null){
							 sessionCode = (String)session.getAttribute("surveyloginname_"+dczjid);
						}
						User user = userService.findByLoginName(sessionCode);
						if(user == null) {
							result.setSuccess(false).setMessage("您暂无权限查看");
						}else {
							String[] ids = limitOpenUser.getLimitids().split(",");
							if(limitOpenUser.getLimittype()==0) {
								for(int i=0;i<ids.length;i++) {
									if(StringUtil.equals(user.getGroupId()+"", ids[i])) {
										result.setSuccess(true);
										return result;
									}
								}
								//循环结束，该用户所在机构不在公开范围内
								result.setSuccess(false).setMessage("您暂无权限查看");
							}else {
								//其他指定用户方式不存在
								result.setSuccess(false).setMessage("您暂无权限查看");
							}
						}
					}
				}else {
					//开启了公开结果，但未指定公开用户，默认全部公开
					result.setSuccess(true);
					return result;
				}
			}else {
				//设置中未开启公开结果
				result.setSuccess(false).setMessage("该问卷暂不公开结果");
			}
		}else {
			//未查询到相关问卷设置
			result.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("checkResult")
	public  String checkResult(String dczjid) {
		String pfileExist ="";
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		String resultPath = webPath + "result_"+ dczjid + ".html";
		 File pfile = new File(resultPath);
		 if(pfile.exists()){
			 pfileExist ="1";
		 }else{
			 pfileExist ="0";
		 }
		return pfileExist;
	}
	

	@ResponseBody
	@RequestMapping("checkPhoneResult")
	public  String checkPhoneResult(String dczjid) {
		String pfileExist ="";
		String webPath = BaseInfo.getRealPath() + "/jsurvey/questionnaire/";  //发布的路径
		String resultPath = webPath + "phoneresult_"+ dczjid + ".html";
		 File pfile = new File(resultPath);
		 if(pfile.exists()){
			 pfileExist ="1";
		 }else{
			 pfileExist ="0";
		 }
		return pfileExist;
	}
}
