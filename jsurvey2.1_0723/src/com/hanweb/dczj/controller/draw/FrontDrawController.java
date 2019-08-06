package com.hanweb.dczj.controller.draw;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.Draw_WinnersInfo;
import com.hanweb.dczj.entity.PrizeSetting;
import com.hanweb.dczj.service.DrawWinnersInfoService;
import com.hanweb.dczj.service.PrizeSettingService;
import com.hanweb.dczj.service.SettingService;

@Controller
@RequestMapping("front/draw")
public class FrontDrawController{

	@Autowired
	PrizeSettingService prizeSettingService;
	@Autowired
	UserService userService;
	@Autowired
	DrawWinnersInfoService drawWinnersInfoService;
	@Autowired
	SettingService settingService;
	
	@RequestMapping("godrawpage")
	public ModelAndView goDrawPage(String dczjid){
		ModelAndView modelAndView = new ModelAndView("dczj/draw/drawluck");
		dczjid = StringUtil.getSafeString(dczjid);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@RequestMapping("gophonedrawpage")
	public ModelAndView goPhoneDrawPage(String dczjid){
		ModelAndView modelAndView = new ModelAndView("dczj/draw/drawphoneluck");
		dczjid = StringUtil.getSafeString(dczjid);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@RequestMapping("getPrizeList")
	@ResponseBody
	public JsonResult getPrizeList(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		dczjid = StringUtil.getSafeString(dczjid);
		
		List<PrizeSetting> drawList = prizeSettingService.getSettingBydczjid(dczjid);
		String prizelist = "[";
		int rank = 0;
		if(drawList != null && drawList.size()>0){
			for(PrizeSetting prizeEn : drawList){
				rank ++;
				prizelist += "{\"id\": "+prizeEn.getIid()+",\"name\": \""+prizeEn.getPrizename()+"\",\"rank\":"+rank+"},";
			}
		}
		prizelist +="{\"id\": 0,\"name\": \"谢谢参与\",\"rank\":7}]";
		jsonResult.addParam("prizelist", prizelist);
		return jsonResult.setSuccess(true);
	}
	
	@RequestMapping("godraw")
	@ResponseBody
	public JsonResult goDraw(String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		Cache cache = CacheManager.getInstance("dczj_draw");
		HttpSession session = SpringUtil.getRequest().getSession();
		String unid = "";
		String submitCheck = "";
		
		if(session!=null){
			unid = (String)session.getAttribute("loginunid_"+dczjid);
			submitCheck = (String)session.getAttribute("check_"+unid+"_"+dczjid);
		}
		if(StringUtil.isEmpty(submitCheck) || !StringUtil.equals(submitCheck, "1")){
			return jsonResult.setSuccess(false).setMessage("请先完成调查!");
		}
		
		Dczj_Setting setting = settingService.getEntityBydczjid(dczjid);
		if(setting.getPrizetime()==0) {
			//仅一次
			String isDraw = cache.get("dczj_"+dczjid+"_user_"+IpUtil.getIp(), new Type<String>() {});
			if(StringUtil.isNotEmpty(isDraw) && StringUtil.equals(isDraw, "1")){
				return jsonResult.setSuccess(false).setMessage("对不起，您已经参与过抽奖!");
			}
		}else {
			//随调查次数
			String isDraw = cache.get("dczj_"+dczjid+"_user_"+unid, new Type<String>() {});
			if(StringUtil.isNotEmpty(isDraw) && StringUtil.equals(isDraw, "1")){
				return jsonResult.setSuccess(false).setMessage("对不起，您已经参与过抽奖!");
			}
		}
		List<PrizeSetting> drawList = prizeSettingService.getSettingBydczjid(dczjid);
		String[] prizesId = new String[10000]; 
		int index = (int) (Math.random() * 10000);
		int num = 0;
		for (PrizeSetting prize : drawList) {  
		    Integer probability = NumberUtil.getInt(prize.getPrizeprobability()*100);
		    //循环商品概率
		    for (int i = 0; i < probability; i++) {
		        prizesId[num] = prize.getIid()+"";
		        num ++;
		    }
		}
		cache.put("dczj_"+dczjid+"_user_"+unid, "1");
		String prizeId = prizesId[index]; 
		Draw_WinnersInfo winnersInfo = new Draw_WinnersInfo();
		winnersInfo.setDczjid(NumberUtil.getInt(dczjid));
		synchronized (cache) {
			PrizeSetting drawEn = prizeSettingService.getEntityByIid(NumberUtil.getInt(prizeId));
			if(drawEn != null){
				int prizeNum = drawEn.getPrizeremainder() - 1;
				if(prizeNum >= 0){
					winnersInfo.setPrizeid(NumberUtil.getInt(prizeId));
					winnersInfo.setPrizename(drawEn.getPrizename());
					drawEn.setPrizeremainder(prizeNum);
					prizeSettingService.updateEntity(drawEn);
					jsonResult.addParam("prizeId", drawEn.getIid());
					jsonResult.addParam("prizeName", drawEn.getPrizename());
				}else{
					winnersInfo.setPrizeid(0);
					winnersInfo.setPrizename("谢谢参与");
					jsonResult.addParam("prizeId", 0);
					jsonResult.addParam("prizeName", "谢谢参与");
				}
			}else{
				winnersInfo.setPrizeid(0);
				winnersInfo.setPrizename("谢谢参与");
				jsonResult.addParam("prizeId", 0);
				jsonResult.addParam("prizeName", "谢谢参与");
			}
		}
		winnersInfo.setLoginname(unid);
//		winnersInfo.setWinnerid(user.getIid());
//		winnersInfo.setWinnername(user.getName());
		winnersInfo.setWintime(new Date());
		drawWinnersInfoService.insert(winnersInfo);
		cache.put("dczj_"+dczjid+"_user_"+IpUtil.getIp(), "1");
		return jsonResult.setSuccess(true);
	}
}
