package com.hanweb.dczj.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.dao.CheckedBoxRecoDAO;
import com.hanweb.dczj.dao.ContentRecoDAO;
import com.hanweb.dczj.dao.CountDAO;
import com.hanweb.dczj.dao.RadioRecoDAO;
import com.hanweb.dczj.dao.TotalRecoDAO;
import com.hanweb.dczj.entity.TotalReco;

public class TotalRecoService {

	@Autowired
	private TotalRecoDAO totalRecoDAO;
	@Autowired
	private RadioRecoDAO radioRecoDAO;
	@Autowired
	private CheckedBoxRecoDAO checkedBoxRecoDAO;
	@Autowired
	private ContentRecoDAO contentRecoDAO;
	@Autowired
	private CountDAO countDAO;
	
	public List<TotalReco> findSubmitRecoListsByFormid(String dczjid, int page, int pagesize, String starttime, String endtime,String check,String sorttype){
		return totalRecoDAO.findSubmitRecoListsByFormid(dczjid, page, pagesize, starttime, endtime,check,sorttype);
	}
	
	public int getTotalSurveyNumByFormid(String dczjid, String starttime, String endtime){
		return totalRecoDAO.getTotalSurveyNumByFormid(dczjid, starttime, endtime);
	}
	public TotalReco findById(String id) {
		return totalRecoDAO.queryForEntityById(NumberUtil.getInt(id));
	}
	
	public int removeById(String id) {
		return totalRecoDAO.remove(NumberUtil.getInt(id));
	}
	
