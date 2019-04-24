<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>在线用户列表</title>
<h:head pagetype="dialog" grid="true"></h:head>
<script type="text/javascript">
function toolbarAction(action) {
	switch (action) {
	case 'reload':
		window.location.reload();
		break;
	}
}

function kickUser(sessionId) {
	ajaxSubmit("user_kick.do?sessionId=" + sessionId, {
		success:function(result){
			if(result.success){
				location.reload();
			}else{
				alert(result.message);
			}
		}
	});
}
</script>

<style>
body {
	padding: 10px;
}
</style>
</head>
<body>
	<div id="page-content">
		<h:grid></h:grid>
	</div>
</body>
</html>