<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>调查征集系统</title>
<h:head pagetype="page"></h:head>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<style type="text/css">
.title-top {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 20px;
	border-bottom: 1px solid #CCC;
}
.title-btn {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 5px;
}
.title-table {
	padding-left: 5px;
}
.dczj-btn {
    width: 80px;
    background-color: #5FB878;
}
.sort-pic {
    margin-right: 8px;
    cursor: pointer;
}
.delete-pic {
    cursor: pointer;
}
.table-pic {
    cursor: pointer;
}
.top-pic {
    margin-left: 8px;
    cursor: pointer;
}
</style>

</head>

<body>
	<div class="title-btn">
		<input type="hidden" id="dczjid" name="dczjid" value="${infoEn.iid}">
	<input type="hidden" id="webid" name="webid" value="${webid }">
	<input type="hidden" id="orderid" name="orderid" value="${infoEn.orderid }">
	<input type="hidden" id="username" name="username"value="${username}">
	<input type="hidden" id="creator" name="creator" value="${creator }">
	<input type="hidden" id="createtime" name="createtime"value="${infoEn.createtime}">
	<table style="width: 500px; margin-top: 20px;">
		<tr style="line-height: 50px;">
			<td class="label" align="right">题库名称：</td>
			<td><input type="text" id="quesbankname" name="quesbankname"
				class="layui-input" style="width: 100%;" value="${infoEn.quesbankname}"
				placeholder="请输入标题"></td>
		</tr>
		
	</table>
	<div id="button-div">
		<input type="button" class="btn btn-primary"  id="btn-submit" value="确认" onclick="toolbarAction('ok')"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="btn" value="取消" id="btn-close" style="margin: 0px;" onclick="closeDialog();" />
	</div>
	</div>

<script src="${contextPath}/resources/dczj/js/jquery.cookie.js"></script>
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script src="${contextPath}/resources/dczj/js/quesbank.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
</body>
</html>