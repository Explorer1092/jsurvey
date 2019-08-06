package com.hanweb.support.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.MailSend;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.TemplateParser;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.support.controller.resetpwd.ResetPwd;
import com.hanweb.support.controller.resetpwd.ResetPwdCache;

/**
 * 重置密码
 * 
 * @author 李杰
 *
 */
@Service
public class ResetPwdService {
	private final Log logger = LogFactory.getLog(getClass());

	public void sendResetPwdEmail(ResetPwd resetPwd) throws OperationException {
		Properties emailProp = new Properties(BaseInfo.getRealPath()
				+ "/WEB-INF/config/mail_server.properties");
		String emailHost = emailProp.getString("resetpwd.host");
		String emailUser = emailProp.getString("resetpwd.user");
		String emailPwd = emailProp.getString("resetpwd.pwd");
		String subject = emailProp.getString("resetpwd.subject");
		String form = emailProp.getString("resetpwd.form");
		String formNickname = emailProp.getString("resetpwd.form.nickname");
		String content = emailProp.getString("resetpwd.content");
		if (StringUtil.isEmpty(formNickname)) {
			formNickname = "admin";
		}
		if (StringUtil.isEmpty(subject)) {
			subject = "密码找回";
		}
		MailSend mailSend = new MailSend();
		mailSend.setHostName(emailHost);
		mailSend.setAuthentication(emailUser, emailPwd);
		mailSend.setFrom(form, formNickname);
		mailSend.addTo(resetPwd.getEmail());
		mailSend.setSubject(subject);
		String dateStr = DateUtil.getCurrDate(DateUtil.YYYY_MM_DD_HH_MM_SS);
		String token = Md5Util.encode(resetPwd.getName() + "/" + resetPwd.getToken() + "/" + dateStr);
		String url = BaseInfo.getDomain() + "/resetpwd/change.do?token=" + token;
		mailSend.setHtmlMsg(TemplateParser.parserTemplate(content, "url", url));
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 2);
			ResetPwdCache.add(new ResetPwdCache(token, resetPwd.getName(), calendar.getTime()));
			mailSend.send();
		} catch (Exception e) {
			logger.error("mail send error", e);
			ResetPwdCache.remove(token);
			throw new OperationException("email 发送失败，请联系管理员");
		}
	}
}
