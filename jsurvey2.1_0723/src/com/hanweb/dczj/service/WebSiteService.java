package com.hanweb.dczj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.dczj.controller.website.WebSiteFormBean;
import com.hanweb.dczj.dao.WebSiteDAO;
import com.hanweb.dczj.entity.DCZJ_WebSite;

public class WebSiteService {

	@Autowired
	private WebSiteDAO webSiteDAO;
	
	@Autowired
	private TitleInfoService titleInfoService;
	
	
	/**
	 * 通过网站ID获得下属网站集合（树）
	 * 
	 * @param iid
	 *           网站ID
	 * @return List<DCZJ_WebSite>
	 */
	public List<DCZJ_WebSite> findChildWebByIid(Integer rangeId) {
		return webSiteDAO.findChildWebByIid(rangeId);
	}

	/**
	 * 通过网站ID获取网站实体
	 * 
	 * @param iid
	 *            网站ID
	 * @return 网站实体
	 */
	public DCZJ_WebSite findByIid(Integer iid) {
		if(iid == null){
			iid = 0;
		}
		DCZJ_WebSite webSite = webSiteDAO.findByIid(iid);
		return webSite;
	}

	/**
	 * 新增网站
	 * 
	 * @param web
	 *            网站实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean add(WebSiteFormBean web) throws OperationException{
		if (web == null) {
			return false;
		}
//		boolean isSuccess = false;
		String codeId = web.getCodeId();
//		List<Integer> roleIdList = StringUtil.toIntegerList(web.getRoleIds(), ",");

		DCZJ_WebSite tempWeb = this.findByCodeId(codeId);
		if (tempWeb != null) {
			throw new OperationException("网站标识已存在,请重新设置！");
		}
		int num = this.findNumOfSameName(web.getName(), web.getPid());
		if (num > 0) {
			throw new OperationException("网站名称已存在,请重新设置！");
		}
		web.setPinYin(PinyinUtil.getHeadByString(web.getName()));

		int iid = webSiteDAO.insert(web);
		return iid > 0 ? true : false;
	}

	/**
	 * 通过网站名称及父网站ID获得同名网站名称的个数
	 * 
	 * @param name
	 *            网站名称
	 * @param pid
	 *            父网站ID
	 * @return 0 - 不存在<br/>
	 *         n - 存在n个
	 */
	private int findNumOfSameName(String name, Integer pid) {
		return findNumOfSameName(0, name, pid);
	}

	/**
	 * 通过网站ID、网站名称及父网站ID获得同名网站名称的个数
	 * 
	 * @param id
	 *            网站ID
	 * @param name
	 *            网站名称
	 * @param pid
	 *            父网站ID
	 * @return 0 - 不存在<br/>
	 *         n - 存在n个
	 */
	private int findNumOfSameName(int id, String name, Integer pid) {
		if (StringUtil.isEmpty(name)) {
			return 0;
		}
		int num = webSiteDAO.findNumOfSameName(id, name, pid);
		return num;
	}

	/**
	 * 通过网站标识取得网站
	 * 
	 * @param codeId
	 *            网站标识
	 * @return 网站实体
	 */
	private DCZJ_WebSite findByCodeId(String codeId) {
		if (StringUtil.isEmpty(codeId)) {
			return null;
		}
		DCZJ_WebSite webSite = webSiteDAO.findByCodeId(codeId);
		return webSite;
	}

	
	/**
	 * 修改网站
	 * 
	 * @param group
	 *            网站实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean modify(WebSiteFormBean web) throws OperationException{
		if (web == null || NumberUtil.getInt(web.getIid()) == 0) {
			return false;
		}
		Integer iid = web.getIid();
		boolean isSuccess = false;

		List<Integer> groupIdList = new ArrayList<Integer>();
//		List<Integer> roleIdList = StringUtil.toIntegerList(web.getRoleIds(), ",");

		groupIdList.add(iid);

		int num = this.findNumOfSameName(iid, web.getName(), web.getPid());
		if (num > 0) {
			throw new OperationException("网站名称已存在,请重新设置！");
		}
		web.setPinYin(PinyinUtil.getHeadByString(web.getName()));

		isSuccess = webSiteDAO.update(web);

/*		if (!isSuccess) {
			throw new OperationException("更新操作失败！");
		}

		isSuccess = roleRelationDAO.deleteGroups(null, groupIdList);
		if (!isSuccess) {
			throw new OperationException("删除用户角色关系失败！");
		}
		// 新增用户对应的角色
		if (isSuccess && CollectionUtils.isNotEmpty(roleIdList)) {
			isSuccess = roleService.modifyGroupMembers(roleIdList, iid);
			if (!isSuccess) {
				throw new OperationException("更新机构角色关系失败！");
			}
		}*/

