package com.hanweb.dczj.dao;

import java.util.List;
import java.util.Map;

import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.DCZJ_WebSite;

public class WebSiteDAO extends BaseJdbcDAO<Integer, DCZJ_WebSite> {

	/**
	 * 通用查询机构的sql语句
	 * 
	 * @return
	 */
	private String getSql() {
		String sql = "SELECT a.iid, a.name, a.spec, a.pid, a.codeid," + " (SELECT name FROM "
				+ Tables.WEBSITE + " WHERE iid = a.pid) pname, orderid FROM " + Tables.WEBSITE + " a";
		return sql;
	}
	
	/**
	 * 通过网站ID获得下属网站集合（树）
	 * 
	 * @param webId
	 *            机构ID
	 * @return List<Map> 每个Map包含一个机构实体
	 */
	public List<DCZJ_WebSite> findChildWebByIid(Integer rangeId) {
		String sql = "SELECT a.iid, a.name, a.codeid, a.pid, CASE WHEN EXISTS(SELECT 1 FROM "
				+ Tables.WEBSITE
				+ " b WHERE b.pid = a.iid) THEN 1 ELSE 0 END isparent "
				+ " FROM "
				+ Tables.WEBSITE
				+ " a WHERE a.pid=:iid  ORDER BY a.iid ASC,a.orderid ASC";
		Query query = createQuery(sql);
		query.addParameter("iid", rangeId);
		return super.queryForEntities(query);
	}

	/**
	 * 通过网站ID获取网站实体
	 * 
	 * @param iid
	 *            网站ID
	 * @return 网站实体
	 */
	public DCZJ_WebSite findByIid(int iid) {
		String sql = this.getSql() + " WHERE iid=:iid";
		Query query = createQuery(sql);
		query.addParameter("iid", iid);
		DCZJ_WebSite webSite = this.queryForEntity(query);
		return webSite;
	}

	/**
	 * 通过网站标识取得网站
	 * 
	 * @param codeId
	 *            网站标识
	 * @return 网站实体
	 */
	public DCZJ_WebSite findByCodeId(String codeId) {
		String sql = this.getSql() + " WHERE codeid=:codeid";
		Query query = createQuery(sql);
		query.addParameter("codeid", codeId);
		DCZJ_WebSite webSite = this.queryForEntity(query);
		return webSite;
	}

	/**
	 * 通过网站ID、网站名称及父网站ID获得同名网站名称的个数
	 * 
	 * @param iid
	 *            网站ID
	 * @param name
	 *            网站名称
	 * @param pid
	 *            父网站ID
	 * @return 0 - 不存在<br/>
	 *         n - 存在n个
	 */
	public int findNumOfSameName(int iid, String name, Integer pid) {
		int num = 0;

		String sql = " SELECT COUNT(iid) FROM " + Tables.WEBSITE + " WHERE name=:name";
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
	 * 找出父网站下所有下属网站ID
	 * 
	 * @param pid
	 *            父网站ID
	 * @return id的List集合
	 */
	public List<Map<String, Object>> findIdsByPid(Integer pid) {
		String sql = "SELECT iid FROM  " + Tables.WEBSITE + " WHERE pid = :pid";

		Query query = createQuery(sql);
		query.addParameter("pid", pid);

		List<Map<String, Object>> idsList = super.queryForList(query);

		return idsList;
	}

	/**
	 * 获得下属网站数
	 * 
	 * @param ids
	 *            网站ID串 如:1,2,3
	 * @return
	 */
	public int findCountSubGroup(List<Integer> ids) {
		String sql = "SELECT COUNT(iid) FROM " + Tables.WEBSITE + " WHERE pid IN (:ids)";
		Query query = createQuery(sql);
		query.addParameter("ids", ids);
		int num = this.queryForInteger(query);
		return num;
	}

	/**
	 * 找出网站的所有下属网站
	 * 
	 * @param pid
	 *            父网站ID
	 * @return 网站实体List
	 */
	public List<DCZJ_WebSite> findChildrenById(Integer id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT iid, name, spec, pid, codeid,")
				.append("(SELECT codeid FROM " + Tables.WEBSITE + " WHERE iid = a.pid) parcodeid,")
				.append("(SELECT name FROM " + Tables.WEBSITE + " WHERE iid = a.pid) pname,")
				.append("orderid FROM  " + Tables.WEBSITE + " a ");
		sql.append(" WHERE a.pid=:pid");

		sql.append(" ORDER by a.orderid ASC,a.iid ASC");
		Query query = createQuery(sql.toString());
		query.addParameter("pid", id);
		List<DCZJ_WebSite> webList = super.queryForEntities(query);

		return webList;
	}

	/**
	 * 通过webid找到网站名称
	 * @param webid
	 * @return
	 */
	public String findWebNameByIid(Integer iid) {
		DCZJ_WebSite webSite = this.findByIid(iid);
		String WebName = "";
		if(webSite != null){
			WebName = webSite.getName();
		}
		return WebName;
	}

	/**
	 * 查找所有网站集合
	 * @return
	 */
	public List<DCZJ_WebSite> findAllWebId() {
		String sql = this.getSql() + " ORDER BY iid ASC,orderid ASC";
		Query query = createQuery(sql);
		return this.queryForEntities(query);
	}

	

}
