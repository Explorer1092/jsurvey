 $.ajax({
	type : "post",
	url : "../manager/dczj/getwebidsession.do",
	data : {},
	async : false,
	cache : false,
	success : function(result) {
		if (result.success) {
			var newwebid = result.params.webid;
			var webname = result.params.webname;
			if(webname != ""){
				$('#webtree').val(webname);
			}
//			var zTree = $.fn.zTree.getZTreeObj("rangemenu");//获取ztree对象
//            var node = zTree.getNodeByParam('id', newwebid);//获取id为1的点
//            zTree.selectNode(node);//选择点
//            zTree.setting.callback.onClick(null, zTree.setting.treeId, node);//调用事件

			$("#page").attr("src","../manager/dczj/titlelist.do?webid="+newwebid);
		}
	}
})
$.ajax({
	type : "post",
	url : "../manager/dczj/gettitlesession.do",
	data : {},
	async : false,
	cache : false,
	success : function(result) {
		if (result.success) {
			var olddczjid = result.params.dczjid;
			if (olddczjid != "" && olddczjid !=null && olddczjid != undefined) {
				checkpage(olddczjid);
			} 
		}
	}
})

function refresh(webid){
	$("#page").attr("src","../manager/dczj/titlelist.do?webid="+webid);
}

function checkpage(dczjid) {
	$.ajax({
		type : "post",
		url : "../manager/dczj/settitlesession.do",
		data : {
			"dczjid" : dczjid
		},
		async : false,
		cache : false,
		success : function(result) {
			if (result.success) {
				var math = Math.round(Math.random() * 10000000);
				$("#page").attr("src","../manager/dczj/quesindex.do?dczjid=" + dczjid+"&value="+math);
			} else {
				alert("请联系管理员");
			}
		}
	})
}