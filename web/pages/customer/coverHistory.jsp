<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
<link href="${pageContext.request.contextPath}/theme/css/displaytag.css"
	rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/appScripts/customer.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/appScripts/utils.js"
	type="text/javascript"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/util.js"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/interface/validateCustomerMSISDN.js"></script>
<script>
	$(function() {
		$("#search-icon").click(function() {

			if (!validateCoverHistory()) {
				checkMSISDNStatusForCoverHistory($("#msisdn").val());
			}

		});
		
	});

	/**
	 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
	 * 
	 * @param msisdnStatus boolean result of the DWR method invoked.
	 * 
	 * @see checkMSISDNStatus()
	 */
	function loadMSISDNCheckForExist(msisdnStatus) {

		var isMsisdnExist = msisdnStatus.split(",")[0];

		if (isMsisdnExist == '0') {
			setError("msisdn", 36, 'Mobile Number');

		}
		if (showValidationErrors("validationMessages_parent"))
			return true;
		else {
			document.getElementById("changeCoverForm").action = "${pageContext.request.contextPath}/customer.controller.getCoverHistory.task";
			document.getElementById("changeCoverForm").submit();

		}
	}

	$(document).ready(function() {
		$("#div_searchResults").show('slow');
	});

	$(function() {
		$("body").keypress(function(e) {
			if (e.keyCode == '13') {
				$("#search-icon").click();
				return false;
			}
		});
	});

	$(function() {
		$("#moreBtn")
				.click(
						function() {

							document.getElementById("changeCoverForm").action = "${pageContext.request.contextPath}/customer.controller.getMoreCoverHistory.task";
							document.getElementById("changeCoverForm").submit();

						});
	});
