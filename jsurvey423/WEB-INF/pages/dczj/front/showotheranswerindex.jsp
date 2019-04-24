<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@taglib prefix="h" uri="/hanweb-tags"%> --%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看其它答案</title>
<%-- <h:head pagetype="page" ></h:head> --%>
<script type='text/javascript' src='../../resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>
<script type="text/javascript">
$(function(){
	var error = '${errorMessage}';
	if(error != ''){
		alert(error);
		window.close();
	}else{
		var quesid = $("#quesid").val();
		var quesType = $("#quesType").val();
		var answid = $("#answid").val();
		var url = "show_otheranswlist.do?quesid="+ quesid +"&pageNo=0&quesType="+quesType+"&answid="+answid;
		$.ajax({
			url: url,
			cache: false,
			success: function(html){
			    $("#listtd").empty(); 
				$("#listtd").append(html);
			}
		});
	}
});
</script>

</head>
<body>
 <input type=hidden id = "quesid" value = "${quesid}"/>
 <input type=hidden id = "answid" value = "${answid}"/>
 <input type=hidden id = "quesType" value = "${quesType}"/>
 <div id="listtd"></div>
</body>
</html>