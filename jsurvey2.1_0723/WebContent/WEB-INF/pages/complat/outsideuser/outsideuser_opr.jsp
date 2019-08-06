<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>前台用户管理</title>
<h:head pagetype="dialog" validity="true" checkpwd="true" ipass="true" select="true" iconfont="true" cookie="true" security="true"></h:head>
<script type="text/javascript">
$(function(){
	$('#pwd').iPass({keyup:function(){
		EvalPwd($(this).next().val());
	}});
	
	$('#oprform').validity(
		function() {
			$("#name").require("姓名不能为空").maxLength(33,
					"姓名不能超过33个字");
			$("#loginname").require("登录名不能为空").maxLength(33,
					"登录名不能超过33个字，中文占用3个字");
			if ('${url }' == 'add_submit.do') {
				$("#pwd").require("密码不能为空").maxLength(18,
						"密码不能超过18个字");
			};
			$("#phone").maxLength(80, "固定电话最多80个字");
			$("#mobile").maxLength(80, "移动电话最多80个字");
			$("#email").maxLength(80, "电子邮箱最多80个字");
			$("#address").maxLength(80, "联系地址最多80个字");
			$("#contact").maxLength(80, "联系方式最多80个字");
		},{
			beforeSubmit : function(validateResult) {
				var u_loginname = $('#loginname').val();
				var u_password = $('#pwd').val();
				$('#en_loginname').val(RSAencode(u_loginname));
				$('#en_pwd').val(RSAencode(u_password));
			},
			success:function(result){
				if(result.success){
					closeDialog(true);
				}else{
					alert(result.message);
				}
			}
		}
	);
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
	<form action="${url }" method="post" id="oprform" name="oprform">
		<input type="hidden" name="iid" id="iid" value="${outsideuser.iid }" />
		<div id="dialog-content">
			<table border="0" align="center" cellpadding="10" cellspacing="0"
				class="table">
				<tr>
					<td align="right" class="label">姓名</td>
					<td class="required">*</td>
					<td><input type="text" id="name" name="name"
						class="input-text" maxlength="33" value="${outsideuser.name }" />&nbsp;</td>
				</tr>
				<tr>
					<td align="right" class="label">登录名</td>
					<td class="required">*</td>
					<td><c:choose>
							<c:when test="${url=='modify_submit.do'}">
								<input type="text" name="loginName" id="loginname"
									class="input-text" maxlength="33" value="${outsideuser.loginName }"
									disabled="disabled" />&nbsp;
					</c:when>
							<c:otherwise>
								<input id="en_loginname" type="hidden" name="loginName"/>
								<input type="text" id="loginname"
									class="input-text" maxlength="33" value="${outsideuser.loginName }" />&nbsp;
					</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<td align="right" class="label">密码</td>
					<td class="required">*</td>
					<td>
						<input id="en_pwd" type="hidden" name="pwd"/>
						<input type="text" id="pwd"
						class="input-text" maxlength="18" value="" onkeyup="javascript:EvalPwd(this.value)"/></td>
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
					<td align="right" class="label">固定电话</td>
					<td>&nbsp;</td>
					<td><input type="text" name="phone" id="phone"
						class="input-text" maxlength="80" value="${outsideuser.phone}" /></td>
				</tr>
				<tr>
					<td align="right" class="label">移动电话</td>
					<td>&nbsp;</td>
					<td><input type="text" name="mobile" id="mobile"
						class="input-text" maxlength="80" value="${outsideuser.mobile}" /></td>
				</tr>
				<tr>
					<td align="right" class="label">电子邮箱</td>
					<td>&nbsp;</td>
					<td><input type="text" name="email" id="email"
						class="input-text" maxlength="80" value="${outsideuser.email}" /></td>
				</tr>
				<tr>
					<td align="right" class="label">联系地址</td>
					<td>&nbsp;</td>
					<td><input type="text" name="address" id="address"
						class="input-text" maxlength="80" value="${outsideuser.address}" /></td>
				</tr>
				<tr>
					<td align="right" class="label">联系方式</td>
					<td>&nbsp;</td>
					<td>
						<textarea name="contact" id="contact" class="input-textarea" maxlength="80" 
							onkeydown="checklength(this);" onmousedown="checklength(this);">${outsideuser.contact}</textarea>
				</tr>
			</table>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" /> <input
					type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>