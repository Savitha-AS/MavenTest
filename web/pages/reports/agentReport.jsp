<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
	
	
	
	<script>
	
			$(function() {
				
				 $('input[name="reportType"]').click(function() {					
					
					if($(this).val() == 3){	

						$("#quickSelectFilterDivForDeducted").show();
						$("#rolesDiv").show();
						
						$("#reportFilterDiv").hide();
						$("#dateFilterDiv").hide();
						$("#quickSelectFilterDiv").hide();
						
					}else{												
						$("#quickSelectFilterDivForDeducted").hide();						
						$("#reportFilterDiv").show();
						
					}
					$("#rolesDiv").show();
						 
					
				}); 
				
				$('input[name="filterOption"]').click(function() {
					
					if($(this).val() == 0) {
						$("#dateFilterDiv").show('slow');
						$("#quickSelectFilterDiv").hide('slow');
						$('input[name="quickSelectOption"]').removeAttr('checked');
					}
					else {
						$("#quickSelectFilterDiv").show('slow');
						$("#dateFilterDiv").hide('slow');
						clearField('fromDate');
						clearField('toDate');
					}
					
					$("#rolesDiv").show('slow');					
				});
				
				$('input[name="reportType"]').click(function() {
				
					if($(this).val() == 2) {	

						$("#role").val("2");
					 	$("#role option[value='0']").attr("disabled",true);
						 	
					} else {
						$("#role option[value='0']").attr("disabled",false);
						$("#role").val("-1");
							
					}
					
				});
				
				$("#downloadReportLink").click(function() {
					if(!validateDlAgentReport())
					{
						
						$("input[name=filterOption]").removeAttr("disabled");
						
						document.getElementById("generateAgentReportForm").action="${pageContext.request.contextPath}/customerReport.controller.downloadAgentReport.task";
						document.getElementById("generateAgentReportForm").submit();						
					}				
				});
			});		
	</script>
</head>
<body onload="keepSubMenuSelected()">
<form method="post" id="generateAgentReportForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="33" />
			<div class="pagetitle">
				<h3>
					<spring:message code="agtreport.header.text" /> 
				</h3>
			</div>

		<div style="text-align: right; margin-right: 5px;">
			<spring:message code="platform.text.mandatory" />
		</div>
		<br />
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1" id="reportTypeDiv">
			<div class="data_label" id="label_reportType">
				<spring:message code="agtreport.text.reportType" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width:auto;">
				<input type="radio" id="reportType" name="reportType" value="0" /><spring:message code="agtreport.text.reportTypePerformance" />&nbsp;&nbsp;&nbsp; 
				<input type="radio" id="reportType" name="reportType" value="1" /><spring:message code="agtreport.text.reportTypeSalary" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="reportType" name="reportType" value="2" /><spring:message code="agtreport.text.reportTypeCSCWeekly" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="reportType" name="reportType" value="3" /><spring:message code="agtreport.text.reportTypeDeducted" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>		

		<div class="data_container_color2" id="reportFilterDiv" style="display: none;">
			<div class="data_label" id="label_filterOption">
				<spring:message code="agtreport.text.filter" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="filterOption" name="filterOption" value="0" /><spring:message code="agtreport.text.bydate" />&nbsp;&nbsp;&nbsp; 
				 <input type="radio" id="filterOption" name="filterOption" value="1" /><label id="quick"><spring:message code="agtreport.text.quickselect" />&nbsp;&nbsp;&nbsp;</label>
			</div>
		</div>
		

		<div class="data_container_color1" id="dateFilterDiv" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
			<div class="data_text">
				<div style="float: left; margin-top: 3px;" id="label_fromDate">
					<spring:message code="platform.text.from" />&nbsp;&nbsp;
				</div>
				<div style="float: left">
					<input type="text" id="fromDate" name="fromDate" readonly="readonly"
						style="width: 70px" value="" />
					<div class="calendar-icon" onclick="javascript:clearField('fromDate')" id="calBut1" title="<spring:message code='tooltip.platform.calendar'/>"></div>
					<div class="clear-icon" onclick="javascript:clearField('fromDate')" id="clrBut1" title="<spring:message code='tooltip.platform.clear'/>"></div>				
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
					&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="platform.text.to" />&nbsp;&nbsp;
				</div>
				<div>
					<input type="text" id="toDate" name="toDate" readonly="readonly"
						style="width: 70px" value="" />
					<div class="calendar-icon" onclick="javascript:clearField('toDate')" id="calBut2" title="<spring:message code='tooltip.platform.calendar'/>"></div>
					<div class="clear-icon" onclick="javascript:clearField('toDate')" id="clrBut2" title="<spring:message code='tooltip.platform.clear'/>"></div>				
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

		<div class="data_container_color1" id="quickSelectFilterDiv" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="0" /><spring:message code="agtreport.text.currweek" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="1" /><spring:message code="agtreport.text.prevweek" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="2" /><spring:message code="agtreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="3" /><spring:message code="agtreport.text.prevmonth" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
		<div class="data_container_color1" id="quickSelectFilterDivForDeducted" style="display: none;">
			
			<div class="data_label" id="label_quickSelectOptionForDeducted">
				<spring:message code="agtreport.text.quickselect" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			
			<div class="data_text" style="width: auto">
				<input type="radio" id="quickSelectOptionForDeducted" name="quickSelectOptionForDeducted" value="0" /><spring:message code="agtreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOptionForDeducted" name="quickSelectOptionForDeducted" value="1" /><spring:message code="agtreport.text.prevmonth" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>

		<div class="data_container_color2" id="rolesDiv" style="display: none;">
			<div class="data_label" id="label_role"><spring:message code="report.text.role">
				</spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<div class="" id="div_id_15">
					<select id="role" name="role" style="width: 200px;">
						<option></option>
						<option value="0">All</option>
						<c:forEach var="roleMap" items="${roleMap}">
					    	<option value="${roleMap.key}">${roleMap.value}</option>
					 	</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper">
				<li class="btn-inner" id="downloadReportLink">
					<span>
						<spring:message code="report.button.downloadreport" />
					</span>
				</li>
			</ul>
		</div>
		
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>