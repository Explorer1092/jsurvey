<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page" calendar="true"></h:head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
html {
	height: 99%;
	width: 100%;
	overflow-x: hidden;
}

body {
	background-color: #efefef;
	height: 100%;
	width: 100%;
	margin: 0px;
}
.table_style{
	height : 90%;
	width: 100%;
}
.pic_style{
    height:90%;
	width:30%;
	text-align:center;
}
.five_style_1{
	text-align:center;
	color:#FFFFFF;
}
.five_style_2{
}
</style>
<script type="text/javascript">
	$(function() {
		var bodyheight = $("#page-body").height();
		var contentheight = bodyheight - 30;
		$("#div-content").css('height', contentheight);
		
		totalCount();
		var pccount = $("#pccount").val();
		var phonecount = $("#phonecount").val();
		var myChart2 = echarts.init(document.getElementById('main_2'));
		var option = {
			    tooltip: {
			        trigger: 'item',
			        formatter: "{a} <br/>{b}: {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        x: 'left',
			        data:['电脑端提交','移动端提交']
			    },
			    series: [
			        {
			            name:'访问来源',
			            type:'pie',
			            radius: ['50%', '70%'],
			            avoidLabelOverlap: false,
			            label: {
			                normal: {
			                    show: false,
			                    position: 'center'
			                },
			                emphasis: {
			                    show: true,
			                    textStyle: {
			                        fontSize: '16',
			                        fontWeight: 'bold'
			                    }
			                }
			            },
			            labelLine: {
			                normal: {
			                    show: false
			                }
			            },
			            data:[
			                {value:pccount, name:'电脑端提交'},
			                {value:phonecount, name:'移动端提交'}
			            ]
			        }
			    ]
			};
		myChart2.setOption(option);
		var type = $("#type").val();
		if(type == 3){
			testcount();	
		}
	})
	
	function totalCount(time){
		var dczjid=$("#dczjid").val();
		$.ajax({
			url : "totalCountEcharts.do",
			type : "post",
			data : {
				dczjid : dczjid,
				date : time
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					var myChart1 = echarts.init(document.getElementById('main_1'));
					var timeData = eval("["+result.params.timedata+"]");
					var countdata = eval("["+result.params.countdata+"]");
					var option = {
						    xAxis: {
						        type: 'category',
						        data: timeData
						    },
						    yAxis: {
						        type: 'value'
						    },
						    series: [{
						        data: countdata,
						        type: 'line'
						    }]
						};
					myChart1.setOption(option);
				} else {
					alert("图表加载失败，请联系管理员！");
				}
			}
		});
	}
	
	function testcount(){
		var myChart3 = echarts.init(document.getElementById('main_3'));
		var grade0 = $("#grade0").val();
		var grade1 = $("#grade1").val();
		var grade2 = $("#grade2").val();
		var grade3 = $("#grade3").val();
		var option3 = {
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: ['较差','及格','良好','优秀']
			    },
			    series : [
			        {
			            name: '访问来源',
			            type: 'pie',
			            radius : '55%',
			            center: ['50%', '60%'],
			            data:[
			                {value:grade0, name:'较差'},
			                {value:grade1, name:'及格'},
			                {value:grade2, name:'良好'},
			                {value:grade3, name:'优秀'}
			            ],
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			};
		myChart3.setOption(option3);
	}
	
	function restart(){
		var gradescore0 = $("#gradescore0").val();
		var gradescore1 = $("#gradescore1").val();
		var gradescore2 = $("#gradescore2").val();
		var gradescore3 = $("#gradescore3").val();
		if(gradescore0 == "" || gradescore1 == "" || gradescore2 == "" || gradescore3 == ""){
			alert("请填写分值！");
			return;
		}else{		
			var re = /^[0-9]*$/;
			if (!re.test(gradescore0) || !re.test(gradescore1) || !re.test(gradescore2) || !re.test(gradescore3)) {
				alert("请填写正数！");
				return;
			}
			var dczjid=$("#dczjid").val();
			$.ajax({
				url : "testCountEcharts.do",
				type : "post",
				data : {
					dczjid : dczjid,
					gradescore0 : gradescore0,
				    gradescore1 : gradescore1,
				    gradescore2 : gradescore2,
				    gradescore3 : gradescore3
				},
				async : false,
				cache : false,
				success : function(result) {
					if (result.success) {
						var myChart3 = echarts.init(document.getElementById('main_3'));
						var grade0 = result.params.grade0;
						var grade1 = result.params.grade1;
						var grade2 = result.params.grade2;
						var grade3 = result.params.grade3;
						var option3 = {
							    tooltip : {
							        trigger: 'item',
							        formatter: "{a} <br/>{b} : {c} ({d}%)"
							    },
							    legend: {
							        orient: 'vertical',
							        left: 'left',
							        data: ['较差','及格','良好','优秀']
							    },
							    series : [
							        {
							            name: '访问来源',
							            type: 'pie',
							            radius : '55%',
							            center: ['50%', '60%'],
							            data:[
							                {value:grade0, name:'较差'},
							                {value:grade1, name:'及格'},
							                {value:grade2, name:'良好'},
							                {value:grade3, name:'优秀'}
							            ],
							            itemStyle: {
							                emphasis: {
							                    shadowBlur: 10,
							                    shadowOffsetX: 0,
							                    shadowColor: 'rgba(0, 0, 0, 0.5)'
							                }
							            }
							        }
							    ]
							};
						myChart3.setOption(option3);
					} else {
						alert("图表加载失败，请联系管理员！");
					}
				}
			});
		}
	} 
	
	function check(){
		var time = $("#endtime").val();
		if(time != null || time != ''){
			totalCount(time);
		}else{
			alert("请选择时间！");
		}
	}
</script>
</head>
<body scroll="no">
    <input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
    <input type="hidden" id="pccount" name="pccount" value="${pccount}">
    <input type="hidden" id="phonecount" name="phonecount" value="${phonecount}">
    <input type="hidden" id="type" name="type" value="${titleInfo.type}">
    <input type="hidden" id="grade0" name="grade0" value="${grade0}">
    <input type="hidden" id="grade1" name="grade1" value="${grade1}">
    <input type="hidden" id="grade2" name="grade2" value="${grade2}">
    <input type="hidden" id="grade3" name="grade3" value="${grade3}">
	<div style="height: 100%;" id="page-body">
		<div style="height: 30px; width: 100%; background: #efefef; margin: auto; line-height: 30px; font-size: 14px; padding-left: 20px;font-weight: bold;">
			参与概况</div>
		<div id="div-content" class="div-content">
			<div style="height: 50%; width: 100%;border-top: 1px solid #CCC;">
				<div style="height: 100%; width: 35%; float: left;border-bottom: 1px solid #CCC;border-right:1px solid #CCC;background-color: #ffffff;">
				    <div style="background-color:rgb(217,83,79);width: 60%;margin-left: 15%;margin-top: 3%;height: 26%;border-radius:5px;">
				       <table class="table_style" >
						<tbody>
							<tr>
								<td rowspan="2" class="pic_style"><img src="${contextPath}/resources/dczj/images/count/u76.png" style="background-size:contain|cover;width:75%;height:auto;"></td>
								<td class="five_style_1">问卷浏览量</td>
							</tr>
							<tr>
								<td class="five_style_1 five_style_2" id="pv_count">${vc}</td>
							</tr>
						</tbody>
					</table>
				    </div>
				    <div style="background-color:rgb(141,205,141);width: 60%;margin-left: 15%;margin-top: 3%;height: 26%;border-radius:5px;">
				       <table class="table_style" >
						<tbody>
							<tr>
								<td rowspan="2" class="pic_style"><img src="${contextPath}/resources/dczj/images/count/u82.png" style="background-size:contain|cover;width:75%;height:auto;"></td>
								<td class="five_style_1">问卷回收量  </td>
							</tr>
							<tr>
								<td class="five_style_1 five_style_2" id="pv_count">${totalCount}</td>
							</tr>
						</tbody>
					</table>
				    </div>
				    <div style="background-color:rgb(142,213,247);width: 60%;margin-left: 15%;margin-top: 3%;height: 26%;border-radius:5px;">
				       <table class="table_style" >
						<tbody>
							<tr>
								<td rowspan="2" class="pic_style"><img src="${contextPath}/resources/dczj/images/count/u88.png" style="background-size:contain|cover;width:75%;height:auto;"></td>
								<td class="five_style_1">回收率</td>
							</tr>
							<tr>
								<td class="five_style_1 five_style_2" id="pv_count">${percentage}%</td>
							</tr>
						</tbody>
					</table>
				    </div>
				</div>
				<div style="height: 100%; width: 64%; float: left;border-bottom: 1px solid #CCC;">
				    <div style="height: 30px; width: 100%;padding-left:10px;line-height: 30px;background-color: #ffffff;border-bottom: 1px solid #CCC;">问卷回收量
				     <input type="text" id="endtime" name="endtime" value="" class="jcalendar input-text" style="width:150px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly="readonly" />
				     <input type="button" class="btn btn-primary" onclick="check();" value="检索">
				     </div>
					<div class="echart-div" id="main_1" style="width:100%;height:85%;border: 0px solid #ccc; padding: 1px;margin-left:auto;margin-right:auto;"></div>
				</div>
			</div>
			<div style="height: 50%; width: 100%;">
			<div style="height: 100%; width: 35%; float: left;border-right:1px solid #CCC;">
			     <div style="height: 30px; width: 97%;padding-left:3%;line-height: 30px;background-color: #ffffff;border-bottom: 1px solid #CCC;">问卷来源</div>
			     <div class="echart-div" id="main_2" style="width:100%;height:85%;border: 0px solid #ccc; padding: 1px;margin-left:auto;margin-right:auto;"></div>
			</div>
			<c:if test="${titleInfo.type != 3 }">
			<div style="height: 100%; width: 30%; float: left;border-right:1px solid #CCC;">
			     <div style="height: 30px; width: 97%;padding-left:3%;line-height: 30px;background-color: #ffffff;border-bottom: 1px solid #CCC;">TOP5 来源地区</div>
			     <div style="width: 100%">
			        <table style="width: 90%;padding-left: 5%">
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">地区</td><td style="text-align: center;">问卷量</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipAddressCount[0][0]}</td><td style="text-align: center;">${ipAddressCount[0][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipAddressCount[1][0]}</td><td style="text-align: center;">${ipAddressCount[1][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipAddressCount[2][0]}</td><td style="text-align: center;">${ipAddressCount[2][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipAddressCount[3][0]}</td><td style="text-align: center;">${ipAddressCount[3][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipAddressCount[4][0]}</td><td style="text-align: center;">${ipAddressCount[4][1]}</td></tr>
			        </table>
			     </div>
			</div>
			<div style="height: 100%; width: 34%; float: left;">
			     <div style="height: 30px; width: 97%;padding-left:3%;line-height: 30px;background-color: #ffffff;border-bottom: 1px solid #CCC;">TOP5 IP</div>
			     <div style="width: 100%">
			       <table style="width: 90%;padding-left: 5%">
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">IP</td><td style="text-align: center;">问卷量</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipCount[0][0]}</td><td style="text-align: center;">${ipCount[0][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipCount[1][0]}</td><td style="text-align: center;">${ipCount[1][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipCount[2][0]}</td><td style="text-align: center;">${ipCount[2][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipCount[3][0]}</td><td style="text-align: center;">${ipCount[3][1]}</td></tr>
			           <tr style="height: 25px;line-height: 25px"><td style="text-align: left;">${ipCount[4][0]}</td><td style="text-align: center;">${ipCount[4][1]}</td></tr>
			        </table>
			     </div>
			</div>
			</c:if>
			<c:if test="${titleInfo.type == 3 }">
			<div style="height: 100%; width: 64%; float: left;border-right:1px solid #CCC;">
			     <div style="height: 30px; width: 98%;padding-left:2%;line-height: 30px;background-color: #ffffff;border-bottom: 1px solid #CCC;">测评等级占比图</div>
			     <div style="height: 100%; width: 50%; float: left;">
			     <div class="echart-div" id="main_3" style="width:100%;height:85%;border: 0px solid #ccc; padding: 1px;margin-left:auto;margin-right:auto;"></div>
			     </div>
			     <div style="height: 100%; width: 50%; float: left;">
			        <table>
			           <tr><td>提示：填写每个区间最低分</td></tr>
			           <tr><td>较差（<input type="text" id="gradescore0" name="gradescore0" value="" style="width: 40px;">）</td></tr>
			           <tr><td>及格（<input type="text" id="gradescore1" name="gradescore1" value="" style="width: 40px;">）</td></tr>
			           <tr><td>良好（<input type="text" id="gradescore2" name="gradescore2" value="" style="width: 40px;">）</td></tr>
			           <tr><td>优秀（<input type="text" id="gradescore3" name="gradescore3" value="" style="width: 40px;">）</td></tr>
			           <tr><td><input type="button" class="btn btn-primary" onclick="restart();" value="确定"></td></tr>
			        </table>
			     </div>
			</div>
			</c:if>
		</div>
	</div>
	</div>
</body>
<h:import type="js" path="/resources/dczj/echarts/echarts.min.js"></h:import>
</html>