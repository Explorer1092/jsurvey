<%@page import="com.hanweb.common.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通用开发平台3.0</title>
<h:head cookie="true" dialog="true" message="true" iconfont="true" tip="true" scroll="true"></h:head>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<h:import type="css" path="/ui/widgets/poshytip/tip-twitter/tip-twitter.css"></h:import>
<style>
html,body {
	height: 100%;
	overflow: hidden;
}

body {
	margin: 0;
	padding: 0;
}

#page-wrap {
	position: absolute;
	z-index: 10;
	right: 0;
}

#page {
	width: 100%;
	height: 100%;
}

#menu-wrap {
	display: none;
	position: absolute;
	z-index: 10;
	border-right: 3px solid #EFEFEF;
}

#menu {
	width: 240px;
	height: 100%;
}

.dialog-content {
	overflow: hidden;
}

#sso {
	width: 0;
	height : 100%;
	background-color: #20222A;
	/* 	background-image: linear-gradient(to right, rgb(85, 164, 242), */
	/* 		rgb(182, 212, 56) 50%, rgb(255, 194, 14)); */
	/* 	background-repeat: no-repeat; */
	/* 	background-color: rgb(197, 208, 48); */
	overflow: hidden;
	float: left;
	text-align: center;
}
#sso>i.ssohead{
	color: #FFF;
	height: 50px;
	display: block;
	line-height: 50px;
}
#sso>div{
	height: 56px;
	line-height: 56px;
}

#sso.running {
	width: 53px;
}

#sso i {
	height: 30px;
	line-height: 30px;
}

#sso a {
	color: #FFF;
	text-decoration: none;
	padding: 0 7px;
	cursor: pointer;
	border-left: 3px solid #20222A;
	border-left-width:5px\0;
	vertical-align: middle;
	display: block;
}

#sso a.app_current {
    border-left: 3px solid #38A4C9;
    background-color: #080707;
    border-left-width:5px\0;
}

#sso a img {
    width: 15px;
    height: 15px;
    vertical-align: middle;
    margin-left: -3px;
}

#sso a:hover,#sso a.app_current:hover,#sso a.active {
	/* background-color: #38A4C9; */
	border-left: 3px solid #38A4C9;
	border-left-width:5px\0;
}
.main-wrap{
	margin-left: 53px;
	height: 100%;
}
#header {
	width: 100%;
	/* 	background-color: #F9F9F9; */
	/* 	background: url(${contextPath}/ui/images/nav-tail.png) bottom repeat-x; */
	background: url(${contextPath}/ui/images/nav-bg.png) bottom repeat-x;
	box-shadow: 0 1px 5px #888;
	z-index: 20;
	_border-bottom: 1px solid #CCC;
	/*border-top:1px solid #20222A;*/
	position: relative;
}

#header-container {
	height: 50px;
	min-width: 960px;
	margin: 0 20px;
}

#logo {
	float: left;
	width: 280px;
	height: 50px;
	line-height: 50px;
	font-size: 36px;
	color: #FFF;
}

.tip-twitter {
    padding: 11px 13px;
}

.nav-menu {
	float: right;
	height: 50px;
}

.nav-menu li {
	float: left;
	font-size: 15px;
	width: 110px;
	height: 100%;
	text-align: center;
	line-height: 50px;
	position: relative;
	z-index: 100;
}

.nav-menu div {
	width: 100%;
	height: 100%;
	cursor: default;
	overflow: hidden;
}

.nav-title {
	color: #333;
}

.nav-menu .hover {
 	background-color: #EFEFEF;
 	-webkit-transition: all 0.3s ease-out 0s;
	-moz-transition: all 0.3s ease-out 0s;
	transition: all 0.3s ease-out 0s;
}

.nav-menu .active {
	background-color: #3498DB;
}

.nav-menu .active .nav-title {
	color: #FFF;
}

.nav-menu .separator {
	border-left: 1px solid #CCC;
	border-right: 1px solid #FFF;
	height: 50px;
	width: 0;
}

.nav-account-btn {
	line-height: 50px;
	font-size: 20px;
}

.nav-submenu {
	display: none;
	position: absolute;
	top: 50px;
	left: 0;
	background-color: #F9F9F9;
	border: 1px solid #CCC;
	border-bottom-left-radius: 5px;
	border-bottom-right-radius: 5px;
	box-shadow: 1px 2px 1px #EFEFEF;
}

.nav-submenu li {
	font-size: 15px;
	width: auto;
	min-width: 108px;
	_width: 108px;
	height: 36px;
	line-height: 36px;
	float: none;
	text-align: left;
	-webkit-transition: all 0.3s ease-out 0s;
	-moz-transition: all 0.3s ease-out 0s;
	transition: all 0.3s ease-out 0s;
}

.nav-submenu span {
	padding: 0 10px;
}

