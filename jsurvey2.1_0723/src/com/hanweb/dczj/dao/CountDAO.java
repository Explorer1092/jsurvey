package com.hanweb.dczj.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Count;

public class CountDAO extends BaseJdbcDAO<Integer, Count>{

	public Integer getCountnNum(String date, Integer dczjid, Integer quesid, Integer answid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.COUNT+" WHERE countdate=:countdate AND dczjid=:dczjid AND quesid=:quesid AND answid=:answid";
		Query query = createQuery(sql);
		query.addParameter("countdate", date);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		return queryForInteger(query);
	}
	
	public boolean updateCount(Count count) {
		String sql = "UPDATE "+Tables.COUNT+" SET selectcount=:selectcount WHERE countdate=:countdate "
				+ "AND dczjid=:dczjid AND quesid=:quesid AND answid=:answid ";
		Query query = createQuery(sql);
		query.addParameter("selectcount", count.getSelectcount());
		query.addParameter("countdate", count.getCountdate());
		query.addParameter("dczjid", count.getDczjid());
		query.addParameter("quesid", count.getQuesid());
		query.addParameter("answid", count.getAnswid());
		if(execute(query)>0) {
			return true;
		}else {
			return false;
		}
	}
	
	public Integer getCountResult(String starttime, String endtime, Integer dczjid, Integer quesid, Integer answid) {
		String sql = "SELECT SUM(selectcount) FROM "+Tables.COUNT+" WHERE dczjid=:dczjid AND quesid=:quesid "
				+ " AND answid=:answid AND countdate>=:starttime AND countdate<=:endtime";
		Query query = createQuery(sql);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		String[][] dataArray = queryForArrays(query);
		int resultSum = 0;
		if(dataArray != null && dataArray.length>0 && StringUtil.isNotEmpty(dataArray[0][0])) {
			resultSum = NumberUtil.getInt(dataArray[0][0]);
		}
		return resultSum;
	}
	
	public Integer getAllCountResult(Integer dczjid, Integer quesid, Integer answid) {
		String sql = "SELECT SUM(selectcount) FROM "+Tables.COUNT+" WHERE dczjid=:dczjid AND quesid=:quesid "
				+ " AND answid=:answid ";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		String[][] dataArray = queryForArrays(query);
		int resultSum = 0;
		if(dataArray != null && dataArray.length>0 && StringUtil.isNotEmpty(dataArray[0][0])) {
			resultSum = NumberUtil.getInt(dataArray[0][0]);
		}
		return resultSum;
	}

	public String[][] getCurrentData(int answId, int quesId) {
		String strSql = "SELECT SUM(a.selectcount),(SELECT SUM(selectcount) FROM " + Tables.COUNT + 
				" b WHERE b.quesid = :quesid) FROM " + Tables.COUNT + " a WHERE a.answid = :answid" + 
				" AND a.quesid = :quesid GROUP BY a.quesid,a.answid";
		Query query = createQuery(strSql);
		query.addParameter("quesid", quesId);
		query.addParameter("answid", answId);
		return this.queryForArrays(query);
	}
	
	public Integer removeByDczjid(String dczjid) {
		String sql = "DELETE FROM "+Tables.COUNT+" WHERE dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}

	/**
	 * 获取实体集合总数
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Integer findEntitysCount(Integer dczjid, Integer quesid, Integer answid){
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE dczjid=:dczjid AND quesid=:quesid AND answid=:answid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		return queryForInteger(query);
	}

	public int getRecoConutByTime(int quesid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE quesid=:quesid";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		return queryForInteger(query);
	}


	public int checkRightBoxAnswCount(int answid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE answid=:answid "
				+ " AND isright = 1";
		Query query = this.createQuery(sql);
		query.addParameter("answid", answid);
		return queryForInteger(query);
	}


	public int getCheckConutByTime(int quesid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE quesid=:quesid ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);

		return queryForInteger(query);
	}
}
