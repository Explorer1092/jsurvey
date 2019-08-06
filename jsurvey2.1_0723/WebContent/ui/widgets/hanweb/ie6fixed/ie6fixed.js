$.fn.ie6fixed = function() {
	if ($.browser.msie && $.browser.version < 8) {
		$('body').css({
			'background-attachment' : 'fixed'
		});
	} else {
		return;
	}

	var obj = $(this)[0];
	$(this).css("position", "absolute");

	var top = parseInt($(this).css('top'));
	var bottom = parseInt($(this).css('bottom'));
	var height = $(this).height();

	function fixdPosition() {
		if (bottom > 0) {
			obj.style.top = document.documentElement.scrollTop
					+ $(window).height() - bottom - height + 'px';
		} else {
			obj.style.top = (document.body.scrollTop || document.documentElement.scrollTop)
					+ top + 'px';
		}
	}

	window.onscroll = function() {
		fixdPosition();
	};

	window.onresize = function() {
		fixdPosition();
	};
}