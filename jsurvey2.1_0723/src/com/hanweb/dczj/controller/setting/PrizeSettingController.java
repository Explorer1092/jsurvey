package com.hanweb.dczj.controller.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.PrizeSetting;
import com.hanweb.dczj.service.PrizeSettingService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/setting")
public class PrizeSettingController {

	@Autowired
	PrizeSettingService prizeSettingService;
	@Autowired
	TitleInfoService titleInfoService;
	
	@RequestMapping("prize_opr")
	public ModelAndView setPrize(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/prize_opr");
		List<PrizeSetting> prizeSettings = prizeSettingService.getSettingBydczjid(dczjid);
		if(prizeSettings == null || prizeSettings.size()==0) {
			prizeSettingService.init(dczjid);
			prizeSettings = prizeSettingService.getSettingBydczjid(dczjid);
		}
		modelAndView.addObject("prizeSettings", prizeSettings);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@RequestMapping("show_submit")
	@ResponseBody
	public JsonResult showSubmit() {
		JsonResult jsonResult = JsonResult.getInstance();
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		String userIp = IpUtil.getIp();
		String dczjid = SpringUtil.getRequest().getParameter("dczjid");
		float probability1 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_1"));
		float probability2 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_2"));
		float probability3 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_3"));
		float probability4 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_4"));
		float probability5 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_5"));
		float probability6 = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_6"));
		float number = probability1 + probability2 + probability3 + probability4 + probability5 + probability6;
		if(number > 100){
			return jsonResult.setSuccess(false).setMessage("奖品概率总和不能大于100");
		}
		
		boolean bl = false;
		for(int i=1;i<=6;i++){
			int i_id = NumberUtil.getInt(SpringUtil.getRequest().getParameter("prize_id_"+i));
			String prize_name = SpringUtil.getRequest().getParameter("prize_name_"+i);
			int prize_number =  NumberUtil.getInt(SpringUtil.getRequest().getParameter("prize_number_"+i));
			String prize_remainder = SpringUtil.getRequest().getParameter("prize_remainder_"+i);
			float probability = NumberUtil.getFloat(SpringUtil.getRequest().getParameter("prize_probability_"+i));
			PrizeSetting drawPrizeEn = new PrizeSetting();
			drawPrizeEn.setIid(i_id);
			//drawPrizeEn.setI_formid(i_formid);
			drawPrizeEn.setPrizeid(i);
			drawPrizeEn.setPrizename(prize_name);
			drawPrizeEn.setPrizenumber(prize_number);
			if(StringUtil.isNotEmpty(prize_remainder)){
				drawPrizeEn.setPrizeremainder(NumberUtil.getInt(prize_remainder));
			}else{
				drawPrizeEn.setPrizeremainder(prize_number);
			}
			drawPrizeEn.setPrizeprobability(probability);
			bl = prizeSettingService.modify(drawPrizeEn);
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将id为"+i_id+"奖品设置修改为名称"+prize_name+",奖品数量"+prize_number));
			if(!bl){
				return jsonResult.setSuccess(false).setMessage("操作失败，请联系管理员！");
			}
		}
		//改变发布状态
		titleInfoService.setUpdateHtml(dczjid, 0);
		jsonResult.setSuccess(true);
		return jsonResult;
	}
}
