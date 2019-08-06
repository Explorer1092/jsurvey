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
<title>角色列表</title>
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
				ajaxSubmit("remove.do?ids=" + ids, {
					success:function(result){
						if(result.success){
							location.reload();
						}else{
							alert(result.message);
						}
					}
				});
			});
			break;
		case 'add':
			openDialog('role/add_show.do', 550, 320, {
				title : '角色新增'
			});
			break;
		}
	}
	function edit(id, name) {
		openDialog('role/modify_show.do?iid=' + id, 550, 320, {
			title : '角色编辑'
		});
	}

	function modifyIsDefault(iid, checkbox) {
		var isDefault = checkbox.checked ? 1 : 0; //切换当前角色缺省状态
		ajaxSubmit('isdefault_modify.do', {
			data : "iid=" + iid + "&isDefault=" + isDefault,
			success:function(result){
				if(!result.success){
					alert(result.message);
				}
			}
		});
	}
	
	function setMembers(id) {
		openDialog('role/members/list.do?roleId=' + id + '&pageSize=10', 600, 570, {
			title : '角色成员设置'
		});
	}
	
	function setRight(id) {
		openDialog('role/rights/modify_show.do?iid=' + id , 550, 300, {
			title : '角色权限设置'
		});
	}
</script>
</head>
<body>
	<div id="page-title">
		<span id="page-location">角色管理</span>
	</div>
	<div id="page-content">
		<h:grid></h:grid>
	</div>
</body>
</html>