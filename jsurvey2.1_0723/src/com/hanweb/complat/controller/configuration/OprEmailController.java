package com.hanweb.complat.controller.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.app.SystemMailSender;
import com.hanweb.complat.entity.SystemEmail;

/**
 * 发送邮件
 * @author 李杰
 *
 */
@Controller
@Permission(module = "configuration")
@RequestMapping("manager/configuration/email")
public class OprEmailController {

	/**
	 * 打开邮件设置页面
	 * 
	 * @return
	 */
	@RequestMapping("modify_show")
	public ModelAndView showModify() {
		ModelAndView modelAndView = new ModelAndView("/complat/configuration/email_opr");
		Properties properties = new Properties(BaseInfo.getRealPath() + "/WEB-INF/config/mail_server.properties");
		String host = properties.getString("resetpwd.host");
		String user = properties.getString("resetpwd.user");
		String pwd = properties.getString("resetpwd.pwd");
		String subject = properties.getString("resetpwd.subject");
		String form = properties.getString("resetpwd.form");
		String formNickname = properties.getString("resetpwd.form.nickname");
		String content = properties.getString("resetpwd.content");

		modelAndView.addObject("host", host);
		modelAndView.addObject("user", user);
		modelAndView.addObject("pwd", pwd);
		modelAndView.addObject("subject", subject);
		modelAndView.addObject("form", form);
		modelAndView.addObject("form_nickname", formNickname);
		modelAndView.addObject("content", content);
		modelAndView.addObject("url", "modify_submit.do");

		return modelAndView;
	}

	/**
	 * 提交邮件设置修改
	 * 
	 * @param systemEmail
	 * @return
	 */
	@RequestMapping("modify_submit")
	@ResponseBody
	public JsonResult addSubmit(SystemEmail systemEmail) {
		Properties properties = new Properties(BaseInfo.getRealPath() + "/WEB-INF/config/mail_server.properties");
		properties.setProperty("resetpwd.host", systemEmail.getHost());
		properties.setProperty("resetpwd.user", systemEmail.getUser());
		properties.setProperty("resetpwd.pwd", systemEmail.getPwd());
		properties.setProperty("resetpwd.subject", systemEmail.getSubject());
		properties.setProperty("resetpwd.form", systemEmail.getForm());
		properties.setProperty("resetpwd.form.nickname", systemEmail.getFormNickname());
		properties.setProperty("resetpwd.content", systemEmail.getContent());
		properties.save();
		SystemMailSender.loadProp();
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.set(ResultState.MODIFY_SUCCESS);
		return jsonResult;
	}
}
