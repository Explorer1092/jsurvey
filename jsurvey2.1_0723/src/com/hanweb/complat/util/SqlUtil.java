package com.hanweb.complat.util;

/**
 * Sql语句工具
 * 
 * @author 陈健
 * 
 */
public class SqlUtil {
	/**
	 * 清理where条件头尾空格及AND、OR
	 * 
	 * @return
	 */
	public static String trimWhere(String sqlWhere) {
		sqlWhere = sqlWhere.trim();

		String temp = sqlWhere.toLowerCase();

		String and = "and";
		String or = "or";
		String blank = " ";

		if (temp.startsWith(and + blank)) {
			sqlWhere = sqlWhere.substring(4);
		} else if (temp.startsWith(or + blank)) {
			sqlWhere = sqlWhere.substring(3);
		}

		temp = sqlWhere.toLowerCase();

		if (temp.endsWith(blank + and)) {
			sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 4);
		} else if (temp.endsWith(blank + or)) {
			sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 3);
		}

		return sqlWhere;
	}

	public static String trimWhere(StringBuilder sqlWhere) {
		return trimWhere(sqlWhere.toString());
	}
}
