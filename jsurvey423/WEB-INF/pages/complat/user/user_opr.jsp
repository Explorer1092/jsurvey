<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户管理</title>
<h:head pagetype="dialog" multiselect="true" menu="true" tree="true" validity="true" ipass="true" checkpwd="true" tip="true" iconfont="true" cookie="true" security="true"></h:head>
<script>
	var isWebAdminUser = false; //被编辑用户是否为网站管理员
// 	var isGroupAdminUser = false;//被编辑用户是否为机构管理员
	var isSysAdminUser = false;//被编辑用户是否为系统管理员
	
	$(function() {
		//密码输入控件，防自动填充
		$('#pwd').iPass({keyup:function(){
			EvalPwd($(this).next().val());
		}});
		
		var groupMenu = ${groupMenu};
		
		$('#groupname').menu({
			tree : 'groupmenu',
			height : 200,
			init : function() {
				setting('groupmenu', onClickGroup, onDbClickGroup, groupMenu);
			}
		});

		$('#rangename').menu({
			tree : 'rangemenu',
			height : 200,
			init : function() {
				setting('rangemenu', onClickRange, onDbClickRange, groupMenu);
			}
		});

		$('#rolenames').multiselect({
			options : ${allRoleOptions},
			selected : ${selectedRoleIds},
			noremove : ${noremoveRoleIds},
			target : 'roleids',
			callback : selectRole
		});
		
		$("#oprform").validity(function() {
			$("#name").require("姓名不能为空")
						.match('username1', "姓名只能由字母、数字、下划线、中文组成，不能以下划线开头和结尾")
						.maxLength(33, "姓名不能超过33个字");
			$("#loginname").require("登录名不能为空")
					.match('username1', "登录名只能由字母、数字、下划线、中文组成，不能以下划线开头和结尾")
					.maxLength(33, "登录名不能超过33个字");
			if ('${url }' == 'add_submit.do' || $.trim($("#pwd").val()).length > 0) {
				$("#pwd").require("密码不能为空")
					.minLength(6, "密码最少要6个字")
					.maxLength(18, "密码最多18个字");
			}
			$("#email").require("电子邮箱不能为空").match('email','电子邮箱地址不正确').maxLength(80, "电子邮箱地址最多80个字");
			$("#roleids").require("所属角色不能为空");
			$("#groupname").require("所属机构不能为空");
			if(isSysAdminUser == false && isGroupAdminUser){
				$('#rangename').require('机构管理范围必须选择');
			}
			if(isSysAdminUser == false && isWebAdminUser){
				$('#rangeWebNames').require('网站管理范围必须选择');
			}
			$("#headship").maxLength(80, "职务最多80个字");
			$("#phone").maxLength(80, "固定电话最多80个字");
			$("#mobile").maxLength(80, "移动电话最多80个字");
			$("#address").maxLength(80, "联系地址最多80个字");
			$("#contact").maxLength(80, "联系方式最多80个字");
		}, {
			beforeSubmit : function(validateResult) {
				var u_loginname = $('#loginname').val();
				var u_pwd = $('#pwd').val();
				$('#en_loginname').val(RSAencode(u_loginname));
				$('#en_pwd').val(RSAencode(u_pwd));
			},
			success : function(result) {
				if (result.success) {
					closeDialog(true);
				} else {
					alert(result.message);
				}
			}
		});
	});


	/**
	 *	机构选择节点点击回调
	 */
	function onClickGroup(event, treeId, treeNode) {
		$('#groupid').val(treeNode.id);
		$('#groupname').val(treeNode.name);
	}

	function onDbClickGroup(event, treeId, treeNode) {
		if (treeNode == null) {
			return;
		}
		if (treeNode.isDisabled || treeNode.id == '${formBean.rangeId }')//根节点及失效节点双击无效
			return;
		$('#groupid').val(treeNode.id);
		$('#groupname').val(treeNode.name);
		$('#groupname_menu').fadeOut(50);
	}

	function onClickRange(event, treeId, treeNode) {
		$('#rangeid').val(treeNode.id);
		$('#rangename').val(treeNode.name);
	}

	function onDbClickRange(event, treeId, treeNode) {
		if (treeNode == null) {
			return;
		}
		if (treeNode.isDisabled || treeNode.id == '${formBean.rangeId }') //根节点及失效节点双击无效 
			return;
		$('#rangeid').val(treeNode.id);
		$('#rangename').val(treeNode.name);
		$('#rangename_menu').fadeOut(50);
	}

	/**
	 *	初始化树
	 */
	function setting(treeName, onClickFunction, onDblClickFunction, rootNode) {
		var setting = {
			async : {
				enable : true,
				url : '${contextPath}/manager/menu/menuforuser_search.do',
				autoParam : [ "id=groupId", "isDisabled" ]
			},
			callback : {
				beforeClick : beforeClick,
				beforeDblClick : beforeClick,
				onClick : onClickFunction,
				onDblClick : onDblClickFunction
			}
		};
		$("#" + treeName).tree(setting, rootNode);
		$("#" + treeName).tree().refreshNode('${formBean.rangeId}');
	}

	function selectRole() {
		var selectedArray = $('#roleids').val().split(',');
		isSysAdminUser = false;
		isGroupAdminUser = false;
		isWebAdminUser = false;
		$.each(selectedArray, function(i, value) {
			if (value == "2") { /* 用户有调查征集管理员权限 */
				isWebAdminUser = true;
			} else if (value == "1") {
				isSysAdminUser = true;
			} 
		});
		var isSysAdmin = ${currentUser.sysAdmin}; //操作人为系统管理员时
		

		/* 机构管理员操作系统管理员  或者  用户操作自己时  不允许删除角色 */
		if ((!isSysAdmin && isSysAdminUser) || $('#iid').val() == '${currentUser.iid}') {
			$('#rolenames').css('backgroundColor', '#EAEAEA');
			$("li").remove('.multiselect-input');
			$("span").each(function() {
				$(this).remove('.multiselect-delete');
			});
			$('#rangename').attr('disabled', true);
		}

		if (!isSysAdminUser && isGroupAdminUser && !isWebAdminUser) {
			$('#displayrange').show(100, function() {
				$('#rangeid').attr('disabled', false);
				if ($('#rangeid').val().length == 0)
					$('#rangeid').val('${formBean.rangeId}');
				if ($('#rangename').val().length == 0)
					$('#rangename').val('${formBean.rangeName}');
			});
			$('#displaywebrange').hide(0, function() {
// 				$('#rangeid').attr('disabled', true);
			});
		} else if (!isSysAdminUser && !isGroupAdminUser && isWebAdminUser) {
			$('#displaywebrange').show(100, function() {
// 				$('#rangeid').attr('disabled', true);
			});
			$('#displayrange').hide(0, function() {
// 				$('#rangeid').attr('disabled', true);
			});
		} else if (!isSysAdminUser && isGroupAdminUser && isWebAdminUser) {
			$('#displayrange').show(100, function() {
				$('#rangeid').attr('disabled', false);
				if ($('#rangeid').val().length == 0)
					$('#rangeid').val('${formBean.rangeId}');
				if ($('#rangename').val().length == 0)
					$('#rangename').val('${formBean.rangeName}');
			});
			$('#displaywebrange').show(100, function() {
			});
		} else {
			$('#displayrange').hide(0, function() {
				$('#rangeid').attr('disabled', true);
			});
			$('#displaywebrange').hide(0, function() {
// 				$('#rangeid').attr('disabled', true);
			});
		}
	}
	
	/**
	 *	机构选择节点点击/双击前回调，禁止选择“机构选择”
	 */
	function beforeClick(treeId, treeNode, clickFlag) {
		if (treeNode.isDisabled)
			return false;
		return (treeNode.id != 0);
	}
	
	/**
	 *	对textarea进行字数控制
	 */
	function checklength(obj) {
	    var max = $(obj).attr('maxlength');
	    if(max == null || max == "" || max == undefined) {
	        return;
	    }
	    if(obj.value.length > max) {
	        obj.value=obj.value.substring(0,(max-1));
	        return;
	    }
	}
	
	function webRights(userId) {
        openDialog('${contextPath}/manager/user/webRights.do?userId='+userId, 400, 400,{
        	title : '网站管理'
        });
	}
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform" name="oprform">
		<input type="hidden" name="user.iid" id="iid" value="${formBean.user.iid }" />
		<input type="hidden" name="user.groupId" id="groupid" value="${formBean.user.groupId }" />
		<input type="hidden" name="rangeId" id="rangeid" value="${formBean.rangeId }" />
		<input type="hidden" name="roleIds" id="roleids" value="${formBean.roleIds }" />
		<input type="hidden" name="rangeWebIds" id="rangeWebIds" value="${formBean.rangeWebIds }" />
		<div id="dialog-content">
			<table border="0" align="center" class="table">
				<tr>
					<td align="right" class="label">姓名</td>
					<td class="required">*</td>
					<td>
						<input type="text" id="name" name="user.name" class="input-text" maxlength="33" class="input-text" value="${formBean.user.name }" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">登录名</td>
					<c:choose>
						<c:when test="${url=='modify_submit.do'}">
							<td>&nbsp;</td>
							<td>
								<input type="text" name="user.loginName" id="loginname" maxlength="33" class="input-text" value="${formBean.user.loginName }" disabled="disabled" />
								&nbsp;
							</td>
						</c:when>
						<c:otherwise>
							<td class="required">*</td>
							<td>
								<input id="en_loginname" type="hidden" name="user.loginName"/>
								<input type="text" id="loginname" maxlength="33" class="input-text" value="${formBean.user.loginName }" />
								&nbsp;
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td align="right" class="label"><h:tip title="字母、数字、符号组合的密码更安全"></h:tip>密码</td>
					<c:choose>
						<c:when test="${url=='modify_submit.do'}">
							<td>&nbsp;</td>
						</c:when>
						<c:otherwise>
							<td class="required">*</td>
						</c:otherwise>
					</c:choose>
					<td>
						<input id="en_pwd" type="hidden" name="user.pwd"/>
						<input type="password" id="pwd" class="input-text" maxlength="18" pwdpower="pwdpower"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">密码强度</td>
					<td>&nbsp;</td>
					<td>
						<ul id="pwdpower">
							<li id="pweak"></li>
							<li id="pmedium"></li>
							<li id="pstrong"></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">所属机构</td>
					<td class="required">*</td>
					<td>
						<input type="text" name="user.groupName" id="groupname" class="input-text" value="${formBean.groupName}" readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">电子邮箱</td>
					<td class="required">*</td>
					<td>
						<input type="text" name="user.email" id="email" class="input-text" maxlength="80" value="${formBean.user.email}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">角色</td>
					<td class="required">*</td>
					<td>
						<ul id="rolenames" class="input-text multiselect"></ul>
					</td>
				</tr>
