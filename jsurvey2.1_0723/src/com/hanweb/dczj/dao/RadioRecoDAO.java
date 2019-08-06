package com.hanweb.dczj.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.datasource.DataSourceSwitch;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Count;
import com.hanweb.dczj.entity.RadioReco;

public class RadioRecoDAO extends BaseJdbcDAO<Integer, RadioReco> {
	
	String headSql = "SELECT iid,dczjid,quesid,answid,answcontent,ip,submittime,audi,unid,replyid FROM "+Tables.RADIORECO;
	
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
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE dczjid=:dczjid AND quesid=:quesid AND answid=:answid "
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
	 * 获取实体集合总数
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Integer findEntitysCount(Integer quesid, Integer answid){
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE quesid=:quesid AND answid=:answid ";
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
	public List<RadioReco> findEntitys(Integer page, Integer limit, Integer dczjid, Integer quesid, Integer answid, Date starttime, Date endtime){
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
	public List<RadioReco> findEntitys(Integer page, Integer limit, Integer quesid, Integer answid){
		String sql = headSql + " WHERE quesid=:quesid AND answid=:answid ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		query.addParameter("answid", answid);
		query.setPageNo(page);
		query.setPageSize(limit);
		return queryForEntities(query);
	}
	
	/**
	 * 查找value
	 * @param ids
	 * @return
	 */
	public String findTotalRecoByIds1(String formId,String quesId,String unid) {
		String[][] data = null;
		String sql = "SELECT answid FROM "+Tables.RADIORECO+" WHERE dczjid =:formid AND quesid =:quesid AND unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("formid", formId);
		query.addParameter("quesid", quesId);
		query.addParameter("unid", unid);
		data = this.queryForArrays(query);
		if(data != null && data.length>0) {
			return data[0][0];
		}else {
			return "";
		}
		
	}
	
	/**
	 * 分页查询待导出数据
	 * @param dczjid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String[][] findExcelValue(String dczjid, int page, int pageSize){
		String sql = "SELECT a.quesid,b.answname,a.unid FROM "+Tables.RADIORECO+" a,"+Tables.DCZJANSWINFO
				+" b WHERE a.answid = b.iid AND a.dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.setPageNo(page);
		query.setPageSize(pageSize);
		return this.queryForArrays(query);
	}
	
	/**
	 * 查询有多少个  分页用
	 * @param dczjid
	 * @return
	 */
	public Integer findExcelValueCount(String dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	
	/**
	 * 按时间统计
	 * @param dczjid
	 * @param date yyyy-MM-dd
	 * @return
	 */
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
		String sql = "SELECT quesid,answid,COUNT(1) FROM "+Tables.RADIORECO+" WHERE dczjid =:dczjid AND submittime>=:starttime "
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
		String sql = "DELETE FROM "+Tables.RADIORECO+" WHERE dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}
	public List<RadioReco> queryByuuid(String unid,int quesid) {
		String sql="SELECT iid,dczjid,quesid,answid,answcontent,ip,submittime,audi,unid,replyid FROM "+Tables.RADIORECO+" where unid =:unid and quesid=:quesid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		query.addParameter("quesid", quesid);
		return queryForEntities(query);
	}
	public int queryNumByunid(String unid) {
		String sql="SELECT count(1) FROM "+Tables.RADIORECO+" r left join "+Tables.DCZJANSWINFO+" a on r.answid=a.iid where a.isright=1  and r.unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("unid", unid);
		return this.queryForInteger(query);	
	}
	public int findCountByXAndY(int xanswid, int yanswid) {
		int count = 0;
		String sql = "SELECT count(1) FROM " +Tables.RADIORECO + " WHERE unid IN ( SELECT unid FROM "+Tables.RADIORECO + " WHERE answid=:xanswid) AND answid =:yanswid" ;
		Query query = this.createQuery(sql);
		query.addParameter("xanswid", xanswid);
		query.addParameter("yanswid", yanswid);
		count = this.queryForInteger(query);
		return count;
	}

	public int getRecoConutByTime(int quesid, Date starttime, Date endtime) {
		String sql = "SELECT COUNT(1) FROM "+Tables.RADIORECO+" WHERE quesid=:quesid "
				+ "AND submittime>=:starttime AND submittime<=:endtime ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		query.addParameter("starttime", starttime);
		query.addParameter("endtime", endtime);
		return queryForInteger(query);
	}

	public List<Count> getCountByDczjid1(String dczjid, String date) {
		DataSourceSwitch.changeDefault();
		String sql = "SELECT quesid,answid,COUNT(1) FROM "+Tables.RADIORECO+" WHERE dczjid =:dczjid"
				+ "   GROUP BY quesid,answid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
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
	
}
