<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<h:head pagetype="page"></h:head>
<style type="text/css">
.content{
   margin-right: 50px;
   margin-top: 20px;
   position:relative
</style>
<script type="text/javascript">
function qecodedownload(){
    iframeSubmit("downloadFile.do?picname=${picname}");
}

function copycode(){
	iframeSubmit("copycode.do?href=${pccontent}");
	layer.msg('已复制成功', {icon: 6,time: 1500,anim: 5});
}

function openhref(){
	var front_check_dczjid = '${dczjid}';
	$.ajax({
		 type: "post",
		 async:false, 
		 url:"../../front/jsurvey/checkUserLimit.do",
		 data:"dczjid="+front_check_dczjid,
		 success:function(result){
		 	if(result.success){
		    	window.open("../../front/jsurvey/userlogin_pc.do?dczjid="+front_check_dczjid);
		 	}else{
		 		window.open("${pccontent}");
		 	}
		}
	});
	
}
</script>
</head>
<body>
<div class="content">
<div style="width: 100%;margin-left: 50px;">
 <span style="line-height: 30px;font-size: 14px;font-weight: bolder;">分享问卷链接</span>
</div>
<div style="width: 100%;margin-top: 10px;margin-left: 50px;">
<input type="text" class="input-text" value="${pccontent}" readonly="readonly">
<input type="button" class="btn" onclick="copycode();" value="复制"> 
<input type="button" class="btn" onclick="openhref();" value="打开"> 
</div>
<div style="width: 100%;margin-top: 20px;margin-left: 50px;position:absolute;z-index: 1">
<span style="line-height: 30px;font-size: 14px;font-weight: bolder;">手机端二维码</span>
</div>
<div style="width: 100%;margin-left: 50px;position:absolute;top:128px;z-index: 0">
<span><img alt="手机端访问地址" src="${picpath}"></span>
<span style="display: inline-block;margin-left: 30px;">使用手机扫描二维码即可参与问卷活动</span>
</div>
<div style="width: 100%;margin-left: 50px;position:absolute;top:295px;z-index: 0">
	<input type="button" class="btn btn-success" style="width: 150px;" value="下载二维码" onclick="qecodedownload();" >
</div>
</div>
</body>
</html>