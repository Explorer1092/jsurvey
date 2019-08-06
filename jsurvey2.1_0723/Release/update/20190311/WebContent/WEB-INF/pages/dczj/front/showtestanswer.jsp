<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看成绩单</title>
<%-- <h:head pagetype="page"></h:head> --%>
<script type='text/javascript' src='../../resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>
<style type="text/css">
body{
   background-color: #f0f0f0;
   height: 100%
}

#main {
    margin: auto;
    width: 1000px;
    height: 100%;
    padding: 50px;
    background-color: #FFFFFF;
    border: 0px solid #AAC6EB;
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
</style>
</head>
<body>
<div id = "main">
<div id = "title">${titlename }</div>
<div id = "quesansw">
<span style="font-size: 15px;font-weight: bold;">成绩单</span>
<div style="width: 100%;height: 120px;padding-top: 10px;">
<div style="width: 100px;height: 100px;float: left;border:1px solid #ccc;"><img src="../../resources/front/images/1.png" style="margin-left: 9px;margin-top: 5px;"></div>
<div style="width: 100px;height: 100px;float: left;border:1px solid #ccc;border-left-width: 0px;text-align: center;line-height: 100px;">${userrightscore }/${quesallscore}</div>
<div style="width: 100px;height: 100px;float: left;margin-left: 50px;border:1px solid #ccc;"><img src="../../resources/front/images/2.png" style="margin-left: 9px;margin-top: 5px;"></div>
<div style="width: 100px;height: 100px;float: left;border:1px solid #ccc;border-left-width: 0px;text-align: center;line-height: 100px;">${userrightnum}/${quesallnum}</div>
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