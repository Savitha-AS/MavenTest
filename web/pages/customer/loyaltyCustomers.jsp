<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1"


    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html> --%>

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
		var msisdn1;
		$("#search-icon")
				.click(
						function() {
							msisdn = $('#msisdn').val();
							console.log(msisdn1);
							$("#message_div").hide();
							if (!validateDeRegisterCustomer()) {
								document
										.getElementById("applyLocaltyToCustomer").action = "${pageContext.request.contextPath}/customer.controller.getLoyaltyInformation.task";
								document.getElementById(
										"applyLocaltyToCustomer").submit();
							}

						});

	});

	$(function() {
		$("#saveCreditBtn")
				.click(
						function() {
							if (!validateLoyaltyProductSelected()) {

								if (showValidationErrors("validationMessages_parent"))
									return true;
								else {
									confirmSave(
											"Are you sure you want to continue?",
											function(result) {
												if (result) {
													$(
															'input:checkbox[id=productIdSelected]')
															.attr('disabled',
																	false);
													document
															.getElementById("applyLocaltyToCustomer").action = "${pageContext.request.contextPath}/customer.controller.applyLoyalty.task";
													document
															.getElementById(
																	"applyLocaltyToCustomer")
															.submit();
													$("#loyaltyProductList")
															.hide();
													$("#saveCreditBtn").hide();
													$("#backBtn").hide();
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
		$("#backBtn")
				.click(
						function() {
							document.getElementById("applyLocaltyToCustomer").action = "${pageContext.request.contextPath}/customer.controller.loyaltyCustomers.task";
							document.getElementById("applyLocaltyToCustomer")
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
	<form method="post" id="applyLocaltyToCustomer">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>

		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
				<input type="hidden" id="pageId" value="51" />
				<div class="pagetitle">
					<h3>
						<spring:message code="loyaltyToCustomer.header.text"></spring:message>
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
				
				<c:if test="${fn:length(faultmessage) gt 0}">
					<div id="message_div" style="text-align: center; color: red;">
						<spring:message code="${faultmessage}"></spring:message>
					</div>
				</c:if>

				<div class="data_container_color1">
					<div class="data_label" id="label_msisdn">
						<spring:message code="platform.text.msisdn"></spring:message>
						&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
					</div>
					<div class="data_text" style="width: 70px;">
						<input type="text" name="msisdn" id="msisdn" maxlength="10"
							size="10" value="${loyaltymsisdn}" />
					</div>

					<div id="search-icon"></div>
				</div>



<%-- 
				<div id="loyaltyProductList" style="display: none">
					<div class="data_container_color2">
					
						<div class="data_label" id="label_productId">
							<spring:message code="platform.text.productsForLoyalty" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						
						<div class="data_text">
							<div id="div_id_2" style="float: left;">
								<c:forEach var="customer" items="${loyalCustomers}">
									<c:if test='${customer.loyaltyProvidedDate  eq "N/A" }'>

										<input type="checkbox" value="${customer.productId}"
											id="productId" name="productId"> </input>
										<c:out value="${customer.productName}" />

									</c:if>

									<c:if test='${customer.loyaltyProvidedDate  ne "N/A" }'>
										<input type="checkbox" value="${customer.productId}"
											id="productIdDisabled" name="productIdDisabled" value="${customer.productId}"
											checked="checked" disabled="disabled"> </input>
										<c:out value="${customer.productName}" />
									</c:if>
								</c:forEach>

							</div>
						</div>
					</div>
					</div> --%>
					
					
					
				<div id="loyaltyProductList" style="display: none">
					<div class="data_container_color2">
					
						<div class="data_label" id="label_productId">
							<spring:message code="platform.text.productsForLoyalty" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						
						<div class="data_text">
							<div id="div_id_2" style="float: left;">
								<c:forEach var="customer" items="${loyalCustomers}">
									<c:if test='${customer.loyaltyProvidedDate  eq "N/A" }'>

										<input type="radio" value="${customer.productId}"
											id="productId" name="productId"> </input>
										<c:out value="${customer.productName}" />

									</c:if>

									<c:if test='${customer.loyaltyProvidedDate  ne "N/A" }'>
										<input type="radio" value="${customer.productId}"
											id="productIdDisabled" name="productIdDisabled" value="${customer.productId}"
											checked="checked" disabled="disabled"> </input>
										<c:out value="${customer.productName}" />
									</c:if>
								</c:forEach>

							</div>
						</div>
					</div>
					</div> 
					
					
				<div id="XLPackage" style="display: none">
		           <div class="data_container_color1">
						<div class="data_label" id="label_XLPackage">
						 <spring:message code="platform.text.pakageXL" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span> 
						</div>
						<div class="data_text">
							<div id="div_id_2" style="float: left;">
								
							  <select id="pakageXL" name="pakageXL" style="width: 80px;">
									  <option value=""></option>
									  <option value="1" ><spring:message code="loyalty.text.datapack"></spring:message></option>
									  <option value="0"><spring:message code="loyalty.text.smspack"></spring:message></option>
							  </select>
								
							</div>
						</div>
					</div>
				</div> 


				<div id="HPPackage" style="display: none">
			 		<div class="data_container_color2">
						<div class="data_label" id="label_HPPackage">
						 <spring:message code="platform.text.pakageHP" />
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span> 
						</div>
						<div class="data_text">
								<select id="pakageHP" name="pakageHP" style="width: 80px;">
									<option value=""></option>
									<option value="1"><spring:message code="loyalty.text.datapack"></spring:message></option>
									<option value="0" ><spring:message code="loyalty.text.smspack"></spring:message></option>
								</select>
						</div>
					</div> 
					</div>
					
			
					<div id="div_loyaltyProductDetails">

						<!-- show customer deregistered details -->
						<c:if test="${frameWork:isNotNull(loyalCustomers)}">
							<br />


							<div
								style="overflow-x: auto; height: auto; margin-left: 2px; width: 100%;">

								<fieldset style="padding-bottom: 10px">
									<legend id="label_regcustomer_subheader11">
										<%-- <spring:message code="loyaltyToCustomer.subheader1.text"></spring:message> --%>
									</legend>
									<display:table id="loyalCustomers" name="loyalCustomers"
										excludedParams="*" style="width:690px; height:100%;"
										cellspacing="0" cellpadding="0">

										<!-- customer name -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="custreport.text.customername"
											property="customerName"
											style="text-align:left; width :10px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- mobile number -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.msisdn" property="msisdn"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- product -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.isLoyaltyProvided"
											property="isLoyaltyEligible"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- deducted amount -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.confirmedDate"
											property="confirmedDate"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- cover earned -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.isConfirmed" property="isConfirmed"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- previous month usage -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.loyaltyProvidedDate"
											property="loyaltyProvidedDate"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

										<!-- deregby -->
										<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
											titleKey="platform.text.productId" property="productName"
											style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										</display:column>

									</display:table>
								</fieldset>
							</div>
						</c:if>
					</div>
				</div>

				<!-- 		<br /> -->
				<div id="buttons" style="text-align: center; display: none">
					<ul class="btn-wrapper">
						<li class="btn-inner" id="backBtn"><span><spring:message
									code="platform.button.back" /></span></li>
					</ul>
					<ul class="btn-wrapper">
						<li class="btn-inner" id="saveCreditBtn"><span> <spring:message
									code="platform.button.creditLoyalty" />
						</span></li>
					</ul>
				</div>


			</div>
		
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>


	<script type="text/javascript">
		var productId;
		if ('${custexists}' == "true") {

			$("#message_div").show();
	
			if ($("#loyalCustomers").length > 0) {
				$("#div_loyaltyProductDetails").show('slow');
				$("#loyaltyProductList").show('slow');
				$("#buttons").show();
				$("#search-icon").hide('slow');
				if ('${loyaltyEligible}' == "false") {
					$("#saveCreditBtn").hide();
				}
					
				 if(($('input:radio[value=2]').is(
					':disabled') == true))
						{
						 $("#XLPackage").show('slow'); 
						loadDropDownList('pakageXL', '${xlPackage}');
						document.getElementById("pakageXL").disabled = true;						
						}
				if(($('input:radio[value=3]').is(
				':disabled') == true))
					{
					$("#HPPackage").show('slow');
					loadDropDownList('pakageHP', '${hpPackage}');
					document.getElementById("pakageHP").disabled = true;	
					}
				
				
			} else
				$("#div_loyaltyProductDetails").hide('slow');

						
			$("input[type=radio]")
			.click(
					function() {
						
						 if(($('input:radio[value=2]').is(
						':checked') == true ))
							{
							$("#XLPackage").show('slow');
						
						
							}
					    if(($('input:radio[value=3]').is(
						':checked') == true ))
							{
							
							$("#HPPackage").show('slow');
								
							}
						if(($('input:radio[value=2]').is(
						':checked') == false))
							{
							
							$("#XLPackage").hide('slow');
						
							}
						if(($('input:radio[value=3]').is(
						':checked') == false))
							{
								$("#HPPackage").hide('slow');
							
							}
						
						
					});

			
			
		} else if ('${custexists}' == "false") {
			$("#message_div").show();
			$("#loyaltyProductList").hide();
			$("#saveCreditBtn").hide();
			$("#backBtn").hide();
		}
	</script>
</body>
</html>
