(function($){
	jQuery.fn.grid = function(config) {
		var getAttrVal = function(attr, defaultVal){
			var value = $this.attr(attr);
			if(value && value != ''){
				value = $.trim(value);
			}else if(defaultVal){
				value = defaultVal;
			}else{
				value = null;
			}
			return value;
		}
		var $this = $(this);
		var id = $this.attr('id');
		// form="${form!}" url="${url!}" callback="${callback!}" before="${before!}" complete="${complete!}" error="${error!}"
		// mask="${mask!}" p='${p!}' pg="${pg!}" plist="${plist!}"
		var form = $('#' + $this.attr('form'));
		var data = null;
		if(form.size() == 1){
			data = form.serialize();
			form.submit(function(){
				$paging.current = 1;
				data = form.serialize();
				loadData();
				return false;
			});
		}
		var mask = getAttrVal('mask', 'true')=='true'?true:false;
		var callback = getAttrVal('callback');
		var url = getAttrVal('url');
		var before = getAttrVal('before');
		var complete = getAttrVal('complete');
		var error = getAttrVal('error');
		var p = getAttrVal('p', 1);
		var pg = getAttrVal('pg');
		var plist = getAttrVal('plist', '10,15,20');
		var toolbar = getAttrVal('toolbar', 'true')=='true'?true:false;
		plist = plist.split(',');
		if(pg == null){
			pg = plist[0];
		}
		// 初始化分页
		var $paging = null;
		var initPaging = function(p ,pg, plist, count){
			if($paging == null){
				$paging = $('.grid-pagination', $this).Paging({
					current:p, 
					pagesize: pg, 
					pageSizeList:plist, 
					count: count,  
					toolbar : toolbar,
					callback:function(pagecount, size, currentpage){
						loadData(currentpage, size);
					},
					changePagesize:function(ps){
						loadData();
					}
				})[0];
			}else{
				$paging.render({
					count: count,
					pagesize : pg,
					current:p
				});
			}
		}
		// 获取数据
		var $list = $('.grid-data-list', $this);
		var loadData = function(){
			var p = $paging.current;
			var pg = $paging.pagesize;
			ajaxSubmit(url, {
				data:'pageNumber=' + p + '&pageSize='+pg + (data?'&' + data:''),
				before:function(){
					if(mask){
						$list.addClass('min-height');
						$this.mask('数据加载中...');
					}
					before?window[before]($list, $paging, form):null;
				},
				success:function(result){
					if(callback){
						var res = callback?window[callback](result, $list, $paging, form):'';
						$list.html(res);
					}else {
						if(result && result.data){
							$list.html(result.data.toString());
						}
					}
					initPaging(p, pg, null, result.total);
				},
				error:function(){
					if(error){
						var res = window[error]($list, $paging, form);
						if(res == true){
							$list.append('<div class="data-grid-error">查询数据失败</div>');
						}else{
							$list.append(res);
						}
					}else{
						$list.append('<div class="data-grid-error">查询数据失败</div>');
					}
				},
				complete:function(){
					complete?window[complete]($list, $paging, form):null;
					if($.trim($list.html()) == ''){
						$list.append('<div class="data-grid-noresult">未查询到任何数据</div>');
					}
					if(mask){
						$list.removeClass('min-height');
						$this.unmask();
					}
				}
			});
		}
		initPaging(p, pg, plist, 0);
		loadData();
	};
})(jQuery);

$(function(){
	$('.data-grid-wrap').grid();
})