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
<title>封停列表</title>
<h:head pagetype="page" grid="true"></h:head>
<script type="text/javascript">
	function toolbarAction(action) {
		switch (action) {
		case 'remove':
			var ids = getCheckedIds();
			if (ids == "") {
				alert("未选中任何记录");
				return;
			}
			confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
				ajaxSubmit("remove.do?pid=${pid}&ids=" + ids, {
					success:function(result){
						if(result.success){
							location.reload();
						}
					}
				});
			});
			break;
		}
	}
</script>
</head>
<body>
	<div id="page-title">
		系统参数 / 
		 <span id="page-location">封停管理</span>
	</div>
	<div id="page-content">
		<div class="grid-advsearch">
			用户登录名<input type="text" class="input-text"
				style="width: 120px; margin: 0 30px 0 10px;" /> 用户姓名<input
				type="text" class="input-text"
				style="width: 120px; margin: 0 30px 0 10px;" /> <input type="button"
				class="btn btn-primary" value="检索" /> <input
				type="button" class="btn advsearch-cancel" value="返回" />
			<div class="line-dotted"></div>
		</div>
		<h:grid></h:grid>
	</div>
</body>
</html>