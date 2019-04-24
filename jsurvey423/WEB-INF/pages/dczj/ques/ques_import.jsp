<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调查表导入</title>
<h:head pagetype="dialog" upload="true" validity="true" iconfont="true"></h:head>
<script type="text/javascript">
$(function(){
	$('#oprform').validity(function(){
		$('#file').require('请选择文件');
	},{type:'iframe'});
});

//导出案例
function downloadEx() {
	window.open("downloadfile.do");
}
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform" name="oprform"
		enctype="multipart/form-data">
		<input type="hidden" value="${dczjid }" name="dczjid">
		<div id="dialog-content" style="text-align:center;">
			<span>
				<input type="file" id="file" name="file" input-width="200"/>
<!-- 				<a class="link" href="javascript:void(0);" onclick="downloadEx();" style="margin-left:10px;">下载参考样例</a> -->
			</span>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="导入" /> <input
					type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>