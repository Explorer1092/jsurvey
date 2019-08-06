<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:head cookie="true" dialog="true" message="true" iconfont="true" menu="true" tree="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script type="text/javascript">
$(function(){
	var treeMenu = ${tree};
    $('#webtree').menu({
		tree : 'rangemenu',
		height : 200,
		init : function() {
			setting('rangemenu', onClickRange, onDbClickRange, treeMenu);
		}
	});
	function onClickRange(event, treeId, treeNode) {
		$('#webId').val(treeNode.id);
		$('#webName').val(treeNode.name);
		$('#webid').val(treeNode.id);
		$('#webtree').val(treeNode.name);
		$('#webtree_menu').fadeOut(50);
	}
	function setting(treeName, onClickFunction, onDblClickFunction, rootNode) {
		var setting = {
			async : {
				enable : true,
				url : '${contextPath}/manager/system/sync_loadtree.do',
				autoParam : [ "id=webid", "isDisabled" ]
			},
			callback : {
				beforeClick : beforeClick,
				beforeDblClick : beforeClick,
				onClick : onClickFunction,
				onDblClick : onDblClickFunction
			}
		};
		$("#" + treeName).tree(setting, rootNode);
		$("#" + treeName).tree().refreshNode('');
	}
	function onDbClickRange(event, treeId, treeNode) {
		if (treeNode == null) {
			return;
		}
		if (treeNode.isDisabled)//根节点及失效节点双击无效
			return;
		$('#webid').val(treeNode.id);
		$('#webtree').val(treeNode.name);
	}
	function beforeClick(treeId, treeNode, clickFlag) {
		if (treeNode.isDisabled)
			return false;
		return (treeNode.id != 0);
	}
	
	//数据调用树
	/* var treeMenu_datacall = ${tree_datacall};
    $('#webtree_datacall').menu({
		tree : 'rangemenu_datacall',
		height : 200,
		init : function() {
			setting_datacall('rangemenu_datacall', onClickRange_datacall, onDbClickRange_datacall, treeMenu_datacall);
		}
	});
	function onClickRange_datacall(event, treeId, treeNode) {
		if(treeNode.id == 0){
			$('#webtree_datacall_menu').fadeOut(50);
		}else{
			$('#webid_datacall').val(treeNode.id);
			$('#webtree_datacall').val(treeNode.name);
			$('#webtree_datacall_menu').fadeOut(50);
			var pagetype = $('#pagetype_datacall').val();
			changePage(pagetype);
		}
	}
	function setting_datacall(treeName_datacall, onClickFunction_datacall, onDblClickFunction_datacall, rootNode_datacall) {
		var setting_datacall = {
			async : {
				enable : true,
				url : '${contextPath}/manager/websiteconfig/sync_loadtree_datacall.do',
				autoParam : [ "id", "isDisabled" ]
			},
			callback : {
				beforeClick : beforeClick_datacall,
				beforeDblClick : beforeClick_datacall,
				onClick : onClickFunction_datacall,
				onDblClick : onDblClickFunction_datacall
			}
		};
		$("#" + treeName_datacall).tree(setting_datacall, rootNode_datacall);
		$("#" + treeName_datacall).tree().refreshNode('');
	}
	function onDbClickRange_datacall(event, treeId, treeNode) {
		if (treeNode == null) {
			return;
		}
		if (treeNode.isDisabled)//根节点及失效节点双击无效
			return;
		$('#webid_datacall').val(treeNode.id);
		$('#webtree_datacall').val(treeNode.name);
	}
	function beforeClick_datacall(treeId, treeNode, clickFlag) {
		if (treeNode.isDisabled)
			return false;
		return (treeNode.id != 0);
	}
	nodisplayweb(); */
});
</script>

