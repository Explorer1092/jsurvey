<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="/WEB-INF/tag/hanweb-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统参数</title>
<h:head pagetype="page" validity="true" toggle="true"></h:head>
<!--使用${contextPath}可以获得应用路径，并无/结尾-->
<script>
	/**
	 * 表单验证
	 */
	$(function() {
		var enabledistributed = '${setting.enabledistributed}';
		if(enabledistributed == 0){
			$('#distributed1').css("display","none");
		}else{
			$('#distributed1').css("display","table-row");
		}
		$('#closesystem').toggle({
			onValue : 1,
			offValue : 0});
		$('#enabledistributed').toggle({
			toggleAfter:function(){
				var distribute = $('#enabledistributed').val();
				if(distribute == 0){
					$('#distributed1').css("display","none");
				}else{
					$('#distributed1').css("display","table-row");
				}
			}
		});
		
		$('#oprform').validity(function() {
			$('#loginerror').require('登录错误次数必须填写').match('intege1','登录错误次数必须为正整数').range(1,100,'登录错误次数可输入范围为1-100');
			$('#bantimes').require('登录封停时间必须填写').match('intege1','登录封停时间必须为正整数').range(1,100,'登录封停时间可输入范围为1-100');
			$('#filetmp').require('文件暂存目录必须填写');
			$('#imagedir').require('高级编辑器图片目录必须填写');
			$('#attachmentdir').require('高级编辑器附件目录必须填写');
		},{
			type : 'iframe',
		});

		$('#enableVerifyCode').toggle({
			onValue : true,
			offValue : false
		});
		
		$('#enableoss').toggle();
	});
	
</script>
</head>
<body>
	<div id="page-title">
		系统参数 / 
		<span id="page-location">参数设置</span>
	</div>
	<div id="page-content">
		<!--表单主体-->
		<form action="${url}" method="post" id="oprform" name="oprform">
			<!--隐藏变量区-->
			<table border="0" align="center" class="table">
			    <tr>
					<td align="right" class="label">IP参数名称</td>
					<td>
						<input type="text" id="ipHead" name="ipHead" maxlength="33" class="input-text" value="${setting.ipHead }" />
						<div style="color: #cc0066;text-align: left;">
							配置代理服务器中设置的IP参数名称，一般情况下Nginx为X-Real-IP，但要先去Nginx配置
						</div>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">密码强度等级</td>
					<td>
						<label style="margin-right:20px;"><input type="radio" name="checkLevel" value="0" data-value="${setting.checkLevel}"/>弱</label>
						<label style="margin-right:20px;"><input type="radio" name="checkLevel" value="1" data-value="${setting.checkLevel}"/>中</label>
						<label><input type="radio" name="checkLevel" value="2" data-value="${setting.checkLevel}"/>强</label>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">登录验证码</td>
					<td>
						<input type="hidden" id="enableVerifyCode" name="enableVerifyCode" value="${setting.enableVerifyCode}" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">登录错误次数</td>
					<td>
						<input type="text" id="loginerror" name="loginError" maxlength="2" class="input-text" style="width:40px;" value="${setting.loginError }" />
					</td>
				</tr>
				<tr>
					<td align="right" class="label">登录封停时间（分钟）</td>
					<td>
						<input type="text" id="bantimes" name="banTimes" maxlength="2" class="input-text" style="width:40px;" value="${setting.banTimes }" />
					</td>
				</tr>
				<tr style="display:none;">
					<td align="right" class="label">session失效时间（分钟）</td>
					<td>
						<input type="text" id="sessiontime" name="sessionTime" maxlength="33" class="input-text" value="${setting.sessionTime }" />
					</td>
				</tr>
				<tr >
					<td align="right" class="label">文件暂存目录</td>
					<td>
						<textarea id="filetmp" name="fileTmp" maxlength="200" class="input-textarea" style="width:500px;height:50px;">${setting.fileTmp}</textarea>
					</td>
				</tr>
				<tr >
					<td align="right" class="label" >高级编辑器图片目录</td>
					<td>
						<textarea id="imagedir" name="imageDir" maxlength="200" class="input-textarea" style="width:500px;height:50px;">${setting.imageDir}</textarea>
					</td>
				</tr>
				<tr > 
					<td align="right" class="label" >高级编辑器附件目录</td>
					<td>
						<textarea id="attachmentdir" name="attachmentDir" maxlength="200" class="input-textarea" style="width:500px;height:50px;">${setting.attachmentDir}</textarea>
					</td>
				</tr>
