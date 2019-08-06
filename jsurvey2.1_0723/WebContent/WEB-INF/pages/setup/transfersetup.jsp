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
<h:head validity="true" select="true" tip="true"></h:head>
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
			beforeSubmit : function(validateResult) {
				if(validateResult){
					top.$('body').mask('保存中，请稍后...');
				}
			},
			success:function(jsonResult){
				top.$('body').unmask();
				alert(jsonResult.message);
				if(jsonResult.success){
					location.reload();
				}
			}
		}
	);
	
	$('#transferForm').validity(
			function(){
				$('#out_webid').require('迁出站点id必须填写');
				$('#in_webid').require('迁入站点id必须填写');
			},{
				beforeSubmit : function(validateResult) {
					if(validateResult){
						top.$('body').mask('数据迁移中，请稍后...');
					}
				},
				success:function(jsonResult){
					top.$('body').unmask();
					alert(jsonResult.message);
					if(jsonResult.success){
						location.reload();
					}
				}
			}
		);
	
});

</script>
</head>
<body>
<div class="crumb-path">JSURVEY1.8数据库设置&nbsp;&nbsp;<span style="font-size: 15px;"><h:tip title="本次迁移只支持迁移在线调查和意见征集的基本数据（在线调查：标题，题目，选项及每个选项有多少人选择；意见征集：征集内容及回复内容）"></h:tip></span></div>
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
			<tr>
				<td colspan="2">
					<input type="submit" value="保存" class="btn btn-primary"/>
				</td>
			</tr>
		</table>
	</form>
	<form id="transferForm" action="${contextPath}/setup/main/transfer/transfer.do" method="post">
		<table cellpadding="0" cellspacing="0" width="100%" id="mainTB">
			<tr>
				<td>
					迁出站点ID
				</td>
				<td>
					<input id="out_webid" name="out_webid" type="text" class="input-text" style="width:300px;" value="${out_webid }"/>
					<span style="color:blue;margin-left:10px;">迁出站点ID为JSURVEY1.8站点ID</span>  
				</td>
			</tr>
			<tr>
				<td>
					迁入站点ID
				</td>
				<td>
					<input id="in_webid" name="in_webid" type="text" class="input-text" style="width:300px;" value="${in_webid }"/> 
					<span style="color:blue;margin-left:10px;">迁入站点ID为JSURVEY2.0站点ID</span>   
				</td>
			</tr>
			<tr>
				<td style="width: 100px;">
					迁移内容
				</td>
				<td>
					<select id="transfer_content" name="transfer_content" style="width:300px;" data-value="${transfer_content}">  
					    <option value="1">在线调查</option>
					    <option value="2">意见征集</option>
					     <option value="3">测评</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input id="transfer" type="submit" value="迁移" class="btn btn-primary"/>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>