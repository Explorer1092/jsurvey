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
<input type="hidden" id="starttime" value="${starttime}">
<input type="hidden" id="endtime" value="${endtime}">
<input type="hidden" id="dczjid" value="${dczjid}">
<input type="hidden" id="answid" value="${answid}">
<input type="hidden" id="quesid" value="${quesid}">
<input type="hidden" id="type" value="${type}">
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;">查看答案</div>
</div>
<div id="answercontent"></div>
<div id="otheranswer" style="background-color: #f0f0f0;text-align: center"></div>
</body>
<script type="text/javascript">
$(function(){
	
});
layui.use('laypage', function(){
	  var laypage = layui.laypage;
	  var count = refreshPage(1,10);
	  //执行一个laypage实例
	  laypage.render({
	    elem: 'otheranswer' //注意，这里的 test1 是 ID，不用加 # 号
	    ,count: count //数据总数，从服务端得到
	    ,groups: 10
	    ,layout: ['prev', 'page', 'next', 'skip', 'limit']
	  	,jump: function(obj, first){
		    //首次不执行
		    if(!first){
		    	refreshPage(obj.curr, obj.limit);
		    }
		  }
	  });
});
function refreshPage(page,limit){
	var starttime = $("#starttime").val();
	var endtime = $("#endtime").val();
	var dczjid = $("#dczjid").val();
	var answid = $("#answid").val();
	var quesid = $("#quesid").val();
 	var type = $("#type").val();
 	var count = 0;
	$.ajax({
    	  url: "showotheranser.do",
    	  type: "post",
    	  data: {
    		  page: page,
    		  limit: limit,
    		  dczjid: dczjid,
    		  answid: answid,
    		  quesid: quesid,
    		  type: type,
    		  starttime: starttime,
    		  endtime: endtime
    	  },
		  cache: false,
		  async : false,
		  success: function(result){
			  if (result.success) {
				  var jsonResult = result.params;
			  	  $("#answercontent").html(jsonResult.divDom);
			  	  count = jsonResult.count;
			  } else {
				  alert("操作失败，请联系管理员！");
			  }
		  }
      });
	return count;
};
</script>
</html>