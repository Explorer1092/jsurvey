package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.SurveyMobilelogDAO;
import com.hanweb.dczj.entity.Mobilelog;

public class SurveyMobilelogService {

	@Autowired
	private SurveyMobilelogDAO surveyMobilelogDAO;

	public String[][] getDate(String formid, String mobile) {
		return surveyMobilelogDAO.getDate(formid,mobile);
	}
	public String[][] getDate2(String topicid, String mobile) {
		return surveyMobilelogDAO.getDate2(topicid,mobile);
	}
	public boolean insert(Mobilelog mobileLog) {
		return surveyMobilelogDAO.insert(mobileLog) > 0 ;
	}
	public String[][] getDate3(String voteid, String mobile) {
		return surveyMobilelogDAO.getDate3(voteid,mobile);
	}
}
