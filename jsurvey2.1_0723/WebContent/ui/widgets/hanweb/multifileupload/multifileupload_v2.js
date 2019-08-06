(jQuery.fn.multiUpload = function(options) {
	var defOptions = {
		// 自动上传
		autoStart : false,
		// 是否可以连续上传
		continuous : false,
		// 文件服务器上传地址
		uploadUrl : null,
		// 上传组件地址
		swfUrl : null,
		// 过滤文件类型
		filters : null,
		// 每个文件大小
		fileSize : '1mb',
		// 文件个数
		limit : 10,
		// 上传方式
		runtimes : 'flash,html5,html4',
		// 添加文件后回调function(up, files)
		onAdded : null,
		// 删除文件后的回调function(up, files)
		onRemoved : null,
		// 上传后回调function(up, file, json, info)
		afterUpload : null,
		// 上传完成回调function(up, files)
		completeUpload : null
	};

	defOptions = $.extend(defOptions, options);

	var checkNull = function(obj) {
		if (!obj || $.trim(obj) == '') {
			return true;
		}
		return false;
	};

	var $this = $(this);

	if (checkNull(defOptions.uploadUrl) || checkNull(defOptions.swfUrl)) {
		$this.html('<div style="text-align: center;color:red;">多文件上传组件缺少必要参数</div>');
		return;
	}

	var browseBtnCtrl = function(up) {
		var unUploadFileCount = 0;
		$.each(up.files, function(index, file) {
			if (file.status == plupload.QUEUED) {
				unUploadFileCount = unUploadFileCount + 1;
			}
		});
		if (unUploadFileCount >= defOptions.limit) {
			up.disableBrowse(true);
		} else {
			up.disableBrowse(false);
		}
	};

	var limitCtrl = function(up) {
		var unUploadFileCount = 0;
		var upUploadFiles = new Array();
		$.each(up.files, function(index, file) {
			if (file.status == plupload.QUEUED) {
				unUploadFileCount = unUploadFileCount + 1;
				upUploadFiles.push(file);
			}
		});
		if (unUploadFileCount > defOptions.limit) {
			alert('最多上传 ' + defOptions.limit + ' 个文件，多余的会被自动清除');
			$.each(upUploadFiles, function(index, file) {
				var count = index + 1;
				if (count > defOptions.limit) {
					up.removeFile(file);
				}
			});
			up.refresh();
		}
	};

	var settings = {
		runtimes : defOptions.runtimes,
		url : defOptions.uploadUrl,
		max_file_size : defOptions.fileSize,

		filters : defOptions.filters,
		// Resize images on clientside if we can
		// resize : {width : 320, height : 240, quality : 90},

		multiple_queues : defOptions.continuous,
		// Flash settings
		flash_swf_url : defOptions.swfUrl,

		multipart_params : {},
		/**
		 * preinit : { Init : function(up, info) { } },
		 */

		init : {
			FilesAdded : function(up, files) {
				limitCtrl(up);
				browseBtnCtrl(up);
				if (defOptions.onAdded) {
					defOptions.onAdded(up, files);
				}
				if (defOptions.autoStart) {
					up.start();
				}
			},
			FilesRemoved : function(up, files) {
				browseBtnCtrl(up);
				if (defOptions.onRemoved) {
					defOptions.onRemoved(up, files);
				}
			},
			FileUploaded : function(up, file, info) {
				var json = $.parseJSON(info.response);
				if (!json.success) {
					up.trigger('Error', {
						code : plupload.GENERIC_ERROR,
						message : json.message,
						file : file
					});
				}
				if (defOptions.afterUpload) {
					defOptions.afterUpload(up, file, json, info);
				}
			},
			UploadComplete : function(up, files) {
				if (defOptions.completeUpload) {
					defOptions.completeUpload(up, files);
				}
			}
		}
	};
	var obj = $this.pluploadQueue(settings);
	var height = $this.height()
			- $('.plupload_filelist_header', $this).outerHeight()
			- $('.plupload_filelist_footer', $this).outerHeight();
	$('#' + $this.attr('id') + '_filelist', $this).height(height);
	return obj;
})(jQuery);
