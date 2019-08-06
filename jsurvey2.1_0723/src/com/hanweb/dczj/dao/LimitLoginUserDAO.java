package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.LimitLoginUser;

public class LimitLoginUserDAO extends BaseJdbcDAO<Integer, LimitLoginUser>{

	String headsql = "SELECT iid,dczjid,limittype,limitids FROM "+Tables.JUSURVEYLIMITLOGIN;
	
	public Integer findCountBydczjid(Integer dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.JUSURVEYLIMITLOGIN+" WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForInteger(query);
	}
	
	public Integer updateLimitidsBydczjd(Integer dczjid,String limitids,Integer limittype) {
		String sql = "UPDATE "+Tables.JUSURVEYLIMITLOGIN+" SET limitids=:limitids,limittype=:limittype WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("limitids", limitids);
		query.addParameter("limittype", limittype);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}
	
	public LimitLoginUser findEntityBydczjid(Integer dczjid,Integer limittype) {
		String sql = "";
		if (limittype==null) {
			 sql = headsql+" WHERE dczjid=:dczjid ";
		}else {
			 sql = headsql+" WHERE dczjid=:dczjid and limittype=:limittype";	
		}
		
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		if (limittype!=null) {
			query.addParameter("limittype", limittype);
		}		
		return queryForEntity(query);
	}
	
}
