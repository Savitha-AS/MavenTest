<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/report.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"></link>
	<script>		
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#genBtn").click(function() { 
				if(!validateGenFinReport()){
					//showData();
					document.getElementById("generateFinancialReportForm").action="${pageContext.request.contextPath}/financialReport.controller.generateFinancialReport.task";
					document.getElementById("generateFinancialReportForm").submit();
				}
			});
			
			$('body').keypress(function(e){
				if(e.which == 13){
					$("#genBtn").click();
				}
			});

			$("#clearBtn").click(function() {
				clearField("txt_1");
				clearField("txt_2");
				clearField("txt_3");
				clearField("txt_4");
			});

			$("#downloadReportLink").click(function() {				
				document.getElementById("generateFinancialReportForm").action="${pageContext.request.contextPath}/financialReport.controller.downloadFinancialReport.task?totalCust=${financialReportVo.totalActiveCustomers}&freeSum=${financialReportVo.freeSumAssured}&paidSum=${financialReportVo.paidSumAssured}&totalSum=${financialReportVo.totalSumAssured}";
				document.getElementById("generateFinancialReportForm").submit();
			});						
		});

		function showData(){
			$("#reportData").show('slow');
		}
	</script>  
</head>
<body onload="keepSubMenuSelected()">
	
	<form method="post" id="generateFinancialReportForm"> 
	
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">		
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="28" />
		<div class="pagetitle">
			<h3><spring:message code="finreport.header.text"></spring:message>
			</h3>			
		</div>	
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
	
		<div class="data_container_color1">
			<div class="data_label" style="width:320px;"  id = "label_txt_1"><spring:message code="finreport.text.totcustomer"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:270px;">
				<input type="text" id="txt_1" name="totalActiveCustomers" size="4"  maxlength="3"/> &nbsp; <spring:message code="platform.text.currency"></spring:message>
			</div>
		</div>
	
		<div class="data_container_color2">
			<div class="data_label" style="width:320px;" id = "label_txt_2"><spring:message code="finreport.text.freeassured"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:270px;">
				<div class="" id="div_id_2">
					<input type="text" id="txt_2" name="freeSumAssured" size="4" maxlength="4"/> &nbsp; %
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" style="width:320px;" id = "label_txt_3"><spring:message code="finreport.text.paidassured"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:270px;" >
				<div class="" id="div_id_3">
					<input type="text" id="txt_3" name="paidSumAssured" size="4" maxlength="4"/> &nbsp; %
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" style="width:320px;" id = "label_txt_4"><spring:message code="finreport.text.totassured"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:270px; text-align: left;">
				<input type="text" id="txt_4" name="totalSumAssured"  size="4"  maxlength="4"/> &nbsp; %
			</div>
		</div>
		<br/>
		
		<div style="text-align: center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="genBtn"><span><spring:message code="report.button.generatereport"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
			
		</div>	
		<br/><br/>
	
		<div id="reportData" style="display:none">
			<fieldset>
				<legend>											
					<spring:message code="finreport.text.for"></spring:message>&nbsp;${currentMonth}
				</legend>
				<div class="data_container_color1">
					<div class="data_label" style="width:240px;float:left"><spring:message code="finreport.text.actcust"></spring:message>&nbsp;:</div>
					<div class="data_label" style="width:250px; text-align: left;" >
						<input type="text" id="txt_51" value="${financialReportVo.totalActiveCustomers}" class="labelTxtBox_color1" style="border:0px" readonly="readonly" /> &nbsp; <spring:message code="platform.text.currency"></spring:message>
					</div>
				</div>
				<div class="data_container_color2">
					<div class="data_label" style="width:240px;float:left"><spring:message code="finreport.text.freesumassured"></spring:message>&nbsp;:</div>
					<div class="data_label" style="width:260px; text-align: left;">
						<input type="text" id="txt_52" value="${financialReportVo.freeSumAssured}" class="labelTxtBox_color2" style="border:0px" readonly="readonly" /> &nbsp; <spring:message code="platform.text.currency"></spring:message>
					</div>
				</div>											
				<div class="data_container_color1">
					<div class="data_label" style="width:240px;float:left"><spring:message code="finreport.text.paidsumassured"></spring:message>&nbsp;:</div>
					<div class="data_label" style="width:260px; text-align: left;">
						<input type="text" id="txt_53" value="${financialReportVo.paidSumAssured}" class="labelTxtBox_color1" style="border:0px" readonly="readonly" /> &nbsp; <spring:message code="platform.text.currency"></spring:message>
					</div>
				</div>
				<div class="data_container_color2">
					<div class="data_label" style="width:240px;float:left"><spring:message code="finreport.text.totsumassured"></spring:message>&nbsp;:</div>
					<div class="data_label" style="width:260px; text-align: left;">
						<input type="text" id="txt_54" value="${financialReportVo.totalSumAssured}" class="labelTxtBox_color2" style="border:0px" readonly="readonly" /> &nbsp; <spring:message code="platform.text.currency"></spring:message>
					</div>
				</div>
				<br/>
			</fieldset>
			<br/>
			<div style="text-align:center">
				<ul class="btn-wrapper"><li class="btn-inner" id="downloadReportLink"><span><spring:message code="report.button.downloadreport"/></span></li></ul>
			</div>
			<br/>
		</div>						
	</div>
	</div>
							
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	
	<c:if test='${financialReportVo.totalActiveCustomers != null}'>
		<script type="text/javascript"> 
				$("#reportData").show('slow');
		</script>
	</c:if>
	</form>
</body>
</html>