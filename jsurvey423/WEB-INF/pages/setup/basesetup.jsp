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
<title>系统参数设置</title>
<h:head validity="true" select="true" upload="true" toggle="true" checkpwd="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<h:import type="css" path="/resources/setup/css/main.css"></h:import>
<script type="text/javascript">
$(function(){
	pwdPower = ${checkPasswordLevel};
	switch (pwdPower) {
	case 0:
		$('#power').html('弱');
		break;
	case 1:
		$('#power').html('中');
		break;
	case 2:
		$('#power').html('强');
		break;
	}
	$('.toggle').toggle({
		jisToggle : function($obj){
			if($obj.attr('name') == 'sso'){
				if($obj.val() == 1){
					$('#ssoinfo').show();
				}else{
					$('#ssoinfo').hide();
				}
			}
		},
		toggleInit:function(){
			this.jisToggle(this.toggleObj);
		},
		toggleAfter:function(){
			this.jisToggle(this.toggleObj);
		}
	});
	
	$('#mainForm').validity(
		function(){
			$('#domain').require('应用全路径必须填写').match('url', '应用全路径必须为url格式').assert(function(){
				var domain = $.trim($('#domain').val());
				if(domain.lastIndexOf('/') == (domain.length - 1)){
					return false;
				}else{
					return true;
				}
			},'应用全路径不用以"/"结尾');
			$('#password').assert(function() {
				if($('#password').val()==''){
					return true;
				}
				if (level == "weak") {
					levelNum = 0;
				} else if (level == "medium") {
					levelNum = 1;
				} else if (level == "strong") {
					levelNum = 2;
				}
				if (levelNum < pwdPower) {
					return false;
				} else {
					return true;
				}
			}, '密码强度低于系统要求');
			if($('#sso').val() == 1){
				$('#appmark').require('appmark必须填写');
				$('#enckey').require('enckey必须填写');
				$('#jisurl').require('jisurl必须填写').match('url', 'jisurl必须为url格式');
			}
		}
	,{type:'iframe'});
});
</script>
</head>
<body>
<div class="crumb-path">系统参数设置</div>
<div style="padding: 10px;">
	<form id="mainForm" action="${url}" method="post" enctype="multipart/form-data">
		<table cellpadding="0" cellspacing="0" width="100%" id="mainTB">
			<tr>
				<td style="width: 100px;">
					系统安装路径
				</td>
				<td>
					${appPath }
				</td>
			</tr>
			<tr>
				<td style="width: 100px;">
					应用全路径
				</td>
				<td>
					<input id="domain" name="domain" type="text" class="input-text" style="width:300px;" value="${domain }"/> 
					<font color="blue">路径到应用名称，结尾不需要"/"，以http或者https开头。</font>
				</td>
			</tr>
			<tr>
				<td>
					应用名称
				</td>
				<td>
					${appname }
				</td>
			</tr>
			<tr>
				<td>
					版本
				</td>
				<td>
					${version }
				</td>
			</tr>
			<tr>
				<td>
					后台admin密码
					<div>等级要求：<font id="power" color="red"></font></div>
				</td>
				<td>
					<input id="password" name="password" type="password" class="input-text" style="width:300px;" onkeyup="javascript:EvalPwd(this.value)" onkeydown="if(event.keyCode==32) {event.returnValue = false;return false;}" onpaste="return false"/>  
					<ul id="pwdpower">
						<li id="pweak"></li>
						<li id="pmedium"></li>
						<li id="pstrong"></li>
					</ul>
				</td>
			</tr>
			<tr>
				<td>
					日志显示
				</td>
				<td>
					<select id="debug" name="debug" style="width:300px;" data-value="${debug}">
						<option value="1">错误</option>
						<option value="2">警告</option>
						<option value="3">信息</option>
						<option value="4">调试</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					数据交换接口
				</td>
				<td>
					<input type="hidden" name="sync" class="toggle" value="${sync}" />
				</td>
			</tr>
			<tr>
				<td>
					JIS配置
				</td>
				<td>
					<div id="jisConfig">
						<div style="color: blue">请先去JIS系统注册后，再来填写</div>
						<div><span>appmark：</span><input id="appmark" name="jisConfig.appmark" value="${jisConfig.appmark }" class="input-text" style="width:300px;"/></div>
						<div><span>enckey：</span><input id="enckey" name="jisConfig.enckey" value="${jisConfig.enckey }" class="input-text" style="width:300px;"/></div>
						<div><span>jisurl：</span><input id="jisurl" name="jisConfig.jisurl" value="${jisConfig.jisurl }" class="input-text" style="width:300px;"/></div>
						<div>
							<span>加密方式：</span>
							<select id="encrypttype" name="jisConfig.encrypttype" style="width:300px;" data-value="${jisConfig.encrypttype }">
								<option value="0">不加密</option>
								<option value="1">MD5加密</option>
								<option value="2">MD5+base64加密</option>
							</select>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					SSO接口
				</td>
				<td>
					<input type="hidden" id="sso" name="sso" class="toggle" value="${sso}" /> 
					<div id="ssoinfo">
						<div style="font-weight: bold;color: #FF6600">单点登录需要用到JIS，请先填写JIS配置</div>
						<div style="font-weight: bold;">本系统的单点登录地址为：<a style="color:#FF6600;">应用路径/jis/ssologin.do</a></div>
					</div>
				</td>
			</tr>
			<!-- 
			<tr>
				<td>
					验证码
				</td>
				<td>
					<input type="checkbox" name="code" value="1" data-value="${code }"/> 
				</td>
			</tr>
			 -->
			 <tr>
				<td>
					授权信息
				</td>
				<td>
					${regMessage }
				</td>
			</tr>
			<tr>
				<td>
					机器码
				</td>
				<td>
					<span style="color: rgb(56, 143, 201);">${machineCode }</span>
				</td>
			</tr>
			<tr>
				<td>
					授权文件
				</td>
				<td>
					<input type="file" name="licence" class="input-text" style="width: 400px;height: 23px"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="保存" class="btn btn-primary"/>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>