<style type="text/css">
	.layui-bg-main{background-color:#172951!important}
	.layui-input{height: 30px;}
</style>
</head>
<body class="layui-layout-body" >
	<input type="hidden" id="webId">
	<input type="hidden" id="webName">
	<div class="layui-layout layui-layout-admin">
	  <div class="layui-header layui-bg-main">
	    <div class="layui-logo layui-bg-main" style="width:200px;">
	    	<span id="logo" style="font-size:18px;"><font color="#5CB85C"><b>JSURVEY</b></font></span>
	    	<span style="font-size:16px;"><font color="#eeeeee"><b>调查征集系统 </b></font></span>
	    </div>
	    <!-- 头部区域（可配合layui已有的水平导航） -->
	    <ul class="layui-nav layui-layout-left layui-bg-main">
	    	<li class="layui-nav-item"><a href="${contextPath}/manager/index.do">我的调查</a></li>
 	    	<li class="layui-nav-item" id="cptk"><a href="${contextPath}/manager/QuesBank.do">测评题库</a></li>
	    </ul>
	    <ul class="layui-nav layui-layout-right">
	      <li class="layui-inline" style="padding-right: 20px;">
	      	<input type="text" id="webtree" class="layui-input" readonly = "readonly" value="${webname}">
	      </li>
	      <%-- <li class="layui-inline" style="padding-right: 20px;">
	      	<input type="text" id="webtree_datacall" class="layui-input" readonly = "readonly" value="">
	      </li> --%>
	      <li class="layui-inline" style="width: 60px;cursor: pointer;"><img alt="系统设置" width="30px;" src="${contextPath}/resources/dczj/images/u10.png"></li>
	      <li class="layui-nav-item">
	        <a href="javascript:;">您好，${user.name}</a>
	        <dl class="layui-nav-child">         
	          <dd><a style="cursor: pointer;" onclick="openDialog('${contextPath}/manager/user/modify_self_show.do',500,515,{title:'账户设置'})">账户设置</a></dd>
	          <dd><a style="cursor: pointer;" onclick="openDialog('${contextPath}/manager/onlineuser/list.do',500,515,{title:'在线用户'})">在线用户</a></dd>
	          <dd><a style="cursor: pointer;" onclick="top.location.href='${contextPath}/logout.do'">系统退出</a></dd>
	        </dl>
	      </li>
	    </ul>
	  </div>
	</div>
	
	<div class="layui-side layui-bg-black layui-bg-main" style="top:60px;">
	    <div class="layui-side-scroll">
	      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
	      <ul class="layui-nav layui-nav-tree layui-bg-main"  lay-filter="test">
	        <li class="layui-nav-item layui-nav-itemed">
	          <a class="" href="javascript:;">系统管理</a>
	          <dl class="layui-nav-child">
	            <dd><a style="cursor: pointer;" onclick="var webId = $('#webId').val();var webName = $('#webName').val();changePage(encodeURI('${contextPath}/manager/website/list.do?webId='+webId+'&webName='+webName));display();nodisplayweb();">&nbsp;&nbsp;网站管理</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/datacall/list.do');nodisplay();displayweb();">&nbsp;&nbsp;数据调用</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/system/grouplist.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;机构管理</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/system/userlist_show.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;用户管理</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/sensitive/list.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;敏感词管理</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/log/logtable.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;操作日志</a></dd>
	          </dl>
	        </li>
	        <li class="layui-nav-item">
	          <a href="javascript:;">系统参数</a>
	          <dl class="layui-nav-child">
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/configuration/modify_show.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;参数设置</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/configuration/email/modify_show.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;邮件设置</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/banlist/list.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;封停管理</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/system/dbpool/info.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;数据库连接</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/system/info.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;线程</a></dd>
	            <dd><a style="cursor: pointer;" onclick="changePage('${contextPath}/manager/configuration/log/info.do');nodisplay();nodisplayweb();">&nbsp;&nbsp;日志设置</a></dd>
	          </dl>
	        </li>
	        <!-- <li class="layui-nav-item"><a href="">云市场</a></li>
	        <li class="layui-nav-item"><a href="">发布商品</a></li> -->
	      </ul>
	    </div>
	  </div>
	
	<div class="layui-body" style="top:60px;">   
	   	<iframe src="${contextPath}/manager/website/list.do" name="page" id="page" frameborder="0" style="height: 98%;width: 100%;"></iframe>
	</div>
</body>
<script type="text/javascript" src="${contextPath}/resources/dczj/js/systemManager.js"></script>
<script>

layui.use('element', function(){
  var element = layui.element;

});
</script>
</html>