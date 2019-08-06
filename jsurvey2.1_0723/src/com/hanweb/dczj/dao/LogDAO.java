package com.hanweb.dczj.dao;

import java.util.List;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.BaseJdbcDAO;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.util.Properties;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.dczj.entity.Dczj_Log;

public class LogDAO extends BaseJdbcDAO<Integer, Dczj_Log>{

	public boolean deleteByiids(List<Integer> integerList) {
		String strSql = "DELETE FROM "+ Tables.LOG +" WHERE i_id IN (:strIdsList)";
		Query query = createQuery(strSql);
		query.addParameter("strIdsList", integerList);
		boolean bl = this.delete(query);
		return bl;	
	}
	
	public boolean checkTableExist(String tableName){
		boolean result = false;
		Properties prop = getSetupProp();
		String dbname = prop.getString("dbname");
    	String strSql = "";
    	switch(BaseInfo.getDbType()){
    	case 1:
    		strSql = "select * from all_tables where table_name=:tableName AND OWNER = :owner";
    		break;
    	case 2:
    		//strSql = "select * from sysobjects where id = object_id(:sqlserver)";
    		strSql = "select * from "+tableName+".INFORMATION_SCHEMA.TABLES where TABLE_NAME=:tableName";
    		break;
    	case 5:
    		strSql = "select * from information_schema.tables where table_schema=:dbname and table_name =:tableName";
    		break;
//    	case 7:
//    		strSql = "select * from all_tables where table_name=:tableName AND OWNER = :owner";
//    		break;
//    	case 6:
//    		strSql = "select 1 from "+jcmsdbname+".public.jcms_publishstate";
//    		break;
    	}	    	
    	if(strSql.trim().length() > 0){
    		Query query = createQuery(strSql);
    		if(BaseInfo.getDbType()==1||BaseInfo.getDbType()==7){
    			query.addParameter("tableName", tableName.toUpperCase());
	    		query.addParameter("owner", dbname.toUpperCase());
    		}else{
	    		query.addParameter("tableName", tableName);
	    		query.addParameter("owner", dbname);
    		}
    		query.addParameter("dbname", dbname);
//    		query.addParameter("kingbase", jcmsdbname+".public.jcms_publishstate");
    		String[][] tableData = this.queryForArrays(query);
    		//System.out.println(strSql);
    		if(tableData != null && tableData.length > 0){
    			result = true;
    		}
    	}
    	return result;
	}
	
	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}
}
