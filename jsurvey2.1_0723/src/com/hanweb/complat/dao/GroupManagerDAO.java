package com.hanweb.complat.dao;

import java.util.List;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.GroupManager;

/**
 * 机构管理范围
 * 
 * @author ZhangC
 * 
 */
public class GroupManagerDAO extends BaseJdbcDAO<Integer, GroupManager> {

	/**
	 * 通过用户ID获得其管理的机构ID
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public Integer findRangeIdByUserId(Integer userId) {
		String sql = "SELECT groupid FROM " + Tables.GROUPMANAGER + " WHERE userid = :userId";
		Query query = createQuery(sql);
		query.addParameter("userId", userId);

		return this.queryForInteger(query);
	}

	/**
	 * 通过机构ID串删除对应用户机构管理关系
	 * 
	 * @param ts
	 *            事务
	 * @param groupIdsList
	 *            机构ID集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteByGroupIds(List<Integer> groupIdsList) {
		String sql = "DELETE FROM " + Tables.GROUPMANAGER + " WHERE groupid IN (:groupIdsList)";
		Query query = createQuery(sql);
		query.addParameter("groupIdsList", groupIdsList);

		return this.delete(query);
	}

	/**
	 * 通过用户ID串删除对应用户机构管理关系
	 * 
	 * @param ts
	 *            事务
	 * @param userIdsList
	 *            用户ID集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteByUserIds(List<Integer> userIdsList) {
		String sql = "DELETE FROM " + Tables.GROUPMANAGER + " WHERE userid IN (:userIdsList)";
		Query query = createQuery(sql);
		query.addParameter("userIdsList", userIdsList);

		return this.delete(query);
	}

}
