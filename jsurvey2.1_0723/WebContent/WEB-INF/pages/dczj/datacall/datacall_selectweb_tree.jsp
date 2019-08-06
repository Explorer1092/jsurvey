<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="dialog" validity="true" tree="true"></h:head>
<script type="text/javascript">
	$(function() {
		var jact_webids = '${jact_webids}';
		var zNodes = ${tree};
		var setting = {
			view : {
				showLine : true,
				selectedMulti : false
			},
			check : {
				enable : true
			},
			data : {
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "pId"
				}
			},
			async : {
				enable : true,
				url : '${contextPath}/manager/datacall/menuforgroupsearch.do?jact_webids='+jact_webids,
				autoParam : [ "id=webId", "isDisabled"]
			}
		};
		$("#tree").tree(setting, zNodes);
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		treeObj.setting.check.chkboxType = {'Y' : '','N' : ''};
// 		treeObj.expandAll(true);
		
		$("#oprform").validity(function(){
			$('#menuIds').assert(function(){
				var treeObj = $.fn.zTree.getZTreeObj("tree");
				var nodes = treeObj.getCheckedNodes(true);
				var menuIds = "";
				var menuNames = "";
				$(nodes).each(function(){
					menuIds += $(this).prop("id") + ",";
					menuNames += $(this).prop("name") + ",";
				});
				if(menuIds.length > 0){
					menuIds = menuIds.substring(0, menuIds.length - 1);
				}
				if(menuNames.length > 0){
					menuNames = menuNames.substring(0, menuNames.length - 1);
				}
				$('#menuIds').val(menuIds);
				getParentWindow().document.getElementById('jact_webids').value = menuIds;
				getParentWindow().document.getElementById('webnames').value = menuNames;
				return true;
			}, '请选择网站');
		},{
			success:function(result){
				if(result.success){
					closeDialog();
				} else {
					alert(result.message);
				}
			}
		});
		
	});
	
	
</script>
</head>
<body>
	<form action="${url}" method="post" id="oprform">
		<input type="hidden" id="menuIds" name="menuIds" value="" />
		<div style="padding-top: 0px; overflow-y: auto; height: 300px;">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div id="dialog-toolbar" style="border-top: 1px solid #CCC;">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" /> 
				<input type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>