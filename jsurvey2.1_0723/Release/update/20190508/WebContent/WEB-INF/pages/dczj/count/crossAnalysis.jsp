<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page" tip="true"></h:head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet"
	href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<h:import type="js" path="/resources/dczj/echarts/echarts.min.js"></h:import>
<style type="text/css">
html {
	height: 99%;
	width: 100%;
	overflow-x: hidden;
}

body {
	background-color: #ffffff;
	height: 100%;
	width: 100%;
	margin: 0px;
}

.div-content {
	height: 150px;
	width: 100%;
	margin: 0px;
	border-top: 1px solid #CCC;
	border-bottom: 1px solid #CCC;
	text-align: left;
}
</style>
</head>
<body>
	<input type="hidden" id="dczjid" value="${dczjid }">
	<input type="hidden" id="yquesid" value="">
	<input type="hidden" id="xquesid" value="">
	<div style="height: 100%;" id="page-body">
		<div
			style="height: 30px; width: 100%; background: #efefef; margin: auto; line-height: 30px; font-size: 14px; padding-left: 20px; font-weight: bold;">交差分析</div>
		<form class="layui-form" action="${url}" id="oprform">
			<div id="div-content" class="div-content">
				<table style="width: 60%; margin-left: 20%">
					<tr style="height: 40px; line-height: 40px;">
						<td><h:tip title="测评问卷需要单选题才能进行交叉分析"></h:tip>自变量X</td>
						<td>因变量Y</td>
					</tr>
					<tr style="height: 40px; line-height: 40px;">
						<td>
							<div class="layui-input-block" style="width: 80%; margin-left: 0">
								<select name="xquesid" lay-filter="xselect">
									<option value="">请选择</option>
									<c:forEach var="quesEn" items="${quesList}">
										<option value="${quesEn.iid }">${quesEn.quesname}</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td>
							<div class="layui-input-block" style="width: 80%; margin-left: 0">
								<select name="yquesid" lay-filter="yselect">
									<option value="">请选择</option>
									<c:forEach var="quesEn" items="${quesList1}">
										<option value="${quesEn.iid }">${quesEn.quesname}</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
				</table>
				<div style="width: 100%; margin-top: 20px;">
					<button type="button" onclick="crossanalysis();"
						class="layui-btn layui-btn-normal layui-btn-sm"
						style="margin-left: 44%">交叉分析</button>
				</div>
			</div>
		</form>
		<div style="width: 100%; background-color: #eee; height: 30px;"></div>
		<div style="width: 100%; text-align: center;" id="table-content"></div>
		<div style="height: 400px; width: 100%;margin-top: 20px;">
			  <div class="echart-div" id="myChartAnalysis" style="width:80%;height:100%;margin-left: 10%"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	layui.use([ 'form' ], function() {
		var form = layui.form;
		form.on('select(xselect)', function(data) {
			$("#xquesid").val(data.value)
		});
		form.on('select(yselect)', function(data) {
			$("#yquesid").val(data.value)
		});
	})
	function crossanalysis() {
		var xquesid = $("#xquesid").val();
		var yquesid = $("#yquesid").val();
		$.ajax({
			url : "oprcrossanalysis.do",
			type : "post",
			data : {
				xquesid : xquesid,
				yquesid : yquesid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					var html = result.params.html;
					var legend_data = result.params.legend_data;
					var yAxis_data = result.params.yAxis_data;
					var seriesData = result.params.seriesData;
					$('#table-content').html(html);
					echartanalysis(legend_data,yAxis_data,seriesData);
				}
			}
		});
	}

	function echartanalysis(legend_data,yAxis_data,seriesData) {
		var myChart = echarts.init(document.getElementById('myChartAnalysis'));
		var xData = eval(legend_data);
		var yData = eval(yAxis_data);
		var seriesDatas = eval(seriesData);
		var option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend : {
				data : xData
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : {
				type : 'value'
			},
			yAxis : {
				type : 'category',
				data : yData
			},
			series :seriesDatas
		};
		myChart.setOption(option);
	}
</script>
</html>