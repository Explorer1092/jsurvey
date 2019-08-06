<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<h:head pagetype="page" codeedit="true" validity="true"></h:head>
<link type="text/css" rel="stylesheet"
	href="${contextPath}/resources/dczj/layui/css/layui.css">
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
    overflow-y: hidden;
}
#rows2 {
    padding-top:0px; 
	padding-left: 312px;
	height: 100%;
 	overflow-y: hidden;
}

#rows3 {
   background-color: #ffffff;
/*    margin-left: 40px;
   margin-right: 40px; */
   min-height: 100%;
   text-align: center;
   overflow-y:hidden;
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
	height: 75px;
	line-height: 75px;
	text-align: center;
	margin-top:20px;
}

.row1-content {
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
	max-width: 100%;
	max-height: 100%;
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

.row-button-title {
	margin: 0;
	padding: 0;
	border: 1px solid #CCC;
	outline: none;
	height: 30px;
	width: 100px;
	background-color: #FFF;
	cursor: pointer;
}

.active-button {
	background-color: #1E9FFF;
	color: #FFF;
	border-color: #1E9FFF;
}

.row-button-title:hover {
	background-color: #308ef2;
	color: #FFF;
	border-color: #308ef2;
}
</style>
<script type="text/javascript">
var pcEditor ="";
var phoneEditor ="";
$(function(){
	pcEditor = $("#pceditor").codeedit(); //创建codemirror编辑器对象
	phoneEditor = $("#phoneeditor").codeedit(); //创建codemirror编辑器对象
	$(".CodeMirror-wrap").css('height',$("#pc-div-content").height());
//	var type = $("#pagetype").val();
	var type = sessionStorage.getItem("type");
	if(type == null){
		type = 0;
	}
	if(type == 0){
		$("#pc-div-content").css('display','');
		$("#phone-div-content").css('display','none');
	}else if(type == 2){
		$("#pc-div-content").css('display','none');
		$("#phone-div-content").css('display','');
	}
	selectTemplate(type);
	$("#oprform").validity(function() {

	}, {
		success : function(result) {
			if (result.success) {
				sessionStorage.setItem("type",$("#templatetype").val())
				window.location.href='${contextPath}/manager/tempalte/showcustom.do?dczjid=${dczjid}&type=${type}';
			} else {
				alert(result.message);
			}
		}
	});
});

function selectTemplate(type) {
	$("#templatetype").val(type);
	var val = Math.random();
	if(type == 0){
		$("#pc-div-content").css('display','');
		$("#button-pc").addClass("active-button");
		$("#phone-div-content").css('display','none');
		$("#button-phone").removeClass("active-button");
		$("#iframepage").css("width","100%");
		$("#iframepage").attr("src","${contextPath}/manager/tempalte/iframecustomtemplate.do?dczjid=${dczjid}&type=0&val="+val);
	}else if(type == 2){
		$("#pc-div-content").css('display','none');
		$("#button-pc").removeClass("active-button");
		$("#phone-div-content").css('display','');
		$("#button-phone").addClass("active-button");
		$("#iframepage").attr("src","${contextPath}/manager/tempalte/iframecustomtemplate.do?dczjid=${dczjid}&type=2&val="+val);
		$("#iframepage").css("width","500px");
	}
}

function newchange() {
	window.location.href='${contextPath}/manager/tempalte/tempalte_show.do?dczjid=${dczjid}';
}

function restoredefault(){
	var type = $("#templatetype").val();
	if(type == 0){
		var pcDefaultPage = $("#pcDefaultPage").val();
		$("#pceditor").val(pcDefaultPage);
		pcEditor.setValue(pcDefaultPage);
	}else if(type == 2){
		var phoneDefaultPage = $("#phoneDefaultPage").val();
		$("#phoneeditor").val(phoneDefaultPage);
		phoneEditor.setValue(phoneDefaultPage);
	}
}

function displaybutton(){
	if($("#savecommon").css('display') == 'none'){
		$("#savecommon").css('display','');
	}else{
		$("#savecommon").css('display','none');
	}
	
}

function beforecommonsubmit(){
	var pcPageCode = pcEditor.getValue();
	var phonePageCode = phoneEditor.getValue();
	openDialog('tempalte/showcommontemplateimport.do?dczjid=${dczjid}&type=${type}&pcPageCode='+encodeURIComponent(pcPageCode)+'&phonePageCode='+encodeURIComponent(phonePageCode), 400, 200,{
	    	title : '上传图片'
	});
// 	$.post('showcommontemplateimport.do', {
// 		dczjid : '${dczjid}',
// 		type : '${type}',
// 		pcPageCode : pcPageCode,
// 		phonePageCode : phonePageCode
// 	}, function(str) {
// 		layer.open({
// 			type : 1,
// 			title : '自定义模板',
// 			area : [ '450px', '250px' ],
// 			content : str,
// 			btn : [ '确认添加', '放弃编辑' ],
// 			yes : function(index, layero) {
// 				$("#oprform").submit();
// 			}
// 		})
// 	})
}

function commonsubmit(){
	var pcPageCode = pcEditor.getValue();
	var phonePageCode = phoneEditor.getValue();
    $.ajax({
		type : "post",
		url : "${contextPath}/manager/tempalte/commontemplatesubmit.do",
		data : {
			dczjid : '${dczjid}',
			type : '${type}',
			pcPageCode : pcPageCode,
			phonePageCode : phonePageCode
		},
		async : false,
		cache : false,
		success : function(result) {
			if (result.success) {
				window.location.href='${contextPath}/manager/tempalte/showcustom.do?dczjid=${dczjid}&type=${type}';
			}
		}
	})
}
</script>
</head>
<body>
 <input type="hidden" id="pagetype" value="0">
 <div style="height: 100%;">
	<div id="rows1">
		<div class="row1-title">
			<input id="button-pc" onclick="selectTemplate(0);" class="row-button-title active-button" type="button" value="电脑端" /> 
			<input id="button-phone" onclick="selectTemplate(2);" class="row-button-title" type="button" value="移动端" />
		</div>
		<form action="${url}" method="post" id="oprform">
		<input type="hidden" name="dczjid" value="${dczjid}">
		<input type="hidden" name="type" value="${type}">
		<input type="hidden" id="templatetype" name="templatetype" value="0">
		<div style="display:none;"><textarea name="pcDefaultPage" id="pcDefaultPage">${pcDefaultPage }</textarea></div>
		<div style="display:none;"><textarea name="phoneDefaultPage" id="phoneDefaultPage">${phoneDefaultPage }</textarea></div>
     	<div id="div-content" class="row1-content" style="overflow:hidden;">
			<div id="pc-div-content" style="height: 100%;width: 100%;">
			<textarea id="pceditor" name="pcPageCode" style="display:none;">${pcPageCode}</textarea>
			</div>
			<div id="phone-div-content" style="height: 100%;width: 100%;">
			<textarea id="phoneeditor" name="phonePageCode" style="display:none;">${phonePageCode}</textarea>
			</div>
		</div>
		<div class="row1-bottom">
		   <div style="position: fixed;margin-left: 10px;margin-top: 5px;height: 63px;">
		   <div style="height: 30px;line-height: 30px;width: 93px;">
		   <input class="row-button-title active-button" style="width: 60px;vertical-align:middle;margin-right: 1px;" type="submit" value="保存" /><img onclick="displaybutton();" style="height: 30px;cursor: pointer;" src="${contextPath}/resources/dczj/images/template/u8888.png"> 
		   </div>
		   <div id="savecommon" style="height: 30px;line-height: 30px;width: 93px;margin-top: 3px;display: none;">
		   <input class="row-button-title active-button" style="width: 93px;vertical-align:middle;margin-right: 1px;font-size: 11px;" type="button" value="保存为常用模板" onclick="beforecommonsubmit();"/>
		   </div>
		   </div>
		   <div style="height: 30px;line-height: 30px;position: fixed;margin-left: 110px;margin-top: 6px;">
		   <input class="row-button-title active-button" style="width: 90px;" type="button" value="恢复默认" onclick="restoredefault();"/> 
		   <input class="row-button-title active-button" style="width: 90px;" type="button" value="重新选择" onclick="newchange();"/> </div>
		</div>
		</form>
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
	<iframe src="${contextPath}/manager/tempalte/iframecustomtemplate.do?dczjid=${dczjid}&type=0&templateid=${templateid}&uuid=${uuid}" name="iframepage" id="iframepage" frameborder="0" style="width: 100%;"></iframe>
	</div></div>
	</div>
</body>
</html>