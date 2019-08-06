<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志设置</title>
<h:head pagetype="page" validity="true" toggle="true" select="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<script>
$(function(){
	$('#logdetail').click(function(){
		var logdetail = $(this).prop('checked');
		ajaxSubmit('change_logdetail.do', {
			data:{logdetail:logdetail}
		});
	});
});
function changeLevel(obj){
	var logName = $(obj).data('logname');
	var level = $(obj).val();
	ajaxSubmit('change_level.do', {
		data:{loggerName:logName,level:level}
	});
}
</script>
</head>
<body>
	<div id="page-title">
		系统参数 / 
		<span id="page-location">日志设置<span style="color: #cc0066;">（本页面中的设置皆为临时设置，重启后恢复初始状态）</span></span>
	</div>
	<div id="page-content">
		<table border="0" align="left" class="table">
			<tr>
				<td align="right" class="label">打印详细错误日志</td>
				<td>
					<label>打印详细<input type="checkbox" id="logdetail" name="logdetail" value="true"  data-value="${logdetail }"></label>
				</td>
			</tr>
			<tr>
				<td align="right" class="label">日志级别详细设置</td>
				<td>
					<div style="margin-bottom: 10px;overflow: hidden;">
						<form action="info.do">
						类路径 <input name="filter" type="text" class="input-text" value="${filter}"/> 
						<input type="submit" class="btn btn-primary" value="过滤" />
						</form>
					</div>
					<c:forEach items="${levelTree }" var="logger">
					<div style="margin-bottom: 10px;overflow: hidden;">
						<span>
							<span style="width: 500px;text-overflow:ellipsis;float:left;border-bottom: 1px dashed #DDDDDD;font-size: 14px" title="${logger.key }">${logger.key }</span>
							<input name="loggerName" type="hidden" value="${logger.key }"/>
						</span>
						<span style="margin-left: 5px">
							<select name="level" data-value="${logger.value}" style="width: 80px" onchange="changeLevel(this)" data-logname="${logger.key }">
								<option value="">未设置</option>
								<option value="DEBUG">DEBUG</option>
								<option value="INFO">INFO</option>
								<option value="WARN">WARN</option>
								<option value="ERROR">ERROR</option>
								<option value="FATAL">FATAL</option>
								<option value="ALL" style="color: #FF0000;">ALL</option>
								<option value="OFF" style="color: #FF0000;">OFF</option>
							</select>
						</span>
					</div>
					</c:forEach>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>