<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>机构列表</title>
<h:head pagetype="page" grid="true"></h:head>
<style type="text/css">
	#menu-wrap {
	position: absolute;
	z-index: 10;
	border-right: 3px solid #EFEFEF;
	}
</style>
<script type="text/javascript">
	function toolbarAction(action) {
		switch (action) {
		case 'remove':
			var ids = getCheckedIds();
			if (ids == "") {
				alert("未选中任何记录");
				return;
			}
			confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
				ajaxSubmit("${contextPath}/manager/group/remove.do?pid=${groupId}&ids=" + ids, {success:function(result){
					if(result.success){
						//Tree.getInstance().removeNode(result.params.remove);
						//location.reload();
						parent.location.reload();
					}else{
						alert(result.message);
					}
				}});
			});
			break;
		case 'add':
			openDialog('${contextPath}/manager/group/add_show.do?pid=${groupId}', 550, 480, {
				title : '机构新增'
			});
			break;
		case 'import':
			openDialog('${contextPath}/manager/group/import_show.do', 550, 170, {
				title : '机构导入'
			});
			break;
		case 'export':
			var ids = getCheckedIds();
			iframeSubmit("${contextPath}/manager/group/export.do?ids=" + ids + "&pid=${groupId}");
			break;
		}
	}

	function edit(iid, name) {
		openDialog('${contextPath}/manager/group/modify_show.do?iid=' + iid, 550, 480, {
			title : '机构编辑'
		});
	}
</script>
</head>
<body >
	<div id="page-title">
		系统管理 / 
		<c:if test="${groupId == 0}">
			<span id="page-location">机构管理</span>
		</c:if>
		<c:if test="${groupId > 0 && groupName != ''}">
			机构管理  / <span id="page-location">${groupName}</span>
		</c:if>
		<c:if test="${groupId<0}">
			机构管理  / <span id="page-location">检索结果</span>
		</c:if>
	</div>
	<div id="page-content">
		<h:grid></h:grid>
	</div>
</body>
</html>