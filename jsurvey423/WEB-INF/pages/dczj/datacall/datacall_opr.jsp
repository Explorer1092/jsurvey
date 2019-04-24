<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="dialog" validity="true" dialog="true" toggle="true"
	tip="true" select="true" tabs="true"></h:head>
</head>
<style type="text/css">
</style>
<script type="text/javascript">
	$(function() {
		var iid = $('#iid').val();
		if(iid != null && iid != undefined && iid != ''){
			if('${en.datacall_type}' == 0){
				$('#type_1').attr('disabled','true');
			}else{
				$('#type_0').attr('disabled','true');
			}
		}
		
		$("#oprform").validity(function() {
			$("#name").require("请输入模板名称");
			$("#webnames").require("请选择所属站点");
			$("#number").require("请输入数据行数");
		}, {
			success : function(result) {
				if (result.success) {
					closeDialog(true);
				} else {
					alert(result.message);
				}
			}
		});
	});
	
	function selectWeb(){
		var webids = $("#jact_webids").val();
		openDialog('${contextPath}/manager/datacall/selectWeb.do?jact_webids='+webids, 400, 400,{
		    title : '网站管理'
		});
	}
</script>
<body>
	<form action="${url }" method="post" id="oprform">
	    <input type="hidden" id="jact_webids" name="datacall_webids" value="${en.datacall_webids}">
	    <input type="hidden" id="iid" name="iid" value="${en.iid}">
	    <input type="hidden" id="webid" name="webid" value="${en.webid}">
	    <!-- <input type="hidden" id="createdate" name="createdate" value="${en.createdate}"> -->
	    <input type="hidden" id="createname" name="createname" value="${en.createname}">
	    <input type="hidden" id="state" name="state" value="${en.state}">
	    <input type="hidden" id="updatehtml" name="updatehtml" value="${en.updatehtml}">
		<div id="dialog-content">
			<table class="table">
				<tr>
					<td class="label" align="right">模板名称:</td>
					<td><input type="text" class="input-text" id="name" name="datacall_name"
						value="${en.datacall_name}">
					</td>
				</tr>
				<tr>
					<td class="label" align="right">所属站点:</td>
					<td><input type="text" class="input-text" id="webnames"
						name="datacall_webnames" value="${en.datacall_webnames}"
						readonly="readonly" onclick="selectWeb();" style="cursor: pointer;">
					</td>
				</tr>
				<tr>
					<td class="label" align="right">数据类别:</td>
					<td><input type="checkbox" name="datacall_types" value="0"
						data-value="${en.datacall_types}">在线调查 <input type="checkbox"
						name="datacall_types" value="1" data-value="${en.datacall_types}">意见征集
						 <input type="checkbox"
						name="datacall_types" value="2" data-value="${en.datacall_types}">网上投票
					</td>
				</tr>
				<tr>
					<td class="label" align="right">展示方式:</td>
					<td><input type="radio" id="type_0" name="datacall_type" value="0"
						data-value="${en.datacall_type}">固定长度 <input type="radio" id="type_1"
						name="datacall_type" value="1" data-value="${en.datacall_type}">列表页
					</td>
				</tr>
				<tr>
					<td class="label" align="right"><h:tip title="若选择列表页则为分页条数"></h:tip>数据行数:</td>
					<td><input type="text" class="input-text" id="number" name="datacall_number"
						value="${en.datacall_number}"></td>
				</tr>
				<tr>
					<td class="label" align="right">展现顺序:</td>
					<td>页面按照&nbsp;&nbsp;<select name="datacall_sorttype" style="width: 100px;"
						data-value="${en.datacall_sorttype}"><option value="0">创建时间</option>
							<option value="1">开始时间</option>
							<option value="2">结束时间</option>
					</select>&nbsp;&nbsp; <select name="datacall_sort" style="width: 70px;"
						data-value="${en.datacall_sort}"><option value="0">逆序</option>
							<option value="1">顺序</option>
					</select>&nbsp;&nbsp;排序</td>
				</tr>
				<tr>
					<td class="label" align="right">展现条件:</td>
					<td><select name="filtercondition"
						data-value="${en.filtercondition}"><option value="0">不限</option>
							<option value="1">只展现未开始的</option>
							<option value="2">只展现进行中的</option>
							<option value="3">只展现已结束的</option>
					</select></td>
				</tr>
			</table>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" /> <input
					type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>