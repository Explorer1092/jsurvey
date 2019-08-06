<%@page import="com.hanweb.dczj.dao.DisplayConfigDAO"%>
<%@page import="com.hanweb.dczj.dao.TotalRecoDAO"%>
<%@page import="com.hanweb.dczj.dao.TitleInfoDAO"%>
<%@page import="com.hanweb.dczj.dao.ThanksSettingDAO"%>
<%@page import="com.hanweb.dczj.dao.QuesInfoDAO"%>
<%@page import="com.hanweb.dczj.dao.CheckedBoxRecoDAO"%>
<%@page import="com.hanweb.dczj.dao.AnswInfoDAO"%>
<%@page import="com.hanweb.common.basedao.BaseDAO"%>
<%@page import="com.hanweb.common.BaseInfo"%>
<%@page import="com.hanweb.common.annotation.ColumnType"%>
<%@page import="com.hanweb.common.basedao.Query"%>
<%@page import="com.hanweb.common.util.SpringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	boolean success = false;
	try{
		AnswInfoDAO answInfoDAO = SpringUtil.getBean(AnswInfoDAO.class);
		success = answInfoDAO.addColumn("jsurvey_answinfo", "isright", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_answinfo 表 isright 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_answinfo 表 isright 字段 新增【失败】" + "<br>");
		}
	}
	
	
	try{
		CheckedBoxRecoDAO checkedBoxRecoDAO = SpringUtil.getBean(CheckedBoxRecoDAO.class);
		success = checkedBoxRecoDAO.addColumn("jsurvey_checkedboxreco", "isright", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_checkedboxreco 表 isright 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_checkedboxreco 表 isright 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		QuesInfoDAO quesInfoDAO = SpringUtil.getBean(QuesInfoDAO.class);
		success = quesInfoDAO.addColumn("jsurvey_quesinfo", "quesscore", ColumnType.FLOAT,"2","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_quesinfo 表 quesscore 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_quesinfo 表 quesscore 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		ThanksSettingDAO thanksSettingDAO = SpringUtil.getBean(ThanksSettingDAO.class);
		success = thanksSettingDAO.addColumn("jsurvey_thankssetting", "isdetail", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_thankssetting 表 isdetail 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_thankssetting 表 isdetail 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		ThanksSettingDAO thanksSettingDAO = SpringUtil.getBean(ThanksSettingDAO.class);
		success = thanksSettingDAO.addColumn("jsurvey_thankssetting", "isjump", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_thankssetting 表 isjump 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_thankssetting 表 isjump 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		TotalRecoDAO totalRecoDAO = SpringUtil.getBean(TotalRecoDAO.class);
		success = totalRecoDAO.addColumn("jsurvey_totalreco", "sumscore", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_totalreco 表 sumscore 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_totalreco 表 sumscore 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		TotalRecoDAO totalRecoDAO = SpringUtil.getBean(TotalRecoDAO.class);
		success = totalRecoDAO.addColumn("jsurvey_totalreco", "ipaddress", ColumnType.VARCHAR,"255","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_totalreco 表 ipaddress 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_totalreco 表 ipaddress 字段 新增【失败】" + "<br>");
		}
	}
	
	try{
		DisplayConfigDAO displayConfigDAO = SpringUtil.getBean(DisplayConfigDAO.class);
		success = displayConfigDAO.addColumn("jsurvey_displayconfig", "isshowscore", ColumnType.INT,"11","0");
	}catch(Exception e){
		e.printStackTrace();
	} finally {
		if(success){
			out.println("jsurvey_displayconfig 表 isshowscore 字段 新增【成功】" + "<br>");
		}else{
			out.println("jsurvey_displayconfig 表 isshowscore 字段 新增【失败】" + "<br>");
		}
	}
	out.println("用完删除" + "<br>");
%>