package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.DrawWinnersInfoDAO;
import com.hanweb.dczj.entity.Draw_WinnersInfo;

public class DrawWinnersInfoService {

	@Autowired
	private DrawWinnersInfoDAO drawWinnersInfoDAO;
	
	public int insert(Draw_WinnersInfo winnersInfo) {
		return drawWinnersInfoDAO.insert(winnersInfo);
	}

	public int findUserNum(String loginName, String formId) {
		return drawWinnersInfoDAO.findUserNum(loginName,formId);
	}
	
	/**
	 * 按照调查征集id获取参与抽奖的人数
	 * @param dczjid
	 * @return
	 */
	public int findDrawNumByDczjid(String dczjid) {
		return drawWinnersInfoDAO.findDrawNumByDczjid(dczjid);
	}
	
	/**
	 * 按dczjid和奖品名称获取集合
	 * @param dczjid
	 * @param prizename
	 * @return
	 */
	public List<Draw_WinnersInfo> findDrawsByDczjidAndPrizename(String page, String limit,String dczjid, String prizename) {
		return drawWinnersInfoDAO.findDrawsByDczjidAndPrizename(page, limit, dczjid, prizename);
	}
	
	/**
	 * 按dczjid和奖品名称获取投票总数
	 * @param dczjid
	 * @param prizename
	 * @return
	 */
	public Integer findDrawNumByDczjidAndPrizename(String dczjid, String prizename) {
		return drawWinnersInfoDAO.findDrawNumByDczjidAndPrizename(dczjid, prizename);
	}

}
