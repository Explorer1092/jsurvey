package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.DisplayConfig;

public class DisplayConfigDAO extends BaseJdbcDAO<Integer, DisplayConfig>{

	private String getSql() {
		String sql = "SELECT iid,dczjid,isprogress,istitlenumber,chooseframe_style,isopencontent,contentsize,cssstyle,isshowscore FROM " + Tables.JSURVEYDISPLAYCONFIG;
		return sql;
	}
	
	public DisplayConfig findEntityByDczjid(Integer dczjid) {
		String sql = this.getSql() + " WHERE dczjid =:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntity(query);
	}

}
