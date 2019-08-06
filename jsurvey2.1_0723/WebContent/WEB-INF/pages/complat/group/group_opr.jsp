<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<title>机构管理</title>
<h:head pagetype="dialog" multiselect="true" tree="true" validity="true"></h:head>

<script type="text/javascript">
	function randomChar(num) {
		var jsSeed = "0123456789qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		var jsTmp = "";
		if (num == '' || num <= 0) {
			num = 12;
		}
		for ( var i = 0; i < num; i++) {
			jsTmp += jsSeed.charAt(Math.ceil(Math.random() * 1000000)
					% jsSeed.length);
		}
		return jsTmp;
	}

	function getRandomCode(num) {
		$('#codeid').val(randomChar(num));
	}

	/**
	 *	机构选择节点点击前回调
	 */
	function beforeClick(treeId, treeNode, clickFlag) {
		if (treeNode.isDisabled)
			return false;
		return (treeNode.id != 0);
	}

	/**
	 *	机构选择节点点击回调
	 */
	function onClick(event, treeId, treeNode) {
		if (treeNode.isDisabled)
			return;
		$('#pid').val(treeNode.id);
		$('#pname').val(treeNode.name);
	}

	function onDblClick(event, treeId, treeNode) {
		if(treeNode == null){
			return;
		}
		if (treeNode.isDisabled)
			return;
		if (treeNode.id == '${rangeId}'){
			onTopClick();
		} else {
			$('#pid').val(treeNode.id);
			$('#pname').val(treeNode.name);
		}
		$('#pname_menu').fadeOut(50);
	}

	/**
	 *	机构选择 根节点 点击回调
	 */
	function onTopClick() {
		$('#pid').val('');
		$('#pname').val("无");
	}
	
	$(function() {
		if('${url }'== 'add_submit.do'){	//新增时自动生成机构标识
			getRandomCode(12);
		}
		$('#pname').menu({
			width: 310,
			height: 250,
			tree : 'groupmenu',
			init : function(menu, obj, treeJq) {
				var setting = {
					async : {
						enable : true,
						url : '${contextPath}/manager/menu/menuforgroup_search.do',
						autoParam : [ "id=groupId", "isDisabled" ],
						otherParam : [ "currentId", $('#iid').val() ] //当前操作机构id
					},
					callback : {
						beforeClick : beforeClick,
						onClick : onClick,
						onDblClick : onDblClick
					}
				};

// 				var rootNode = ${groupMenu};
				//treeJq.tree(setting, rootNode);
				treeJq.tree(setting);
				//treeJq.tree().refreshNode('${rangeId}');
			}
		});
		
		$('#rolenames').multiselect({
			options : ${allRoleOptions},
			selected : ${selectedRoleIds},
			noremove : ${noremoveRoleIds},
			target : 'roleids'
		});
	
		$("#oprform").validity(function() {
				$("#name").require("机构名称不能为空").match('username1',
						"机构名称只能由字母、数字、下划线、中文组成，不能以下划线开头和结尾").maxLength(33,
						"机构名称不能超过33个字");
				if ('${url }' == 'add_submit.do') {
					$("#codeid").require("机构标识不能为空").match('codeid',
						"机构标识只能由字母、数字组成").maxLength(12, "机构标识不能超过12个字");
				}
				$("#spec").maxLength(85, "机构描述不能超过85个字");

		},{
			success:function(result){
				if(result.success){
					/* var treeObj = Tree.getInstance();
					treeObj.refreshNode(result.params.refresh);
					treeObj.removeNode(result.params.remove); */
					parent.frames["page"].location.reload();
					closeDialog(true);
				}else{
					alert(result.message);
				}
			}
		});
		
		$('.clean').click(function() {
			<c:choose>
				<c:when test="${rangeId > 0}">
					$('#pid').val(${rangeId});
					$('#pname').val('${rangeName}');
				</c:when>
				<c:otherwise>
					$('#pid').val(0);
					$('#pname').val('');
				</c:otherwise>
			</c:choose>
		});
	});
</script>
</head>
<body>
	<form action="${url }" method="post" id="oprform">
		<input type="hidden" name="iid" id="iid" value="${group.iid }" /> 
		<input type="hidden" name="pid" id="pid" value="${group.pid}" /> 
		<input type="hidden" name="prevPid" id="prevpid" value="${group.pid}" /> 
		<input type="hidden" id="rangeid" value="${rangeId}" /> 
		<input type="hidden" name="roleIds" id="roleids" value="${roleIds }" />
		<div id="dialog-content">
			<table border="0" align="center" 
				class="table">
				<tr>
					<td align="right" class="label">机构名称</td>
					<td class="required">*</td>
					<td><input type="text" id="name" name="name" maxlength="33"
						class="input-text" value="${group.name }" /></td>
				</tr>
				<tr>
					<td align="right" class="label">机构标识</td>
					<c:choose>
						<c:when test="${url=='modify_submit.do'}">
							<td>&nbsp;</td>
							<td><input type="text" id="codeid" name="codeId"
								class="input-text" value="${group.codeId }" disabled />&nbsp;
							</td>
						</c:when>
						<c:otherwise>
							<td class="required">*</td>
							<td>
								<span class="input-append">
									<input type="text" id="codeid" name="codeId" class="input-text" value="${group.codeId }" style="width: 275px;" /><i class="iconfont add-on button" title="重新生成" onclick="getRandomCode(12);">&#xf5022;</i>
								</span>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td align="right" class="label">所属机构</td>
					<td>&nbsp;</td>
					<td>
						<span class="input-append">
							<input type="text" name="pname" id="pname"
								readonly="readonly" style="width:276px;" class="input-text" value="${group.pname}" /><i class="iconfont add-on inner button clean" style="color:#CCC;" title="清除">&#xf3015;</i>
						</span>
					</td>
				</tr>
				<!-- <tr>
					<td align="right" class="label">角色</td>
					<td>&nbsp;</td>
					<td><ul id="rolenames" class="input-text multiselect"></ul></td>
				</tr> -->
				<tr>
					<td align="right" class="label">机构描述</td>
					<td>&nbsp;</td>
					<td><textarea id="spec" name="spec" class="input-textarea">${group.spec}</textarea>
					</td>
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