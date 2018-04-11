<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setHeader("Cache-Control", "no-store");
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<jsp:include page="../../includes/calendar.jsp"></jsp:include>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/report.js" type="text/javascript"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/appScripts/utils.js"></script>
	<script>		
		
	$(function() {

		$("#downloadReportLink").click(function() {
			if(!validateDlWeeklyTigoReport())
			{
				document.getElementById("generateTigoWeeklyReportForm").action="${pageContext.request.contextPath}/weeklyReport.controller.downloadWeeklyReport.task";
				document.getElementById("generateTigoWeeklyReportForm").submit();
				
				clearField("fromDate");
				clearField("toDate");
				clearField("refDate");
				resetRadioButtons("registrationType");
				clearField("targetEndDateReg");
			}				
		});
	});
	
	</script>  
</head>
<body onload="keepSubMenuSelected()">
	
	<form method="post" id="generateTigoWeeklyReportForm"> 
	
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->	
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="36" />
			<div class="pagetitle">
				<h3><spring:message code="weeklyReport.header.text"></spring:message>
				</h3>			
			</div>	
			<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
			<br/>
			<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
			<div class="data_container_color1" id="dateRangeSelectDiv">
				<div class="data_label" id="label_dateRangeSelect" style="width: 200px;"><spring:message code="weeklyReport.text.dateselect"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
				<div class="data_text">
					<div style="float: left; margin-top: 3px;" id="label_fromDate">
						<spring:message code="platform.text.from" />&nbsp;&nbsp;
					</div>
					<div style="float: left">
						<input type="text" id="fromDate" name="fromDate" readonly="readonly" 
							style="width: 70px" value="" />
						<div class="calendar-icon" onclick="javascript:clearField('fromDate')" id="calBut1" title="<spring:message code='tooltip.platform.calendar'/>"></div>
						<div class="clear-icon" onclick="javascript:clearField('fromDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
						<script>
						   Calendar.setup({
						        inputField : "fromDate",
						        trigger    : "calBut1",
						        onSelect   : function() { 
						        				this.hide();
						        			 },
						        showTime   : false,
						        dateFormat : "%d/%m/%Y"	
						    });
						</script>
					</div>
					<div style="float: left; margin-top: 3px;" id="label_toDate">
						&nbsp;&nbsp;<spring:message code="platform.text.to" />&nbsp;&nbsp;
					</div>
					<div>
						<input type="text" id="toDate" name="toDate" readonly="readonly" 
							style="width: 70px" value="" />
						<div class="calendar-icon" onclick="javascript:clearField('toDate')" id="calBut2" title="<spring:message code='tooltip.platform.calendar'/>"></div>
						<div class="clear-icon" onclick="javascript:clearField('toDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
						<script>
						   Calendar.setup({
						        inputField : "toDate",
						        trigger    : "calBut2",
						        onSelect   : function() { 
						        				this.hide();
						        			 },
						        showTime   : false,
						        dateFormat : "%d/%m/%Y"	
						    });
						</script>
					</div>
				</div>
			</div>
			
			<div id="dateRefDiv">
				<div class="data_container_color2">
					<div class="data_label" id="label_refDate" style="width: 140px;"><spring:message code="weeklyReport.text.refDate" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						<div id="div_id_2" style="float:right; vertical-align: middle;">
							<%-- Tooltip : Currently supports for type csv, message. --%>
							<c:set var="propVal"><spring:message code='weeklyReport.text.refDateHelp' /></c:set>
							<jsp:include page="../../includes/tooltip.jsp">
								<jsp:param name="tooltipId" value="refDateTooltip"/>
								<jsp:param name="tooltipTitle" value="${propVal}"/>
								<jsp:param name="tooltipValue" value="" />
								<jsp:param name="tooltipType" value="message"/>
							</jsp:include>&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
					<div class="data_label" id="label_refDate" style="width: 95px;">&nbsp;</div>
					<div class="data_text">
						<div style="float:left vertical-align: middle;">&nbsp;&nbsp;&nbsp;
							<input type="text" id="refDate" name="refDate" readonly="readonly"  style="width:70px"
								value=""/>
							<div class="calendar-icon" onclick="javascript:clearField('refDate')" id="calBut3" title="<spring:message code='tooltip.platform.calendar'/>"></div>
							<div class="clear-icon" onclick="javascript:clearField('refDate')" title="<spring:message code='tooltip.platform.clear'/>"></div>				
							<script>
							   Calendar.setup({
							        inputField : "refDate",
							        trigger    : "calBut3",
							        onSelect   : function() { 
							        				this.hide();
							        			 },
							        showTime   : false,
							        dateFormat : "%d/%m/%Y"	
							    });
							</script>
						</div>
					</div>
				</div>
			</div>
			
			<div class="data_container_color1" id="registrationTypeDiv" >
				<div class="data_label" id="label_registrationType" style="width: 235px;"><spring:message code="weeklyReport.text.regType" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
				<div class="data_text">
					<div class="" id="div_id_3" style="float: left; vertical-align: middle;">
						<input type="radio" id="registrationType" name="registrationType" value="0" /><spring:message code="weeklyReport.text.free" />&nbsp;&nbsp;&nbsp;
						<input type="radio" id="registrationType" name="registrationType" value="1" /><spring:message code="weeklyReport.text.offer" />&nbsp;&nbsp;&nbsp;
						<div id="div_id_2" style="float:right; vertical-align: middle;">
							<%-- Tooltip : Currently supports for type csv, message. --%>
							<c:set var="propVal"><spring:message code='tooltip.deregcustomer.active.offers'/></c:set>
							<jsp:include page="../../includes/tooltip.jsp">
								<jsp:param name="tooltipId" value="offerTooltip"/>
								<jsp:param name="tooltipTitle" value="${propVal}" />
								<jsp:param name="tooltipValue" value="${productsList}"/>
								<jsp:param name="tooltipType" value="csv"/>
							</jsp:include>
						</div>
					</div>
				</div>
			</div>
			
			<div class="data_container_color2">
				<div class="data_label" id="label_targetEndDateReg" style="width: 235px;"><spring:message code="weeklyReport.text.targetEndDateReg" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
				<div class="data_text">
					<input type="text" id="targetEndDateReg" name="targetEndDateReg" style="width: 70px" maxlength="10" />
				</div>
			</div>
			<br /><br />
			
			<div style="text-align:center;">
				<ul class="btn-wrapper"><li class="btn-inner" id="downloadReportLink"><span><spring:message code="report.button.downloadreport" /></span></li></ul>
			</div>
			<br/>
						
		</div>
	</div>
							
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>

	</form>
</body>
</html>