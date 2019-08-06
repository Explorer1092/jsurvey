package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.QuesInfo;

public class QuesInfoDAO extends BaseJdbcDAO<Integer, QuesInfo> {

	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,orderid,quesname,note,dczjid,type,col,ismustfill,minselect,maxselect,textinputwidth,textinputheight,content,state,dczjtype,relyanswid,showpublish,validaterules,quesscore FROM "
				+ Tables.DCZJQUESINFO;
		return sql;
	}

	/**
	 * 通过dczjid和type查找集合
	 * @param dczjid
	 * @param type
	 * @return
	 */
	public List<QuesInfo> findQuesListByDczjidAndType(String dczjid ,String type) {
		String sql = this.getSql() + " WHERE dczjid = :dczjid AND type = :type AND state = 0 ORDER BY orderid DESC , iid ASC";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("type", type);
		return this.queryForEntities(query);
	}
	
	/**
	 * 通过dczjid查找集合
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findQuesListByDczjid(String dczjid) {
		String sql = this.getSql() + " WHERE dczjid = :dczjid AND state = 0 ORDER BY orderid DESC , iid ASC";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}
	
	/**
	 * 通过dczjid查找集合
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findQuesListForExcel(String dczjid) {
		String sql = this.getSql() + " WHERE dczjid = :dczjid AND state = 0 AND type != 3 AND type != 4 ORDER BY orderid DESC , iid ASC";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

	/**
	 * 删除题目
	 * @param quesid
	 * @param string
	 * @return
	 */
	public boolean deleteById(String iid, String state) {
		String where = " iid =:iid ";
		UpdateSql query = new UpdateSql(Tables.DCZJQUESINFO);
		query.setWhere(where);
		query.addParam("state", state);
		query.addWhereParam("iid", iid);
		return this.update(query);
	}

	/**
	 * 通过dczjid查找实体
	 * @param dczjid
	 * @return
	 */
	public QuesInfo findQuesByDczjid(Integer dczjid) {
		String sql = this.getSql() + " WHERE dczjid = :dczjid AND state = 0 ORDER BY orderid DESC , iid ASC";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntity(query);
	}

	/**
	 * 通过主键ID查找List
	 * @param iid
	 * @return
	 */
	public List<QuesInfo> findQuesListByIid(int iid) {
		String sql = this.getSql() + " WHERE iid = :iid AND state = 0 ORDER BY orderid DESC , iid ASC";
		Query query = this.createQuery(sql);
		query.addParameter("iid", iid);
		return this.queryForEntities(query);
	}

	
	public List<QuesInfo> findQuesByType(String dczjid) {
		String strSql = this.getSql() + " WHERE state = 0 And (type = 0 or type = 1) ";
		if(StringUtil.isNotEmpty(dczjid)){
			strSql += " AND dczjid=:dczjid";
		}
		strSql +=" ORDER BY orderid DESC,iid ASC";
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

	/**
	 * 查找所有分页
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findQuesPage(String dczjid) {
		String strSql = this.getSql() + " WHERE state = 0 And type = 4 AND dczjid=:dczjid";
		strSql +=" ORDER BY orderid DESC,iid ASC";
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

	/**
	 * 查找所有的分页和问答题
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findTextQuesAndPageQues(String dczjid) {
		String strSql = this.getSql() + " WHERE state = 0 And (type = 0 or type = 1 or type = 2 or type = 4 or type = 5) ";
		if(StringUtil.isNotEmpty(dczjid)){
			strSql += " AND dczjid=:dczjid";
		}
		strSql +=" ORDER BY orderid DESC,iid ASC";
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

	public List<QuesInfo> findDQuesByDczjId(String dczjid) {
		String strSql = this.getSql() + " WHERE state = 0 And relyanswid !=0 ";
		if(StringUtil.isNotEmpty(dczjid)){
			strSql += " AND dczjid=:dczjid";
		}
		strSql +=" ORDER BY orderid DESC,iid ASC";
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

	public String[][] findMinOrder(List<Integer> quesIdList) {
		String strSql = "SELECT MIN( orderid ) FROM " + Tables.DCZJQUESINFO + " WHERE iid IN (:quesIdList)";
		Query query = createQuery(strSql);
		query.addParameter("quesIdList", quesIdList);
		return this.queryForArrays(query);
	}

	public boolean modifyOrder(Integer iid, int newOrderid) {
		UpdateSql sql = new UpdateSql(Tables.DCZJQUESINFO,"iid=:iid");
		sql.addInt("orderid", newOrderid);
		sql.addWhereParamInt("iid", iid);
		return this.update(sql);
	}

	public List<QuesInfo> findRadioQuesByDczjId(String dczjid) {
		String strSql = this.getSql() + " WHERE state = 0 And (type = 0 or type = 6)AND dczjid=:dczjid ORDER BY orderid DESC,iid ASC";
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		return this.queryForEntities(query);
	}

}
