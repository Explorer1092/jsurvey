$(function() {
	
	jsurveyvisitcount();
	
	var x = document.cookie;
    var uv = "";	   
    var date = new Date();//获取当前时间
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var currentDate = ""+year+month+strDate;
    var cookieid = $('#dczjid').val();
    cookieid = "phonecookie"+cookieid	    
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
		type:"post",
		url:"../../front/jsurvey/visitCount.do",
		data:{jsurveyid:jsurveyid},
		datatype:"json",
	});

}

function setUUID(uuid){
	$('#jsurveyuuid').val(uuid);
}

//改变单选的其他选项输入框状态
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

//发送手机验证码
function sendCode(formid,phoneMessage){
	var mobileishavavalid = false;
	$('#iphonenumber').isValid(function(v){
		mobileishavavalid = v;
	});
	if(!mobileishavavalid){
		return;
	}
	
	var mobile=$('#iphonenumber').val();
	$.ajax({
		type:"post",
		url:"../../front/survey/ajax_sendcodes.do?",
		data:"mobile="+mobile+"&formid="+formid+"&phoneMessage="+encodeURI(phoneMessage),
		success:function(result){
			if(result.success) {
				mui.alert($.trim(result.message));
				var num = 120;
				timer = setInterval(function(){
					$("#sendMobileCodeTime").css('display','inline-block');
					$("#sendMobileCode").hide();
					$("#sendMobileCodeTime").html(num--+"s后重新发送");
					num = num--;
				}, 1000);
				setTimeout(function(){
					clearInterval(timer);
					$("#sendMobileCodeTime").hide();
					$("#sendMobileCode").show();
				}, 121*1000);
			}else {
				mui.alert(result.message);
			}
		}
	});
}

//单选框样式切换
function chooseimg(obj,frame_style,quesId,iid,answid,filepath){
	var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
		$( "#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/images/chooseframe/radio"+frame_style+".png");
	}
	$("#"+answid).prop( "checked", true );
	$(obj).attr("src",filepath+"/resources/dczj/images/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
}
//单选框样式切换 点击答案时
function chooseimgword(frame_style,quesId,iid,answid,filepath){
	var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
		$( "#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/images/chooseframe/radio"+frame_style+".png");
	}
	$("#"+answid).prop( "checked", true );
	$("#img"+iid).attr("src",filepath+"/resources/dczj/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
}
//复选框样式切换
function choosecheckimg(obj,frame_style,quesId,iid,answid,filepath){
	var checkstate = $("#"+answid).prop("checked");
	if(checkstate==false){
		$("#"+answid).prop( "checked", true );
		$(obj).attr("src",filepath+"/resources/dczj/chooseframe/checkcheck"+frame_style+".png");
	}else{
		$("#"+answid).prop( "checked", false );
		$(obj).attr("src",filepath+"/resources/dczj/chooseframe/check"+frame_style+".png");
	}
}
//复选框样式切换 点击答案时
function choosecheckimgword(frame_style,quesId,iid,answid,filepath){
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyMessage_other"+quesId).css("display","");
	}else if(iid==0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);
		$("#surveyAnsw_"+quesId+ "_other").val("");
		$("#surveyMessage_other"+quesId).css("display","none");
	}
	var checkstate = $("#"+answid).prop("checked");
	if(checkstate==false){
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/checkcheck"+frame_style+".png");
	}else{
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/images/chooseframe/check"+frame_style+".png");
	}
}
function changecode(formid){
	document.getElementById('valicode'+formid).src="../../front/dczj/que_code.do?sessionid=surveyform"+formid+"&random="+Math.random();
}
function showerrormessage(message){
	$("#errormessage").html(message);
}

function phonesurveysubmit(){
	var dczjid = $("#dczjid").val();
	$("#dczj"+dczjid).submit();
}

//清除单选
function clearoption(framestyle,quesId){
	var radioName = "surveyAnsw"+quesId;
	$('#clearoption' + quesId).css("display", "none");
	if(framestyle > 0){
		var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
		if(value!==undefined){
			$("#img"+value).attr("src","../../resources/dczj/images/chooseframe/radio"+framestyle+".png");
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

function showDraw(){
	$("#bgcs").css({display:"none",height:$(document).height()});
	$("#popboxcs").css("display","none");
	$("#drawbgcs").css({display:"block",height:$(document).height()});
	$("#drawboxcs").css("display","block");
    var objH= '380';//浮动对象的高度 
    var ch=$(window).height();//屏幕的高度   
    var objT=(Number(ch)-Number(objH))/2;   //思路  浮动高度+（（屏幕高度-对象高度））/2
    $("#drawboxcs").css("top",objT);
}

function closeDrawWind(){
	$("#drawbgcs").css({display:"none",height:$(document).height()});
	$("#drawboxcs").css("display","none");
}

//显示查看结果提示页面弹窗
var t ;
var intervalid; 
function alertResultWind(jumpurl){
	$("#surveySubmit").attr('disabled','disabled');
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

//跳转到手机结果页面
function showPhoneResult(resulturl,dczjid){
	$.ajax({		
		url: "../../check/open/user.do",
		data:{dczjid:dczjid},
		datatype:"json",
		success: function(result){
			if(result.success){
				$.ajax({		
					url: "../../check/open/checkPhoneResult.do",
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

//隐藏查看结果提示页面弹窗
function closeResultWind(){
	$("#bgcs").css({display:"none",height:$(document).height()});
	$("#popboxcs").css("display","none");
	parent.location.reload();
}


function showDetail(){
	var uuid = $('#jsurveyuuid').val();
	var jsurveyid = $('#dczjid').val();
	var rightnum = $('#rightnum').val();
	var sumscore = $("#sumscore").val();

	var wx= (function(){
	return navigator.userAgent.toLowerCase().indexOf('micromessenger') !== -1
	}
	)();
	if(wx){
       location.href = "../../front/dczj/show_testansw.do?uuid="+uuid+"&dczjid="+jsurveyid+"&rightnum="+rightnum+"&sumscore="+sumscore+"&state=1";		
	}else {
		titlePost('../../front/dczj/show_testansw.do?uuid='+uuid,{dczjid :jsurveyid,rightnum:rightnum, sumscore:sumscore,state:1});
	}

	/*if(isWeiXin()){
		wx.navigateTo({			
			  url: "../../front/dczj/show_testansw.do?uuid="+uuid+"&dczjid="+jsurveyid+"&rightnum="+rightnum+"&sumscore="+sumscore+"&state=1"
			})
	}else{
		titlePost('../../front/dczj/show_testansw.do?uuid='+uuid,{dczjid :jsurveyid,rightnum:rightnum, sumscore:sumscore,state:1});

	}
*/
}

function titlePost(URL, PARAMS) {
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    temp_form.target = "_blank";
    temp_form.method = "post";
    temp_form.style.display = "none";
    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp_form .appendChild(opt);
    }
    document.body.appendChild(temp_form);
    temp_form.submit();
}
function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	//console.log(ua);//mozilla/5.0 (iphone; cpu iphone os 9_1 like mac os x) applewebkit/601.1.46 (khtml, like gecko)version/9.0 mobile/13b143 safari/601.1
	  if(ua.match(/MicroMessenger/i) == 'micromessenger' || ua.match(/_SQ_/i) == '_sq_'){
		return true;
	} else {
		return false;
	}
}

