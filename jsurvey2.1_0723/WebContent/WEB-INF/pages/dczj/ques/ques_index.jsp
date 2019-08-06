<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<h:head pagetype="page" cookie="true"></h:head>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script src="${contextPath}/resources/dczj/js/quesindex.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
<style type="text/css">
html, body {
	margin: 0px;
	height: 100%;
	width: 100%;
	overflow-y:hidden;
}

.ques-title {
	height: 51px;
	background-color: #FFFFFF;
	margin-left: 10px;
	margin-right: 10px;
}

.ques-title-td {
	height: 48px;
	width: 120px;
	text-align: center;
	color: #666666;
	cursor: pointer;
	font-size: 18px;
	border-bottom: 1px solid #CCC;
}

.ques-title-td-active {
	color: #0080DD;
	border-bottom: 1px solid #0080DD;
}

.ques-title-td:hover {
	height: 48px;
	width: 120px;
	text-align: center;
	color: #0080DD;
	cursor: pointer;
	font-size: 18px;
	border-bottom: 1px solid #0080DD;
}

.ques-title-table {
	width: 100%;
	height: 50px;
	border-bottom: 1px solid #CCC;
}

.layui-btn {
    display: inline-block;
    height: 30px;
    line-height: 30px;
    padding: 0 18px;
    background-color: #009688;
    color: #fff;
    white-space: nowrap;
    text-align: center;
    font-size: 14px;
    border: none;
    border-radius: 2px;
    cursor: pointer;
}
</style>
</head>
<body>
	<input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
	<div style="height: 100%">
		<div class="ques-title">
			<table class="ques-title-table">
				<tr>
					<td id="ques-page0" class="ques-title-td"
						onclick="changePage('${contextPath}/manager/dczj/quesedit.do?dczjid=${dczjid}','0');">编辑问卷</td>
					<c:if test="${ istemplate == 0}">
					<td id="ques-page1" class="ques-title-td"
					    onclick="changePage('${contextPath}/manager/tempalte/tempalte_show.do?dczjid=${dczjid}','1');">模板样式</td>
					</c:if>	
					<c:if test="${ istemplate == 1}">
					<td id="ques-page1" class="ques-title-td"
					    onclick="changePage('${contextPath}/manager/tempalte/showcustom.do?dczjid=${dczjid}&type=${type}','1');">模板样式</td>
					</c:if>
					<td id="ques-page2" class="ques-title-td"
						onclick="changePage('${contextPath}/manager/setting/page_show.do?dczjid=${dczjid}','2');">设置</td>
					<td id="ques-page3" class="ques-title-td"
						onclick="changePage('${contextPath}/manager/count/page_show.do?dczjid=${dczjid}','3');">数据统计</td>
					<td align="right">
						<button class="layui-btn" style="margin-right: 10px;" onclick="titlepublish(${dczjid})">发布问卷</button>
						<img alt="返回" src="../../resources/dczj/images/u2080.png" style="width: 30px; margin-right: 20px; cursor: pointer;"onclick="backhomepage();">
					</td>
				</tr>
			</table>
		</div>
		<div class="layui-body" id="divquespage" style="top: 50px; left: 0px; background-color: #f0f0f0">
			<iframe src="" name="quespage" id="quespage" frameborder="0" style="width: 100%; height: 99%;"></iframe>
		</div>
		
		<div class="layui-body" id="divquespage1" style="top: 50px; left: 0px; background-color: #f0f0f0;display: none;">
			<iframe src="" name="quespage1" id="quespage1" frameborder="0" style="width: 100%; height: 99%;" scrolling="no"></iframe>
		</div>
	</div>
	<script type="text/javascript">
	var dczjid = $("#dczjid").val();
	$.ajax({
		type : "post",
		url : "getquessession.do",
		data : {
			"dczjid" : dczjid
		},
		async : false,
		cache : false,
		success : function(result) {
			if (result.success) {
				var url = result.params.url;
				var type = result.params.type;
				if (url != "" && url != null && type != "" && type != null) {
					changePage(url, type);
				} else {
					url = '${contextPath}/manager/dczj/quesedit.do?dczjid=${dczjid}';
					type = 0;
					changePage(url, type);
				}
			} else {
				alert("操作失败，请联系管理员！");
			}
		}
	})
	</script>
</body>
</html>