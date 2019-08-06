<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page"  calendar="true" dialog="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<h:import type="js" path="/resources/dczj/echarts/echarts.min.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
	.layui-table-view .layui-table td, .layui-table-view .layui-table th {
	    padding: 5px 0;
	    border-top: none;
	    border-left: none;
	    border-right: none;
	}
	.layui-btn-group .layui-btn-primary:hover {
	    border-color: #C9C9C9;
	    color: #ffffff;
	    background-color: #1E9FFF;
	}
	body {
		height: 100%;
		width: 99.8%;
	}
</style>
</head>
<body>
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;width: 100px;display: inline-block;">结果报表</div>
	<div style="float: right;margin-right: 15px;width: 70px;padding-top: 5px;">
		<button class="layui-btn layui-btn-radius layui-btn-sm layui-btn-normal" id="hideicon" name="1" onclick="hideEchart()">隐藏图表</button>
	</div>
	
</div>
<div style="height: 40px;width: 100%;padding-top: 10px;">
	<span style="padding-left: 15px;">选择时间：</span>
	<input type="text" id="starttime" name="starttime" value="${starttime}" class="jcalendar input-text" style="width:150px;" 
			onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endtime\')}'})" readonly="readonly">
	<span>-</span>
	<input type="text" id="endtime" name="endtime" value="${endtime}" class="jcalendar input-text" style="width:150px;" 
			onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'starttime\')}'})" readonly="readonly">
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="reload();" style="margin-left: 15px;">检索</button>
	<button class="layui-btn layui-btn-danger layui-btn-sm" onclick="javascript:window.location.reload()" style="margin-right: 15px;float: right;">刷新</button>
	<button class="layui-btn layui-btn-warm layui-btn-sm" onclick="toolbarAction('export')" style="margin-left: 15px;margin-right: 15px;float: right;">导出EXCEL</button>
