var quesJson;
var pageNum = 0;
var pageNumCount = 0;
var phonedczjid = $("#dczjid").val();
var validateQuesJson;

$.ajax({
	 type: "post",
	 url:"../../front/dczj/findQuesAndAnswJson.do",
	 data:"dczjid="+phonedczjid,
	 success:function(result){
	 	if(result.success){
            var hiddenQuesJson = result.params.hiddenQuesJson;
            quesJson = JSON.parse(hiddenQuesJson);
            for(var j in quesJson){
            	$('#surveyTable' + quesJson[j].hiddenQuesId).css("display", "none");
            }
	 	}
	}
});
var scorejson;
var scorenum;
var detail;	
//获取测评信息
$.ajax({
	 type:"post",
	 async:false, 
	 url:"../../front/dczj/findRightSocre.do",
	 data:"dczjid="+phonedczjid,
	 success:function(result){
	 	if(result.success){
	 		scorejson = result.params.json;
	 		scorenum = result.params.scorenum;
	 		detail = result.params.detail;
	 	}
	}
});

$.ajax({
	 type: "post",
	 url:"../../front/dczj/findPageNum.do",
	 data:"dczjid="+phonedczjid,
	 success:function(result){
	 	if(result.success){
	 		pageNumCount = result.params.pageNum;
	 		for(var pageA = 0; pageA <=pageNumCount ;pageA++){
	 			$('#surveyPaging' + pageA).css("display", "none");
	 		}
	 		$('#userform').css("display", "none");
	 		$('#surveySubmitBtn').css("display", "none");
	 		$('#surveyReplayBtn').css("display", "none");
	 		$('#surveyPaging0').css("display", "");
	 		var html = "<div id=\"phonebtnNext\" style=\"padding: 5px 5px 5px 5px;\"><button class=\"important-button3\" type=\"button\" onclick=\"phone_show_next_page();\">下一页</button></div>";
	 		$("#dczj"+phonedczjid).append(html);
	 	}
	}
});

$.ajax({
	 type: "post",
	 url:"../../front/dczj/findValidateQues.do",
	 data:"dczjid="+phonedczjid,
	 success:function(result){
	 	if(result.success){
	 		var textJson = result.params.validateQuesJson;
	 		validateQuesJson = JSON.parse(textJson);
	 	}
	}
});

document.onclick = function(event) {
	phoneDisplayQues();
	
	phoneValidateQues();
};


function phoneDisplayQues(){
	for(var k in quesJson){
    	if(quesJson[k].type == 0){
    		var radioName = "surveyAnsw"+ quesJson[k].dQuesId;
    		var radioVal = $('input:radio[name="'+radioName+'"]:checked').val();
    		if(radioVal != null && radioVal == quesJson[k].dAnswID){
    			$('#surveyTable' + quesJson[k].hiddenQuesId).css("display", "");
			}else{
				$('#surveyTable' + quesJson[k].hiddenQuesId).css("display", "none");
			}
    	}else if(quesJson[k].type == 1){
			var checkboxQues = document.getElementsByName("surveyAnsw"+ quesJson[k].dQuesId);               
            for(var m=0;m<checkboxQues.length;m++){
                if(checkboxQues[m].checked){
                	if(checkboxQues[m].value == quesJson[k].dAnswID){
                		$('#surveyTable' + quesJson[k].hiddenQuesId).css("display", "");
                		break;
                	}else{
                		$('#surveyTable' + quesJson[k].hiddenQuesId).css("display", "none");
                	}
                }else{
                	$('#surveyTable' + quesJson[k].hiddenQuesId).css("display", "none");
                }
            }
		}
    }
}

