$(function() {
	$('.batchselect').on('mouseenter', 'li', function() {
		$(this).addClass('hover');
	});

	$('.batchselect').on('mouseleave', 'li', function() {
		$(this).removeClass('hover');
	});

	$('.batchselect').on(
			'click',
			'li',
			function(e) {
				var batchselect = $(this).parent();
				if (e.shiftKey) {
					var lastSelectedIndex = batchselect.children('li').index(
							$('.lastselect'));
					var currSelectedIndex = batchselect.children('li').index(
							$(this));
					batchselect.find('.selected:not(.lastselect)').removeClass(
							'selected');
					var begin = lastSelectedIndex;
					var end = currSelectedIndex;
					if (lastSelectedIndex > currSelectedIndex) {
						begin = currSelectedIndex;
						end = lastSelectedIndex;
					}
					batchselect.children('li').slice(begin, end).addClass(
							'selected');
					$(this).addClass('selected');
				} else if (!e.ctrlKey) {
					batchselect.children('.selected').removeClass('selected');
					$(this).addClass('selected');
				} else {
					$(this).toggleClass('selected');
				}
				$('.lastselect').removeClass('lastselect');
				$(this).removeClass('hover').addClass('lastselect');
			}).on('selectstart', 'li', function() {
		return false;
	});

	$(document).bind('keydown', 'ctrl+a', function(e) {
		$('.lastselect').parent().children('li').addClass('selected');
		return false;
	});
});

function openOrgselect() {
	openDialog(contextPath + '/manager/orgselect.do?orgType=u,g', 800, 500, {
		callback : addMembers
	});
}

function addMembers(users, groups) {
	$.each(users, function(id, user) {
		if ($('.user#' + id).size() != 0) {
			return;
		}
		var name = user.name;
		var ic = user.ic;
		var li = '<li class="user" id="' + id + '"><i class="icon-user"></i>'
				+ name;

		if (ic) {
			li += ' <span class="ic">&lt;' + ic + '&gt;</span>';
		}

		li += '</li>';

		$('.batchselect').append(li);
	});

	$.each(groups, function(id, group) {
		if ($('.group#' + id).size() != 0) {
			return;
		}
		var name = group.name;
		var ic = group.ic;
		var li = '<li class="group" id="' + id
				+ '"><i class="icon-sitemap"></i>' + name;

		if (ic) {
			li += ' <span class="ic">&lt;' + ic + '&gt;</span>';
		}

		li += '</li>';

		if ($('.group').size() > 0) {
			$('.batchselect .group:last').after(li);
		} else {
			$('.batchselect').prepend(li);
		}

	});
}

function removeMembers() {
	$('.selected').animate({
		'margin-left' : '-=100',
		opacity : '0.1'
	}, 'fast', function() {
		$(this).css('visibility', 'hidden').slideUp('fast', function() {
			$(this).remove();
		});
	});
}

/**
 * 保存
 */
function save() {
	var groups = new Array();
	var users = new Array();

	$('.group').each(function() {
		groups.push($(this).attr('id'));
	});

	$('.user').each(function() {
		users.push($(this).attr('id'));
	});
	ajaxSubmit("modify_submit.do?iid=" + $("#iid").val() + "&users=" + users
			+ "&groups=" + groups, {
		success:function(result){
			if(result.success){
				closeDialog(true);
			}else{
				alert(result.message);
			}
		}
	});
}