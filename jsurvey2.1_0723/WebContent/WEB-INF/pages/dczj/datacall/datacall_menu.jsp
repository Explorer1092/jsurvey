<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线调查菜单页</title>
<h:head tree="true"></h:head>
<script type="text/javascript">
	/* $(function() {
		var zNodes = ${tree};
		$('#tree').tree(null, zNodes);
	}); */
	$(function() {
		var zNodes = ${tree};
		var setting = {
			view : {
				fitWidth : true
			},
			async: {
				enable: true,
				url: '${contextPath}/manager/jactmenu/menuwithurlforuser_search.do',
				autoParam: ["id=webId", "isDisabled"]
			}
		};
		$("#tree").tree(setting, zNodes);
	});
</script>
<style type="text/css">
body {
	margin: 5px 0 0 0;
	padding: 0;
	background-color: #F6F6F6;
}
</style>
</head>
<body>
	<div id="treewrap" style="position:absolute;top:5px;bottom:0;left:0;right:0;overflow:auto;">
		<ul id="tree" class="ztree"></ul>
	</div>
</body>
</html>