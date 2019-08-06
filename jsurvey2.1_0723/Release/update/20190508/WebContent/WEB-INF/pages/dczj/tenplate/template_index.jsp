<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<h:head cookie="true"></h:head>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
<style type="text/css">
html,body {
	margin: 0px;
	height: 100%;
}
#rows1 {
	width: 300px;
	height: 100%;
	margin-left: 10px;
	position: fixed;
	background-color: #ffffff;
	border-right: solid 1px #CCC;
	vertical-align: middle;
}
#rows2 {
    padding-top:0px; 
	padding-left: 312px;
	height: 100%
}

#rows3 {
   background-color: #ffffff;
   /**margin-left: 40px;
   margin-right: 40px;**/
   min-height: 100%;
}
.row1-title {
	width: 100%;
	height: 40px;
	line-height: 40px;
	text-align: center;
	border-bottom: solid 1px #CCC;
}

.row1-bottom {
	width: 100%;
	height: 50px;
	line-height: 50px;
	text-align: center;
	margin-top:10px;
}

.row-button {
	margin: 0;
	padding: 0;
	border: 1px solid #FFF;
	outline: none;
	height: 30px;
	width: 100px;
	background-color: #FFF;
	cursor: pointer;
}

.row-button-application {
	background-color: #1E9FFF;
	color: #FFF;
}

.row-button-custom {
	background-color: #1E9FFF;
	color: #FFF;
}

.row1-conten {
	border-bottom: solid 1px #CCC;
	overflow: auto;
	background-color: #f0f0f0;
	cursor: pointer;
}

.styimg {
	border: 1px solid #CCC;
	width: 198px;
	height: 120px;
	margin-left: 50px;
	margin-top: 20px;
	margin-bottom: 20px;
	background-color: #FFF;
}

.styimg-img {
	width: auto;
	height: auto;
	width: 198px;
	height: 100px;
	cursor：pointer;
}
.icon-btn{
  text-align: center;
}
.icon-img{
   width: 20px;
   margin-right: 5px;
   cursor: pointer;
}
</style>
<script type="text/javascript">
	$(function(){
		$(".styimg").css("border","1px solid #CCC").css("box-shadow","");
		var selectTemplate = sessionStorage.getItem("selectTemplate");
		$("#styimg"+selectTemplate).css("border","2px solid #5CB85C").css("box-shadow","1px 1px 10px #000000");
	})
</script>
</head>
<body>
    <input type="hidden" id="type" name="type" value="${type}">
    <input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
    <input type="hidden" id="commontemplateid" name="commontemplateid">
    <input type="hidden" id="contextPath" name="contextPath" value="${contextPath}">
    <div style="height: 100%;">
	<div id="rows1">
		<div class="row1-title">
			<span style="color: #666">模板演示选择</span>
		</div>
		<div id="div-content" class="row1-conten">
			<ul>
				<li>
				    <div id="styimgdczj1" class="styimg" onclick="styleimgcheck('dczj1',this);" onmouseover="displayimg(this)" onmouseout="hiddenimg(this)" title="此套模板默认模板" >
				    <input id="dczj1" name="styleimgradio" style="display: none" type="radio" value="dczj1">
				    <img class="styimg-img" alt="" src="${contextPath}/resources/dczj/images/template/1.png">
					<div class="icon-btn" style="display: none;">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u88.png" class="icon-img" onclick="editTemplate('dczj1');">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u86.png" class="icon-img" onclick="downloadtemplate('dczj1');">
<%-- 					  <img alt="" src="${contextPath}/resources/dczj/images/template/u80.png" class="icon-img"> --%>
					</div>
					</div>
				</li>
	<%-- 			<c:if test="${templateList != NULL}">
				<li>
				    <div class="styimg" onclick="styleimgcheck('dczj2',this);" onmouseover="displayimg(this)" onmouseout="hiddenimg(this)" title="此套模板为上次使用的模板">
				    <input id="dczj2" name="styleimgradio" style="display: none" type="radio" value="dczj2">
				    <img class="styimg-img" alt="" src="${imagepath}">
					<div class="icon-btn" style="display: none;">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u88.png" class="icon-img" onclick="editTemplate('dczj2');">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u86.png" class="icon-img" onclick="downloadtemplate('dczj2');">
					</div>
					</div>
				</li>
				</c:if> --%>
				<c:if test="${commonTemplateStyleList != null }">
				<c:forEach var="en" items="${commonTemplateStyleList }">
				<li>
				    <div id="styimg${en.iid}" class="styimg" onclick="styleimgcheck('${en.iid}',this);" onmouseover="displayimg(this)" onmouseout="hiddenimg(this)" title="此套模板为常用模板">
				    <input id="${en.iid}" name="styleimgradio" style="display: none" type="radio" value="${en.iid}">
				    <img class="styimg-img" alt="" src="${contextPath}/template/commontemplate_${en.iid}/image/${en.styleimgname}">
					<div class="icon-btn" style="display: none;">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u88.png" class="icon-img" onclick="editTemplate('${en.iid}');">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u86.png" class="icon-img" onclick="downloadtemplate('${en.iid}');">
					  <img alt="" src="${contextPath}/resources/dczj/images/template/u80.png" class="icon-img" onclick="deleteTemplate('${en.iid}');">
					</div>
					</div>
				</li>
				</c:forEach>
				</c:if>
			</ul>
		</div>
		<div class="row1-bottom">
			<input id="button-application" class="row-button row-button-application" type="button" value="应用" onclick="applicationtemplate();"/>
			<input id="button-custom" class="row-button row-button-custom" type="button" value="自定义" onclick="custom();"/>
		</div>
	</div>
	<script src="${contextPath}/resources/dczj/js/template.js"></script>
	<script type="text/javascript">
	 layui.use('layer', function(){
		  var layer = layui.layer;
		});
	 $(function(){
		 $("#iframepage").css('height',$("#rows3").height()-5);
	 })
	</script>
	<div id="rows2"><div id="rows3">
	<iframe src="${contextPath}/manager/tempalte/iframetemplate.do?commoncomplateid=dczj1&dczjid=${dczjid}&type=0" name="iframepage" id="iframepage" frameborder="0" style="width: 100%;"></iframe>
	</div></div>
	</div>
</body>
</html>