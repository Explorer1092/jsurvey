<%@page import="com.hanweb.dczj.service.DataMigrationService"%>
<%@page import="com.hanweb.common.util.SpringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    out.println("*************开始重新统计数据**************</br>");
    String Webid = ""; 
    DataMigrationService dataMigrationService = SpringUtil.getBean(DataMigrationService.class);
    boolean bl = dataMigrationService.refreshData(Webid);
    if(bl){
    	out.println("*************success**************");
    	System.out.println("成功");
    }else{
    	out.println("*************false**************");
    	System.out.println("失败");
    }
%>