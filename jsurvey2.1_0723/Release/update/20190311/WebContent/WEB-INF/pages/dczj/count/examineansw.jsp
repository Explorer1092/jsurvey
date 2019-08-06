<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="dialog" dialog="true" editor="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
</head>
<body>
<div id="dialog-content">
	<div style="font-weight: bold; height: 35px;line-height: 35px;margin-left: 50px;">答案详情</div>
	<div style="margin-left: 50px;">
	<script type="text/plain" id=answcontent name="answcontent">${contentReco.answcontent}</script> <script>var editorOption = { initialFrameWidth :580, initialFrameHeight : 150, wordCount : false,elementPathEnabled : false,toolbars : [ [ 'source', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline', 'forecolor','|', 'rowspacingtop', 'rowspacingbottom', 'lineheight', '|', 'fontfamily', 'fontsize', '|', 'link', 'unlink', '|','insertimage' ] ]}; ueditor = UE.getEditor('answcontent',editorOption); </script>
	</div>
<%-- 	<textarea id="answcontent" name="answcontent" style="width: 450px;margin-left: 50px;" placeholder="请输入" class="layui-textarea">${contentReco.answcontent}</textarea> --%>
	<div style="font-weight: bold;height: 35px;line-height: 35px;margin-left: 50px;">编辑回复</div>
	<div style="margin-left: 50px;">
	<script type="text/plain" id=replycontent name="replycontent">${contentReco.replycontent}</script> <script>var editorOption = { initialFrameWidth : 580, initialFrameHeight : 150, wordCount : false,elementPathEnabled : false,toolbars : [ [ 'source', '|', 'undo', 'redo', '|', 'bold', 'italic', 'underline','forecolor','|', 'rowspacingtop', 'rowspacingbottom', 'lineheight', '|','fontfamily', 'fontsize','|', 'link', 'unlink', '|','insertimage' ] ]}; ueditor = UE.getEditor('replycontent',editorOption); </script>
	</div>
<%-- 	<textarea id="replycontent" name="replycontent" style="width: 450px;margin-left: 50px;" placeholder="请输入" class="layui-textarea">${contentReco.replycontent}</textarea> --%>
	<div style="margin-top:20px;margin-left: 220px;margin-bottom: 20px;">
		<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="save(${contentReco.iid})">保存</button>
		<c:choose>
			<c:when test="${contentReco.audi == '1'}">
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="setUnAudi(${contentReco.iid})">置为未审核</button>
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="saveopenly(${contentReco.iid})">置为已公开</button>
			</c:when>
			<c:when test="${contentReco.audi == '2'}">
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="setUnAudi(${contentReco.iid})">置为未审核</button>
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="saveAndAudi(${contentReco.iid})">置为已审核</button>
			</c:when>
			<c:otherwise>
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="saveAndAudi(${contentReco.iid})">置为已审核</button>
				<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="saveopenly(${contentReco.iid})">置为已公开</button>
			</c:otherwise>
		</c:choose>
		
		<!-- <button class="layui-btn layui-btn-normal layui-btn-sm" onclick="dispass(${contentReco.iid})">不通过</button> -->
	</div>
</div>
</body>
<script type="text/javascript">
	function save(contentid){
		var uu = UE.getEditor('replycontent',editorOption);
		var replycontent = uu.getContent().replace("<p>","").replace("</p>","");

		$.ajax({
			url : "savereplycontent.do",
			type : "post",
			data : {
				contentid : contentid,
				replycontent : replycontent
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					closeDialog();
					parent.location.reload();
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	};
	function setUnAudi(contentid){
		$.ajax({
			url : "unaudi.do",
			type : "post",
			data : {
				contentid : contentid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					closeDialog();
					parent.location.reload();
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	};
	function saveAndAudi(contentid){	
		var uu = UE.getEditor('replycontent',editorOption);
		var replycontent = uu.getContent().replace("<p>","").replace("</p>","");
		$.ajax({
			url : "saveandaudi.do",
			type : "post",
			data : {
				contentid : contentid,
				replycontent : replycontent
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					closeDialog();
					parent.location.reload();
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	};
	
	function saveopenly(contentid){	
		var uu = UE.getEditor('replycontent',editorOption);
		var replycontent = uu.getContent().replace("<p>","").replace("</p>","");
		$.ajax({
			url : "saveopenly.do",
			type : "post",
			data : {
				contentid : contentid,
				replycontent : replycontent
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					closeDialog();
					parent.location.reload();
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	};
	
	
	function dispass(contentid){
		$.ajax({
			url : "dispass.do",
			type : "post",
			data : {
				contentid : contentid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					closeDialog();
					parent.location.reload();
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	};
</script>
</html>