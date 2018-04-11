<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
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
		$("#search-icon")
				.click(
						function() {
							if (!validateDeRegisterCustomer()) {
								document.getElementById("deRegisterCustomer").action = "${pageContext.request.contextPath}/customer.controller.getCustProductsForDeReg.task";
								document.getElementById("deRegisterCustomer")
										.submit();
							}
						});
	});

	$(function() {
		$("#saveBtn")
				.click(
						function() {
							$("#message_div").hide();
							if (!validateDeRegisterProductSelected()) {

								if (showValidationErrors("validationMessages_parent"))
									return true;
								else {
									confirmSave(
											"Are you sure you want to continue?",
											function(result) {
												if (result) {
													$(
															'input:checkbox[id=productId]')
															.attr('disabled',
																	false);
													document
															.getElementById("deRegisterCustomer").action = "${pageContext.request.contextPath}/customer.controller.deRegisterCustomer.task";
													document
															.getElementById(
																	"deRegisterCustomer")
															.submit();

												}
											});
								}
							}
						});
	});

	$("body").keypress(function(e) {
		if (e.keyCode == '13') {
			$("#search-icon").click();
			return false;
		}
	});
	$(function() {
		$("#dreg_backBtn")
				.click(
						function() {
							document.getElementById("deRegisterCustomer").action = "${pageContext.request.contextPath}/customer.controller.loadDeRegisterCustomer.task";
							document.getElementById("deRegisterCustomer")
									.submit();
						});
	});

	$(function() {
		$("#backBtn")
				.click(
						function() {
							document.getElementById("deRegisterCustomer").action = "${pageContext.request.contextPath}/customer.controller.loadDeRegisterCustomer.task";
							document.getElementById("deRegisterCustomer")
									.submit();
						});
	});