		return isSuccess;
	}

	/**
	 * 递归获得父网站下的所有子孙网站
	 * 
	 * @param pid
	 *            父网站ID
	 * @param webList
	 *            网站实体List
	 * @return 网站实体List
	 */
	public List<Integer> findIdsByPId(Integer pid,List<Integer> idsList) {
		if (idsList == null) {
			idsList = new ArrayList<Integer>();
		}
		List<Map<String, Object>> chlidIdsList = webSiteDAO.findIdsByPid(pid);

		int id = 0;

		for (Map<String, Object> map : chlidIdsList) {
			id = NumberUtil.getInt(map.get("iid"));
			idsList.add(id);
			idsList = this.findIdsByPId(id, idsList);
		}

		return idsList;
	}

	/**
	 * 删除网站
	 * 
	 * @param ids
	 *            网站ID串 如:1,2,3
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean removeByIds(String ids) throws OperationException {
		List<Integer> idsLsit = StringUtil.toIntegerList(ids, ",");

		if (CollectionUtils.isEmpty(idsLsit)) {
			return false;
		}
		boolean isSuccess = false;

		boolean hasSubWeb = this.checkSubWeb(ids);
		if (hasSubWeb) {
			throw new OperationException("所选网站存在下属网站,请先删除下属网站!");
		}
//		boolean hasSubUser = this.checkSubUser(ids);
//		if (hasSubUser) {
//			throw new OperationException("所选网站存在用户,请先删除用户!");
//		}
		isSuccess = webSiteDAO.deleteByIds(idsLsit);/* 删除网站 */
		if (!isSuccess) {
			throw new OperationException("删除网站失败!");
		}
//		isSuccess = groupManagerDAO.deleteByGroupIds(idsLsit);/* 删除对应用户机构管理关系 */
//		if (!isSuccess) {
//			throw new OperationException("删除对应用户机构管理关系失败!");
//		}
//		isSuccess = roleRelationDAO.deleteByGroupIds(idsLsit);/* 删除对应角色机构关系 */
//		if (!isSuccess) {
//			throw new OperationException("删除对应角色机构关系失败!");
//		}
		return isSuccess;
	}

	/**
	 * 检查是否有下属网站
	 * 
	 * @param ids
	 *            网站ID串 如:1,2,3
	 * @return true - 有下属网站<br/>
	 *         false - 无下属网站
	 */
	public boolean checkSubWeb(String ids) {
		List<Integer> idsList = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idsList)) {
			return false;
		}
		int num = webSiteDAO.findCountSubGroup(idsList);

		return num > 0 ? true : false;
	}
	
	/**
	 * 检查网站中是否有用户存在
	 * 
	 * @param ids
	 *            网站ID串 如:1,2,3
	 * @return true - 有用户<br/>
	 *         false - 无用户
	 */
//	public boolean checkSubUser(String ids) {
//		List<Integer> idsList = StringUtil.toIntegerList(ids, ",");
//		if (CollectionUtils.isEmpty(idsList)) {
//			return false;
//		}
//		int num = userDAO.findCountSubUser(idsList);
//
//		return num > 0 ? true : false;
//	}

	/**
	 * 迭代获得网站下的所有子孙网站
	 * 
	 * @param id
	 *            网站ID
	 * @param webList
	 *            网站实体List
	 * @return 网站实体List
	 */
	public List<DCZJ_WebSite> findAllSeedsById(Integer id) {
		List<DCZJ_WebSite> chlidWebList = this.findChildWebByIid(id);
		if (chlidWebList == null) {
			return null;
		}
		List<DCZJ_WebSite> webList = new ArrayList<DCZJ_WebSite>();
		List<DCZJ_WebSite> seedsList = null;
		for (DCZJ_WebSite website : chlidWebList) {
			webList.add(website);
			seedsList = this.findAllSeedsById(website.getIid());
			webList.addAll(seedsList);
		}
		
		return webList;
	}

	/**
	 * 找出网站下所有下属网站
	 * 
	 * @param id
	 *            网站ID
	 * @return 网站实体List
	 */
	public List<DCZJ_WebSite> findChildrenById(Integer id) {
		List<DCZJ_WebSite> chlidWebList = webSiteDAO.findChildrenById(id);
		return chlidWebList;
	}

	/**
	 * 通过网站IDS，获得网站名称
	 * 
	 * @param ids
	 *            网站IDs
	 * @return 网站名称
	 */
	public String findRangeWebNamesByWebIds(String rangWebIds) {
		String RangeWebNames = "";
		String webName = "";
		List<Integer> webIdList = StringUtil.toIntegerList(rangWebIds, ",");
		if(CollectionUtils.isNotEmpty(webIdList)){
			for(Integer webid : webIdList){
				webName = webSiteDAO.findWebNameByIid(webid);
				RangeWebNames += webName + ",";
			}
		}
		if(StringUtil.isNotEmpty(RangeWebNames)){
			RangeWebNames = RangeWebNames.substring(0,RangeWebNames.length()-1);
		}
		return RangeWebNames;
	}

	/**
	 * 通过webid找到网站名称
	 * @param webid
	 * @return
	 */
	public String findWebNameByWebId(Integer webid) {
		return webSiteDAO.findWebNameByIid(webid);
	}

	/**
	 * 查找所有网站集合
	 * @return
	 */
	public List<DCZJ_WebSite> findAllWebId(){
		return webSiteDAO.findAllWebId();
	}

	/**
	 * 检查网站下是否还有问卷表
	 * @param webid
	 * @return
	 */
	public boolean checkSubJsurvey(int webid) {
		int count = 0;
		count = titleInfoService.checkNumJsurveyByWebid(webid);
		if(count > 0) {
			return false;
		}
		return true;
	}
}
