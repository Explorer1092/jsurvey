package com.hanweb.complat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.User;

/**
 * 用户
 * 
 * @author ZhangC
 * 
 */
public class UserDAO extends BaseJdbcDAO<Integer, User> {

	/**
	 * 通用查询语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT iid, uuid, name, loginname, pwd, groupid, dynamiccodekey, (SELECT name FROM "
				+ Tables.GROUP + " b WHERE groupid = b.iid) groupname, headship, phone, mobile,"
				+ " contact, email, address, enable," + " address, createtime, pinyin, commonregion FROM " + Tables.USER
				+ " ";
		return sql;
	}

	/**
	 * 通过用户ID获得用户实体
	 * 
	 * @param iid
	 *            用户ID
	 * @return
	 */
	public User findByIid(int iid) {
		String sql = this.getSql() + "WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		User user = this.queryForEntity(query);
		return user;
	}

	/**
	 * 获得用户所在机构ID
	 * 
	 * @param iid
	 *            用户ID
	 * @return
	 */
	public Integer findGroupIdByIid(int iid) {
		String sql = "SELECT groupid FROM " + Tables.USER + " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);

		Integer groupId = this.queryForInteger(query);

		return groupId;
	}

	/**
	 * 获得指定用户ID串的用户集合
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3
	 * @return
	 */
	public List<User> findByIds(List<Integer> idsList) {

		String sql = "SELECT a.iid, a.name,a.loginname, a.pwd, a.groupid, b.name AS groupname,"
				+ " a.headship, a.mobile, a.phone, a.contact, a.email, a.address," + " b.codeid, b.name,"
				+ " (SELECT name FROM " + Tables.GROUP + " WHERE iid = b.pid) pargroupname " + " FROM " + Tables.USER
				+ " a, " + Tables.GROUP + " b " + " WHERE a.groupid = b.iid AND a.iid IN (:idsList) "
				+ " ORDER BY a.iid DESC";

		Query query = createQuery(sql);
		query.addParameter("idsList", idsList);
		List<User> userList = super.queryForEntities(query);
		return userList;
	}

	/**
	 * 获得指定机构ID串下的所有用户集合
	 * 
	 * @param groupidList
	 *            机构ID串集合
	 * @return
	 */
	public List<User> findByGroupIds(List<Integer> groupidList) {

		String sql = "SELECT a.iid, a.name,a.loginname, a.pwd, a.groupid, b.name AS groupname,"
				+ " a.headship, a.mobile, a.phone, a.contact, a.email, a.address," + " b.codeid, b.name,"
				+ " (SELECT name FROM " + Tables.GROUP + " WHERE iid = b.pid) pargroupname " + " FROM " + Tables.USER
				+ " a, " + Tables.GROUP + " b " + " WHERE a.groupid = b.iid "
				+ " AND a.groupid IN (:groupidList) ORDER BY a.iid DESC ";

		Query query = createQuery(sql);
		query.addParameter("groupidList", groupidList);
		List<User> userList = super.queryForEntities(query);

		return userList;
	}

	/**
	 * 获得所有用户实体（排除超级管理员）
	 * 
	 * @return
	 */
	public List<User> findAllUsers() {
		String sql = "SELECT a.iid, a.name,a.loginname, a.pwd, a.groupid, b.name AS groupname,"
				+ " a.headship, a.mobile, a.phone, a.contact, a.email, a.address," + " b.codeid, b.name,"
				+ " (SELECT name FROM " + Tables.GROUP + " WHERE iid = b.pid) pargroupname " + " FROM " + Tables.USER
				+ " a, " + Tables.GROUP + " b " + " WHERE a.groupid = b.iid AND a.uuid <> :uuid ORDER BY a.iid DESC";
		Query query = createQuery(sql);
		query.addParameter("uuid", "bdf699cf97284db0a9524332f0648f22");
		List<User> userList = super.queryForEntities(query);

		return userList;
	}

	/**
	 * 获得机构ID下第n-m条用户集合
	 * 
	 * @param groupId
	 *            机构ID
	 * @param beginNum
	 *            起始值
	 * @param endNum
	 *            结束值
	 * @return
	 */
	public List<User> findListByRowNum(String groupId, int beginNum, int endNum) {
		String sql = "SELECT iid, name, loginname FROM " + Tables.USER + " WHERE groupid=:groupId' ORDER BY iid DESC";
		Query query = createQuery(sql);
		query.setStart(beginNum);
		query.setEnd(endNum);
		query.addParameter("groupId", groupId);
		List<User> userList = super.queryForEntities(query);

		return userList;
	}

	/**
	 * 通过登录名获得用户ID
	 * 
	 * @param userId
	 *            用户id
	 * @param loginName
	 *            用户登录名
	 * @return
	 */
	public int findIidByLoginName(Integer userId, String loginName) {
		String sql = "SELECT iid FROM " + Tables.USER + " WHERE loginname=:loginName";
		if (userId != null && userId > 0) {
			sql = sql + " AND iid<>:userId";
		}
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		query.addParameter("userId", userId);
		Integer iid = this.queryForInteger(query);

		return NumberUtil.getInt(iid);
	}

