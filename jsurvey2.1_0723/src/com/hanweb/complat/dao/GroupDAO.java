package com.hanweb.complat.dao;

import java.util.List;
import java.util.Map;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.Group;

/**
 * 机构
 * @author 李杰
 *
 */
public class GroupDAO extends BaseJdbcDAO<Integer, Group> {

	/**
	 * 通过机构ID获取机构实体
	 * 
	 * @param iid
	 *            机构ID
	 * @return 机构实体
	 */
	public Group findByIid(int iid) {
		String sql = this.getSql() + " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		Group group = this.queryForEntity(query);
		return group;
	}

	/**
	 * 通过机构ID串获取机构
	 * 
	 * @param ids
	 *            机构ID串 如:1,2,3
	 * @return 机构实体集合
	 */
	public List<Group> findByIds(List<Integer> idsLsit) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT iid, name, spec, pid, codeid,")
				.append("(SELECT codeid FROM " + Tables.GROUP + " WHERE iid = a.pid) parcodeid,")
				.append("(SELECT name FROM " + Tables.GROUP + " WHERE iid = a.pid) pname,")
				.append("orderid FROM  " + Tables.GROUP + " a ")
				.append(" WHERE iid IN (:idsLsit) ").append(" ORDER by a.orderid ASC,a.iid ASC");