</div>
<c:forEach var="quesInfo" items="${quesInfos}" varStatus="status">
	<hr>
	<div style="height: 20px;width: 100%;">
		<div style="font-weight: bold;padding-left: 15px;">${status.count}.${quesInfo.quesname}</div>
	</div>
	<c:choose>
		<c:when test="${quesInfo.type==0 or quesInfo.type==1 or quesInfo.type==8}">
			<table id="ques_${quesInfo.iid}" lay-filter="test"></table>
			<script type="text/javascript">
			layui.use('table', function(){
				  var table = layui.table;
				  var starttime = $("#starttime").val();
				  var endtime = $("#endtime").val();
				  //第一个实例
				  table.render({
				    elem: '#ques_${quesInfo.iid}'
				    ,url: 'quescount.do' //数据接口
				    ,where: {quesid:${quesInfo.iid},starttime:starttime,endtime:endtime,randcode:Math.random()*10000}
				    ,cols: [[ //表头
				      {field: 'option', title: '选项', align: 'left'}
				      ,{field: 'count', title: '小计', align: 'center'}
				      ,{field: 'percent', title: '百分比', align: 'center'}
				    ]]
				  });
				  
				});
			</script>
			<div class="layui-btn-group echart-div" style="margin-left: 20px;">
			  <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="funChart('main_${quesInfo.iid}',1,${quesInfo.iid});">
			    	饼状图
			  </button>
			  <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="funChart('main_${quesInfo.iid}',2,${quesInfo.iid});">
			    	柱状图
			  </button>
			</div>
			<div class="echart-div" id="main_${quesInfo.iid}" style="width:100%;height:300px;border: 0px solid #ccc; padding: 1px;margin-left:auto;margin-right:auto;"></div>
			<script type="text/javascript">
				var myChart = echarts.init(document.getElementById('main_${quesInfo.iid}'));
				var countArray = new Array;
				var nameArray = new Array;
				var seriesArray = new Array;
				var starttime = $("#starttime").val();
				var endtime = $("#endtime").val();
				$.ajax({
					type : 'post',
					async : false,
					url :'${pageContext.request.contextPath}/manager/count/datasearch.do',
					dataType : 'json',
					cache : false,
					data:{
						quesid:${quesInfo.iid},
						starttime:starttime,
						endtime:endtime
					},
					success : function(jsonData){
						
						var json = eval(jsonData);
						$.each(json, function (index, item){
							if(index=="status" && item !="0"){
								//数据异常             	 
		                    	 return;
							}
							if(index=="data"){
								var area = eval(item);
								$.each(area, function (index, item){
									if(index == "datacount"){
										var areayeardata = eval(item);
										$.each(areayeardata, function (index, item){
											nameArray.push(item.name);
											countArray.push(item.count);
											seriesArray.push({"value":item.count, "name":item.name});
										});
									}
								});
							}
						});
						var option = {
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    legend: {
							        orient: 'vertical',
							        icon: "circle",
							        left: 'right',
							        data: nameArray
							    },
							    series : [
							        {
							            name: '小计',
							            type: 'pie',
							            radius : '70%',
							            center: ['50%', '60%'],
							            data:seriesArray,
							            itemStyle: {
							                emphasis: {
							                    shadowBlur: 10,
							                    shadowOffsetX: 0,
							                    shadowColor: 'rgba(0, 0, 0, 0.5)'
							                }
							            }
							        }
							    ],
							    color: [
							        '#00acee',
							        '#52cdd5',
							        '#79d9f1',
							        '#a7e7ff',
							        '#c8efff'
							    ]
							};
						myChart.setOption(option);
					}
				});
			</script>
		</c:when>
		
		<c:when test="${quesInfo.type==6 or quesInfo.type==7}">
		<div style="height: 30px;width: 100%;line-height: 30px;">
		<div style="padding-left: 15px;font-size: 12px;">正确率：<span style="color: blue">${quesInfo.answcount}%</span></div>
	    </div>
			<table id="ques_${quesInfo.iid}" lay-filter="test"></table>
			<script type="text/javascript">
			layui.use('table', function(){
				  var table = layui.table;
				  var starttime = $("#starttime").val();
				  var endtime = $("#endtime").val();
				  //第一个实例
				  table.render({
				    elem: '#ques_${quesInfo.iid}'
				    ,url: 'quescount.do' //数据接口
				    ,where: {quesid:${quesInfo.iid},starttime:starttime,endtime:endtime,randcode:Math.random()*10000}
				    ,cols: [[ //表头
					      {field: 'option', title: '选项', align: 'left'}
					      ,{field: 'count', title: '小计', align: 'center'}
					      ,{field: 'percent', title: '百分比', align: 'center'}
					    ]]
				  });
				  
				});
			</script>
			<div class="layui-btn-group echart-div" style="margin-left: 20px;">
			  <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="funChart('main_${quesInfo.iid}',1,${quesInfo.iid});">
			    	饼状图
			  </button>
			  <button class="layui-btn layui-btn-primary layui-btn-sm" onclick="funChart('main_${quesInfo.iid}',2,${quesInfo.iid});">
			    	柱状图
			  </button>
			</div>
			<div class="echart-div" id="main_${quesInfo.iid}" style="width:100%;height:300px;border: 0px solid #ccc; padding: 1px;margin-left:auto;margin-right:auto;"></div>
			<script type="text/javascript">
				var myChart = echarts.init(document.getElementById('main_${quesInfo.iid}'));
				var countArray = new Array;
				var nameArray = new Array;
				var seriesArray = new Array;
				var starttime = $("#starttime").val();
				var endtime = $("#endtime").val();
				$.ajax({
					type : 'post',
					async : false,
					url :'${pageContext.request.contextPath}/manager/count/datasearch.do',
					dataType : 'json',
					cache : false,
					data:{
						quesid:${quesInfo.iid},
						starttime:starttime,
						endtime:endtime
					},
					success : function(jsonData){
						var json = eval(jsonData);
						$.each(json, function (index, item){
							if(index=="status" && item !="0"){
								//数据异常             	 
		                    	 return;
							}
							if(index=="data"){
								var area = eval(item);
								$.each(area, function (index, item){
									if(index == "datacount"){
										var areayeardata = eval(item);
										$.each(areayeardata, function (index, item){
											nameArray.push(item.name);
											countArray.push(item.count);
											seriesArray.push({"value":item.count, "name":item.name});
										});
									}
								});
							}
						});
						var option = {
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    legend: {
							        orient: 'vertical',
							        icon: "circle",
							        left: 'right',
							        data: nameArray
							    },
							    series : [
							        {
							            name: '小计',
							            type: 'pie',
							            radius : '70%',
							            center: ['50%', '60%'],
							            data:seriesArray,
							            itemStyle: {
							                emphasis: {
							                    shadowBlur: 10,
							                    shadowOffsetX: 0,
							                    shadowColor: 'rgba(0, 0, 0, 0.5)'
							                }
							            }
							        }
							    ],
							    color: [
							        '#00acee',
							        '#52cdd5',
							        '#79d9f1',
							        '#a7e7ff',
							        '#c8efff'
							    ]
							};
						myChart.setOption(option);
					}
				});
			</script>
		</c:when>
		
		<c:when test="${quesInfo.type==5}">
			<button class="layui-btn layui-btn-normal layui-btn-sm" 
			onclick="otheranswer(0,5,${quesInfo.iid})" style="margin-left: 15px;">查看答案</button>
			&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: #01AAED">现有答案：${quesInfo.answcount}条</span>
		</c:when>
		<c:otherwise>
			<button class="layui-btn layui-btn-normal layui-btn-sm" 
			onclick="examineanswer(0,2,${quesInfo.iid})" style="margin-left: 15px;">查看答案</button>
			&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: #01AAED">现有答案：${quesInfo.answcount}条</span>
		</c:otherwise>
	</c:choose>
