var divheight = $("#rows1").height();
var contentheight = divheight - 116;
var dczjid = $("#dczjid").val();
var type = $("#type").val();
$("#div-content").css('height',contentheight);

function displayimg(obj){
	$(".icon-btn",obj).css("display","");
}

function hiddenimg(obj) {
	$(".icon-btn",obj).css("display","none");
}

function styleimgcheck(iid,obj){
	$(".styimg").removeAttr('style');
	$(obj).css("border","2px solid #5CB85C").css("box-shadow","1px 1px 10px #000000");
	$("#"+iid).prop("checked", true);
	var path = $("#contextPath").val();
	$("#iframepage").attr("src",path+"/manager/tempalte/iframetemplate.do?commoncomplateid="+iid+"&dczjid="+dczjid+"&type=0")
}

function applicationtemplate(){
	var iid  = $('input[name="styleimgradio"]:checked').val();
	if (iid==undefined) {
		alert("未选中任何模板！");
		return;
	}
	layer.confirm('你确定要应用这条模板吗？', function(index){
		layer.close(index);
		$.ajax({
			type : "post",
			url : 'applicationtemplate.do',
			data : {
				"commoncomplateid" : iid,
				"dczjid" : dczjid,
				"type" : type
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					window.location.reload();
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	});
}

function custom(){
	$.post('showtemplateimport.do', {
		"dczjid" : dczjid,
		"type" : type
	}, function(str) {
		layer.open({
			type : 1,
			title : '自定义模板',
			area : [ '450px', '330px' ],
			content : str,
			btn : [ '确认添加', '放弃编辑' ],
			yes : function(index, layero) {
				$("#oprform").submit();
			}
		})
	})
}



function editTemplate(templateid){
	location.href="showCommonTemplate.do?dczjid="+dczjid+"&type="+type+"&templateid="+templateid;
}

function deleteTemplate(templateid){
	layer.confirm('你确定要删除这条模板吗？', function(index){
		layer.close(index);
		$.ajax({
			type : "post",
			url : 'deleteTemplate.do',
			data : {
				"templateid" : templateid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					window.location.reload();
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	});
}

function downloadtemplate(commoncomplateid){
	location.href='downloadtemplate.do?commoncomplateid='+commoncomplateid+'&dczjid='+dczjid+'&type='+type;
}