<!-- 				<tr style="display:none;"> -->
<!-- 					<td align="right" class="label">是否运用memcache</td> -->
<!-- 					<td> -->
<%-- 						<input type="hidden" id="enablememcache" name="enablememcache" value="${setting.enablememcache}"  /> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr id="memcachetr" style="display:black;"> -->
<!-- 					<td align="right" class="label">memcache缓存IP</td> -->
<!-- 					<td> -->
<%-- 						<textarea id="memcacheIp" name="memcacheIp" maxlength="200" class="input-textarea" style="width:500px;height:50px;">${setting.memcacheIp}</textarea> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr id="memcachetr1" style="display:black;"> -->
<!-- 					<td align="right" class="label">memcache端口号</td> -->
<!-- 					<td> -->
<%-- 						<textarea id="memcacheport" name="memcacheport" maxlength="200" class="input-textarea" style="width:500px;height:50px;">${setting.memcacheport}</textarea> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td align="right" class="label">样式标签设置</td> -->
<!-- 					<td> -->
<%-- 						<textarea id="styletag" name="styletag" maxlength="500" class="input-textarea" style="width:500px;height:50px;">${setting.styletag}</textarea> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td align="right" class="label">短信接口url配置</td>
					<td>
						<textarea id="messageurl" name="messageurl" maxlength="500" class="input-textarea" style="width:500px;height:50px;">${setting.messageurl}</textarea>
						<br><span style="color:#308EF2;font-weight:bold;"> url参考示例：http://12.34.56.78/demo/message/send</span>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">短信接口参数配置</td>
					<td>
						<textarea id="messageparam" name="messageparam" maxlength="500" class="input-textarea" style="width:500px;height:50px;">${setting.messageparam}</textarea>
						<br><span style="color:#308EF2;font-weight:bold;"> param参考示例：method=send&message=:message&time=:time&mobile=:mobile<br><p style="color:red">注意  =：前的为参数名，可改变，=：后的为所传参数值 不可改变其名称,（包括：短信内容，手机号码，时间）</p></span>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">是否开启云存储</td>
					<td>
						<input type="hidden" id="enableoss" name="enableoss" value="${setting.enableoss}"  />
						<span style="color:#308EF2;font-weight:bold;">(如开启请同步修改配置文件并重启应用)</span>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">是否运用分布式</td>
					<td>
						<input type="hidden" id="enabledistributed" name="enabledistributed" value="${setting.enabledistributed}"  />
						<span style="color:#308EF2;font-weight:bold;"> (如开启请同步开启oss和mq，并做好相关配置)</span>
					</td>
				</tr>
				<tr id="distributed1" style="display:block;">
					<td align="right" class="label">所属服务器类型</td>
					<td>
						<input type="text" id="servertype" name="servertype" maxlength="2" class="input-text" style="width:40px;" readonly="readonly" value="${setting.servertype }" />
					    <span style="color:#308EF2;font-weight:bold;"> (本输入框只读，请自行在配置文件中输入数字编号)</span>
					</td>
				</tr>
				<tr>
					<td align="right" class="label">是否关闭系统</td>
					<td>
						<input type="hidden" id="closesystem" name="closesystem" value="${setting.closesystem}"  />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="submit" class="btn btn-primary" value="保存" />&nbsp;
						<input type="reset" class="btn" value="重置" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>