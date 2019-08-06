$(function(){
	$('textarea.codeedit').not('.lazy').each(function(index, element){
		$(element).codeedit();
	});
});
(function($) {
	jQuery.fn.codeedit = function(options){
		var mixedMode = {
			name: "htmlmixed"
		};
		var setting = {
			lineNumbers: true,
			matchBrackets: true,
			styleActiveLine: true,
			lineWrapping: true,
			indentUnit:4,
			indentWithTabs:true,
			extraKeys: { "Alt-/": "autocomplete"},
			mode: mixedMode
		};
		$.extend(setting, options);
		var $this = $(this);
		var cm = $this.codemirror(setting);
		cm.setSize('auto', $this.height()?$this.height():'auto');
		return cm;
	}
})(jQuery);