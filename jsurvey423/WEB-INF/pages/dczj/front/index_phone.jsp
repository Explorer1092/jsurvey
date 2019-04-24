<%@page import="com.hanweb.common.BaseInfo"%>
<%@page import="com.hanweb.common.util.security.SecurityUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<h:head validity="true" security="true" cookie="true" message="true"></h:head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="user-scalable=no, width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>调查征集系统登录页面</title>
<style type="text/css">
@charset "utf-8";
/* DaTouWang URL: www.datouwang.com */
html,body,div,p,form,label,ul,li,dl,dt,dd,ol,img,button,b,em,strong,small,h1,h2,h3,h4,h5,h6
	{
	margin: 0;
	padding: 0;
	border: 0;
	list-style: none;
	font-style: normal;
}

body {
	font-family: SimHei, 'Helvetica Neue', Arial, 'Droid Sans', sans-serif;
	font-size: 14px;
	color: #333;
	background: #f2f2f2;
}

a,a.link {
	color: #666;
	text-decoration: none;
	font-weight: 500;
}

a,a.link:hover {
	color: #666;
}

h1,h2,h3,h4,h5,h6 {
	font-weight: normal;
}

.login {
	width: 100%;
	height: 100%;
	background:
		url(../../resources/dczj/images/login/phonebackground.jpg)
		no-repeat;
	background-size: cover;
	position: fixed;
	z-index: -10;
}

.welcome {
	width: 100%;
	margin: 25% 0;
}

.welcome img {
	width: 100%;
}

.login-inp {
	margin: 0 30px 15px 30px;
	border: 1px solid #fff;
	border-radius: 5px;
}

.login-inp label {
	width: 4em;
	text-align: center;
	display: inline-block;
	color: #fff;
}

.login-inp input {
	line-height: 35px;
	color: #fff;
	background-color: transparent;
	border: none;
	outline: none;
}

.login-inp a {
	display: block;
	width: 100%;
	text-align: center;
	line-height: 40px;
	color: #fff;
	font-size: 16px;
	letter-spacing: 5px;
}

.login-txt {
	text-align: center;
	color: #fff;
}

.login-txt a {
	color: #fff;
	padding: 0 5px;
}
</style>
<%
SecurityUtil.init(BaseInfo.getRealPath());
String publicKey = SecurityUtil.getPublicKey();
%>
<script type="text/javascript">
$(function(){
	
	window.alert = function(msg, type, fu) {
		$('input, button').blur();
		top.$.messager.alert('消息', msg, type, fu);
	};
	
	var dczjid = $('#dczjid').val();
// 	document.getElementById('piccode').src="../../front/survey/que_code.do?random="+Math.random();
	
	$("#oprform").validity(function() {
	},{
		beforeSubmit : function(validateResult) {
		var u_user = $('#loginname').val();
		var u_password = $('#loginpwd').val();
// 		alert("u_user " + RSAencode(u_user));
		$('#enc_name').val(Aencode(u_user));
		$('#enc_password').val(Aencode(u_password));
// 		alert("enc_name " + $('#enc_name').val());
	  },success:function(result){
			if(result.success){
				window.location.href="../../jsurvey/questionnaire/phonejsurvey_"+dczjid+".html";
			}else{
// 				changecode();
				alert(result.message);
			}
		}
	});
});

// function changecode(){
// 	document.getElementById('piccode').src="../../front/survey/que_code.do?random="+Math.random();
// }

function loginsumit(){
	var user = $('#loginname').val();
	var password = $('#loginpwd').val();
// 	var code = $('#logincode').val();
	if(user != undefined && user != '' && user.length > 0){
		if(password != undefined && password != '' && password.length > 0){
// 			if(code != undefined && code != '' && code.length > 0){
				$("#oprform").submit();
// 			}else{
// 				mui.alert("验证码不能为空！");
// 			}
		}else{
			alert("密码不能为空！");
		}
	}else{
		alert("用户名不能为空！");
	}
}

function Aencode(str){
	var pubk =$('#pubkey').val();		
	var result = '';
	if(pubk && str){
		var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(pubk);
	    result = encrypt.encrypt(str);
	}
	return result;
}
</script>
<script src=" ../../resources/dczj/mui/js/mui.min.js"></script>
</head>
<body>
	<form action="${url }" method="post" id="oprform" name="oprform"class="form">
		<input type="hidden" name="dczjid" id="dczjid" value="${dczjid}" /> 
		<input type="hidden" id="pubkey" name="pubkey" value="<%=publicKey%>"/>
		<input id="enc_name" type="hidden" name="name" /> 
		<input id="enc_password" type="hidden" name="password" />
		<div class="login">
			<div class="welcome">
				<img src="../../resources/dczj/images/checkresult/welcome.png">
			</div>
			<div class="login-form">
				<div class="login-inp">
					<label>账号</label><input id="loginname" name="loginname" type="text">
				</div>
				<div class="login-inp">
					<label>密码</label><input id="loginpwd" name="loginpwd" type="password">
				</div>
				<div class="login-inp">
					<a onclick="loginsumit();">立即登录</a>
				</div>
			</div>
		</div>
	</form>
</body>
</html>