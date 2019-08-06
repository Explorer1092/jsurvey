window.nativeAlert = alert;
window.nativeConfirm = confirm;
window.alert = function(msg, options, ok) {
	var layerTmp = null;
	if(top.layer){
		layerTmp = top.layer;
	}else{
		layerTmp = layer;
	}
	layerTmp.alert(msg.toString(), {
		icon : 0,
		closeBtn : 0,
		anim : -1,
		isOutAnim : false
	}, function(index){
		if(ok){
			ok();
		}
		layerTmp.close(index);
	});
}
window.confirm = function(msg, ok, cancel) {
	var layerTmp = null;
	if(top.layer){
		layerTmp = top.layer;
	}else{
		layerTmp = layer;
	}
	layerTmp.confirm(msg.toString(), {
		icon : 3,
		closeBtn : 0,
		anim : -1,
		isOutAnim : false
	}, function(index) {
		if (ok) {
			ok();
		}
		layer.close(index);
	}, function(index) {
		if (cancel) {
			cancel();
		}
	});
}
window.alertErr = function(msg, options) {
	var opt = {
		icon : 2,
		closeBtn : 0,
		anim : -1,
		isOutAnim : false
	};
	opt = $.extend(opt, options);
	var layerTmp = null;
	if(top.layer){
		layerTmp = top.layer;
	}else{
		layerTmp = layer;
	}
	layerTmp.alert(msg.toString(), opt);
}
window.alertOk = function(msg, options) {
	var opt = {
		icon : 1,
		closeBtn : 0,
		anim : -1,
		isOutAnim : false
	};
	opt = $.extend(opt, options);
	var layerTmp = null;
	if(top.layer){
		layerTmp = top.layer;
	}else{
		layerTmp = layer;
	}
	layerTmp.alert(msg.toString(), opt);
}