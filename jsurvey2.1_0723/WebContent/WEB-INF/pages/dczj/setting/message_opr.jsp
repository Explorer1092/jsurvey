<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="dialog" validity="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script type="text/javascript">
$(function(){
	$("#oprform").validity(function() {
		$("#content").maxLength(80,"短信内容过长，请控制在80字以内！");
	}, {
		success : function(result) {
			if (result.success) {
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
<form id="oprform" action="${contextPath}/manager/setting/add_sumbit.do">
	<input type="hidden" id="iid" name="iid" value="${messageContent.iid}">
	<input type="hidden" id="dczjid" name="dczjid" value="${messageContent.dczjid}">
	<div style="height: 30px;margin-left: 50px;color: #333333;padding-top: 30px;">编辑短信发送内容：</div>
	<textarea style="width: 400px;height: 150px;margin-left: 50px;margin-right: 50px;" id="content" name="content" 
	required lay-verify="required" placeholder="请输入短信内容" class="layui-textarea">${messageContent.content}</textarea>
	<div style="margin-top: 20px;">
		<button style="margin-right: 37px;float: right;" class="layui-btn layui-btn-normal">保存</button>
	</div>
</form>
</body>
</html>