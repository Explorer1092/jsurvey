package com.hanweb.complat.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.User;

/**
 * JIS HTTP接口
 * 
 * @author ZhangC
 * 
 */
public class LdapDAO extends BaseJdbcDAO<Integer, User> {

	/**
	 * 置 用户为失效状态
	 * 
	 * @param user
	 *            用户对象
	 * @return
	 */
	public boolean disableUser(User user) {
		UpdateSql updateSql = new UpdateSql(Tables.USER);
		updateSql.setWhere("iid=:iid");
		updateSql.addWhereParamInt("iid", user.getIid());
		updateSql.addWhereParamInt("enable", user.getEnable());

		return this.update(updateSql);
	}

	/**
	 * 通过登录名获得用户
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	public User findUserByLoginName(String loginName) {
		String sql = getEntitySql() + " WHERE loginname=:loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		return this.queryForEntity(query);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 *            用户对象
	 */
	public void updateUser(User user) {
		UpdateSql updateSql = new UpdateSql(Tables.USER);
		updateSql.setWhere("iid=:iid");
		updateSql.addWhereParamInt("iid", user.getIid());
		updateSql.addString("loginname", user.getLoginName());
		updateSql.addString("pwd", user.getPwd());
		updateSql.addString("name", user.getName());
		updateSql.addInt("sex", user.getSex());

		this.update(updateSql);
	}

}
