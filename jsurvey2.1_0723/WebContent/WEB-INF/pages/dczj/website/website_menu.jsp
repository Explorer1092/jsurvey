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
<title>机构列表树形菜单</title>
<h:head tree="true" iconfont="true"></h:head>
<script type="text/javascript">
	$(function() {
		var zNodes = ${tree};
		var setting = {
			async : {
				enable : true,
				url : '${contextPath}/manager/menu/menuwithurlforweb_search.do',
				autoParam : [ "id=webId", "isDisabled" ]
			}
		};
		$('#tree').tree(setting, zNodes);
		$('#searchbtn').click(searchGroup);
		$('#searchtext').keyup(function(event) {
			if (event.keyCode == 13) {
				searchGroup();
			}
		});
		
		function searchGroup() {
			var searchText = $('#searchtext').val();
			if ($.trim(searchText) == '') {
				return;
			}
			top.page.location.href = "${contextPath}/manager/website/list.do?webId=-1&searchText="+searchText;
		}
	});

</script>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background-color: #F6F6F6;
}

#searchbtn {
	cursor: pointer;
}

/* .ztree-contextmenu { */
/* 	position: absolute; */
/* 	visibility: hidden; */
/* 	top: 0; */
/* 	cursor: pointer; */
/* 	list-style: none outside none; */
/* 	background-color: #F9F9F9; */
/* 	border: 1px solid #CCC; */
/* 	border-radius: 5px; */
/* 	box-shadow: 1px 2px 1px #EFEFEF; */
/* } */

/* .ztree-contextmenu li { */
/* 	width: auto; */
/* 	min-width: 100px; */
/* 	_width: 100px; */
/* 	line-height: 30px; */
/* 	text-align: left; */
/* 	padding: 0 10px; */
/* } */

/* #jqContextMenu { */
/* 	box-shadow: 2px 2px 1px #EFEFEF; */
/* } */

</style>
</head>
<body>
	<div style="height:30px;background-color:#EFEFEF;padding:10px;border-bottom:1px solid #CCC;">
		<span class="input-append">
			<input id="searchtext" type="text" class="input-text" style="width:180px;" placeholder="请输入网站名称或标识"/><i id="searchbtn" class="iconfont add-on">&#xf5007;</i>
		</span>
	</div>
	<div id="treewrap" style="position:absolute;top:51px;bottom:0;left:0;right:0;overflow:auto;">
		<ul id="tree" class="ztree"></ul>
	</div>
</body>
</html>