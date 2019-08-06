package com.hanweb.dczj.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.TotalReco;

public class TotalRecoDAO extends BaseJdbcDAO<Integer, TotalReco> {

	String headSql = "iid,mobile,createdate,type,ip,dczjid,unid,code,submitstate,sumscore,ipaddress";
	
	/**
	 * 查找调查表数据  分页
	 * @param state
	 * @return
	 */
	public List<TotalReco> findSubmitRecoListsByFormid(String dczjid, int page, int pagesize, String starttime, String endtime,String check,String sorttype) {
		String sql = "SELECT "+headSql+" FROM "+ Tables.TOTALRECO+" WHERE 1 = 1";
		if(StringUtil.isNotEmpty(dczjid)){
			sql += " AND dczjid =:dczjid";
		}
		if(StringUtil.isNotEmpty(starttime)){
			sql += " AND createdate >=:starttime";
		}
		if(StringUtil.isNotEmpty(endtime)){
			sql += " AND createdate <=:endtime";
		}
		
		String orderby = " ORDER BY iid DESC";
		if(StringUtil.isNotEmpty(sorttype)&&StringUtil.isNotEmpty(check)){
		if(sorttype.equals("1")) {
		if(check.equals("1")){
			orderby = " ORDER BY  createdate DESC";
		}
		if(check.equals("2")){
			orderby = " ORDER BY sumscore DESC";
		}
		if(check.equals("3")){
			orderby = " ORDER BY createdate DESC,sumscore DESC";
		}}else if(sorttype.equals("0")){
			if(check.equals("1")){
				orderby= " ORDER BY createdate ASC";
			}
			if(check.equals("2")){
				orderby= " ORDER BY sumscore ASC";
			}
			if(check.equals("3")){
				orderby = " ORDER By createdate ASC,sumscore ASC";
			}
		}}
		/*sql += " ORDER BY iid DESC";*/
		sql +=orderby;
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		if(StringUtil.isNotEmpty(starttime)){
			starttime = starttime + " 00:00:00";
			query.addParameter("starttime", stringToDate(starttime));
		}
		if(StringUtil.isNotEmpty(endtime)){
			endtime = endtime + " 23:59:59";
			query.addParameter("endtime", stringToDate(endtime));
		}
		query.setPageNo(page);
		query.setPageSize(pagesize);
		List<TotalReco> totalRecos = queryForEntities(query);
		return totalRecos;
	}
	
	/**
     * 通过formid查询投票总表 数量
     * @param time 时间节点
     */
	public int getTotalSurveyNumByFormid(String dczjid, String starttime, String endtime){
		int num = 0;
		String strSql = "SELECT COUNT(1) FROM "+ Tables.TOTALRECO+" where dczjid =:dczjid";
		if(StringUtil.isNotEmpty(starttime)){
			strSql += " AND createdate >=:starttime";
		}
		if(StringUtil.isNotEmpty(endtime)){
			strSql += " AND createdate <=:endtime";
		}
		Query query = this.createQuery(strSql);
		query.addParameter("dczjid", dczjid);
		if(StringUtil.isNotEmpty(starttime)){
			starttime = starttime + " 00:00:00";
			query.addParameter("starttime", stringToDate(starttime));
		}
		if(StringUtil.isNotEmpty(endtime)){
			endtime = endtime + " 23:59:59";
			query.addParameter("endtime", stringToDate(endtime));
		}
		num = this.queryForInteger(query);
		return num;
	}
	
	/**
	 * String转date 
	 * String标准 yyyy-MM-dd HH:mm:ss
	 */
	public Date stringToDate(String dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}
	
	/**
	 * 按id删除
	 * @param id
	 * @return
	 */
	public int remove(int id) {
		String sql = "DELETE FROM "+ Tables.TOTALRECO + " WHERE iid=:id";
		Query query = createQuery(sql);
		query.addParameter("id", id);
		return execute(query);
	}
	
	/**
	 * 按dczjid批量删除
	 * @param dczjid
	 * @return
	 */
	public int removeAll(int dczjid) {
		String sql = "DELETE FROM "+ Tables.TOTALRECO + " WHERE dczjid=:dczjid";
		Query query = createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}
	
