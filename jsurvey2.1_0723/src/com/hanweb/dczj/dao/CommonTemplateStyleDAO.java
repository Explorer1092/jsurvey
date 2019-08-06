package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.CommonTemplateStyle;

public class CommonTemplateStyleDAO extends BaseJdbcDAO<Integer, CommonTemplateStyle>{

	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,webid,templatename,type,dczjid,state,styleimgname,liststyle,resultstyle,phoneliststyle,phoneresultstyle FROM "
				+ Tables.DCZJCOMMONTEMPSTYLE;
		return sql;
	}
	
	public List<CommonTemplateStyle> findListByWebid(int webid, int type) {
		String sql = this.getSql() + " WHERE state = 0 AND webid =:webid AND type =:type ORDER BY iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("type", type);
		return this.queryForEntities(query);
	}

}
