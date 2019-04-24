var contextPath = $('script:first').attr('src');
contextPath = contextPath.substr(0, contextPath.indexOf('/ui/lib/'));
/**
 * 通过隐藏的Iframe提交表单
 * 
 * @param url
 * @returns
 */
function iframeSubmit(url){
	var size = $('body #hiddenSubmit').size();
	if(size==0){
		$('body').append('<iframe frameborder="0" id="hiddenSubmit" name="hiddenSubmit" style="width: 0;height: 0" src=""></iframe>');
	}
	$('body #hiddenSubmit').attr('src',url);
}

/**
 * Ajax方式提交表单
 * 
 * @param url
 * @param setting
 * @returns
 */
function ajaxSubmit(url,setting){
	var opt = {
			type:'json',
			contentType : 'application/x-www-form-urlencoded',
			before:null,
			error:function(a,b,msg){alert('操作失败');},
			success:function(){},
			complete:function(){},
			data:null
	};
	opt = $.extend(opt, setting);
	$.ajax({
		type: "post",
		url: url,
		beforeSend : opt.before,
		dataType: opt.type,
		error : opt.error,
		contentType : opt.contentType,
		data:opt.data,
		success : opt.success,
		complete:opt.complete
	});
}

$(function(){
	// 不可用控件样式
	$(':disabled').addClass('disabled');
	
	// 日历
	if(typeof($dp)!='undefined'&&typeof(WdatePicker)!='undefined'&&jQuery.fn.jcalendar){
		$('.jcalendar').jcalendar();
	}
	
	// 校验
	if(typeof($.validity) != 'undefined'){
		$.validity.setup({ 
			outputMode:"showErr",
			elementSupport : ":text, :password, textarea, select, :radio, :checkbox, input[type='hidden'], input[type='tel'], input[type='email'], input[type='file']"
		});
	}
	
	// 复选框
	$(':checkbox').each(function(index,element){
		var data_values = $(element).attr('data-value');
		if(data_values){
			var list_data = eval(data_values);
			var length = $(list_data).size();
			// 如果是数组
			if(length >= 1 && $.isArray(list_data)){
				$.each(list_data,function(i, n){
					if(n == $(element).val()){
						$(element).attr("checked",true);
					}
				});
			}else{
				if(data_values.indexOf(',') > -1){
					var data_value = data_values.split(',');
					$.each(data_value,function(i, n){
						if(n == $(element).val()){
							$(element).attr("checked",true);
						}
					});
				}else{
					if(data_values == $(element).val()){
						$(element).attr("checked",true);
					}
				}
			}
		}
	});
	
	// 单选框
	$(':radio').each(function(index,element){
		var data_value = $(element).attr('data-value');
		if(data_value){
			if(data_value == $(element).val()){
				$(element).attr("checked",true);
			}
		}
	});
});
// 获得URL中的参数值
$.extend({
	getUrlParam: function(name, url) {
		url = url ? url : window.location.href;
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); 
		var r = url.substr(url.indexOf("\?")+1).match(reg); 
		if (r!=null) {
			return unescape(r[2]);
		}
		
		return null; 
	}
});