function phoneValidateQues(){
	var mustfillquesid = sessionStorage.getItem("mustfillquesid_"+phonedczjid);
	var mustfilltype = sessionStorage.getItem("mustfilltype_"+phonedczjid);
	if(mustfillquesid >0){
		if(mustfilltype == 0){
			 var radioname = "surveyAnsw"+ mustfillquesid;
			 var radioval = $('input:radio[name="'+radioname+'"]:checked').val();   
			 if(radioval != null && radioval != undefined){
				 $('#surveyMessage'+mustfillquesid).html("");
				 sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
				 sessionStorage.removeItem("mustfilltype_"+phonedczjid);
			 }
		}else if(mustfilltype == 1){
			 var checkboxnum = 0 ;
	    	 var checkboxname = "surveyAnsw"+ mustfillquesid;
			 var checkboxName=document.getElementsByName(checkboxname);
			 var checkboxminvalue = sessionStorage.getItem("mustfillmin_"+phonedczjid);
			 var checkboxmaxvalue = sessionStorage.getItem("mustfillmax_"+phonedczjid);
			 for(var kkk=0;kkk<checkboxName.length;kkk++){
		         if(checkboxName[kkk].checked){
		             checkboxnum ++;
		          }
		      }
			  if(checkboxnum >= checkboxminvalue && checkboxnum <= checkboxmaxvalue){
				  $('#surveyMessage'+mustfillquesid).html("");
				  sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
				  sessionStorage.removeItem("mustfilltype_"+phonedczjid);
				  sessionStorage.removeItem("mustfillmin_"+phonedczjid);
				  sessionStorage.removeItem("mustfillmax_"+phonedczjid);
			  }
		}else if(mustfilltype == 2){
			var textValue = $("#surveyAnsw_"+mustfillquesid+"_0").val();
			textValue = $.trim(textValue)
			if(textValue.length > 0){
				 sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
				 sessionStorage.removeItem("mustfilltype_"+phonedczjid);
				 $('#surveyMessage'+mustfillquesid).html("");
			}
		}else if(mustfilltype == 5){
			var textValue = $("#surveyAnsw_"+mustfillquesid+"_0").val();
			textValue = $.trim(textValue)
			var validaterules = sessionStorage.getItem("mustfillvalidaterules_"+phonedczjid);
			if(textValue.length > 0){
				if(validaterules == 0){
					sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
					sessionStorage.removeItem("mustfilltype_"+phonedczjid);
					sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
					$('#surveyMessage'+mustfillquesid).html("");
				}else if(validaterules == 1){
					var reg = /^[\u4E00-\u9FA5]+$/; 
	    			if (reg.test(textValue)){
	    				sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
						sessionStorage.removeItem("mustfilltype_"+phonedczjid);
						sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
						$('#surveyMessage'+mustfillquesid).html("");
	    			} 
				}else if(validaterules == 2){
					if (parseFloat(textValue).toString() != "NaN") {
						sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
						sessionStorage.removeItem("mustfilltype_"+phonedczjid);
						sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
						$('#surveyMessage'+mustfillquesid).html("");
	    			}
				}else if(validaterules == 3){
					var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); 
	    			if (reg.test(textValue)){
	    				sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
						sessionStorage.removeItem("mustfilltype_"+phonedczjid);
						sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
						$('#surveyMessage'+mustfillquesid).html("");
	    			} 
				}else if(validaterules == 4){
					var reg=/^[1][3,4,5,7,8][0-9]{9}$/; 
	    			if (reg.test(textValue)){
	    				sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
						sessionStorage.removeItem("mustfilltype_"+phonedczjid);
						sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
						$('#surveyMessage'+mustfillquesid).html("");
	    			} 
				}
			}
		}
	}
}