	/**
	 * 获得机构下所有用户ID的集合
	 * 
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public List<Integer> findIdListByGroupId(String groupId) {
		List<Integer> userIdList = new ArrayList<Integer>();
		String sql = "SELECT iid FROM " + Tables.USER + " WHERE groupid =:groupId";
		Query query = createQuery(sql);
		query.addParameter("groupId", groupId);
		List<Map<String, Object>> resultList = this.queryForList(query);

		for (Map<String, Object> result : resultList) {
			userIdList.add(NumberUtil.getInt(result.get("iid")));
		}

		return userIdList;
	}

	/**
	 * 通过登录名获得用户实体
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return
	 */
	public User findByLoginName(String loginName) {
		String sql = this.getSql() + " WHERE loginname=:loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		User user = this.queryForEntity(query);
		return user;
	}

	/**
	 * 通过登录名获得用户密码
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return
	 */
	public String findPassword(String loginName) {
		String pwd = "";
		String sql = "SELECT pwd FROM " + Tables.USER + " WHERE loginname=:loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		Map<String, Object> queryMap = super.queryForMap(query);
		if (queryMap != null) {
			pwd = StringUtil.getString(queryMap.get("pwd"));
		}

		return pwd;
	}

	/**
	 * 获得机构下用户数量
	 * 
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public int findNumByGroupId(Integer groupId) {
		int num = 0;
		String sql = "SELECT COUNT(iid) FROM " + Tables.USER + " WHERE groupid IN (:groupId)";
		Query query = createQuery(sql);
		query.addParameter("groupId", groupId);
		num = this.queryForInteger(query);

		return num;
	}

	/**
	 * 删除用户
	 * 
	 * @param ids
	 *            用户ID集
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	@Override
	public boolean deleteByIds(List<Integer> ids) {
		String sql = "DELETE FROM " + Tables.USER + " WHERE iid IN (:ids)";
		Query query = createQuery(sql);
		query.addParameter("ids", ids);
		int row = super.execute(query);

		return row > 0 ? true : false;
	}

	/**
	 * 修改个人密码
	 * 
	 * @param iid
	 *            用户ID
	 * @param pwd
	 *            用户密码
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updatePassword(Integer iid, String pwd) {
		UpdateSql sql = new UpdateSql(Tables.USER);

		sql.addString("pwd", pwd);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return super.update(sql);

	}

	/**
	 * 修改个人密码
	 * 
	 * @param iid
	 *            用户ID
	 * @param loginId
	 *            用户名
	 * @param pwd
	 *            用户密码
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateLoginIdAndPassword(Integer iid, String loginId, String pwd) {
		UpdateSql sql = new UpdateSql(Tables.USER);

		sql.addString("loginname", loginId);
		sql.addString("pwd", pwd);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return super.update(sql);

	}

	/**
	 * 更新用户的有效性
	 * 
	 * @param iid
	 *            用户ID
	 * @param enable
	 *            是否有效<br/>
	 *            1 - 有效<br/>
	 *            0 - 无效
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateEnable(int iid, int enable) {
		UpdateSql sql = new UpdateSql(Tables.USER);

		sql.addInt("enable", enable);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return super.update(sql);
	}

	/**
	 * 更新用户的有效性
	 * 
	 * @param iid
	 *            用户ID
	 * @param enable
	 *            是否有效<br/>
	 *            1 - 有效<br/>
	 *            0 - 无效
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateEnable(String loginId, int enable) {
		UpdateSql sql = new UpdateSql(Tables.USER);

		sql.addInt("enable", enable);
		sql.setWhere("loginname = :loginname");
		sql.addWhereParamString("loginname", loginId);

		return super.update(sql);
	}

	/**
	 * 按姓名、或姓名首字母缩写查询
	 * 
	 * @param keyword
	 *            关键字
	 * @return
	 */
	public List<User> findByNameOrPinYin(String keyword) {
		String sql = "SELECT iid, name, loginname FROM " + Tables.USER
				+ " WHERE name LIKE :name OR pinyin LIKE :pinYin ";
		Query query = createQuery(sql);
		query.addParameter("name", keyword, LikeType.LR);
		query.addParameter("pinYin", keyword.toUpperCase(), LikeType.LR);

		List<User> userList = super.queryForEntities(query);
		return userList;
	}

	/**
	 * 获得角色相关的用户列表
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	public List<User> findUsersByRoleId(Integer roleId) {

		String sql = "SELECT a.iid, a.name, a.loginname FROM " + Tables.USER + " a, " + Tables.ROLERELATION
				+ " b WHERE a.iid = b.userid " + "AND b.roleid = :roleId ORDER BY a.iid DESC";
		Query query = createQuery(sql);
		query.addParameter("roleId", roleId);

		List<User> userList = super.queryForEntities(query);

		return userList;
	}

	/**
	 * 获得所有有email的用户id
	 * 
	 * @param ids
	 * @return
	 */
	public List<Integer> findUserIdHasEmail(List<Integer> ids) {
		String sql = "SELECT iid FROM " + Tables.USER + " WHERE email IS NOT NULL AND email <> :email";
		if (CollectionUtils.isNotEmpty(ids)) {
			sql += " AND iid IN (:ids)";
		}
		Query query = createQuery(sql);
		query.addParameter("email", "");
		query.addParameter("ids", ids);
		return queryForObjects(query, Integer.class);
	}

	/**
	 * 更新用户的动态码密钥
	 * 
	 * @param iid
	 * @param code
	 * @return
	 */
	public boolean updateDynamicCode(Integer iid, String code) {
		UpdateSql sql = new UpdateSql(Tables.USER);

		sql.addString("dynamiccodekey", code);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return super.update(sql);
	}
	
}
