<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags"  prefix="spring"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/insuranceProvider.js" type="text/javascript"></script>		
	<script>	
	
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() { 
				if(!validateModifyInsuranceProvider()){
					confirmSave("", function(result) {
						if(result){
							document.getElementById("updateCompanyFrm").action="${pageContext.request.contextPath}/insuranceCompany.controller.modifyInsuranceProviderDetails.task?companyId=${VO_INS_PROVIDER.companyId}";
							document.getElementById("updateCompanyFrm").submit();
						}
					});
				}				
			});
			
			$("#backBtn").click(function() {
					document.getElementById("updateCompanyFrm").action="${pageContext.request.contextPath}/insuranceCompany.controller.listInsuranceProviders.task";
					document.getElementById("updateCompanyFrm").submit();			
			});
			
			$("body").keypress(function(e) {
				if (e.keyCode == '13') {
					$("#saveBtn").click();
					return false;
				}
			});

			$("#clearBtn").click(function() {
				clearField("companyNumber");
				clearField("branchName");
				clearField("addressLine1");
				clearField("addressLine2");
				clearField("city");
				clearField("state");
				clearField("country");
				clearField("zipCode");
				clearField("primaryContact");
				clearField("primaryContactMobile");
			});
		});

	</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="updateCompanyFrm" >
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="19" />	
		<div class="pagetitle">
			<h3><spring:message code="modinsprovider.header.text"></spring:message>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
		<br/>
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

		<div class="data_container_color1">
			<div class="data_label" id="label_companyName" style="width:260px;"><spring:message code="insprovider.text.compname"></spring:message>&nbsp;:</div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_1">
					<input type="text" class="labelTxtBoxLeft_color1" style="border:0px;width:110px;" readonly="readonly" id="companyName" name="companyName" value="${VO_INS_PROVIDER.companyName}" />
				</div>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id="label_companyNumber" style="width:260px;"><spring:message code="insprovider.text.compcontactnum"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_2">
					<input type="text" id="companyNumber" maxlength="10" name="companyNumber"  style="width:110px" value="${VO_INS_PROVIDER.companyNumber}"/>
				</div>
			</div>
		</div>
		
		<div class="data_container_color1">
			<div class="data_label" id="label_branchName" style="width:260px;"><spring:message code="insprovider.text.branchname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text"  style="width:328px;">
				<div id="div_id_3">
					<input type="text" id="branchName" maxlength="50" name="branchName"  style="width:110px" value="${VO_INS_PROVIDER.branchName}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_addressLine1" style="width:260px;"><spring:message code="insprovider.text.address1"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
				<div id="div_id_4">
					<input type="text" id="addressLine1" maxlength="90" name="addressLine1"  style="width:110px" value="${VO_INS_PROVIDER.addressLine1}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" style="width:260px;"><spring:message code="insprovider.text.address2"></spring:message>&nbsp;:</div>
			<div class="data_text" style="width:328px;">
					<input type="text" id="addressLine2" maxlength="90" name="addressLine2"  style="width:110px" value="${VO_INS_PROVIDER.addressLine2}"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_city" style="width:260px;"><spring:message code="insprovider.text.city"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
					<input type="text" id="city" name="city" maxlength="50"  style="width:110px" value="${VO_INS_PROVIDER.city}"/>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_state" style="width:260px;"><spring:message code="insprovider.text.state"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_7">
					<input type="text" id="state" name="state" maxlength="50"  style="width:110px" value="${VO_INS_PROVIDER.state}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_country" style="width:260px;"><spring:message code="insprovider.text.country"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<div id="div_id_8">
					<input type="text" id="country" name="country" maxlength="50"  style="width:110px" value="${VO_INS_PROVIDER.country}"/>
				</div>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_zipCode" style="width:260px;"><spring:message code="insprovider.text.zip"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
					<input type="text" id="zipCode" name="zipCode" maxlength="9"  style="width:110px" value="${VO_INS_PROVIDER.zipCode}"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" id="label_primaryContact" style="width:260px;"><spring:message code="insprovider.text.primcntname"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;text-align: left;">
				<input type="text" id="primaryContact" name="primaryContact" maxlength="80"  style="width:110px" value="${VO_INS_PROVIDER.primaryContact}"/>
			</div>
		</div>
		<div class="data_container_color1">
			<div class="data_label" id="label_primaryContactMobile" style="width:260px;"><spring:message code="insprovider.text.primcntmsisdn"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text" style="width:328px;">
				<input type="text" id="primaryContactMobile" maxlength="10" name="primaryContactMobile"  style="width:110px" value="${VO_INS_PROVIDER.primaryContactMobile}"/>
			</div>
		</div>
		<div class="data_container_color2">
			<div class="data_label" style="width:260px;"><spring:message code="platform.text.regby"></spring:message>&nbsp;:</div>
			<input type="text" id="createdBy" class="labelTxtBoxLeft_color2" style="border:0px" name="createdBy"  style="width:110px" value="${VO_INS_PROVIDER.createdBy}"/>
		</div>
		<div class="data_container_color1">
			<div class="data_label" style="width:260px;"><spring:message code="platform.text.regdate"></spring:message>&nbsp;:</div>
			<input type="text" id="createdDate" class="labelTxtBoxLeft_color1" style="border:0px" name="createdDate"  style="width:110px" value="${VO_INS_PROVIDER.createdDate}"/>
		</div>
		<div class="data_container_color2">
			<div class="data_label" style="width:260px;"><spring:message code="platform.text.modby"></spring:message>&nbsp;:</div>
			<input type="text" id="modifiedBy" class="labelTxtBoxLeft_color2" style="border:0px" name="modifiedBy"  style="width:110px" value="${VO_INS_PROVIDER.modifiedBy}"/>
		</div>
		<div class="data_container_color1">
			<div class="data_label" style="width:260px;"><spring:message code="platform.text.moddate"></spring:message>&nbsp;:</div>
			<input type="text" id="modifiedDate" class="labelTxtBoxLeft_color1" style="border:0px" name="modifiedDate" style="width:110px" value="${VO_INS_PROVIDER.modifiedDate}"/>
		</div>
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.savechanges"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
			<ul class="btn-wrapper"><li class="btn-inner" id="backBtn"><span><spring:message code="platform.button.back"/></span></li></ul>
			
		</div>
		<br/>
	</div>
	</div>
<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>

