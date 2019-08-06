package com.hanweb.dczj.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.Count;

public class CheckedBoxRecoDAO extends BaseJdbcDAO<Integer, CheckedBoxReco> {

	String headSql = "SELECT iid,dczjid,quesid,answid,answcontent,ip,submittime,audi,unid,replyid,isright FROM "+Tables.CHECKEDBOXRECO;
	
	/**
	 * 获取实体集合总数
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Integer findEntitysCount(Integer dczjid, Integer quesid, Integer answid, Date starttime, Date endtime){
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE dczjid=:dczjid AND quesid=:quesid AND answid=:answid "
				+ "AND submittime>=:starttime AND submittime<=:endtime ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		return queryForInteger(query);
	}
	
	/**
	 *  获取实体集合总数
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public Integer findEntitysCount(Integer quesid, Integer answid){
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE quesid=:quesid AND answid=:answid";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		return queryForInteger(query);
	}
	
	/**
	 * 分页查询获取实体集合
	 * @param page
	 * @param limit
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<CheckedBoxReco> findEntitys(Integer page, Integer limit, Integer dczjid, Integer quesid, Integer answid, Date starttime, Date endtime){
		String sql = headSql + " WHERE dczjid=:dczjid AND quesid=:quesid AND answid=:answid "
				+ "AND submittime>=:starttime AND submittime<=:endtime ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		query.setPageNo(page);
		query.setPageSize(limit);
		return queryForEntities(query);
	}
	
	/**
	 * 分页查询获取实体集合
	 * @param page
	 * @param limit
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public List<CheckedBoxReco> findEntitys(Integer page, Integer limit, Integer quesid, Integer answid){
		String sql = headSql + " WHERE quesid=:quesid AND answid=:answid ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		query.setPageNo(page);
		query.setPageSize(limit);
		return queryForEntities(query);
	}
	
	/**
	 * 查询提交值
	 * @param formid
	 * @param i_id
	 * @return
	 */
	public String findValueByQuesAndDczjid(String formid, String quesid, String unid) {
		String ids = "";
		String sql = "SELECT answid FROM "+ Tables.CHECKEDBOXRECO + " WHERE dczjid=:formid AND quesid =:quesid AND unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("formid", formid);
		query.addParameter("quesid", quesid);
		query.addParameter("unid", unid);
		String[][] array = this.queryForArrays(query);
		if(array!=null && array.length>0) {
			for(int i=0;i<array.length;i++) {
				ids = ids + array[i][0] + ",";
			}
			return ids.substring(0, ids.length()-1);
		}else {
			return "";
		}
	}
	
	public String[][] findExcelValue(String dczjid, int page, int pageSize){
		String sql = "SELECT a.quesid,b.answname,a.unid FROM "+Tables.CHECKEDBOXRECO+" a,"+Tables.DCZJANSWINFO
				+" b WHERE a.answid = b.iid AND a.dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.setPageNo(page);
		query.setPageSize(pageSize);
		return this.queryForArrays(query);
	}
	
	public Integer findExcelValueCount(String dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	
	public List<Count> getCountByDczjid(String dczjid, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date starttime = new Date();
		try {
			starttime = sdf.parse(date+" 00:00:00");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Date endtime = new Date();
		try {
			endtime = sdf.parse(date+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "SELECT quesid,answid,COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE dczjid =:dczjid AND submittime>=:starttime"
				+ " AND submittime<=:endtime GROUP BY quesid,answid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		String[][] dataArray = this.queryForArrays(query);
		List<Count> counts = new ArrayList<Count>();
		if(dataArray!=null && dataArray.length>0) {
			for(int i=0;i<dataArray.length;i++) {
				Count count = new Count(); 
				count.setAnswid(NumberUtil.getInt(dataArray[i][1]));
				count.setCountdate(date);
				count.setDczjid(NumberUtil.getInt(dczjid));
				count.setQuesid(NumberUtil.getInt(dataArray[i][0]));
				count.setSelectcount(NumberUtil.getInt(dataArray[i][2]));
				counts.add(count);
			}
			return counts;
		}else {
			return null;
		}
	}
	
	public Integer removeByDczjid(String dczjid) {
		String sql = "DELETE FROM "+Tables.CHECKEDBOXRECO+" WHERE dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}
	public List<CheckedBoxReco> queryByunid(String unid) {
		String sql="SELECT iid,dczjid,quesid,answid,answcontent,ip,submittime,audi,unid,replyid,isright FROM "+Tables.CHECKEDBOXRECO+" where unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		return queryForEntities(query);
	}
	public int queryNumByunid(String unid) {
		String sql="SELECT count(1) FROM "+Tables.CHECKEDBOXRECO+" c left join "+Tables.DCZJANSWINFO+" a on c.answid=a.iid where a.isright=1  and c.unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		return this.queryForInteger(query);	
	}
	public int queryCountByunid(String unid,int answid) {
		String sql="SELECT count(1) FROM "+Tables.CHECKEDBOXRECO +" where unid=:unid and answid=:answid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		query.addParameter("answid", answid);
		return this.queryForInteger(query);	
	}

	public int getCheckConutByTime(int quesid, Date starttime, Date endtime) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE quesid=:quesid "
				+ "AND submittime>=:starttime AND submittime<=:endtime ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		return queryForInteger(query);
	}

	public int checkRightBoxAnswCount(int answid, Date starttime, Date endtime) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CHECKEDBOXRECO+" WHERE answid=:answid "
				+ "AND submittime>=:starttime AND submittime<=:endtime AND isright = 1";
		Query query = this.createQuery(sql);
		query.addParameter("answid", answid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		return queryForInteger(query);
	}
}