/*日历控件外框样式*/
.WdateBox {
	background-color:#FFF;
	border:1px solid #BBB;
	border-radius: 5px;
	box-shadow: 1px 1px 1px 1px #EEE;
	margin-top: 1px;
}
</style>
<script>
	var cookiePath = '${contextPath}/';

	var sso = ${sso};

	var height;
	var channel = $.cookie('channel');
	var menuUrl = $.cookie('menuUrl');
	var pageUrl = $.cookie('pageUrl');
	
	if(!pageUrl && !menuUrl){
		pageUrl = '${pageUrl}';
		menuUrl = '${menuUrl}';
	}
	
	$(function() {
		channel = channel ? channel : $('li[channel]').attr('channel');
		$('body').on('mousedown', '*', function() {
			if (typeof($dp) != 'undefined') {
				try {
					$dp.hide();//修复弹出框内的日历控件有时候关闭窗口不消失
				} catch(e) {
					
				}
			}
		});
		
		$('.nav-menu > li').hover(function() {
			$(this).not('.active').addClass('hover');
			$(this).children('.nav-submenu').slideDown(50);
		}, function() {
			$(this).not('.active').removeClass('hover');
			$(this).children('.nav-submenu').hide();
		});

		$('.nav-submenu > li').hover(function() {
			$(this).css('background-color', '#EFEFEF');
		}, function() {
			$(this).css('background-color', '');
		});

		$('li[channel=' + channel + ']').addClass('active');
		$('#page').attr('src', pageUrl);
		$('#menu').attr('src', menuUrl);

		height = $(window).height() - $('#header').outerHeight();

		$('#page-wrap').css('top', $('#header').outerHeight());

		if (sso) {
			$('#sso').addClass('running').niceScroll({cursorcolor:'#808080',cursorwidth:'3px',cursorborder:'0px',cursorborderradius:'2px'});
			$('#main_wrap').addClass('main-wrap');
			ssoCheck();
		}
		
		initLayout();

		$(window).resize(function() {
			height = $(window).height() - $('#header').outerHeight();
			initLayout();
		});

		$('.nav-link[page]').click(function() {
			$('.active').removeClass('active');
			$('.hover').removeClass('hover');
			var currentNav = $(this).closest('li[channel]');
			currentNav.addClass('active');

			channel = currentNav.attr('channel');
			pageUrl = $(this).attr('page');
			menuUrl = $(this).attr('menu');
			menuUrl = menuUrl ? menuUrl : null;

			initLayout();

			$('#page').attr('src', pageUrl);
			$('#menu').attr('src', menuUrl);

			$.cookie('channel', channel, {path: cookiePath});
			$.cookie('pageUrl', pageUrl, {path: cookiePath});
			$.cookie('menuUrl', menuUrl, {path: cookiePath});
		});
	});

	/**
	 * 设置布局
	 */
	function initLayout() {
		// sso 菜单宽度
		var ssoWidth = 0;
		if(sso){
			ssoWidth = 53;
		}
		var rightWidth = $(window).width() - ssoWidth;
		if (menuUrl) {
			$('#page-wrap').width(rightWidth - 240).height(height);
			$('#menu-wrap').css({
				top : $('#header').outerHeight(),
				left : 'auto'
			}).show().height(height);
		} else {
			$('#page-wrap').width(rightWidth).height(height);
			$('#menu-wrap').hide();
		}
	}
	/**
	 * 获取sso列表
	 */
	function ssoCheck() {
		var url = "${contextPath}/manager/sso/apps_show.do";
		$.ajax({
			type : "POST",
			url : url,
			success : function(msg) {
				$('#sso').append(msg);
				$('#sso div a').poshytip({
					className: 'tip-twitter',
					showOn: 'hover',
					alignTo: 'target',
					alignX: 'inner-left',
					alignY: 'center',
					offsetX: 60,
					showTimeout: -1,
					hideTimeout: 0
				});
			}
		});
	}
	
	/**
	 * 保持用户在线，20分钟一次
	 */
	setInterval(function(){
		var url = '${contextPath}/manager/user/keep_online.do';
		ajaxSubmit(url, {
			type:'html',
			error:function(a,b,msg){
			}
		});
	}, 1200000);
	
	function ssoLogin(uuid){
		location.href = '${contextPath}/manager/sso/sso_login.do?uuid=' + uuid + '&t='+new Date().getTime()
	}
</script>
</head>
<body>
	<div id="sso">
		<i class="iconfont ssohead">&#xf5031;</i>
	</div>
	<div id="main_wrap">
		<div id="header">
			<div id="header-container">
				<div id="logo">
					<img src="${contextPath}/resources/complat/images/logo.png" style="height: 34px; margin-top: 8px;" />
				</div>
				${topMenuHtml}
			</div>
		</div>
		<div id="page-wrap">
			<iframe name="page" id="page" frameborder="0" style="height: 100%;"></iframe>
		</div>
		<div id="menu-wrap">
			<iframe name="menu" id="menu" frameborder="0" allowTranspancy="true"></iframe>
		</div>
	</div>
	<div id="dialog-content"></div>
</body>
</html>