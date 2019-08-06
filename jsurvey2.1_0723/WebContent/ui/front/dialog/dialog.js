/**
 * 打开窗口，如果top层有弹窗组件则从top弹窗，否则从当前页面弹窗
 * @param url
 * @param width
 * @param height
 * @param options
 * @returns
 */
function openDialog(url, width, height, options) {
	var opt = {
		type : 2,
		anim : -1,
		isOutAnim : false,
		title : '',
		shadeClose : true,
		area : [ width + 'px', height + 'px' ],
		content : url,
		success : function(layero, index) {
			layero.find('iframe')[0].contentWindow.parentWindow = window;
		},
		parentWindow : window
	}
	opt = $.extend(opt, options);
	if (top.layer) {
		top.layer.open(opt);
	} else {
		layer.open(opt);
	}
}

/**
 * 获得父页面Window对象
 * 
 * @returns
 */
function getParentWindow() {
	return window.parentWindow;
}

/**
 * 刷新父页面
 * 
 * @returns
 */
function refreshParentWindow() {
	var win = getParentWindow();
	if(win){
		win.location.reload();
	}
}

/**
 * 关闭窗口
 * 
 * @param refreshParent
 * @returns
 */
function closeDialog(refreshParent) {
	if(refreshParent){
		refreshParentWindow();
	}
	var pre = null;
	if (top.layer) {
		pre = top.layer;
	}else{
		pre = parent.layer;
	}
	var index = pre.getFrameIndex(window.name);
	pre.close(index);
}
/**
 * 关闭所有dialog
 * @returns
 */
function closeAllDialog(){
	if (top.layer) {
		top.layer.closeAll();
	}else{
		layer.closeAll();
	}
}