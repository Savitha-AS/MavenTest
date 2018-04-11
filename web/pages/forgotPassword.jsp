<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../includes/global_javascript.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/user.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/theme_gh/css/style.css" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/css/jquery.alerts.css" />
	<script>
		
	$(function() {
		$("#submitBtn").click(function() { 	
			var userUid = document.getElementById("userUid").value;
			document.getElementById("userUid").value = userUid.toUpperCase();		
		
			if(validateTextBox("userUid")){
				document.getElementById("forgotPwdFrm").action="${pageContext.request.contextPath}/pages/user.controller.checkForUser.task";
				document.getElementById("forgotPwdFrm").submit();
			} else {
				alert("Please enter Login ID");
			}			
		});	


		$("body").keypress(function(e) {
			if (e.keyCode == '13') {
				$("#submitBtn").click();
				return false;
			}
		});
		
	});
	
	$(function() {
		$("#goBackBtn").click(function() {
			window.location="${pageContext.request.contextPath}/pages/login.jsp";
		});
	});
	</script>
</head>

<body>
	<form method="post" id="forgotPwdFrm">
	<jsp:include page="../includes/global_header.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
			
		<div class="pagetitle">
			<h3><spring:message code="forgotpwd.header.text"></spring:message></h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"></spring:message></div>
		<br/>
		
		<c:choose>
			<c:when test="${status eq true}">
				<c:if test="${fn:length(message) gt 0}">
					<div id="message_div" style="text-align: center; color: Green;">
						<spring:message code="${message}"></spring:message>
					</div>
					
				</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="${fn:length(message) gt 0}">
					<div id="message_div" style="text-align: center; color: Red;">
						${message}
					</div>
				</c:if>
			</c:otherwise>		
		</c:choose>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_userUid" style="width:200px;"><spring:message code="forgotpwd.text.userUid"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text username-field">
				<input type="text" id="userUid" name="userUid" class="userUid" style="width:110px;"/>
			</div>
		</div>
		
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="submitBtn"><span><spring:message code="platform.button.submit"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="goBackBtn"><span><spring:message code="platform.button.cancel"/></span></li></ul>
		</div>
		<br/>
	</div>
	</div>
	<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</form>
<script type="text/javascript">
var status='${status}';
if(status == true || status == 'true'){
	setTimeout(function() {
		window.location="${pageContext.request.contextPath}/pages/login.jsp";
	}, 10000);
}
	
</script>
</body>
</html>