<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>修改当前账户</title>
<h:head validity="true" pagetype="dialog" multiselect="true" checkpwd="true" ipass="true" message="true" placeholder="true" iconfont="true" cookie="true" security="true"></h:head>
<script type="text/javascript">
$(function(){
	var selecter = $('#rolenames');
	var userRoles = ${allRoleOptions};
	$.each(userRoles, function(i, value) {
		selecter.append('<li><span class="multiselect-selected">' + value + '</span></li>');
	});
	selecter.append('<li class="multiselect-clear"></li>');
	$('#rolenames').css('backgroundColor','#EAEAEA');

	//密码输入控件，防自动填充
	$('#pwd').iPass({keyup:function(){
		EvalPwd($(this).next().val());
	}});
	$('#repwd').iPass();
	
	$('#mainForm').validity(function(){
		$("#name").require("姓名不能为空")
				.match('username1', "姓名只能由字母、数字、下划线、中文组成，不能以下划线开头和结尾")
				.maxLength(33, "姓名不能超过33个字");
		if($('#pwd').val().length > 0 || $('#repwd').val().length > 0){
			$('#pwd').require('密码不能为空').
			minLength(6,'密码最少要6个字').
			maxLength(18,'密码最多18个字').
			assert(function(){
				if($('#pwd').val() == $('#repwd').val()){
					return true;
				}else{
					return false;
				}
			},'两次密码必须一致');
			$('#repwd').require('密码必须填写');
		}
		$("#headship").maxLength(80, "职务最多80个字");
		$("#phone").maxLength(80, "固定电话最多80个字");
		$("#mobile").maxLength(80, "移动电话最多80个字");
		$("#email").require("电子邮箱不能为空").match('email','电子邮箱地址不正确').maxLength(80, "电子邮箱地址最多80个字");
		$("#contact").maxLength(80, "联系方式最多80个字");
		$("#address").maxLength(80, "联系地址最多80个字");
		$("#commonRegion").maxLength(80, "常用登录地址最多80个字");
	}, {
		beforeSubmit : function() {
			var u_password = $('#pwd').val();
			var u_repassword = $('#repwd').val();
			$('#en_pwd').val(RSAencode(u_password));
			$('#en_repwd').val(RSAencode(u_repassword));
		},
		success : function(result){
			if(result.success){
				closeDialog();
			}else{
				alert(result.message);
			}
		}
	});
});

/**
 *	对textarea进行字数控制
 */
function checklength(obj) {
    var max = $(obj).attr('maxlength');
    if(max == null || max == "" || max == undefined) {
        return;
    }
    if(obj.value.length > max) {
        obj.value=obj.value.substring(0,(max-1));
        return;
    }
}
</script>
</head>
<body>
	<form action="${url}" method="post" id="mainForm">
		<input type="hidden" name="groupId" id="groupid" value="${user.groupId }" />
		<div id="dialog-content">
			<table border="0" align="center" class="table">
				<tr>
					<td align="right" class="label">姓名</td>
					<td class="required">*</td>
					<td>
						<input type="text" id="name" name="name" class="input-text" maxlength="33" class="input-text" value="${user.name}" <c:if test="${isSuperAdmin}">readonly="readonly"</c:if> />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">登录名</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="loginName" id="loginname" maxlength="33" class="input-text" value="${user.loginName}" disabled="disabled" />
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="right" class="label">密码</td>
					<td>&nbsp;</td>
					<td>
						<input id="en_pwd" type="hidden" name="pwd"/>
						<input type="password" id="pwd" class="input-text" onpaste="return false" pwdpower="pwdpower"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">密码强度</td>
					<td>&nbsp;</td>
					<td>
						<ul id="pwdpower">
							<li id="pweak"></li>
							<li id="pmedium"></li>
							<li id="pstrong"></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">确认密码</td>
					<td>&nbsp;</td>
					<td>
						<input id="en_repwd" type="hidden" name="repwd"/>
						<input type="password" id="repwd" class="input-text" onkeydown="if(event.keyCode==32) {event.returnValue = false;return false;}" onpaste="return false" />
					</td>
				</tr>
				<c:if test="${!isSuperAdmin}">
				<tr>
					<td align="right" class="label">所属机构</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="groupName" id="groupname" class="input-text" value="${groupName}" readonly="readonly" />
					</td>
				</tr>
				</c:if>
				<tr>
					<td align="right" class="label">电子邮箱</td>
					<td class="required">*</td>
					<td>
						<input type="text" name="email" id="email" class="input-text" maxlength="80" value="${user.email}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">角色</td>
					<td>&nbsp;</td>
					<td>
						<ul id="rolenames" class="input-text multiselect"></ul>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">职务</td>
					<td>&nbsp;</td>
					<td><input type="text" name="headship" id="headship"
						class="input-text" maxlength="80" value="${user.headship}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">固定电话</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="phone" id="phone" class="input-text" maxlength="80" value="${user.phone}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">移动电话</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="mobile" id="mobile" class="input-text" maxlength="80" value="${user.mobile}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">联系地址</td>
					<td>&nbsp;</td>
					<td><input type="text" name="address" id="address"
						class="input-text" maxlength="80" value="${user.address}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">联系方式</td>
					<td>&nbsp;</td>
					<td>
						<textarea name="contact" id="contact" class="input-textarea" maxlength="80" 
							onkeydown="checklength(this);" onmousedown="checklength(this);">${user.contact}</textarea>
					</td>
				</tr>
			</table>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" />
				<input type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>