<!-- 				<tr id="displayrange"> -->
<!-- 					<td align="right" class="label">机构管理范围</td> -->
<!-- 					<td class="required">*</td> -->
<!-- 					<td> -->
<%-- 						<input type="text" name="user.rangeName" id="rangename" class="input-text" value="${formBean.rangeName}" readonly="readonly" /> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr id="displaywebrange">
					<td align="right" class="label">网站管理范围</td>
					<td class="required">*</td>
					<td>
						<input type="text" name="rangeWebNames" id="rangeWebNames" class="input-text" value="${formBean.rangeWebNames}" readonly="readonly" onclick="webRights(${formBean.user.iid });"/>
					</td>
				</tr>
				</tr>
				<tr>
					<td align="right" class="label">职务</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="user.headship" id="headship" class="input-text" maxlength="80" value="${formBean.user.headship}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">固定电话</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="user.phone" id="phone" class="input-text" maxlength="80" value="${formBean.user.phone}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">移动电话</td>
					<td>&nbsp;</td>
					<td>
						<input type="text" name="user.mobile" id="mobile" class="input-text" maxlength="80" value="${formBean.user.mobile}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">联系地址</td>
					<td>&nbsp;</td>
					<td><input type="text" name="user.address" id="address"
						class="input-text" maxlength="80" value="${formBean.user.address}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">联系方式</td>
					<td>&nbsp;</td>
					<td>
						<textarea name="user.contact" id="contact" class="input-textarea" maxlength="80" 
							onkeydown="checklength(this);" onmousedown="checklength(this);">${formBean.user.contact}</textarea>
					</td>
				</tr>
			</table>
		</div>
		<div id="dialog-toolbar">
			<div id="dialog-toolbar-panel">
				<input type="submit" class="btn btn-primary" value="保存" />
				<input type="button" class="btn" value="取消" onclick="closeDialog();" />
			</div>
		</div>
	</form>
</body>
</html>