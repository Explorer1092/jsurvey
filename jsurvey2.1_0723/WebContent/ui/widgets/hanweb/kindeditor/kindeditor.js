$.fn.simpleEditor = function(options){
	var defOptions = {
			resizeType:0,
			allowPreviewEmoticons : false,
			allowImageUpload : false,
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons', 'image', 'link']
	}
	defOptions = $.extend(defOptions, options);
	var $this = $(this);
	return KindEditor.create($this, defOptions);
	
};
$(function(){
	$('.simple_editor').each(function(index, element){
		$(element).simpleEditor();
	});
});