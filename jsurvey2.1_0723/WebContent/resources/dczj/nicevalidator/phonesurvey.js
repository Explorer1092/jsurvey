var filepathstyle = '../..';
$(function() {
   var newfilepath = $("#filepath").val();
   if (newfilepath != ''){
	   filepathstyle = newfilepath;
   }
});

//查看结果	
function showResult(formid, resulturl){
		 $.ajax({
			 type: "GET",
			 url:filepathstyle+"/front/survey/ajaxsurvey_result.do",
			 data:"formid=" + formid + "&random=" + new Date(),
			 success:function(result){
			 	if(result.success){
			 		location.href = resulturl;
			 		//window.open(resulturl+"&build=0&silentlyBuild=1");
			 	}else{
			 		alert(result.message);
			 	}
		 	}
		 });
	}

//改变单选的其他选项输入框状态

function otherChange0(quesId,iid){
	$('#clearoption' + quesId).css("display", "inline");
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);

	}else if(iid!=0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyAnsw_"+quesId+ "_other").val("");
	}
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
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);
		$("#surveyMessage_other"+quesId).css("display","");
	}else if(iid==0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyAnsw_"+quesId+ "_other").val("");
		$("#surveyMessage_other"+quesId).css("display","none");
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

//清空 刷新页面
function qingkong(){
	 window.location.reload();
}

//查看答案
	function showAnswer(formid, answerurl){
		 $.ajax({
			 type: "GET",
			 url:filepathstyle+"/front/survey/ajaxsurvey_answer.do",
			 data:"formid=" + formid + "&random=" + new Date(),
			 success:function(result){
				 if(result.success){
				 		//window.open(answerurl);
				 		location.href = answerurl;
				 	}else{
				 		alert(result.message);
				 	}
		 	}
		 });
	}
	
	function viewResult(obj){
		var formid= $(obj).attr("formid");
		var url = $(obj).attr("url");
		var ajaxURL = $(obj).attr("baseurl") + "/ajax_surveyresult.jsp" ;
		
		$.ajax({
			 type: "GET",
			 url: ajaxURL,
			 data:"formid=" + formid + "&random=" + new Date(),
			 success:function(result){
			 	if(result.message == "OK"){
			 		window.open(url);
			 	}else{
			 		alert(result.message);
			 	}
		 	}
		});
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
		url:filepathstyle+"/front/survey/ajax_sendcodes.do?",
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

//跳转页面打开
function showSuccessTip(jumpurl){
		location.href = jumpurl;
}
//单选框样式切换
function chooseimg(obj,frame_style,quesId,iid,answid,filepath){
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);

	}else if(iid!=0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyAnsw_"+quesId+ "_other").val("");
	}
	var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
	//	$("#surveyAnsw_"+quesId+"_"+value).attr("checked",false);
		$( "#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/radio"+frame_style+".png");
	}
	//$("#"+answid).attr("checked",'checked');
	$("#"+answid).prop( "checked", true );
	$(obj).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
}
//单选框样式切换 点击答案时
function chooseimgword(frame_style,quesId,iid,answid,filepath){
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);

	}else if(iid!=0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyAnsw_"+quesId+ "_other").val("");
	}
	var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
	if(value!==undefined){
	//	$("#surveyAnsw_"+quesId+"_"+value).attr("checked",false);
		$( "#surveyAnsw_"+quesId+"_"+value ).prop( "checked", false );
		$("#img"+value).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/radio"+frame_style+".png");
	}
	//$("#"+answid).attr("checked",'checked');
	$("#"+answid).prop( "checked", true );
	$("#img"+iid).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/radiocheck"+frame_style+".png");
	$('#clearoption' + quesId).css("display", "inline");
}
//复选框样式切换
function choosecheckimg(obj,frame_style,quesId,iid,answid,filepath){
	var state =$("#surveyAnsw_"+quesId+ "_other").attr("disabled");
	if(iid==0&&state=='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",false);
		$("#surveyMessage_other"+quesId).css("display","");
	}else if(iid==0&&state!='disabled'){
		$("#surveyAnsw_"+quesId+ "_other").attr("disabled",true);
		$("#surveyAnsw_"+quesId+ "_other").val("");
		$("#surveyMessage_other"+quesId).css("display","none");
	}
	var checkstate = $("#"+answid).prop("checked");
	if(checkstate==false){
		//$("#"+answid).attr("checked",true);
		$("#"+answid).prop( "checked", true );
		$(obj).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/checkcheck"+frame_style+".png");
	}else{
		//$("#"+answid).attr("checked",false);
		$("#"+answid).prop( "checked", false );
		$(obj).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/check"+frame_style+".png");
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
		//$("#"+answid).attr("checked",true);
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/checkcheck"+frame_style+".png");
	}else{
		//$("#"+answid).attr("checked",false);
		$("#checkimg"+iid).attr("src",filepath+"/resources/dczj/survey/images/chooseframe/check"+frame_style+".png");
	}
}
function changecode(formid){
	document.getElementById('valicode'+formid).src=filepathstyle+"/front/survey/que_code.do?sessionid=surveyform"+formid+"&random="+Math.random();
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
	parent.location.reload();
}

