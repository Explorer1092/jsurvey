package com.hanweb.dczj.service;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.mq.BaseMessageListener;
import com.hanweb.common.mq.ReceiveManager;
import com.hanweb.common.util.FileUtil;

public class RegisterMqListenerService {
	public void registerDczjMq(){
		/**
		 * 在线调查订阅
		 */
		ReceiveManager.registerTopicListener("1", "com.hanweb.dczj.survey.topic",
				new BaseMessageListener() {
			@Override
			public String handleMessage(Map<String, Object> paramMap, MapMessage paramMapMessage,Session session){
				try {
					String surveyloadPath = BaseInfo.getRealPath()+paramMapMessage.getStringProperty("surveyloadPath");
					String surveycontent = paramMapMessage.getStringProperty("surveycontent");
					FileUtil.writeStringToFile(surveyloadPath, surveycontent);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		ReceiveManager.registerTopicListener("1", "com.hanweb.dczj.phonesurvey.topic",
				new BaseMessageListener() {
			@Override
			public String handleMessage(Map<String, Object> paramMap, MapMessage paramMapMessage,Session session){
				try {
					String phoneSurveyloadPath = BaseInfo.getRealPath()+paramMapMessage.getStringProperty("phoneSurveyloadPath");
					String phoneSurveyContent = paramMapMessage.getStringProperty("phoneSurveyContent");
					FileUtil.writeStringToFile(phoneSurveyloadPath, phoneSurveyContent);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}
