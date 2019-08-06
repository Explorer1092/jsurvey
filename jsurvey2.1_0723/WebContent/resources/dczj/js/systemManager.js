function sysManager(){
	windows.location.href = "${contextPath}/manager/sitegroup/sitegroup_show.do?webid=${webid}";
}
function changePage(url){
	$("#page").attr("src",url);
}
function nodisplay(){
	$("#webtree").css("display","none");
}
function display(){
	$("#webtree").css("display","inline");
}
function displayweb(){
	$("#webtree_datacall").css("display","inline");
}
function nodisplayweb(){
	$("#webtree_datacall").css("display","none");
}