function phone_show_next_page() {
	 var pageQuesJson = $("#surveyText"+pageNum).val();
	 var checknum = 0;
	 if(pageQuesJson != '' && pageQuesJson != undefined){
		 var surveyPageQuesJson = JSON.parse(pageQuesJson);
		 for ( var ii in surveyPageQuesJson) {
			 var quesId = surveyPageQuesJson[ii].quesId;
			 var type = surveyPageQuesJson[ii].type;
			 var tableId = "surveyTable" + quesId;
			 var mustfill =  surveyPageQuesJson[ii].mustfill;
            if($('#'+tableId).css("display") != "none"){
            	if(type == 2){
           		 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
					 textValue = $.trim(textValue)
           		 if(textValue.length > 2000){
		//				  sessionStorage.setItem("mustfillquesid_"+projsurveyid,quesId);
		//				  sessionStorage.setItem("mustfilltype_"+projsurveyid,type);
						  $('#surveyMessage'+quesId).html("(输入的字数不能超过2000!)");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					 } 
           	   }
            	
			     if(type == 0 && mustfill == 1){
			    	 var radioname = "surveyAnsw"+ quesId;
					 var radioval = $('input:radio[name="'+radioname+'"]:checked').val();   
					 if(radioval == null){
						 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						 $('#surveyMessage'+quesId).html("（该题目必填）");
						 location.href = "#" + tableId;
						 checknum++;
						 return;
					 }
					  }else if(type == 6 && mustfill == 1){
			    	 var radioname = "surveyAnsw"+ quesId;
					 var radioval = $('input:radio[name="'+radioname+'"]:checked').val();   
					 if(radioval == null){
						 sessionStorage.setItem("mustfillquesid_"+projsurveyid,quesId);
						 sessionStorage.setItem("mustfilltype_"+projsurveyid,type);
						 $('#surveyMessage'+quesId).html("（该题目必填）");
						 location.href = "#" + tableId;
						 checknum++;
						 return;
					 }
			     }else if(type == 1 && mustfill == 1){
			    	 var checkboxnum = 0 ;
			    	 var checkboxname = "surveyAnsw"+ quesId;
					 var checkboxName=document.getElementsByName(checkboxname);
					 var checkboxminvalue = surveyPageQuesJson[ii].min;
					 var checkboxmaxvalue = surveyPageQuesJson[ii].max;
					 for(var jjj=0;jjj<checkboxName.length;jjj++){
				         if(checkboxName[jjj].checked){
				             checkboxnum ++;
				          }
				      }
					  if(checkboxnum < checkboxminvalue || checkboxnum > checkboxmaxvalue){
						  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						  sessionStorage.setItem("mustfillmin_"+phonedczjid,checkboxminvalue);
						  sessionStorage.setItem("mustfillmax_"+phonedczjid,checkboxmaxvalue);
						  $('#surveyMessage'+quesId).html("（该题目必填且范围在"+checkboxminvalue+"~"+checkboxmaxvalue+"）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					  }
					    }else if(type == 7 && mustfill == 1){
			    	 var checkboxnum = 0 ;
			    	 var checkboxname = "surveyAnsw"+ quesId;
					 var checkboxName=document.getElementsByName(checkboxname);
					 var checkboxminvalue = surveyPageQuesJson[ii].min;
					 var checkboxmaxvalue = surveyPageQuesJson[ii].max;
					 for(var jjj=0;jjj<checkboxName.length;jjj++){
				         if(checkboxName[jjj].checked){
				             checkboxnum ++;
				          }
				      }
					  if(checkboxnum < checkboxminvalue || checkboxnum > checkboxmaxvalue){
						  sessionStorage.setItem("mustfillquesid_"+projsurveyid,quesId);
						  sessionStorage.setItem("mustfilltype_"+projsurveyid,type);
						  sessionStorage.setItem("mustfillmin_"+projsurveyid,checkboxminvalue);
						  sessionStorage.setItem("mustfillmax_"+projsurveyid,checkboxmaxvalue);
						  $('#surveyMessage'+quesId).html("（该题目必填且范围在"+checkboxminvalue+"~"+checkboxmaxvalue+"）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					  }
			     }else if(type == 2 && mustfill == 1){
					 var charLimit = surveyPageQuesJson[ii].charlimit;
					 var textMin = surveyPageQuesJson[ii].min;
					 var textMax = surveyPageQuesJson[ii].max;
					 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
					 textValue = $.trim(textValue)
					 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
						  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						  $('#surveyMessage'+quesId).html("（该问答题必填！）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					 }
			     }else if(type == 5 && mustfill == 1){
			    	 var validaterules = surveyPageQuesJson[ii].validaterules;
			    	 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
			    	 textValue = $.trim(textValue)
			    	 if(mustfill == 1){
			    		 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
							  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
							  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
							  sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
							  $('#surveyMessage'+quesId).html("（该题目必填！）");
							  location.href = "#" + tableId;
							  checknum++;
							  return;
						 }
			    	 }
			    	 if(validaterules == 0){
			    		 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
			    			 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
							 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
							 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
							 $('#surveyMessage'+quesId).html("（该题目必填！）");
							 location.href = "#" + tableId;
							 checknum++;
							 return;
						 }
			    	 }else if(validaterules == 1){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg = /^[\u4E00-\u9FA5]+$/; 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入中文！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
						 }
			    	 }else if(validaterules == 2){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 if (parseFloat(textValue).toString() == "NaN") {
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入数字！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 }
			    		 }
			    	 }else if(validaterules == 3){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入正确的邮箱！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
			    		 }
			    	 }else if(validaterules == 4){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg=/^[1][3,4,5,7,8][0-9]{9}$/; 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入正确的手机号码！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
			    		 }
			    	 }
			     }
			 }
		 }
		 
		 if(checknum > 0){
			 alert('有必填项未填或者不符合要求！');
		 }else{
			 $('#btnPre').css("display", "");
			 $('#surveyPaging' + pageNum).css("display", "none");
			 pageNum = pageNum + 1;
			 $('#surveyPaging' + pageNum).css("display", "");
			 if(pageNum >= pageNumCount){
				 $('#userform').css("display", "");
			     $('#surveySubmitBtn').css("display", "");
			 	 $('#surveyReplayBtn').css("display", "");
			 	 $('#phonebtnNext').css("display", "none");
			 }
		 }
	 }
}



