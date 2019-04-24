<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="page" validity="true" codeedit="true" tip="true"></h:head>
<style type="text/css">
#toolbar {
	width: 100%;
	height: 55px;
	text-align: center;
	line-height: 55px;
	padding-left: 20px;
	position: fixed;
	left: 0;
	bottom: 0;
	border-top: 1px solid #F6F8FA;
	position: fixed;
	background-color: #E8E8E8;
	z-index: 100;
}

td {
	white-space: nowrap;
	word-break: keep-all;
}

.opr_left_td {
	color: #000000;
	background-color: #f4f8ff;
	padding-right: 3px;
}

.opr_center_td {
	color: #000000;
}

.opr_right_td {
	color: #aaaaaa;
	background-color: #ffffff;
	padding-left: 10px;
}

.opr_tr {
	color: #000000;
}
</style>
<script type="text/javascript">
	var jacteditor = "";
	$(function() {
		jacteditor = $("#template").codeedit();

		$("#oprform").validity(function() {

		}, {
			success : function(result) {
				if (result.success) {
					back();
				} else {
					alert(result.message);
				}
			}
		});
	});

	function back() {
		location.href = '${contextPath}/manager/datacall/list.do?webId=${webId}';
	}

	function insertAtCursor(tag) {
		var value = '<!--' + tag + '-->';
		jacteditor.replaceSelection(value);
	}
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform">
		<input type="hidden" name="iid" value="${en.iid}"> <input
			type="hidden" name="datacallid" value="${en.datacallid}"> <input
			type="hidden" name="type" value="${en.type}">
		<div id="page-title">
			数据调用 / <span id="page-location">模板样式</span>
		</div>
		<div id="dialog-content">
			<table class="table">
				<tr>
					<td class="label" align="right" style="width: 120px;">模板标签：</td>
					<td>
						<fieldset style="border-width: 1px; border-color: #DDDDDD; width: 780px;">
							<legend><b><font color="#43A3F3" style="cursor: pointer;" title="标签只有放在<!--dczj_start-->与<!--dczj_end-->之间才会生效">模板样式标签</font></b></legend>
							<table width="100%" style="padding-left: 5px; padding-right: 5px;" border="0">
								<tr>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_titie')"><b><font color="#333333">标题</font></b></td>
									<td width="50px;"></td>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_state')"><b><font color="#333333">状态</font></b></td>
									<td width="50px;"></td>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_href')"><b><font color="#333333">跳转地址</font></b></td>
								</tr>
								<tr style="height: 20px;"></tr>
								<tr>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_time')"><b><font color="#333333">创建时间</font></b></td>
									<td width="50px;"></td>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_starttime')"><b><font color="#333333">开始时间</font></b></td>
									<td width="50px;"></td>
									<td class="opr_left_td"  align="center" style="cursor:pointer;" onclick="insertAtCursor('dczj_endtime')"><b><font color="#333333">结束时间</font></b></td>
								</tr>
							</table>
						</fieldset></td>
				</tr>
			</table>
			<table class="table" style="margin-bottom: 55px;margin-top: 20px;">
				<tr>
					<td class="label" align="right" style="width: 120px;">模板样式：</td>
					<td><div style="height: 300px; width: 800px; border-style: solid; border-width: 1px 1px 1px 1px;border-color: #DDDDDD;">
							<textarea style="height: 300px;" id="template" name="template">${en.template}</textarea>
						</div></td>
				</tr>
			</table>
		</div>
		<div id="toolbar">
			<input type="submit" class="btn btn-primary" value="保存" /> <input
				type="button" class="btn" value="返回" onclick="back();" />
		</div>
	</form>
</body>
</html>