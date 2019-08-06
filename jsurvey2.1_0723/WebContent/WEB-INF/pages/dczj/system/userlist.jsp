<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>机构列表</title>
<h:head pagetype="page" grid="true" tree="true" iconfont="true"></h:head>
<style type="text/css">
	#menu-wrap {
	position: absolute;
	z-index: 10;
	border-right: 3px solid #EFEFEF;
	}
	
	body {
		margin: 0;
		padding: 0;
		/*background-color: #F6F6F6;*/
	}
	
	#searchbtn {
		cursor: pointer;
	}
	
	#page-wrap {
	position: absolute;
	z-index: 10;
	right: 0;
}
</style>
<script type="text/javascript">
	$(function() {
		$('#page-wrap').width($(top.window).width() - 460);
		var zNodes = ${tree};
		var setting = {
			async : {
				enable : true,
				url : '${contextPath}/manager/system/menuwithurlforuser_search.do',
				autoParam : [ "id=groupId", "isDisabled" ]
			},
			callback:{
				onClick:function(event,treeId,treeNode){
					var url;
					if(treeNode.id == 'outside'){
						url = "${contextPath}/manager/outsideuser/list.do";
					}else{
						url = "${contextPath}/manager/user/list.do?groupId="+treeNode.id;
					}
					$("#grouplist").attr("src",url);
				}
			}
		};
		$('#tree').tree(setting, zNodes);

		$('#searchbtn').click(searchGroup);
		$('#searchtext').keyup(function(event) {
			if (event.keyCode == 13) {
				searchGroup();
			}
		});
		
		function searchGroup() {
			var searchText = $('#searchtext').val();
			if ($.trim(searchText) == '') {
				return;
			}
			//top.page.location.href = "${contextPath}/manager/group/list.do?groupId=-1&searchText="+searchText;
			var texturl = "${contextPath}/manager/user/list.do?groupId=-1&searchText="+searchText;
			$("#grouplist").attr("src",texturl);
		}
	});

</script>
</head>
<body style="height: 100%;width: 100%">
	<div id="menu-wrap" style="height: 100%;">
		<div style="height:30px;background-color:#EFEFEF;padding:10px;border-bottom:1px solid #CCC;">
			<span class="input-append">
				<input id="searchtext" type="text" class="input-text" style="width:180px;" placeholder="请输入机构名称或标识"/><i id="searchbtn" class="iconfont add-on">&#xf5007;</i>
			</span>
		</div>
		<div id="treewrap" style="position:absolute;top:51px;bottom:0;left:0;right:0;overflow:auto;">
			<ul id="tree" class="ztree"></ul>
		</div>
	</div>
	<div id="page-wrap" style="height: 100%;width: 80%;">
		<iframe src="${contextPath}/manager/user/list.do" name="grouplist" id="grouplist" frameborder="0" allowTranspancy="true" height="100%;" width="100%"></iframe>
	</div>
</body>
</html>