<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>调查征集系统</title>
<h:head pagetype="page"></h:head>
<link type="text/css" rel="stylesheet" href="../../resources/dczj/layui/css/layui.css">
<script
	src="${contextPath}/resources/dczj/layui/lay/modules/carousel.js"></script>
<script src="${contextPath}/resources/dczj/layui/lay/modules/table.js"></script>
<script src="${contextPath}/resources/dczj/layui/lay/modules/laytpl.js"></script>
<script src="${contextPath}/resources/dczj/js/quesbank.js"></script>

<script type="text/javascript">
/* function submit(webid){
	window.location.href="addquesbank_submit.do?webid="+webid;
} */
function toolbarAction(action) {
	switch (action) {
		case 'ok':
			var webid = $('#webid').val();
			var newquesbankname = $('#newquesbankname').val();
			var username = $('#username').val();
			var creator = $('#creator').val();
			if(newquesbankname == ""){
				alert("题库名称为空，请填写正确的题库名称");
				return;
			}
			//iframeSubmit(encodeURI("${pageContext.request.contextPath}/manager/dczj/addquesbank_submit.do?
					//webid="+webid+"&quesbankname="+quesbankname+"&username="+username+"&creator="+creator));
			break;
	}
}
</script>

<style type="text/css">
.dczj-btn {
   background-color: #5FB878;
}

/*  #btn-submit{
	margin-left: 70%;
} 

#btn-close{
	margin-left: 80%;
} */

#button-div{
	margin-left: 62%;
	margin-top: 20%;
}

</style>
</head>
<body>

	<input type="hidden" id="dczjid" name="dczjid" value="${infoEn.iid}">
	<input type="hidden" id="webid" name="webid" value="${webid }">
	<input type="hidden" id="orderid" name="orderid" value="${infoEn.orderid }">
	<input type="hidden" id="username" name="username"value="${username}">
	<input type="hidden" id="creator" name="creator" value="${creator }">
	<input type="hidden" id="createtime" name="createtime"value="${infoEn.createtime}">
	<table style="width: 500px; margin-top: 20px;">
		<tr style="line-height: 50px;">
			<td class="label" align="right">题库名称：</td>
			<td><input type="text" id="newquesbankname" name="newquesbankname"
				class="layui-input" style="width: 100%;" value="${infoEn.quesbankname}"
				placeholder="请输入标题" ></td>
		</tr>		
	</table>
<!-- 	<div id="button-div">
		<input type="button" class="btn btn-primary"  id="btn-submit" value="确认" onclick="toolbarAction('ok')"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="btn" value="取消" id="btn-close" style="margin: 0px;" onclick="closeDialog();" />
	</div> -->
</body>
</html>