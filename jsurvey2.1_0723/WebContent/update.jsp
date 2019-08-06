<%@page import="org.apache.commons.collections.CollectionUtils"%>
<%@page import="com.hanweb.common.util.FileUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="com.hanweb.common.datasource.DataSourceConfig"%>
<%@page import="com.hanweb.common.util.StringUtil"%>
<%@page import="com.hanweb.common.util.Properties"%>
<%@page import="com.hanweb.common.BaseInfo"%>
<%@page import="com.hanweb.common.annotation.ColumnType"%>
<%@page import="com.hanweb.common.basedao.Query"%>
<%@page import="com.hanweb.common.util.SpringUtil"%>
<%@page import="com.hanweb.setup.service.UpgradeService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	
	// 更新setup数据库用户与密码，加密
	String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
	Properties properties = new Properties(dbProp);
	String dbUser = properties.getString("username");
	String dbPassword = properties.getString("password");
	if(StringUtil.isNotEmpty(dbUser) && DataSourceConfig.decode(dbUser) == null){
		properties.setProperty("username", DataSourceConfig.encode(dbUser));
	}
	if(StringUtil.isNotEmpty(dbPassword) && DataSourceConfig.decode(dbPassword) == null){
		properties.setProperty("password", DataSourceConfig.encode(dbPassword));
	}
	properties.save();
	
	// 更新extend_db数据库用户与密码，加密
	File dir = new File(BaseInfo.getRealPath() + "/WEB-INF/config/extend_db/");
		if (dir.exists()) {
			List<File> files = FileUtil.filterFiles(dir, new String[] { "properties" }, true);
			if (CollectionUtils.isNotEmpty(files)) {
				for (File file : files) {
					properties = new Properties(file.getPath());
					dbUser = properties.getString("username");
					dbPassword = properties.getString("password");
					if(StringUtil.isNotEmpty(dbUser) && DataSourceConfig.decode(dbUser) == null){
						properties.setProperty("username", DataSourceConfig.encode(dbUser));
					}
					if(StringUtil.isNotEmpty(dbPassword) && DataSourceConfig.decode(dbPassword) == null){
						properties.setProperty("password", DataSourceConfig.encode(dbPassword));
					}
					properties.save();
				}
			}
		}
		out.print("执行完成");
%>