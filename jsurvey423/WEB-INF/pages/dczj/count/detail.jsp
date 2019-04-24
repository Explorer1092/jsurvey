<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="h" uri="/hanweb-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<h:head pagetype="page"  calendar="true"></h:head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>调查征集系统</title>
<h:import type="js" path="/ui/script/ui.js"></h:import>
<link type="text/css" rel="stylesheet" href="${contextPath}/resources/dczj/layui/css/layui.css">
<script src="${contextPath}/resources/dczj/layui/layui.js"></script>
<style type="text/css">
	html{
		height: 99%;
		width: 100%;
	}
	body {
		height: 100%;
		width: 100%;
	}
	.label {
	    color: #333333; 
	    font-size: 14px; 
	}
</style>
</head>
<body>
<div style="background-color: #f0f0f0;height: 40px;width: 100%;">
	<div style="font-weight: bold;padding-top: 10px;padding-left: 15px;">参与明细</div>
</div>
<div style="height: 40px;width: 100%;">
	<table style="width: 98%;margin-left: 15px;margin-top: 7px;">
		<tr style="width: 100%;">
			<td style="margin-left: 10px;" width="25%"><span style="font-weight: bold;">COOKIED:</span>&nbsp;${totalReco.unid}</td>
			<td width="25%"><span style="font-weight: bold;">提交来源:</span>&nbsp;
				<c:choose>
					<c:when test="${totalReco.type==1}">
						pc端
					</c:when>
					<c:when test="${totalReco.type==2}">
						手机端
					</c:when>
					<c:otherwise>
						微信
					</c:otherwise>
				</c:choose>
			</td>
			<td width="25%"><span style="font-weight: bold;">参与人IP:</span>&nbsp;${totalReco.ip}</td>
			<td width="25%"><span style="font-weight: bold;">提交时间:</span>&nbsp;<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" 
			value="${totalReco.createdate}" /></td>
		</tr>
	</table>
	<hr>
</div>
<div style="width: 100%;">
	<table border="0" class="table" style="text-align: left;margin-left: 10px;">
	<c:forEach var="quesInfo" items="${quesInfos}" varStatus="num">
		<c:choose>
			<c:when test="${quesInfo.type==0}">
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<c:forEach var="answInfo" items="${quesInfo.answMoreList}">
					<tr>
						<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
							<label><input name="${answInfo.iid}" type="radio" value="${answInfo.iid}" data-value="${quesInfo.answvalue}">&nbsp;${answInfo.answname}</label>
						</td>
					</tr>
				</c:forEach>	
			</c:when>
			<c:when test="${quesInfo.type==1}">
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<c:forEach var="answInfo" items="${quesInfo.answMoreList}">
					<tr>
						<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
							<label><input name="${answInfo.iid}" type="checkbox" value="${answInfo.iid}" data-value="${quesInfo.answvalue}">&nbsp;${answInfo.answname}</label>
						</td>
					</tr>
				</c:forEach>	
			</c:when>
			<c:when test="${quesInfo.type==2}">
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<tr>
					<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
						<textarea class="input-textarea"  style="width: 715px" placeholder="">${quesInfo.answvalue}</textarea>
					</td>
				</tr>
			</c:when>
			<c:when test="${quesInfo.type==3}">
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<tr>
					<td class="label" style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">${quesInfo.content}</td>
				</tr>
			</c:when>
			<c:when test="${quesInfo.type==4}">
				<tr>
					<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
						----------------此处分页----------------
					</td>
				</tr>
			</c:when>
			<c:when test="${quesInfo.type==5}">
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<tr>
					<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
						${quesInfo.answvalue}
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="label">${num.count}.${quesInfo.quesname}</td>
				</tr>
				<c:forEach var="answInfo" items="${quesInfo.answMoreList}">
					<tr>
						<td style="padding-left: 15px;padding-right: 0px;padding-top: 0px;padding-bottom: 0px;">
							<input type="text" maxlength="33" class="input-text" value="${quesInfo.answvalue}"/>
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	</table>
</div>
</body>
</html>