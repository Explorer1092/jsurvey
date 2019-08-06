<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>备选答案图片导入</title>
<h:head pagetype="dialog" highlighter="true" dialog="true" validity="true" upload="true"></h:head>
<script type="text/javascript">
	$(function() {
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
			if(file != 'jpg' && file != 'png' && file != 'jpeg' && file != 'JPG' && file != 'PNG' && file != 'JPEG'){
				alert('只能上传.jpg/.png/.jpeg后缀的图片!','info');
			}else{
				$("#oprform").submit();
			}
		}else{
			alert('请上传图片','info');
		}
	}
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform" name="oprform"
		enctype="multipart/form-data">
		<input type="hidden" value="${dczjid }" name="dczjid">
		<input type="hidden" value="${quesid }" name="quesid">
		<input type="hidden" value="${answid }" name="answid">
		<div id="dialog-content" style="text-align:center;">
			<span>
				<input type="file" id="file" name="file" input-width="200"/>
			</span>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="button" class="btn btn-primary" value="导入" onclick="importsubmit();"/> 
				<input type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>