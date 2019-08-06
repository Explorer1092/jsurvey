<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><!--标题--></title>
<h:head pagetype="page" grid="true" validity="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<style type="text/css">
	#page-content{
		min-width: 940px;
   	 	height: 50px;
    	/* border-bottom: 1px solid #CCC; */
   	 	margin: 0 20px;
    	font-size: 15px;
    	line-height: 50px;
    	color: #000;
	}
	.input-text {
		width: 500px
	}
	.label{
		text-align: right;
	}
</style>

<script>
	$(function() {
		$('#emailform').validity(function() {
			 $('#user').require('邮箱地址').match('email','用户名需要邮箱格式');
			 $('#form').require('邮箱地址').match('email','发件人需要邮箱格式');
		}, {
			success : function(result) {
				if(result.success){
					alert('保存成功');
				} else {
					alert('保存失败');
				}
			}
		});
	});
	
</script>
</head>
<body>
	<div id="page-title">
		系统参数 / 
		<span id="page-location">邮件设置</span>
	</div>
	<div id="page-content">
		<form action="${url}" method="post" id="emailform">
			<table border="0" align="center" class="table" style="width: 800px">
				<tr>
					<td colspan="2">
						邮箱登录设置
					</td>
				</tr>
				<tr>
					<td class="label" style="width: 100px">HOST</td>
					<td><input type="text" class="input-text" name="host" value="${host}" id="host"></td>
				</tr>
				<tr>
					<td class="label">用户名 </td>
					<td><input type="text" class="input-text" name="user" value="${user}" id="user"></td>
				</tr>
				<tr>
					<td class="label">密码 </td>
					<td><input type="password" class="input-text" name="pwd" value="${pwd}" id="pwd"></td>
				</tr>
				<tr>
					<td class="label">发件人邮箱 </td>
					<td><input type="text" class="input-text" name="form" value="${form}" id="form"></td>
				</tr>
				<tr>
					<td class="label">发件人昵称 </td>
					<td><input type="text" class="input-text" name="form_nickname" value="${form_nickname}" id="form_nickname"></td>
				</tr>
			</table>
			<table border="0" align="center" class="table" style="width: 800px">
				<tr>
					<td colspan="2">
						密码找回设置
					</td>
				</tr>
				<tr>
					<td class="label" style="width: 100px">主题 </td>
					<td>
						<input type="text" class="input-text" name="subject" value="${subject}" id="subject">
						<div style="font-size: 14px;color: #cc0066">默认为“密码找回”</div>
					</td>
				</tr>
				<tr>
					<td class="label">邮件内容 </td>
					<td>
						<textarea  class="input-text" maxlength="200" class="input-textarea" style="width:500px;height:200px;" name="content" id="content">${content}</textarea>
						<div style="font-size: 14px;color: #cc0066">\${url}为找回密码的url，自动填充请不要修改</div>
					</td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2">
						<input class="btn btn-primary" type="submit" value="保存">
						<input class="btn" type="reset" value="重置">
					</td>
				</tr>
			</table>	
		</form>
	</div>
</body>
</html>