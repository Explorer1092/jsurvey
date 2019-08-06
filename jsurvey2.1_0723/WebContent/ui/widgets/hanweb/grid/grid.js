var gridCache = {};
var gridOptions = {};
var Grid = {
		getInstance:function(gridId){
			if(typeof(gridId) == 'undefined'){
				gridId = 'grid';
			}
			return gridCache[gridId];
		}
};
/**
 * 获得选中的ID
 * @param gridId
 * @returns
 */
function getCheckedIds(gridId) {
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var checked = grid.datagrid('getChecked');
	var checkedIds = new Array();
	$.each(checked, function(i, n) {
		var checkBox = $(n[grid.idFieldName]);
		if(!checkBox.attr('disabled')){
			checkedIds.push($(checkBox).val());
		}
	});
	return checkedIds.join(',');
}

/**
 * 获得选中的指定字段值
 * @param fieldName
 * @param gridId
 * @returns
 */
function getCheckedAttr(fieldName, gridId) {
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var checked = grid.datagrid('getChecked');
	var checkedAttr = new Array();
	var value;
	$.each(checked, function(i, n) {
		value = $.trim(n[fieldName]);
		checkedAttr.push(value);
	});
	return checkedAttr.join(',');
}

/**
 * 获取选中的，指定多个字段的文字值
 * @param fieldNames {Array}
 * @param gridId
 * @returns {Array}
 */
function getCheckedAttrs(fieldNames, gridId) {
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var checked = grid.datagrid('getChecked');
	var checkedAttrs = new Array();
	var attrs;
	$.each(checked, function(i, n) {
		attrs = {};
		$.each(fieldNames, function(i, fieldName) {
			attrs[fieldName] = $(n[fieldName]).text().trim();
		});
		checkedAttrs.push(attrs);
	});
	return checkedAttrs;
}

/**
 * 获得选中的指定字段纯文字值
 * @param fieldName
 * @param gridId
 * @returns
 */
function getCheckedAttrText(fieldName, gridId) {
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var checked = grid.datagrid('getChecked');
	var checkedAttr = new Array();
	var value;
	$.each(checked, function(i, n) {
		value = $(n[fieldName]).text();;
		checkedAttr.push(value);
	});
	return checkedAttr.join(',');
}

/**
 * 修改单元格内容
 * @param index
 * @param cellValues
 * @param gridId
 */
function updateCell(index, cellValues, gridId) {
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	grid.datagrid('updateRow', {
		index : index,
		row : cellValues
	});
}

function checkRowWithId(field, value, gridId){
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var rows = grid.datagrid('getRows');
	if(rows){
		$.each(rows, function(index, row){
			var fieldValue = row[field];
			if(fieldValue){
				fieldValue = $(fieldValue).val();
			}
			if(value){
				value = $.trim(value);
			}
			if(fieldValue == value){
				grid.datagrid('checkRow', index);
			}
		});
	}
}

function checkRow(field, value, gridId){
	gridId = gridId ? gridId : 'grid';
	var grid = $('#'+gridId).grid();
	var rows = grid.datagrid('getRows');
	if(rows){
		$.each(rows, function(index, row){
			var fieldValue = row[field];
			if(fieldValue){
				fieldValue = $.trim(fieldValue);
			}
			if(value){
				value = $.trim(value);
			}
			if(fieldValue == value){
				grid.datagrid('checkRow', index);
			}
		});
	}
}

function gridOnClickRow (rowIndex, rowData) {
	$(this).datagrid('clearSelections');
	$(this).datagrid('selectRow', rowIndex);
}

function gridOnSortColumn (sort, order){
	var gridId = $(this).attr('id');
	var grid = gridCache[gridId];
	
	grid.sortName = sort;
	grid.sortOrder = order;
	window.location.href = grid.getUrl() + '?pageNumber=' + grid.pageNumber
		+ '&pageSize=' + grid.pageSize + grid.getCurrentParams() + grid.getSortParam()
		+ '&searchType=' + grid.searchType + '&grid_opr_date=' + grid.getGridDate();
}

