package com.hanweb.dczj.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.dao.AnswInfoDAO;
import com.hanweb.dczj.entity.AnswInfo;

public class AnswInfoService {

	@Autowired
	private AnswInfoDAO answInfoDAO;
	
	/**
	 * 新增
	 * @param answEn
	 * @return
	 */
	public int add(AnswInfo answEn) {
		return answInfoDAO.insert(answEn);
	}

	/**
	 *  通过quesid获取list
	 * @param quesId
	 * @return
	 */
	public List<AnswInfo> getAnswListByQuesId(int quesId) {
		return answInfoDAO.getAnswListByQuesId(quesId);
	}

	/**
	 * 通过quesid获取answid
	 * @param quesid
	 * @return
	 */
	public List<Integer> findAnswidByQuesid(Integer quesid) {
		ArrayList<Integer>answIdList = new ArrayList<Integer>();
		List<AnswInfo> answList = this.getAnswListByQuesId(quesid);
		if(answList != null &&answList.size() >0){
			for(AnswInfo en : answList){
				answIdList.add(en.getIid());
			}
		}
		return answIdList;
	}

	/**
	 * 删除答案
	 * @param quesid
	 * @return
	 */
	public boolean deleteByQuesid(String quesid) {
		return answInfoDAO.deleteByQuesId(quesid, "1");//删除答案
	}

	/**
	 * 查看答案总数量
	 * @param quesId
	 * @return
	 */
	public int findCount(int quesid) {
		if(StringUtil.isEmpty(quesid+"")){
			return 0;
		}
		return answInfoDAO.findCount(quesid);
	}

	/**
	 * 获取实体
	 * @param answid
	 * @return
	 */
	public AnswInfo getEntity(int answid) {
		return answInfoDAO.queryForEntityById(answid);
	}

	/**
	 * 删除答案
	 * @param answid
	 * @return
	 */
	public boolean delete(String answid) {
		return answInfoDAO.delete(answid);
	}

	/**
	 * 查看最小排序
	 * @param idsList
	 * @return
	 */
	public String[][] findMinOrder(List<Integer> answIdList) {
		return answInfoDAO.findMinOrder(answIdList);
	}

	/**
	 * 更改排序字段
	 * @param iid
	 * @param newOrderid
	 * @return
	 */
	public boolean modifyOrder(Integer iid, int newOrderid) {
		return answInfoDAO.modifyOrder(iid,newOrderid);
	}

	/**
	 * 选项修改
	 * @param surveyAnsw
	 * @return
	 */
	public boolean modify(AnswInfo surveyAnsw) {
		return answInfoDAO.update(surveyAnsw);
	}

	public int getBasePointSum(int quesId) {
		return answInfoDAO.getBasePointSum(quesId);
	}

	/**
	 * 通过题目Id找到正确选项
	 * @param iid
	 * @return
	 */
	public String findRightAnswByQuesid(Integer quesId) {
		String rightAnsw = "";
		List<AnswInfo> answInfoList = answInfoDAO.getAnswListByQuesId(quesId);
		if(answInfoList != null && answInfoList.size() >0) {
			for(AnswInfo answEn : answInfoList) {
				if(StringUtil.equals(answEn.getIsright(), "1")) {
					rightAnsw += answEn.getAnswname()+",";
				}
			}
		}
		if(StringUtil.isNotEmpty(rightAnsw)) {
			rightAnsw = rightAnsw.substring(0,rightAnsw.length()-1);
		}
		return rightAnsw;
	} 

	public int findNumByDczjid(int dczjid) {
		return answInfoDAO.getNumByDczjid(dczjid);	
	}
	public String[][] findRightAnswid(String quesid){
		return answInfoDAO.findRightAnsw(quesid);
	}
	public int findRightNum(String quesid) {
		return answInfoDAO.findRightNum(quesid);
	}

	public String findRightAnswIdByQuesid(Integer quesId) {
		String rightAnsw = "";
		List<AnswInfo> answInfoList = answInfoDAO.getAnswListByQuesId(quesId);
		if(answInfoList != null && answInfoList.size() >0) {
			for(AnswInfo answEn : answInfoList) {
				if(StringUtil.equals(answEn.getIsright(), "1")) {
					rightAnsw += answEn.getIid()+",";
				}
			}
		}
		if(StringUtil.isNotEmpty(rightAnsw)) {
			rightAnsw = rightAnsw.substring(0,rightAnsw.length()-1);
		}
		return rightAnsw;
	}
}
