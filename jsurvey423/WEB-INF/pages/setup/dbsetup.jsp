<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>数据库初始化</title>
<h:head validity="true" select="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<h:import type="css" path="/resources/setup/css/main.css"></h:import>
<script type="text/javascript">
$(function(){
	//$('#dbPassword').iPass();
	
	$('#mainForm').validity(
		function(){
			$('#ip').require('ip必须填写');
			$('#port').require('端口必须填写');
			$('#dbName').require('数据库名称必须填写');
			$('#dbUser').require('数据库用户必须填写');
		},{
			success:function(result){
				alert(result.message);
				if(result.success){
					location.reload();
				}
			}
		}
	);
});
function initDB(){
	top.$('body').mask('处理中，请稍后...');
	ajaxSubmit('initdb.do', {
		success:function(result){
			top.$('body').unmask();
			alert(result.message);
			location.reload();
		}
	});
}
</script>
</head>
<body>
<div class="crumb-path">数据库设置</div>
<div style="padding: 10px;">
	<form id="mainForm" action="${url}" method="post">
		<table cellpadding="0" cellspacing="0" width="100%" id="mainTB">
			<tr>
				<td style="width: 100px;">
					数据库类型
				</td>
				<td>
					<select id="dbType" name="dbType" style="width:300px;" data-value="${dbtype}">  
					    <option value="1">ORACLE</option>
					    <option value="2">MSSQL</option>
					    <option value="5">MYSQL</option>
					    <option value="6">KingbaseES（金仓）</option>
					    <option value="7">DMBase（达梦）</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					数据库服务器IP
				</td>
				<td>
					<input id="ip" name="ip" type="text" class="input-text" style="width:300px;" value="${ip }"/>  
				</td>
			</tr>
			<tr>
				<td>
					数据库连接端口
				</td>
				<td>
					<input id="port" name="port" type="text" class="input-text" style="width:300px;" value="${port }"/>  
				</td>
			</tr>
			<tr>
				<td>
					数据库名称(SID)
				</td>
				<td>
					<input id="dbName" name="dbName" type="text" class="input-text" style="width:300px;" value="${dbName }"/>  
				</td>
			</tr>
			<tr>
				<td>
					数据库用户
				</td>
				<td>
					<input id="dbUser" name="dbUser" type="text" class="input-text" style="width:300px;" value="${dbUser }"/>  
				</td>
			</tr>
			<tr>
				<td>
					数据库密码
				</td>
				<td>
					<input id="dbPassword" name="dbPassword" type="password" class="input-text" style="width:300px;"/>
					<c:if test="${!canInit}">
						<span style="color:blue;margin-left:10px;">不修改密码则不需要填写</span>
					</c:if>
				</td>
			</tr>
			<c:if test="${prepared}">
			<tr style="color: red">
				<td colspan="2">
					在更改数据库配置信息之前，请先确认本应用没有正在使用当前数据库
				</td>
			</tr>
			</c:if>
			<tr>
				<td colspan="2">
					<input type="submit" value="保存" class="btn btn-primary"/>
					<c:if test="${canInit}">
						<input type="button" value="初始化" class="btn btn-success" onclick="initDB()" style="margin-left: 10px"/>
					</c:if>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>