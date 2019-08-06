<%@page import="com.hanweb.common.BaseInfo"%>
<%@page import="com.hanweb.common.util.security.SecurityUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户登录验证</title>
<h:head validity="true" message="true" dialog="true"  tip="true" placeholder="true" checkpwd="true" iconfont="true" cookie="true" security="true"></h:head>
<!-- <link rel="stylesheet" href="../../resources/dczj/survey/images/checkresult/style.css" id="styleCss"/> -->
<%
SecurityUtil.init(BaseInfo.getRealPath());
String publicKey = SecurityUtil.getPublicKey();
%>
<script type='text/javascript'>if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {window.location.href='../../front/survey/userlogin_phone.do?dczjid=${dczjid}';}</script>
<script type="text/javascript">
$(function(){
	$(".placeholder-cover").css('padding-left','80px');
	$(".placeholder-cover").css('color','#dbdbdb');
	
	window.alert = function(msg, type, fu) {
		$('input, button').blur();
		top.$.messager.alert('消息', msg, type, fu);
	};
	
	var dczjid = $('#dczjid').val();
	document.getElementById('piccode').src="../../front/dczj/que_code.do?random="+Math.random();
	
	$("#oprform").validity(function() {
	},{
		beforeSubmit : function(validateResult) {
		var u_user = $('#loginname').val();
		var u_password = $('#loginpwd').val();
		$('#enc_name').val(RSAencode(u_user));
		$('#enc_password').val(RSAencode(u_password));
	  },success:function(result){
			if(result.success){
				window.location.href="../../jsurvey/questionnaire/jsurvey_"+dczjid+".html";
			}else{
				changecode();
				alert(result.message);
			}
		}
	});
});

function changecode(){
	document.getElementById('piccode').src="../../front/dczj/que_code.do?random="+Math.random();
}

function loginsumit(){
	var user = $('#loginname').val();
	var password = $('#loginpwd').val();
	var code = $('#logincode').val();
	$('#enc_name').val(RSAencode(user));
	$('#enc_password').val(RSAencode(password));
	
	if(user != undefined && user != '' && user.length > 0){
		if(password != undefined && password != '' && password.length > 0){
			if(code != undefined && code != '' && code.length > 0){
				$("#oprform").submit();
			}else{
				alert("验证码不能为空！");
			}
		}else{
			alert("密码不能为空！");
		}
	}else{
		alert("用户名不能为空！");
	}
}
$(document).keyup(function(event){
	  if(event.keyCode ==13){
		  loginsumit();
	  }
});

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
<style type="text/css">
#web_bg {
  background: url(../../resources/dczj/images/login/pcbackground.jpg);
  position:fixed;
  top: 0;
  left: 0;
  width:100%;
  height: 100%;
  min-width: 1000px;
  z-index:-10;
  zoom: 1;
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-background-size: cover; 
  -o-background-size: cover; 
  background-position: center 0;
}
.form { 
    width: 100%;
    padding-left: 55%;
    padding-top: 18%;
  }
#form-body{
     width: 365px;
}

.ui-input { position: relative; margin: 20px 0;font-size: 0;}
  
 ::-webkit-input-placeholder { /* WebKit, Blink, Edge */
    color:    #dbdbdb;
}
 :-moz-placeholder { /* Mozilla Firefox 4 to 18 */
   color:    #dbdbdb;
   opacity:  1;
}
::-moz-placeholder { /* Mozilla Firefox 19+ */
   color:    #dbdbdb;
   opacity:  1;
}
:-ms-input-placeholder { /* Internet Explorer 10-11 */
   color:    #dbdbdb;
}
::-ms-input-placeholder { /* Microsoft Edge */
   color:    #dbdbdb;
}

#loginname{
  background: url(../../resources/dczj/images/login/user.png) no-repeat;
  background-position:20px center;
  color: #ffffff;
  width: 270px; 
  height: 48px; 
  border: 0; 
  font-size: 14px; 
  outline: none;  
  border: 1px solid #FFFFFF;
  padding-left: 80px; 
  background-color: transparent;
  border-radius:8px;
}

#loginpwd{
  background: url(../../resources/dczj/images/login/pwd.png) no-repeat;
  background-position:20px center;
  color: #ffffff;
   width: 270px; 
     height: 48px; 
     border: 0; 
     font-size: 14px; 
     outline: none;  
     border: 1px solid #FFFFFF;
     padding-left: 80px; 
     background-color: transparent;
     border-radius:8px;
}

#logincode{
  background: url(../../resources/dczj/images/login/check.png) no-repeat;
  background-position:20px center;
  color: #ffffff;
   width: 270px; 
     height: 48px; 
     border: 0; 
     font-size: 14px; 
     outline: none;  
     border: 1px solid #FFFFFF;
     padding-left: 80px; 
     background-color: transparent;
     border-radius:8px;
}
#loginbtn{
    text-align: center;
	line-height:45px;
	width: 270px;
	height: 45px;
	margin: auto;  
    cursor: pointer;
    background-color: #00d4a4;
    color: #FFFFFF;
    font-size: 14px;
    font-family: 微软雅黑;
    border-radius:5px;
}
#loginbtn:HOVER {
   background-color: #00EE76;
}
</style>
</head>
<body>
<div id="web_bg">
<form action="${url }" method="post" id="oprform" name="oprform" class="form">
<input type="hidden" name="dczjid" id="dczjid" value="${dczjid}"/>
			<input type="hidden" id="pubkey" name="pubkey" value="<%=publicKey%>"/>
			<input id="enc_name" type="hidden" name="name"/>
			<input id="enc_password" type="hidden" name="password"/>
<div id="form-body">
  <div class="ui-input">
		<input type="text"  id="loginname" name="loginname"  placeholder="请输入用户名" autocomplete="off" />
  </div>
  <div class="ui-input">
		<input type="password"  id="loginpwd" name="loginpwd"  placeholder="请输入密码" autocomplete="off"/>
  </div>
  <div class="ui-input">
        <table><tr><td></td><td></td></tr></table>
		<input type="text" id="logincode" name="logincode"  placeholder="请输入验证码" autocomplete="off" style="width: 140px "/>
		<img id="piccode" src="" title="如果看不清数字，请点图片刷新验证码" style="cursor:pointer;vertical-align: middle;width: 130px" onclick="changecode();">
  </div> 
  <div>
       <div style="width: 365px;text-align: center;"> <div id="loginbtn" onclick="loginsumit();">立即登录</div></div>
<!-- 		<input type="button" class="btn btn-success" value="立即登录" style="width: 270px;height: 44px;margin-left: 40px;background-color: #00d4a4;border-color: #00d4a4;" onclick="loginsumit();"/> -->
  </div>
</div>
</form>
</div>
</body>
</html>