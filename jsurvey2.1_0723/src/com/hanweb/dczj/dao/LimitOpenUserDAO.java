package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.LimitOpenUser;

public class LimitOpenUserDAO extends BaseJdbcDAO<Integer, LimitOpenUser>{

String headsql = "SELECT iid,dczjid,limittype,limitids FROM "+Tables.JUSURVEYLIMITOPEN;
	
	public Integer findCountBydczjid(Integer dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.JUSURVEYLIMITOPEN+" WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForInteger(query);
	}
	
	public Integer updateLimitidsBydczjd(LimitOpenUser limitOpenUser) {
		String sql = "UPDATE "+Tables.JUSURVEYLIMITOPEN+" SET limitids=:limitids WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", limitOpenUser.getDczjid());
		query.addParameter("limitids", limitOpenUser.getLimitids());
		return execute(query);
	}
	
	public LimitOpenUser findEntityBydczjid(Integer dczjid) {
		String sql = headsql+" WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForEntity(query);
	}
}
