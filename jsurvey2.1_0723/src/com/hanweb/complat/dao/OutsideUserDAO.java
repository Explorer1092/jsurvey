package com.hanweb.complat.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.OutsideUser;

/**
 * 外网用户
 * 
 * @author ZhangC
 * 
 */
public class OutsideUserDAO extends BaseJdbcDAO<Integer, OutsideUser> {

	/**
	 * 通用用户id获得前台用户信息
	 * 
	 * @param iid
	 *            前台用户id
	 * @return
	 */
	public OutsideUser findByIid(int iid) {
		String sql = "SELECT iid, name, loginname, headship, enable, mobile, "
				+ "phone, contact, email, address FROM " + Tables.OUTSIDEUSER
				+ " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		OutsideUser outsideUser = this.queryForEntity(query);
		return outsideUser;
	}

	/**
	 * 获得外网用户
	 * 
	 * @param loginName
	 *            登陆名
	 * @return
	 */
	public OutsideUser findByLoginName(String loginName) {
		String sql = getEntitySql() + " WHERE loginname = :loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		return queryForEntity(query);
	}

	/**
	 * 更新前台用户密码
	 * 
	 * @param outsideUser
	 *            前台用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 */

	public boolean updatePwd(int iid, String pwd) {
		UpdateSql sql = new UpdateSql(Tables.OUTSIDEUSER);

		sql.setWhere("iid=" + iid);
		sql.addString("pwd", pwd);

		return super.update(sql);
	}

	/**
	 * 获得同名前台用户的个数
	 * 
	 * @param loginName
	 *            前台用户登录名
	 * @return 0 - 无同名<br/>
	 *         n - n个同名
	 */
	public int findIidByLoginName(Integer userId, String loginName) {
		String sql = "SELECT COUNT(iid) FROM " + Tables.OUTSIDEUSER + " WHERE loginname=:loginName";
		if (userId != null && userId > 0) {
			sql = sql + " AND iid<>:userId";
		}
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		query.addParameter("userId", userId);
		return this.queryForInteger(query);
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
		UpdateSql sql = new UpdateSql(Tables.OUTSIDEUSER);

		sql.addInt("enable", enable);
		sql.setWhere("iid = :iid");
		sql.addWhereParamInt("iid", iid);

		return this.update(sql);

	}

	/**
	 * 删除外网用户
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	public boolean deleteByLoginName(String loginName) {
		String sql = "DELETE FROM " + Tables.OUTSIDEUSER + " WHERE loginname = :loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		return delete(query);
	}

	/**
	 * 更新用户的有效性
	 * 
	 * @param loginName
	 *            登录名
	 * @param enable
	 *            是否有效<br/>
	 *            1 - 有效<br/>
	 *            0 - 无效
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean updateEnable(String loginName, int enable) {
		UpdateSql sql = new UpdateSql(Tables.OUTSIDEUSER);

		sql.addInt("enable", enable);
		sql.setWhere("loginname = :loginName");
		sql.addWhereParamString("loginName", loginName);

		return this.update(sql);

	}
}
