<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>密码重置</title>
<h:head pagetype="page" validity="true"></h:head>

<script type="text/javascript">
</script>
</head>
<body>
	<div id="page-title">
		<span id="page-location">密码重置</span>
	</div>
	<div id="page-content">
		<form action="${url }" method="post" id="oprform">
			<table border="0" align="center" class="table">
				<tr>
					<td align="right" class="label">用户名</td>
					<td class="required">*</td>
					<td><input type="text" id="name" name="name" maxlength="33"
						class="input-text" value="${fn:escapeXml(resetpwd.name) }" autocomplete="off" style="width: 200px"/></td>
				</tr>
				<tr>
					<td align="right" class="label">邮箱</td>
					<td class="required">*</td>
					<td><input type="text" id="email" name="email" maxlength="33"
						class="input-text" value="${fn:escapeXml(resetpwd.email) }" autocomplete="off" style="width: 200px"/></td>
				</tr>
				<tr>
					<td align="right" class="label">验证码</td>
					<td class="required">*</td>
					<td>
						<input type="text" id="verifycode" name="verifycode" maxlength="4"
						class="input-text" value="" autocomplete="off" style="width: 60px"/>
						<h:verifycode url="verifycode.do"></h:verifycode>
					</td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td>
						<input type="submit" class="btn btn-primary" value="找回" />&nbsp;
						<input type="reset" class="btn" value="重置" />
					</td>
				</tr>
				<tr>
					<td colspan="3" style="text-align: center;color: red">
						${message }
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>