package com.hanweb.dczj.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.TitleInfoDAO;
import com.hanweb.dczj.entity.TitleInfo;

public class TitleInfoService {

	@Autowired
	private TitleInfoDAO titleInfoDAO;
	
	/**
	 * title新增
	 * @param infoEn
	 * @return
	 */
	public int add(TitleInfo infoEn) {
		return titleInfoDAO.insert(infoEn);
	}
	
	/**
	 * iid查找实体
	 * @param int1
	 * @return
	 */
	public TitleInfo getEntity(int iid) {
		return titleInfoDAO.queryForEntityById(iid);
	}
	
	/**
	 * 
	 * @param webid
	 * @param dczjtype
	 * @param dczjstate
	 * @param titlename
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<TitleInfo> findInfoListByWebid(int webid,int dczjtype, int dczjstate, String titlename, int page, int limit) {
		return titleInfoDAO.findInfoListByWebid(webid,dczjtype,dczjstate,titlename,page,limit);
	}

	/**
	 * 
	 * @param dczjtype
	 * @param dczjstate
	 * @param titlename
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<TitleInfo> findInfoList(int dczjtype, int dczjstate, String titlename, int page, int limit) {
		return titleInfoDAO.findInfoList(dczjtype,dczjstate,titlename,page,limit);
	}

	/**
	 * 
	 * @param dczjtype 
	 * @param dczjstate 
	 * @return
	 */
	public int findNum(int dczjtype, int dczjstate, String titlename) {
		return titleInfoDAO.findNum(dczjtype,dczjstate,titlename);
	}

	/**
	 * 
	 * @param dczjtype 
	 * @param webid
	 * @return
	 */
	public int findNumByWebid(int dczjtype,int dczjstate,String titlename,int webid) {
		return titleInfoDAO.findNumByWebid(dczjtype,dczjstate,titlename,webid);
	}

	/**
	 * 根据站点ID查找所有问卷实体
	 * @param webid
	 * @return
	 */
	public int findNumByWebid(int webid) {
		return titleInfoDAO.findNumByWebid(webid);
	}
	
	/**
	 * 
	 * @param webid
	 * @param type
	 * @return
	 */
    public List<TitleInfo> findTitleByWebIdAndType(Integer webid, int type) {
		return titleInfoDAO.findTitleByWebIdAndType(webid,type);
	}

    /**
	 * 
	 * @param webid
	 * @param datatype
	 * 数据类型
	 * @return
	 */
    public List<TitleInfo> findTitleByWebId(int webid, String datatype) {
		return titleInfoDAO.findTitleByWebId(webid, datatype);
	}
	
    public List<TitleInfo> findTitleByWebId1(int webid, String datatype) {
		return titleInfoDAO.findTitleByWebId1(webid, datatype);
	}
    
	public List<Integer> findDczjIdByWebIdAndType(int webid, int type) {
		List<TitleInfo> titleList = this.findTitleByWebIdAndType(webid, type);
		ArrayList<Integer> dczjIdList = new ArrayList<Integer>();
		if(titleList != null && titleList.size()>0) {
			for(TitleInfo en : titleList){
				dczjIdList.add(en.getIid());
			}
		}
		return dczjIdList;
	}

	/**
	 * 查找最新的排序
	 * @param dczjIdList
	 * @param type 
	 * @return
	 */
	public String[][] findMinOrder(List<Integer> dczjIdList, int type) {
		return titleInfoDAO.findMinOrder(dczjIdList,type);
	}

	/**
	 * 更新排序字段
	 * @param integer
	 * @param i
	 * @return
	 */
	public boolean modifyOrder(Integer iid, int orderid) {
		return titleInfoDAO.modifyOrder(iid,orderid);
	}

	/**
	 * 删除
	 * @param dczjid
	 * @return
	 */
	public boolean delete(int dczjid) {
		return titleInfoDAO.delete(dczjid);
	}

	/**
	 * 改变发布状态
	 * @param dczjid
	 * @param isBuild
	 */
	public boolean setUpdateHtml(String dczjid, int isBuild) {
		return titleInfoDAO.setUpdateHtml(dczjid, isBuild);
	}
	
	/**
	 * 改变调查征集状态
	 * @param dczjid
	 * @param state
	 * @return
	 */
	public boolean updateState(String dczjid, int state) {
		return titleInfoDAO.updateState(dczjid, state);
	}

	/**
	 * 修改实体
	 * @param infoEn
	 * @return
	 */
	public boolean modify(TitleInfo infoEn) {
		return titleInfoDAO.update(infoEn);
	}
	
	/**
	 * 按进行状态state获取list
	 * @param state
	 * @return
	 */
	public List<TitleInfo> getTitleListByStatus(String state){
		return titleInfoDAO.getTitleListByStatus(state);
	}

	public int checkNumJsurveyByWebid(int webid) {
		return titleInfoDAO.checkNumJsurveyByWebid(webid);
	}

	public List<TitleInfo> findTitleByWebId(String webid) {
		return titleInfoDAO.findTitleByWebId(webid);
	}

}
