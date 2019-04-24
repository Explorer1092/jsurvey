<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改登陆密码</title>
<h:head dialog="true" message="true" iconfont="true" pagetype="page" validity="true" checkpwd="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<script>
	/**
	 * 表单验证
	 */
	$(function() {
		var pwdPower = ${checkPasswordLevel};
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
		$('#oprform').validity(function() {
			$('#password').require('密码不能为空').minLength(6, '密码最少要6个字').maxLength(18, '密码最多18个字').assert(function() {
				if ($('#password').val() == $('#repassword').val()) {
					return true;
				} else {
					return false;
				}
			}, '两次密码必须一致').assert(function() {
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
			$('#repassword').require('密码必须填写');
		}, {
			success : function(result) {
				alert(result.message);
				if (result.success) {
					top.location.href = '${contextPath}/setup/logout.do';
				}
			}
		});
	});
</script>
</head>
<body>
	<div id="breadcrumb">系统安装</div>
	<div id="page-title">修改登陆密码</div>
	<div id="page-content">
		<!--表单主体-->
		<form action="${url}" method="post" id="oprform" name="oprform">
			<table border="0" align="center" class="table">
				<tr>
					<td colspan="2" class="label">需要强度：</td>
					<td>
						<font color="red" id="power"></font>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">密码</td>
					<td class="required">*</td>
					<td>
						<input type="password" id="password" name="password" maxlength="33" class="input-text" onkeyup="javascript:EvalPwd(this.value)" onkeydown="if(event.keyCode==32) {event.returnValue = false;return false;}" onpaste="return false" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label"></td>
					<td class="required"></td>
					<td>
						<table cellpadding="0" cellspacing="0" border="0" title="字母加数字加符号就会强" id="pwdpower" style="width: 100%">
							<tr>
								<td id="pweak" style="">弱</td>
								<td id="pmedium" style="">中</td>
								<td id="pstrong" style="">强</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">重复密码</td>
					<td class="required">*</td>
					<td>
						<input type="password" id="repassword" name="repassword" maxlength="33" class="input-text" onkeydown="if(event.keyCode==32) {event.returnValue = false;return false;}" onpaste="return false" />
					</td>
				</tr>
				<tr>
					<td height="60" colspan="4" align="center">
						<input type="submit" class="btn btn-primary" value="保存" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>