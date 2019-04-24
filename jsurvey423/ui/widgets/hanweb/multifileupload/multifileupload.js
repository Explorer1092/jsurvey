var uploadFiles;

jQuery.fn.multifileupload = function(options) {
	if (options == 'getFiles') {
		var files = {};
		$(this).next('.upload-filelist').children('li').each(function() {
			var uuid = $(this).attr('uuid');
			var name = $(this).children('.upload-filename').text();
			files[uuid] = name;
		});
		return files;
	}
	
	$(this).click(function() {
		uploadFiles = null;
		var fileListContainer = $(this).next('.upload-filelist');

		var dialogOptions = {title: '文件上传'};

		dialogOptions.onClose = function() {
			if (uploadFiles != null) {
				var uuids = new Array();
				var fileNames = new Array();
				var newNames = new Array();
				$.each(uploadFiles, function(index, uploadFile){
					var uuid = uploadFile.uuid;
					var fileName = uploadFile.fileName;
					var newName = uploadFile.newName;
					uuids.push(uuid);
					fileNames.push(fileName);
					newNames.push(newName);
					var li = '<li uuid="' + uuid + '"><i class="iconfont" title="移除">&#xf3008;</i><span class="upload-filename">' + fileName + '</span></li>';
					fileListContainer.append(li);
				});
				if(options.onClose){
					options.onClose(uuids.join(','), fileNames.join(','), newNames.join(','));
				}
			}
		};

		dialogOptions.upload = options;

		openDialog(options.dialogUrl, 500, 500, dialogOptions);
	});
};

$(function() {
	$('.upload-filelist').on('click', 'i', function() {
		$(this).parent().animate({
			'margin-left' : '+=100',
			opacity : '0.1'
		}, 'fast', function() {
			$(this).css('visibility', 'hidden').slideUp('fast', function() {
				$(this).remove();
				var uuid = $(this).attr('uuid');
				delete uploadFiles[uuid];
			});
		});
	});
});
