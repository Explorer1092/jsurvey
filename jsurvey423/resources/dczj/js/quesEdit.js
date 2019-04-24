var dczjid = $("#dczjid").val();

//刷新页面后进入题目保存的位置
var ques_sessionid = sessionStorage.getItem('ques_sessionid');
if(ques_sessionid != null){
	var ques_num = sessionStorage.getItem('ques_num');
	if($('#surveydiv_'+ques_sessionid).length>0){
		ajaxSubmit("updatequesdiv.do?dczjid="+dczjid, {
			success : function(result) {
				if (result.success) {
					var html = result.message;
					$('#rows3').html(html);
					location.href = "#surveydiv_" + ques_sessionid;
					sessionStorage.removeItem('ques_sessionid');
				}
			}
		});
	}
}

function quessettings(type){
	if(type == 0){
		$("#button-display").removeClass("active-button");
		$("#button-select").addClass("active-button");
		$("#selectques").css("display","");
		$("#displaysettings").css("display","none");
	}else if(type == 1){
		$("#button-select").removeClass("active-button");
		$("#button-display").addClass("active-button");
		$("#selectques").css("display","none");
		$("#displaysettings").css("display","");
	}
}
//新增题目
function addques(type){
	$.ajax({
		type : "post",
		url : "addques.do",
		data : {
			"dczjid" : dczjid,
			"type" : type
			},
		success : function(result) {
			if (result.success) {
				var quesid = result.params.quesid;
				addqueshtml(quesid);
			} else {
				layer.msg(result.message, {
					icon : 2
				});
			}
		}
	})
}

//新增题目后生成的html
function addqueshtml(quesid){
	$.ajax({
		type : "post",
		url : "addqueshtml.do",
		data : {
			"quesid" : quesid
			},
		success : function(result) {
			if (result.success) {
				var html = result.message;
				$("#rows3").append(html);
				location.href = "#surveydiv_" + quesid;
			} else {
				layer.msg(result.message, {
					icon : 2
				});
			}
		}
	})
}

//按钮显示
function showbutton(quesid) {
	var h2 = $("div.surveylist");
	for ( var i = 0; i < h2.length; i++) {
		var divListId = h2.eq(i).attr("id");
		$("#" + divListId + "").css("box-shadow", "");
	};
	var h3 = $("div.surveybutton");
	for ( var i = 0; i < h3.length; i++) {
		var divId = h3.eq(i).attr("id");
		$("#" + divId + "").css("display", "none");
	};
	var h4 = $("div.surveyAnsw");
	for ( var j = 0; j < h4.length; j++) {
		var divAnswId = h4.eq(j).attr("id");
		$("#" + divAnswId + "").css("display", "none");
	};
	var buttonid = "surveybutton_" + quesid;
	var surveyid = "surveydiv_" + quesid;
	$("#" + buttonid + "").css("display", "block");
	$("#" + surveyid + "").css("box-shadow","1px 1px 10px #000000"); 
}

