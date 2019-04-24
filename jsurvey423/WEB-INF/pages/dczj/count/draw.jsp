<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
hr {
    height: 1px;
    margin: 0px 0;
    border: 0;
    clear: both;
}
.layui-btn-sm {
    height: 20px;
    line-height: 20px;
    padding: 0 10px;
    font-size: 12px;
}
.layui-table, .layui-table-view {
    margin: 0px 0;
}
.layui-table tbody tr:hover, .layui-table thead tr, .layui-table-click, .layui-table-header, .layui-table-hover, .layui-table-mend, .layui-table-patch, .layui-table-tool, .layui-table-total, .layui-table-total tr, .layui-table[lay-even] tr:nth-child(even) {
    background-color: #ffffff;
}
.layui-table-view .layui-table td, .layui-table-view .layui-table th {
    padding: 5px 0;
    border-top: none;
    border-left: none;
    border-right: none;
}
#layui-table-page1{
	text-align: center;
}
.layui-laypage .layui-laypage-curr .layui-laypage-em {
    position: absolute;
    left: -1px;
    top: -1px;
    padding: 1px;
    width: 100%;
    height: 100%;
    background-color: #1E9FFF;
}
</style>
</head>
<body>
<input type="hidden" id="dczjid" value="${dczjid}">
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;display: inline-block;padding-right: 30px;">中奖概况</div>
	<div style="padding-top: 10px;padding-left: 15px;display: inline-block;">参与问卷人数:<span style="color: #01AAED;">${joinQuesNum}人次</span></div>
	<div style="padding-top: 10px;padding-left: 15px;display: inline-block;">参与抽奖人数:<span style="color: #01AAED;">${joinDrawNum}人次</span></div>
</div>
<div style="height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;display: inline-block;width: 10%;"></div>
	<c:forEach items="${prizeSettings}" var="prizeSetting">
		<div style="padding-top: 10px;padding-left: 15px;display: inline-block;width: 10%;text-align: center;">${prizeSetting.prizename}</div>
	</c:forEach>
</div>
<hr>
<div style="height: 40px;width: 100%;">
	<div style="padding-left: 15px;display: inline-block;width: 10%;text-align: center;">奖品数量</div>
	<c:forEach items="${prizeSettings}" var="prizeSetting">
		<div style="padding-top: 10px;padding-left: 15px;display: inline-block;width: 10%;text-align: center;">${prizeSetting.prizenumber}</div>
	</c:forEach>
</div>
<hr>
<div style="height: 40px;width: 100%;">
	<div style="padding-left: 15px;display: inline-block;width: 10%;text-align: center;">剩余数量</div>
	<c:forEach items="${prizeSettings}" var="prizeSetting">
		<div style="padding-top: 10px;padding-left: 15px;display: inline-block;width: 10%;text-align: center;">${prizeSetting.prizeremainder}</div>
	</c:forEach>
</div>
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;display: inline-block;">中奖明细</div>
	<div style="margin-left: 35px;display: inline-block;width: 80%;">
		<div style="display: inline-block;width: 13%;text-align: center;">
			<span class="layui-btn layui-btn-radius layui-btn-normal layui-btn-sm btn-span" 
			style="cursor: pointer;" onclick="changespan(this);">全部</span>
		</div>
		<c:forEach items="${prizeSettings}" var="prizeSetting">
			<div style="display: inline-block;width: 13%;text-align: center;">
				<span class="btn-span" style="cursor: pointer;" 
				 onclick="changespan(this);">${prizeSetting.prizename}</span>
			 </div>
		</c:forEach>
	</div>
	
</div>
<table id="drawtable" lay-filter="test"></table>
</body>
<script type="text/javascript">
layui.use('table', function(){
	  var table = layui.table;
	  var dczjid = $("#dczjid").val();
	  var col;
	  //var prizename = $(".layui-btn layui-btn-radius layui-btn-normal layui-btn-sm").html();
	  var prizename = "全部";
	//获取表头数据
	  $.ajax({
		  url : 'tablehead.do',
		  type : 'post',
		  async : false,
		  dataType : "json",
		  data : {
				"dczjid" : dczjid
			},
		  success : function(result){
			  if(result.success){
				  
				  col = result.params.col;
				}else{
					alert(result.message);
				}
		  }
	  });
	  //第一个实例
	  table.render({
		id: 'drawtable'  
	    ,elem: '#drawtable'
	    ,url: 'drawtable.do' //数据接口
	    ,page: true
	    ,where: {dczjid:dczjid,prizename:prizename}
	    ,cols: col
	  });
	  
}); 

function changespan(span){
	$(".btn-span").removeClass("layui-btn layui-btn-radius layui-btn-normal layui-btn-sm");
	$(span).addClass("layui-btn layui-btn-radius layui-btn-normal layui-btn-sm");
	var table = layui.table;
	dczjid = $("#dczjid").val();
	prizename = $(span).html();
	table.reload('drawtable', {
		  url: 'drawtable.do'
		  ,where: {dczjid:dczjid,prizename:prizename}
		});
}
</script>
</html>