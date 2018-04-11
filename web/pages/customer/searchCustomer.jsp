<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/customer.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/theme/uiScripts/datetimepicker.js" type="text/javascript"></script>
	<script>	

		/* $(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#searchBtn").click(function() { 	
				if(!validateSearchCustomer()){
					document.getElementById("searchCustomerFrm").action="${pageContext.request.contextPath}/customer.controller.searchCustomerDetails.task";
					document.getElementById("searchCustomerFrm").submit();
				}				
			});
			
			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#searchBtn").click();
					return false;
				}
			});
		});

		$(function() {
			$("#clearBtn").click(function(){				
				clearField("fname");
				clearField("sname");
				clearField("msisdn");
				clearField("custId")
			});
		});
	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="searchCustomerFrm" >
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="17" />		
		<div class="pagetitle">
			<h3><spring:message code="searchcustomer.header.text"></spring:message>
			</h3>			
		</div>	
		
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		<br/>												
		<div class="data_container_color1">
			<div class="data_label" id="label_fname"><spring:message code="platform.text.firstname"></spring:message>&nbsp;:</div>
			<div class="data_text">
					<input type="text" id="fname" name="fname" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		<div class="data_container_color2" id="label_sname">
			<div class="data_label"><spring:message code="platform.text.surname"></spring:message>&nbsp;:</div>
			<div class="data_text">
				<div class="" id="div_id_2">
					<input type="text" id="sname" name="sname" style="width:110px;" maxlength="50"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1" id="label_msisdn">
			<div class="data_label"><spring:message code="platform.text.msisdn"></spring:message>&nbsp;:</div>
			<div class="data_text" >
				<div class="" id="div_id_3" style="float:left;">
					<input type="text" id="msisdn" name="msisdn" maxlength="10" style="width:110px"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2" id="label_custId">
			<div class="data_label"><spring:message code="platform.text.custId"></spring:message>&nbsp;:</div>
			<div class="data_text" >
				<div class="" id="div_id_4" style="float:left;">
					<input type="text" id="custId" name="custId" maxlength="10" style="width:110px"/>
				</div>
			</div>
		</div>		
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="searchBtn"><span><spring:message code="platform.button.search"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
		</div>
		<br />
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
</body>
</html>