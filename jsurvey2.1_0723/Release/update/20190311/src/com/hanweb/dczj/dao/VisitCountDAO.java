package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.VisitCount;

public class VisitCountDAO extends BaseJdbcDAO<Integer, VisitCount>{

	String headSql = "SELECT iid,dczjid,visitcount FROM "+ Tables.VISITCOUNT;
	
	public VisitCount findVistCount(String dczjid) {
		String sql = this.headSql + " WHERE dczjid =:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntity(query);
	}

	
}
