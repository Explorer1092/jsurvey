<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="page" grid="true"></h:head>
</head>
<style type="text/css">
 #webadd {
   cursor: pointer;
 }
 #webdelete {
   margin-left: 5px;
   cursor: pointer;
 }
 
</style>
<script type="text/javascript">
$(function() {
	var html = '${btn}';
	html = html.myReplace('&lt;', '<').myReplace('&gt;', '>').myReplace('&quot;', '"');
	$(".datagrid-toolbar-btn-wrap").html(html);
	$("#webadd").click(function(){
		openDialog('../website/add_show.do?pid=${webId}', 550, 480, {
			title : '网站新增'
		});
	});
	$("#webdelete").click(function(){
		var ids = getCheckedIds();
		if (ids == "") {
			alert("未选中任何记录");
			return;
		}
		confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
			ajaxSubmit("remove.do?pid=${webId}&ids=" + ids,
					{
						success : function(result) {
							if (result.success) {
								/* Tree.getInstance().removeNode(
										result.params.remove); */
								parent.location.reload();
							} else {
								alert(result.message);
							}
						}
					});
		});
	});
});

	function toolbarAction(action) {
		switch (action) {
		case 'remove':
			var ids = getCheckedIds();
			if (ids == "") {
				alert("未选中任何记录");
				return;
			}
			confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
				ajaxSubmit("remove.do?pid=${webId}&ids=" + ids,
						{
							success : function(result) {
								if (result.success) {
									Tree.getInstance().removeNode(
											result.params.remove);
									location.reload();
								} else {
									alert(result.message);
								}
							}
						});
			});
			break;
		case 'add':
			openDialog('../website/add_show.do?pid=${webId}', 550, 480, {
				title : '网站新增'
			});
			break;
		}
	}

	function edit(iid, name) {
		openDialog('../website/modify_show.do?iid=' + iid, 550, 480, {
			title : '网站编辑'
		});
	}
	
	String.prototype.myReplace=function(f,e){//吧f替换成e
	    var reg=new RegExp(f,"g"); //创建正则RegExp对象   
	    return this.replace(reg,e); 
	};
</script>
<body>
	<div id="page-title">
		系统管理 / <span id="page-location">网站管理</span>
	</div>
	<div id="page-content">
		<h:grid></h:grid>
	</div>
</body>
</html>