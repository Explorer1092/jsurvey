$.fn.toggle = function(options) {
	var settings = {
		offValue: 0,
		onValue: 1,
		toggleInit: function() {},
		toggleBefore: function() {},
		toggleAfter: function() {},
		ajax: null
	};
	
	settings = $.extend(settings, options);

	settings.onValue += '';
	settings.offValue += '';

	$(this).each(function() {
		var toggleObj = $(this);
		var toggleVal = toggleObj.val();
		var toggleState = toggleVal == settings.onValue ? 'toggle-on' : 'toggle-off';
		var toggleObjId = toggleObj.attr('id');
		toggleObjId = toggleObjId ? toggleObjId : Math.round(Math.random() * 10000);
		var toggleId = 'toggle_' + toggleObjId;
		
		toggleObj.addClass('toggle');
		toggleObj.wrap('<div id="' + toggleId + '" class="toggle-wrap ' + toggleState
				 + ' ' + settings.size + '"></div>');
		toggleObj.before('<div class="toggle-handle ' + settings.size + '"></div>');
		
		if (toggleState == 'toggle-on') {
			var wrap = $('#' + toggleId);
			handle = wrap.children('.toggle-handle');
			
			handle.css('left', wrap.width() - handle.width() - 4);
		}
		settings.toggleObj = toggleObj;
		settings.toggleInit();
	});

	$('.toggle-wrap').click(function() {
		var wrap = $(this);
		if (wrap.is('.running')) {
			return;
		}
		
		var handle = wrap.children('.toggle-handle');

		settings.wrap = wrap;
		settings.handle = handle;
		settings.toggleObj = wrap.children('.toggle');
		
		wrap.addClass('running');

		settings.toggleBefore();
		if (settings.ajax) {
			ajaxToggle();
		} else {
			toggle();
		}
	});

	function toggle() {
		if (settings.toggleObj.val() == settings.onValue) {
			turnOff();
		} else {
			turnOn();
		}
	}

	function ajaxToggle() {
		var name = settings.toggleObj.attr('name');
		
		var data = settings.ajax.data;
		data = data == null ? {} : data;
		
		if (settings.wrap.is('.toggle-off')) {
			data[name] = settings.onValue;
		} else {
			data[name] = settings.offValue;
		}
		
		var ajaxData = settings.toggleObj.attr('data');
		if (ajaxData != null) {
			ajaxData = eval('(' + ajaxData + ')');
			data = $.extend(data, ajaxData);
		}
		
		$.ajax({
			url: settings.ajax.url,
			data: data,
			dataType: 'json',
			cache: false,
			success: function(result) {
				if (result.success) {
					toggle();
					if (settings.ajax.success) {
						settings.ajax.success(result);
					}
				} else {
					if (settings.ajax.error) {
						settings.ajax.error(result);
					}
				}
			},
			error: function(result) {
				if (settings.ajax.error) {
					settings.ajax.error(result);
				}
			}
		});
	}

	function turnOn() {
		settings.handle.animate({
			left : (settings.wrap.width() - settings.handle.width() - 4)
		}, 'fast', function() {
			settings.toggleObj.val(settings.onValue);
			settings.wrap.removeClass('toggle-off').addClass('toggle-on');
			settings.wrap.removeClass('running');
			settings.toggleAfter();
		});
	}

	function turnOff() {
		settings.handle.animate({
			left : 0
		}, 'fast', function() {
			settings.toggleObj.val(settings.offValue);
			settings.wrap.removeClass('toggle-on').addClass('toggle-off');
			settings.wrap.removeClass('running');
			settings.toggleAfter();
		});
	}
};