package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.MessageContentDAO;
import com.hanweb.dczj.entity.MessageContent;

public class MessageContentService {

	@Autowired
	MessageContentDAO messageContentDAO;
	
	/**
	 * 通过dczjid获取实体
	 * @param dczjid
	 * @return
	 */
	public MessageContent getEntityBydczjid(String dczjid) {
		return messageContentDAO.getEntityBydczjid(NumberUtil.getInt(dczjid));
	}
	
	public Integer init(String dczjid) {
		MessageContent messageContent = new MessageContent();
		messageContent.setDczjid(NumberUtil.getInt(dczjid));
		messageContent.setContent("你的短信验证码为：{codenumber}，谢谢您的参与");
		return messageContentDAO.insert(messageContent);
	}
	
	public Integer modify(MessageContent messageContent) {
		return messageContentDAO.modify(messageContent);
	}
}
