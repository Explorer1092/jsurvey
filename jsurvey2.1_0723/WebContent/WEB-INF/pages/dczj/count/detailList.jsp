<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page"  calendar="true" select="true"></h:head>
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
	.layui-form-radio {
    line-height: 28px;
    margin: 0px 0px 0 0; 
    padding-right: 10px;
    cursor: pointer;
    font-size: 0;
}
.layui-form-item .layui-inline {
    margin-bottom: 0px;
}
</style>
</head>
<body>
<!-- <form class="layui-form" action=""> -->
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;">参与明细</div>
</div>
<div style="height: 40px;width: 100%;padding-top: 10px;">
	<span style="padding-left: 15px;">选择时间：</span>
	<input type="hidden" id="dczjid" value="${dczjid}">
	<input type="hidden" id="createdate" value="${createdate}">
	<input type="hidden" id="earnscore" value="${earnscore}">
	<input type="text" id="starttime" name="starttime" value="${starttime}" class="jcalendar input-text" style="width:150px;" 
			onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endtime\')}'})" readonly="readonly">
	<span>-</span>
	<input type="text" id="endtime" name="endtime" value="${endtime}" class="jcalendar input-text" style="width:150px;" 
			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'starttime\')}'})" readonly="readonly">
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="reload();" style="margin-left: 15px;">检索</button>
	<button class="layui-btn layui-btn-danger layui-btn-sm" onclick="javascript:window.location.reload()" style="margin-right: 15px;float: right;">刷新</button>
	<button class="layui-btn layui-btn-danger layui-btn-sm" onclick="removeAll()" style="margin-left: 15px;float: right;">清空数据</button>
	<button class="layui-btn layui-btn-warm layui-btn-sm" onclick="toolbarAction('export')" style="margin-left: 15px;float: right;">导出EXCEL</button>
	</div>
	
<div class="layui-form layui-form-item">
<table id="detail" lay-filter="test" border="0">

<%-- <tr height="30px;"><td colspan="4"  height="30px;"><span style="padding-left: 15px;">选择排序：</span>
    <input type="radio" id="escore" name="escore" value="0" title="创建时间"  checked>
    <c:if test="${type==3}">
	<input type="radio" id="escore"  name="escore" value="1" title="得分" >
	</c:if>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="btssort();" style="margin-left: 184px;">正序</button>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="stbsort();" style="margin-left: 15px;">逆序</button></td></tr>
<tr> --%>

<c:if test="${type==3}">
<td style="width: 120px;"><span style="padding-left: 15px;">总分：${sumscore}</span></td>
<td style="width: 120px;"><span style="padding-left: 15px;">最高分：${maxscore}</span></td>
<td style="width: 120px;"><span style="padding-left: 15px;">最低分：${minscore}</span></td>
<td style="width: 120px;"><span style="padding-left: 15px;">平均分：${avgscore}</span></td>
</c:if>
</tr>
</table>

</div>
<!-- </form> --> 
</body>
<c:if test="${type==3}">
<script type="text/javascript">
var check;
var sorttype;
layui.use('table', function(){
	  var table = layui.table;
	  //第一个实例
	  table.render({
	    elem: '#detail'
	   ,autoSort: false 
	    //,height: 312
	    ,url: 'list.do' //数据接口
	    ,page: true //开启分页
	    ,where: {randcode:Math.random()*10000,dczjid:${dczjid}}
	    ,cols: [[ //表头
	      {field: 'iid', title: 'ID', width:'5%', align: 'center'}
	      ,{field: 'unid', title: 'COOKIED', width:'30%', align: 'center'}
	      ,{field: 'ip', title: '参与人IP', width:'20%', align: 'center'} 
	      ,{field: 'type', title: '提交来源', width: '13%', align: 'center'}
	      ,{field: 'createdate', title: '提交时间', width: '20%',   align: 'center', sort: true}
	      ,{field: 'earnscore', title: '得分', width: '13%',   align: 'center', sort: true}
	      //,{field: 'delete', title: '删除', width: '10%', align: 'center'}
	    ]]
	  });

	  table.on('sort(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		  if(obj.type == "asc"){
			  sorttype = 0; 
		  }else if(obj.type == "desc"){
			  sorttype = 1;  
		  }
		  if(obj.field == "createdate"){
			  check = 1;
		  }else if(obj.field == "earnscore"){
			  check = 2;  
		  }
			  
		  var starttime = $("#starttime").val();
	  	  var endtime = $("#endtime").val();
		  table.reload('detail', {
//		    initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
		    url: 'list.do' //数据接口
			,where: {starttime:starttime,endtime:endtime,check:check,sorttype:sorttype}
		 	,page: {
	          curr: 1 //重新从第 1 页开始
		    },
		    });
	  
	});
	  
	  

	});
