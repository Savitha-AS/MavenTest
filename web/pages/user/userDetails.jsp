<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="com.mip.application.view.UserVO"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script>	
		
		/*	$(function() {
				$( "button, input:button, input:submit").button();
			});  */
			
			$(function() {
				$("#backBtn").click(function() { 
					document.getElementById("userDetailsFrm").action="${pageContext.request.contextPath}/user.controller.listUsers.task";
					document.getElementById("userDetailsFrm").submit();
				});
			});	
	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="userDetailsFrm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="13" />		
		<div class="pagetitle">
			<h3><spring:message code="userdetails.header.view"/>
			</h3>			
		</div>	

		<br/>
		<div class="data_container_color1">
			<div class="data_label" id="label_user_uid"><spring:message code="platform.text.userid"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:228px;">
				<c:out value="${userVO.userUid}"/>
			</div>
		</div>

		<div class="data_container_color2">
			<div class="data_label" id="label_fname"><spring:message code="platform.text.firstname"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<div id="div_id_1">
					<c:out value="${userVO.fname}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_sname"><spring:message code="platform.text.surname"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<div id="div_id_2">
					<c:out value="${userVO.sname}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_msisdn"><spring:message code="platform.text.msisdn"/>&nbsp;:&nbsp;</div>
			<div class="data_label"  style="width:328px;">
				<div id="div_id_3">
					<c:out value="${userVO.msisdn}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_dob"><spring:message code="platform.text.dob"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<c:out value="${userVO.dob}"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_age"><spring:message code="platform.text.age"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<c:out value="${userVO.age}"/>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_gender"><spring:message code="platform.text.gender"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<div id="div_id_7">
					<c:out value="${userVO.gender}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_role"><spring:message code="platform.text.role"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<div id="div_id_8">
					<c:out value="${userVO.role}"/>
				</div>
			</div>
		</div>	
		
		<div class="data_container_color1">
			<div class="data_label" id="label_branch"><spring:message code="platform.text.branch"/>&nbsp;:&nbsp;</div>
			<div class="data_label" style="width:328px;">
				<div id="div_id_8">
					<c:out value="${userVO.branch}"/>
				</div>
			</div>
		</div>								
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="backBtn"><span><spring:message code="platform.button.back"/></span></li></ul>
		</div>
		<br/>
	</div>
	</div>
<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>

