package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.TitleInfo;

public class TitleInfoDAO extends BaseJdbcDAO<Integer, TitleInfo> {

	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,webid,orderid,username,createtime,titlename,state,ispublish,isdelete,servertype,type FROM " + Tables.DCZJTITLEINFO;
		return sql;
	}
	
	/**
	 * 查询list集合
	 * @param webid
	 * @param dczjtype
	 * @param dczjstate
	 * @param titlename
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<TitleInfo> findInfoListByWebid(int webid,int dczjtype, int dczjstate, String titlename, int page, int limit) {
		String sql = this.getSql() + " WHERE webid =:webid AND isdelete = 0 AND type =:dczjtype ";
		if(dczjstate < 3) {
			sql += " AND state =:dczjstate";
		}
		if(StringUtil.isNotEmpty(titlename)) {
			sql += " AND titlename LIKE :titlename";
		}
		sql += " ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("dczjtype", dczjtype);
		if(dczjstate < 3) {
			query.addParameter("dczjstate", dczjstate);
		}
		if(StringUtil.isNotEmpty(titlename)) {
			query.addParameter("titlename", titlename,LikeType.LR);
		}
		query.setPageNo(page);
		query.setPageSize(limit);
		return this.queryForEntities(query);
	}

	/**
	 * 
	 * @param dczjtype
	 * @param dczjstate
	 * @param titlename
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<TitleInfo> findInfoList(int dczjtype, int dczjstate, String titlename, int page, int limit) {
		String sql = this.getSql() + " WHERE isdelete = 0 AND type =:dczjtype";
		if(dczjstate < 3) {
			sql += " AND state =:dczjstate";
		}
		if(StringUtil.isNotEmpty(titlename)) {
			sql += " AND titlename LIKE :titlename";
		}
		sql += " ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("dczjtype", dczjtype);
		if(dczjstate < 3) {
			query.addParameter("dczjstate", dczjstate);
		}
		if(StringUtil.isNotEmpty(titlename)) {
			query.addParameter("titlename", titlename,LikeType.LR);
		}
		query.setPageNo(page);
		query.setPageSize(limit);
		return this.queryForEntities(query);
	}

	/**
	 * 
	 * @param dczjtype 
	 * @param dczjstate 
	 * @return
	 */
	public int findNum(int dczjtype, int dczjstate, String titlename) {
		String sql = "SELECT count(1) FROM " + Tables.DCZJTITLEINFO +" WHERE isdelete = 0 AND type =:dczjtype";
		if(dczjstate < 3) {
			sql += " AND state =:dczjstate";
		}
		if(StringUtil.isNotEmpty(titlename)) {
			sql += " AND titlename LIKE :titlename";
		}
		Query query = createQuery(sql);
		query.addParameter("dczjtype", dczjtype);
		if(dczjstate < 3) {
			query.addParameter("dczjstate", dczjstate);
		}
		if(StringUtil.isNotEmpty(titlename)) {
			query.addParameter("titlename", titlename,LikeType.LR);
		}
		return this.queryForInteger(query);
	}

	/**
	 * 
	 * @param dczjtype 
	 * @param webid
	 * @return
	 */
	public int findNumByWebid(int dczjtype,int dczjstate,String titlename,int webid) {
		String sql = "SELECT count(1) FROM " + Tables.DCZJTITLEINFO +" WHERE isdelete = 0 AND webid =:webid AND type =:dczjtype";
		if(dczjstate < 3) {
			sql += " AND state =:dczjstate";
		}
		if(StringUtil.isNotEmpty(titlename)) {
			sql += " AND titlename LIKE :titlename";
		}
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("dczjtype", dczjtype);
		if(dczjstate < 3) {
			query.addParameter("dczjstate", dczjstate);
		}
		if(StringUtil.isNotEmpty(titlename)) {
			query.addParameter("titlename", titlename,LikeType.LR);
		}
		return this.queryForInteger(query);
	}

	
	/**
	 * 
	 * @param dczjtype 
	 * @param webid
	 * @return
	 */
	public int findNumByWebid(int webid) {
		String sql = "SELECT count(1) FROM " + Tables.DCZJTITLEINFO +" WHERE isdelete = 0 AND webid =:webid ";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		return this.queryForInteger(query);
	}
	/**
	 * 
	 * @param webid
	 * @param type
	 * @return
	 */
	public List<TitleInfo> findTitleByWebIdAndType(Integer webid, int type) {
		String sql = this.getSql() + " WHERE webid =:webid AND isdelete = 0 AND type =:type ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("type",type);
		return this.queryForEntities(query);
	}
	
	/**
	 * 
	 * @param webid
	 * @param type
	 * @return
	 */
	public List<TitleInfo> findTitleByWebId(int webid, String datatype) {
		String sql = this.getSql() + " WHERE webid =:webid";
		if(StringUtil.isNotEmpty(datatype)) {
			sql += " AND type IN(:datatype)";
		}
		sql += " AND isdelete = 0 ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("datatype", StringUtil.toIntegerList(datatype));
		return this.queryForEntities(query);
	}
	
	public List<TitleInfo> findTitleByWebId1(int webid, String datatype) {
		String sql = this.getSql() + " WHERE webid =:webid";
		if(StringUtil.isNotEmpty(datatype)) {
			sql += " AND type IN(:datatype)";
		}
		sql += " AND isdelete = 0 AND ispublish = 1 ORDER BY orderid ASC, iid DESC";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		query.addParameter("datatype", StringUtil.toIntegerList(datatype));
		return this.queryForEntities(query);
	}
	
	/**
	 * 查找最新的排序
	 * @param dczjIdList
	 * @param type 
	 * @return
	 */
	public String[][] findMinOrder(List<Integer> dczjIdList, int type) {
		String strSql =  "SELECT MIN(orderid ) FROM " + Tables.DCZJTITLEINFO + " WHERE type =:type AND iid IN (:dczjIdList)";
		Query query = createQuery(strSql);
		query.addParameter("dczjIdList", dczjIdList);
		query.addParameter("type", type);
		return this.queryForArrays(query);
	}

	/**
	 * 更新排序字段
	 * @param iid
	 * @param orderid
	 * @return
	 */
	public boolean modifyOrder(Integer iid, int orderid) {
		UpdateSql sql = new UpdateSql(Tables.DCZJTITLEINFO,"iid=:iid");
		sql.addInt("orderid", orderid);
		sql.addWhereParamInt("iid", iid);
		return this.update(sql);
	}

	/**
	 * 删除
	 * @param dczjid
	 * @return
	 */
	public boolean delete(int dczjid) {
		String where = " iid =:dczjid ";
		UpdateSql query = new UpdateSql(Tables.DCZJTITLEINFO);
		query.setWhere(where);
		query.addParam("isdelete", 1);
		query.addWhereParam("dczjid", dczjid);
		return this.update(query);
	}

	/**
	 * 改变发布状态
	 * @param dczjid
	 * @param isBuild
	 * @return
	 */
	public boolean setUpdateHtml(String dczjid, int isBuild) {
		UpdateSql updateSql;
		if(NumberUtil.getInt(dczjid) == 0)
			updateSql = new UpdateSql(Tables.DCZJTITLEINFO,"1=1");
		else
			updateSql = new UpdateSql(Tables.DCZJTITLEINFO,"iid = "+dczjid);
		updateSql.addInt("ispublish", isBuild);
		return this.update(updateSql);
	}
	
	/**
	 * 按status获取list
	 * @param status
	 * @return
	 */
	public List<TitleInfo> getTitleListByStatus(String state){
		String sql = this.getSql() + " WHERE state =:state AND isdelete = 0 ";
		Query query = createQuery(sql);
		query.addParameter("state", state);
		return this.queryForEntities(query);
	}
	
	/**
	 * 改变调查征集状态
	 * @param dczjid
	 * @param state
	 * @return
	 */
	public boolean updateState(String dczjid, int state) {
		UpdateSql updateSql;
		if(NumberUtil.getInt(dczjid) == 0)
			updateSql = new UpdateSql(Tables.DCZJTITLEINFO,"1=1");
		else
			updateSql = new UpdateSql(Tables.DCZJTITLEINFO,"iid = "+dczjid);
		updateSql.addInt("state", state);
		return this.update(updateSql);
	}

	public int checkNumJsurveyByWebid(int webid) {
		String sql = "SELECT count(1) FROM "+Tables.DCZJTITLEINFO+" WHERE isdelete = 0 AND webid =:webid";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		return this.queryForInteger(query);
	}

	public List<TitleInfo> findTitleByWebId(String webid) {
		String sql = this.getSql() + " WHERE webid =:webid AND isdelete = 0 ";
		Query query = createQuery(sql);
		query.addParameter("webid", webid);
		return this.queryForEntities(query);
	}

}