</script>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="changeCoverForm">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>
		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following "pageId" field should hold the value of "menuId" coming from database for this page. -->
				<input type="hidden" id="pageId" value="49" />

				<div class="pagetitle">
					<h3>
						<spring:message code="coverHistory.header.text" />
					</h3>
				</div>

				<div style="text-align: right; margin-right: 5px;">
					<spring:message code="platform.text.mandatory" />
				</div>
				<br />

				<jsp:include page="../../includes/global_validations.jsp"
					flush="true"></jsp:include>

				<c:if test="${fn:length(message) gt 0}">
					<div id="message_div" style="text-align: center; color: Green;">
						<spring:message code="${message}"></spring:message>
					</div>
				</c:if>


				<c:if test="${frameWork:isNull(VO_CUSTOMER) && frameWork:isNull(VO_CUSTOMER_HP) && frameWork:isNull(VO_CUSTOMER_IP)}">
					<div class="data_container_color1">
						<div class="data_label" id="label_msisdn" style="width: 170px;">
							<spring:message code="platform.text.msisdn" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text" style="width: 358px;">
							<div id="div_id_1" style="float: left;">
								<input type="text" id="msisdn" name="msisdn"
									value="${VO_CUSTOMER[0].msisdn}" maxlength="10"
									style="width: 110px;" />
							</div>
							<div id="search-icon"></div>
						</div>
					</div>
				</c:if>
				

				<c:if test="${frameWork:isNotNull(VO_CUSTOMER) || frameWork:isNotNull(VO_CUSTOMER_HP) || frameWork:isNotNull(VO_CUSTOMER_IP)}">
					<div class="data_container_color1">
						<div class="data_label" id="label_msisdn" style="width: 170px;">
							<spring:message code="platform.text.msisdn" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text" style="width: 358px;">
							<div id="div_id_1">
								<input type="text" id="msisdn" name="msisdn"
									value="${mobileNumber}" maxlength="10"
									style="text-align: left; width: 110px; border: 0px"
									readonly="readonly" class="labelTxtBox_color1" />
							</div>

						</div>
					</div>
				</c:if>
				
			

				<div style="overflow-x: auto; display: none;" id="div_searchResults">
					<c:if test="${fn:length(NOT_REGISTERED_XL) gt 0 }">					

						<fieldset>						
							<legend id="label_regcustomer_subheader1">
								<spring:message code="xl.free.details"></spring:message>
							</legend>

							<spring:message code="cover.details.notfound"></spring:message>
						</fieldset>

					</c:if>
					</div>
					
			
                <div id="XLAndFreeProductCoverDetails_div" >	
					<c:choose>
						<c:when test="${frameWork:isNotNull(VO_CUSTOMER)}">
							<br/>
							
						<div style="overflow-x: auto; text-align: center; color: Red;" id="gracePeriod_XL_div">								
							<spring:message code="platform.text.gracePeriod.info"></spring:message>							
						</div>
						
							<fieldset>
								<legend id="label_regcustomer_subheader1">
									<spring:message code="xl.free.details"></spring:message>
								</legend>

								<display:table id="VO_CUSTOMER" name="VO_CUSTOMER"
									style="width:690px;" cellspacing="0" cellpadding="0"
									excludedParams="">
									<display:setProperty name="basic.empty.showtable" value="false"></display:setProperty>
									<display:setProperty name="basic.msg.empty_list">
										<spring:message code="no.coverhistory.found"></spring:message>
										<br/>
										<br/>
									</display:setProperty>

									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.month" property="month"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.cover" property="freeAndxlCover"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.priorMonthCharges"
										property="chargesXL"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.priorMonthAirtime"
										property="prevMonthUsage"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
								</display:table>

							</fieldset>
						</c:when>
					</c:choose>
					</div>
					<br/>

                   <c:if test="${fn:length(NOT_REGISTERED_HP) gt 0 }">
						<fieldset>
							<legend id="label_regcustomer_subheader1">
								<spring:message code="hospital.support.details"></spring:message>
							</legend>

							<spring:message code="cover.details.notfound"></spring:message>
						</fieldset>

					</c:if>
                 <div id="hospitalCoverDetails_div" >	
                     <c:choose>

						<c:when test="${frameWork:isNotNull(VO_CUSTOMER_HP)}">
						<br/>
							<div style="overflow-x: auto; text-align: center; color: Red;" id="gracePeriod_HP_div">								
							<spring:message code="platform.text.gracePeriod.info"></spring:message>							
							</div>

							<fieldset>
								<legend id="label_regcustomer_subheader1">
									<spring:message code="hospital.support.details"></spring:message>
								</legend>

								<display:table id="VO_CUSTOMER_HP" name="VO_CUSTOMER_HP"
									style="width:690px;" cellspacing="0" cellpadding="0"
									excludedParams="">
									<display:setProperty name="basic.empty.showtable" value="false"></display:setProperty>
									<display:setProperty name="basic.msg.empty_list">
										<spring:message code="no.coverhistory.found"></spring:message>
										<br/>
										<br/>
									</display:setProperty>

									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.month" property="month"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.coverHospital" property="coverHP"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.priorMonthCharges"
										property="chargesHP"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>


								</display:table>
							</fieldset>
						</c:when>
					</c:choose>
					</div>
					<br/>
					<c:if test="${fn:length(NOT_REGISTERED_IP) gt 0 }">					
						<fieldset>
							<legend id="label_regcustomer_subheader1">
								<spring:message code="income.support.details"></spring:message>
							</legend>

							<spring:message code="cover.details.notfound"></spring:message>
						</fieldset>

					</c:if>
					
				
				<div id="incomeProtectionCoverDetails_div" >	
                        <c:choose>
						<c:when test="${frameWork:isNotNull(VO_CUSTOMER_IP)}">
						<div style="overflow-x: auto; text-align: center; color: Red;" id="gracePeriod_IP_div">								
							<spring:message code="platform.text.gracePeriod.info"></spring:message>							
						</div>
						<div style="overflow-x: auto; text-align: center; color: Red;" id="no_claim_IP_div">								
							<spring:message code="platform.text.noclaim.bonus.date">${noClaimBonusDate}</spring:message>							
						</div>
							<fieldset>
								<legend id="label_regcustomer_subheader1">
									<spring:message code="income.support.details"></spring:message>
								</legend>

								<display:table id="VO_CUSTOMER_IP" name="VO_CUSTOMER_IP"
									style="width:690px;" cellspacing="0" cellpadding="0"
									excludedParams="">
									<display:setProperty name="basic.empty.showtable" value="false"></display:setProperty>
									<display:setProperty name="basic.msg.empty_list">
										<spring:message code="no.coverhistory.found"></spring:message>
										<br/>
										<br/>
									</display:setProperty>

									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.month" property="month"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.coverIncome" property="coverIP"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="coverHistory.text.priorMonthCharges"
										property="chargesIP"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
                               </display:table>
							</fieldset>
						</c:when>
					</c:choose>	
				</div>
				</div>
				<br/>

				<c:if
					test="${frameWork:isNotNull(VO_CUSTOMER_HP) || frameWork:isNotNull(VO_CUSTOMER) || frameWork:isNotNull(VO_CUSTOMER_IP) }">
					<c:if test="${fn:length(DISABLE_MORE) eq 0}">
						<div style="text-align: center;">
							<ul class="btn-wrapper">
								<li class="btn-inner" id="moreBtn"><span><spring:message
											code="platform.button.more" /></span></li>
							</ul>
						</div>
					</c:if>
				</c:if>
			</div>

		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
	<script type="text/javascript">
	
	if('${VO_CUSTOMER.productId}' == '1,2,3'){
		$("#incomeProtectionCoverDetails_div").hide('slow');
		$("#hospitalCoverDetails_div").show('slow');
		$("#XLAndFreeProductCoverDetails_div").show('slow');
	}else  if('${VO_CUSTOMER.productId}' == '1,2,4'){
		$("#incomeProtectionCoverDetails_div").show('slow');
		$("#hospitalCoverDetails_div").hide('slow');
		$("#XLAndFreeProductCoverDetails_div").show('slow');
	}else if('${VO_CUSTOMER.productId}' == '4'){
	    $("#incomeProtectionCoverDetails_div").show('slow');
	    $("#hospitalCoverDetails_div").hide('slow');
	    $("#XLAndFreeProductCoverDetails_div").hide('slow');
	}else if('${VO_CUSTOMER.productId}' == '3'){
		 $("#incomeProtectionCoverDetails_div").hide('slow');
		 $("#hospitalCoverDetails_div").show('slow');
		 $("#XLAndFreeProductCoverDetails_div").hide('slow');
	}else if('${VO_CUSTOMER.productId}' == '1,2,3,4'){
		 $("#incomeProtectionCoverDetails_div").show('slow');
		 $("#hospitalCoverDetails_div").hide('slow');
		 $("#XLAndFreeProductCoverDetails_div").show('slow');
	}
	</script>
	
	<script>
	if('${gracePeriodXLExist}' == 1){
		 $("#gracePeriod_XL_div").show();
	}
	else if('${gracePeriodXLExist}' == 0){
		 $("#gracePeriod_XL_div").hide();
	}
	
	if('${gracePeriodHPExist}' == 1){
		 $("#gracePeriod_HP_div").show();
	}
	else if('${gracePeriodHPExist}' == 0){
		 $("#gracePeriod_HP_div").hide();
	}
	
	if('${gracePeriodIPExist}' == 1){
		 $("#gracePeriod_IP_div").show();
	}
	else if('${gracePeriodIPExist}' == 0){
		 $("#gracePeriod_IP_div").hide();
	}
	

	</script>
</body>
</html>