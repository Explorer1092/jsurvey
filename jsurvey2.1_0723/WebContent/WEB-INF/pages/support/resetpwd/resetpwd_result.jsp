<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">   
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
		<c:if test="${success }">
		<div style="width: 400px;margin: auto">
			<h3 style="color: #3374AC">邮件发送成功</h3>
			<div>邮件发送成功，请尽快前往邮箱进行密码重置操作！</div>
		</div>
		</c:if>
	</div>
</body>
</html>