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
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<style type="text/css">
.title-top {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 20px;
	border-bottom: 1px solid #CCC;
}
.title-btn {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 5px;
}
.title-table {
	padding-left: 5px;
}
.dczj-btn {
    width: 80px;
    background-color: #5FB878;
}
.sort-pic {
    margin-right: 8px;
    cursor: pointer;
}
.delete-pic {
    cursor: pointer;
}
.table-pic {
    cursor: pointer;
}
.top-pic {
    margin-left: 8px;
    cursor: pointer;
}
</style>

</head>

<body>
    <input type="hidden" id="dczj_type" value="0">
    <input type="hidden" id="dczj_state" value="3">
    <input type="hidden" id="webid" value="${webid}">
    <c:if test="${webid != 0}">
	<div class="title-top">
		<button class="layui-btn layui-btn-normal" style="width: 150px;"
			onclick="oprTitleAdd()">创建题库</button>
	</div>
	</c:if>
	<div class="title-btn">
		<table style="width: 100%">
			<tr>
				<td style="width: 35%">
				<table style="width: 100%">
						<td style="width: 35%"><label class="layui-form-label"></label>
							<div class="layui-btn-group demoTest" style="margin-top: 5px;"></div>
						</td>
						<td style="width: 30%"><label class="layui-form-label" style="width:40px;"></label>
							<div class="layui-btn-group demoTest" style="margin-top: 5px;"></div>
						</td>
					<tr>
						<td style="width: 50%;"><label class="layui-form-label"></label></td>
						<td class="label" align="right">题库标题：</td>
						<td style="width: 30%"><input type="text" name="quesbankname" id="quesbankname" class="layui-input" style="width: 95%;height: 32px;" placeholder="请输入题库标题"></td>					
						<td><button class="layui-btn layui-btn-sm layui-btn-normal" style="width: 70px;" onclick="searchtitle();">检索</button></td>
					</tr>
				</table>
			</td>
			</tr>
		</table>
	</div>
	<div class="title-table">
		<table class="layui-hide" id="quesbanktable" lay-filter="quesbankTableReload"></table>
	</div>
<script src="${contextPath}/resources/dczj/js/jquery.cookie.js"></script>
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<script src="${contextPath}/resources/dczj/js/quesbank.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
<script type="text/javascript">
function oprTitleAdd() {
	if(webid == 0){
		layer.msg('请先选择具体的站点！', {
			icon : 2
		});
		return
	}else{
	var type = $('#dczj_type').val();
	$.post('addquesbank_show.do', {
		"webid" : webid
	}, function(str) {
		layer.open({
			type : 1,
			title : '创建题库',
			area : [ '650px', '330px' ],
			content : str,
			btn : [ '确认添加', '放弃编辑' ],
			yes : function(index, layero) {
				var url = "addquesbank_submit.do";
				var webid = $('#webid').val();
				var newquesbankname = $('#newquesbankname').val();
				var username = $('#username').val();
				var creator = $('#creator').val();
				$.ajax({
					type : "post",
					url : url,
					data : {
						"webid" : webid,
						"quesbankname" : newquesbankname,
						"username" : username,
						"creator":creator
					},
					async : false,
					cache : false,
					success : function(result) {
						alert("success："+result.success);
 						if (result.success) {
							layer.close(index);
							window.location.href = "https://www.baidu.com";
						} else {
							layer.msg(result.message, {
								icon : 2
							});
						}
					}
				})
			}
		})
	})}
}
</script>
</body>
</html>