<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="dialog" validity="true" tree="true" dialog="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
.layui-tab-brief>.layui-tab-title .layui-this {
    color: #01AAED;
}
.layui-tab-brief>.layui-tab-more li.layui-this:after, .layui-tab-brief>.layui-tab-title .layui-this:after {
    border: none;
    border-radius: 0;
    border-bottom: 2px solid #01AAED;
}
.inlinediv-body{
	display: inline-block;
	vertical-align: top;
	border: 1px solid #01AAED;
	width: 200px;
	height: 300px;
	background-color:#f6f6f6;
}
.inlinediv-btn{
	display: inline-block;
	vertical-align: top;
	width: 50px;
	height: 300px;
	margin-left: 24px;
	margin-right: 24px;
}
.inlinediv-head{
	display: inline-block;
	vertical-align: top;
	width: 175px;
	height: 20px;
	margin-left: 50px;
	margin-right: 50px;
	margin-bottom: 5px;
}
</style>
<script type="text/javascript">
</script>
</head>
<body>
<input type="hidden" id="groups" name="groups" value="${limitLoginUser.groups}">
<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
  <ul class="layui-tab-title">
    <li class="layui-this">选题组卷</li>
    <li onclick="finduser()">抽题组卷</li>
  </ul>
  <div class="layui-tab-content">
	<div class="layui-tab-item layui-show">
	<iframe src="" style="width:565px;height:500px;border:0px;"></iframe>
	</div>
<!-- 	<div class="layui-tab-item">
		<div style="margin-top: 10px;">
			<span style="margin-top:15px;">已选xx题</span>
			<button style="margin-right: 17px;float: right;" class="layui-btn layui-btn-normal" onclick="submit(2);">保存</button>
			<button style="margin-right: 17px;float: right;" class="layui-btn layui-btn-primary" onclick="submit(2);">取消</button>
		</div>		  
	</div> -->
  
</div>
</div>
</body>
<script type="text/javascript">
layui.use('element', function(){
	  var element = layui.element;
	  element.on('tab(docDemoTabBrief)', function(data){
		    console.log(data);
		  });
	});
layui.use('form', function(){
	  var form = layui.form;
	  
	  //监听提交
	  /*form.on('submit(formDemo)', function(data){
	    layer.msg(JSON.stringify(data.field));
	    return false;
	  });*/
	});
</script>
</html>