</script>
</c:if>
<c:if test="${type==0||type==1||type==2}">
<script type="text/javascript">
layui.use('table', function(){
	  var table = layui.table;
	  //第一个实例
	  table.render({
	    elem: '#detail'
	    ,autoSort: false 
	    //,height: 312
	    ,url: 'list.do' //数据接口
	    ,page: true //开启分页\
	    ,where: {randcode:Math.random()*10000,dczjid:${dczjid}}
	    ,cols:[[ //表头
	      {field: 'iid', title: 'ID', width:'8%',  align: 'center'}
	      ,{field: 'unid', title: 'COOKIED', width:'30%', align: 'center'}
	      ,{field: 'ip', title: '参与人IP', width:'23%', align: 'center'} 
	      ,{field: 'type', title: '提交来源', width: '20%', align: 'center'}
	      ,{field: 'createdate', title: '提交时间', width: '20%', align: 'center', sort: true}
	      //,{field: 'delete', title: '删除', width: '10%', align: 'center'}
	    ]]
	  });

	  table.on('sort(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		  var check;
		  var sorttype;
		  if(obj.type == "asc"){
			  sorttype = 0; 
		  }else if(obj.type == "desc"){
			  sorttype = 1;  
		  }
		  if(obj.field == "createdate"){
			  check = 1;
		  }else if(obj.field == "earnscore"){
			  check = 2;  
		  }
			  
		  var starttime = $("#starttime").val();
	  	  var endtime = $("#endtime").val();
		  table.reload('detail', {
//		    initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
		    url: 'list.do' //数据接口
			,where: {starttime:starttime,endtime:endtime,check:check,sorttype:sorttype}
		 	,page: {
	          curr: 1 //重新从第 1 页开始
		    },
		    });
	  
	});
});
</script>
</c:if>
<script type="text/javascript">
	function reload(check,sorttype){
		var table = layui.table;
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		table.reload('detail', {
			  url: 'list.do' //数据接口
			  ,where: {starttime:starttime,endtime:endtime,check:check,sorttype:sorttype}
		 	  ,page: {
	          curr: 1 //重新从第 1 页开始
	        },
			});
	};

	function toolbarAction(action){
		switch (action){
		case 'export':
			var starttime = $("#starttime").val();
			var endtime = $("#endtime").val();
			iframeSubmit("exportdetailnew.do?dczjid=${dczjid}&starttime="+starttime+"&endtime="+endtime+"&type=${type}"); 
			break;
		}
	};
	function search(){
			var x = document.getElementById("select1").value;
				  document.form1.select2.options[0].value="1";
				  document.form1.select2.length=1;  
			if(x=="2"){
				  document.form1.select2.options[0].text="提交时间";
			}else if(x=="1"){
				document.form1.select2.options[0].text="得分";
			}
		}
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
	function btssort(){
		var dczjid = $('#dczjid').val();
		var sorttype=0;
		var check;
		var checked=$("input[name='escore']:checked").val();
		if(checked==0){
		  check=1;
		}
		if(checked==1){
		  check=2;
		}
		confirm("您确认执行吗",function(){
			reload(check,sorttype);
		});
		
	};
	function stbsort(){
		var dczjid = $('#dczjid').val();
		var sorttype=1;
		var check;
		var checked=$("input[name='escore']:checked").val();
		if(checked==0){
		  check=1;
		}
		if(checked==1){
		  check=2;
		}
		confirm("您确认执行吗",function(){
			reload(check,sorttype);
		});
	
	};
	function showdetail(id){
		parent.changePage('${contextPath}/manager/count/showdetail.do?id='+id);
	};
</script>
</html>
