<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>SSO</title>
<style type="text/css">
body{
	overflow: hidden;
	color: #fff;
	width: 100%;
	font-size: 9pt;
	white-space:nowrap;
	word-break:keep-all;
}
a{
	color: #fff;
	text-decoration: none;
	font-weight: bold;
	font-size: 9pt;
}
</style>
</head>
<body topmargin="3" leftmargin="0" bgColor="transparent">
<c:if test="${formBean.ssourl == ''}">SSO接口地址不存在，请将本系统在sso系统中进行注册。</c:if>
<c:if test="${type == '5' && (xmlstate == 'OK' || xmlstate == 'ok')}">
<script type="text/javascript" language="javasript">
function submitForm(jsAppId){
	if(jsAppId==''){
		alert('请正确选择登录的系统!');
		return false;
	}
	var jsLoginUser=document.getElementById('loginuser');
	var jsLoginPass=document.getElementById('loginpass');
	var jsModifyUser=document.getElementById('modifyuser');
	var jsTuTitle=document.getElementById(jsAppId+"Tu").title;
	var jsTpTitle=document.getElementById(jsAppId+"Tp").title;
	var jsTlTitle=document.getElementById(jsAppId+"Tl").title;
	var jsTcTitle=document.getElementById(jsAppId+"Tc").title;
	var jsTmTitle=document.getElementById(jsAppId+"Tm").title;
	
	if(jsTmTitle=='1'){
		jsTmTitle='T';
	}if(jsTmTitle=='0'){
		jsTmTitle='F';
	}if(jsTmTitle=='2'){//支持修改用户090225
		jsTmTitle='U';
	}
	var jsWindow="_blank";
	if(jsTcTitle=="1"){		//窗口切换方式 1 自身 2 新窗口
		jsWindow="_top";
	}
	jsLoginUser.value=jsTuTitle;
	jsLoginPass.value=jsTpTitle;
	jsModifyUser.value=jsTmTitle;
	document.formSso.action=jsTlTitle;
	document.formSso.target=jsWindow;
	document.formSso.submit();
}
</script>
<form method="post" name="formSso" id="formSso">
<input type="hidden" id="loginuser" name="loginuser"> 
<input type="hidden" id="loginpass" name="loginpass"> 
<input type="hidden" id="modifyuser" name="modifyuser"> 
<input type="hidden" id="state" name="state" value="${state}"> 
<input type="hidden" id="result" name="result" value="${result}"> 
<input type="hidden" id="email" name="email" value="${user.email}"> 
<input type="hidden" id="mobile" name="mobile" value="${user.mobile}">
<input type="hidden" id="t_name" name="t_name" value="${user.name}">
<input type="hidden" id="sex" name="sex" value="${user.sex}"> 
<input type="hidden" id="ldapurl" name="ldapurl" value="">
<input type="hidden" id="domainname" name="domainname" value="">
<input type="hidden" id="web" name="web" value="0"> 
<input type="hidden" id="url" name="url" value="">
<input type="hidden" id="address" name="address" value="${user.address}">
<div style="display: none;">
<c:forEach items="${loginuser}" varStatus="i" var="field">
	<span id="${appid[i.index]}Tu" title="${field}"></span>
	<span id="${appid[i.index]}Tp" title="${loginpass[i.index]}"></span> 
	<span id="${appid[i.index]}Tl" title="${url[i.index]}"></span>
	<span id="${appid[i.index]}Tc" title="${cuttype[i.index]}"></span> 
	<span id="${appid[i.index]}Tm" title="${modifyuser[i.index]}"></span>
	
</c:forEach>
</div>
<table align="right">
	<tr>
		<td style="color: #fff; font-weight: bold;">&nbsp;&nbsp;</td>
		<td align="right" >
		<c:forEach items="${appname}" varStatus="i" var="field"><c:if 
			test="${i.index!=0}">|</c:if><a href="javascript:" 
			onclick="return submitForm('${appid[i.index]}');">〖${field}〗</a></c:forEach>
		</td>
		<td style="color: #fff; font-weight: bold;">&nbsp;&nbsp;</td>
	</tr>
</table>
</form>
</c:if>
</body>
</html>