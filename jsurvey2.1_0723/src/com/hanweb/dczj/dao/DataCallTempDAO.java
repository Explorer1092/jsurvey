package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.DataCallTemplate;

public class DataCallTempDAO extends BaseJdbcDAO<Integer, DataCallTemplate>{

	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,datacallid,type,template FROM " + Tables.DCZJDATATEMP;
		return sql;
	}
	
	/**
	 * 通过jactid和type查找实体
	 * @param jactid
	 * @param type
	 * @return
	 */
	public DataCallTemplate findTemplate(String datacallid, String type) {
		String sql = this.getSql() + " WHERE datacallid=:datacallid ";
		if(StringUtil.isNotEmpty(type)){
			sql += " AND type =:type";
		}
		Query query = createQuery(sql);
		query.addParameter("datacallid", datacallid);
		if(StringUtil.isNotEmpty(type)){
			query.addParameter("type", type);
		}
		return this.queryForEntity(query);
	}
}
