$(function() {
	var toolbarHeight = $('#dialog-toolbar').outerHeight();
	toolbarHeight = toolbarHeight ? toolbarHeight : 0;
	$('#dialog-content').outerHeight(
			$(window).height() - toolbarHeight);
});