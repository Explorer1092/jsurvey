<%@page language="java" contentType="text/html; charset=UTF-8"
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
<title>角色成员列表</title>
<h:head pagetype="dialog" grid="true" iconfont="true" select="true"></h:head>
<script type="text/javascript">
	function toolbarAction(action) {
		switch (action) {
		case 'remove':
			var ids = getCheckedIds();
			if (ids == "") {
				alert("未选中任何记录");
				return;
			}
			removeMembers(ids);
			break;
		case 'add':
			openDialog(contextPath + '/manager/orgselect.do?orgType=${orgType}', 800, 500, {
				title: '成员新增',
				callback : addMembers
			});
			break;
		case 'clean':
			confirm('您将清空当前角色下的所有成员\n是否继续？', cleanMembers);
			break;
		}
	}
	
	/**
	 * 新增成员
	 */
	function addMembers(users, groups) {
		var groupsArray = new Array();
		var usersArray = new Array();
		
		$.each(groups, function(id) {
			groupsArray.push(id);
		});

		$.each(users, function(id) {
			usersArray.push(id);
		});
		
		ajaxSubmit("modify_submit.do?roleId=${roleId}&users=" + usersArray.join() + "&groups=" + groupsArray.join(), {
			success:function(result){
				if(result.success){
					location.reload();
				}else{
					alert(result.message);
				}
			}
		});
	}

	/**
	 * 删除成员
	 */
	function removeMembers(ids) {
		var groupsArray = new Array();
		var usersArray = new Array();
		
		var idsArray = ids.split(',');
		$.each(idsArray, function(index, id) {
			if (id.indexOf('g_') != -1) {
				groupsArray.push(id.replace('g_', ''));
			} else {
				usersArray.push(id.replace('u_', ''));
			}
		});
		
		confirm("您确定要删除这" + ids.split(",").length + "条记录吗", function() {
			ajaxSubmit("remove.do?roleId=${roleId}&userIds="+usersArray.join()+"&groupIds="+groupsArray.join(), {
				success:function(result){
					if(result.success){
						location.reload();
					}else{
						alert(result.message);
					}
				}
			});
		});
	}
	
	/**
	 * 清空成员
	 */
	function cleanMembers() {
		ajaxSubmit('clean.do?roleId=${roleId}', {
			success:function(result){
				if(result.success){
					location.reload();
				}else{
					alert(result.message);
				}
			}
		});
	}
</script>
<style>
.user {
	color: #226699;
}

.group {
	color: #F88E32;
}

.user i,.group i {
	margin-right: 5px;
}

html {
	overflow: inherit;
}

body {
	overflow-y: scroll;
	padding: 10px;
}
</style>
</head>
<body>
	<div class="grid-advsearch">
		<form>
			成员类型
			<select name="memberType" data-value="${memberType}" style="width: 80px; margin: 0 30px 0 10px;">
				<option value="0">全部</option>
				<option value="1">机构</option>
				<option value="2">用户</option>
			</select>
			成员名称<input name="memberName" type="text" class="input-text" value="${memberName}" style="width: 120px; margin: 0 20px 0 10px;" />
			<input type="button" class="btn btn-primary" value="检索" style="margin-right:5px;"/>
			<input type="button" class="btn advsearch-cancel" value="返回" />
			<div class="line-dotted"></div>
		</form>
	</div>
	<h:grid></h:grid>
</body>
</html>