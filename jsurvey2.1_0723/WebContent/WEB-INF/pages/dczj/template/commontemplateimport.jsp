<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<h:head pagetype="dialog" dialog="true" upload="true" validity="true"></h:head>
<script type="text/javascript">
	$(function() {
// 		SyntaxHighlighter.config.clipboardSwf = '${contextPath}/ui/widgets/highlighter/scripts/clipboard.swf';
// 		SyntaxHighlighter.all();
		$('#oprform').validity(function() {
			$('#file').require('必须选择文件');
		}, {
			type : 'iframe'
		});
	});
	
	function importsubmit(){
		var file = $("#file").val();
		if(file != ''){
			file=file.substring(file.lastIndexOf('.')+1,file.length);
			if(file != 'jpg' && file != 'png' && file != 'PNG'&& file != 'JPG'){
				alert('只能上传jpg或png格式的图片','info');
			}else{
				$("#oprform").submit();
			}
		}else{
			alert('请选择图片','info');
		}
	}
</script>
</head>
<body>
    <form action="${url }" method="post" id="oprform" name="oprform" enctype="multipart/form-data" style="padding-top: 15px">
    <input type="hidden" name="type" value="${type }">
    <input type="hidden" name="dczjid" value="${dczjid }">
    <div style="display:none;"><textarea name="pcPageCode" id="pcPageCode">${pcPageCode }</textarea></div>
    <div style="display:none;"><textarea name="phonePageCode" id="phonePageCode">${phonePageCode }</textarea></div>
	<div style="text-align:center;height: 80px;">
		<span>
			<input type="file" id="file" name="file" input-width="200"/>
		</span>
	</div>
	<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="button" class="btn btn-primary" value="导入" onclick="importsubmit();" /> 
				<input type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>