(function($){
	jQuery.fn.grid = function(config) {
		var gridId = $(this).attr("id");
		var grid = gridCache[gridId];
		if(typeof(grid) != 'undefined'){
			return grid;
		}
		var grid = $('#'+gridId);
		grid.pageSize = config.pageSize;
		grid.idFieldName = config.idFieldName;
		grid.recordTotal = config.recordTotal;
		grid.pageList = config.pageList;
		grid.pageNumber = config.pageNumber;
		grid.searchType = config.searchType;
		grid.sortName = config.sortName;
		grid.sortOrder = config.sortOrder;
		
		var recordTotal = config.recordTotal;
		var pageSize = config.pageSize;
		var pageList = config.pageList;
		var pageNumber = config.pageNumber;
		var showPageList = config.showPageList;
		
		$('#'+gridId+'_toolbar .datagrid-toolbar-simple-search form .simple_search_custom_wrap').prepend($('.grid-simple-search form').html());
		var searchTextShow = $('.grid-simple-search').attr('searchText-show');
		var searchBtnShow = $('.grid-simple-search').attr('searchBtn-show');
		$('.grid-simple-search form').remove();
		grid.getGridDate = function() {
			return new Date().getTime();
		};

		grid.getUrl = function() {
			var url = window.location.href;
			if (url.indexOf('?') != -1) {
				url = url.substring(0, url.indexOf('?'));
			}
			return url;
		};

		grid.gridSimpleSearch = function() {
			window.location.href = grid.getUrl() + '?pageSize=' + pageSize
					+ grid.getSimpleSearchParam() + grid.getGridParam() + grid.getSortParam() + '&grid_opr_date='
					+ grid.getGridDate();
		};

		grid.getSimpleSearchParam = function() {
			var queryStr = '';
			var params = $('#'+gridId+'_toolbar .datagrid-toolbar-simple-search form').serializeArray();
//			if (searchText && searchText.length != 0) {
//				queryStr += ('&searchText' + '=' + encodeURI($(
//						'#' + gridId + '_simple_search').val()));
//			}
			if(params && params.length > 0){
				for(var param in params){
					queryStr += '&' + params[param].name + '=' + encodeURI(params[param].value);
				}
			}
			return queryStr;
		};

		grid.getCurrentParams = function() {
			var queryStr = '';
			if(grid.searchType == 1){
				queryStr = grid.getAdvSearchParam();
			}else{
				queryStr = grid.getSimpleSearchParam();
			}
			queryStr = queryStr + grid.getGridParam();
			return queryStr;
		};

		grid.getGridParam = function() {
			var queryStr = '';
			var queryParams = grid.datagrid('options').queryParams;
			$.each(queryParams, function(key, value) {
				queryStr += ('&' + key + '=' + encodeURIComponent(value));
			});
			return queryStr;
		};
		
		grid.getSortParam = function(){
			var queryStr = '';
			if(grid.sortName && grid.sortName.length > 0){
				queryStr = "&sortName=" + grid.sortName + "&sortOrder=" + grid.sortOrder;
			}
			return queryStr;
		};
		
		grid.getAdvSearchParam = function() {
			var params = $('.grid-advsearch form').serializeArray();
			var query = '';
			if(params && params.length > 0){
				for(var param in params){
					query += '&' + params[param].name + '=' + encodeURI(params[param].value);
				}
			}
			return query;
		};

		grid.gridAdvSearch = function() {
			window.location.href = grid.getUrl() + '?pageSize=' + pageSize
					+ grid.getAdvSearchParam() + grid.getGridParam() + grid.getSortParam() + '&searchType=1&grid_opr_date='
					+ grid.getGridDate();
		};
		
		grid.datagrid('getPager').pagination({
			total : recordTotal,
			pageSize : pageSize,
			pageList : pageList,
			showPageList:showPageList,
			pageNumber : pageNumber,
			beforePageText : '第',
			afterPageText : '页     共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
			onSelectPage : function(pgNumber, pgSize) {
				$.cookie('_page_size', pgSize);
				window.location.href = grid.getUrl() + '?pageNumber=' + pgNumber
						+ '&pageSize=' + pgSize + grid.getCurrentParams() + grid.getSortParam()
						+ '&searchType=' + grid.searchType + '&grid_opr_date=' + grid.getGridDate();
			},
			onBeforeRefresh : function(){
				window.location.reload();
				return false;
			}
		});
		
		var $simpleSearchBtn = $('#'+gridId+'_toolbar .datagrid-toolbar-search-wrap .btn-primary').click(function(){
			grid.gridSimpleSearch();
		});
		if(searchBtnShow == 0){
			$simpleSearchBtn.hide();
		}
		if(searchTextShow == 0){
			$('#'+gridId + '_simple_search').hide();
		}
		$('#'+gridId+'_toolbar .datagrid-toolbar-search-wrap .datagrid-toolbar-simple-search form').submit(function(){
			grid.gridSimpleSearch();
			return false;
		});
		
		$('#'+gridId+'_toolbar .datagrid-toolbar-search-wrap .btn-advsearch').click(function() {
			$('.grid-advsearch').slideDown('fast');
			$('.datagrid-toolbar-search-wrap').hide();
		});
		if(grid.searchType == 1){
			$('.grid-advsearch').slideDown(0);
			$('.datagrid-toolbar-search-wrap').hide();
		}
		$('.grid-advsearch .btn-primary').click(function() {
			grid.gridAdvSearch();
		});
		
		$('.grid-advsearch form').submit(function(){
			grid.gridAdvSearch();
			return false;
		}).keydown(function(event){
			if(event.keyCode == 13){
				$(this).submit();
			}
		});
		
		$('.grid-advsearch .advsearch-cancel').click(function() {
			grid.gridSimpleSearch();
//			$('.grid-advsearch').slideUp('fast');
//			$('.datagrid-toolbar-search-wrap').show();
		});
		
		$(window).resize(
			function(){
				grid.datagrid('resize');
			}
		);
		
		grid.getCheckedIds = function(){
			var checked = grid.datagrid('getChecked');
			var checkedIds = new Array();
			$.each(checked, function(i, n) {
				var checkBox = $(n[grid.idFieldName]);
				if(!checkBox.attr('disabled')){
					checkedIds.push($(checkBox).val());
				}
			});
			return checkedIds.join(',');
		};
		
		grid.getCheckedAttr = function(fieldName){
			var checked = grid.datagrid('getChecked');
			var checkedAttr = new Array();
			var value;
			$.each(checked, function(i, n) {
				value = $.trim(n[fieldName]);
				checkedAttr.push(value);
			});
			return checkedAttr.join(',');
		};
		
		grid.getCheckedAttrText = function(fieldName){
			var checked = grid.datagrid('getChecked');
			var checkedAttr = new Array();
			var value;
			$.each(checked, function(i, n) {
				value = $(n[fieldName]).text();;
				checkedAttr.push(value);
			});
			return checkedAttr.join(',');
		};
		
		grid.updateCell = function(index, cellValues){
			grid.datagrid('updateRow', {
				index : index,
				row : cellValues
			});
		};
		
		grid.getSelectedRow = function(){
			return grid.datagrid('getSelected');
		};
		
		grid.updateSelectedCell = function(obj){
			var row = grid.getSelectedRow();
			var index = grid.datagrid('getRowIndex', row);
			grid.updateCell(index, obj);
		};
		
		gridCache[gridId] = grid;
		$('.input-text').placeholder();
	};
})(jQuery);

$(function(){
	/**
	 * 下拉按钮
	 */
	$('.datagrid-toolbar-btn').on('mouseenter mousedown', function(e) {
		var menuTop = $(this).offset().top + $(this).height();
		var menuLeft = $(this).offset().left;
		$(this).children('.datagrid-toolbar-subbtns').css({top:menuTop,left:menuLeft}).show();
		e.stopPropagation();
	}).on('mouseleave', function(e) {
		$('.datagrid-toolbar-subbtns').hide();
	});

	$(document).mousedown(function() {
		$('.datagrid-toolbar-subbtns').hide();
	});

	$('.datagrid-toolbar-subbtn').hover(function() {
		$(this).addClass('hover');
	},function() {
		$(this).removeClass('hover');
	}).click(function() {
		$('.datagrid-toolbar-subbtns').hide();
	});
	
	$.each(gridOptions, function(gridId,config){
		$('#'+gridId).grid(config);
		if (config.dataSize == 0) {
			$('#'+gridId).parent('.datagrid-view').after('<div class="norecord"><i class="iconfont">&#xf503b;</i> 无记录</div>');
		}
	});
});