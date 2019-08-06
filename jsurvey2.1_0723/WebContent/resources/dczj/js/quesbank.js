var webid = $('#webid').val();
quesbankTable('');

// 新增
/*function quesbankAdd() {
	if(webid == 0){
		layer.msg('请先选择具体的站点！', {
			icon : 2
		});
		return
	}else{
	$.post('addquesbank_show.do', {
		"webid" : webid
	}, function(str) {
		layer.open({
			title : '创建题库',
			type : 1,
			area : [ '650px', '330px' ],
			content : str,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				var url = "addquesbank_submit.do?";
				var quesbankname = $('#quesbankname').val();
				var username = $('#username').val();
				var creator = $('#creator').val();
				$.ajax({
					type : "post",
					url : url,
					data : {
						"webid" : webid,
						"quesbankname" : quesbankname,
						"username" : username,
						"creator" :creator
					},
				    async : true,
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
	})}
}*/


function titleinfoedit(dczjid) {
	$.post('titlemodifyshow.do', {
		"dczjid" : dczjid
	}, function(str) {
		layer.open({
			type : 1,
			title : '编辑题库',
			area : [ '650px', '330px' ],
			content : str,
			btn : [ '确认', '取消' ],
			yes : function(index, layero) {
				var url = "titlemodifysubmit.do";
				var quesbankname = $('#quesbankname').val();
				var dczjid = $('#dczjid').val();
				$.ajax({
					type : "post",
					url : url,
					data : {
						"quesbankname" : quesbankname,
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

// 加载题库列表页
function quesbankTable(quesbankname) {
	var url = 'questitlejson.do?webid=' + webid +'&quesbankname='+quesbankname;
	layui.use('table', function() {
		var table = layui.table;
		table.render({
			elem : '#quesbanktable',
			url : url,
			cols : [ [ {
				field : 'quesbankname',
				title : '题库名称',
				templet:'<div><span title="{{d.quesbankname}}";display:block;>{{d.quesbankname}}</span></div>',  
				width : '10%',
			}, {
				field : 'creator',
				title : '创建人',
				width : '30%',
				align : 'center'
			}, {
				field : 'createtime',
				title : '创建时间',
				width : '30%',
				align : 'center'
			}, {
				field : 'operation',
				title : '操作',
				width : '30%',
				align : 'center'
			} ] ],
			id : 'quesbankTableReload',
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
			    othis.css('background-color', '#009688').siblings().removeAttr('style');
			    if (key == 'indicator_dczj') {
				    $('#dczj_type').val(value);
				    $.cookie('dczj_type', value);
			    } else if (key == 'indicator_state') {
				    $('#dczj_state').val(value);
			    }
			    quesbankTable('');
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
	var quesbankname = $('#quesbankname').val();
	if(quesbankname != ''){
		quesbankTable(quesbankname);
	}else{
		quesbankTable('');
	}
}


// 删除
function remove_quesbank(dczjid){
	layer.confirm('你确定要删除这条信息吗', function(index){
		layer.close(index);
		$.ajax({
			type : "post",
			url : 'remove_quesbank.do',
			data : {
				"dczjid" : dczjid
			},
			async : false,
			cache : false,
			success : function(result) {
				if (result.success) {
					quesbankTable('');
				} else {
					layer.msg(result.message, {
						icon : 2
					});
				}
			}
		})
	});  
}


