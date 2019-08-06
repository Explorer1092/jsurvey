package com.hanweb.complat.dao;

import java.util.List;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.complat.entity.BanList;

/**
 * 封停
 * 
 * @author 李杰
 * 
 */
public class BanListDAO extends BaseJdbcDAO<Integer, BanList> {

	/**
	 * 通过用户登录名和IP获得封停管理列表
	 * 
	 * @param loginName
	 *            用户登录名
	 * @param ipAddr
	 *            用户登录IP
	 * @return
	 */
	public BanList findByLoginNameAndIp(String loginName, String ipAddr) {
		Query query = createQuery(getEntitySql()
				+ "WHERE ipaddr = :ipAddr AND loginName = :loginName");
		query.addParameter("ipAddr", ipAddr);
		query.addParameter("loginName", loginName);
		List<BanList> banLists = queryForEntities(query);
		if (banLists != null && banLists.size() > 0) {
			return banLists.get(0);
		}
		return null;
	}
}
