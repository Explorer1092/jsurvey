$.fn.menu = function(options) {
	var inputJq = $(this);
	var inputJqId = inputJq.attr('id');
	var menu;
	var menuId = inputJqId + '_menu';
	var exist = $('#' + menuId).size() > 0;

	var settings = {
		width : inputJq.outerWidth() - 2,
		height : null,
		maxHeight : null,
		tree : null,
		content : null,
		init : null,
		event : 'click',
		menuId : menuId,
		callback: null
	};

	settings = $.extend(settings, options);

	var heightStyle = settings.height ? 'height:' + settings.height + 'px' : '';

	var menu;
	if (exist) {
		menu = $('#' + settings.menuId);
		menu.css({
			width : settings.width,
			height : settings.height,
			maxHeight : settings.maxHeight
		});
	} else {
		$('body').append('<div id="' + settings.menuId + '" class="menu" ' + 'style="width:' + settings.width + 'px;' + heightStyle + '"><div class="menu-content"></div></div>');
		menu = $('#' + settings.menuId);
	}
	var menuContent = menu.children('.menu-content');

	if (settings.tree) {
		menuContent.append('<ul id="' + settings.tree + '" class="ztree"></ul>');
	} else if (settings.content) {
		menuContent.html(settings.content);
	}

	if (settings.init) {
		var treeJq = $('#' + settings.tree);
		settings.init(menu, inputJq, treeJq);
	}
	//
	// if (inputJq.is('[readonly]')) {
	// return;
	// }

	if (!exist) {
		$("body").bind("mousedown", onMouseDownBody);
		inputJq.css('cursor', 'pointer').focus(function() {
			$(this).blur();
		});

		inputJq.on(settings.event, toggleMenu);
		
		$(window).resize(hideMenu);
	}

	function toggleMenu() {
		if (menu.is(':hidden')) {
			showMenu();
		} else {
			hideMenu();
		}
	}

	/**
	 * 显示菜单
	 */
	function showMenu() {
		if(inputJq.hasClass('disabled')){
			return;
		}
		if (settings.height && menu.outerHeight() > settings.height) {
			menu.height(settings.height);
		} else 	if (settings.maxHeight && menu.outerHeight() > settings.maxHeight) {
			menu.height(settings.maxHeight);
		} else {
			menu.height('auto');
		}
		
		var inputJqOffset = inputJq.offset();
		var documentScrollTop = $(document).scrollTop();

		var left = inputJqOffset.left;
		menu.css('left', left + 'px');
		var top = inputJqOffset.top + inputJq.outerHeight() + 1;
		var bottom = $(window).height() - inputJqOffset.top + 1;
		
		var winHeight = $(window).height();
		var upHeight = documentScrollTop + inputJqOffset.top - 1; //输入框上边距
		var downHeight = documentScrollTop + winHeight - top; //输入框下边距
		var menuHeight = menu.outerHeight();
		
		if (menuHeight < downHeight) { //菜单高度小于下边距，向下展开
			menu.css('bottom', 'auto');
			menu.css('top', top + 'px');
		} else if (menuHeight < upHeight ) { //菜单高度小于上边距，向上展开
			menu.css('top', 'auto');
			menu.css('bottom', bottom + 'px');
		} else { //菜单高度大于上下边距
			if (downHeight > upHeight) { //下边距大于上边距，向下展开，最大高度下边距
				menu.outerHeight(downHeight - 5);
				menu.css('bottom', 'auto');
				menu.css('top', top + 'px');
			} else { //上边距大于下边距，向上展开，最大高度上边距
				menu.outerHeight(upHeight - 5);
				menu.css('top', 'auto');
				menu.css('bottom', bottom + 'px');
			}
		}

		if (menu.is(':hidden')) {
			menu.slideDown(50, function() {
				menu.children('.menu-content').css('overflow', 'auto');
			});
		}

		if (settings.callback && settings.callback.show) {
			settings.callback.show(menu, inputJq, treeJq);
		}
	}

	/**
	 * 隐藏菜单
	 */
	function hideMenu() {
		menu.children('.menu-content').css('overflow', '');
		menu.fadeOut(50);
		
		if (settings.callback && settings.callback.hide) {
			settings.callback.hide(menu, inputJq, treeJq);
		}
	}

	/**
	 * 其他区域点击隐藏菜单
	 */
	function onMouseDownBody(event) {
		if (event.target.id != inputJqId && $(event.target).closest('#' + inputJqId).size() == 0 && event.target.id != settings.menuId && $(event.target).closest('#' + settings.menuId).length == 0) {
			hideMenu();
		}
	}
};

$(function(){
	var menuSelectedClass = 'selected';
	$('.level-menu').each(function(index, element){
		var $menu = $(element);
		var $lis = $('>li', $menu);
		$lis.click(function(){
			var $li = $(this);
			if(!$li.hasClass(menuSelectedClass)){
				$lis.removeClass(menuSelectedClass);
				$li.addClass(menuSelectedClass);
			}
		});
	});
})