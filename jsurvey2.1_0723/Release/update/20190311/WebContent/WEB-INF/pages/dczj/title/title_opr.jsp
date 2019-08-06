<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<h:head pagetype="page"></h:head>
<link type="text/css" rel="stylesheet" href="../../resources/dczj/layui/css/layui.css">
<%-- <script src="${contextPath}/resources/dczj/layui/layui.js"></script> --%>
<script
	src="${contextPath}/resources/dczj/layui/lay/modules/carousel.js"></script>
<script src="${contextPath}/resources/dczj/layui/lay/modules/table.js"></script>
<script src="${contextPath}/resources/dczj/layui/lay/modules/laytpl.js"></script>
<style type="text/css">
.dczj-btn {
   background-color: #5FB878;
}
</style>
</head>
<body>
	<input type="hidden" id="type" name="type" value="${infoEn.type}">
	<input type="hidden" id="username" name="username"
		value="${infoEn.username}">
	<input type="hidden" id="dczjid" name="" dczjid"" value="${infoEn.iid}">
	<table style="width: 500px; margin-top: 20px;">
		<tr style="line-height: 50px;">
			<td class="label" align="right">问卷标题：</td>
			<td><input type="text" id="titlename" name="titlename"
				class="layui-input" style="width: 100%;" value="${infoEn.titlename}"
				placeholder="请输入标题"></td>
		</tr>
		<tr style="line-height: 50px;">
			<td class="label" align="right">问卷类型：</td>
			<c:if test="${dczjid != 0}">
				<td class="label">${typename}</td>
			</c:if>
			<c:if test="${dczjid == 0}">
				<td>
					<div class="layui-btn-group typeopr" style="margin-top: 5px;">
						<button id="jsurveytype0" class="layui-btn layui-btn-sm dczj-btn"
							data-type="set" style="width: 80px;" data-key="dczj_type"
							data-value="0">调查</button>
						<button id="jsurveytype1" class="layui-btn layui-btn-sm dczj-btn"
							data-type="set" style="width: 80px;" data-key="dczj_type"
							data-value="1">征集</button>
						<button id="jsurveytype2" class="layui-btn layui-btn-sm dczj-btn"
							data-type="set" style="width: 80px;" data-key="dczj_type"
							data-value="2">投票</button>
						<button id="jsurveytype3" class="layui-btn layui-btn-sm dczj-btn"
							data-type="set" style="width: 80px;" data-key="dczj_type"
							data-value="3">测评</button>
					</div>
				</td>
			</c:if>
		</tr>
		<tr style="line-height: 50px;">
			<td class="label" align="right">问卷站点：</td>
			<td class="label">${webName}</td>
		</tr>
		<tr style="line-height: 50px;">
			<td class="label" align="right">创建用户：</td>
			<td class="label">${infoEn.username}</td>
		</tr>
	</table>
	<script type="text/javascript">
		$(function() {
			var type = $("#type").val();
			if (type != '' && type != undefined && type != null) {
	//			$('#jsurveytype' + type).css('background-color', '#5FB878');
				$('#jsurveytype' + type).css('background-color', '#009688');
			} else {
				$("#type").val(0);
	//			$('#jsurveytype0').css('background-color', '#5FB878');
				$('#jsurveytype0').css('background-color', '#009688');
				
			}
		})
		var $ = layui.$, active = {
			set : function(othis) {
				var THIS = 'layui-bg-normal', key = othis.data('key'), value = othis.data('value');
				othis.css('background-color', '#009688').siblings().removeAttr('style');
				if (key == 'dczj_type') {
					$('#type').val(value);
				}
			}
		};
		// 其它示例
		$('.typeopr .layui-btn').on('click', function() {
			var othis = $(this), type = othis.data('type');
			active[type] ? active[type].call(this, othis) : '';
		});
	</script>
</body>
</html>