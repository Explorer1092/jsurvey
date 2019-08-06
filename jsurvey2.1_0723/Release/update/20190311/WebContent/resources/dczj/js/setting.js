$(function(){
	$("#isstart").toggle({
		toggleAfter:function(){
			var startdate = $('#starttime').val();
			var endDate = $('#endtime').val();
			modifyStateByTime(startdate,endDate);
		}
	});
	$("#toggle_isstart").click(function(){
		if($("#isstart").val()==0){
			modify("isstart",1,0);
		}else{
			modify("isstart",0,0);
		}
	});
	$("#isend").toggle({
		toggleAfter:function(){
			var startdate = $('#starttime').val();
			var endDate = $('#endtime').val();
			modifyStateByTime(startdate,endDate);
		}
	});
	$("#toggle_isend").click(function(){
		if($("#isend").val()==0){
			modify("isend",1,0);
		}else{
			modify("isend",0,0);
		}
	});
	$("#islimituser").toggle();
	$("#toggle_islimituser").click(function(){
		if($("#islimituser").val()==0){
			$(".islimituser_hide").css("visibility","visible");
			modify("islimituser",1,0);
		}else{
			$(".islimituser_hide").css("visibility","hidden");
			modify("islimituser",0,0);
		}
	});
	//$("#iscode").toggle();
	$("#submitlimit").toggle();
	$("#isopen").toggle();
	$("#toggle_isopen").click(function(){
		if($("#isopen").val()==0){
			$(".isopen_hide").css("visibility","visible");
			modify("isopen",1,0);
		}else{
			$(".isopen_hide").css("visibility","hidden");
			modify("isopen",0,0);
		}
	});
	$("#isprize").toggle();
	
	$("#limitnumber0").change(function(){
		modify("limitnumber",$("#limitnumber0").val(),2);
	});
	
	$("#limitnumber1").change(function(){
		modify("limitnumber",$("#limitnumber1").val(),2);
	});
	
	$("#limitnumber2").change(function(){
		modify("limitnumber",$("#limitnumber2").val(),2);
	});
	$("#jumpurl").change(function(){
		modify("jumpurl",$("#jumpurl").val(),2);
	});
	
	function hasClass(elem, cls) {
		  cls = cls || '';
		  if (cls.replace(/\s/g, '').length == 0) return false; //当cls没有参数时，返回false
		  return new RegExp(' ' + cls + ' ').test(' ' + elem.className + ' ');
		}
		 
		function addClass(ele, cls) {
		  if (!hasClass(elem, cls)) {
		    ele.className = ele.className == '' ? cls : ele.className + ' ' + cls;
		  }
		}
		 
		function removeClass(ele, cls) {
		  if (hasClass(elem, cls)) {
		    var newClass = ' ' + elem.className.replace(/[\t\r\n]/g, '') + ' ';
		    while (newClass.indexOf(' ' + cls + ' ') >= 0) {
		      newClass = newClass.replace(' ' + cls + ' ', ' ');
		    }
		    elem.className = newClass.replace(/^\s+|\s+$/g, '');
		  }
		}
})

function modify(name,value,type){
	//先改变调查征集发布状态
	var dczjid = $("#dczjid").val();
	$.ajax({
		type : "post",
		url : "modify_ispublish.do",
		data : {"dczjid" : dczjid},
		success : function(result){
			if(result.success){
				$.ajax({
					type : "post",
					url : "modifysetting.do",
					data : {
						"name" : name,
						"value" : value,
						"dczjid" : dczjid,
						"type" : type
					}
				})
			}else{
				alert(result.message);
			}
		}
	})
};

function modifyStateByTime(starttime, endtime){
	var dczjid = $("#dczjid").val();
	var isstart = $("#isstart").val();
	var isend = $("#isend").val();
	$.ajax({
		type : "post",
		url : "modify_state.do",
		data : {"dczjid" : dczjid, "starttime" : starttime, "endtime" : endtime, "isstart" : isstart, "isend" : isend},
		success : function(result){
			if(result.success == false){
				alert(result.message);
			}
		}
	})
};

function openResultUser(){
	var islimituser = $("#islimituser").val();
	var contextPath = $("#contextPath").val();
	var dczjid = $("#dczjid").val();
	if(islimituser == 1){
		openDialog(contextPath+'/manager/setting/limitopen_user.do?dczjid='+dczjid,600,500,{title:'指定公开用户'})
	}else{
		alert("请先限制登录用户参与");
	}
}

