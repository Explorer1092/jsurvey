<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>系统安装界面</title>
<h:head message="true" loadmask="true"></h:head>
<h:import type="css" path="/resources/setup/css/main.css"></h:import>
<script type="text/javascript">
$(function(){
	$("#centerFrame").height($("body").height()-56);
	$(window).resize(function(){
		$("#centerFrame").height($("body").height()-56);
	});
	$('#nav li').on('click',function(){
		$('#nav li').removeClass('active');
		$(this).addClass('active');
	});
});
</script>
</head>
<body style="overflow: hidden;">
<table cellpadding="0" cellspacing="0" style="width: 100%;">
	<tr>
		<td colspan="3" class="top">
			<span class="log"><img src="${contextPath}/resources/setup/images/install.png" width="40" height="40" style="vertical-align: middle;margin-right: 7px"/>系统安装</span>
			<span class="logout"><a href="${logout_url }" style="color:#FFF;">退出</a></span>
		</td>
	</tr>
	<tr>
		<td valign="top" class="left" style="vertical-align: top;">
			<ul class="nav" id="nav">
				<li class="active"><a href="${baseSetup}" target="centerFrame"><img src="${contextPath}/resources/setup/images/setup.png" width="23" height="23"/>系统参数设置</a></li>
				<li><a href="${dbSetup}" target="centerFrame"><img src="${contextPath}/resources/setup/images/database.png" width="23" height="23"/>数据库设置</a></li>
			    <li><a href="${transferSetup}" target="centerFrame"><img src="${contextPath}/resources/setup/images/database.png" width="23" height="23"/>数据迁移设置</a></li>
			</ul>
		</td>
		<td id="center" valign="top" style="overflow: hidden;">
			<iframe id="centerFrame" name="centerFrame" style="width: 100%;" frameborder="0" src="${center_url }"></iframe>
		</td>
	</tr>
</table>
</body>
</html>