</script>
<style>
.data_label {
	width: 190px;
}
</style>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="deRegisterCustomer">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>

		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
				<input type="hidden" id="pageId" value="32" />
				<div class="pagetitle">
					<h3>
						<spring:message code="deregcustomer.header.text"></spring:message>
					</h3>
				</div>
				<div style="text-align: right; margin-right: 5px;">
					<spring:message code="platform.text.mandatory" />
				</div>
				<br />
				<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

				<c:if test="${fn:length(message) gt 0}">
					<div id="message_div" style="text-align: center; color: Green;">
						<spring:message code="${message}" arguments="${msisdn}"></spring:message>
					</div>
					<br />
				</c:if>

				<div class="data_container_color1">
					<div class="data_label" id="label_msisdn">
						<spring:message code="platform.text.msisdn"></spring:message>
						&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
					</div>
					<div class="data_text" style="width: 70px;">
						<input type="text" name="msisdn" id="msisdn" maxlength="10"
							size="10" value="${VO_CUSTOMER.msisdn}" />
					</div>
					<div id="search-icon"></div>
				</div>

				<div id="deRegProductList" style="display: none">
					<div class="data_container_color2">
						<div class="data_label" id="label_productId">
							<spring:message code="deregcustomer.text.option" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<div id="div_id_2" style="float: left;">
								<c:forEach var="products"
									items="${VO_CUSTOMER.purchasedProducts}">
									<input type="checkbox" id="productId" name="productId"
										value="${productsList[products-1].productId}" />&nbsp;${productsList[products-1].productName}&nbsp;&nbsp;
							</c:forEach>
							</div>
						</div>
					</div>



					<!-- show customer details before de registration  -->
					<c:if test="${frameWork:isNotNull(customerDetailsList)}">
						<br />
						<div
							style="overflow-x: auto; height: auto; margin-left: 2px; width: 100%;">
							<fieldset style="padding-bottom: 10px">
								<legend id="regcustomer.subheader1.text">
									<spring:message code="regcustomer.subheader1.text"></spring:message>
								</legend>
								<display:table id="customerDetailsList"
									name="customerDetailsList"
									requestURI="/customer.controller.searchCustomerDetails.task"
									excludedParams="*" style="width:690px; height:100%;"
									cellspacing="0" cellpadding="0" >

									<!-- customer name -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="custreport.text.customername" property="custName"
										style="text-align:left; width :10px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<!-- mobile number -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.msisdn" property="msisdn"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<!-- product name -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.offerDetails"
										property="purchasedProducts"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<%-- <!-- reg product level -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.regProductLevel"
										property="regProductLevel"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column> --%>

									<!--confirmation status -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.confirmationStatus"
										property="isConfirmed"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<!-- deducted amount -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.deductedAmountAsOnDate"
										property="deductedOfferAmount"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>

									<!-- earned cover -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.coverEarnedInTheCurrentMonth"
										property="earnedCover"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									
									<!-- previous Month Usage -->
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.prevMonthUsage" property="prevMonthUsage"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
								</display:table>
							</fieldset>

						</div>
						<br />
						<div style="text-align: center;">
							<ul class="btn-wrapper">
								<li class="btn-inner" id="backBtn"><span><spring:message
											code="platform.button.back" /></span></li>
							</ul>
							<ul class="btn-wrapper">
								<li class="btn-inner" id="saveBtn"><span> <spring:message
											code="platform.button.deregister" />
								</span></li>
							</ul>
						</div>

					</c:if>

					
				</div>

               <div id="div_deRegesteredProductDetails" >
				<!-- show customer deregistered details -->
				<c:if test="${frameWork:isNotNull(customerDeregDetailsList)}">
					<br />
					<div 
						style="overflow-x: auto; height: auto; margin-left: 2px; width: 100%;">

						<fieldset style="padding-bottom: 10px">
							<legend id="label_regcustomer_subheader11">
								<spring:message code="deregcustomer.subheader1.text"></spring:message>
							</legend>
							<display:table id="customerDeregDetailsList"
								name="customerDeregDetailsList"
								requestURI="/customer.controller.searchCustomerDetails.task"
								excludedParams="*" style="width:690px; height:100%;"
								cellspacing="0" cellpadding="0">

								<!-- customer name -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="custreport.text.customername" property="custName"
									style="text-align:left; width :10px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- mobile number -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.msisdn" property="msisdn"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- product -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.offerDetails"
									property="purchasedProducts"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<%-- <!-- reg product level -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.regProductLevel"
									property="regProductLevel"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column> --%>

								<!-- deducted amount -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.deductedAmount"
									property="deductedOfferAmount"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- cover earned -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.coverEarned" property="earnedCover"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- previous month usage -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.prevMonthUsage" property="prevMonthUsage"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- deregby -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.deRegBy" property="deRegBy"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- deregDate -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.deRegDate" property="deRegDate"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

								<!-- date of customer removal -->
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="platform.text.dateOfCustomerRemoval"
									property="dateOfCustomerRemoval"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
								</display:column>

							</display:table>
						</fieldset>
					</div>
					<br />
					<div id="div_dreg_backBtn"  style="text-align: center;">
						<ul class="btn-wrapper">
							<li class="btn-inner" id="dreg_backBtn"><span><spring:message
										code="platform.button.back" /></span></li>
						</ul>

					</div>
				</c:if>
				</div>




			</div>
		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
	<script type="text/javascript">
		if ('${custexists}' == "true") {
			
			$("#msisdn").val('${VO_CUSTOMER.msisdn}');
			$('#msisdn').attr('readonly', true);
			
			
			if ( $("#customerDetailsList").length > 0 )
				{
			 		$("#deRegProductList").show('slow'); 
			 		$("#div_dreg_backBtn").hide('slow'); 
				}
			
			if ( $("#customerDeregDetailsList").length > 0 )
				$("#div_deRegesteredProductDetails").show('slow'); 
			else
				$("#div_deRegesteredProductDetails").hide('slow'); 
			
			
			
			$("input[type=checkbox][id=productId]")
					.click(
							function() {

								if ($(this).is(":checked")) {
									if ($(this).val() == '1') {

										$(
												'input[type=checkbox][id=productId][value="2"]')
												.attr("checked", true);
										$(
												'input[type=checkbox][id=productId][value="2"]')
												.attr("disabled", true);
									}

									//exta life
									if ($(this).val() == '2') {

										$(
												'input[type=checkbox][id=productId][value="1"]')
												.attr("checked", true);
										$(
												'input[type=checkbox][id=productId][value="1"]')
												.attr("disabled", true);
									}

								} else {
									if ($(this).val() == '1') {

										$(
												'input[type=checkbox][id=productId][value="2"]')
												.attr("checked", false);
										$(
												'input[type=checkbox][id=productId][value="2"]')
												.attr("disabled", false);
									}

									//extra life
									if ($(this).val() == '2') {

										$(
												'input[type=checkbox][id=productId][value="1"]')
												.attr("checked", false);
										$(
												'input[type=checkbox][id=productId][value="1"]')
												.attr("disabled", false);
									}
								}
								/*alert($(this).is(":checked"));
								
								 if ($(this).attr("checked") && $(this).val()=="1"){
									if(('${subscribedProductList}').match("2")=="2"){
										alert("Select Xtra-Life also");
										$('input[type=checkbox][id=productId][value="2"]').attr(
												"checked", true);
										$('input[type=checkbox][id=productId][value="2"]').attr(
												"disabled", true);
									}else{
										alert("Customer not subscribed to Xtra-Life");
									}					
								 }else if($(this).attr("checked",false) && $(this).val()=="1"){
									 if(('${subscribedProductList}').match("2")=="2"){							 
											$('input[type=checkbox][id=productId][value="2"]').attr(
													"checked", false);
											$('input[type=checkbox][id=productId][value="2"]').click(function(){
												 $('input[type=checkbox][id=productId][value="2"]').attr(
															"checked", true);
											 });
											$('input[type=checkbox][id=productId][value="2"]').attr(
													"disabled", false);
										}
								 }else{
										alert("Other products selected");
										$(this).attr("checked",true);
								 }*/
							});
		} else if ('${custexists}' == "false") {
			$("#deRegProductList").hide('slow');
		}
	</script>
</body>
</html>
