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
</head>
<body>
	<div id="page-title">
		<span id="page-location">密码重置</span>
	</div>
	<div id="page-content">
		<c:choose>
			<c:when test="${resetPwdCache == null }">
				<div style="width: 400px;margin: auto">
					<h3 style="color: #3374AC">操作失败</h3>
					<div>此次密码重置的安全凭证已过期失效，无法设置帐号信息</div>
				</div>
			</c:when>
			<c:otherwise>
				<form action="${url }" method="post" id="oprform">
					<input type="hidden" name="token" value="${fn:escapeXml(resetPwdCache.token) }">
					<table border="0" align="center" class="table">
						<tr>
							<td align="right" class="label">用户名</td>
							<td class="required">*</td>
							<td>${fn:escapeXml(resetPwdCache.name )}</td>
						</tr>
						<tr>
							<td align="right" class="label">新密码</td>
							<td class="required">*</td>
							<td><input type="password" id="newpwd" name="newPwd" maxlength="33"
								class="input-text" value="" autocomplete="off" style="width: 200px"/></td>
						</tr>
						<tr>
							<td align="right" class="label">重复密码</td>
							<td class="required">*</td>
							<td><input type="password" id="re-newpwd" name="reNewPwd" maxlength="33"
								class="input-text" value="" autocomplete="off" style="width: 200px"/></td>
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
								<input type="submit" class="btn btn-primary" value="确定" />&nbsp;
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
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>