package com.hanweb.setup.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.EntityManager;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.basedao.SqlType;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.jdbc.OracleSeq;
import com.hanweb.setup.dao.DataInitDAO;

/**
 * 数据初始化业务
 * 
 * @author 李杰
 *
 */
public class DataInitService {

	// private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DataInitDAO dataInitDAO;

	public void addData() {
		addEntityTable();
		String queryString = null;
		switch (BaseInfo.getDbType()) {
		case 1:
			queryString = FileUtil.readFileToString(new File(BaseInfo.getRealPath() + "/WEB-INF/sql/oracle/table.sql"),
					"utf-8");
			executeSqlForTable(queryString);
			queryString = FileUtil.readFileToString(
					new File(BaseInfo.getRealPath() + "/WEB-INF/sql/oracle/seq_trigger.sql"), "utf-8");
			executeSqlForSeq(queryString, false);
			break;
		case 2:
			queryString = FileUtil.readFileToString(new File(BaseInfo.getRealPath() + "/WEB-INF/sql/mssql/table.sql"),
					"utf-8");
			executeSqlForTable(queryString);
			break;
		case 5:
			queryString = FileUtil.readFileToString(new File(BaseInfo.getRealPath() + "/WEB-INF/sql/mysql/table.sql"),
					"utf-8");
			executeSqlForTable(queryString);
			break;
		default:
			break;
		}
	}

	private void addEntityTable() {
		Collection<Map<SqlType, String>> collection = EntityManager.getEntitiesSql();
		String createTableSql = null;
		String indexSqls = null;
		for (Map<SqlType, String> map : collection) {
			createTableSql = map.get(SqlType.TABLE);
			indexSqls = map.get(SqlType.INDEX);
			if (StringUtil.isNotEmpty(createTableSql)) {
				dataInitDAO.executeForDDL(createTableSql);
			}
			if (StringUtil.isNotEmpty(indexSqls)) {
				String[] indexes = StringUtil.split(indexSqls, ";");
				for (String index : indexes) {
					dataInitDAO.executeForDDL(index);
				}
			}
			String commentSql = map.get(SqlType.ALTER);
			if (StringUtil.isNotEmpty(commentSql)) {
				String[] sqls = StringUtil.split(commentSql, ";");
				for (String sql : sqls) {
					dataInitDAO.executeForDDL(sql);
				}
			}
		}
		if (BaseInfo.getDbType() == BaseInfo.ORACLE) {
			String seq = null;
			String trigger = null;
			String enableSeq = null;
			for (Map<SqlType, String> map : collection) {
				seq = map.get(SqlType.SEQ);
				trigger = map.get(SqlType.TRIGGER);
				enableSeq = map.get(SqlType.ENABLE_SEQ);
				if (!StringUtil.isEmpty(seq) && !StringUtil.isEmpty(trigger) && !StringUtil.isEmpty(enableSeq)) {
					dataInitDAO.executeForDDL(seq);
					dataInitDAO.executeForDDL(trigger);
					dataInitDAO.executeForDDL(enableSeq);
				}
			}
		}
	}

	/**
	 * 
	 * replaceSpescailStr:(去除sql中的特殊字符).
	 * 
	 * @param queryString
	 *            sql
	 * @return String sql .
	 */
	private String replaceSpescailStr(String queryString) {
		if (StringUtils.isBlank(queryString)) {
			return "";
		}
		queryString = queryString.replaceAll("\n", "");
		queryString = queryString.replaceAll("\r", "");
		return queryString;
	}

	/**
	 * 
	 * executeSqlForTable:(组织建表语句).
	 * 
	 * @param queryString
	 *            sql .
	 */
	private void executeSqlForTable(String queryString) {
		if (StringUtils.isBlank(queryString)) {
			return;
		}
		String[] strArr = null;
		queryString = replaceSpescailStr(queryString);
		strArr = queryString.split(";");
		for (int i = 0; i < strArr.length; i++) {
			dataInitDAO.executeForDDL(strArr[i]);
		}
	}

	/**
	 * executeSqlForSeq:(组织触发器语句).
	 * 
	 * @param queryString
	 *            sql.
	 * @param isDrop
	 *            是否删除 .
	 */
	private void executeSqlForSeq(String queryString, boolean isDrop) {
		if (StringUtils.isBlank(queryString)) {
			return;
		}
		queryString = replaceSpescailStr(queryString);
		String[] seqArr = queryString.split(";");
		if (isDrop) {
			for (int i = 0; i < seqArr.length; i++) {
				dataInitDAO.executeForDDL(OracleSeq.disableSeq(seqArr[i]));
			}
			for (int i = 0; i < seqArr.length; i++) {
				dataInitDAO.executeForDDL(OracleSeq.removeSeq(seqArr[i]));
			}
		} else {
			for (int i = 0; i < seqArr.length; i++) {
				dataInitDAO.executeForDDL(OracleSeq.createSeq(seqArr[i]));
				dataInitDAO.executeForDDL(OracleSeq.createTrigger(seqArr[i]));
			}
			for (int i = 0; i < seqArr.length; i++) {
				dataInitDAO.executeForDDL(OracleSeq.enableSeq(seqArr[i]));
			}
		}
	}

	public void initDateBase() {
		List<String> sqls = FileUtil.readLines(new File(BaseInfo.getRealPath() + "/WEB-INF/sql/init/init.sql"),
				"utf-8");

		// boolean isWin = BaseInfo.isWin();

		if (sqls != null) {
			for (String sql : sqls) {
				if (StringUtils.isBlank(sql)) {
					continue;
				}
				if (sql.endsWith(";")) {
					sql = sql.substring(0, sql.length() - 1);
				}
				// if (BaseInfo.getDbType() == BaseInfo.KINGBASEES && !isWin) {
				// sql = CharsetUtil.toGBK(sql);
				// }
				Query query = dataInitDAO.createQuery(sql);
				dataInitDAO.execute(query);
			}
		}
	}
}
