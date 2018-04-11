<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"></link>
	<script>
	var errorMessage = '<%=String.valueOf(request.getAttribute("errorMessage"))%>';
	$(function() {
		$('input[name="repStyle"]').click(function() {
			if($(this).val() == 1) {
				$("#repStyleOld").show('slow');
				$("#repStyleNew").hide('slow');
				$("#repStyleCustom").hide('slow');
			}else if($(this).val() == 2){
				$("#repStyleOld").hide('slow');
				$("#repStyleNew").show('slow');
				$("#repStyleCustom").hide('slow');
			}else if($(this).val() == 3){
				$("#repStyleOld").hide('slow');
				$("#repStyleNew").hide('slow');
				$("#repStyleCustom").show('slow');
			}	
		});
	});
	
		$(function() {	
			
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
				
				
			});
			
			$("#downloadReportLink").click(function() {
				if(!validateGenCustReport())
				{
					document.getElementById("generateCustReportForm").action="${pageContext.request.contextPath}/customerReport.controller.downloadCustomerReportCSV.task";
					document.getElementById("generateCustReportForm").submit();
				 }				
			});	
			$('body').keypress(function(e){
				if(e.which == 13){
					$("#downloadReportLink").click();
				}
			});				
		});
	</script>
</head>

<body onload="keepSubMenuSelected()">
	<form method="post" id="generateCustReportForm">
	<jsp:include page="../../includes/calendar.jsp"></jsp:include>
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="27" />	
		<div class="pagetitle">
			<h3><spring:message code="custreport.header.text"></spring:message> 
			</h3>			
		</div>	

		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div id="messageDiv" style="text-align: center; color: Red; display: none; height: 25px;">
			<spring:message code="reports.no.data.exist"></spring:message>
		</div>
		
		<div class="data_container_color1" id="repTypeFilterDiv">
			<div class="data_label" id="label_repStyle">
			<spring:message code="report.text.reportStyle"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<input type="radio" id="repStyle" name="repStyle" value="1" /><spring:message code="report.text.reportStyleOld" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="repStyle" name="repStyle" value="2" /><spring:message code="report.text.reportStyleNew" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="repStyle" name="repStyle" value="3" /><spring:message code="report.text.reportStyleCustom" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
		<div id="repStyleOld"  style="display: none">
		<div class="data_container_color2">
			<div class="data_label" id="label_reportRange" >
				<spring:message code="custreport.text.showrecords"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<div id="div_id_1" style="float: left;">
					<select id="reportRange" name="reportRange" style="width: 130px">
						<option value=""/>
						<c:forEach var="repRangeMap" items='${reportRangesMap}'>
							<option value='${repRangeMap.key}'>${repRangeMap.value}</option>
						</c:forEach>
					</select> &nbsp;&nbsp;&nbsp;
				</div>
			</div>
		</div>
		</div>
		
		<div id="repStyleNew"  style="display: none">
		<div class="data_container_color2" id="regModeFilterDiv">
			<div class="data_label" id="label_regMode">
				<spring:message code="custreport.text.regMode"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<input type="radio" id="regMode" name="regMode" value="0" checked="checked"/><spring:message code="custreport.text.all" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="regMode" name="regMode" value="1" /><spring:message code="custreport.text.agent" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="regMode" name="regMode" value="2" /><spring:message code="custreport.text.self" />&nbsp;&nbsp;&nbsp; 
			</div>
		</div>
		
		<div class="data_container_color1" id="regTypeFilterDiv">
			<div class="data_label" id="label_regType">
				<spring:message code="custreport.text.productType"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
			<select id="regType" name="regType" >
				<option value="-1"></option>
				<option value="0"><spring:message code="custreport.text.all" /></option>
				<option value="1"><spring:message code="custreport.text.freeOnly" /></option>
				<option value="2"><spring:message code="custreport.text.xl" /></option>
				<option value="3"><spring:message code="custreport.text.hp" /></option>
			</select>								
			</div>
		</div>
		
		<div class="data_container_color2" id="confStatFilterDiv">
			<div class="data_label" id="label_confStat">
				<spring:message code="custreport.text.confStat"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<input type="radio" id="confStat" name="confStat" value="0" checked="checked"/><spring:message code="custreport.text.all" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="confStat" name="confStat" value="1" /><spring:message code="custreport.text.confirmed" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="confStat" name="confStat" value="2" /><spring:message code="custreport.text.unConfirmed" />&nbsp;&nbsp;&nbsp; 
			</div>
		</div>
		
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1" id="reportFilterDiv">
			<div class="data_label" id="label_filterOption">
				<spring:message code="custreport.text.filter" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<input type="radio" id="filterOption" name="filterOption" value="0" /><spring:message code="custreport.text.bydate" />&nbsp;&nbsp;&nbsp; 
				<input type="radio" id="filterOption" name="filterOption" value="1" /><spring:message code="custreport.text.quickselect" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
		
		<div class="data_container_color2" id="dateFilterDiv" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
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
					&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="platform.text.to" />&nbsp;&nbsp;
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

		<div class="data_container_color2" id="quickSelectFilterDiv" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
			<div class="data_text" style="width: 500px">
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="0" /><spring:message code="custreport.text.currweek" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="1" /><spring:message code="custreport.text.prevweek" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="2" /><spring:message code="custreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="3" /><spring:message code="custreport.text.prevmonth" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
		
		</div>	
		
		<div id="repStyleCustom"  style="display: none">
		<div class="data_container_color2">
			<div class="data_label" id="label_reportCustom" >
				<spring:message code="report.text.reportStyleCustom"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width: 500px">
				<input type="radio" id="customSelectOption" name="customSelectOption" value="1" /><spring:message code="custreport.text.renewedPoliciesReport" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="customSelectOption" name="customSelectOption" value="2" /><spring:message code="custreport.text.newPoliciesReport" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		</div>
		
		<div style="text-align: center;"> 
			<br/>
			<ul class="btn-wrapper"><li class="btn-inner" id="downloadReportLink"><span><spring:message code="report.button.downloadreport"/></span></li></ul>
		</div>			
		
	</div>
	</div>

	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>

	</form>
	<script>
		if(errorMessage == "true")
		$("#messageDiv").show();
		loadDropDownList('regType', '0');
	</script>
</body>
</html>