	/**
	 * 通过提交唯一码判断是否已经提交数据
	 * @param unid
	 * @return
	 */
	public int findRecoIsHaveSubmitByUnid(String unid) {
		String sql = "SELECT count(1) FROM "+ Tables.TOTALRECO+" WHERE unid =:unid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		int count = 0;
		count = this.queryForInteger(query);
		return count;
	}
	
	/**
     * 查询当天所有调查提交总量  缓存用
     * @param time 时间节点
     */
	public int getAllTotalSurveyNum(){
		int num = 0;
		String time = "";
		time = DateUtil.currDay() + " 00:00:00";
		String strSql = "SELECT COUNT(1) FROM "+ Tables.TOTALRECO+" where submitstate=0";
		strSql += " AND createdate >= :createdate";
		Query query = this.createQuery(strSql);
		query.addParameter("createdate", stringToDate(time));
		num = this.queryForInteger(query);
		return num;
	}
	
	/**
	 * 查找已提交调查表数据   用于缓存清空
	 * @param state
	 * @return
	 */
	public String[][] findSubmitRecoLists(String  state, int start, int pagesize) {
		String time = "";
		time = DateUtil.currDay() + " 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sql = "SELECT mobile,dczjid,unid,ip FROM "+ Tables.TOTALRECO+" WHERE 1 = 1";
		if(StringUtil.isNotEmpty(state)){
			sql += " AND submitstate =:state";
		}
		sql += " AND createdate >= :createdate";
		Query query = this.createQuery(sql);
		query.addParameter("state", state);
		query.addParameter("createdate", date);
		String[][] totalRecoListData = null;
		totalRecoListData =  this.queryForArrays(query, start, start+pagesize);
		return totalRecoListData;
	}

	public int findRecoIsHaveSubmitByDczjid(String dczjid) {
		String sql = "SELECT count(*) FROM "+ Tables.TOTALRECO+" WHERE dczjid =:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		int count = 0;
		count = this.queryForInteger(query);
		return count;
	}

	public int findResourceCount(String dczjid, int type) {
		String sql = "SELECT count(*) FROM "+ Tables.TOTALRECO+" WHERE dczjid =:dczjid AND type =:type";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("type", type);
		int count = 0;
		count = this.queryForInteger(query);
		return count;
	}
	
	/**
	 * 获取地区统计数量
	 * @param dczjid
	 * @return
	 */
	public String[][] findAddressCount(String dczjid){
		String sql = "SELECT ipaddress,COUNT(1) as t FROM "+Tables.TOTALRECO +" WHERE dczjid =:dczjid GROUP BY ipaddress ORDER by t desc";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.setPageNo(1);
		query.setPageSize(5);
		return this.queryForArrays(query);
	}

	public String[][] findIpCount(String dczjid) {
		String sql = "SELECT ip,COUNT(1) as t FROM "+Tables.TOTALRECO +" WHERE dczjid =:dczjid GROUP BY ip ORDER by t desc";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.setPageNo(1);
		query.setPageSize(5);
		return this.queryForArrays(query);
	}
	public int findMaxScore(String dczjid) {
		String sql = "SELECT MAX(sumscore) FROM "+ Tables.TOTALRECO+" where dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	public int findMinScore(String dczjid) {
		String sql = "SELECT MIN(sumscore) FROM "+ Tables.TOTALRECO+" where dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	public int findAvgScore(String dczjid) {
		String sql = "SELECT avg(sumscore) FROM "+ Tables.TOTALRECO+" where dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	public int findSumScore(String dczjid) {
		String sql = "SELECT SUM(sumscore) FROM "+ Tables.TOTALRECO+" where dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}

	public int findNumByGrade(int minscore, int maxscore, String dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.TOTALRECO +" WHERE dczjid =:dczjid AND sumscore >=:minscore AND sumscore <:maxscore";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("minscore", minscore);
		query.addParameter("maxscore", maxscore);
		return this.queryForInteger(query);
	}
}
