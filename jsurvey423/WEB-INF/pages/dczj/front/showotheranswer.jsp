<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@taglib prefix="h" uri="/hanweb-tags"%> --%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看其它答案</title>
<%-- <h:head pagetype="page"></h:head> --%>
<script type='text/javascript' src='../../resources/dczj/nicevalidator/jquery-1.12.3.min.js'></script>
<script type="text/javascript">
$(function(){
	var error = '${errorMessage}';
	console.log(error);
	if(error != ''){
	alert(error);
		window.close();
	}
});

function gopage(number){
	var pageNum = $("#pagenum").val();
	var quesid = $("#quesid").val();
	var quesType = $("#quesType").val();
	var pagesize=$("#pagesize").val();
	var answid=$("#answid").val();
	  if (number=="" || number==null || number < 1) {
			number = 1;
		}
		if (number>pageNum) {
			number = pageNum;
		}
		var url = "show_otheranswlist.do?quesid="+ quesid +"&pageNo="+number+"&quesType="+quesType+"&answid="+answid;
		 $.ajax({
			 url: url,
			 cache: false,
			 success: function(html){
				$("#listtd").empty(); 
				$("#listtd").append(html);
			  }
			});
}
</script>
<style>
.tb {
	background-color:#FFE4D9;
}

.tb thead td{
	background-color:#129FEA;
	height:24px;
	font-weight:bold;
	color:#EBEBEB;
}

.bg{
	background-color:expression("#EFF6FB,#FFFFFF".split(",")[rowIndex%2]);
}

.td {
	word-break:break-all;
}
.default_container {
	position: relative;
}

.default_pgContainer {
	min-height:100px;
	_height:100px;
}

.default_pgToolbar {
	height:30px;
	valign:middle;
	font-size:12px;
	line-height:29px;
	color:#999999;
	margin:10px 25px -1px 25px;
}

.default_pgToolbar td {
	font-size:12px;
	padding:0 1px 0 1px;
}

.default_pgPanel {
	float:left;
	padding:0 10px 0 10px;
}

.default_pgBtn {
	padding: 8px;
    text-align: center;
    text-decoration: none;
}

.default_pgPress {
	margin:2px 0 0 2px;
}

.default_pgPrev {
	background: url(${contextPath}/resources/front/images/next_disabled.gif) no-repeat center;
}

.default_pgPrevDisabled {
	background: #fafafa;
    color: #666666;
    cursor: auto;
	border:1px solid #cccccc;
	margin:0 10px;
}

.default_pgNext {
	background: #fafafa;
    color: #666666;
    cursor: auto;
	border:1px solid #cccccc;
	margin:0 10px;
}

.default_pgNextDisabled{
	background: url(${contextPath}/resources/front/images/next_disabled.gif) no-repeat center;
	cursor:auto;
}

.default_pgFirst {
	background: url(${contextPath}/resources/front/images/first.gif) no-repeat center;
}

.default_pgFirstDisabled {
	background: #fafafa;
    color: #666666;
    cursor: auto;
	border:1px solid #cccccc;
}

.default_pgLast {
	background: #fafafa;
    color: #666666;
    cursor: auto;
	border:1px solid #cccccc;
	margin-right:10px;
}

.default_pgCurrentPage {
	width:30px;
	height:16px;
	line-height:16px;
	text-align:center;
	border:1px solid #CCCCCC;
	background:url(${contextPath}/resources/front/images/text_bg.gif) repeat-x;
}

.default_pgSearchInfo {
	padding-left:20px;
}

</style>
</head>
<body>
 <c:if test="${recoList != null &&  fn:length(recoList) != 0}">
<div  style="padding:20px 20px;margin-left:150px;margin-right:150px;">
	<table width="100%" border="0" cellpadding="3" cellspacing="1" class="tb">
	<thead><tr>
		<td width="100">序号</td><td>答案</td><td width="130">提交时间</td>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="entity" items="${recoList}" varStatus="status">
	<tr>
	<td width="100" height="35px">${status.index+1}</td><td>${entity.answcontent}</td><td width="130"><fmt:formatDate value="${entity.submittime}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
</div>
<div  style="padding-left:400px;">
	<!-- 分页 -->
	<table border="0" cellspadding="0" cellspacing="0" class="default_pgPanel">
      <tbody>
      <tr>
      	<td valign="middle"><a class="default_pgBtn default_pgFirst default_pgFirstDisabled" title="首页" style="cursor: pointer;"onclick="gopage(1);">首页</a></td>
		<td valign="middle"><a class="default_pgBtn default_pgPrev default_pgPrevDisabled" title="上页"  style="cursor: pointer;"onclick="gopage(${pageNo-1});"><&nbsp;上一页</a></td>
		<td valign="middle">第 <input class="default_pgCurrentPage" type="text" value="${pageNo}" disabled="disabled" title="页码"> 页 / 共 <span class="default_pgTotalPage">${pageNum}</span> 页</td>
		<td valign="middle"><a class="default_pgBtn default_pgNext" title="下页" style="cursor: pointer;"onclick="gopage(${pageNo+1});">下一页>&nbsp;</a></td>
		<td valign="middle"><a class="default_pgBtn default_pgLast" title="尾页" style="cursor: pointer;"onclick="gopage(${pageNum});">尾页</a></td>
		<td valign="middle" class="default_pgSearchInfo">检索到&nbsp;${total}&nbsp;条记录</td>
      </tr>
     </tbody>
    </table>
    <input type=hidden id = "pagenum" value = "${pageNum}"/>
	<input type=hidden id = "deptid" value = "${deptid}"/>
	<input type=hidden id = "pagesize" value = "${pagesize}"/>
	<input type=hidden id = "quesid" value = "${quesid}"/>
	<input type=hidden id = "answid" value = "${answid}"/>
	<input type=hidden id = "quesType" value = "${quesType}"/>
	</div>
</c:if>
</body>
</html>