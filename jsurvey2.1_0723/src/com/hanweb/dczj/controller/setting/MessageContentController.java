package com.hanweb.dczj.controller.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.MessageContent;
import com.hanweb.dczj.service.MessageContentService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/setting")
public class MessageContentController {

	@Autowired
	MessageContentService messageContentService;
	@Autowired
	TitleInfoService titleInfoService;
	
	@RequestMapping("message_opr")
	public ModelAndView setMessage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/message_opr");
		MessageContent messageContent = messageContentService.getEntityBydczjid(dczjid);
		if(messageContent == null) {
			messageContentService.init(dczjid);
			messageContent = messageContentService.getEntityBydczjid(dczjid);
		}
		modelAndView.addObject("messageContent", messageContent);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("add_sumbit")
	public JsonResult addSubmit(String iid, String content, String dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		MessageContent messageContent = new MessageContent();
		messageContent.setIid(NumberUtil.getInt(iid));
		messageContent.setContent(content);
		int bl = 0;
		try {
			bl = messageContentService.modify(messageContent);
			if(bl > 0) {
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				String userIp = IpUtil.getIp();
				LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
						.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将id为"+iid+"短信内容验证修改为"+content));
				//改变发布状态
				titleInfoService.setUpdateHtml(dczjid, 0);
				return jsonResult.setSuccess(true);
			}else {
				return jsonResult.setSuccess(false).setMessage("操作失败，请联系管理员！");
			}
		} catch (Exception e) {
			return jsonResult.setSuccess(false).setMessage("操作失败，请联系管理员！");
		}
	}
}
