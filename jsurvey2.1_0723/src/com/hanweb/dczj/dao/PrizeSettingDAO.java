package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.PrizeSetting;

public class PrizeSettingDAO extends BaseJdbcDAO<Integer, PrizeSetting>{

	public String getSql() {
		String sql = "SELECT iid,dczjid,prizeid,prizename,prizenumber,prizeremainder,prizeprobability FROM " + Tables.PRIZESETTING;
		return sql;
	}
	
	public List<PrizeSetting> getSettingBydczjid(Integer dczjid) {
		String sql = getSql()+" WHERE dczjid=:dczjid order by iid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForEntities(query);
	}
	
	public Integer updateById(PrizeSetting prizeSetting) {
		String sql = "UPDATE "+Tables.PRIZESETTING+ " SET prizeid=:prizeid,"
				+ "prizename=:prizename,prizenumber=:prizenumber,prizeremainder=:prizeremainder"
				+ ",prizeprobability=:prizeprobability WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("prizeid", prizeSetting.getPrizeid());
		query.addParameter("prizename", prizeSetting.getPrizename());
		query.addParameter("prizenumber", prizeSetting.getPrizenumber());
		query.addParameter("prizeremainder", prizeSetting.getPrizeremainder());
		query.addParameter("prizeprobability", prizeSetting.getPrizeprobability());
		query.addParameter("iid", prizeSetting.getIid());
		return execute(query);
	}
	
}
