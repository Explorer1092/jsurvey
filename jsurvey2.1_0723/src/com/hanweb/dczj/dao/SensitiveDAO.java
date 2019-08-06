package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Sensitive;

public class SensitiveDAO extends BaseJdbcDAO<Integer, Sensitive>{

	public List<Sensitive> findsensitiveList(int start, int pagesize) {
		String strSql = "SELECT i_id,vc_sensitiveword FROM " + Tables.SENSITIVE;
		Query query = this.createQuery(strSql);
		query.setPageNo(start+1);
		query.setPageSize(pagesize);
		List<Sensitive> sensitiveList = null;
		sensitiveList = this.queryForEntities(query);
		if(sensitiveList != null){
			return sensitiveList;
		}
		return null;
	}

	public int getcount() {
		String strSql = "";
		strSql = "SELECT count(1) FROM " + Tables.SENSITIVE;
		Query query = this.createQuery(strSql);
		int num = this.queryForInteger(query);
		return num;
	}

	public int findCountByName(String vc_sensitiveword) {
		String strSql = "";
		strSql = "SELECT count(1) FROM " + Tables.SENSITIVE +" WHERE vc_sensitiveword =:vc_sensitiveword";
		Query query = this.createQuery(strSql);
		query.addParameter("vc_sensitiveword", vc_sensitiveword);
		int num = this.queryForInteger(query);
		return num;
	}

}
