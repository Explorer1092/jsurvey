package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.QuesBankTitleInfoDAO;
import com.hanweb.dczj.entity.QuesBankTitleInfo;
import com.hanweb.dczj.entity.TitleInfo;

public class QuesBankTitleInfoService {
	@Autowired
	private QuesBankTitleInfoDAO quesBankTitleInfoDAO;

	public List<QuesBankTitleInfo> findInfoList(String quesbankname, int page, int limit) {
		return quesBankTitleInfoDAO.findInfoList(quesbankname,page,limit);
	}

	public int findNum(String quesbankname) {
		return quesBankTitleInfoDAO.findNum(quesbankname);
	}

	public List<QuesBankTitleInfo> findInfoListByWebid(int webid, String quesbankname,
			int page, int limit) {
		return quesBankTitleInfoDAO.findInfoListByWebid(webid,quesbankname,page,limit);

	}

	public int findNumByWebid(String quesbankname, int webid) {
		return quesBankTitleInfoDAO.findNumByWebid(quesbankname,webid);

	}

	public List<QuesBankTitleInfo> findTitleByWebId(Integer webid) {
		return quesBankTitleInfoDAO.findTitleByWebId(webid);
	}

	public int add(QuesBankTitleInfo infoEn) {
System.out.println("===service---add");
		return quesBankTitleInfoDAO.insert(infoEn);
	}

	public boolean delete(int dczjid) {
		return quesBankTitleInfoDAO.delete(dczjid);
	}

	public QuesBankTitleInfo getEntity(int iid) {
		return quesBankTitleInfoDAO.queryForEntityById(iid);
	}
	

}
