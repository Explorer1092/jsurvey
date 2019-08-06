<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<h:head pagetype="page" grid="true"></h:head>
</head>
<style type="text/css">
#jactadd {
	cursor: pointer;
}

#jactdelete {
	margin-left: 5px;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	$(function() {
		$("#grid_simple_search").attr({
			maxlength : "50"
		});
		var html = '${btn}';
		html = html.myReplace('&lt;', '<').myReplace('&gt;', '>').myReplace('&quot;', '"');
		$(".datagrid-toolbar-btn-wrap").html(html);
		$("#jactadd").click(function() {
// 			location.href="${contextPath}/manager/jact/add_show.do?webId=${webId}";
			openDialog('${contextPath}/manager/datacall/add_show.do?webId=${webId}', 520, 480, {
				title : '数据调用 &nbsp;>&nbsp;新增'
			});
		});
		$("#jactdelete").click(function() {
			var ids = getCheckedIds();
			if (ids == "") {
				alert("未选中任何记录");
				return;
			}
			confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
				ajaxSubmit("remove.do?ids=" + ids, {
					success : function(result) {
						if (result.success) {
							location.reload();
						} else
							alert(result.message);
					}
				});
			});
		});
	});

	//编辑调查表
	function edit(i_id) {
		var webIdtest = $("#webId").val();
		openDialog('${contextPath}/manager/datacall/modify_show.do?i_id=' + i_id + '&webId=' + webIdtest, 520, 480, {
			title : '数据调用 &nbsp;>&nbsp;编辑'
		});
// 		location.href='${contextPath}/manager/jact/modify_show.do?i_id=' + i_id + '&webId=' + webIdtest;
	}
	
	String.prototype.myReplace = function(f, e) {//吧f替换成e
		var reg = new RegExp(f, "g"); //创建正则RegExp对象   
		return this.replace(reg, e);
	};
	
	function settempstyle(jactid,type){
		location.href='${contextPath}/manager/datacalltempstyle/showtempstyle.do?jactid=' + jactid+'&type='+type;
	}
	
	function publish(jactid){
		var aletMessage = "您确定要发布吗？";
		confirm(aletMessage, function(){
			ajaxSubmit("${contextPath}/manager/datacallpublish/dopublish.do?jactid="+jactid,
					{
						success:function(result){
							if(result.success){
								location.reload();
							}else
								alert(result.message);
						}
					}	
				);
		});
	}
	
	function showfrontpage(jactid){
		var url = "../../jsurvey/list/index_"+jactid+".html";
		window.open(url);
	}
</script>
<body>
	<div id="page-title">
		 系统管理 / <span id="page-location">数据调用</span>
	</div>
	<div id="page-content">
		<input type="hidden" id="webId" value="${webId}">
		<h:grid></h:grid>
	</div>
</body>
</html>