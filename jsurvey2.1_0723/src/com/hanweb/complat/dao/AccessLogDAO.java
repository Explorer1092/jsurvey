package com.hanweb.complat.dao;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.AccessLog;

/**
 * 登录日志
 * 
 * @author 陈健
 * 
 */
public class AccessLogDAO extends BaseJdbcDAO<Integer, AccessLog> {

	/**
	 * 查询登录日志数量
	 * 
	 * @param loginName
	 * @return
	 */
	public int findCount(String loginName) {
		String sql = "SELECT COUNT(*) FROM " + Tables.ACCESSLOG + " WHERE loginname = :loginName";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		int count = this.queryForInteger(query);
		return count;
	}

	/**
	 * 删除id最小的记录
	 * 
	 * @param loginName
	 * @param deleteNumber
	 */
	public void deleteByMinId(String loginName) {
		String sql = "DELETE FROM " + Tables.ACCESSLOG
				+ " WHERE iid = (SELECT c.minid FROM (SELECT MIN(b.iid) minid FROM "
				+ Tables.ACCESSLOG + " b WHERE b.loginname = :loginName) c)";
		Query query = createQuery(sql);
		query.addParameter("loginName", loginName);
		this.delete(query);
	}
}
