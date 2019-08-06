package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Draw_WinnersInfo;

public class DrawWinnersInfoDAO extends BaseJdbcDAO<Integer, Draw_WinnersInfo>{
	
	String headSql = "SELECT iid,dczjid,prizeid,prizename,winnerid,loginname,winnername,wintime FROM "+ Tables.DRAWWINNERSINFO;

	public int findUserNum(String loginName, String dczjid) {
		String sql = "SELECT count(1) FROM "+ Tables.DRAWWINNERSINFO + " WHERE dczjid =:dczjid AND loginname =:loginName ";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("loginName", loginName);
		return this.queryForInteger(query);
	}
	
	/**
	 * 获取该问卷参与抽奖的人数
	 * @param dczjid
	 * @return
	 */
	public int findDrawNumByDczjid(String dczjid) {
		String sql = "SELECT count(1) FROM "+ Tables.DRAWWINNERSINFO + " WHERE dczjid =:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	
	/**
	 * 按dczjid和奖品名称获取集合
	 * @param dczjid
	 * @param prizename
	 * @return
	 */
	public List<Draw_WinnersInfo> findDrawsByDczjidAndPrizename(String page, String limit,String dczjid, String prizename) {
		String sql = headSql +" WHERE dczjid =:dczjid ";
		if(StringUtil.isNotEmpty(prizename)) {
			sql = sql + " AND prizename=:prizename ";
		}
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		if(StringUtil.isNotEmpty(prizename)) {
			query.addParameter("prizename", prizename);
		}
		query.setPageNo(NumberUtil.getInt(page));
		query.setPageSize(NumberUtil.getInt(limit));
		return queryForEntities(query);
	}
	
	/**
	 * 按dczjid和奖品名称获取投票总数
	 * @param dczjid
	 * @param prizename
	 * @return
	 */
	public Integer findDrawNumByDczjidAndPrizename(String dczjid, String prizename) {
		String sql = "SELECT COUNT(1) FROM "+ Tables.DRAWWINNERSINFO +" WHERE dczjid =:dczjid ";
		if(StringUtil.isNotEmpty(prizename)) {
			sql = sql + " AND prizename=:prizename ";
		}
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		if(StringUtil.isNotEmpty(prizename)) {
			query.addParameter("prizename", prizename);
		}
		return queryForInteger(query);
	}

}
