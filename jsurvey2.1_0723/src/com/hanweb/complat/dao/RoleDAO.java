package com.hanweb.complat.dao;

import java.util.ArrayList;
import java.util.List;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.Role;

/**
 * 角色
 * 
 * @author ZhangC
 * 
 */
public class RoleDAO extends BaseJdbcDAO<Integer, Role> {

	/**
	 * 通用角色id获得角色信息
	 * 
	 * @param iid
	 *            角色id
	 * @return
	 */
	public Role findByIid(int iid) {
		String sql = "SELECT iid, name, isdefault, spec, type FROM " + Tables.ROLE
				+ " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		Role role = this.queryForEntity(query);
		return role;
	}

	/**
	 * 获得同名角色的个数
	 * 
	 * @param iid
	 *            角色ID
	 * @param name
	 *            角色名称
	 * @return 0 - 无同名<br/>
	 *         n - n个同名
	 */
	public int findNumBySameName(int iid, String name) {
		String sql = "SELECT COUNT(iid) FROM " + Tables.ROLE + " WHERE name=:name";
		Query query = createQuery(sql);

		query.addParameter("name", name);

		if (iid > 0) {
			sql += " AND iid  <> :iid";
			query.setSql(sql);
			query.addParameter("iid", iid);
		}
		return this.queryForInteger(query);
	}

	/**
	 * 切换缺省状态
	 * 
	 * @param iid
	 *            角色ID
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateIsDefault(int iid, int isDefault) {
		UpdateSql sql = new UpdateSql(Tables.ROLE);

		sql.addInt("isdefault", isDefault);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return super.update(sql);
	}

	/**
	 * 获得用户缺省状态的角色 (包括缺省角色及其所在机构的角色)
	 * 
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public List<Role> findDefaultUserRoles(Integer groupId) {

		String sql = "(SELECT  b.iid, b.name FROM "
				+ Tables.ROLE
				+ " b WHERE b.isdefault=1) UNION "
				+ "(SELECT b.iid, b.name FROM "
				+ Tables.ROLERELATION
				+ " a,"
				+ Tables.ROLE
				+ " b WHERE a.roleid = b.iid AND (a.groupid = 0 OR a.groupid = :groupId)) ORDER BY iid";

		Query query = createQuery(sql);
		query.addParameter("groupId", groupId);

		List<Role> roleList = this.queryForEntities(query);

		return roleList;
	}

	/**
	 * 取得所有角色ID和名称
	 * 
	 * @param type
	 *            角色类型
	 * @return
	 */
	public List<Role> findAllRoles() {
		String sql = "SELECT iid, name, type FROM " + Tables.ROLE + " ORDER BY iid";
		Query query = createQuery(sql);
		List<Role> roleList = this.queryForEntities(query);

		return roleList;
	}

	/**
	 * 获得所有缺省角色
	 */
	public List<Role> findDefaultRoles() {
		String sql = "SELECT iid, name, type FROM " + Tables.ROLE
				+ " WHERE isdefault=1 ORDER BY iid";
		Query query = createQuery(sql);
		List<Role> roleList = this.queryForEntities(query);
		return roleList;
	}

	/**
	 * 获得用户及其所在机构的角色
	 * 
	 * @param userId
	 *            用户ID
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public List<Role> findRolesByUserIdOrGroupId(Integer userId, Integer groupId) {
		List<Role> roleList = new ArrayList<Role>();
		if (NumberUtil.getInt(userId) == 1) {
			Role role = new Role();
			role.setIid(1);
			role.setName("系统管理员");
			role.setType(0);
			roleList.add(role);
			return roleList;
		}

		String sql = "SELECT DISTINCT b.iid, b.name, b.type " + "FROM " + Tables.ROLERELATION
				+ " a," + Tables.ROLE + " b " + " WHERE a.roleid = b.iid  AND ( ";
		if (userId == null && groupId == null) {
			sql += "a.groupid = 0 ";
		} else {
			sql += "a.groupid = 0 OR (";
			if (userId == null) {
				sql += "a.userid IS NULL AND a.groupid = :groupId ";
			} else {
				sql += "a.userid = :userId ";
				if (groupId == null) {
					sql += "AND a.groupid IS NULL ";
				} else {
					sql += "OR a.groupid = :groupId ";
				}
			}
			sql += ")";
		}
		sql += " ) ORDER BY b.iid";

		Query query = createQuery(sql);

		query.addParameter("userId", userId);
		query.addParameter("groupId", groupId);

		roleList = super.queryForEntities(query);

		return roleList;
	}

	/**
	 * 按名称或名称首字母查询
	 * 
	 * @param keyword
	 * @return
	 */
	public List<Role> findByNameOrPinYin(String keyword) {
		String sql = "SELECT iid, name FROM " + Tables.ROLE
				+ " WHERE name LIKE :name OR pinyin LIKE :pinYin ";
		Query query = createQuery(sql);
		query.addParameter("name", keyword, LikeType.LR);
		query.addParameter("pinYin", keyword.toUpperCase(), LikeType.LR);

		List<Role> roleList = super.queryForEntities(query);
		return roleList;
	}

}
