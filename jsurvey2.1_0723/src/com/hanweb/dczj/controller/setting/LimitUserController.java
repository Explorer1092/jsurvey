package com.hanweb.dczj.controller.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.LimitLoginUser;
import com.hanweb.dczj.entity.LimitOpenUser;
import com.hanweb.dczj.service.LimitLoginUserService;
import com.hanweb.dczj.service.LimitOpenUserService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/setting")
public class LimitUserController {

	@Autowired
	LimitLoginUserService limitLoginUserService;
	@Autowired
	LimitOpenUserService limitOpenUserService;
	@Autowired
	TitleInfoService titleInfoService;
	
	@ResponseBody
	@RequestMapping("limitlogin_submit")
	public JsonResult loginSubmit(String dczjid, String names, String limittype) {
		JsonResult jsonResult = JsonResult.getInstance();
		
		LimitLoginUser limitLoginUser = new LimitLoginUser();
		limitLoginUser.setDczjid(NumberUtil.getInt(dczjid));
		limitLoginUser.setLimitids(names);
		limitLoginUser.setLimittype(NumberUtil.getInt(limittype));//限制类型为：机构
		if(limitLoginUserService.insertEntity(limitLoginUser)) {
			jsonResult.setSuccess(true);
		}else {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		//改变发布状态
		titleInfoService.setUpdateHtml(dczjid, 0);
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("limitopen_submit")
	public JsonResult openSubmit(String dczjid, String names, String limittype) {
		JsonResult jsonResult = JsonResult.getInstance();
		
		LimitOpenUser limitOpenUser = new LimitOpenUser();
		limitOpenUser.setDczjid(NumberUtil.getInt(dczjid));
		limitOpenUser.setLimitids(names);
		limitOpenUser.setLimittype(NumberUtil.getInt(limittype));//限制类型为：机构
		if(limitOpenUserService.insertEntity(limitOpenUser)) {
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			String userIp = IpUtil.getIp();
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将jsurveyid为"+dczjid+"的指定公开用户限制类型修改为"+limittype+"限制ids为"+names));
			jsonResult.setSuccess(true);
		}else {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		//改变发布状态
		titleInfoService.setUpdateHtml(dczjid, 0);
		return jsonResult;
	}
}
