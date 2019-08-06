package com.hanweb.dczj.dao;


import java.util.Date;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.ContentReco;

public class ContentRecoDAO extends BaseJdbcDAO<Integer, ContentReco> {
	
	String sqlhead = "SELECT iid, dczjid, quesid, answid, answcontent, ip, submittime, audi, unid, replyid, replycontent FROM "+Tables.CONTENTRECO;
	
	/**
	 * 通过id更新回复和审核状态
	 * @param contentid
	 * @param replycontent
	 * @return
	 */
	public Integer updateReplyContentById(Integer contentid,String replycontent,String audi) {
		String sql = "Update "+Tables.CONTENTRECO+" SET replycontent=:replycontent,audi=:audi WHERE iid=:iid";
		Query query = this.createQuery(sql);
		query.addParameter("replycontent", replycontent);
		query.addParameter("audi", audi);
		query.addParameter("iid", contentid);
		return execute(query);
	}
	
	/**
	 * 通过id更新回复
	 * @param contentid
	 * @param replycontent
	 * @return
	 */
	public Integer updateReplyContentById(Integer contentid,String replycontent) {
		String sql = "Update "+Tables.CONTENTRECO+" SET replycontent=:replycontent WHERE iid=:iid";
		Query query = this.createQuery(sql);
		query.addParameter("replycontent", replycontent);
		query.addParameter("iid", contentid);
		return execute(query);
	}
	
	/**
	 * 通过iid获取实体
	 * @param iid
	 * @return
	 */
	public ContentReco findEntityById(Integer iid) {
		return queryForEntityById(iid);
	}
	
	/**
	 * 按id删除
	 * @param iid
	 * @return
	 */
	public Integer deletById(Integer iid) {
		String sql = "DELETE FROM "+Tables.CONTENTRECO+" WHERE iid=:iid";
		Query query = this.createQuery(sql);
		query.addParameter("iid", iid);
		return execute(query);
	}
	
	/**
	 * 设置审核状态
	 * @param iid
	 * @param audi
	 * @return
	 */
	public Integer updateAudiById(Integer iid, String audi) {
		String sql = "Update "+Tables.CONTENTRECO+" SET audi=:audi WHERE iid=:iid";
		Query query = this.createQuery(sql);
		query.addParameter("audi", audi);
		query.addParameter("iid", iid);
		return execute(query);
	}
	
	/**
	 * 查询提交值
	 * @param formid
	 * @param i_id
	 * @return
	 */
	public String findValueByQuesAndDczjid(String formid, String quesid, String unid) {
		String sql = "SELECT answcontent FROM "+ Tables.CONTENTRECO + " WHERE dczjid=:formid AND quesid =:quesid AND unid=:unid";
		Query query = this.createQuery(sql);
		query.addParameter("formid", formid);
		query.addParameter("quesid", quesid);
		query.addParameter("unid", unid);
		String[][] array = this.queryForArrays(query);
		if(array!=null && array.length>0) {
			return array[0][0];
		}else {
			return "";
		}
	}
	
	public String[][] findExcelValue(String dczjid, int page, int pageSize){
		String sql = "SELECT quesid,answcontent,unid FROM "+Tables.CONTENTRECO+" WHERE dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.setPageNo(page);
		query.setPageSize(pageSize);
		return this.queryForArrays(query);
	}
	
	public Integer findExcelValueCount(String dczjid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE dczjid =:dczjid ";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return this.queryForInteger(query);
	}
	
