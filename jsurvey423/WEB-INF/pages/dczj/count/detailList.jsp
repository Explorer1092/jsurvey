<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page"  calendar="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
	html{
		height: 99%;
		width: 99%;
	}
	body {
		height: 100%;
		width: 100%;
	}
	.layui-table-body {
		overflow-x: hidden;
	}
</style>
</head>
<body>
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;">参与明细</div>
</div>
<div style="height: 40px;width: 100%;padding-top: 10px;">
	<span style="padding-left: 15px;">选择时间：</span>
	<input type="hidden" id="dczjid" value="${dczjid}">
	<input type="text" id="starttime" name="starttime" value="${starttime}" class="jcalendar input-text" style="width:150px;" 
			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endtime\')}'})" readonly="readonly">
	<span>-</span>
	<input type="text" id="endtime" name="endtime" value="${endtime}" class="jcalendar input-text" style="width:150px;" 
			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'starttime\')}'})" readonly="readonly">
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="reload();" style="margin-left: 15px;">检索</button>
	<button class="layui-btn layui-btn-danger layui-btn-sm" onclick="removeAll()" style="margin-left: 15px;margin-right: 15px;float: right;">清空数据</button>
	<button class="layui-btn layui-btn-warm layui-btn-sm" onclick="toolbarAction('export')" style="margin-left: 15px;float: right;">导出EXCEL</button>
</div>
<table id="detail" lay-filter="test"></table>
</body>
<script type="text/javascript">
layui.use('table', function(){
	  var table = layui.table;
	  //第一个实例
	  table.render({
	    elem: '#detail'
	    //,height: 312
	    ,url: 'list.do' //数据接口
	    ,page: true //开启分页
	    ,where: {randcode:Math.random()*10000,dczjid:${dczjid}}
	    ,cols: [[ //表头
	      {field: 'iid', title: 'ID', width:'5.3%', sort: true, align: 'center'}
	      ,{field: 'unid', title: 'COOKIED', width:'35%', align: 'center'}
	      ,{field: 'ip', title: '参与人IP', width:'20%', align: 'center'} 
	      ,{field: 'type', title: '提交来源', width: '20%', align: 'center'}
	      ,{field: 'createdate', title: '提交时间', width: '20%', align: 'center'}
	      //,{field: 'delete', title: '删除', width: '10%', align: 'center'}
	    ]]
	  });
	  
	});
	function reload(){
		var table = layui.table;
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		table.reload('detail', {
			  url: 'list.do' //数据接口
			  ,where: {starttime:starttime,endtime:endtime}
			});
	};

	function toolbarAction(action){
		switch (action){
		case 'export':
			var starttime = $("#starttime").val();
			var endtime = $("#endtime").val();
			iframeSubmit("exportdetailnew.do?dczjid=${dczjid}&starttime="+starttime+"&endtime="+endtime); 
			break;
		}
	};
	/* function remove(id){
		var flag = confirm("您确认要删除本条数据吗",function(){
			$.ajax({
				type : "post",
				url : "remove.do",
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(result){
					if(result.isSuccess == 1){
						reload();
					}else{
						alert("删除失败，请联系系统管理员");
					}
				}
			});
		});
	}; */
	function removeAll(){
		var dczjid = $('#dczjid').val();
		confirm("这项操作会清空明细数据，您确认执行吗",function(){
			$.ajax({
				type : "post",
				url : "removeall.do",
				dataType : "json",
				data : {
					"dczjid" : dczjid
				},
				success : function(result){
					if(result.success){
						alert("删除成功");
						reload();
					}else{
						alert(result.message);
					}
				}
			});
		});
		
	};
	function showdetail(id){
		parent.changePage('${contextPath}/manager/count/showdetail.do?id='+id);
	};
</script>
</html>