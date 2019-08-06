package com.hanweb.dczj.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Dczj_Setting;

public class SettingDao extends BaseJdbcDAO<Integer, Dczj_Setting>{

	public String getSql() {
		String sql = "SELECT iid,dczjid,type,isstart,starttime,isend,endtime,islimituser,codes,iscode,submitlimit,limittype,limittime,limitnumber,isopen,isjump,jumpurl,isprize,prizetime FROM "+ Tables.CONFIG;
		return sql;
	}
	
	public Dczj_Setting getEntityBydczjid(String dczjid){
		String sql = "SELECT iid,dczjid,type,isstart,starttime,isend,endtime,islimituser"
				+ ",codes,iscode,submitlimit,limittype,limittime,limitnumber,isopen,isjump,jumpurl,isprize,prizetime FROM "
				+ Tables.CONFIG + " WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", NumberUtil.getInt(dczjid));
		return queryForEntity(query);
	}
	
	public Integer modifyColumnById(String name, String value, Integer dczjid ,Integer type) {
		String sql = "UPDATE "+ Tables.CONFIG +" SET "+name+"=:value WHERE dczjid =:dczjid";
		Query query = createQuery(sql);
		//0 数字型
		if(type == 0) {
			query.addParameter("value", NumberUtil.getInt(value));
		}else if(type == 1) {
			//1日期型
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = sdf.parse(value);
			} catch (ParseException e) {
				return 0;
			}
			query.addParameter("value", date);
		}else if(type == 2) {
			//3字符型
			query.addParameter("value", value);
		}
		query.addParameter("dczjid", dczjid);
		return this.execute(query);
	}
	
	/**
	 * 更新抽奖状态
	 * @param dczjid
	 * @param isPrize
	 * @return
	 */
	public Integer modifyIsPrize(String dczjid, Integer isprize) {
		String sql = "UPDATE "+ Tables.CONFIG +" SET isprize=:isprize WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("isprize", isprize);
		return execute(query);
	}
	
}
