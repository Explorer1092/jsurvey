package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.AnswInfo;

public class AnswInfoDAO extends BaseJdbcDAO<Integer, AnswInfo>{

	/**
	 * 通用查询的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid,orderid,dczjid,quesid,answname,answnote,basepoint,state,answimgname,allowfillinair,isright FROM "
				+ Tables.DCZJANSWINFO;
		return sql;
	}
	
	/**
	 * 通过quesid获取答案列表
	 * @param quesId
	 * @return
	 */
	public List<AnswInfo> getAnswListByQuesId(int quesId) {
		String sql = this.getSql() + " WHERE quesid =:quesId AND state = 0 ORDER BY orderid DESC,iid ASC";
		Query query = createQuery(sql);
		query.addParameter("quesId", quesId);
		return this.queryForEntities(query);
	}

	/**
	 * 删除答案
	 * @param quesid
	 * @param string
	 * @return
	 */
	public boolean deleteByQuesId(String quesid, String state) {
		String where = " quesid =:quesid ";
		UpdateSql query = new UpdateSql(Tables.DCZJANSWINFO);
		query.setWhere(where);
		query.addParam("state", state);
		query.addWhereParam("quesid", quesid);
		return this.update(query);
	}

	/**
	 * 查看答案总数量
	 * @param quesId
	 * @return
	 */
	public int findCount(int quesid) {
		String sql = "SELECT COUNT(*) FROM " + Tables.DCZJANSWINFO + " WHERE state = 0 AND quesid=:quesid";
		Query query =  this.createQuery(sql);
		query.addParameter("quesid", quesid);
		return this.queryForInteger(query);
	}

	/**
	 * 删除答案
	 * @param answid
	 * @return
	 */
	public boolean delete(String answid) {
		String where = " iid =:iid ";
		UpdateSql query = new UpdateSql(Tables.DCZJANSWINFO);
		query.setWhere(where);
		query.addParam("state", 1);
		query.addWhereParam("iid", answid);
		return this.update(query);
	}

	/**
	 * 查看最小排序
	 * @param idsList
	 * @return
	 */
	public String[][] findMinOrder(List<Integer> answIdList) {
		String strSql = "SELECT MIN( orderid ) FROM " + Tables.DCZJANSWINFO + " WHERE iid IN (:idsList)";
		Query query = createQuery(strSql);
		query.addParameter("idsList", answIdList);
		return this.queryForArrays(query);
	}

	/**
	 * 
	 * @param iid
	 * @param newOrderid
	 * @return
	 */
	public boolean modifyOrder(Integer iid, int newOrderid) {
		UpdateSql sql = new UpdateSql(Tables.DCZJANSWINFO,"iid=:iid");
		sql.addInt("orderid", newOrderid);
		sql.addWhereParamInt("iid", iid);
		return this.update(sql);
	}

	public int getBasePointSum(int quesId) {
		String strSql = "SELECT SUM(basepoint) FROM " + Tables.DCZJANSWINFO +" WHERE quesid = :quesid";
		Query query = createQuery(strSql);
		query.addParameter("quesid", quesId);
		return this.queryForInteger(query);
	}
	public int getNumByDczjid(int dczjid) {
		String sql="SELECT count(1) FROM "
				+ Tables.DCZJANSWINFO +" where isright=1 and dczjid = :dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	public String[][] findRightAnsw(String quesid){
		String strSql = "SELECT iid FROM "+Tables.DCZJANSWINFO + " WHERE isright=:isright AND quesid =:quesid";
		Query query = this.createQuery(strSql);
		query.addParameter("quesid", quesid);
		query.addParameter("isright", "1");
		return queryForArrays(query);
	}
	public int findRightNum(String quesid) {
		String strSql = "SELECT COUNT(1) FROM "+Tables.DCZJANSWINFO + " WHERE isright=:isright AND quesid =:quesid";
		Query query = this.createQuery(strSql);
		query.addParameter("quesid", quesid);
		query.addParameter("isright", "1");
		return queryForInteger(query);
	}
	
}
