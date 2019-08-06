
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no">
<meta name="MobileOptimized" content="320">
<meta name="apple-touch-fullscreen" content="YES">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache" content="no-cache" />
<title>查看成绩单</title>
<%-- <h:head pagetype="page"></h:head> --%>
<script type='text/javascript' src='../../resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>
<style type="text/css">
body{
   background-color: #f0f0f0;
   height: 100%;
}

#main {
	margin:auto;
	width:100%;
/* 	padding:2%; */
	border:0px solid #3185CE;
}
#title {
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    height: 30px;
    border-bottom: 1px dashed #ccc;
}

#quesansw {
    padding-top: 10px;
    border-bottom: 1px dashed #ccc;
}

.div_content{
    width: 100%;
    border-bottom: 1px dashed #ccc;
}

 
 .cjd {
  display: inline-block;
  width:20%;
  /* height:60%; */
  font-size: 12px;
  position: relative;
  vertical-align: middle;
}

.cjd:before {
  content: "";
  display: inline-block;
  padding-bottom: 100%;
  width: .1px;
  vertical-align: middle;
}
.cjd img {
  margin-right:3px;
  width:88%;
  display: inline-block;
  vertical-align: middle;
  position: relative;
}
.cjd img:before{
  content: "";
  display: inline-block;
  padding-bottom: 100%;
  width: .1px;
  vertical-align: middle;
}
</style>
</head>
<body>
<div id = "main">
<div id = "title">${titlename }</div>
<div id = "quesansw">
<span style="font-size: 15px;font-weight: bold;">成绩单</span>
<div class="cjd" style="width: 100%;height: 100px;padding-top: 10px;">
<div class="cjd"  style="float: left;border:1px solid #ccc;text-align: center;">
     <img src="../../resources/front/images/1.png" style=""> 
</div>
<div class="cjd" style="float: left;border:1px solid #ccc;border-left-width: 0px;text-align: center;">${userrightscore }/${quesallscore}</div>
<div class="cjd" style="float: left;margin-left: 40px;border:1px solid #ccc;text-align: center;">
     <img src="../../resources/front/images/2.png" style="">
</div>
<div class="cjd"  style="float: left;border:1px solid #ccc;border-left-width: 0px;text-align: center;">${userrightnum}/${quesallnum}</div>
</div>
<div style="width: 100%;height: 30px;line-height: 30px;">
<span style="font-size: 15px;font-weight: bold;">答案解析</span>
</div>
</div>
<div style="width: 100%;padding-top: 5px;font-size: 13px;color: #333">
${testContent }
</div>

</div>
</body>
</html>