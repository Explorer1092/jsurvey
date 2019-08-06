package com.hanweb.dczj.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.CheckedBoxRecoDAO;
import com.hanweb.dczj.dao.CountDAO;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.Count;

public class CheckedBoxRecoService {

	@Autowired
	CheckedBoxRecoDAO checkedBoxRecoDAO;
	@Autowired
	CountDAO countDAO;
	
	/**
	 * 多选题实体总数
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Integer checkBoxAnsListCount(String dczjid, String quesid, String answid, String starttime, String endtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(starttime+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endtime+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return checkedBoxRecoDAO.findEntitysCount(NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid), NumberUtil.getInt(answid), startDate, endDate);
	}
	
	/**
	 * 多选题实体总数
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public Integer getRecoConut(String quesid, String answid) {
		return checkedBoxRecoDAO.findEntitysCount(NumberUtil.getInt(quesid), NumberUtil.getInt(answid));
	}
	
	/**
	 * 单选题实体集合（分页）
	 * @param page
	 * @param limit
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<CheckedBoxReco> checkBoxAnsList(String page, String limit, String dczjid, String quesid, String answid, String starttime, String endtime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(starttime+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endtime+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return checkedBoxRecoDAO.findEntitys(NumberUtil.getInt(page), NumberUtil.getInt(limit), NumberUtil.getInt(dczjid)
				, NumberUtil.getInt(quesid), NumberUtil.getInt(answid), startDate, endDate);
	}
	
	/**
	 * 单选题实体集合（分页）
	 * @param page
	 * @param limit
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public List<CheckedBoxReco> findOtherAnswerList(Integer page, Integer limit, String quesid, String answid){
		return checkedBoxRecoDAO.findEntitys(page, limit, NumberUtil.getInt(quesid), NumberUtil.getInt(answid));
	}
	
	public String getValueByDczjidAndQuesid(String dczjid, String quesid, String unid) {
		return checkedBoxRecoDAO.findValueByQuesAndDczjid(dczjid, quesid, unid);
	}
	
	/**
	 * 根据dczjid统计每个问题的选择个数，并入统计表
	 * @param dczjid
	 * @return
	 */
	public void countByDczjid(String dczjid) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = "";
		if(month<10) {
			if(day<10) {
				date = year+"-0"+month+"-0"+day;
			}else {
				date = year+"-0"+month+"-"+day;
			}
		}else {
			if(day<10) {
				date = year+"-"+month+"-0"+day;
			}else {
				date = year+"-"+month+"-"+day;
			}
		}
		List<Count> counts = checkedBoxRecoDAO.getCountByDczjid(dczjid,date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		if(month<10) {
			if(day<10) {
				date = year+"-0"+month+"-0"+day;
			}else {
				date = year+"-0"+month+"-"+day;
			}
		}else {
			if(day<10) {
				date = year+"-"+month+"-0"+day;
			}else {
				date = year+"-"+month+"-"+day;
			}
		}
		List<Count> yesterdayCounts = checkedBoxRecoDAO.getCountByDczjid(dczjid,date);
		if(yesterdayCounts != null && yesterdayCounts.size()>0 && counts != null && counts.size()>0) {
			counts.addAll(yesterdayCounts);
		}
		if(counts != null && counts.size()>0) {
			for(Count count : counts) {
				if(countDAO.getCountnNum(count.getCountdate(), count.getDczjid(), count.getQuesid(), count.getAnswid()) > 0) {
					countDAO.updateCount(count);
				}else {
					countDAO.insert(count);
				}
			}
		}
	}
	
	/**
	 * 循环插入多选
	 * @param checkedBoxRecoList
	 * @return
	 */
	public boolean insertFor(List<CheckedBoxReco> checkedBoxRecoList) {
		boolean success = false;
		for(CheckedBoxReco reco:checkedBoxRecoList){
			success = checkedBoxRecoDAO.insert(reco) > 0 ? true:false;
		}
		return success;
	}
	public List<CheckedBoxReco> findByunid(String unid) {
		return checkedBoxRecoDAO.queryByunid(unid);
	}
	public int findNumByunid(String unid) {
		return checkedBoxRecoDAO.queryNumByunid(unid);
	}
	public int findCountByunid(String unid,int answerid) {
		return checkedBoxRecoDAO.queryCountByunid(unid, answerid);
	}

	public int getCheckConutByTime(String quesid, String starttime, String endtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(starttime+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endtime+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return checkedBoxRecoDAO.getCheckConutByTime(NumberUtil.getInt(quesid),startDate, endDate);
	}

	public int checkRightBoxAnswCount(String answid, String starttime, String endtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date();
		try {
			startDate = sdf.parse(starttime+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = new Date();
		try {
			endDate = sdf.parse(endtime+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return checkedBoxRecoDAO.checkRightBoxAnswCount(NumberUtil.getInt(answid),startDate, endDate);
	
	}
}
