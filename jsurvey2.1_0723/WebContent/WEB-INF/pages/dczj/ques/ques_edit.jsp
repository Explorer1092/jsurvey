<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
<title></title>
<h:head pagetype="page" validity="true" toggle="true" highlighter="true" editor="true" select = "true" codeedit="true" tip="true" ></h:head>
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
   margin-left: 25px;
   margin-right: 25px;
   min-height: 100%;
}
.row1-title {
	width: 100%;
	height: 50px;
	line-height: 50px;
	text-align: center;
	border-bottom: solid 1px #CCC;
}

.row-button {
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

.active-button:hover {
	background-color: #308ef2;
	color: #FFF;
	border-color: #308ef2;
}

.ui-draggable {
	width: 220px;
	height: 34px;
	color: #666666;
	font-family: "微软雅黑";
	font-size: 13px;
	margin-bottom: 10px;
	margin-top: 10px;
	margin-left: 30px;
	cursor: pointer;
	border: solid 1px #CCC;
	vertical-align: middle;
}

.toggle-on {
	background-color: #01AAED;
}

.setting-ul {
	padding-top: 5px;
	margin-left: 20px;
	padding-left: 20px;
	margin-right: 20px;
	padding-bottom: 5px;
	border-bottom: 1px solid #CCC;
}

.setting-li {
	height: 24px;
	line-height: 24px;
	padding-top: 5px;
	padding-bottom: 5px;
}

.setting-span {
	height: 24px;
	line-height: 24px;
	font-size: 15px;
	vertical-align: bottom;
	margin-left: 15px;
	font-weight: bold;
}

.li-radio {
	padding-left: 30px;
}

.li-radio img{
	margin-left: 1px;
}

.li-font {
	font-weight: bold;
	font-size: 17px;
}

.surveylist {
    padding-left: 10px;
	margin-left: 50px;
	margin-right : 50px;
	border: solid 1px #3185ce;
	cursor: pointer;
	overflow:show;
}
.survey{
   padding-top: 20px;
   padding-bottom: 20px;
}
.surveybutton {
	height: 30px;
	margin-left: 50px;
	margin-right: 50px;
}

.surveyAnsw {
	margin-left: 50px;
	margin-right: 50px;
	margin-top: 20px;
    padding-bottom : 20px;
	border: solid 1px #3185ce;
}

.surveysort {
	width: 40px;
	margin-left: 500px;
}
.answ_img{
   width: 100px;
   height: 100px;
}

.survey_listbg{
   font-size: 18px;
   line-height:24px;
   margin:0px;
   padding:0px;
}
.span-ques-text{
   font-size: 14px;
   height: 30px;
   line-height: 30px;
   padding : 8px;
}
.row1-bottom {
	width: 100%;
	height: 45px;
	line-height: 45px;
	text-align: center;
}

.layui-form-radio>i:hover, .layui-form-radioed>i {
    	color: #01AAED;
	}
#edui1{
	z-index:99!important;
}
</style>
<script type="text/javascript">
    var styleEditor ="";
	$(function() {
		$("#isprogress").toggle();
		$("#istitlenumber").toggle();
		$("#isopencontent").toggle();
		$("#isshowscore").toggle();
		
		var divheight = $("#rows1").height();
		var contentheight = divheight - 430;
		
        var xssz = $("#xssz").outerHeight();
        if(xssz <= 200){
        	contentheight = divheight - 400;
        }else{
        	contentheight = divheight - 440;
        }
		
		
        $("#div-content").css('height',contentheight);	
		styleEditor = $("#styleeditor").codeedit();
		$(".CodeMirror-wrap").css('height',contentheight);
		$("#displaysettings").css('display','none');
		$("#oprform").validity(function() {
		}, {
			success : function(result) {
				if (result.success) {
					window.location.reload();
				} else {
					alert(result.message);
				}
			}
		});
	});
	function restoredefault(){
		var defaultcssstyle = $("#defaultcssstyle").val();
		$("#styleeditor").val(defaultcssstyle);
		styleEditor.setValue(defaultcssstyle);
	}
	function allAdd(){
		openDialog('${contextPath}/manager/dczj/all_add.do?dczjid=${dczjid}',600,500,{
				title : '从题库批量添加'
		}); 
	}
