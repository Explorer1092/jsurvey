<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
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
</style>
</head>
<body>
    <input type="hidden" id="dczj_type" value="0">
    <input type="hidden" id="dczj_state" value="3">
    <input type="hidden" id="webid" value="${webid}">
    <c:if test="${webid != 0}">
	<div class="title-top">
		<button class="layui-btn layui-btn-normal" style="width: 150px;"
			onclick="oprTitleAdd()">创建问卷</button>
	</div>
	</c:if>
	<div class="title-btn">
		<table style="width: 100%">
			<tr>
				<td style="width: 28%"><label class="layui-form-label">问卷类型：</label>
					<div class="layui-btn-group demoTest" style="margin-top: 5px;">
						<button id="jsurvey0" class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_dczj" data-value="0">调查</button>
						<button id="jsurvey1" class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_dczj" data-value="1">征集</button>
						<button id="jsurvey2" class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_dczj" data-value="2">投票</button>
					</div></td>
				<td style="width: 35%"><label class="layui-form-label">状态：</label>
					<div class="layui-btn-group demoTest" style="margin-top: 5px;">
						<button class="layui-btn layui-btn-sm dczj-btn" style="background-color: #5FB878;width: 80px;" data-key="indicator_state" data-type="set" data-value="3">全部</button>
						<button class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_state" data-value="0">正在进行</button>
						<button class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_state" data-value="1">尚未开始</button>
						<button class="layui-btn layui-btn-sm dczj-btn" data-type="set" style="width: 80px;" data-key="indicator_state" data-value="2">已经结束</button>
					</div></td>
				<td style="width: 37%">
					<table style="width: 100%">
						<tr>
							<td style="width: 70px;"><label class="layui-form-label">问卷标题：</label></td>
							<td style="width: 60%"><input type="text" name="title" id="title" class="layui-input" style="width: 95%;height: 32px;" placeholder="请输入标题"></td>
							<td><button class="layui-btn layui-btn-sm layui-btn-normal" style="width: 70px;" onclick="searchtitle();">检索</button></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div class="title-table">
		<table class="layui-hide" id="dczjtable" lay-filter="dczjtableReload"></table>
	</div>
<script src="${contextPath}/resources/dczj/js/jquery.cookie.js"></script>
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script src="${contextPath}/resources/dczj/js/title.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
</body>
</html>