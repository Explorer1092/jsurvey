<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>预览页面</title>
<h:head pagetype="page"></h:head>
<style type="text/css">
html {
	height: 100%
}

.ques-title {
	height: 51px;
	background-color: #FFFFFF;
	width: 100%;
	position: fixed;
	z-index: 99;
}

.ques-title-table {
	width: 100%;
	height: 50px;
	border-bottom: 1px solid #CCC;
}

#phonepic {
	background-repeat: no-repeat;
	background-size: 100% 100%;
}
</style>
</head>
<body id="body" style="margin: 0px; font-size: 16px; background-color: #e6e6e6; height: 100%">
	<input type="hidden" name="pcstyle" id="pcstyle" value="${pcstyle }">
	<input type="hidden" name="ispublish" id="ispublish" value="${ispublish }">
	<input type="hidden" name="state" id="state" value="${state }">
	<div class="ques-title">
		<table class="ques-title-table">
			<tr>
				<td style="width: 25%;"></td>
				<td style="width: 25%; text-align: right; vertical-align: middle;">
					<div id="pcdiv" style="height: 32px; line-height: 32px; cursor: pointer;" onclick="dopreview(1);">
						<img id="pcimg" src="${contextPath}/resources/dczj/images/publish/pcgray.png" style="vertical-align: middle;">电脑端预览
					</div>
				<td
					style="width: 25%; text-align: left; vertical-align: middle; line-height: 30px;">
					<div id="phonediv" style="height: 32px; line-height: 32px; cursor: pointer;" onclick="dopreview(0);">
						<img id="phoneimg" src="${contextPath}/resources/dczj/images/publish/phonegray.png" style="vertical-align: middle;">手机端预览
					</div>
				</td>
				<td style="width: 25%;" align="right"><input type="button" class="btn" onclick="closepreview();" value="关闭"></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		var pcstyle = $("#pcstyle").val();
		if (pcstyle == 1) {
			$("#pcimg").attr("src","${contextPath}/resources/dczj/images/publish/pcblue.png");
			$("#pcdiv").css("color", "#1E9FFF");
		} else if (pcstyle == 0) {
			$("#phoneimg").attr("src","${contextPath}/resources/dczj/images/publish/phoneblue.png");
			$("#phonediv").css("color", "#1E9FFF");
		}
		function dopreview(type) {
			if (type == 1) {
				$("#pcimg").attr("src","${contextPath}/resources/dczj/images/publish/pcblue.png");
				$("#phoneimg").attr("src","${contextPath}/resources/dczj/images/publish/phonegray.png");
				$("#pcdiv").css("color", "#1E9FFF");
				$("#phonediv").css("color", "");
				location.href = "dopreview.do?dczjid=${dczjid}&pcstyle=1&webid=${webid}";
			} else if (type == 0) {
				$("#pcimg").attr("src","${contextPath}/resources/dczj/images/publish/pcgray.png");
				$("#phoneimg").attr("src","${contextPath}/resources/dczj/images/publish/phoneblue.png");
				$("#pcdiv").css("color", "");
				$("#phonediv").css("color", "#1E9FFF");
				location.href = "dopreview.do?dczjid=${dczjid}&pcstyle=0&webid=${webid}";
			}
		}

		function closepreview() {
			location.href = "titlelist.do?webid=${webid}";
		}
		
		function jsurveysubmitvalidate(){
			alert("预览状态下不可以投票");
		}
		
        function phonejsurveysubmitvalidate(){
        	alert("预览状态下不可以投票");
		}
	</script>
	<c:if test="${pcstyle == 1}">
		<div style="width: 100%; position: absolute; top: 51px; z-index: 1">
			${strContent}</div>
	</c:if>
	<c:if test="${pcstyle == 0}">
		<script type="text/javascript">
			var bodyheight = $("#body").height();
			$("#phonepic").css('height', bodyheight - 51);
		</script>
		<div style="padding-top: 51px; background-color: #e6e6e6;">
			<div id="phonepic" style="width: 400px;height: 540px;margin: auto;top: 0; left: 0; bottom: 0; right: 0;background-image: url(${contextPath}/resources/dczj/images/u34.png) ">
				<div style="padding-top: 70px; padding-left: 45px; padding-right: 45px; padding-bottom: 70px; height: 400px;width: 310px;">
					<div style="height: 100%;width: 99%;overflow-y: scroll;">${phoneStrContent}</div>
				</div>
			</div>
		</div>
	</c:if>
</body>
</html>