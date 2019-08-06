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
	html{
		height: 99%;
		width: 99%;
		overflow-x:hidden;
	}
	body{
		background: #ffffff;
		height: 99.5%;
		width: 99%;
	}
	.select{
		color: #01AAED;
	}
    .innerbox::-webkit-scrollbar {/*滚动条整体样式*/
         width: 4px;     /*高宽分别对应横竖滚动条的尺寸*/
         height: 4px;
     }
     .innerbox::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
         border-radius: 5px;
         -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
         background: rgba(0,0,0,0.2);
     }
     .innerbox::-webkit-scrollbar-track {/*滚动条里面轨道*/
         -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
         border-radius: 0;
         background: rgba(0,0,0,0.1);
     }
</style>
</head>
<body>
	<input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
	<div class="selectbutton" style="display: inline-block;width:175px;border-right: 1px solid #dddddd;height: 100%;vertical-align: top;overflow-y:scroll;">
		
		<div style="border-bottom: 1px solid #dddddd;height: 125px;">
			<div style="padding-top: 25px;padding-left: 50px;cursor: pointer;" onclick="clearselect();selectcygk();changePage('${contextPath}/manager/count/generalizationlist.do?dczjid=${dczjid}')">
				<img id="png_cygk" style="padding-left: 5px;" width="50px;" alt="参与概况"  src="${contextPath}/resources/dczj/images/count/u40.png">
				<div id="write_cygk" style="padding-top: 2px;">参与概况</div>
			</div>
		</div>
		<div style="border-bottom: 1px solid #dddddd;height: 125px;">
			<div style="padding-top: 25px;padding-left: 50px;cursor: pointer;" onclick="clearselect();selectcymx();changePage('${contextPath}/manager/count/detaillist.do?dczjid=${dczjid}')">
				<img id="png_cymx" style="padding-left: 5px;" width="50px;" alt="参与明细"  src="${contextPath}/resources/dczj/images/u5226.png">
				<div id="write_cymx" style="padding-top: 2px;">参与明细</div>
			</div>
		</div>
		
		<div style="border-bottom: 1px solid #dddddd;height: 125px;">
			<div  style="padding-top: 25px;padding-left: 50px;cursor: pointer;" onclick="clearselect();selectjgbb();changePage('${contextPath}/manager/count/report.do?dczjid=${dczjid}')">
				<img id="png_jgbb" width="50px;" alt="报表结果"  src="${contextPath}/resources/dczj/images/u5266.png">
				<div id="write_jgbb" style="padding-top: 2px;">结果报表</div>
			</div>
		</div>
		<div style="border-bottom: 1px solid #dddddd;height: 125px;">
			<div  style="padding-top: 25px;padding-left: 50px;cursor: pointer;" onclick="clearselect();selectjcfx();changePage('${contextPath}/manager/count/crossanalysis.do?dczjid=${dczjid}')">
				<img id="png_jcfx" width="50px;" alt="交差分析"  src="${contextPath}/resources/dczj/images/count/u50.png">
				<div id="write_jcfx" style="padding-top: 2px;">交差分析</div>
			</div>
		</div>
		<div style="border-bottom: 1px solid #dddddd;height: 125px;">
			<div  style="padding-top: 25px;padding-left: 50px;cursor: pointer;" onclick="checkIsPrize('${contextPath}/manager/count/showdraw.do?dczjid=${dczjid}');">
				<img id="png_zjjg" width="50px;" alt="中奖结果"  src="${contextPath}/resources/dczj/images/u5270.png">
				<div id="write_zjjg" style="padding-top: 2px;">中奖结果</div>
			</div>
		</div>
	</div>
<!-- 	<div class="layui-body" style="top: 50px; left: 0px; background-color: #f0f0f0"> -->
<!-- 			<iframe src="" name="quespage" id="quespage" frameborder="0" style="width: 100%; height: 99%"></iframe> -->
<!-- 		</div> -->
	<div class="layui-body" id="frame" style="left: 175px;height: 100%;vertical-align: top;">
		<iframe id="page" src="${contextPath}/manager/count/generalizationlist.do?dczjid=${dczjid}" frameborder="0" style="height: 99%;width: 100%;"></iframe>
	</div>
	
</body>
<script type="text/javascript">
    var width = 
	$(function(){
		var dczjid = $("#dczjid").val();
		$.ajax({
			type : "post",
			url : "getquessession.do",
			data : {
				"dczjid" : dczjid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					var url = result.params.url;
					var selectid = result.params.selectid;
					if (url != "" && url != null && selectid != "" && selectid != null) {
						$('#page').attr("src",url);
						if(selectid == "cymx"){
							selectcymx();
						}else if(selectid == "jgbb"){
							selectjgbb();
						}else if(selectid == "cygk"){
							selectcygk();
						}else if(selectid == "jcfx"){
							selectjcfx();
						}else{
							selectzjjg();
						}
					} else {
						selectcygk();
					}
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		})
		$("#frame").css("width",$(document).width()-180);
	})
	function clearselect(){
    	$("#write_cygk").removeClass("select");
		$("#write_cymx").removeClass("select");
		$("#write_jgbb").removeClass("select");
		$("#write_zjjg").removeClass("select");
		$("#write_jcfx").removeClass("select");
		$("#png_cygk").attr("src","${contextPath}/resources/dczj/images/count/u40.png");
		$("#png_cymx").attr("src","${contextPath}/resources/dczj/images/u5226.png");
		$("#png_jgbb").attr("src","${contextPath}/resources/dczj/images/u5266.png");
		$("#png_zjjg").attr("src","${contextPath}/resources/dczj/images/u5270.png");
		$("#png_jcfx").attr("src","${contextPath}/resources/dczj/images/count/u50.png");
	};
	function selectcygk(){
		$("#write_cygk").addClass("select");
		$("#png_cygk").attr("src","${contextPath}/resources/dczj/images/count/u64.png");
	}
	function selectcymx(){
		$("#write_cymx").addClass("select");
		$("#png_cymx").attr("src","${contextPath}/resources/dczj/images/u5754.png");
	}
	function selectjgbb(){
		$("#write_jgbb").addClass("select");
		$("#png_jgbb").attr("src","${contextPath}/resources/dczj/images/u6099.png");
	}
	function selectjcfx(){
		$("#write_jcfx").addClass("select");
		$("#png_jcfx").attr("src","${contextPath}/resources/dczj/images/count/u120.png");
	}
	function selectzjjg(){
		$("#write_zjjg").addClass("select");
		$("#png_zjjg").attr("src","${contextPath}/resources/dczj/images/u6768.png");
	}
	function changePage(url){
		var selectid = $(".select").attr("id").split("_")[1];
		var dczjid = $("#dczjid").val();
		$.ajax({
			type : "post",
			url : "setquessession.do",
			data : {
				"url" : url,
				"dczjid" : dczjid,
				"selectid": selectid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					$('#page').attr("src",url);
				} else {
					alert("操作失败，请联系管理员！");
				}
			}
		})
	}
	function checkIsPrize(url){
		var dczjid = $("#dczjid").val();
		$.ajax({
			type : "post",
			url : "check_isprize.do",
			data : {
				"dczjid" : dczjid,
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					clearselect();
					selectzjjg();
					changePage(url);
				} else {
					alert(result.message);
				}
			}
		})
			
	}
</script>
</html>