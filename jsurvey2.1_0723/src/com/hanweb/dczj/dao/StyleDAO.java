package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Style;

public class StyleDAO extends BaseJdbcDAO<Integer, Style>{

	public String getSql() {
		String sql = "SELECT iid,pcstyle,pcresultstyle,phonestyle,phoneresultstyle FROM "+ Tables.DCZJSTYLE;
		return sql;
	}
	
	/**
	 * 通过DCZJID获取实体
	 * @param dczjid
	 * @return
	 */
	public Style getEntityByDczjid(String dczjid) {
		String sql = this.getSql() + " WHERE dczjid =:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForEntity(query);
	}

}
