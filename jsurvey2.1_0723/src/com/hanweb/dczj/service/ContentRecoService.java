package com.hanweb.dczj.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.ContentRecoDAO;
import com.hanweb.dczj.entity.ContentReco;

public class ContentRecoService {

	@Autowired
	ContentRecoDAO contentRecoDAO;
	
	public String getValueByDczjidAndQuesid(String dczjid, String quesid, String unid) {
		return contentRecoDAO.findValueByQuesAndDczjid(dczjid, quesid, unid);
	}
	
	/**
	 * 获取一段时间内的答题总数
	 * @param dczjid
	 * @param quesid
	 * @param startStr yyyy-MM-dd
	 * @param endStr yyyy-MM-dd
	 * @return
	 */
	public Integer findContentSumByTime(Integer dczjid, Integer quesid, String startStr, String endStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(startStr+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endStr+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return contentRecoDAO.findContentSumByTime(dczjid, quesid, startDate, endDate);
	}
	
	/**
	 * 获取文本题总数，包含审核状态
	 * @param audi
	 * @param quesid
	 * @return
	 */
	public Integer getRecoConut(String quesid, String audi) {
		return contentRecoDAO.findContentSum(NumberUtil.getInt(audi), NumberUtil.getInt(quesid));
	}
	/**
	 * 获取文本题总数
	 * @param quesid
	 * @return
	 */
	public Integer getRecoConut(String quesid) {
		return contentRecoDAO.findContentSum(NumberUtil.getInt(quesid));
	}
	
	/**
	 * 获取一段时间内的文本类型实体List
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param startStr yyyy-MM-dd
	 * @param endStr yyyy-MM-dd
	 * @return
	 */
	public List<ContentReco> findEntityListByTime(String page,String limit,String dczjid, String quesid, String answid, String startStr, String endStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(startStr+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endStr+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return contentRecoDAO.findEntityListByTime(page, limit, NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid)
				, NumberUtil.getInt(answid), startDate, endDate);
	}
	
	public List<ContentReco> findEntityListByTime1(String page,String limit,String dczjid, String quesid, String answid, String startStr, String endStr,String ids){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(startStr+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endStr+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return contentRecoDAO.findEntityListByTime1(page, limit, NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid)
				, NumberUtil.getInt(answid), startDate, endDate,ids);
	}
	
	
	
	/**
	 * 文本类型实体List（分页）,含审核状态
	 * @param page
	 * @param limit
	 * @param quesid
	 * @param answid
	 * @param audi
	 * @return
	 */
	public List<ContentReco> findOtherAnswerList(Integer page, Integer limit, String quesid,Integer audi){
		return contentRecoDAO.findEntitys(page, limit, NumberUtil.getInt(quesid), audi);
	}
	
	/**
	 * 文本类型实体List（分页）
	 * @param page
	 * @param limit
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public List<ContentReco> findOtherAnswerList(Integer page, Integer limit, String quesid){
		return contentRecoDAO.findEntitys(page, limit, NumberUtil.getInt(quesid));
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
	public Integer findEntityCountByTime(String dczjid, String quesid, String answid, String startStr, String endStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(startStr+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endStr+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return contentRecoDAO.findEntityCountByTime(NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid)
				, NumberUtil.getInt(answid), startDate, endDate);
	}
	
	public Integer findEntityCountByTime1(String dczjid, String quesid, String answid, String startStr, String endStr,String ids){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(startStr+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endStr+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return contentRecoDAO.findEntityCountByTime1(NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid)
				, NumberUtil.getInt(answid), startDate, endDate,ids);
	}
	
	
	/**
	 * 批量审核通过
	 * @param iid
	 * @return
	 */
	public Integer updateAllAudiById(String iid) {
		return contentRecoDAO.updateAudiById(NumberUtil.getInt(iid), "1");
	}
	
	/**
	 * 批量已公开
	 * @param iid
	 * @return
	 */
	public Integer updateAllAudiById1(String iid) {
		return contentRecoDAO.updateAudiById(NumberUtil.getInt(iid), "2");
	}
	
	/**
	 * 审核
	 * @param iid
	 * @param audi 1：通过 ，2：公开
	 * @return
	 */
	public Integer updateAudiById(String iid, String audi) {
		return contentRecoDAO.updateAudiById(NumberUtil.getInt(iid), audi);
	}
	
	/**
	 * 按id删除
	 * @param iid
	 * @return
	 */
	public Integer deleteContentByid(String iid) {
		return contentRecoDAO.deletById(NumberUtil.getInt(iid));
	}
	
	/**
	 * 获取实体
	 * @param contentid
	 * @return
	 */
	public ContentReco findEntityById(String contentid) {
		return contentRecoDAO.findEntityById(NumberUtil.getInt(contentid));
	}
	
	/**
	 * 保存并审核
	 * @param contentid
	 * @param replyContent
	 * @return
	 */
	public Integer saveAndAudi(String contentid, String replyContent) {
		return contentRecoDAO.updateReplyContentById(NumberUtil.getInt(contentid), replyContent, "1");
	}
	
	/**
	 * 保存并公开
	 * @param contentid
	 * @param replyContent
	 * @return
	 */
	public Integer saveOpenly(String contentid, String replyContent) {
		return contentRecoDAO.updateReplyContentById(NumberUtil.getInt(contentid), replyContent, "2");
	}
	
	/**
	 * 保存
	 * @param contentid
	 * @param replyContent
	 * @return
	 */
	public Integer saveReply(String contentid, String replyContent) {
		return contentRecoDAO.updateReplyContentById(NumberUtil.getInt(contentid), replyContent);
	}
	
	/**
	 * 循环插入填空
	 * @param contentRecoList
	 * @return
	 */
	public boolean insertFor(List<ContentReco> contentRecoList) {
		boolean success = false;
		for(ContentReco reco:contentRecoList){
			success = contentRecoDAO.insert(reco) > 0 ? true:false;
		}
		return success;
	}
}
