<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
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
	<jsp:include page="../includes/global_css.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">		
		<div class="pagetitle">
			<h3>Failure</h3>			
		</div>	
		<div class="error-div-header" style="height: 10px">&nbsp;</div>
		<div class="error-div-body">
			<br />
			<div style="float: left">
				<img src="${pageContext.request.contextPath}/theme/images/global-error.png" height="50" width="50"/>&nbsp;&nbsp;
			</div>
			<div style="margin-top: 20px; margin-left: 15px;">
				<c:if test="${fn:length(errorModelMap.errorMessage) gt 0}">
					<c:out value="${errorModelMap.errorMessage}"/>
				</c:if>
				<br/>				
				<c:if test="${fn:length(errorModelMap.ticketNumber) gt 0}">
					Please contact the Administrator and provide them with the following 
					ticket number : <c:out value="${errorModelMap.ticketNumber}"/>.
				</c:if>
				<br/> <br/>
				<c:if test="${fn:length(errorModelMap.goBackLink) gt 0}">
					&nbsp;&nbsp;Click <a href="<c:url value='${errorModelMap.goBackLink}'/>">here</a>&nbsp;to go back and retry.
				</c:if>
			</div>			
		</div>
		<div class="error-div-footer">&nbsp;</div>
		<br/>									
	</div>	
	</div>
<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</body>
</html>