function phonesurveysubmitvalidate(){
	 var checknum = 0;
	 if(validateQuesJson != null && validateQuesJson != undefined){
		 for ( var ii in validateQuesJson) {
			 var quesId = validateQuesJson[ii].quesId;
			 var type = validateQuesJson[ii].type;
			 var tableId = "surveyTable" + quesId;
			 var mustfill =  validateQuesJson[ii].mustfill;
             if($('#'+tableId).css("display") != "none"){
            	 if(type == 2){
            		 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
					 textValue = $.trim(textValue)
            		 if(textValue.length >2000){
		//				  sessionStorage.setItem("mustfillquesid_"+projsurveyid,quesId);
		//				  sessionStorage.setItem("mustfilltype_"+projsurveyid,type);						  
						  $('#surveyMessage'+quesId).html("(输入的字数不能超过2000!)");
						 location.href = "#" + tableId;
						  checknum++;
						  return;
					 } 
            	 }
            	 
			     if(type == 0 && mustfill == 1){
			    	 var radioname = "surveyAnsw"+ quesId;
					 var radioval = $('input:radio[name="'+radioname+'"]:checked').val();   
					 if(radioval == null){
						 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						 $('#surveyMessage'+quesId).html("（该题目必填）");
						 location.href = "#" + tableId;
						 checknum++;
						 return;
					 }
					  }else if(type == 6 && mustfill == 1){
			    	 var radioname = "surveyAnsw"+ quesId;
					 var radioval = $('input:radio[name="'+radioname+'"]:checked').val();   
					 if(radioval == null){
						 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						 $('#surveyMessage'+quesId).html("（该题目必填）");
						 location.href = "#" + tableId;
						 checknum++;
						 return;
					 }
			     }else if(type == 1 && mustfill == 1){
			    	 var checkboxnum = 0 ;
			    	 var checkboxname = "surveyAnsw"+ quesId;
					 var checkboxName=document.getElementsByName(checkboxname);
					 var checkboxminvalue = validateQuesJson[ii].min;
					 var checkboxmaxvalue = validateQuesJson[ii].max;
					 for(var jjj=0;jjj<checkboxName.length;jjj++){
				         if(checkboxName[jjj].checked){
				             checkboxnum ++;
				          }
				      }
					  if(checkboxnum < checkboxminvalue || checkboxnum > checkboxmaxvalue){
						  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						  sessionStorage.setItem("mustfillmin_"+phonedczjid,checkboxminvalue);
						  sessionStorage.setItem("mustfillmax_"+phonedczjid,checkboxmaxvalue);
						  $('#surveyMessage'+quesId).html("（该题目必填且范围在"+checkboxminvalue+"~"+checkboxmaxvalue+"）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					  }
					    }else if(type == 6 && mustfill == 1){
			    	 var checkboxnum = 0 ;
			    	 var checkboxname = "surveyAnsw"+ quesId;
					 var checkboxName=document.getElementsByName(checkboxname);
					 var checkboxminvalue = validateQuesJson[ii].min;
					 var checkboxmaxvalue = validateQuesJson[ii].max;
					 for(var jjj=0;jjj<checkboxName.length;jjj++){
				         if(checkboxName[jjj].checked){
				             checkboxnum ++;
				          }
				      }
					  if(checkboxnum < checkboxminvalue || checkboxnum > checkboxmaxvalue){
						  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						  sessionStorage.setItem("mustfillmin_"+phonedczjid,checkboxminvalue);
						  sessionStorage.setItem("mustfillmax_"+phonedczjid,checkboxmaxvalue);
						  $('#surveyMessage'+quesId).html("（该题目必填且范围在"+checkboxminvalue+"~"+checkboxmaxvalue+"）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					  }
			     }else if(type == 2 && mustfill == 1){
					 var charLimit = validateQuesJson[ii].charlimit;
					 var textMin = validateQuesJson[ii].min;
					 var textMax = validateQuesJson[ii].max;
					 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
					 textValue = $.trim(textValue)
					 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
						  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
						  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
						  $('#surveyMessage'+quesId).html("（该问答题必填！）");
						  location.href = "#" + tableId;
						  checknum++;
						  return;
					 }
			     }else if(type == 5 && mustfill == 1){
			    	 var validaterules = validateQuesJson[ii].validaterules;
			    	 var textValue = $("#surveyAnsw_"+quesId+"_0").val();
			    	 textValue = $.trim(textValue)
			    	 if(mustfill == 1){
			    		 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
							  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
							  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
							  sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
							  $('#surveyMessage'+quesId).html("（该题目必填！）");
							  location.href = "#" + tableId;
							  checknum++;
							  return;
						 }
			    	 }
			    	 if(validaterules == 0){
			    		 if(!(textValue.length > 0 && textValue != '' && textValue != undefined)){
							  sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
							  sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
							  sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
							  $('#surveyMessage'+quesId).html("（该题目必填！）");
							  location.href = "#" + tableId;
							  checknum++;
							  return;
						 }
			    	 }else if(validaterules == 1){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg = /^[\u4E00-\u9FA5]+$/; 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入中文！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
						 }
			    	 }else if(validaterules == 2){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 if (parseFloat(textValue).toString() == "NaN") {
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入数字！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 }
			    		 }
			    	 }else if(validaterules == 3){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入正确的邮箱！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
			    		 }
			    	 }else if(validaterules == 4){
			    		 if(textValue.length > 0 && textValue != '' && textValue != undefined){
			    			 var reg=/^[1][3,4,5,7,8][0-9]{9}$/; 
			    			 if (!reg.test(textValue)){
			    				 sessionStorage.setItem("mustfillquesid_"+phonedczjid,quesId);
								 sessionStorage.setItem("mustfilltype_"+phonedczjid,type);
								 sessionStorage.setItem("mustfillvalidaterules_"+phonedczjid,validaterules);
			    				 $('#surveyMessage'+quesId).html("（请输入正确的手机号码！）");
								 location.href = "#" + tableId;
								 checknum++;
								 return;
			    			 } 
			    		 }
			    	 }
			     }
			 }
		 }
	if(scorejson != null && scorejson != undefined){
			 var allrightscore = 0;
			 var newscorejson = scorejson;
			 newscorejson = JSON.parse(newscorejson);
			 var rightnum = 0;
			 for (var iii in newscorejson) {
				 var scorequesid = newscorejson[iii].quesid;
				 var scoretype = newscorejson[iii].type;
				 var scorerightansw = newscorejson[iii].rightansw;
				 var quesscore = newscorejson[iii].quesscore;
				 if(scoretype == 6){
					 var score_radioval = $('input:radio[name="surveyAnsw'+scorequesid+'"]:checked').val(); 
					 if(scorerightansw == score_radioval){
						 allrightscore = parseInt(allrightscore) + parseInt(quesscore);
						 rightnum ++;
					 }
				 }else if(scoretype == 7){
					 var score_checkboxval = "";
					 $('input:checkbox[name="surveyAnsw'+scorequesid+'"]:checked').each(function(){
						 score_checkboxval+=$(this).val()+","
					 });
					 if(scorerightansw+"," == score_checkboxval){
						 allrightscore = parseInt(allrightscore) + parseInt(quesscore);
						 rightnum ++;
					 }
				 }
			 }
			 $("#sumscore").val(allrightscore);
			 $("#rightnum").val(rightnum);
			 var divhtml;
			 if(detail != null && detail!="null"){
				 divhtml = "您的答对的题目："+rightnum+"题<br>您的得分："+allrightscore+"分"; 
			 }
			 /*if(detail == "1"){
				 divhtml += " <label style='cursor:pointer;padding-left:10px;color:#56b6ff;' onclick='showDetail();'><u>答案详情</u></label>"
			 }*/
			 $("#bgdeatail").html(divhtml);
			}
		 if(checknum > 0){
			 alert('有必填项未填或者不符合要求！');
		 }else{
			 sessionStorage.removeItem("mustfillquesid_"+phonedczjid);
			 sessionStorage.removeItem("mustfilltype_"+phonedczjid);
			 sessionStorage.removeItem("mustfillmin_"+phonedczjid);
			 sessionStorage.removeItem("mustfillmax_"+phonedczjid);
			 sessionStorage.removeItem("mustfillvalidaterules_"+phonedczjid);
			 phonesurveysubmit();
		 }
	 }
}