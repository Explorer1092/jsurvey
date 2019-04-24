<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<h:head pagetype="page" calendar="true" toggle="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script type="text/javascript" src="${contextPath}/resources/dczj/js/setting.js"></script>
<script src="${contextPath}/resources/dczj/js/closetree.js"></script>
<style type="text/css">
	body{
		background-color: white;
	}
	.toggle-on{
		background-color: #01AAED;
	}
	.layui-form-radio>i:hover, .layui-form-radioed>i {
    	color: #01AAED;
	}
	.layui-form-select dl dd.layui-this {
	    background-color: #01AAED;
	    color: #fff;
	}	
</style>
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
</head>
<body style="width: 100%;height: 100%;">
<form class="layui-form" action="" style="padding-left: 40px;" lay-filter="settingform">
	<input type="hidden" id="dczjid" name="dczjid" value="${setting.dczjid}">
	<input type="hidden" id="contextPath" name="contextPath" value="${contextPath}">
	<div style="display: inline-block;width: 22%;height:500px;">
		<table>
			<tr height="70px;">
				<td><img alt="参与限制" src="${contextPath}/resources/dczj/images/u3916.png"></td>
				<td style="font-size: 16px;color: #333333;font-weight: bold;">参与限制</td>
			</tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;">
					<input type="hidden" value="${setting.isstart}" id="isstart" name="isstart" />
				</td>
				<td width="150px;" style="padding-top: 8px;">定时发布问卷</td>
			</tr>
			<tr height="40px;">
				<td></td>
				<td>
					<input type="text" id="starttime" name="starttime" value="<fmt:formatDate value="${setting.starttime}" type="date" pattern="yyyy-MM-dd"/>" class="jcalendar input-text" style="width:150px;" 
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){var endDate = $('#endtime').val();var startdate = $('#starttime').val();modify('starttime',startdate,1);modifyStateByTime(startdate, endDate);},maxDate:'#F{$dp.$D(\'endtime\')}'})" readonly="readonly">
				</td>
			</tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;"><input type="hidden" value="${setting.isend}" id="isend" name="isend" /></td>
				<td width="150px;" style="padding-top: 8px;">设定问卷结束时间</td>
			</tr>
			<tr height="40px;">
				<td></td>
				<td>
					<input type="text" id="endtime" name="endtime" value="<fmt:formatDate value="${setting.endtime}" type="date" pattern="yyyy-MM-dd"/>" class="jcalendar input-text" style="width:150px;" 
					onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){var startdate = $('#starttime').val();var endDate = $('#endtime').val();modify('endtime',endDate,1);modifyStateByTime(startdate, endDate);},minDate:'#F{$dp.$D(\'starttime\')}'})" readonly="readonly">
				</td>
			</tr>
			<tr height="40px;">
				<td style="padding-top: 8px;"><input type="hidden" value="${setting.islimituser}" id="islimituser" name="islimituser" /></td>
				<td width="150px;" style="padding-top: 8px;">限定登录用户参与</td>
			</tr>
			<tr class="islimituser_hide" height="20px;"><td></td><td style="font-size: 12px;color: #999999">默认限制全部登录用户</td></tr>
			<tr class="islimituser_hide"><td></td><td><a onclick="openDialog('${contextPath}/manager/setting/limit_user.do?dczjid=${dczjid}',600,500,{title:'指定登录用户'})" style="text-decoration: underline;color: #01AAED;cursor: pointer;">指定登录用户</a></td></tr>
		</table>
	</div>
	<div style="display: inline-block;width: 28%;height:500px;">
		<table>
			<tr height="70px;">
				<td><img alt="提交限制" src="${contextPath}/resources/dczj/images/u3918.png"></td>
				<td style="font-size: 16px;color: #333333;font-weight: bold;">提交限制</td>
			</tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;"><input type="hidden" value="${setting.iscode}" id="iscode" name="iscode" /></td>
				<td width="300px;" style="padding-top: 8px;">验证码</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input lay-filter="codes" type="radio" name="codes" value="0" title="图形验证码 ">
					<div class="layui-form-item" style="margin-bottom: 0px;">
						<div class="layui-inline" style="margin-right: 0px;">
							<div class="layui-input-inline">
								<input lay-filter="codes" type="radio" name="codes" value="1" title="手机短信验证码 ">
							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr class="codes_hide"><td></td><td><a onclick="openDialog('${contextPath}/manager/setting/message_opr.do?dczjid=${dczjid}',500,350,{title:'设置短信内容'});"
			 style="text-decoration: underline;color: #01AAED;cursor: pointer;">设置短信验证内容</a></td></tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;"><input type="hidden" value="${setting.submitlimit}" id="submitlimit" name="submitlimit" /></td>
				<td width="300px;" style="padding-top: 8px;">限制参与提交次数</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="radio" lay-filter="limittype" name="limittype" value="0" title="限制同一IP">
					<div class="layui-form-item" style="margin-bottom: 0px;">
						<div class="layui-inline" style="margin-right: 0px;">
							<label class="layui-form-label" style="padding-left: 0px;padding-right: 8px;">限同一IP地址</label>
							<div class="layui-input-inline" style="width: 80px;">
								<select id="limittime" name="limittime" lay-filter="limittime">
				  					<option value="0">只能</option>
				  					<option value="1">每小时</option>
				  					<option value="2">每天</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-mid">提交</label>
							<div class="layui-input-inline" style="width: 30px;">
								<input type="text" id="limitnumber" name="limitnumber" required lay-verify="required" placeholder="1" autocomplete="off" class="layui-input" value="${setting.limitnumber}">
							</div>
							<label class="layui-form-mid">次</label>
						</div>
					</div>
					<input type="radio" lay-filter="limittype" name="limittype" value="1" title="限制同一手机号">
					<div class="layui-form-item">
						<div class="layui-inline" style="margin-right: 0px;">
							<label class="layui-form-label" style="padding-left: 0px;padding-right: 8px;">限同一手机号</label>
							<div class="layui-input-inline" style="width: 80px;">
								<select id="limittime" name="limittime" lay-filter="limittime">
				  					<option value="0">只能</option>
				  					<option value="1">每小时</option>
				  					<option value="2">每天</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-mid">提交</label>
							<div class="layui-input-inline" style="width: 30px;">
								<input type="text" id="limitnumber" name="limitnumber" required lay-verify="required" placeholder="1" autocomplete="off" class="layui-input" value="${setting.limitnumber}">
							</div>
							<label class="layui-form-mid">次</label>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: inline-block;width: 24%;height:500px;">
		<table>
			<tr height="70px;">
				<td><img alt="公开结果&跳转设置" src="${contextPath}/resources/dczj/images/u3920.png"></td>
				<td style="font-size: 16px;color: #333333;font-weight: bold;">公开结果&跳转设置</td>
			</tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;"><input type="hidden" value="${setting.isopen}" id="isopen" name="isopen" /></td>
				<td width="250px;" style="padding-top: 8px;">公开问卷结果</td>
			</tr>
			<tr class="isopen_hide" height="20px;"><td></td><td style="font-size: 12px;color: #999999">默认对能够参与用户都公开</td></tr>
			<tr class="isopen_hide"><td></td><td><a onclick="openResultUser();" style="text-decoration: underline;color: #01AAED;cursor: pointer;">指定公开用户</a></td></tr>
			<tr><td colspan="2"><hr></td></tr>
			<tr>
				<td></td>
				<td>
					<input type="radio" lay-filter="isjump" name="isjump" value="0" title="显示感谢信息">
					<div class="layui-form-item" style="margin-bottom: 0px;">
						<div class="layui-inline" style="margin-right: 0px;">
							<div class="layui-input-inline" style="width: 250px;">
								<a class="isjump_0_hide" onclick="openDialog('${contextPath}/manager/setting/thankscontent_opr.do?dczjid=${dczjid}',600,400,{title:'编辑感谢内容'})" 
								style="text-decoration: underline;color: #01AAED;cursor: pointer;">编辑感谢信内容,设置抽奖和结果按钮</a>
							</div>
						</div>
					</div>
					<input type="radio" lay-filter="isjump" name="isjump" value="1" title="跳转至指定页面">
					<div class="layui-form-item" style="margin-bottom: 0px;">
						<div class="layui-inline" style="margin-right: 0px;">
							<div class="layui-input-inline" style="width: 200px;">
								<input type="text" id="jumpurl" name="jumpurl" 
								required lay-verify="required" placeholder="请输入指定页面URL" autocomplete="off" class="layui-input isjump_1_hide"
								 value="${setting.jumpurl}"> 
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div style="display: inline-block;width: 22%;height:500px;">
		<table>
			<tr height="70px;">
				<td><img alt="参与奖品" src="${contextPath}/resources/dczj/images/u3922.png"></td>
				<td style="font-size: 16px;color: #333333;font-weight: bold;">参与奖品</td>
			</tr>
			<tr height="40px;">
				<td width="60px;" style="padding-top: 8px;"><input type="hidden" value="${setting.isprize}" id="isprize" name="isprize" /></td>
				<td width="250px;" style="padding-top: 8px;">提供参与奖品</td>
			</tr>
			<tr class="isprize_hide"><td></td><td><a onclick="openDialog('${contextPath}/manager/setting/prize_opr.do?dczjid=${dczjid}',700,450,{title:'设置抽奖规则'})" 
			style="text-decoration: underline;color: #01AAED;cursor: pointer;">设置抽奖规则</a></td></tr>
			<tr>
				<td></td>
				<td>
					<input type="radio" lay-filter="prizetime" name="prizetime" value="0" title="仅可以参与一次">
					<input type="radio" lay-filter="prizetime" name="prizetime" value="1" title="跟随参与提交次数 ">
				</td>
			</tr>
		</table>
	</div>
