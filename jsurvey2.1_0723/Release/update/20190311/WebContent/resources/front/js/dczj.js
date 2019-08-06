var contextpath = $("#contextpath").val();

$(function() {
	var isprize = $("#isprize").val();
	if(isprize == 1){
		var u_agent = navigator.userAgent; 
		if(u_agent.indexOf('Trident')>-1&&u_agent.indexOf('rv:11')>-1){ 
			alert("当前浏览器不支持在线抽奖,如需抽奖,请先更换浏览器(支持谷歌，360浏览器极速模式),再做问卷");
		}else if(u_agent.indexOf('MSIE')>-1&&u_agent.indexOf('Trident')>-1){ 
			alert("当前浏览器不支持在线抽奖,如需抽奖,请先更换浏览器(支持谷歌，360浏览器极速模式),再做问卷");
		}else if(u_agent.indexOf('MSIE')>-1){ 
			alert("当前浏览器不支持在线抽奖,如需抽奖,请先更换浏览器(支持谷歌，360浏览器极速模式),再做问卷");
		}
	}
	jsurveyvisitcount();
	
	      var x = document.cookie;
	      var uv = "";	   
	      var date = new Date();//获取当前时间
	      var year = date.getFullYear();
	      var month = date.getMonth() + 1;
	      var strDate = date.getDate();
	      var currentDate = ""+year+month+strDate;
	      var cookieid = $('#dczjid').val();
	      cookieid = "cookie"+cookieid	    
	      if(window.localStorage){
	        uv = eval(localStorage.getItem(cookieid));
	        /***/
	        var cookieName = uv + '=';
	        var cookieStart = document.cookie.indexOf(cookieName);
	        if(cookieStart<=-1){
	        	window.localStorage.clear();
	        	uv = eval(localStorage.getItem(cookieid));
	        }
	        /***/
	        
	        if (uv == null) {
	          var rand1 = parseInt(Math.random() * 4000000000);
	          var rand2 = parseInt(Math.random() * 4000000000);
	          uv = eval(String(rand1) + String(rand2));  	       
	          localStorage.setItem(cookieid,uv); 
	        }else{
	         /* var todaycookie = localStorage.getItem(cookieid);
	          if(todaycookie !=null){
	          }*/
	        }	
	        $('#'+cookieid).val(uv);
	        localStorage.setItem(cookieid,uv); 
	        var d = new Date();
	        d.setTime(d.getTime() + (365 * 24 * 60 * 60 * 1000));
	        var expires = "expires=" + d.toUTCString();
	        document.cookie = ""+uv+"="+uv+"; path=/ ;" + expires;
	      }
		
	
})

function jsurveyvisitcount(){
	var jsurveyid = document.getElementById('dczjid').value;
	$.ajax({		
		url: contextpath+"/front/jsurvey/visitCount.do",
		data:{jsurveyid:jsurveyid},
		datatype:"json",
	});

}

function changecode(dczjid){
	document.getElementById('valicode'+dczjid).src=contextpath+"/front/dczj/que_code.do?sessionid=surveyform"+dczjid+"&random="+Math.random();
}

function surveysubmitvalidate(){
	var formId = document.getElementById('dczjid').value;
	document.getElementById("dczj"+formId).submit();		 
}

function showerrormessage(message){
	$("#errormessage").html(message);
}


//显示查看结果提示页面弹窗
var t ;
var intervalid; 
function alertResultWind(jumpurl){
	var isShowResult = $("#isShowResult").val();
	if(isShowResult == 0){
		$("#pop_result").css("display","none");
	}
	var isDraw = $("#isDraw").val();
	if(isDraw == 0){
		$("#pop_draw").css("display","none");
	}
	$("#bgcs").css({display:"block",height:$(document).height()});
	$("#popboxcs").css("display","block");
	if(jumpurl!=""){
		$("#pop_time").css("display","block");
		$("#pop_result").css("display","none");
		 t = 10; 
		intervalid = setInterval("fun('"+jumpurl+"')", 1000); 
			setTimeout(function () { 
				 clearInterval(intervalid);	
			}, 21000);
	}
}

