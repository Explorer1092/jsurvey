package com.hanweb.complat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.RoleRelation;

/**
 * 角色成员关系
 * 
 * @author ZhangC
 * 
 */
public class RoleRelationDAO extends BaseJdbcDAO<Integer, RoleRelation> {

	/**
	 * 取得机构关联的角色id集合
	 * 
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public List<String> findRoleIdsByGroupId(Integer groupId) {
		List<String> roleIdList = new ArrayList<String>();
		String sql = "SELECT roleid FROM " + Tables.ROLERELATION + " WHERE groupid = :groupId";

		Query query = createQuery(sql);

		query.addParameter("groupId", groupId);

		List<Map<String, Object>> resultList = this.queryForList(query);

		for (Map<String, Object> result : resultList) {
			roleIdList.add(NumberUtil.getInt(result.get("roleid")) + "");
		}
		return roleIdList;
	}

	/**
	 * 根据机构ID串，删除其对应的角色
	 * 
	 * @param groupIdsList
	 *            机构ID 集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteByGroupIds(List<Integer> groupIdsList) {
		return deleteGroups(null, groupIdsList);
	}

	/**
	 * 删除机构对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param groupIdsList
	 *            机构ID 集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteGroups(Integer roleId, List<Integer> groupIdsList) {
		String sql = "DELETE FROM " + Tables.ROLERELATION + " WHERE groupid IN(:groupIds)";
		if (NumberUtil.getInt(roleId) > 0) {
			sql += " AND roleid = :roleId";
		}
		Query query = createQuery(sql);
		query.addParameter("roleId", roleId);
		query.addParameter("groupIds", groupIdsList);

		return this.delete(query);
	}

	/**
	 * 删除用户对应的角色成员关系
	 * 
	 * @param roleId
	 *            角色ID
	 * @param userIdsList
	 *            用户ID 集合
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteUsers(Integer roleId, List<Integer> userIdsList) {
		String sql = "DELETE FROM " + Tables.ROLERELATION + " WHERE userid IN(:userIdsList)";
		if (NumberUtil.getInt(roleId) > 0) {
			sql += " AND roleid = :roleId";
		}
		Query query = createQuery(sql);
		query.addParameter("roleId", roleId);
		query.addParameter("userIdsList", userIdsList);

		return this.delete(query);
	}

	/**
	 * 清除角色相关的机构用户关系
	 * 
	 * @param roleIdList
	 *            角色ID集
	 * @return
	 */
	public boolean deleteByroleId(List<Integer> roleIdList) {
		String sql = "DELETE FROM " + Tables.ROLERELATION + " WHERE roleid IN(:roleIdList)";
		Query query = createQuery(sql);
		query.addParameter("roleIdList", roleIdList);

		return this.delete(query);
	}

	/**
	 * 获得机构型成员的数量
	 * 
	 * @param roleId
	 * @param groupId
	 * @return
	 */
	public int findGroupMemberSize(Integer roleId, Integer groupId) {
		String sql = "SELECT COUNT(*) FROM " + Tables.ROLERELATION
				+ " WHERE roleid = :roleId AND groupid = :groupId";

		Query query = createQuery(sql);

		query.addParameter("roleId", roleId);
		query.addParameter("groupId", groupId);

		Integer size = this.queryForInteger(query);
		return NumberUtil.getInt(size);
	}

	/**
	 * 获得用户型成员的数量
	 * 
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public int findUserMemberSize(Integer roleId, Integer userId) {
		String sql = "SELECT COUNT(*) FROM " + Tables.ROLERELATION
				+ " WHERE roleid = :roleId AND userid = :userId";

		Query query = createQuery(sql);

		query.addParameter("roleId", roleId);
		query.addParameter("userId", userId);

		Integer size = this.queryForInteger(query);
		return NumberUtil.getInt(size);
	}
	/**
	 * 根据用户ID获取角色ID
	 * @param userId
	 * @return
	 */
	public int findRoleByUserId(Integer userId) {
		String sql = "SELECT roleid FROM " + Tables.ROLERELATION
				+ " WHERE userid = :userId";
		Query query = createQuery(sql);
		query.addParameter("userId", userId);
		Integer size = this.queryForInteger(query);
		return NumberUtil.getInt(size);
	}
	
}
