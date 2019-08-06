package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Template;

public class TemplateDAO extends BaseJdbcDAO<Integer, Template>{

	public String getSql() {
		String sql = "SELECT iid,type,pagetype,dczjid,content,path,name FROM "+ Tables.DCZJTEMPLATE;
		return sql;
	}
	
	public List<Template> findTemplatByDczjidAndType(String dczjid, int type) {
		String sql =  this.getSql() + " WHERE dczjid=:dczjid AND type =:type";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("type", type);
		return this.queryForEntities(query);
	}

	public Template findTemplatByDczjidAndTypeAndPagetype(String dczjid, int pagetype) {
		String sql =  this.getSql() + " WHERE dczjid=:dczjid AND pagetype =:pagetype";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("pagetype", pagetype);
		return queryForEntity(query);
	}

}
