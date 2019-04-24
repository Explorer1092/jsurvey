var front_check_dczjid = $("#dczjid").val();
var contextpath = $("#contextpath").val();

$.ajax({
	 type: "post",
	 async:false, 
	 url:contextpath+"/front/jsurvey/checkUserLimit.do",
	 data:"dczjid="+front_check_dczjid,
	 success:function(result){
	 	if(result.success){
	 		window.location.href=contextpath+"/front/jsurvey/userlogin_pc.do?dczjid="+front_check_dczjid;
	 	}
	}
});

$.ajax({
	 type: "post",
	 url:contextpath+"/front/jsurvey/getUserInfo.do",
	 data:"dczjid="+front_check_dczjid,
	 success:function(result){
	 	if(result.success){
	 		username = result.params.username;
	 		email = result.params.email;
	 		mobile = result.params.mobile;
	 		address = result.params.address;
	 		$('#survey_name').val(username);
	 		$('#survey_mail').val(email);
	 		$('#survey_home').val(address);
	 		$('#iphonenumber').val(mobile);
	 	}
	}
});



