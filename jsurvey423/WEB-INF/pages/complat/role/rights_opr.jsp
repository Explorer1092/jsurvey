<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色权限设置</title>
<h:head pagetype="dialog" validity="true" iconfont="true"></h:head>
<script>
	$(function() {
		$("#oprform").validity(
			function() {},
			{
				success:function(result){
					if(result.success){
						closeDialog(true);
					}else{
						alert(result.message);
					}
				}
			}
		);
	});
</script>
</head>
<style>
.rightspan {
	width:120px;  
	float:left; 
	padding:5px 20px;
	overflow:hidden;
	white-space:nowrap;
	text-overflow: ellipsis;
}
</style>
<body>
	<form action="${url }"  method="post" id="oprform" name="oprform">
		<input type="hidden" name="iid" id="iid" value="${iid }" />
		<div id="dialog-content" style="padding: 20px 20px 0 20px; overflow: hidden;">
			<c:forEach items="${allRightList }" var="right">
				<span class="rightspan"title="${right.moduleName }" >
					<input type="checkbox" name="rights" class="checkbox" 
						<c:if test="${fn:contains(selectedRightIds, right.iid) }">checked="checked"</c:if>
						value="${right.iid }" />${right.moduleName }
				</span>
			</c:forEach>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存"/> <input
					type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>