//跳转到手机端结果页面
function showPhoneResult(resulturl,formid,buildSpacetime){
	$.ajax({		
		url: filepathstyle+"/surveyansw/showPhoneResult.do",
		data:{formid:formid,resulturl:resulturl},
		datatype:"json",
		success: function(result){
			if(result=='1'){
				window.location=resulturl; 
			}else{
				$("#bgcs").css({display:"none",height:$(document).height()});
				$("#popboxcs").css("display","none");
				mui.alert("结果页暂未生成，请"+buildSpacetime+"分钟后查看！");
			}
		},
		
        error: function (result) {
        	mui.alert(result.statusText);
        }
		});
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
function phonesurveysubmit(){
	var dczjcondition = 0;
	var configSurveyname = $("#configSurveyname").val();
	var configEnAge = $("#configEnAge").val();
	var configEnIdentifymust = $("#configEnIdentifymust").val();
	var configSurveyposition = $("#configSurveyposition").val();
	var configHomespace = $("#configHomespace").val();
	var configMailmust = $("#configMailmust").val();
	var configQqmust = $("#configQqmust").val();
	var surveyid = $("#surveyid").val();
	var piccodes = $("#piccodes").val();
	var phonecodes = $("#phonecodes").val();
	var limitType = $("#limitType").val();
	var limitTime = $("#limitTime").val();
	var mostSubmit = $("#mostSubmit").val();
	if(piccodes == 1 && phonecodes != 1){
		var code = $("#code").val();
		if(code==''){
			mui.alert("验证码不能为空",'',function(){});
			return;
		}
		$.ajax({
			 type: "post",
			 url:filepathstyle+"/front/survey/ajax_sendpiccodes.do",
			 data:"code=" + code,
			 async: false,
			 success:function(result){
			 if($.trim(result.message) =='图形验证码已失效，请刷新！'){
					mui.alert($.trim(result.message),'',function(){});
					dczjcondition = 1;
					return;
			 }else if($.trim(result.message) =='验证码填写错误！'){
					mui.alert($.trim(result.message),'',function(){});
					dczjcondition = 1;
					return;
			 	}
			 }
		});
	}
	if(phonecodes == 1){
		var mobile = $("#iphonenumber").val();
		var codes = $("#iponecode").val();
		if(mobile==''){
			mui.alert("手机号码不能为空",'',function(){});
			return;
		}
		if(codes==''){
			mui.alert("手机验证码不能为空",'',function(){});
			return;
		}
		$.ajax({
			 type: "post",
			 url:filepathstyle+"/front/survey/ajax_sendphonecodes.do",
			 data:"limitType=" + limitType + "&limitTime=" + limitTime + "&mostSubmit=" + mostSubmit + "&formid=" + surveyid + "&mobile=" + mobile + "&codes=" + codes,
			 async: false,
			 success:function(result){
			 if($.trim(result.message) != ''){
				mui.alert($.trim(result.message),'',function(){});
				dczjcondition = 1;
				return;
			    }
			 }
		});
	}
	if(limitType==1){
		$.ajax({
			 type: "post",
			 url:filepathstyle+"/front/survey/ajax_checkIp.do",
			 data:"limitType=" + limitType + "&limitTime=" + limitTime + "&mostSubmit=" + mostSubmit + "&formid=" + surveyid,
			 async: false,
			 success:function(result){
			 if($.trim(result.message) != ''){
				mui.alert($.trim(result.message),'',function(){});
				dczjcondition = 1;
				return;
			    }
			 }
		});
	}
	
	if(dczjcondition==0){
		$("#surveyForm"+surveyid).submit();
	}
}


//清除单选
function clearoption(beauty,framestyle,quesId){
	var radioName = "surveyAnsw"+quesId;
	$('#clearoption' + quesId).css("display", "none");
	if(beauty == 1){
		var value  = $('input[name="surveyAnsw'+quesId+'"]:checked').val();
		if(value!==undefined){
			$("#img"+value).attr("src",filepathstyle+"/resources/dczj/survey/images/chooseframe/radio"+framestyle+".png");
		}
	}
	$('input:radio[name="'+radioName+'"]').removeAttr('checked');
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
//	location.reload();
}