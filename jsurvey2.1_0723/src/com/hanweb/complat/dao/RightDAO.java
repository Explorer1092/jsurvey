package com.hanweb.complat.dao;

import java.util.List;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.Right;

/**
 * 权限
 * 
 * @author ZhangC
 * 
 */
public class RightDAO extends BaseJdbcDAO<Integer, Right> {

	/**
	 * 获得用户的权限集合
	 * 
	 * @param userId
	 *            用户ID
	 * @param groupId
	 *            机构ID
	 * @return 权限实体集合
	 */
	public List<Right> findByUserIdOrGroupId(Integer userId, Integer groupId) {
		String sql = "SELECT d.iid, d.moduleid, d.modulename, d.functionid, d.functionname, "
				+ "d.dynamicid FROM " + Tables.ROLERELATION + " a, " + Tables.ROLE + " b,"
				+ Tables.ROLERIGHT + " c," + Tables.RIGHT + " d" + " WHERE a.roleid = b.iid "
				+ " AND b.iid = c.roleid" + " AND c.rightid = d.iid " + " AND( a.userid = :userId ";
		if (NumberUtil.getInt(groupId) > 0) {
			sql += " OR a.groupid = :groupId";
		}
		sql += ") ";
		Query query = createQuery(sql);
		query.addParameter("userId", userId);
		query.addParameter("groupId", groupId);

		List<Right> rightList = this.queryForEntities(query);

		return rightList;
	}

	/**
	 * 获得角色的权限集合
	 * 
	 * @param roleId
	 *            角色ID
	 * @return 权限实体集合
	 */
	public List<Right> findByRoleId(Integer roleId) {
		String sql = "SELECT b.iid, b.moduleid, b.modulename, b.functionid, b.functionname, "
				+ "b.dynamicid FROM " + Tables.ROLERIGHT + " a, " + Tables.RIGHT + " b "
				+ " WHERE a.rightid = b.iid " + " AND a.roleid = :roleId ORDER BY b.iid ";
		Query query = createQuery(sql);
		query.addParameter("roleId", roleId);

		List<Right> rightList = this.queryForEntities(query);
		return rightList;
	}

	/**
	 * 获得所有权限集合
	 * 
	 * @return 权限实体集合
	 */
	public List<Right> findAll() {
		String sql = getEntitySql() + " ORDER BY iid ";
		Query query = createQuery(sql);
		List<Right> rightList = this.queryForEntities(query);
		return rightList;
	}

}
