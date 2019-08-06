<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>树形菜单</title>
<h:head pagetype="page" tree="true" highlighter="true"></h:head>
<script type="text/javascript">
	$(function(){
		SyntaxHighlighter.config.clipboardSwf = '${contextPath}/ui/widgets/highlighter/scripts/clipboard.swf';
		SyntaxHighlighter.all();
		var zNodes = ${tree};
		var zTreeSetting = {
				view: {
					// 增加新增按钮
					addHoverDom: function(treeId, treeNode){
						var sObj = $("#" + treeNode.tId + "_span");
						if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
						var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='新增' onfocus='this.blur();'></span>" 
							+ "<span class='button edit' id='editBtn_" + treeNode.tId + "' title='修改' onfocus='this.blur();'></span>" 
							+ "<span class='button remove' id='removeBtn_" + treeNode.tId + "' title='删除' onfocus='this.blur();'></span>";
						sObj.after(addStr);
						var addBtn = $("#addBtn_"+treeNode.tId);
						if (addBtn) addBtn.bind("click", function(){
							// 这里处理新增点击事件
							alert('按我了新增');
							return false;
						});
						var editBtn = $("#editBtn_"+treeNode.tId);
						if (editBtn) {
							editBtn.bind("click", function(){
								// 这里处理修改点击事件
								alert('按我了修改');
								return false;
							});
						}
						var removeBtn = $("#removeBtn_"+treeNode.tId);
						if (removeBtn) {
							removeBtn.bind("click", function(){
								// 这里处理删除点击事件
								alert('按我了删除');
								return false;
							});
						}
					},
					removeHoverDom: function (treeId, treeNode) {
						$("#addBtn_"+treeNode.tId).unbind().remove();
						$("#editBtn_"+treeNode.tId).unbind().remove();
						$("#removeBtn_"+treeNode.tId).unbind().remove();
					},
					selectedMulti: false
				},
				callback:{
					// 点击删除按钮触发
					beforeRemove:function(treeId, treeNode){
						alert('点击了删除');
						console.info(treeId);
						console.info(treeNode);
						// 返回值 false/true，false不修改，如果使用自己的逻辑和效果，此处返回false
						return false;
					},
					// 点击编辑按钮触发
					beforeEditName:function(treeId, treeNode){
						alert('点击了编辑');
						console.info(treeId);
						console.info(treeNode);
						console.info(newName);
						// 返回值 false/true，false不修改，如果使用自己的逻辑和效果，此处返回false
						return false;
					}
				}
		};
		$("#tree").tree(zTreeSetting, zNodes);
	});
</script>
<style type="text/css">
body{
	overflow-x: hidden;
	overflow-y: auto;
}
</style>
</head>
<body>
	<div id="page-title">
		开发指南 / <span id="page-location">${treeNodeName}</span>
	</div>
	<div id="page-content">
		<ul id="tree" class="ztree"></ul>
		<h3>头</h3>
		<pre class='brush:html'>
			&lt;h:head tree="true"&gt;&lt;/h:head&gt;
		</pre>
		<h3>JS</h3>
		<pre class='brush:javascript'>
		$(function(){
			// 树的节点的json数据
			var zNodes = "";
			//	第一个参数为设置项，可以参考ztree的api，第二个为节点的json数据
			$("#我是html dom 的 id").tree(null, zNodes);
		});
		</pre>
		<h3>HTML</h3>
		<pre class='brush:html'>
			<!-- id必须指定，class必须为ztree -->
			<ul id="tree" class="ztree"></ul>
		</pre>
		<h3>JAVA</h3>
		<pre class='brush:java'>
			// 创建一个树实例
			Tree tree = Tree.getInstance();
			// 创建一个树实例  并且设定 节点的id和name在url上的参数名称
			Tree tree = Tree.getInstance("treeNodeId","treeNodeName");
			
			// 插入一个根节点
			// 使用tree.addNode增加节点
			// 使用TreeNode.getInstance创建节点
			tree.addNode(TreeNode.getInstance("cities", null, "全国城市"));
			
			// 插入子节点
			tree.addNode(TreeNode.getInstance("beijing", "cities", "北京(无链接)"));
			tree.addNode(TreeNode.getInstance("tianjin", "cities", "天津(无链接)"));
			tree.addNode(TreeNode.getInstance("nanjing", "cities", "南京(无链接)"));
			
			// 插入子节点 带链接
			tree.addNode(TreeNode.getInstance("jianye", "nanjing", "建邺区"));
			
			// 对于带链接的node，如果需要接参数比如  url为listxxxx.do  参数为 personId=9&personName=lj
			TreeNode.getInstance("jianye", "nanjing", "建邺区", "listxxxx.do").addParam("persionId", 9).addParam("personName", "lj");
			// 将节点list转换为json数据
			// 将节点的json数据带入 view (jsp)
			modelAndView.addObject("tree", tree.parse());
		</pre>
	</div>
</body>
</html>