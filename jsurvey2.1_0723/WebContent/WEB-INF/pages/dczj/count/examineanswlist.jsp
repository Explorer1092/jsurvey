<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page" dialog="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0"> 
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
	.layui-form-checkbox[lay-skin=primary]:hover i {
		border-color: #1E9FFF;
		color: #fff
	}
	
	.layui-form-checked[lay-skin=primary] i {
		border-color: #1E9FFF;
		background-color: #1E9FFF;
		color: #fff
	}
	
	.layui-laypage .layui-laypage-curr .layui-laypage-em {
		position: absolute;
		left: -1px;
		top: -1px;
		padding: 1px;
		width: 100%;
		height: 100%;
		background-color: #1E9FFF
	}
	
	.layui-table-body {
	    position: relative;
	    margin-right: -1px;
	    margin-bottom: -1px;
	    overflow: hidden;
	}
</style>
</head>
<body>
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;">查看答案</div>
</div>
<div style="height: 40px;width: 100%;">
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="setexamine()" style="margin-left: 15px;margin-top: 5px;">置为已审核</button>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="setopenly()" style="margin-left: 15px;margin-top: 5px;">置为已公开</button>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="exportExcel()" style="margin-left: 15px;margin-top: 5px;">导出</button>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="remove()" style="margin-left: 15px;margin-top: 5px;">删除</button>
</div>
<input type="hidden" id="starttime" value="${starttime}">
<input type="hidden" id="endtime" value="${endtime}">
<table id="examineanswer" lay-filter="test"></table>
</body>
<script type="text/javascript">
 layui.use('table', function(){	 
	  var dczjid = ${dczjid};
	  var answid = ${answid};
	  var quesid = ${quesid};
	  var starttime = $("#starttime").val();
	  var endtime = $("#endtime").val();
	  var table = layui.table;
	 
	  //第一个实例
	  table.render({
		id: 'answertable'  
	    ,elem: '#examineanswer'
	    ,url: 'examineanswertable.do' //数据接口
	    ,page: true
	    ,where: {dczjid:dczjid,answid:answid,quesid:quesid,starttime:starttime,endtime:endtime,num:Math.random()}
	    ,cols: [[ //表头
	    	{type:'checkbox',field: 'contentid', width:'3%'}
	        ,{field: 'answcontent', title: '答案详情', width:'37.2%', align: 'center'}
	        ,{field: 'audistate', title: '审核状态', width:'30%', align: 'center'}
	        ,{field: 'replystate', title: '回复', width:'30%', align: 'center'}
	    ]]
	  });
}); 
function setexamine(){
	var table = layui.table;
	var checkStatus = table.checkStatus('answertable');
	var ids = "";
	$.each(checkStatus.data ,function(index,item){
		if(index == checkStatus.data.length-1){
			ids = ids+item.contentid;
		}else{
			ids = ids+item.contentid+",";
		}
		
	});
	if(checkStatus.data.length > 0){
		$.ajax({
			url : "updateexamine.do",
			type : "post",
			data : {
				ids : ids
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					table.reload('answertable', {
						  url: 'examineanswertable.do?num='+Math.random()
						});
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	}
};

//置为已公开
function setopenly(){
	var table = layui.table;
	var checkStatus = table.checkStatus('answertable');
	var ids = "";
	$.each(checkStatus.data ,function(index,item){
		if(index == checkStatus.data.length-1){
			ids = ids+item.contentid;
		}else{
			ids = ids+item.contentid+",";
		}
		
	});
	if(checkStatus.data.length > 0){
		$.ajax({
			url : "updateopen.do",
			type : "post",
			data : {
				ids : ids
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					table.reload('answertable', {
						  url: 'examineanswertable.do?num='+Math.random()
						});
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	}
};

function remove(){
	var table = layui.table;
	var checkStatus = table.checkStatus('answertable');
	var ids = "";
	$.each(checkStatus.data ,function(index,item){
		if(index == checkStatus.data.length-1){
			ids = ids+item.contentid;
		}else{
			ids = ids+item.contentid+",";
		}
		
	});
	if(checkStatus.data.length > 0){
		$.ajax({
			url : "removeanswer.do",
			type : "post",
			data : {
				ids : ids
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					table.reload('answertable', {
						  url: 'examineanswertable.do?num='+Math.random()
						});
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		});
	}
};
function openExaminePage(contentid){
	openDialog('${pageContext.request.contextPath}/manager/count/showexaminepage.do?contentid='+contentid,700,550,{title:'答案详情'});
};
function exportExcel(){
	var table = layui.table;
	var checkStatus = table.checkStatus('answertable');
	var ids = "";
	$.each(checkStatus.data ,function(index,item){
		if(index == checkStatus.data.length-1){
			ids = ids+item.contentid;
		}else{
			ids = ids+item.contentid+",";
		}
		
	});
	iframeSubmit("exportansandrep.do?dczjid=${dczjid}&answid=${answid}&quesid=${quesid}&starttime=${starttime}&endtime=${endtime}&ids="+ids); 
}
</script>
</html>