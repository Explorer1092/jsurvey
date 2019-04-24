<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"
%><%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%><c:choose><c:when test="${state =='S' || state =='D'}">${msg}</c:when><c:when test="${state =='C'}"><c:if 
test="${isSuccess == true}">
<form name="ldapForm" id="ldapForm" method="post" action="../../ssologin.do">
<input type="hidden" name="username" value="${formBean.loginuser}"/>
<input type="hidden" name="password" value="${formBean.loginpass}"/>
<input type="hidden" name="compid" value="${formBean.appid}"/>
</form>
<script language="javascript">
document.getElementById("ldapForm").submit();
</script>
</c:if>
<c:if test="${isSuccess == false}">
<script language="javascript">
alert('登录失败，请重新尝试！');
location.href="../../";
</script></c:if></c:when></c:choose>