//隐藏查看结果提示页面弹窗
function closeResultWind(){
	$("#bgcs").css({display:"none",height:$(document).height()});
	$("#popboxcs").css("display","none");
	location.reload();
}

function fun(jumpurl) {
	endsecond.innerHTML=t--;
	if (t == 0) { 
		if(jumpurl!=undefined){
			location.href = jumpurl; 
		}
		clearInterval(intervalid); 
	} 
}

//前往抽奖
function showDraw(){
	$("#bgcs").css({display:"none",height:$(document).height()});
	$("#popboxcs").css("display","none");
	$("#drawbgcs").css({display:"block",height:$(document).height()});
	$("#drawboxcs").show();
    var objH= '650';//浮动对象的高度 
    var ch=$(window).height();//屏幕的高度   
    var objT=(Number(ch)-Number(objH))/2;   //思路  浮动高度+（（屏幕高度-对象高度））/2
    $("#drawboxcs").css("top",objT);
}

//跳转到结果页面
function showResult(resulturl,dczjid){
	$.ajax({		
		url: contextpath+"/check/open/user.do",
		data:{dczjid:dczjid},
		datatype:"json",
		success: function(result){
			if(result.success){
				$.ajax({		
					url: contextpath+"/check/open/checkResult.do",
					data:{dczjid:dczjid},
					datatype:"json",
					success: function(result){
						if(result=='1'){
							window.location=resulturl; 
						}else{
							$("#bgcs").css({display:"none",height:$(document).height()});
							$("#popboxcs").css("display","none");
							alert("结果页暂未生成，请1分钟后查看！");
						}
					}
				});
			}else{
				alert(result.message);
			}
		},
        error: function (result) {
            alert(result.message);
        }
	});
}

function closeDrawWind(){
	$("#drawbgcs").css({display:"none",height:$(document).height()});
	$("#drawboxcs").css("display","none");
}

function otherChange0(quesId,iid){
	$('#clearoption' + quesId).css("display", "inline");
	var inputArr=document.getElementsByName("surveyAnsw"+quesId);
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].checked){
			$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",false);
		}else{
			$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",true);
		    $("#"+ inputArr[i].id+"_fillInAir").val("");
		}
	}
}

//改变多选的其他选项输入框状态
function otherChange(quesId,iid){	
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	var inputArr=document.getElementsByName("surveyAnsw"+quesId);
	for(var i=0;i<inputArr.length;i++){
		if($("#"+ inputArr[i].id+"_fillInAir").size()>0){
			if(inputArr[i].checked){
				$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",false);
			}else{
				$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",true);
			    $("#"+ inputArr[i].id+"_fillInAir").val("");
			}
		}
	}
}

//单选框样式切换
function chooseimg(obj,frame_style,quesId,iid,answid,filepath){
	var value = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
		$("#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/images/chooseframe/radio"+frame_style+".png");
	}
	$("#"+answid).prop( "checked", true );
	$("#img"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
	var inputArr=document.getElementsByName("surveyAnsw"+quesId);
	for(var i=0;i<inputArr.length;i++){
		if(inputArr[i].checked){
			$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",false);
		}else{
			$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",true);
		    $("#"+ inputArr[i].id+"_fillInAir").val("");
		}
	}
}
//单选框样式切换 点击答案时
function chooseimgword(frame_style,quesId,iid,answid,filepath){
	var value = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
		$("#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/images/chooseframe/radio"+frame_style+".png");
	}
	$("#"+answid).prop( "checked", true );
	$("#img"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
}
//复选框样式切换
function choosecheckimg(obj,frame_style,quesId,iid,answid,filepath){
	var checkstate = $("#"+answid).prop("checked");
	if(checkstate==false){
		$("#"+answid).prop( "checked", true );
		$(obj).attr("src",filepath+"/resources/dczj/images/chooseframe/checkcheck"+frame_style+".png");
	}else{
		$("#"+answid).prop( "checked", false );
		$(obj).attr("src",filepath+"/resources/dczj/images/chooseframe/check"+frame_style+".png");
	}
	var inputArr=document.getElementsByName("surveyAnsw"+quesId);
	for(var i=0;i<inputArr.length;i++){
		if($("#"+ inputArr[i].id+"_fillInAir").size()>0){
			if(inputArr[i].checked){
				$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",false);
			}else{
				$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",true);
			    $("#"+ inputArr[i].id+"_fillInAir").val("");
			}
		}
	}
}
//复选框样式切换 点击答案时
function choosecheckimgword(frame_style,quesId,iid,answid,filepath){
	var checkstate = $("#"+answid).prop("checked");
	if(checkstate==false){
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/checkcheck"+frame_style+".png");
	}else{
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/check"+frame_style+".png");
	}
}

