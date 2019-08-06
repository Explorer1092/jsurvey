<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>多文件上传</title>
<h:head multifileupload="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<h:import type="js" path="/ui/widgets/json/json2.js"></h:import>
<script type="text/javascript">
	$(function() {
		var options = getDialog().dialog('options').upload;

		var uploadedFiles = new Array();

		var index = 0;
		
		var maxSize = options.max_file_size;
		if(!maxSize){
			maxSize = '1mb';
		}
		
		var filtersString = JSON.stringify(options.filters);
		var filters = $.parseJSON(filtersString);
		filters = filters ? filters : [];

		var settings = {
			runtimes : 'flash,html4',
			url : '${uploadUrl}',
			max_file_size : maxSize,
			// 这里 ipv6下一定要加
			headers :{
				jssesionid:'${sessionid}'
			},

			filters: filters,
			// Resize images on clientside if we can
			//resize : {width : 320, height : 240, quality : 90},

			// Flash settings
			flash_swf_url : '${contextPath}/ui/widgets/plupload/js/Moxie.swf',

			init : {
				FilesAdded : function(up, files) {
					var currCount = files.length;//本次选择的
					
					var successCount = 0; //本次成功添加的
					for (var i = 0; i < files.length; i++) {
						if (files[i].status == plupload.QUEUED) {
							successCount++;
						}
					}
					
					var addedCount = up.files.length - successCount;//以前添加的
					
					var leftCount = 10 - addedCount;//剩下还可添加的
					if (leftCount < currCount) {
						alert('每次最多上传 10 个文件，您还能添加 ' + leftCount + '个！');
						while (successCount > 0) {
							up.files.pop();
							successCount--;
						}
						this.refresh();
						return false;
					}
				},
				FileUploaded : function(up, file, info) {
					var json = $.parseJSON(info.response);
					if (json && json.uuid) {
						var uploadedFile = {
								uuid:json.uuid,
								fileName:file.name,
								newName:json.newName
							};
						uploadedFiles.push(uploadedFile);
					} else {
						up.trigger('ERROR', {
							file : file,
							info : info
						});
						up.stop();
						alert('服务器错误，上传失败！');
						var uploader = this;
						closeUpload(uploader);
						return;
					}
				},
				UploadComplete : function(up, files) {
					var parentWindow = getParentWindow();
					parentWindow.uploadFiles = uploadedFiles;
					var uploader = this;
					setTimeout(function(){closeUpload(uploader)}, 300);
				},
				Error: function(up, err) {
		           
		        }
			}
		};
		var $this = $("#uploader");
		$this.pluploadQueue(settings);
		var height = $(window).height() - $('.plupload_filelist_header').outerHeight() - $('.plupload_filelist_footer').outerHeight();
		$('#' + $this.attr('id') + '_filelist', $this).height(height);
	});
	
	//用于修复关闭窗口造成的flash回调错误
	function closeUpload(uploader) {
		uploader.destroy();
		closeDialog();
	}
</script>
<style>
body {
	margin: 0;
	padding: 0;
	overflow: hidden;
}
</style>
</head>
<body>
	<div id="uploader"></div>
</body>
</html>