<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="dialog" validity="true" tree="true" dialog="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
.layui-tab-brief>.layui-tab-title .layui-this {
    color: #01AAED;
}
.layui-tab-brief>.layui-tab-more li.layui-this:after, .layui-tab-brief>.layui-tab-title .layui-this:after {
    border: none;
    border-radius: 0;
    border-bottom: 2px solid #01AAED;
}
.inlinediv-body{
	display: inline-block;
	vertical-align: top;
	border: 1px solid #01AAED;
	width: 200px;
	height: 300px;
	background-color:#f6f6f6;
}
.inlinediv-btn{
	display: inline-block;
	vertical-align: top;
	width: 50px;
	height: 300px;
	margin-left: 24px;
	margin-right: 24px;
}
.inlinediv-head{
	display: inline-block;
	vertical-align: top;
	width: 175px;
	height: 20px;
	margin-left: 50px;
	margin-right: 50px;
	margin-bottom: 5px;
}
</style>
<script type="text/javascript">
	$(function() {
		var zNodes = ${tree};
		var setting = {
			treeId:"groupTree",
			async : {
				enable : true,
				url : '${contextPath}/manager/dczj/menu/menulimitopenuserwithurlforgroup_search.do?dczjid='+${dczjid},
				autoParam : [ "id=groupId", "isDisabled" ]
			},
			check:{
				chkStyle:"checkbox",
				enable:true,
				chkboxType: { "Y": "", "N": "" }
			}
		};
		$('#tree').tree(setting, zNodes);
		
		
		
	});

</script>
</head>
<body>
<input type="hidden" id="groups" name="groups" value="${limitOpenUser.groups}">
<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
  <ul class="layui-tab-title">
    <li class="layui-this">按机构</li>
  </ul>
  <div class="layui-tab-content">
  	<div class="inlinediv-head">
  		
  	</div>
  	<div class="inlinediv-head">
  		已选用户
  	</div>
  	<div class="inlinediv-body" style="margin-left: 25px;overflow: scroll;">
  		<ul id="tree" class="ztree"></ul>
  	</div>
  	<div class="inlinediv-btn">
  		<button style="margin-bottom: 5px;margin-top: 65px;height: 30px;line-height: 30px;width: 53px;" class="layui-btn layui-btn-normal"
  		onclick="check();">&gt;</button>
  		<button style="margin-bottom: 30px;margin-left: 0px;height: 30px;line-height: 30px;width: 53px;" class="layui-btn layui-btn-normal"
  		onclick="checkAll();">&gt;&gt;</button>
  		<button style="margin-bottom: 5px;margin-left: 0px;height: 30px;line-height: 30px;width: 53px;" class="layui-btn layui-btn-normal"
  		onclick="removeCheck();">&lt;</button>
  		<button style="margin-left: 0px;height: 30px;line-height: 30px;width: 53px;" class="layui-btn layui-btn-normal"
  		onclick="removeAllCheck();">&lt;&lt;</button>
  	</div>
  	<div class="inlinediv-body" style="margin-right: 25px;overflow: scroll;">
  		<form action="" id="nameform" style="height: 100%;width: 100%;">
  			<input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
  			<input type="hidden" id="limittype" name="limittype" value="0">
  			<table id="nametable"></table>
  		</form>
  	</div>
  	<div style="margin-top: 10px;">
		<button style="margin-right: 37px;float: right;" class="layui-btn layui-btn-normal" onclick="submit();">保存</button>
	</div>
  </div>
</div>
</body>
<script type="text/javascript">
$(function(){
	var groups = ${limitOpenUser.groups};
	
	if(groups){
		var str = "";
		//var groups = $("#groups").val();
		$.each(groups,function(index,group){
			str += "<tr id=\""+group.iid+"\"><td nowrap=\"nowrap\" style=\"height: 27px;line-height: 27px;\"><input name=\"names\" value=\""
			+group.iid+"\" id=\"name_"+group.iid+"\" type=\"checkbox\"><label for=\"name_"+group.iid+"\" style=\"cursor: pointer;\">"+group.name+"</label><td></tr>";
		});
		$("#nametable").html(str);
	}
})

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
function check(){
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.getCheckedNodes(true);
	var idStr = "";
	var nameStr = "";
	$.each(nodes,function(index,node){
		nameStr += "<tr id=\""+node.id+"\"><td nowrap=\"nowrap\" style=\"height: 27px;line-height: 27px;\"><input name=\"names\" value=\""
		+node.id+"\" id=\"name_"+node.id+"\" type=\"checkbox\"><label for=\"name_"+node.id+"\" style=\"cursor: pointer;\">"+node.name+"</label><td></tr>";
	});
	$("#idform").html(idStr);
	$("#nametable").html(nameStr);
}
function checkAll(){
	var idStr = "";
	var nameStr = "";
	$.ajax({
		url : "checkall.do",
		type : "get",
		success : function(jsonResult){
			if(jsonResult.success){
				var nodes = jsonResult.params.nodes;
				$.each(nodes,function(index,node){
					nameStr += "<tr id=\""+node.iid+"\"><td nowrap=\"nowrap\" style=\"height: 27px;line-height: 27px;\"><input name=\"names\" value=\""
					+node.iid+"\" id=\"name_"+node.iid+"\" type=\"checkbox\"><label for=\"name_"+node.iid+"\" style=\"cursor: pointer;\">"+node.name+"</label><td></tr>";
				});
				$("#nametable").html(nameStr);
			}else{
				alert(jsonResult.message);
			}
		}
	});
}
function removeCheck(){
	var namesid = "";
	var names = $("input[name='names']:checked");
	
	$.each(names,function(index,nameinput){
		$("#"+$(nameinput).val()).remove();		
	});
	
}
function removeAllCheck(){
	$("#nametable").html("");
}
function submit(){
	var dczjid = $("#dczjid").val();
	var limittype = $("#limittype").val();
	var names = "";
	var nameArray = $("input[name='names']");
	$.each(nameArray,function(index,nameinput){
		names += $(nameinput).val()+",";		
	});
	names = names.substring(0,names.length-1);
	$.ajax({
		type : "post",
		url : "${contextPath}/manager/setting/limitopen_submit.do",
		aysc : false,
		cache : false,
		data : {dczjid:dczjid,names:names,limittype:limittype},
		success : function(jsonResult){
			if (jsonResult.success) {
				closeDialog(true);
			} else {
				alert(jsonResult.message);
			}
		}
	});
}
</script>
</html>