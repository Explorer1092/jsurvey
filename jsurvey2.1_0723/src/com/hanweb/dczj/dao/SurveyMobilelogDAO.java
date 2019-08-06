package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.DateUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Mobilelog;

public class SurveyMobilelogDAO extends BaseJdbcDAO<Integer, Mobilelog> {

	public String[][] getDate(String formid, String mobile) {
		String strSql = "SELECT c_createdate FROM " + Tables.MOBILELOG
				+ " WHERE i_formid =:formid "
				+ " AND vc_mobile =:mobile AND c_createdate>= :c_createdate order by c_createdate desc";
		Query query = createQuery(strSql);
		query.addParameter("mobile", mobile);
		query.addParameter("formid", formid);
		String time = DateUtil.getCurrDateTime().substring(0,10)+" 00:00:00";
		query.addParameter("c_createdate", time);
		return this.queryForArrays(query);
	}

	public String[][] getDate2(String topicid, String mobile) {
		String strSql = "SELECT c_createdate FROM " + Tables.MOBILELOG
				+ " WHERE i_topicid =:topicid "
				+ " AND vc_mobile =:mobile  AND c_createdate>= :c_createdate order by c_createdate desc";
		Query query = createQuery(strSql);
		query.addParameter("mobile", mobile);
		query.addParameter("topicid", topicid);
		String time = DateUtil.getCurrDateTime().substring(0,10)+" 00:00:00";
		query.addParameter("c_createdate", time);
		return this.queryForArrays(query);
	}

	public String[][] getDate3(String voteid, String mobile) {
		String strSql = "SELECT c_createdate FROM " + Tables.MOBILELOG
				+ " WHERE i_voteid =:voteid "
				+ " AND vc_mobile =:mobile  AND c_createdate>= :c_createdate order by c_createdate desc";
		Query query = createQuery(strSql);
		query.addParameter("mobile", mobile);
		query.addParameter("voteid", voteid);
		String time = DateUtil.getCurrDateTime().substring(0,10)+" 00:00:00";
		query.addParameter("c_createdate", time);
		return this.queryForArrays(query);
	}

}
