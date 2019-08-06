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
		title : '请选择',
		extensions : '*.*',
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
		completeUpload : null,
		// file, errorCode, errorMsg, errorString
		error:null,
		width : 90,
		height : 30,
		buttonImage : null
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
	
	var $fileValue = $('#' + $this.attr('id') + '_val');
	
	var name = $fileValue.attr('name');
	
	var errTipId = $this.attr('err-tip');
	
	var startBtnId = $this.attr('startBtn');
	
	if(startBtnId){
		defOptions.autoStart = false;
		$('#' + startBtnId).click(function(){
			$this.uploadify('upload','*');
		});
	}
	
	var fileValues = new Array();
	
	function setFileValue (){
		var values = fileValues.join(',');
		$fileValue.val(values);
	}
	
	var $upload = $this.uploadify({
		height : defOptions.height,
		swf : defOptions.swfUrl,
		uploader : defOptions.uploadUrl,
		multi : defOptions.limit==1?false:true,
		width : defOptions.width,
		auto : defOptions.autoStart,
		removeCompleted : false,
		fileObjName : name,
		overrideEvents : ['onSelect','onDialogClose'],
		fileTypeDesc : defOptions.title,
		fileTypeExts : defOptions.extensions,
		successTimeout: 60 * 20,
		buttonImage : defOptions.buttonImage,
		fileSizeLimit : defOptions.fileSize,
		uploadLimit : defOptions.limit,
		onDialogClose : function(queueData){
			if (queueData.filesErrored > 0) {
				if(errTipId){
					$('#' + errTipId).text(queueData.errorMsg).show();
				}else{
					alert(queueData.errorMsg);
				}
			}else{
				if(errTipId){
					$('#' + errTipId).text('').hide();
				}
			}
		},
		onUploadSuccess:function(file, data, response) {
			var queryFile = this.queueData.files[file.id];
			queryFile = $.extend(queryFile, file);
			this.queueData.files[file.id] = queryFile;
			var json = $.parseJSON(data);
			if (defOptions.afterUpload) {
				fileValues.push(json.params.fileName);
				setFileValue();
				defOptions.afterUpload($upload, queryFile, json);
			}
		},
		onUploadError : function(file, errorCode, errorMsg, errorString){
			var queryFile = this.queueData.files[file.id];
			queryFile = $.extend(queryFile, file);
			this.queueData.files[file.id] = queryFile;
			if(defOptions.error){
				defOptions.error($upload, queryFile, errorCode, errorMsg, errorString);
			}
		},
		onSelect:function(file){
			// Get the size of the file
			var fileSize = Math.round(file.size / 1024);
			var suffix   = 'KB';
			if (fileSize > 1000) {
				fileSize = Math.round(fileSize / 1000);
				suffix   = 'MB';
			}
			var fileSizeParts = fileSize.toString().split('.');
			fileSize = fileSizeParts[0];
			if (fileSizeParts.length > 1) {
				fileSize += '.' + fileSizeParts[1].substr(0,2);
			}
			fileSize += suffix;
			
			var fileName = file.name;
			if (fileName.length > 25) {
				fileName = fileName.substr(0,25) + '...';
			}
			
			var up = this;
			var $itemHtml = $('<div id="' + file.id + '" class="uploadify-queue-item">\
				<div class="cancel">\
					<a></a>\
				</div>\
				<span class="fileName">' + fileName + ' (' + fileSize + ')</span><span class="data"></span>\
				<div class="uploadify-progress">\
					<div class="uploadify-progress-bar"><!--Progress Bar--></div>\
				</div>\
			</div>');
			// $(\'#${' + this.settings.id + '}\').uploadify(\'cancel\', \'${fileID}\')
			$itemHtml.find('.cancel>a').click(function(){
				var fileId = $(this).parents('.uploadify-queue-item').attr('id');
				var file = up.queueData.files[fileId];
				$('#' + up.settings.id).uploadify('cancel', fileId);
				delete up.queueData.files[fileId];
				var index = $.inArray(file.sid, fileValues);
				up.queueData.queueSize -= file.size;
				fileValues.splice(index,1);
				setFileValue();
				if(file.uploaded){
					up.settings.onCancel.call(up, file);
				}
			});
			$('#' + up.settings.queueID).append($itemHtml);
			if(defOptions.onAdded){
				defOptions.onAdded($upload, file);
			}
		},
		onCancel:function(file){
			var queryFile = this.queueData.files[file.id];
			queryFile = $.extend(queryFile, file);
			this.queueData.files[file.id] = queryFile;
			if(defOptions.onRemoved){
				defOptions.onRemoved($upload, queryFile);
			}
		},
		onUploadComplete: function(file){
			var queryFile = this.queueData.files[file.id];
			queryFile = $.extend(queryFile, file);
			this.queueData.files[file.id] = queryFile;
			if(defOptions.completeUpload){
				defOptions.completeUpload($upload, queryFile);
			}
		}
	});
	return $upload;
})(jQuery);