</form>
</body>
<script>
$(function(){
	if($("#islimituser").val()==0){
		$(".islimituser_hide").css("visibility","hidden");
	}
	if($("#isopen").val()==0){
		$(".isopen_hide").css("visibility","hidden");
	}
	if($("#isprize").val()==0){
		$(".isprize_hide").css("visibility","hidden");
	}
	var isjump = ${setting.isjump};
	setChecked("isjump", isjump);
	if(isjump==0){
		$(".isjump_1_hide").css("visibility","hidden");
		$(".isjump_0_hide").css("visibility","visible");
	}else{
		$(".isjump_0_hide").css("visibility","hidden");
		$(".isjump_1_hide").css("visibility","visible");
	}
	var iscode = ${setting.iscode};
	if(iscode == 1){
		var codes = ${setting.codes};
		setChecked("codes", codes);
		if(codes==0){
			$(".codes_hide").css("visibility","hidden");
		}
	}else{
		$(".codes_hide").css("visibility","hidden");
	}
	var submitlimit = ${setting.submitlimit};
	if(submitlimit == 1){
		var limittype = ${setting.limittype};
		setChecked("limittype", limittype);
	}
	var isprize = ${setting.isprize};
	if(isprize == 1){
		var prizetime = ${setting.prizetime};
		setChecked("prizetime", prizetime);
	}
	function setChecked(name, value){
		var str=document.getElementsByName(name); 
		var objarray=str.length;
		for(i=0;i<objarray;i++){
			if(str[i].value == value){
				str[i].checked = true;
			}
		}
	}
	layui.use('element', function(){
		 var element = layui.element;
	});
	layui.use('form', function(){
			  var form = layui.form;
			  
			  //监听radio
			form.on('radio(codes)', function(data){
				if(data.value==0){
					$(".codes_hide").css("visibility","hidden");
					var val=$('input:radio[name="limittype"]:checked').val();
					if(val == 1){
						modify("limittype",0,0)
						defaultChecked("limittype");
						form.render('radio');
					}
				}else{
					$(".codes_hide").css("visibility","visible");
				}
				modify("codes",data.value,0)
			}); 
			form.on('radio(limittype)', function(data){
				var val=$('input:radio[name="codes"]:checked').val();
				if(val != 1){
					if(data.value == 1){
						alert("请先打开手机短信验证码！");
						modify("limittype",0,0)
						defaultChecked("limittype");
						form.render('radio');
					}else{
						modify("limittype",data.value,0)
					}
				}else{
					modify("limittype",data.value,0)
				}
				
			});
			form.on('radio(isjump)', function(data){
				if(data.value==0){
					$(".isjump_1_hide").css("visibility","hidden");
					$(".isjump_0_hide").css("visibility","visible");
				}else{
					$(".isjump_0_hide").css("visibility","hidden");
					$(".isjump_1_hide").css("visibility","visible");
				}
				modify("isjump",data.value,0)
			});
			form.on('radio(prizetime)', function(data){
				modify("prizetime",data.value,0)
			});
			
			//监听selected
			form.on('select(limittime)', function(data){
				modify("limittime",data.value,0)
			  //console.log(data.elem); //得到select原始DOM对象
			  //console.log(data.othis); //得到美化后的DOM对象
			});
			
			$("#toggle_iscode").click(function(){
				if($("#iscode").val()==0){
					modify("iscode",1,0);
					defaultChecked("codes");
					modify("codes",0,0);
					form.render('radio');
				}else{
					$(".codes_hide").css("visibility","hidden");
					modify("iscode",0,0);
					unChecked("codes");
					form.render('radio');
				}
			});
			
			$("#toggle_submitlimit").click(function(){
				if($("#submitlimit").val()==0){
					modify("submitlimit",1,0);
					defaultChecked("limittype");
					modify("limittype",0,0);
					form.render('radio');
				}else{
					modify("submitlimit",0,0);
					unChecked("limittype");
					form.render('radio');
				}
			});
			
			$("#toggle_isprize").click(function(){
				if($("#isprize").val()==0){
					$(".isprize_hide").css("visibility","visible");
					modify("isprize",1,0);
					defaultChecked("prizetime");
					modify("prizetime",0,0);
					form.render('radio');
				}else{
					$(".isprize_hide").css("visibility","hidden");
					modify("isprize",0,0);
					unChecked("prizetime");
					form.render('radio');
				}
			});
			
			form.val("settingform", {
				"limittime": ${setting.limittime}		  
				})
			  
			  //监听提交
			  form.on('submit(formDemo)', function(data){
			    layer.msg(JSON.stringify(data.field));
			    return false;
		    });
	});
});
function defaultChecked(name){
	var str=document.getElementsByName(name); 
	str[0].checked = true;
}

 function unChecked(name){
	var str=document.getElementsByName(name); 
	var objarray=str.length;
	for(i=0;i<objarray;i++){
		str[i].checked = false;
	}
} 
</script>
</html>