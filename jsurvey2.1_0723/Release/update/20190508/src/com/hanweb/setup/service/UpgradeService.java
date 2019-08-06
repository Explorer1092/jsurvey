package com.hanweb.setup.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.EntityManager;
import com.hanweb.common.basedao.InsertSql;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.SqlType;
import com.hanweb.common.basedao.UpdateSql;
import com.hanweb.common.util.StringUtil;
import com.hanweb.setup.dao.DataInitDAO;

/**
 * 升级service
 * 
 * @author 李杰
 *
 */
public class UpgradeService {

	@Autowired
	private DataInitDAO dataInitDAO;

	/**
	 * 按照实体增加表
	 * 
	 * @param entityClass
	 */
	public void addTable(Class<?> entityClass) {
		Map<SqlType, String> entitySql = EntityManager.getEntitySql(entityClass);
		String createTableSql = entitySql.get(SqlType.TABLE);
		String indexSqls = entitySql.get(SqlType.INDEX);
		if (StringUtil.isNotEmpty(createTableSql)) {
			dataInitDAO.executeForDDL(createTableSql);
		}
		if (StringUtil.isNotEmpty(indexSqls)) {
			String[] indexes = StringUtil.split(indexSqls, ";");
			for (String index : indexes) {
				dataInitDAO.executeForDDL(index);
			}
		}
		String commentSql = entitySql.get(SqlType.ALTER);
		if (StringUtil.isNotEmpty(commentSql)) {
			String[] sqls = StringUtil.split(commentSql, ";");
			for (String sql : sqls) {
				dataInitDAO.executeForDDL(sql);
			}
		}
		if (BaseInfo.getDbType() == BaseInfo.ORACLE) {
			String seq = entitySql.get(SqlType.SEQ);
			String trigger = entitySql.get(SqlType.TRIGGER);
			String enableSeq = entitySql.get(SqlType.ENABLE_SEQ);
			if (!StringUtil.isEmpty(seq) && !StringUtil.isEmpty(trigger) && !StringUtil.isEmpty(enableSeq)) {
				dataInitDAO.executeForDDL(seq);
				dataInitDAO.executeForDDL(trigger);
				dataInitDAO.executeForDDL(enableSeq);
			}
		}
	}

	/**
	 * 自定义执行方式
	 * 
	 * @param execute
	 * @return
	 */
	public Object execute(IUpgradeExecute execute) {
		Object o = null;
		if (execute != null) {
			execute.execute(this.dataInitDAO);
		}
		return o;
	}

	/**
	 * 执行sql
	 * 
	 * @param queries
	 */
	public void execSql(Query... queries) {
		if (queries != null) {
			for (Query query : queries) {
				dataInitDAO.execute(query);
			}
		}
	}

	/**
	 * 执行sql
	 * 
	 * @param sqls
	 */
	public void execDDLSql(String... sqls) {
		if (sqls != null) {
			for (String sql : sqls) {
				dataInitDAO.executeForDDL(sql);
			}
		}
	}

	/**
	 * 执行更新
	 * 
	 * @param updateSqls
	 */
	public void execUpdate(UpdateSql... updateSqls) {
		if (updateSqls != null) {
			for (UpdateSql updateSql : updateSqls) {
				dataInitDAO.update(updateSql);
			}
		}
	}

	/**
	 * 执行新增
	 * 
	 * @param insertSqls
	 */
	public void execInsert(InsertSql... insertSqls) {
		if (insertSqls != null) {
			for (InsertSql insertSql : insertSqls) {
				dataInitDAO.insert(insertSql);
			}
		}
	}

	/**
	 * 执行查询
	 * 
	 * @param query
	 * @return
	 */
	public List<Map<String, Object>> execSelectItems(Query query) {
		return dataInitDAO.queryForList(query);
	}

	/**
	 * 执行查询
	 * 
	 * @param query
	 * @return
	 */
	public Map<String, Object> execSelectItem(Query query) {
		return dataInitDAO.queryForMap(query);
	}

	/**
	 * 执行查询
	 * 
	 * @param query
	 * @return
	 */
	public Integer execSelectInteger(Query query) {
		return dataInitDAO.queryForInteger(query);
	}

	/**
	 * 执行查询
	 * 
	 * @param query
	 * @return
	 */
	public Long execSelectLong(Query query) {
		return dataInitDAO.queryForLong(query);
	}

	/**
	 * 创建query
	 * 
	 * @param sql
	 * @return
	 */
	public Query createQuery(String sql) {
		return dataInitDAO.createQuery(sql);
	}

	public DataInitDAO getDataInitDAO() {
		return dataInitDAO;
	}
}