	public Integer findContentSumByTime(Integer dczjid, Integer quesid, Date startDate, Date endDate) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE dczjid=:dczjid AND quesid=:quesid "
				+ "AND submittime>=:startDate AND submittime<=:endDate";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		query.addParameter("startDate", startDate);
		query.addParameter("endDate", endDate);
		return this.queryForInteger(query);
	}
	
	/**
	 * 获取文本题总数，包含审核状态
	 * @param audi
	 * @param quesid
	 * @return
	 */
	public Integer findContentSum(Integer audi, Integer quesid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE audi=:audi AND quesid=:quesid ";
		Query query = this.createQuery(sql);
		query.addParameter("audi", audi);
		query.addParameter("quesid", quesid);
		return this.queryForInteger(query);
	}
	
	/**
	 * 获取文本题总数
	 * @param quesid
	 * @return
	 */
	public Integer findContentSum(Integer quesid) {
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE quesid=:quesid ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);
		return this.queryForInteger(query);
	}
	
	/**
	 * 获取一段时间内的文本类型实体List
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param startDate yyyy-MM-dd
	 * @param endDate yyyy-MM-dd
	 * @return
	 */
	public List<ContentReco> findEntityListByTime(String page,String limit,Integer dczjid, Integer quesid, Integer answid, Date startDate, Date endDate){
		String sql = sqlhead + " WHERE dczjid=:dczjid AND quesid=:quesid AND submittime>=:startDate AND submittime<=:endDate";
		if(answid!=0) {
			sql += "AND answid=:answid";
		}
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		if(answid!=0) {
			query.addParameter("answid", answid);
		}
		query.addParameter("startDate", startDate);
		query.addParameter("endDate", endDate);
		query.setPageNo(NumberUtil.getInt(page));
		query.setPageSize(NumberUtil.getInt(limit));
		return queryForEntities(query);
	}
	
	public List<ContentReco> findEntityListByTime1(String page,String limit,Integer dczjid, Integer quesid, Integer answid, Date startDate, Date endDate,String ids){
		String sql = sqlhead + " WHERE dczjid=:dczjid AND quesid=:quesid AND submittime>=:startDate AND submittime<=:endDate";
		if(answid!=0) {
			sql += " AND answid=:answid";
		}
		if(StringUtil.isNotEmpty(ids) && ids != null) {
			sql += " AND iid IN ("+ids+")";
		}
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		if(answid!=0) {
			query.addParameter("answid", answid);
		}
		/*if(StringUtil.isNotEmpty(ids) && ids != null) {
			query.addParameter("ids", ids);
		}*/
		query.addParameter("startDate", startDate);
		query.addParameter("endDate", endDate);
		query.setPageNo(NumberUtil.getInt(page));
		query.setPageSize(NumberUtil.getInt(limit));
		return queryForEntities(query);
	}
	/**
	 * 获取一段时间内的文本类型实体List
	 * @param page
	 * @param limit
	 * @param quesid
	 * @return
	 */
	public List<ContentReco> findEntitys(Integer page,Integer limit, Integer quesid){
		String sql = sqlhead + " WHERE quesid=:quesid ";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);		
		query.setPageNo(page);
		query.setPageSize(limit);
		return queryForEntities(query);
	}
	
	/**
	 * 获取一段时间内的文本类型实体List
	 * @param page
	 * @param limit
	 * @param quesid
	 * @param audi
	 * @return
	 */
	public List<ContentReco> findEntitys(Integer page,Integer limit, Integer quesid, Integer audi){
		String sql = sqlhead + " WHERE quesid=:quesid AND audi=:audi";
		Query query = this.createQuery(sql);
		query.addParameter("quesid", quesid);	
		query.addParameter("audi", audi);
		query.setPageNo(page);
		query.setPageSize(limit);
		return queryForEntities(query);
	}
	
	/**
	 * 获取实体总数 （分页用）
	 * @param page
	 * @param limit
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer findEntityCountByTime(Integer dczjid, Integer quesid, Integer answid, Date startDate, Date endDate){
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE dczjid=:dczjid AND quesid=:quesid AND submittime>=:startDate AND submittime<=:endDate";
		if(answid!=0) {
			sql += "AND answid=:answid";
		}
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		if(answid!=0) {
			query.addParameter("answid", answid);
		}
		query.addParameter("startDate", startDate);
		query.addParameter("endDate", endDate);
		String[][] dataArray = queryForArrays(query);
		if(dataArray!=null && dataArray.length>0) {
			if(StringUtil.isNotEmpty(dataArray[0][0])) {
				return NumberUtil.getInt(dataArray[0][0]);
			}
		}
		return 0;
	}
	
	public Integer findEntityCountByTime1(Integer dczjid, Integer quesid, Integer answid, Date startDate, Date endDate,String ids){
		String sql = "SELECT COUNT(1) FROM "+Tables.CONTENTRECO+" WHERE dczjid=:dczjid AND quesid=:quesid AND submittime>=:startDate AND submittime<=:endDate";
		if(answid!=0) {
			sql += " AND answid=:answid";
		}
		if(StringUtil.isNotEmpty(ids) && ids != null) {
			sql += " AND iid IN ("+ids+")";
		}
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		query.addParameter("quesid", quesid);
		if(answid!=0) {
			query.addParameter("answid", answid);
		}
		/*if(StringUtil.isNotEmpty(ids) && ids != null) {
			query.addParameter("iid", ids);
		}*/
		query.addParameter("startDate", startDate);
		query.addParameter("endDate", endDate);
		String[][] dataArray = queryForArrays(query);
		if(dataArray!=null && dataArray.length>0) {
			if(StringUtil.isNotEmpty(dataArray[0][0])) {
				return NumberUtil.getInt(dataArray[0][0]);
			}
		}
		return 0;
	}
	
	
	public Integer removeByDczjid(String dczjid) {
		String sql = "DELETE FROM "+Tables.CONTENTRECO+" WHERE dczjid=:dczjid";
		Query query = this.createQuery(sql);
		query.addParameter("dczjid", dczjid);
		return execute(query);
	}
}
