<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/report.js" type="text/javascript"></script>
	
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		$(function() {	
			$("#downloadReportLink").click(function() {
				$("#message_div").html("");
				if(!validateDlCoverageReport())
				{
					document.getElementById("generateCoverageReportForm").action="${pageContext.request.contextPath}/reports.controller.downloadCoverageReport.task";
					document.getElementById("generateCoverageReportForm").submit();
				}				
			});	
		});
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="generateCoverageReportForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="39" />	
		<div class="pagetitle">
			<h3><spring:message code="coveragereport.header.text"></spring:message> 
			</h3>			
		</div>	

		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<c:if test="${fn:length(message) gt 0}">
			<div id="message_div" style="text-align: center; color: Red;">
				<spring:message code="${message}"></spring:message>
			</div><br />
		</c:if>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_select">
				<spring:message code="revreport.text.select"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<div style="float: left; margin-top: 3px;" id="label_month">
					<spring:message code="revreport.text.month" />&nbsp;&nbsp;
				</div>
				<div style="float: left">
					<select id="reportMonth" name="reportMonth" style="width: 100px">
						<option value=""/>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="float: left; margin-top: 3px;" id="label_year">
					<spring:message code="revreport.text.year" />&nbsp;&nbsp;
				</div>
				<div style="float: left">
					<select id="reportYear" name="reportYear" style="width: 60px">
						<option value=""/>
					</select>
				</div>
			</div>
		</div>
		<br/>
		<div style="text-align: center;"> 
			<ul class="btn-wrapper"><li class="btn-inner" id="downloadReportLink"><span><spring:message code="report.button.downloadreport"/></span></li></ul>
		</div>			
		<br/>

	</div>
	</div>

	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	<script>
	
		var selectedMonth = '${month}';
		var selectedYear = '${year}';
		
		var today = new Date();
		var years = document.getElementById('reportYear');
		
		if(selectedMonth == '') {
			selectedMonth = today.getMonth()+1;
			selectedYear = today.getFullYear();
		}
		var index=1;
		// Add and select current year
		for (var i=today.getFullYear(); i>=today.getFullYear()-1; i--) {
			if(selectedYear == i) {
				years.options[index++] = new Option(i, i, true, true);
			}
			else {
				years.options[index++] = new Option(i, i);
			}
		}
		
		var monthLookUp = [ '', 'January', 'February', 'March', 'April', 'May',
				'June', 'July', 'August', 'September', 'October', 'November',
				'December' ];
		var months = document.getElementById('reportMonth');
		for ( var i = 1; i <= monthLookUp.length - 1; i++) {
			if (selectedMonth == i)
				// Add and select current month
				months.options[i] = new Option(monthLookUp[i], i, true, true);
			else
				months.options[i] = new Option(monthLookUp[i], i);
		}
	</script>
	</form>
</body>
</html>