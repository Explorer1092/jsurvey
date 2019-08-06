<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统安装界面</title>
<h:head message="true" validity="true" cookie="true" security="true"></h:head>
<h:import type="css" path="/resources/setup/css/login.css"></h:import>
<script type="text/javascript">
$.validity.setup({
	outputMode : "showErr"
}); 
window.confirm = function(msg,okCall,cancelCall){
	top.$.messager.confirm(' ',msg,function(flag){
		if(flag){
			if(typeof(okCall) != 'undefined'){
				okCall();
			}
		}else{
			if(typeof(cancelCall) != 'undefined'){
				cancelCall();
			}
		}
	});
};
$(function(){
	$('#loginform').validity(function(){
		$('#userid').require('请填写用户名');
		$('#userpassword').require('请填写密码');
		$('#randcode').require('请填写验证码');
	},{
		beforeSubmit : function(validateResult) {
			var u_user = $('#userid').val();
			var u_password = $('#userpassword').val();
			$('#enc_name').val(RSAencode(u_user));
			$('#enc_password').val(RSAencode(u_password));
		},
		success:function(result){
			if(result.success){
				top.location.href='main/index.do';
			}else{
				$('#verifyImg').click();
				alert(result.message);
			}
		}
	});
	window.alert = function (msg,type,fu){
		top.$.messager.alert(' ',msg,type,fu);
	};
});
function useridfocus() {
	//默认焦点在用户名上
	$('#userid').focus();
	//如果用户名有值，则默认到密码上
	if ($('#userid').val().length > 0){
		$('#userpassword').focus();
	}
}
</script>
<style>
#verifyImg{
	vertical-align: middle;
}
</style>
</head>
<body style="background-color: #E6EEF0" onload="useridfocus();">
<table border="0" cellspacing="0" cellpadding="0"
	style="height: 100%; width: 100%">
	<tr>
		<td align="center" valign="middle">
			<table border="0" align="center" cellpadding="0" cellspacing="0" class="login-box">
				<tr>
					<td width="441" align="center" style="vertical-align: top">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="67" align="left" valign="middle"
									style="font-family: '黑体'; font-weight: bold; font-size: 16pt; color: #FFF; padding-left: 33px;">
									系统安装
								</td>
								<td></td>
							</tr>
						</table>
						<form action="${url}" method="post" id="loginform">
						<input id="enc_name" type="hidden" name="username"/>
						<input id="enc_password" type="hidden" name="password"/>
						<table align="center" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="15" colspan="2"><div style="height: 15px"></div></td>
							</tr>
							<tr>
								<td height="45" width="50" align="right" valign="middle">登录名：</td>
								<td align="left" valign="middle">
									<input type="text" id="userid" value="${fn:escapeXml(username)}" class="input-text" style="width:220px;"/>
								</td>
							</tr>
							<tr>
								<td height="45" width="50" align="right" valign="middle">密　码：</td>
								<td align="left" valign="middle">
									<input type="password" id="userpassword" class="input-text" style="width:220px;"/>
								</td>
							</tr>
							<tr>
								<td height="45" width="50" align="right" valign="middle">验证码：</td>
								<td align="left" valign="middle">
									<input type="text" id="randcode" maxlength="4" name="randcode" class="input-text" style="width:145px;"/>
									<h:verifycode url="verifyCode.do" width="70" height="30"></h:verifycode>
								</td>
							</tr>
							<tr style="margin-top: 15px">
								<td height="35" width="50" align="right" valign="middle"></td>
								<td align="left" valign="middle">
									<input type="image" class="btnop" 
										style="margin-left: 5px;"
										onmouseover="this.className='btnop2';" onmouseout="this.className='btnop';"
										src="${contextPath}/resources/setup/images/hb_pic_30.gif"/>
								</td>
							</tr>
						</table>
						</form>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<div id="manager-btn">
	<a href="../login.do">控制面板</a>
</div>
</body>
</html>