		Query query = createQuery(sql.toString());
		query.addParameter("idsLsit", idsLsit);
		List<Group> groupList = queryForEntities(query);
		return groupList;
	}

	/**
	 * 通过机构名称获取机构
	 * 
	 * @param name
	 *            机构名称
	 * @return 机构实体集合
	 */
	public List<Group> findByName(String name) {
		String sql = this.getSql() + " WHERE name=:name";
		Query query = createQuery(sql);
		query.addParameter("name", name);
		List<Group> groupList = queryForEntities(query);
		return groupList;
	}

	/**
	 * 通过机构标识获取机构ID
	 * 
	 * @param codeId
	 *            机构标识
	 * @return
	 */
	public int findIdByCodeId(String codeId) {
		String sql = "SELECT iid FROM " + Tables.GROUP + " WHERE codeid=:codeid ";
		Query query = createQuery(sql);
		query.addParameter("codeid", codeId);
		int iid = this.queryForInteger(query);
		return iid;
	}

	/**
	 * 通过机构ID、机构名称及父机构ID获得同名机构名称的个数
	 * 
	 * @param iid
	 *            机构ID
	 * @param name
	 *            机构名称
	 * @param pid
	 *            父机构ID
	 * @return 0 - 不存在<br/>
	 *         n - 存在n个
	 */
	public int findNumOfSameName(Integer iid, String name, Integer pid) {
		int num = 0;

		String sql = " SELECT COUNT(iid) FROM " + Tables.GROUP + " WHERE name=:name";
		if (NumberUtil.getInt(iid) > 0) {
			sql += " AND iid NOT IN(:iid)";
		}
		if (pid != null) {
			sql += " AND pid = :pid";
		} else {
			sql += " AND pid IS NULL";
		}
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		query.addParameter("name", name);
		query.addParameter("pid", pid);

		num = this.queryForInteger(query);
		return num;
	}

	/**
	 * 通过机构标识取得机构
	 * 
	 * @param codeId
	 *            机构标识
	 * @return 机构实体
	 */
	public Group findByCodeId(String codeId) {
		String sql = this.getSql() + " WHERE codeid=:codeid";
		Query query = createQuery(sql);
		query.addParameter("codeid", codeId);
		Group group = this.queryForEntity(query);
		return group;
	}

	/**
	 * 通过机构ID获得下属机构集合（树）
	 * 
	 * @param groupId
	 *            机构ID
	 * @return List<Map> 每个Map包含一个机构实体
	 */
	public List<Group> findChildGroupByIid(Integer iid) {
		String sql = "SELECT a.iid, a.name, a.codeid, a.pid, CASE WHEN EXISTS(SELECT 1 FROM "
				+ Tables.GROUP + " b WHERE b.pid = a.iid) THEN 1 ELSE 0 END isparent " + " FROM "
				+ Tables.GROUP + " a WHERE a.pid=:iid  ORDER BY a.iid ASC,a.orderid ASC";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		return super.queryForEntities(query);
	}

	/**
	 * 通用查询机构的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT a.iid, a.name, a.spec, a.pid, a.codeid," + " (SELECT name FROM "
				+ Tables.GROUP + " WHERE iid = a.pid) pname, orderid FROM " + Tables.GROUP + " a";
		return sql;
	}

	/**
	 * 通过机构ID获得机构名称
	 * 
	 * @param iid
	 *            机构ID
	 * @return 机构名称
	 */
	public String findNameByIid(int iid) {
		String name = "";
		String sql = "SELECT name FROM " + Tables.GROUP + " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);

		Group group = super.queryForEntity(query);
		if (group != null) {
			name = group.getName();
		}
		return name;
	}

	/**
	 * 通过机构名称和父机构ID获取机构
	 * 
	 * @param name
	 *            机构名称
	 * @param pid
	 *            父机构ID
	 * @return 机构实体
	 */
	public Group findByNameAndPId(String name, String pid) {
		String sql = this.getSql() + " WHERE pid =:pid AND name=:name";
		Query query = createQuery(sql);
		query.addParameter("name", name);
		query.addParameter("pid", pid);

		Group group = super.queryForEntity(query);
		return group;
	}

	/**
	 * 通过机构名称及父机构名称 获得 机构实体
	 * 
	 * @param name
	 *            机构名称
	 * @param pName
	 *            父机构名称
	 * @return 机构实体列表
	 */
	public List<Group> findByNameAndPName(String name, String pName) {
		String sql = "SELECT a.iid, a.name, a.spec, a.pid, a.codeid, b.name pname, a.orderid FROM "
				+ Tables.GROUP + " a, " + Tables.GROUP
				+ " b WHERE a.pid = b.iid  AND a.name=:name AND b.name=:pname";
		Query query = createQuery(sql);
		query.addParameter("name", name);
		query.addParameter("pname", pName);
		return super.queryForEntities(query);
	}

	/**
	 * 通过机构ID获得父机构ID
	 * 
	 * @param id
	 * @return
	 */
	public int findPidById(int id) {
		String sql = "SELECT pid FROM " + Tables.GROUP + " WHERE iid=:iid ";
		Query query = createQuery(sql);
		query.addParameter("iid", id);

		int pid = this.queryForInteger(query);
		return pid;
	}

	/**
	 * 更新机构标识(此时系统中机构标识为空 供导入及同步的时使用)
	 * 
	 * @param group
	 *            机构实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateCodeid(Integer iid, String codeId) {
		UpdateSql sql = new UpdateSql(Tables.GROUP);
		sql.addString("codeid", codeId);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return this.update(sql);
	}

	/**
	 * 获得下属机构数
	 * 
	 * @param ids
	 *            机构ID串 如:1,2,3
	 * @return
	 */
	public int findCountSubGroup(List<Integer> ids) {
		String sql = "SELECT COUNT(iid) FROM " + Tables.GROUP + " WHERE pid IN (:ids)";
		Query query = createQuery(sql);
		query.addParameter("ids", ids);
		int num = this.queryForInteger(query);

		return num;
	}

	/**
	 * 获得机构中用户个数
	 * 
	 * @param ids
	 *            机构ID集合
	 * @return
	 */
	public int findCountSubUser(List<Integer> ids) {
		String sql = "SELECT COUNT(iid) FROM " + Tables.USER + " WHERE groupid IN (:ids)";
		Query query = createQuery(sql);
		query.addParameter("ids", ids);
		int num = this.queryForInteger(query);
		return num;
	}

	/**
	 * 修改所属父机构ID
	 * 
	 * @param pid
	 *            父机构ID
	 * @param iid
	 *            机构ID
	 * @return
	 */
	public boolean updatePidById(Integer pid, int iid) {
		UpdateSql sql = new UpdateSql(Tables.GROUP);
		sql.addInt("pid", pid);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return this.update(sql);
	}

	/**
	 * 根据机构编号,判断机构是否存在
	 * 
	 * @param codeid
	 *            机构编号
	 * @return 已存在机构的个数
	 */
	public int findSameCodeid(String codeId) {
		return findNumOfSameGroupByCodeid(0, codeId);
	}

	/**
	 * 已存在 机构标识的个数
	 * 
	 * @param iid
	 *            当前操作的机构编号,新增则为空
	 * @param codeId
	 *            机构编号
	 * @return 已存在机构的个数
	 */
	public int findNumOfSameGroupByCodeid(int iid, String codeId) {
		int num = 0;

		String sql = "SELECT COUNT(iid) FROM " + Tables.GROUP + " WHERE codeid=:codeid";
		if (iid > 0) {
			sql += " AND iid<>:iid";
		}
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		query.addParameter("codeid", codeId);
		num = this.queryForInteger(query);
		return num;
	}

	/**
	 * 获得父机构下指定名称的机构id
	 * 
	 * @param name
	 *            机构名称
	 * @param pid
	 *            父机构ID
	 * @return
	 */
	public Integer findIdOfGroupByPid(String name, Integer pid) {
		String sql = "SELECT iid FROM " + Tables.GROUP + " WHERE name=:name";
		if (NumberUtil.getInt(pid) == 0) {
			sql += " AND (pid = :pidValue OR pid IS NULL)";
		} else {
			sql += " AND pid=:pid";

		}
		Query query = createQuery(sql);
		query.addParameter("name", name);
		query.addParameter("pid", pid);
		query.addParameter("pidValue", "");

		return this.queryForInteger(query);
	}

	/**
	 * 获得父机构下同名机构的个数
	 * 
	 * @param iid
	 *            机构ID
	 * @param name
	 *            机构名称
	 * @param pid
	 *            父机构ID
	 * @return
	 */
	public int findNumOfSameGroupByPid(Integer iid, String name, Integer pid) {
		int num = 0;

		String sql = "SELECT COUNT(iid) FROM " + Tables.GROUP + " WHERE name=:name";
		if (NumberUtil.getInt(iid) > 0) {
			sql += " AND iid<>:iid";
		}
		if (NumberUtil.getInt(pid) == 0) {
			sql += " AND (pid = :pidValue OR pid IS NULL)";
		} else {
			sql += " AND pid=:pid";
		}
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		query.addParameter("name", name);
		query.addParameter("pid", pid);
		query.addParameter("pidValue", "");

		num = this.queryForInteger(query);
		return num;
	}

	/**
	 * 找出机构的所有下属机构
	 * 
	 * @param pid
	 *            父机构ID
	 * @return 机构实体List
	 */
	public List<Group> findChildrenById(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT iid, name, spec, pid, codeid,")
				.append("(SELECT codeid FROM " + Tables.GROUP + " WHERE iid = a.pid) parcodeid,")
				.append("(SELECT name FROM " + Tables.GROUP + " WHERE iid = a.pid) pname,")
				.append("orderid FROM  " + Tables.GROUP + " a ");
		sql.append(" WHERE a.pid=:pid");

		sql.append(" ORDER by a.orderid ASC,a.iid ASC");
		Query query = createQuery(sql.toString());
		query.addParameter("pid", id);
		List<Group> groupList = super.queryForEntities(query);

		return groupList;
	}

	/**
	 * 找出父机构下所有下属机构ID
	 * 
	 * @param pid
	 *            父机构ID
	 * @return id的List集合
	 */
	public List<Map<String, Object>> findIdsByPid(Integer pid) {

		String sql = "SELECT iid FROM  " + Tables.GROUP + " WHERE pid = :pid";

		Query query = createQuery(sql);
		query.addParameter("pid", pid);

		List<Map<String, Object>> idsList = super.queryForList(query);

		return idsList;
	}

	/**
	 * 通过机构名称或名称首字母缩写查询机构
	 * 
	 * @param keyword
	 *            关键字
	 * @return
	 */
	public List<Group> findByNameOrPinYin(String keyword) {
		String sql = "SELECT iid, name, codeid FROM " + Tables.GROUP
				+ " WHERE name LIKE :name OR pinyin LIKE :pinYin ";
		Query query = createQuery(sql);
		query.addParameter("name", keyword, LikeType.LR);
		query.addParameter("pinYin", keyword.toUpperCase(), LikeType.LR);

		List<Group> groupList = super.queryForEntities(query);
		return groupList;
	}

	/**
	 * 获得角色相关的机构列表
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	public List<Group> findGroupsByRoleId(Integer roleId) {

		String sql = "SELECT a.iid, a.name, a.codeid FROM " + Tables.GROUP + " a, "
				+ Tables.ROLERELATION + " b WHERE a.iid = b.groupid "
				+ "AND b.roleid = :roleId ORDER BY a.orderid";

		Query query = createQuery(sql);
		query.addParameter("roleId", roleId);

		List<Group> groupList = super.queryForEntities(query);

		return groupList;
	}
}