//编辑题目及答案
function edit(quesid) {
	var count = $('#surveyAnsw_' + quesid).length;
	if (count <= 0) {
		ajaxSubmit("editques.do?dczjid="+dczjid+"&quesid=" + quesid, {
			success : function(result) {
				if (result.success) {
					var h3 = $("div.surveyAnsw");
					for ( var i = 0; i < h3.length; i++) {
						var divId = h3.eq(i).attr("id");
						$("#" + divId + "").css("display", "none");
					};
					var html = result.message;
					$("#" + quesid).append(html);
					var mustfill = result.params.mustfill;
					var showpublish = result.params.showpublish;
					var dczjtype = result.params.dczjtype;
					var type = result.params.type;
					if(mustfill == 1){
						$("#mustfill_"+quesid).attr("checked","");
					}
					if(showpublish == 1){
						$("#showpublish_"+quesid).attr("checked","");
					}
					if(dczjtype == 1){
						$("#dczjtype_"+quesid).attr("checked","");
					}
					form.render();
					if(type == 5){
						var validaterules = result.params.validaterules;
						var select = 'dd[lay-value=' + validaterules + ']';
						$('#validaterules').siblings("div.layui-form-select").find('dl').find(select).click();
				        form.render('select','validaterules');
					}
					var num = result.params.num;
					var json = result.params.answJson;
					if(json != undefined){
						var jsonAnswId = JSON.parse(json);
						for ( var i in jsonAnswId) {
							$("#allowFillInAir_"+jsonAnswId[i].answId).toggle();
						}
					}
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		});
	} else {
		var h3 = $("div.surveyAnsw");
		for ( var i = 0; i < h3.length; i++) {
			var divId = h3.eq(i).attr("id");
			$("#" + divId + "").css("display", "none");
		};
		$('#surveyAnsw_' + quesid).css("display", "block");
	}
}

//删除题目
function deleteques(quesid) {
	layer.confirm('您确定要删除该选项？', function(index){
		layer.close(index);
		$.ajax({
			type : "post",
			url : 'removeques.do',
			data : {
				"dczjid" : dczjid,
				"quesid" : quesid
			},
			success : function(result) {
				if (result.success) {
					$("#" + quesid).remove();
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	});  
}

//排序
function sort(quesid,type) {
	ajaxSubmit("sortques.do?dczjid="+dczjid+"&quesid=" + quesid + "&type=" + type, {
		success : function(result) {
			if (result.success) {
				var sortQuesId = result.params.sortQuesId;
				if (type == 1) {
					$("#" + quesid).after($("#" + sortQuesId));
				} else if (type == 2) {
					$("#" + sortQuesId).after($("#" + quesid));
				}
			} else {
				alert(result.message);
			}
		}
	});
}

//高级设置
function advancedsetting(quesid){
	if($("#setting_table_"+quesid).css('display') === 'none'){
		$("#setting_table_"+quesid).css('display','');
		$("#setting_img_"+quesid).attr('src',"../../resources/dczj/images/ques/takeup.png");
	}else{
		$("#setting_table_"+quesid).css('display','none');
		$("#setting_img_"+quesid).attr('src',"../../resources/dczj/images/ques/open.png");
	}
}

//答案新增
function addAnsw(quesid, type) {
	ajaxSubmit("addansw.do?dczjid="+dczjid+"&quesid=" + quesid, {
		success : function(result) {
			if (result.success) {
				var answid = result.params.newAnswId;
				ajaxSubmit("addanswhtml.do?quesid=" + quesid + "&answid="+ answid + "&type=" + type, {
					success : function(result) {
						if (result.success) {
							var html = result.message;
							$("#surveyAnsw_tbody_" + quesid).append(html);
							$("#allowFillInAir_"+answid).toggle();
						}
					}
				});
			} else {
				alert(result.message);
			}
		}
	});
}

//删除答案
function removeAnsw(answid) {
	layer.confirm('您确定要删除该答案？', function(index){
		layer.close(index);
		ajaxSubmit("removeansw.do?answid=" + answid, {
			success : function(result) {
				if (result.success) {
					$("#survey_answ_" + answid).remove();
				} else {
					alert(e.message);
				}
			}
		});
	});  
}

//排序
function sortAnsw(quesid, answid, type) {
	ajaxSubmit("sortansw.do?quesid=" + quesid + "&answid=" + answid
			+ "&type=" + type, {
		success : function(result) {
			if (result.success) {
				var sortAnswId = result.params.sortAnswId;
				if (type == 1) {
					$("#survey_answ_" + answid).after($("#survey_answ_" + sortAnswId));
				} else if (type == 2) {
					$("#survey_answ_" + sortAnswId).after($("#survey_answ_" + answid));
				}
			} else {
				alert(result.message);
			}
		}
	});
}

//保存
function divsubmit(quesId,type) {
	if(type == 1 || type == 2){
		var min = parseInt($('#min_'+quesId).val());
		var max = parseInt($('#max_'+quesId).val());
		if(min < max){
			if(min > 0){
				sessionStorage.setItem("ques_sessionid",quesId);
				$("#oprform_"+quesId).submit();
//					setTimeout("reloadHtml();",20);
			}else{
				if(type == 1){
					alert("至少选择项必须为正整数！");
				}else{
					alert("最少填写字数必须为正整数！");
				}
			}
		}else{
			if(type == 1){
				alert("至少选择项必须少于至多选择项！");
			}else{
				alert("最少填写字数必须少于最多填写字数！");
			}
		}
	}else{
		sessionStorage.setItem("ques_sessionid",quesId);
		$("#oprform_"+quesId).submit();
//		setTimeout("reloadHtml();",20);
	}
}

//选择依赖
function selectAnswId(quesid){
	$.post('selectAnswId.do', {
		"dczjid" : dczjid,
		"quesid" : quesid
	}, function(str) {
		layer.open({
			type : 1,
			title : '显示依赖选择',
			area : [ '550px', '400px' ],
			content : str,
			btn : [ '确认添加', '放弃编辑' ],
			yes : function(index, layero) {
				var answid = $("#selectAnswId").val();
				$.ajax({
					type : "post",
					url : 'selectAnswIdSubmit.do',
					data : {
						"quesid" : quesid,
						"answid" : answid
					},
					cache : false,
					success : function(result) {
						if (result.success) {
							layer.close(index);
						} else {
							layer.msg(result.message, {
								icon : 2
							});
						}
					}
				})
			}
		})
	})
}

function clearAnswId(quesId){
	layer.confirm('您确定要删除该答案？', function(index){
		ajaxSubmit("clearAnswId.do?quesId="+quesId,{
			success:function(result){
			   if(result.success){
				   layer.close(index);
			   }else{
				   layer.msg("操作失败，请联系管理员！", {
						icon : 2
					});
			   }
		   }
	    });
	});  
}
//导出
function surveyexport(){
	iframeSubmit("quesexport.do?dczjid="+dczjid);
}
//导入
function surveyimport(){
	openDialog('dczj/quesimportshow.do?dczjid='+dczjid, 600, 170, {
		title : '问题导入'
	});
}
//复制
function copyques(quesid){
	layer.confirm('您确定要复制该选项？', function(index){
		ajaxSubmit("copyques.do?quesId=" + quesid, {
			success : function(result) {
				if (result.success) { 
					layer.close(index);
					var newQuesId = result.params.newquesId;
					var newQuesHtml = result.params.newQuesHtml;
					$("#rows3").append(newQuesHtml);
					location.href = "#surveydiv_" + newQuesId;
				} else {
					alert(result.message);
				}
			}
		});
	});  
}
//图片上传
function uploadpic(quesid,answid){
	openDialog('dczj/answimg_import.do?dczjid='+dczjid+'&quesid='+quesid+'&answid='+answid,
			550, 200, {	title : '上传图片'
	});
}
//给图片名字赋值
function crossname(newFileName,answid){
	var answ_imgname = document.getElementById("answ_imgname_"+answid);  
	answ_imgname.value = newFileName;
}

function addanswdisplay(answid){
	$('#answ_delete_'+answid).css("display","");
}

function deletepic(answid){
	confirm("您确定要删除该选项的图片？", function(){
		$("#answ_imgname_"+answid).val("");
		$('#answ_delete_'+answid).css("display","none");
	});
}