</c:forEach>
<hr>
</body>
<script type="text/javascript">
	function funChart(mainid, type, quesid){
		var countArray = new Array;
		var nameArray = new Array;
		var seriesArray = new Array;
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		$.ajax({
			type : 'post',
			url :'${pageContext.request.contextPath}/manager/count/datasearch.do',
			dataType : 'json',
			cache : false,
			data:{
				quesid:quesid,
				starttime:starttime,
				endtime:endtime
			},
			success : function(jsonData){
				var json = eval(jsonData);
				$.each(json, function (index, item){
					if(index=="status" && item !="0"){
						//数据异常             	 
                    	 return;
					}
					if(index=="data"){
						var area = eval(item);
						$.each(area, function (index, item){
							if(index == "datacount"){
								var areayeardata = eval(item);
								$.each(areayeardata, function (index, item){
									nameArray.push(item.name);
									countArray.push(item.count);
									seriesArray.push({"value":item.count, "name":item.name});
								});
							}
						});
					}
				});
				var myChart = echarts.init(document.getElementById(mainid));
				if(type == 1){
					var option = {
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    legend: {
						        orient: 'vertical',
						        icon: "circle",
						        left: 'right',
						        data: nameArray
						    },
						    series : [
						        {
						            name: '小计',
						            type: 'pie',
						            radius : '70%',
						            center: ['50%', '60%'],
						            data:seriesArray,
						            itemStyle: {
						                emphasis: {
						                    shadowBlur: 10,
						                    shadowOffsetX: 0,
						                    shadowColor: 'rgba(0, 0, 0, 0.5)'
						                }
						            }
						        }
						    ],
						    color: [
						        '#00acee',
						        '#52cdd5',
						        '#79d9f1',
						        '#a7e7ff',
						        '#c8efff'
						    ]
						};
				}else if(type == 2){
					var option = {
						    color: ['#3398DB'],
						    tooltip : {
						        trigger: 'axis',
						        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
						            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
						        }
						    },
						    grid: {
						        left: '20%',
						        right: '25%',
						        bottom: '3%',
						        containLabel: true
						    },
						    xAxis : [
						        {
						            type : 'category',
						            data : nameArray,
						            axisTick: {
						                alignWithLabel: true
						            }
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    series : [
						        {
						        	barMaxWidth:30,
						            name:'小计',
						            type:'bar',
						            data:countArray
						        }
						    ]
						};
					}
				myChart.setOption(option);
			}
		});
	}
	function reload(){
		var start = $("#starttime").val();
		var end = $("#endtime").val();
		parent.changePage('${contextPath}/manager/count/report.do?dczjid=${dczjid}&starttime='+start+'&endtime='+end);
	};
	function otheranswer(answid,type,quesid){
		var start = $("#starttime").val();
		var end = $("#endtime").val();
		parent.changePage('${pageContext.request.contextPath}/manager/count/answlist.do?dczjid=${dczjid}&answid='+answid+'&type='+type+'&quesid='+quesid
				+'&starttime='+start+'&endtime='+end);
	}
	function examineanswer(answid,type,quesid){
		var start = $("#starttime").val();
		var end = $("#endtime").val();
		parent.changePage('${pageContext.request.contextPath}/manager/count/examineanswlist.do?dczjid=${dczjid}&answid='+answid+'&type='+type+'&quesid='+quesid
				+'&starttime='+start+'&endtime='+end);
	}
	function hideEchart(){
		var ishide = $("#hideicon").attr("name");
		if(ishide == 1){
			$("#hideicon").attr("name",0);
			$("#hideicon").html("显示图表");
			$(".echart-div").css("display","none");
		}else{
			$("#hideicon").attr("name",1);
			$("#hideicon").html("隐藏图表");
			$(".echart-div").css("display","");
		}
	}
	function toolbarAction(action){
		switch (action){
		case 'export':
			iframeSubmit("export_reportlist.do?dczjid=${dczjid}&starttime=${starttime}&endtime=${endtime}"); 
			break;
		}
	};
</script>
</html>