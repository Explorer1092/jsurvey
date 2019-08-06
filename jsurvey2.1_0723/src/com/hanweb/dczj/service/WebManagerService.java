package com.hanweb.dczj.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.dao.WebManagerDAO;
import com.hanweb.dczj.entity.Dczj_WebManager;

public class WebManagerService {

	@Autowired
	WebManagerDAO webManagerDAO;
	
	/**
	 * 新增用户管理的网站关系
	 * 
	 * @param userId
	 * @param webIdList
	 * @return
	 */
	public boolean add(int userId, List<Integer> webIdList) {
		boolean isSuccess = false;
		if(CollectionUtils.isNotEmpty(webIdList)){
			for(Integer webId : webIdList){
				if (NumberUtil.getInt(webId) > 0){
					isSuccess = this.add(userId, webId);
					if(!isSuccess){
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 新增用户管理的网站关系
	 * 
	 * @param userId
	 * @param webId
	 * @return
	 */
	private boolean add(int userId, Integer webId) {
        Dczj_WebManager webManager = new Dczj_WebManager();
        webManager.setUserId(userId);
        webManager.setWebId(webId);
        
        int iid = webManagerDAO.insert(webManager);
        
		return iid > 0 ? true : false;
	}

	/**
	 * 通过用户ID获得其管理的网站ID
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public String findRangeWebIdsByUserId(Integer userId) {
		if (NumberUtil.getInt(userId) == 0) {
			return null;
		}
		String webids ="";
		List<Dczj_WebManager> webManagers = webManagerDAO.findRangeIdByUserId(userId);
		if(webManagers != null && webManagers.size()>0 ){
			for(Dczj_WebManager webManager : webManagers){
				webids += webManager.getWebId() + ",";
			}
		}
		if(StringUtil.isNotEmpty(webids)){
			webids = webids.substring(0, webids.length()-1);
		}
		return webids;
	}

	/**
	 * 通过userID获取 webids
	 * @param userId
	 * @return
	 */
	public List<Integer> findWebIdsByUserId(Integer userId) {
		if(NumberUtil.getInt(userId) == 0){
			return null;
		}
		String webIds = this.findRangeWebIdsByUserId(userId);
		List<Integer> webids = StringUtil.toIntegerList(webIds, ",");
		return webids;
	}

	/**
	 * 判断该网站是否被用户管理
	 * @param userId
	 * @param iid
	 * @return
	 */
	public boolean checkWebid(String userId,String iid) {
		List<Integer> webids = this.findWebIdsByUserId(NumberUtil.getInt(userId));
		if(webids != null && webids.size()>0){
			for(Integer webid : webids){
				if(StringUtil.equals(iid, webid+"")){
					return true;
				}
			}
		}
		return false;
	}

}
