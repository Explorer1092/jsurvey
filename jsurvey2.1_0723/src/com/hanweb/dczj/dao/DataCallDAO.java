package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.DataCall;

public class DataCallDAO extends BaseJdbcDAO<Integer, DataCall>{

	private String getSql() {
		String sql = "SELECT iid,webid,createdate,createname,datacall_name,datacall_webids,datacall_webnames,datacall_types,datacall_type"
				+ ",datacall_number,datacall_sorttype,datacall_sort,state,updatehtml,filtercondition FROM " + Tables.DCZJDATACALL;
		return sql;
	}
	
	public boolean UpdateById(DataCall dataCall) {
		String sql = "UPDATE " + Tables.DCZJDATACALL +" SET webid=:webid,createname=:createname,datacall_name=:datacall_name,datacall_webids=:datacall_webids,datacall_webnames=:datacall_webnames,"
				+ "datacall_types=:datacall_types,datacall_type=:datacall_type,datacall_number=:datacall_number,datacall_sorttype=:datacall_sorttype,datacall_sort=:datacall_sort,state=:state,updatehtml=:updatehtml,filtercondition=:filtercondition "
				+ "WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("webid", dataCall.getWebid());
		query.addParameter("createname", dataCall.getCreatename());
		query.addParameter("datacall_name", dataCall.getDatacall_name());
		query.addParameter("datacall_webids", dataCall.getDatacall_webids());
		query.addParameter("datacall_webnames", dataCall.getDatacall_webnames());
		query.addParameter("datacall_types", dataCall.getDatacall_types());
		query.addParameter("datacall_type", dataCall.getDatacall_type());
		query.addParameter("datacall_number", dataCall.getDatacall_number());
		query.addParameter("datacall_sorttype", dataCall.getDatacall_sorttype());
		query.addParameter("datacall_sort", dataCall.getDatacall_sort());
		query.addParameter("state", dataCall.getState());
		query.addParameter("updatehtml", dataCall.getUpdatehtml());
		query.addParameter("filtercondition", dataCall.getFiltercondition());
		query.addParameter("iid", dataCall.getIid());
		if(execute(query)>0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean deleteByid(List<Integer> idList, String state) {
		String where = " iid in(:idList) ";
		UpdateSql query = new UpdateSql(Tables.DCZJDATACALL);
		query.setWhere(where);
		query.addParam("state", state);
		query.addWhereParam("idList", idList);
		return this.update(query);
	}

	public boolean setUpdateHtml(Integer iid, int updateHtml) {
		UpdateSql updateSql;
		if(NumberUtil.getInt(iid) == 0)
			updateSql = new UpdateSql(Tables.DCZJDATACALL,"1=1");
		else
			updateSql = new UpdateSql(Tables.DCZJDATACALL,"iid = "+iid);
		updateSql.addInt("updatehtml", updateHtml);
		return this.update(updateSql);
	}

	public List<DataCall> findAllJact() {
		String sql = this.getSql() + " WHERE state = 0 ORDER BY iid ASC";
		Query query = createQuery(sql);
		return this.queryForEntities(query);
	}
	
}
