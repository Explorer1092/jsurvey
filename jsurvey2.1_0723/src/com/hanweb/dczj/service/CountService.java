package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.CountDAO;
import com.hanweb.dczj.entity.TitleInfo;

/**
 * 统计service
 * @author Administrator
 *
 */
public class CountService {
	
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private CountDAO countDAO;
	@Autowired
	private RadioRecoService radioRecoService;
	@Autowired
	private CheckedBoxRecoService checkedBoxRecoService;
    @Autowired
    private VisitCountService visitCountService;
	
	/**
	 * 统计方法
	 */
	public void count() {
		//1.获取所有进行中的dczjid
		List<TitleInfo> titleInfos = titleInfoService.getTitleListByStatus("0");
		if(titleInfos != null && titleInfos.size()>0) {
			//2.遍历dczjid收集统计信息
			for(TitleInfo titleInfo : titleInfos) {
				radioRecoService.countByDczjid(titleInfo.getIid()+"");
				checkedBoxRecoService.countByDczjid(titleInfo.getIid()+"");
				visitCountService.modifyVc(titleInfo.getIid()+"");
			}
		}
	}
	
	/**
	 * 获取指定时间内的答题数量
	 * @param starttime
	 * @param endtime
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @return
	 */
	public Integer getCountResult(String starttime, String endtime, Integer dczjid, Integer quesid, Integer answid) {
		return countDAO.getCountResult(starttime, endtime, dczjid, quesid, answid);
	}
	
	
	/**
	 * 获取统计数据
	 * @param i_id
	 * @param quesId
	 * @param i_basepoint
	 * @param basePointSum
	 * @return
	 */
	public String[][] getCurrentData(int answId,int quesId,int basePoint,int basePointSum){
		String[][] strData = countDAO.getCurrentData(answId, quesId);
		if(strData==null||strData.length==0){
			String[][] init = {{"0","0"}};
			return init; 
		}
		int thisCount = NumberUtil.getInt(strData[0][0]) + basePoint;
		int totalCount = NumberUtil.getInt(strData[0][1]) + basePointSum;
		double percent = Math.round((double)thisCount/totalCount*10000)/100.0;
		String[][] out = {{""+thisCount,""+percent}};
		return out;
		
	}
	
	/**
	 * 单选题实体总数
	 * @param dczjid
	 * @param quesid
	 * @param answid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Integer radioAnsListCount(String dczjid, String quesid, String answid) {
		return countDAO.findEntitysCount(NumberUtil.getInt(dczjid), NumberUtil.getInt(quesid), NumberUtil.getInt(answid));
	}
	
	public int getRecoConutByTime(String quesid) {		
		return countDAO.getRecoConutByTime(NumberUtil.getInt(quesid));
	}

	public int checkRightBoxAnswCount(String answid) {
		return countDAO.checkRightBoxAnswCount(NumberUtil.getInt(answid));
	}

	public int getCheckConutByTime(String quesid) {
		return countDAO.getCheckConutByTime(NumberUtil.getInt(quesid));
	}

	
	
}
