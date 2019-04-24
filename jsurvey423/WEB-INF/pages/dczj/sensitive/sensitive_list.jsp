<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<h:head pagetype="page" grid="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<style type="text/css">
 #sensitiveadd {
/*    width: 80px; */
/*    height: 28px; */
   cursor: pointer;
 }
 #sensitivedelete {
/*    width: 80px; */
/*    height: 28px; */
   margin-left: 5px;
   cursor: pointer;
 }
 #sensitiveimport {
/*    width: 80px; */
/*    height: 28px; */
   margin-left: 5px;
   cursor: pointer;
 }
</style>
<script>
$(function() {
	$("#grid_simple_search").attr({maxlength:"50"});
	var html = '${btn}';
	html = html.myReplace('&lt;', '<').myReplace('&gt;', '>').myReplace('&quot;', '"');
	$(".datagrid-toolbar-btn-wrap").html(html);
	$("#sensitiveadd").click(function(){
		openDialog('../sensitive/add_show.do', 550, 200, {
			title : '新增'
		});
	});
	$("#sensitivedelete").click(function(){
		var ids = getCheckedIds();
		if (ids == '') {
			alert('未选中任何记录');
			return;
		}
		confirm('您确定要删除这' + ids.split(',').length + '条记录吗', function() {
			ajaxSubmit('remove.do?ids='+ids, {
				success:function(result){
					if(result.success){
						location.reload();
					}else{
						alert(result.message);
					}
				}
			});
		});
	});
	$("#sensitiveimport").click(function(){
		openDialog('${contextPath}/manager/sensitive/import_show.do', 550, 170, {
			title : '敏感词导入'
		});
	});
});
	function toolbarAction(action) {
		switch (action) {
		case 'remove':
			var ids = getCheckedIds();
			if (ids == '') {
				alert('未选中任何记录');
				return;
			}
			confirm('您确定要删除这' + ids.split(',').length + '条记录吗', function() {
				ajaxSubmit('remove.do?ids='+ids, {
					success:function(result){
						if(result.success){
							location.reload();
						}else{
							alert(result.message);
						}
					}
				});
			});
			break;
		case 'add':
			openDialog('sensitive/add_show.do', 550, 200, {
				title : '新增'
			});
			break;
		case 'import':
			openDialog('sensitive/import_show.do', 550, 170, {
				title : '敏感词导入'
			});
			break;
		}
	}

	function edit(iid, name) {
		openDialog('${contextPath}/manager/sensitive/modify_show.do?iid=' + iid, 550, 200, {
			title : '修改'
		});
	}
	
	String.prototype.myReplace=function(f,e){//吧f替换成e
	    var reg=new RegExp(f,"g"); //创建正则RegExp对象   
	    return this.replace(reg,e); 
	};
</script>
</head>
<body>
<div id="page-title">
	系统管理 / 
	<span id="page-location">
		敏感词管理
	</span>
</div>
	<div id="page-content">
    	<!--高级检索开始form无需url -->
		<div class="grid-advsearch">
			<form>
			</form>
		</div>
        <!--高级检索结束 -->
		<h:grid></h:grid>
	</div>
</body>
</html>