	/**
	 * 删除表中数据  同时删除关联radio checkbox content count中数据
	 * @param dczjid
	 * @return
	 */
	public int removeAll(String dczjid) {
		try {
			radioRecoDAO.removeByDczjid(dczjid);
			checkedBoxRecoDAO.removeByDczjid(dczjid);
			contentRecoDAO.removeByDczjid(dczjid);
			totalRecoDAO.removeAll(NumberUtil.getInt(dczjid));
			//最后清除统计数据
			countDAO.removeByDczjid(dczjid);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<TotalReco> findSubmitRecoListsByFormid(String dczjid, String starttime, String endtime,String check,String sorttype){
		List<TotalReco> totalRecos = new ArrayList<TotalReco>();
		int num = totalRecoDAO.getTotalSurveyNumByFormid(dczjid, starttime, endtime);
		int page = num/1000 + 1;
		for(int i=1;i<=page;i++) {
			totalRecos.addAll(totalRecoDAO.findSubmitRecoListsByFormid(dczjid, i, 1000, starttime, endtime,check,sorttype));
		}
		return totalRecos;
	}
	
	/**
	 * 根据调查征集ID获取问题与返回值对应的map
	 * @param dczjid
	 * @return MAP  key:quesid_unid  value:对应的答案字符串
	 */
	public Map<String, String> getExcelValueMap(String dczjid){
		Map<String, String> valueMap = new HashMap<String, String>();
		int page = 0;
		int num = 0;
		String[][] array = null;
		//单选
		num = radioRecoDAO.findExcelValueCount(dczjid);
		page = num/1000 + 1;
		Map<String, String> radioMap = new HashMap<String, String>();
		for(int i=1;i<=page;i++) {
			array = radioRecoDAO.findExcelValue(dczjid, i, 1000);
			if(array != null && array.length > 0) {
				for(int j=0;j<array.length;j++) {
					radioMap.put(array[j][0]+"_"+array[j][2], array[j][1]);
				}
			}
		}
		//多选
		num = checkedBoxRecoDAO.findExcelValueCount(dczjid);
		page = num/1000 + 1;
		Map<String, String> checkboxMap = new HashMap<String, String>();
		for(int ii=1;ii<=page;ii++) {
			array = checkedBoxRecoDAO.findExcelValue(dczjid, ii, 1000);
			if(array != null && array.length > 0) {
				for(int jj=0;jj<array.length;jj++) {
					if(checkboxMap.get(array[jj][0]+"_"+array[jj][2]) != null) {
						String checkbox = checkboxMap.get(array[jj][0]+"_"+array[jj][2]);
						checkboxMap.put(array[jj][0]+"_"+array[jj][2], checkbox+","+array[jj][1]);
					}else {
						checkboxMap.put(array[jj][0]+"_"+array[jj][2], array[jj][1]);
					}
				}
			}
		}
		//文本
		num = contentRecoDAO.findExcelValueCount(dczjid);
		page = num/1000 + 1;
		Map<String, String> contentMap = new HashMap<String, String>();
		for(int iii=1;iii<=page;iii++) {
			array = contentRecoDAO.findExcelValue(dczjid, iii, 1000);
			if(array != null && array.length > 0) {
				for(int jjj=0;jjj<array.length;jjj++) {
					contentMap.put(array[jjj][0]+"_"+array[jjj][2], array[jjj][1]);
				}
			}
		}
		
		valueMap.putAll(radioMap);
		valueMap.putAll(checkboxMap);
		valueMap.putAll(contentMap);
		return valueMap;
	}
	
	/**
	 * 插入实体
	 * @param totalReco
	 * @return
	 */
	public boolean insertEntity(TotalReco totalReco) {
		int flag = totalRecoDAO.insert(totalReco);
		if(flag > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断是否已经提及数据
	 * @param unid
	 * @return
	 */
	public boolean findRecoIsHaveSubmitByUnid(String unid) {
		if(StringUtil.isEmpty(unid)){
			return false;
		}
		return totalRecoDAO.findRecoIsHaveSubmitByUnid(unid) > 0 ? true : false;
	}
	
	/**
	 * 查找未提交状态的数据
	 * @return
	 */
	public List<TotalReco> findSubmitRecoLists(String state) {
		int count = totalRecoDAO.getAllTotalSurveyNum();
		int pagesize = 1000;
		int start = 0;
		int a = count % pagesize;
		int b = count / pagesize;
		int pageNum;
		if (a == 0) {
			pageNum = b;
		} else {
			pageNum = b + 1;
		}
		TotalReco survey_totalreco = null;
		List<TotalReco> survey_totalrecoList = new ArrayList<TotalReco>();
		for(int i=1;i<=pageNum; i++){
			start = (i - 1) * pagesize;
			String[][] survey_totalrecoListData = totalRecoDAO.findSubmitRecoLists(state, start, pagesize);
			if(survey_totalrecoListData!=null&&survey_totalrecoListData.length>0){
				for(int ir = 0; ir <survey_totalrecoListData.length; ir++){
					survey_totalreco = new TotalReco();
					survey_totalreco.setIp(survey_totalrecoListData[ir][3]);
					survey_totalreco.setMobile(survey_totalrecoListData[ir][0]);
					survey_totalreco.setDczjid(Integer.valueOf(survey_totalrecoListData[ir][1]));
					survey_totalrecoList.add(survey_totalreco);
				}
			}
		}
		return survey_totalrecoList;
	}

	public int findRecoIsHaveSubmitByDczjid(String dczjid) {
		return totalRecoDAO.findRecoIsHaveSubmitByDczjid(dczjid);
	}

	public int findResourceCount(String dczjid, int type) {
		return totalRecoDAO.findResourceCount(dczjid,type);
	}

	public String[][] findAddressCount(String dczjid) {
		return totalRecoDAO.findAddressCount(dczjid);
	}

	public String[][] findIpCount(String dczjid) {
		return totalRecoDAO.findIpCount(dczjid);
	}
	public int findMaxScore(String dczjid) {
		return totalRecoDAO.findMaxScore(dczjid);
	}
	public int findMinScore(String dczjid) {
		return totalRecoDAO.findMinScore(dczjid);
	}
	public int findAvgScore(String dczjid) {
		return totalRecoDAO.findAvgScore(dczjid);
	}
	public int findSumScore(String dczjid) {
		return totalRecoDAO.findSumScore(dczjid);
	}

	public int findNumByGrade(int minscore, int maxscore, String dczjid) {
		return totalRecoDAO.findNumByGrade(minscore,maxscore,dczjid);
	}

}
