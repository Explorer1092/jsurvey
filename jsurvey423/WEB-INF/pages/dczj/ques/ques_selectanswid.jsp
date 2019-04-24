<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="dialog" validity="true"></h:head>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<%-- <script src="${contextPath}/resources/dczj/layui/layui.js"></script> --%>
<script src="${contextPath}/resources/dczj/layui/lay/modules/form.js"></script>
<script type="text/javascript">
$(function() {
	var quesId = $("#quesId").val();
	$("#oprform").validity(function() {
	},{
		success:function(result){
			if(result.success){
				closeDialog();
			}else{
				alert(result.message);
			}
		}
	}		
	);
}		
);
</script>
</head>
<body>
	<form class="layui-form" action="${url}" id="oprform">
	<input type="hidden" name="quesId" id="quesId" value="${quesId}">
	<input type="hidden" name="selectAnswId" id="selectAnswId" value="${d_answid}">
		<table>
			<tr style="height: 40px;">
				<td style="width: 100px;text-align: right;font-size: 15px;">当前题目：</td>
				<td style="width: 360px;font-size: 15px;color: #A9A9A9">${quesName}</td>
			</tr>
			<tr style="height: 40px;">
				<td style="width: 100px;text-align: right;font-size: 15px;">关联选项：</td>
				<td>
				<c:if test="${empty d_answname}">
				<select name="answId" lay-verify="required">
				        <option value="">请选择本题所关联的答案</option>
						<c:forEach var="quesEn" items="${quesList}">
							<optgroup label="${quesEn.quesname}">
								<c:forEach var="answEn" items="${quesEn.answMoreList}">
									<option value="${answEn.iid }">${answEn.answname}</option>
								</c:forEach>
						</c:forEach>
				</select>
				</c:if>
				<c:if test="${not empty d_answname}">
				<select name="answId" lay-verify="required">
				        <option value="">${d_answname}</option>
						<c:forEach var="quesEn" items="${quesList}">
							<optgroup label="${quesEn.quesname}">
								<c:forEach var="answEn" items="${quesEn.answMoreList}">
									<option value="${answEn.iid }">${answEn.answname}</option>
								</c:forEach>
						</c:forEach>
				</select>
				</c:if>
				</td>
			</tr>
		</table>
<!-- 		<div class="layui-form-item" style="margin-top: 320px;"> -->
<!-- 			<div class="layui-input-block" style="margin-left: 200px;"> -->
<!-- 				<button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button> -->
<!-- 			</div> -->
<!-- 		</div> -->
	</form>
	<script>
		//Demo
// 		layui.use('form', function() {
			var form = layui.form;
			form.on('select()', function(data){
				 $("#selectAnswId").val(data.value);
			});
			//监听提交
// 			form.on('submit(formDemo)', function(data) {
// 				return true;
// 			});
// 		});
	</script>
</body>
</html>