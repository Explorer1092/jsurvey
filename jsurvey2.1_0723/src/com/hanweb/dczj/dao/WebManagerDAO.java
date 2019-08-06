package com.hanweb.dczj.dao;

import java.util.ArrayList;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Dczj_WebManager;

public class WebManagerDAO extends BaseJdbcDAO<Integer, Dczj_WebManager>{

	/**
	 * 通过用户ID串删除对应用户网站管理关系
	 * 
	 * @param ts
	 *            事务
	 * @param userIdsList
	 *            用户ID集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteByUserIds(List<Integer> userIdsList) {
		String sql = "DELETE FROM " + Tables.WEBMANAGER + " WHERE userid IN (:userIdsList)";
		Query query = createQuery(sql);
		query.addParameter("userIdsList", userIdsList);

		return this.delete(query);
	}

	/**
	 * 通过用户ID获得其管理的网站ID
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<Dczj_WebManager> findRangeIdByUserId(Integer userId) {
		List<Dczj_WebManager> webManagers = new ArrayList<Dczj_WebManager>();
		String sql = "SELECT webid FROM " + Tables.WEBMANAGER + " WHERE userid = :userId";
		Query query = createQuery(sql);
		query.addParameter("userId", userId);
		
		webManagers = super.queryForEntities(query);
		
		return webManagers;
	}

}
