package com.hanweb.complat.dao;

import java.util.Date;
import java.util.List;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.complat.entity.TempFile;

/**
 * 多文件上传管理器
 * @author 李杰
 */
public class TempFileDAO extends BaseJdbcDAO<String, TempFile> {

	/**
	 * 找出小于指定日期的文件
	 * 
	 * @param date
	 *            上传日期
	 * @return
	 */
	public List<TempFile> findLessthanDay(Date date) {
		String sql = getEntitySql() + "WHERE uploaddate < :uploadDate";
		Query query = createQuery(sql);
		query.addParameter("uploadDate", date);
		return this.queryForEntities(query);
	}
}
