<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>敏感词</title>
<h:head pagetype="dialog" validity="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<script>
/**
* 表单验证
*/
$(function(){
	$('#oprform').validity(function() {
		$("#vc_sensitiveword").require("请输入敏感词");
	},{
		success:function(result){
			if(result.success){
				closeDialog(true);
			}else{
				alert(result.message);
			}
		}
	});
});
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform">
    	<!--隐藏变量区-->
		<input type="hidden" name="i_id" id="i_id" value="${en.i_id}" />
		<div id="dialog-content">
        	<!--表单主体-->
			<table border="0" align="center" class="table">
				<tr>
					<td align="right" class="label">敏感词名称</td>
					<td class="required">*</td>
					<td>
						<input type="text" id="vc_sensitiveword" name="vc_sensitiveword" maxlength="50" class="input-text" value="${en.vc_sensitiveword }" />
					</td>
				</tr>
			</table>
		</div>
        <!--表单按钮区-->
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" /> <input
					type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>