function changePage(url, type) {
	$.ajax({
		type : "post",
		url : "setquessession.do",
		data : {
			"url" : url,
			"dczjid" : dczjid,
			"type" : type
		},
		async : false,
		cache : false,
		success : function(result) {
			if (result.success) {
				$(".ques-title-td").removeClass("ques-title-td-active");
				$("#ques-page" + type).addClass("ques-title-td-active");
				if(type!="3"){
					$("#quespage").attr("src", url);
					$("#divquespage").css("display","");
					$("#divquespage1").css("display","none");
				}else{
					$("#quespage1").attr("src", url);
					$("#divquespage").css("display","none");
					$("#divquespage1").css("display","");
				}
			} else {
				alert("操作失败，请联系管理员！");
			}
		}
	})
}

function backhomepage() {
	$.ajax({
		type : "post",
		url : "backhomepage.do",
		data : {
			"dczjid" : dczjid
		},
		async : false,
		cache : false,
		success : function(result) {
			if (result.success) {
				var webid = result.params.webid;
				parent.refresh(webid);
			} else {
				alert("操作失败，请联系管理员！");
			}
		}
	})
}


function titlepublish(dczjid){
	confirm("您确定要发布此调查吗？",function(){
		$.ajax({
			type : "post",
			url : 'checkpublish.do',
			data : {
				"dczjid" : dczjid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					ajaxSubmit("dopublish.do?dczjid="+dczjid,{
						success:function(result){
							if(result.success){
								location.reload();
							}else
								alert(result.message);
						}
					});
				} else {
				    alert(result.message);
				}
			}
		})
	});
}