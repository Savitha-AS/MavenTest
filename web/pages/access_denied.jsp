<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
			<h3>Oops !! 
			</h3>			
		</div>	
		<div style="text-align:left; margin-left: 20px;">
			<br/>
			<img src="${pageContext.request.contextPath}/theme/images/lock.png" style="text-align: bottom;" height="40" width="40"/>
			&nbsp;&nbsp;
			<span style="font-size: 25px">Access to the requested page is</span>
			<div style="text-align: center;"><img src="${pageContext.request.contextPath}/theme/images/access-denied.jpg"></img></div>
		</div>
		<br/>									
	</div>	
	</div>
<jsp:include page="../includes/global_footer.jsp"></jsp:include>
</body>
</html>