var isDraw = $("#isDraw").val();
if (isDraw == 1) {
	$.ajax({
		url : filepathstyle + "/front/draw/checkuser.do",
		data : {
			formId : formId
		},
		datatype : "json",
		success : function(result) {
			if (result.success) {
				var isDrawChecked = result.params.isDrawChecked;
				if(isDrawChecked == 1){
					showDraw();
				}
			}
		}
	});
}