$.fn.multiselect = function(op) {
	var selecter = $(this);

	var settings = {
		width : selecter.outerWidth() - 2,
		options : {},//选项，键为选项值，值为选项文字
		selected : [],//已经选择的值
		noremove : [],//禁止取消的值
		target : '',//选择结果保存的对象ID
		callback : function() {
		}
	};

	settings = $.extend(settings, op);
	
	$.each(settings.selected, function(i, selectedValue) {
		var selectedText = settings.options[selectedValue];
		var display = '';
		if(!selectedText || selectedText.length == 0){
			display = 'class="hide"';
		}
		if (contains(settings.noremove, selectedValue)) {
			selecter.append('<li ' + display + '><span class="multiselect-selected" val="' + selectedValue + '">' + selectedText + '</span></li>');
		} else {
			selecter.append('<li ' + display + '><i class="multiselect-remove iconfont">&#xf3013;</i><span class="multiselect-selected" val="' + selectedValue + '">' + selectedText + '</span></li>');
		}
	});

	// selecter
	// .append('<li class="multiselect-input"><input type="text"
	// readonly="readonly" autocomplete="off" /></li>');
	selecter.append('<div class="multiselect-clear"></div>');

	updateValue();

	var menuId = 'menu_' + parseInt(Math.random() * 1000);
	var menuContent = '<ul id="' + menuId + '" class="multiselect-options">';
	$.each(settings.options, function(value, text) {
		var display = '';
		if(!text || text.length == 0){
			display = 'hide';
		}
		menuContent += '<li class="multiselect-option ' + display + '" val="' + value + '">' + text + '</li>';
	});
	menuContent += '</ul>';

	selecter.menu({
		width : settings.width,
		height : settings.height,
		maxHeight : settings.maxHeight,
		content : menuContent,
		init : function() {
			$.each(settings.selected, function(i, selectedValue) {
				$('#' + menuId).find('li[val=' + selectedValue + ']').hide();
			});

			$('#' + menuId + ' li').click(function() {
				var text = $(this).text();
				var value = $(this).attr('val');
				var display = '';
				if(!text || text.length == 0){
					display = 'class="hide"';
				}
				selecter.children('.multiselect-clear').before('<li ' + display + '><i class="multiselect-remove iconfont">&#xf3013;</i><span class="multiselect-selected" val="' + value + '">' + text + '</span></li>');
				$(this).hide();

				updateMenuPosition();
				updateValue();
			});
		}
	});

	/*
	 * 事件绑定
	 * 
	 */

	selecter.add('.menu').on('selectstart', function() {
		return false;
	});

	// selecter.focus(function() {
	// selecter.find(':text').focus();
	// }).click(function() {
	// selecter.find(':text').focus();
	// });
	selecter.delegate('.multiselect-remove', 'click', function(event) {
		var menu = $(this).parent().parent();
		$('.menu:not(#' + menu.attr('id') + '_menu)').hide();
		var value = $(this).next().attr('val');
		$('#' + menuId).find('li[val=' + value + ']').show();
		$(this).parent().hide(50, function() {
			$(this).remove();
			updateMenuPosition();
			updateValue();
		});
		event.stopPropagation();
	}).delegate('.multiselect-remove', 'mousedown', function(event) {
		event.stopPropagation();
	});

	function updateMenuPosition() {
		var top = selecter.offset().top + selecter.outerHeight() + 1;
		var bottom = $('#' + selecter.attr('id') + '_menu').css('bottom');

		if (bottom == 'auto') {
			$('#' + selecter.attr('id') + '_menu').css('top', top + 'px');
		} else {
			$('#' + selecter.attr('id') + '_menu').css('bottom', bottom);
		}
	}

	function updateValue() {
		var returnArray = new Array();
		selecter.find('.multiselect-selected').each(function() {
			returnArray.push($(this).attr('val'));
		});
		$('#' + settings.target).val(returnArray.join(','));
		settings.callback();
	}

	function contains(arr, str) {
		if (arr == null || arr.length == 0) {
			return false;
		}
		var i = arr.length;
		while (i--) {
			if (arr[i] === str) {
				return true;
			}
		}
		return false;
	}

};