$.fn.select = function(arg1, arg2, opt) {
	// API
	if (arg1) {
		var select = $(this);
		var selectId = select.attr('id');
		var selectWidgetId = $('#' + selectId).parent().attr('id');
		var selectWidget = $('#' + selectWidgetId);
		var menu = $('#' + selectWidgetId + '_menu');
		var options = menu.find('.select-options');

		switch (arg1) {
		case 'setValue': // 设置选中值
			$('#' + selectId).val(arg2);// 设值
			menu.find('.selected').removeClass('selected');
			var option = menu.find('.select-option[val="' + arg2 + '"]');
			option.addClass('selected');// 选中
			selectWidget.find('.select-text').text(option.text());// 设字
			break;
		case 'disabled': // 设置选中值
			if(arg2){
				selectWidget.addClass('disabled')
			}else{
				selectWidget.removeClass('disabled')
			}
			break;
		case 'addOptions': // 添加选项（一个或多个）
			var isArray = arg2 instanceof Array;
			if (!isArray) {
				arg2 = [arg2];
			}
			
			$.each(arg2, function(index, optionMap) {
				var optionText = optionMap.optionText;
				var optionValue = optionMap.optionValue;
				if (options.children('.select-option[val=' + optionValue + ']').size() != 0) {
					return;
				}
				options.append('<li class="select-option" val="' + optionValue + '">' + optionText + '</li>');
			});

			menu.off('click', '.select-option');
			menu.on('click', '.select-option', function() {optionClickEvent(selectWidgetId, menu, selectWidget, $(this));});
			break;
		case 'removeOptions': // 移除选项（一个或多个）
			if (arg2 == null) {
				options.children('.select-option').remove();
				$('#' + selectId).val('');
				selectWidget.find('.select-text').text('');
				return;
			}
			
			// 删除一个选项
			var isArray = arg2 instanceof Array;
			if (!isArray) {
				arg2 = [arg2];
			}
			
			$.each(arg2, function(index, optionValue) {
				options.children('.select-option[val=' + optionValue + ']').remove();
			});
			
			if (options.children('.selected').size() == 0) {
				var initVal = select.attr('data-value');
				var initOption = options.find('.select-option[val=' + initVal + ']');
				if (initVal == '' || options.children('.select-option[val=' + initVal + ']').size() == 0) {
					initOption = options.children('.select-option:first');
					initVal = initOption.attr('val');
				}
				var initText = initOption.find('i').size() > 0 ? initOption.find(':not(i)').text() : initOption.text();
				selectWidget.find('.select-text').text(initText);// 设默认字
				$('#' + selectId).val(initVal);// 设默认值
			}
			break;
		}
		return;
	}

	$(this).each(function() {
		
		var config = {
				
		};
		config = $.extend(config, opt);
		var content = '';

		var select = $(this);
		if(select.get(0).tagName.toLowerCase() == 'input'){
			return true;
		}
		var selectId = select.attr('id');
		var fontSize = select.css('font-size');
		var selectData = select.data();
		var data = '';
		if(selectData){
			var dataArray = new Array();
			$.each(selectData, function(key, value){
				dataArray.push('data-' + key + '="' + value + '"');
			});
			data = dataArray.join(' ');
		}
		selectId = selectId ? selectId : Math.round(Math.random() * 10000);
		var selectWidgetId = 'select_' + selectId;
		var selectName = select.attr('name');
		selectName = selectName ? selectName : selectId;
		var selectStyle = select.attr('style');
		var selectedText = '';
		var selectedValue = '';
		var readOnly = select.is('[readonly]');
		var disabled = select.is('[disabled]');
		var disabledStyle = disabled || readOnly ? ' disabled' : '';
		var onChange = select.attr('onchange');
		var dataChange = select.data('change');
		var selectedInitVal = select.attr('data-value');
		selectedInitVal = selectedInitVal ? selectedInitVal : select.val();
		var height = select.attr('height');

		select.children('option, li').each(function() {
			var selected = '';
			if (selectedInitVal == $(this).val()) {
				selected = ' selected';
				selectedText = $(this).find('i').size() > 0 ? $(this).find(':not(i)').text() : $(this).text();
				selectedValue = $(this).val();
			}
			content += '<li style="font-size:' + fontSize + '" class="select-option' + selected + '" val="' + $(this).val() + '">' + $(this).html() + '</li>';
		});
		content = '<ul class="select-options" data-value="' + selectedInitVal + '">' + content + '</ul>';

		select.replaceWith('<div id="' + selectWidgetId + '" class="select input-text' + disabledStyle + '" style="' + selectStyle + '"><span class="select-text">' + selectedText + '</span><i class="iconfont">&#xf0024;</i><input ' + data + ' id="' + selectId + '" name="' + selectName + '" type="hidden" class="select-val" value="' + selectedValue + '"/></div>');

		if (!readOnly && !disabled) {
			var $select = $('#' + selectWidgetId);
			$select.menu({
				height : height,
				content : content,
				init : function(menu, selectObj) {
					menu.data('onChange', onChange);
					menu.data('dataChange', dataChange);

					menu.on('hover', function() {
						menu.find('.selected').removeClass('selected');
					});
					
					menu.on('click', '.select-option', function() {optionClickEvent(selectWidgetId, menu, selectObj, $(this))});
				},
				callback: {
					show: function(menu, selectObj) {
						var selectedVal = selectObj.children('.select-val').val();
						if(selectedVal && selectedVal != ''){
							menu.find('.select-option[val='+selectedVal+']').addClass('selected');
						}
					},
					hide: function(menu, selectObj) {
						menu.find('.selected').removeClass('.selected');
					}
				}
			});
		}
	});

	function optionClickEvent(selectWidgetId, menu, selectObj, currentClickOption) {
		var text = currentClickOption.find('i').size() > 0 ? currentClickOption.find(':not(i)').text() : currentClickOption.text();
		//menu.find('.selected').removeClass('selected');
		currentClickOption.addClass('selected');
		selectObj.children('.select-text').text(text);
		selectObj.children('.select-val').val(currentClickOption.attr('val'));
		menu.children('.menu-content').css('overflow', '');
		menu.fadeOut(50);
		var onChange = menu.data('onChange');
		var dataChange = menu.data('dataChange');
		if (onChange) {
			onChange = onChange.replace(/this/g, "$('#" + selectWidgetId + " > .select-val')[0]");
			try {
				eval(onChange);
			} catch (e) {
				alert(e.message);
			}
		}else if(dataChange){
			window[dataChange](currentClickOption.attr('val'), text);
		}else if(opt && opt.change){
			opt.change(currentClickOption.attr('val'), text);
		}
	}
};

$(function() {
	// .lazy和表格翻页，不自动使用select控件
	$('select:not(.lazy,.pagination-page-list,.chosen), .select').select();
});