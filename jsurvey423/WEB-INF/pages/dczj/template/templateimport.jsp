<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<h:head  upload="true"></h:head>
<script type="text/javascript">
function goCustom(dczjId,type){
	location.href="showcustom.do?dczjid="+dczjId+"&type="+type;
}
</script>
</head>
<body>
    <form action="${url }" method="post" id="oprform" name="oprform" enctype="multipart/form-data">
    <input type="hidden" name="type" value="${type }">
    <input type="hidden" name="dczjid" value="${dczjid }">
	<table class="table">
		<tr>
			<td width="90px" align="right">模板封面：</td>
			<td><input type="file" id="imgfile" name="imgfile" input-width="180" /></td>
		</tr>
		<tr>
			<td></td>
			<td style="font-size: 10px;color: #666;">模板封面大小建议150*200px大小</td>
		</tr>
		<tr>
			<td width="90px" align="right">电脑模板：</td>
			<td><input type="file" id="pcfile" name="pcfile" input-width="180" /></td>
		</tr>
		<tr>
			<td width="90px" align="right">移动端模板：</td>
			<td><input type="file" id="phonefile" name="phonefile" input-width="180" /></td>
		</tr>
	</table>
	</form>
</body>
</html>