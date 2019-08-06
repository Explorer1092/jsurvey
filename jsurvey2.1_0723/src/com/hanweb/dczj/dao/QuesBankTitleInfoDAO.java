package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.QuesBankTitleInfo;

public class QuesBankTitleInfoDAO extends BaseJdbcDAO<Integer, QuesBankTitleInfo>{
	
	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,webid,orderid,username,quesbankname,creator,createtime,isdelete FROM " + Tables.DCZJQUEABANKTITLEINFO;
		return sql;
	}
	

	public List<QuesBankTitleInfo> findInfoList(String quesbankname, int page, int limit) {
		String sql = this.getSql() + " WHERE 1 = 1 AND isdelete = 0 ";

		if(StringUtil.isNotEmpty(quesbankname)) {
			sql += " AND quesbankname LIKE :quesbankname";
		}
		sql += " ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);

		if(StringUtil.isNotEmpty(quesbankname)) {
			query.addParameter("quesbankname", quesbankname,LikeType.LR);
		}
		query.setPageNo(page);
		query.setPageSize(limit);
		return this.queryForEntities(query);
	}

	public int findNum(String quesbankname) {
		String sql = "SELECT count(1) FROM " + Tables.DCZJQUEABANKTITLEINFO +" WHERE 1 = 1 AND isdelete = 0 ";
		if(StringUtil.isNotEmpty(quesbankname)) {
			sql += " AND quesbankname LIKE :quesbankname";
		}
		Query query = createQuery(sql);
		if(StringUtil.isNotEmpty(quesbankname)) {
			query.addParameter("quesbankname", quesbankname,LikeType.LR);
		}
		return this.queryForInteger(query);
	}

	public List<QuesBankTitleInfo> findInfoListByWebid(int webid, String quesbankname,
			int page, int limit) {
		String sql = this.getSql() + " WHERE webid =:webid AND isdelete = 0  ";
		if(StringUtil.isNotEmpty(quesbankname)) {
			sql += " AND quesbankname LIKE :quesbankname";
		}
		sql += " ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		if(StringUtil.isNotEmpty(quesbankname)) {
			query.addParameter("quesbankname", quesbankname,LikeType.LR);
		}
		query.setPageNo(page);
		query.setPageSize(limit);
		return this.queryForEntities(query);
	}

	public int findNumByWebid(String quesbankname, int webid) {
		String sql = "SELECT count(1) FROM " + Tables.DCZJQUEABANKTITLEINFO +" WHERE webid =:webid AND isdelete = 0 ";
		if(StringUtil.isNotEmpty(quesbankname)) {
			sql += " AND quesbankname LIKE :quesbankname";
		}
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		if(StringUtil.isNotEmpty(quesbankname)) {
			query.addParameter("quesbankname", quesbankname,LikeType.LR);
		}
		return this.queryForInteger(query);
	}


	public List<QuesBankTitleInfo> findTitleByWebId(Integer webid) {
		String sql = this.getSql() + " WHERE webid =:webid AND isdelete = 0 ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		return this.queryForEntities(query);
	}


	public boolean delete(int dczjid) {
		String where = " iid =:dczjid ";
		UpdateSql query = new UpdateSql(Tables.DCZJQUEABANKTITLEINFO);
		query.setWhere(where);
		query.addParam("isdelete", 1);
		query.addWhereParam("dczjid", dczjid);
		return this.update(query);
	}

}
