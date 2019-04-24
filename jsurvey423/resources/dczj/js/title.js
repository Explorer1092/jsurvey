var webid = $('#webid').val();
var cookieValue = $.cookie('dczj_type');
if(cookieValue != '' && cookieValue != undefined && cookieValue != null){
	$('#dczj_type').val(cookieValue);
	$('#jsurvey'+cookieValue).css('background-color', '#5FB878');
}else{
	$('#dczj_type').val(0);
	$('#jsurvey0').css('background-color', '#5FB878');
}
dczjTable('');

// 新增
function oprTitleAdd() {
	var type = $('#dczj_type').val();
	$.post('add_show.do', {
		"webid" : webid,
		"type" : type
	}, function(str) {
		layer.open({
			type : 1,
			title : '创建问卷',
			area : [ '650px', '330px' ],
			content : str,
			btn : [ '确认添加', '放弃编辑' ],
			yes : function(index, layero) {
				var url = "add_submit.do";
				var titlename = $('#titlename').val();
				var type = $('#type').val();
				var username = $('#username').val();
				$.ajax({
					type : "post",
					url : url,
					data : {
						"webid" : webid,
						"titlename" : titlename,
						"type" : type,
						"username" : username
					},
					async : false,
					cache : false,
					success : function(result) {
						if (result.success) {
							var jsurveytype = result.params.type;
							if(jsurveytype != '' && jsurveytype != undefined && jsurveytype != null){
								$.cookie('dczj_type', jsurveytype);
							}
							layer.close(index);
							window.location.reload();
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


function titleinfoedit(dczjid) {
	$.post('titlemodifyshow.do', {
		"dczjid" : dczjid
	}, function(str) {
		layer.open({
			type : 1,
			title : '编辑问卷',
			area : [ '650px', '330px' ],
			content : str,
			btn : [ '确认编辑', '放弃编辑' ],
			yes : function(index, layero) {
				var url = "titlemodifysubmit.do";
				var titlename = $('#titlename').val();
				var dczjid = $('#dczjid').val();
				$.ajax({
					type : "post",
					url : url,
					data : {
						"titlename" : titlename,
						"dczjid" : dczjid
					},
					async : false,
					cache : false,
					success : function(result) {
						if (result.success) {
							layer.close(index);
							window.location.reload();
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

// 加载列表页
function dczjTable(titlename) {
	var dczj_type = $('#dczj_type').val();
	var dczj_state = $('#dczj_state').val();
	var math = Math.round(Math.random() * 10000000);
	var url = 'titlejson.do?webid=' + webid + '&dczjtype=' + dczj_type+ '&dczjstate=' + dczj_state+'&titlename='+titlename+'&Math='+math;
	layui.use('table', function() {
		var table = layui.table;
		table.render({
			elem : '#dczjtable',
			url : url,
			cols : [ [ {
				field : 'titlename',
				title : '问卷标题'
			}, {
				field : 'state',
				title : '状态',
				width : '8%',
				align : 'center'
			}, {
				field : 'edit',
				title : '编辑',
				width : '8%',
				align : 'center'
			}, {
				field : 'preview',
				title : '预览问卷',
				width : '8%',
				align : 'center'
			}, {
				field : 'share',
				title : '分享问卷',
				width : '8%',
				align : 'center'
			}, {
				field : 'number',
				title : '回收份数',
				width : '8%',
				align : 'center'
			}, {
				field : 'publish',
				title : '发布',
				width : '8%',
				align : 'center'
			}, {
				field : 'operation',
				title : '操作',
				width : '12%',
				align : 'center'
			}, {
				field : 'creattime',
				title : '创建时间',
				width : '14%',
				align : 'center'
			} ] ],
			id : 'dczjtableReload',
			page : true
		});
	});
}

// 点击类型和状态时的方法
layui.use([ 'carousel', 'form' ],
	function() {
		var $ = layui.$, active = {
			set : function(othis) {
			    var THIS = 'layui-bg-normal', key = othis.data('key'), value = othis.data('value');
			    othis.css('background-color', '#5FB878').siblings().removeAttr('style');
			    if (key == 'indicator_dczj') {
				    $('#dczj_type').val(value);
				    $.cookie('dczj_type', value);
			    } else if (key == 'indicator_state') {
				    $('#dczj_state').val(value);
			    }
			dczjTable('');
		    }
	    };
	    // 其它示例
	    $('.demoTest .layui-btn').on('click', function() {
		    var othis = $(this), type = othis.data('type');
		    active[type] ? active[type].call(this, othis) : '';
	    });
    });

// 检索
function searchtitle(){
	var titlename = $('#title').val();
	if(titlename != ''){
		dczjTable(titlename);
	}else{
		dczjTable('');
	}
}

// 排序
function sort(dczjId,type,sorttype){
	if(webid == 0){
		layer.msg('请先选择具体的站点！', {
			icon : 2
		});
		return
	}else{
		var url = "sort.do";
		$.ajax({
			type : "post",
			url : url,
			data : {
				"webid" : webid,
				"type" : type,
				"dczjId" :dczjId,
				"sorttype" : sorttype
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					dczjTable('');
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	}
	
}

// 删除
function remove(dczjid){
	layer.confirm('你确定要删除这条信息吗', function(index){
		layer.close(index);
		$.ajax({
			type : "post",
			url : 'remove.do',
			data : {
				"dczjid" : dczjid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					dczjTable('');
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	});  
}

function titleedit(dczjid){
	parent.checkpage(dczjid);
}

function titlepublish(dczjid){
	var aletMessage = "您确定要发布此调查吗？";
	layer.confirm(aletMessage, function(index){
		layer.close(index);
		ajaxSubmit("dopublish.do?dczjid="+dczjid,{
			success:function(result){
				if(result.success){
					location.reload();
				}else
					alert(result.message);
			}
		});
	});
}

function titleshare(dczjid){
	$.post('titleshare.do', {
		"dczjid" : dczjid
	}, function(str) {
		layer.open({
			type : 1,
			title : '问卷分享',
			area : [ '530px', '430px' ],
			content : str,
		})
	})
}

function titlepreview(dczjid){
	location.href="dopreview.do?dczjid="+dczjid+"&pcstyle=1&webid="+webid;
}