</script>
</head>
<body>
<c:if test="${type == 3}"><span style="float:right; padding-right: 45px"id="score${dczjid}">测评总分：${sumscore}分</span><span style="height:20px;float:right;vertical-align: bottom;"></span><span style="float:right; padding-right: 30px" id="num${dczjid}">测评总数：${num}题</span></c:if>
${configEn.cssstyle}
    <input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
    <div style="display:none;"><textarea name="defaultcssstyle" id="defaultcssstyle">${defaultcssstyle }</textarea></div>
	<div style="background-color: #f0f0f0;height: 100%">
		<div id="rows1">
			<div class="row1-title">
				<input id="button-select" class="row-button active-button" type="button" value="选项设置" onclick="quessettings(0)" /> 
				<input id="button-display" class="row-button" type="button" value="显示设置" onclick="quessettings(1)" />
			</div>
			<div id="selectques" style=" height:90%;width:100%; overflow:auto">
			<c:if test="${type == 3}">
				<div class="ui-draggable" onclick="addques(6);">
					<!-- <div style="position: absolute; padding-top: 6px; padding-left: 35px;"> -->
					<div style="display: inline-block;padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/radio.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">测评单选题</div>
				</div>
				<div class="ui-draggable" onclick="addques(7);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/checkbox.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">测评多选题</div>
				</div>
				</c:if>
				<div class="ui-draggable" onclick="addques(3);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/u2172.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">文字说明</div>
				</div>
				<div class="ui-draggable" onclick="addques(0);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2178.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">单选题</div>
				</div>
				<div class="ui-draggable" onclick="addques(1);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2180.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">多选题</div>
				</div>
				<div class="ui-draggable" onclick="addques(5);">
					<div style="display: inline-block; padding-top: 8px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2182.png" />
					</div>
					<div
						style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">单行文本题</div>
				</div>
				<div class="ui-draggable" onclick="addques(2);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/u2184.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">多行文本题</div>
				</div>
				<div class="ui-draggable" onclick="addques(8);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/u2184.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">下拉题</div>
				</div>
				<div class="ui-draggable" onclick="addques(9);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;"
							src="${contextPath}/resources/dczj/images/u2184.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">联动题</div>
				</div>
				<div class="ui-draggable" onclick="addques(4);">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2186.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;">分页</div>
				</div>
				<div style="margin-left: 30px;width: 220px;">
					<hr color="#ccc">
				</div>
				<div class="ui-draggable">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2188.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;" onclick="surveyimport();">导入</div>
				</div>
				<div class="ui-draggable">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2190.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;" onclick="surveyexport();">导出</div>
				</div>
				<div class="ui-draggable">
					<div style="display: inline-block; padding-top: 6px; padding-left: 35px;">
						<img style="width: 20px;" src="${contextPath}/resources/dczj/images/u2190.png" />
					</div>
					<div style="display: inline-block;padding-top: 8px; padding-left: 5px; text-align: left;" onclick="allAdd();">批量添加</div>
				</div>
			</div>
			<div id="displaysettings" style="height: 100%;">
				<form action="${url}" id="oprform" class="layui-form" lay-filter="setting-form">
				    <input type="hidden" value="${configEn.iid}" id="iid" name="iid">
				    <input type="hidden" value="${configEn.dczjid}" id="dczjid" name="dczjid">
				    <div id="xssz" >
					<ul class="setting-ul">
						<li class="setting-li"><input type="hidden" value="${configEn.isprogress}" id="isprogress" name="isprogress" /><span class="setting-span">显示答题进度</span></li>
						<li class="setting-li"><input type="hidden" value="${configEn.istitlenumber}" id="istitlenumber" name="istitlenumber" /><span class="setting-span">显示题目编号</span></li>
						<c:if test="${type == 3}">
						<li class="setting-li"><input type="hidden" value="${configEn.isshowscore}" id="isshowscore" name="isshowscore" /><span class="setting-span">显示题目分值</span></li>
						</c:if>
					</ul>
					<ul class="setting-ul">
						<li class="li-font">控件ICON</li>
						<li class="li-radio"><input type="radio" name="chooseframe_style" value="0" title="默认"></li>
						<li class="li-radio"><input type="radio" name="chooseframe_style" value="1"
							title="<img src='${contextPath}/resources/dczj/images/chooseframe/radio1.png'><img src='${contextPath}/resources/dczj/images/chooseframe/radiocheck1.png'><img src='${contextPath}/resources/dczj/images/chooseframe/check1.png'><img src='${contextPath}/resources/dczj/images/chooseframe/checkcheck1.png'>"></li>
						<li class="li-radio"><input type="radio" name="chooseframe_style" value="2"
							title="<img src='${contextPath}/resources/dczj/images/chooseframe/radio2.png'><img src='${contextPath}/resources/dczj/images/chooseframe/radiocheck2.png'>"></li>
					</ul>
					<ul class="setting-ul">
						<li class="setting-li"><input type="hidden" value="${configEn.isopencontent}" id="isopencontent" name="isopencontent" /><span class="setting-span">公开填空文字结果</span></li>
						<li class="setting-li">允许填空字数：<input type="text" id="contentsize" name="contentsize" class="input-text" value="${configEn.contentsize}" style="width: 30px;margin-right: 10px;">字</li>
					</ul>
					</div>
					<div id="div-content">
					<textarea id="styleeditor" name="cssstyle" style="">${configEn.cssstyle}</textarea>
					</div>
					<div class="row1-bottom" style="margin-top:10px;">
					   <input class="row-button active-button" style="width: 90px;" type="submit" value="确认" />
					   <input class="row-button active-button" style="width: 90px;" type="button" value="恢复默认" onclick="restoredefault();"/> 
					</div>
				</form>
			</div>
		</div>
		<div id="rows2"><div id="rows3">${content}</div></div>
	</div>
	<script>
	    var form;
		layui.use([ 'form' ], function() {
			form = layui.form;
			form.val("setting-form", {
				"chooseframe_style": '${configEn.chooseframe_style}' 
			})
			form.on('select(aihao)', function(data){
				  console.log(data.elem); //得到select原始DOM对象
				  console.log(data.value); //得到被选中的值
				  console.log(data.othis); //得到美化后的DOM对象
				}); 
			
		
		});
// 	    $(function(quesid,answid){
// 		    var rightAnsw = ${isright};
// 		    if(rightAnsw == 1 ){
// 			    $("#rightAnsw_"+quesid+"_"+answid).prop("checked", true );
// 		    } 
// 	   });   
	</script>
	<script src="${contextPath}/resources/dczj/js/quesEdit.js"></script>
</body>
</html>