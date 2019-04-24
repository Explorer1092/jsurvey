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
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script type="text/javascript">
$(function(){
	$("#form").validity(function() {
		var str=document.getElementsByName("jumpbtn"); 
		var objarray=str.length;
		if(str[2].checked == true){
			$("#jumpurl").match('url', '应用全路径必须为url格式');
			$("#btnname").maxLength(8, "按钮字数不能超过8个字");
		}
		$("#thankscontent").maxLength(85, "感谢内容不能超过85个字");
	},{
	success:function(result){
		if(result.success){
			closeDialog(true);
			parent.location.reload();
		}else{
			alert(result.message);
		}
	}
	});
})
</script>
<style type="text/css">
	.layui-form-checkbox[lay-skin=primary]:hover i {
	    border-color: #01AAED;
	    color: #fff;
	}
	.layui-form-checked[lay-skin=primary] i {
	    border-color: #01AAED;
	    background-color: #01AAED;
	    color: #fff;
	}
</style>
</head>
<body>
<form class="layui-form" id="form" action="add_thankscontent.do">
	<input style="height: 0px;border: 0px;display: none;" id="jumpbtnval" value="${thanksSetting.jumpbtn}">
	<input style="height: 0px;border: 0px;display: none;" name="dczjid" id="dczjid" value="${dczjid}">
	<div style="height: 30px;margin-left: 50px;color: #333333;padding-top: 20px;font-weight: bold;">编辑感谢内容：</div>
	<textarea style="width: 350px;height: 85px;margin-left: 50px;margin-right: 100px;min-height: 85px;" id="thankscontent" name="thankscontent" 
	required lay-verify="required" placeholder="请输入感谢内容" class="layui-textarea">${thanksSetting.thankscontent}</textarea>
	<div style="height: 30px;margin-left: 50px;color: #333333;padding-top: 15px;font-weight: bold;">设置前往按钮：</div>
	<div style="padding-left: 50px;">
		<input type="checkbox" name="jumpbtn" value="0" title="查看结果" lay-skin="primary">
		<input type="checkbox" name="jumpbtn" value="1" title="参与抽奖" lay-skin="primary"> 
	</div>
	<div style="padding-left: 50px;padding-top: 15px;" onclick="isShowBtn();"> 
		<input type="checkbox" name="jumpbtn" value="2" title="前往其他页面" lay-skin="primary">
	</div>
	<div id="btn" class="layui-form-item"  style="padding-left: 50px;padding-top: 15px;visibility: hidden;">
		<div class="layui-inline">
			<div class="layui-input-inline" style="width: 150px;">
				<input type="text" id="btnname" name="btnname" value="${thanksSetting.btnname}"  placeholder="输入按钮名称" autocomplete="off" class="layui-input">
			</div>
			<div class="layui-input-inline" style="width: 150px;">
				<input type="text" id="jumpurl" name="jumpurl" value="${thanksSetting.jumpurl}"  placeholder="请输跳转页面地址" autocomplete="off" class="layui-input">
			</div>
		</div>
	</div>
	<div style="margin-top: 20px;">
		<button style="margin-right: 37px;float: right;" class="layui-btn layui-btn-normal">保存</button>
	</div>
</form>
</body>
<script type="text/javascript">
$(function(){
	var jumpbtn = $("#jumpbtnval").val();
	if(jumpbtn){
		var array = jumpbtn.split(",");
		var arraylength = array.length;
		for(j=0;j<arraylength;j++){
			setChecked("jumpbtn",array[j]);
		}
	}
	
	function setChecked(name, value){
		var str=document.getElementsByName(name); 
		var objarray=str.length;
		for(i=0;i<objarray;i++){
			if(str[i].value == value){
				str[i].checked = true;
				if(i == 2){
					$("#btn").css("visibility","visible");
				}
			}
		}
	}
})

function isShowBtn(){
	var btnval = $("#btn").css("visibility");
	if(btnval == 'hidden'){
		$("#btn").css("visibility","visible");
	}else{
		$("#btn").css("visibility","hidden");
		$("#btnname").val("");
		$("#jumpurl").val("");
	}
	
}

layui.use('element', function(){
	  var element = layui.element;

	});
	layui.use('form', function(){
		  var form = layui.form;
		  
		  //监听提交
		  /*form.on('submit(formDemo)', function(data){
		    layer.msg(JSON.stringify(data.field));
		    return false;
		  });*/
		});
</script>
</html>