//清除单选
function clearoption(framestyle,quesId){
	var radioName = "surveyAnsw"+quesId;
	$('#clearoption' + quesId).css("display", "none");
	if(framestyle > 0){
		var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
		if(value!==undefined){
			$("#img"+value).attr("src",contextpath+"/resources/dczj/images/chooseframe/radio"+framestyle+".png");
		}
	}
	$('input:radio[name="'+radioName+'"]').removeAttr('checked');
	var inputArr=document.getElementsByName("surveyAnsw"+quesId);
	for(var i=0;i<inputArr.length;i++){
		if($("#"+ inputArr[i].id+"_fillInAir").size()>0){
			$("#"+ inputArr[i].id+"_fillInAir").attr("disabled",true);
		    $("#"+ inputArr[i].id+"_fillInAir").val("");
		}
	}
}

//发送手机验证码
function sendCode(formid,phoneMessage,randomCode){
	var mobile=$('#iphonenumber').val();
	if(mobile == ''){
		alert("手机号不能为空");
		return;
	}
	var bl = /(^(13|15)[0-9]{9}$)|(^(17|18)[0-9]{9}$)/.test(mobile);
	if(!bl){
	   alert("请输入正确的手机号码");
	   return;
	}
	$.post("../../front/dczj/ajax_sendcodes.do?randomCode="+randomCode
			+"&phoneMessage="+phoneMessage
			+"&mobile="+mobile+"&random="+Math.random()+"&dczjid="+formid,function(result){
		if($.trim(result.message) =='验证码已发送到您的手机，请注意查收!'){
			$('#surveyAnsw_90_0').attr("readonly","readonly");
			$("#vcode").removeAttr('onclick');
			alert($.trim(result.message));
		}else{
			alert($.trim(result.message));
		}
	});
}

function setUUID(uuid){
	$('#jsurveyuuid').val(uuid);
}



function showDetail(){
	var uuid = $('#jsurveyuuid').val();
	var jsurveyid = $('#dczjid').val();
	var rightnum = $('#rightnum').val();
	var sumscore = $("#sumscore").val();
//	window.location= "../../front/dczj/show_testansw.do?uuid="+uuid+"&dczjid="+jsurveyid+"&rightnum="+rightnum+"&sumscore="+sumscore;	

	$.ajax({ 
		  type: "post", 
		  url: contextpath+"/front/dczj/show_testansw.do?uuid="+uuid, 
		  data: {
			  dczjid:jsurveyid,
			  rightnum:rightnum,
			  sumscore:sumscore
		  }, 
		  success: function (str_response) { 
		  var obj = window.open("../../front/dczj/show_testansw.do?uuid="+uuid); //获取新页面的对象 
		  obj.document.write(str_response); //POST获取的返回写入新页面对象 
		  obj.focus(); 
		  obj.document.close();//一定要加上这句，不然会一直加载 
		  } 
		  }); 

}