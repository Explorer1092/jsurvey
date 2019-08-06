package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.ThanksSetting;

public class ThanksSettingDAO extends BaseJdbcDAO<Integer, ThanksSetting>{

	public String getSql() {
		String sql = "SELECT iid,dczjid,thankscontent,jumpbtn,btnname,jumpurl,isdetail,isjump FROM "+Tables.THANKSSETTING;
		return sql;
	}
	
	public ThanksSetting getSettingBydczjid(Integer dczjid) {
		String sql = getSql()+" WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return queryForEntity(query);
	}
	
	public Integer updateBydczjid(ThanksSetting thanksSetting) {
		String sql = "UPDATE "+Tables.THANKSSETTING+" SET "
				+ "thankscontent=:thankscontent,jumpbtn=:jumpbtn,btnname=:btnname,jumpurl=:jumpurl,isdetail=:isdetail,isjump=:isjump WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("thankscontent", thanksSetting.getThankscontent());
		query.addParameter("jumpbtn", thanksSetting.getJumpbtn());
		query.addParameter("btnname", thanksSetting.getBtnname());
		query.addParameter("jumpurl", thanksSetting.getJumpurl());
		query.addParameter("dczjid", thanksSetting.getDczjid());
		query.addParameter("isdetail", thanksSetting.getIsdetail());
		query.addParameter("isjump", thanksSetting.getIsjump());
		return execute(query);
	}
	
}
