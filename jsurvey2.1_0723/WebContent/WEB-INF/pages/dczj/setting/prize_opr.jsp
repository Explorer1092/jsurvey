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
<style type="text/css">
	.layui-form-label {
	    float: left;
	    display: block;
	    padding: 9px 0px;
	    width: 80px;
	    font-weight: 400;
	    line-height: 20px;
	    text-align: right;
	}
	.layui-form-item .layui-inline {
	    margin-bottom: 0px;
	    margin-right: 0px;
	}
	.layui-form-item .layui-input-inline {
	    margin-right: 0px;
	}
</style>
<script type="text/javascript">
$(function(){
	$("#oprform").validity(function() {
		for(var i=1;i<=6;i++){
			$("#prize_name_"+i).require("请输入奖品名称");
			$("#prize_number_"+i).require("请输入奖品数量");
		//	$("#probability_"+i).match(rule, msg);
		}
	}, {
		success : function(result) {
			if (result.success) {
				closeDialog();
			} else {
				alert(result.message);
			}
		}
	});
});

function showDarwWinners(){
	 openDialog('drawwinners/list.do?dczjid=${dczjid}', 600, 400,{
	    	title : '中奖信息'
	    });
}
</script>
</head>
<body>
<form id="oprform" action="${contextPath}/manager/setting/show_submit.do" style="padding-top: 20px;">
	<input type="hidden" id="dczjid" name="dczjid" value="${dczjid}">
	
	<c:forEach var="prizeSetting" items="${prizeSettings}">
		<input type="hidden" id="prize_remainder_${prizeSetting.prizeid}" name="prize_remainder_${prizeSetting.prizeid}" value="${prizeSetting.prizeremainder}">
		<input type="hidden" id="prize_id_${prizeSetting.prizeid}" name="prize_id_${prizeSetting.prizeid}" value="${prizeSetting.iid}">
		<div class="layui-form-item" >
		  <div class="layui-inline">
		    <label class="layui-form-label">奖品${prizeSetting.prizeid}名称：</label>
		    <div class="layui-input-inline" style="width: 140px;">
		      <input type="text" name="prize_name_${prizeSetting.prizeid}"  placeholder="请输入奖品名称" autocomplete="off"  
		      id="prize_name_${prizeSetting.prizeid}" class="layui-input" value="${prizeSetting.prizename}">
		    </div>
		  </div>	  
		  <div class="layui-inline">
		    <label class="layui-form-label">奖品数量：</label>
		    <div class="layui-input-inline" style="width: 50px;">
		      <input type="text" name="prize_number_${prizeSetting.prizeid}" autocomplete="off" class="layui-input" 
		      id="prize_number_${prizeSetting.prizeid}" value="${prizeSetting.prizenumber}">
		    </div>
		  </div>	  
		  <div class="layui-inline">
		    <label class="layui-form-label">获奖概率：</label>
		    <div class="layui-input-inline" style="width: 50px;">
		      <input type="text" name="prize_probability_${prizeSetting.prizeid}" autocomplete="off" class="layui-input" 
		      id="prize_probability_${prizeSetting.prizeid}" value="${prizeSetting.prizeprobability}">
		    </div>
		    <div class="layui-input-inline" style="width: 10px;height: 20px;padding-bottom: 9px;padding-top: 9px;padding-left: 5px;">
		    %
		    </div>
		  </div>
		  <div class="layui-inline">
		    <label class="layui-form-label" style="width: 120px;">剩余奖品数量：${prizeSetting.prizeremainder}</label>
		  </div>	  
		</div>
	</c:forEach>
	<div style="margin-top: 20px;">
		<div class="layui-btn-container" style="float: right;">
			<!-- <input type="button" class="layui-btn layui-btn-normal" value="查看中奖信息" onclick="showDarwWinners();"/>  -->
			<input type="submit" class="layui-btn layui-btn-normal" value="保存" /> 
			<input type="button" class="layui-btn layui-btn-primary" value="取消" onclick="closeDialog();" />
		</div>
	</div>
</form>
</body>
</html>