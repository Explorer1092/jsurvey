package com.hanweb.dczj.controller.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.ThanksSetting;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.ThanksSettingService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/setting")
public class ThanksSettingController {
	
	@Autowired
	ThanksSettingService thanksSettingService;
	@Autowired
	SettingService settingService;
	@Autowired
	TitleInfoService titleInfoService;

	@RequestMapping("thankscontent_opr")
	public ModelAndView setThanksContent(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/thankscontent_opr");
		ThanksSetting thanksSetting = thanksSettingService.getSettingBydczjid(dczjid);
		if(thanksSetting == null) {
			thanksSetting = new ThanksSetting();
		}
		TitleInfo titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid));
		int type = 0;
		if(titleInfo != null ) {
			type = titleInfo.getType();
		}
		modelAndView.addObject("type", type);
		modelAndView.addObject("thanksSetting", thanksSetting);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("add_thankscontent")
	public JsonResult submitThanksSetting(String dczjid, String thankscontent, String jumpbtn, String btnname, String jumpurl,Integer isdetail,Integer isjump) {
		JsonResult jsonResult = JsonResult.getInstance();
		ThanksSetting thanksSetting = new ThanksSetting();
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		String userIp = IpUtil.getIp();
		thanksSetting.setDczjid(NumberUtil.getInt(dczjid));
		thanksSetting.setBtnname(btnname);
		thanksSetting.setJumpbtn(jumpbtn);
		thanksSetting.setJumpurl(jumpurl);
		thanksSetting.setThankscontent(thankscontent);
		thanksSetting.setIsdetail(isdetail);
		thanksSetting.setIsjump(isjump);
		if(thanksSettingService.updateEntity(thanksSetting)) {
			jsonResult.set(ResultState.ADD_SUCCESS);
		}else {
			jsonResult.set(ResultState.ADD_FAIL);
		}
		//联动设置中的抽奖开关
		if(jumpbtn != null && jumpbtn.indexOf("1")>0) {
			//若在此开启了抽奖开关
			settingService.modifyIsPrize(dczjid, 1);
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")")
					.setDescription("将jsurveyid为"+dczjid+"的设参表抽奖开关联动修改为"+1));
		}
		if(jumpbtn ==null || jumpbtn.indexOf("1")<0) {
			//若在此未开启抽奖开关
			settingService.modifyIsPrize(dczjid, 0);
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")")
					.setDescription("将jsurveyid为"+dczjid+"的设参表抽奖开关联动修改为"+0));
		}
		
		LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
				.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")")
				.setDescription("将jsurveyid为"+dczjid+"的感谢内容设置修改为感谢内容"+thankscontent+"，按钮组为"+jumpbtn+"，指定跳转地址按钮为"+btnname+",跳转url为"+jumpurl));
		//改变发布状态
		titleInfoService.setUpdateHtml(dczjid, 0);
		return jsonResult;
	}
}
