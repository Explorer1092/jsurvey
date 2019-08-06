package com.hanweb.complat.dao;

import java.util.List;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.entity.RoleRight;

/**
 * 角色权限关系
 * 
 * @author ZhangC
 * 
 */
public class RoleRightDAO extends BaseJdbcDAO<Integer, RoleRight> {

	/**
	 * 通过角色ID串删除角色权限对应关系
	 * 
	 * @param roleIds
	 *            角色ID串 如:1,2,3
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean deleteByRoleIds(List<Integer> roleIds) {
		String sql = "DELETE FROM " + Tables.ROLERIGHT + " WHERE roleid IN (:roleIds)";
		Query query = createQuery(sql);
		query.addParameter("roleIds", roleIds);

		return this.delete(query);
	}

}
