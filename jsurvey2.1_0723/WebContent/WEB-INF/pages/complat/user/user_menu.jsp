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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户管理树</title>
<h:head tree="true"></h:head>
<script type="text/javascript">
	$(function() {
		var zNodes = ${tree};
		var setting = {
			view : {
				fitWidth : true
			},
			async: {
				enable: true,
				url: '${contextPath}/manager/menu/menuwithurlforuser_search.do',
				autoParam: ["id=groupId", "isDisabled"]
			}
		};
		$("#tree").tree(setting, zNodes);
	});
</script>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background-color: #F6F6F6;
}
</style>
</head>
<body>
	<ul id="tree" class="ztree"></ul>
</body>
</html>