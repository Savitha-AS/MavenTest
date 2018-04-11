<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	/* $(function() {
		$("button, input:button, input:submit").button();
	}); */
	$(function() {
		$("input[type='checkbox']").click(function() {
			$("#deduction").hide();

			if ($("input[type='checkbox']").is(":checked")) {

				var isDeductionModeShow = $("#is_DeductionMode").val();

				var dedVal = $("#deductionMode").val();

				if (dedVal == 0)
					$("#deductionMode").val("1");

				//only for call center 
				if (isDeductionModeShow == "true") {

					$("#deduction").show();

				} else {
					$("#deduction").hide();

				}
			} else
				$("#deductionMode").val("0");
		});
		$("#search-icon")
				.click(
						function() {
							if (!validateRegisterCustomer()) {
								document.getElementById("registerCustomer").action = "${pageContext.request.contextPath}/customer.controller.checkCustomerExists.task";
								document.getElementById("registerCustomer")
										.submit();
							}
						});
	});

	$(function() {
		$("#saveBtn").click(
				function() {
					$("#message_div").hide();
					if (!validateRegCust('${is_editable}', '${custexists}',
							'${is_hpCust}', '${is_xlCust}', '${is_ipCust}',
							'${isPAReg}')) {
						
						resetErrors();
						var inMsisdn=$("#insMsisdn").val();;
						var ipMsisdn=$("#ipInsMsisdn").val();
												
						
						var msisdnCodes = $("#msisdnCodes").val();
											
						if(inMsisdn.length >0)
						{
							
							inMsisdn = inMsisdn.substr(0, 3);
							
							if(msisdnCodes.search(inMsisdn)==-1)
							  setError("insMsisdn", 33, getFieldText("insMsisdn"));
							
							if (showValidationErrors("validationMessages_parent"))
								return true;
						}
						if(ipMsisdn.length > 0)
						{
							ipMsisdn = ipMsisdn.substr(0, 3);
							if(msisdnCodes.search(ipMsisdn)==-1)
								 setError("ipInsMsisdn", 33, getFieldText("ipInsMsisdn"));
							if (showValidationErrors("validationMessages_parent"))
								return true;
						}
						
						if (showValidationErrors("validationMessages_parent"))
							return true;
						else
							{
						checkForExistingMSISDN('${VO_CUSTOMER.custId}');
							
							}
						
					}
				});
	});

	$(function() {
		$("body").keypress(function(e) {
			if (e.keyCode == '13') {
				$("#search-icon").click();
				return false;
			}
		});
	});

	/**
	 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
	 * 
	 * @param msisdnExists boolean result of the DWR method invoked.
	 * 
	 * @see checkForExistingMSISDN()
	 */
	function loadMSISDNCheck(msisdnExists) {

		if (msisdnExists == "true,false") {
			setError("msisdn", 20, "Mobile Number");
		} else if (msisdnExists == "false,true") {
			setError("msisdn", 33, "Mobile Number");
		} else if (msisdnExists == "true,true") {
			setError("msisdn", 20, "Mobile Number");
			setError("msisdn", 33, "Mobile Number");
		}

		if (showValidationErrors("validationMessages_parent"))
			return true;
		else {
			confirmSave(
					"Has the customer agreed for the terms & conditions"
							+ " of TIGO Insurance and for the below declaration? \n\n"
							+ "\"To the best of my knowledge and belief, I and the "
							+ "insured family member are in good health and \nfree"
							+ " from any adverse medical conditions.\"",
					function(result) {
						if (result) {
							if ('${custexists}' == "true") {
								$('input:radio[id=gender]').attr('disabled',
										false);
								$('input:checkbox[id=productId]').attr(
										'disabled', false);
							}
							document.getElementById("registerCustomer").action = "${pageContext.request.contextPath}/customer.controller.registerCustomer.task";
							document.getElementById("registerCustomer")
									.submit();
						}
					});
		}
	}

	$(function() {
		$("#backBtn")
				.click(
						function() {
							document.getElementById("registerCustomer").action = "${pageContext.request.contextPath}/customer.page.registerCustomer.task";
							document.getElementById("registerCustomer")
									.submit();
						});
	});

	$(function() {
		$("#onlyBackBtn")
				.click(
						function() {
							document.getElementById("registerCustomer").action = "${pageContext.request.contextPath}/customer.page.registerCustomer.task";
							document.getElementById("registerCustomer")
									.submit();
						});
	});

	$(function() {
		$("#clearBtn")
				.click(
						function() {

							if ('${is_editable}' == "true"
									&& '${custexists}' == "false") {
                               
								clearField("fname");
								clearField("sname");

								//$("#msisdn").attr("readonly", false);
								clearField("dob");
								clearField("age");
								resetRadioButtons("gender");
								resetDropDownList("insRelation");
								clearField("insRelFname");
								clearField("insRelSurname");
								clearField("insRelIrDoB");
								clearField("insRelAge");
								clearField("ipNomFirstName");
								clearField("ipNomSurName");
								clearField("ipNomAge");
								clearField("insMsisdn");
								clearField("ipInsMsisdn");
								resetCheckBox("productId");
								resetDropDownList("productCoverIdIP");

								 $("#displayIrdInfo_div").hide('slow');
								$("#displayIrdInfoForIp_div").hide('slow');
								$("#displayofferLevel_div").hide('slow'); 

								 $('input:checkbox[value=2]').attr('disabled',
										false);
								$('input:checkbox[value=3]').attr('disabled',
										false);
								$('input:checkbox[value=4]').attr('disabled',
										false); 

							} else if ('${is_editable}' == "true"
									&& '${custexists}' == "true") {

								clearField("fname");
								clearField("sname");
							/* 	clearField("dob");
								clearField("age"); */
								resetRadioButtons("gender");

								if ('${is_xlCust}' == "true") {
									resetDropDownList("insRelation");
									clearField("insRelFname");
									clearField("insRelSurname");
									clearField("insRelIrDoB");
									clearField("insRelAge");
									clearField("insMsisdn");

								}
								if ('${is_ipCust}' == "true") {
          
									clearField("ipNomFirstName");
									clearField("ipNomSurName");
									clearField("ipNomAge");
									
									clearField("ipInsMsisdn");
								}

								resetDropDownList("insRelation");
								clearField("insRelFname");
								clearField("insRelSurname");
								clearField("insRelIrDoB");
								clearField("insRelAge");
								clearField("insMsisdn");

								//resetDropDownList("productCoverIdIP");
								clearField("ipNomFirstName");
								clearField("ipNomSurName");
								clearField("ipNomAge");
								clearField("ipInsMsisdn");

								/* if ('${is_xlCust}' == "true"
										&& '${is_hpCust}' == "true"
										&& '${is_ipCust}' == "true") {

								} else if ('${is_xlCust}' == "true"
										&& '${is_hpCust}' == "false"
										&& '${is_ipCust}' == "false") {
									$('input:checkbox[value=3]').attr(
											'checked', false);
									$('input:checkbox[value=4]').attr(
											'checked', false);
								} else if ('${is_xlCust}' == "false"
										&& '${is_hpCust}' == "true"
										&& '${is_ipCust}' == "false") {
									$('input:checkbox[value=2]').attr(
											'checked', false);
									$('input:checkbox[value=4]').attr(
											'checked', false);

								} else if ('${is_xlCust}' == "false"
										&& '${is_hpCust}' == "false"
										&& '${is_ipCust}' == "true") {
									$('input:checkbox[value=2]').attr(
											'checked', false);
									$('input:checkbox[value=3]').attr(
											'checked', false);
								} else if ('${is_xlCust}' == "true"
										&& '${is_hpCust}' == "false"
										&& '${is_ipCust}' == "true") {
									$('input:checkbox[value=3]').attr(
											'checked', false);

								} */

								/* resetCheckedBox("productId",
										'${VO_CUSTOMER.purchasedProducts}'); */
								//$("#displayIrdInfo_div").hide('slow');
								//$("#saveButtons_div").hide('slow');
								//$("#deduction").hide();
								//$("#msisdn").attr("readonly", false);
							} else if ('${is_editable}' == "false") {

								if ('${is_xlCust}' == "false") {

									resetDropDownList("insRelation");
									clearField("insRelFname");
									clearField("insRelSurname");
									clearField("insRelIrDoB");
									clearField("insRelAge");

								}

								if ('${is_ipCust}' == "false") {

								    resetDropDownList("productCoverIdIP");
									clearField("ipNomFirstName");
									clearField("ipNomSurName");
									clearField("ipNomAge");
								}

							}
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
	<form id="registerCustomer" method="post" action="">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>

		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
				<input type="hidden" id="pageId" value="16" />
				<div class="pagetitle">
					<h3>
						<spring:message code="regcustomer.header.text"></spring:message>
					</h3>
				</div>
				<div style="text-align: right; margin-right: 5px;">
					<spring:message code="platform.text.mandatory" />
				</div>
				<br />
				<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

				<c:if test="${fn:length(message) gt 0}">
					<div id="message_div" style="text-align: center; color: Green;">
						<spring:message code="${message}"></spring:message>
					</div>
				</c:if>

				<input type="hidden" id="custId" name="custId"
					value="${VO_CUSTOMER.custId}" /> <input type="hidden"
					id="is_DeductionMode" name="is_DeductionMode"
					value="${is_DeductionMode}" />
					
				 <input type="hidden"
					id="productRegistered" name="productRegistered"
					value="${VO_CUSTOMER.productRegistered}" />	
				
				<input type="hidden"
					id="productActiveRegister" name="productActiveRegister"
					value="${VO_CUSTOMER.productRegistered}" />
						
				<input type="hidden"
					id="isReactive" name="isReactive"
					value="${VO_CUSTOMER.isReactive}" />
					
				<input type="hidden"
					id="isCustExist" name="isCustExist"
					value="${VO_CUSTOMER.isCustExist}" />	
					
				<input type="hidden"
					id="msisdnCodes" name="msisdnCodes"
					value="${msisdnCodes}" />
				
				<input type="hidden"
					id="reactivationMsg" name="reactivationMsg"
					value="${reactivationMsg}" />	
				
				<input type="hidden"
					id="custProductCancellation" name="custProductCancellation"
					value="${VO_CUSTOMER.custProductCancellation}" />						
				
				<fieldset>
					<legend id="label_regcustomer_subheader1">
						<spring:message code="regcustomer.subheader1.text"></spring:message>
					</legend>
					<div class="data_container_color1">
						<div class="data_label" id="label_msisdn">
							<spring:message code="platform.text.msisdn"></spring:message>
							&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
						</div>
						<div class="data_text" style="width: 358px;">
							<div id="div_id_1" style="float: left;">
								<input type="text" id="msisdn" name="msisdn" maxlength="10"
									style="width: 110px;" value="${VO_CUSTOMER.msisdn}" />
							</div>
							<div id="search-icon"></div>
						</div>
					</div>
					<br />
				</fieldset>
				
   				<c:if test="${fn:length(reactivationMsg) gt 0}">
   				<br />
					<div id="message_div" style="text-align: center; color: Green;">
						${reactivationMsg}
					</div>
				</c:if>	
				
				<c:if test="${fn:length(custEXistInBimaCancellationMsg) gt 0}">
   				<br />
					<div id="message_div" style="text-align: center; color: Green;">
						${custEXistInBimaCancellationMsg}
					</div>
				</c:if>		
				
				
						

				<br />
				<div id="displayProduct_div" style="display: none">
					<fieldset>
						<legend id="label_regcustomer_subheader1">
							<spring:message code="regcustomer.subheader4.text"></spring:message>
						</legend>
						<div class="data_container_color1">
							<div class="data_label" id="label_productId">
								<spring:message code="products.text.available"></spring:message>
								&nbsp;:&nbsp;
							</div>
							<div class="data_text">
								<div id="div_id_2" style="float: left;">
									<c:choose>
										<c:when test="${custexists eq false}">
											<c:forEach var="products" items="${productsList}">
												<c:if test="${products.productId ne 1}">
													<input type="checkbox" id="productId" name="productId"
														value="${products.productId}" />&nbsp;${products.productName}&nbsp;&nbsp;
						</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:forEach var="products" items="${productsList}">
												<c:if test="${products.productId ne 1}">
													<input type="checkbox" id="productId" name="productId"
														value="${products.productId}" />&nbsp;${products.productName}&nbsp;&nbsp;
						</c:if>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<br />
					</fieldset>
					<br />
				</div>

				<div id="displayofferLevel_div" style="display: none;">

					<fieldset>
						<legend id="label_regcustomer_subheader5">
							<spring:message code="platform.text.offerCoverDetailsIP"></spring:message>
						</legend>

						<div class="data_container_color1">
							<div class="data_label" id="label_productCoverIdIP">
								<spring:message code="platform.text.offerCoverLevel"></spring:message>
								&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<select id="productCoverIdIP" name="productCoverIdIP"
									style="width: 180px;">
									<option value=""></option>
									<c:forEach var="offers" items="${offerCoverDetailsList}">
										<option value="${offers.productCoverId}">
											${offers.productCoverCharges} GHC
											<fmt:formatNumber value="${offers.productCover}" />
											&${offers.productCoverIP}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						</br>
					</fieldset>
					</br>
				</div>




				<div id="displayCustInfo_div" style="display: none">
					<fieldset>
						<legend id="label_regcustomer_subheader1">
							<spring:message code="regcustomer.subheader1.text"></spring:message>
						</legend>

						<div class="data_container_color2">
							<div class="data_label" id="label_fname">
								<spring:message code="platform.text.firstname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="fname" name="fname"
											style="width: 110px" maxlength="100" value="${VO_CUSTOMER.fname}" />
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${is_editable}">
												<input type="text" id="fname" name="fname"
													style="width: 110px" maxlength="100"
													value="${VO_CUSTOMER.fname}" />
											</c:when>
											<c:otherwise>
												<input type="text" id="fname" name="fname"
													style="width: 110px; text-align: left; border: 0px"
													maxlength="100" value="${VO_CUSTOMER.fname}"
													readonly="readonly" class="labelTxtBox_color2" />
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="data_container_color1">
							<div class="data_label" id="label_sname">
								<spring:message code="platform.text.surname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_2">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="sname" name="sname"
												style="width: 110px" maxlength="50" value="${VO_CUSTOMER.sname}" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="sname" name="sname"
														style="width: 110px" maxlength="50"
														value="${VO_CUSTOMER.sname}" />
												</c:when>
												<c:otherwise>
													<input type="text" id="sname" name="sname"
														style="width: 110px; text-align: left; border: 0px"
														maxlength="50" value="${VO_CUSTOMER.sname}"
														readonly="readonly" class="labelTxtBox_color1" />
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>

						<div class="data_container_color2">
							<div class="data_label" id="label_dob">
								<spring:message code="platform.text.dob"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text" style="text-align: left">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="dob" name="dob" style="width: 110px"
											readonly="readonly" value="${VO_CUSTOMER.dob}" />
										<div class="calendar-icon"
											onclick="javascript:clearDate('dob','age')" id="calBut1"
											title="<spring:message code='tooltip.platform.calendar'/>"></div>
										<div class="clear-icon"
											onclick="javascript: clearDate('dob','age')"
											title="<spring:message code='tooltip.platform.clear'/>"></div>
										<script>
											Calendar.setup({
												inputField : "dob",
												trigger : "calBut1",
												onSelect : function() {
													this.hide();
													calculateAge('dob', 'age');
												},
												dateFormat : "%d/%m/%Y"
											});
										</script>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${is_editable eq true}">
												<%-- <input type="text" id="dob" value="${VO_CUSTOMER.dob}"
													name="dob" style="width: 110px" readonly="readonly" />
												<div class="calendar-icon"
													onclick="javascript:clearDate('dob','age')" id="calBut1"
													title="<spring:message code='tooltip.platform.calendar'/>"></div>
												<div class="clear-icon"
													onclick="javascript: clearDate('dob','age')"
													title="<spring:message code='tooltip.platform.clear'/>"></div>
												<script>
													Calendar.setup({
														inputField : "dob",
														trigger : "calBut1",
														onSelect : function() {
															this.hide();
															calculateAge('dob',
																	'age');
														},
														dateFormat : "%d/%m/%Y"
													});
												</script> --%>
												
												<input type="text" id="dob" name="dob"
													style="width: 110px; text-align: left; border: 0px"
													value="${VO_CUSTOMER.dob}" readonly="readonly"
													class="labelTxtBox_color2" />
												
											</c:when>
											<c:otherwise>
												<input type="text" id="dob" name="dob"
													style="width: 110px; text-align: left; border: 0px"
													value="${VO_CUSTOMER.dob}" readonly="readonly"
													class="labelTxtBox_color2" />
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_age">
								<spring:message code="platform.text.ageAtReg"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="age" name="age" style="width: 20px"
											maxlength="2"  value="${VO_CUSTOMER.age}"  />
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${is_editable eq true}">
												<%-- <input type="text" id="age" name="age"
													value="${VO_CUSTOMER.age}" style="width: 20px"
													maxlength="2" /> --%>
												<input type="text" id="age" name="age"
													style="width: 20px; text-align: left; border: 0px"
													maxlength="2" value="${VO_CUSTOMER.age}"
													readonly="readonly" class="labelTxtBox_color1" />	
											</c:when>
											<c:otherwise>
												<input type="text" id="age" name="age"
													style="width: 20px; text-align: left; border: 0px"
													maxlength="2" value="${VO_CUSTOMER.age}"
													readonly="readonly" class="labelTxtBox_color1" />
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>

							</div>
						</div>
						
					

						<div class="data_container_color2">
							<div class="data_label" id="label_gender">
								<spring:message code="platform.text.gender"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="radio" id="gender" name="gender" value="M" />
										<spring:message code="platform.text.male"></spring:message>&nbsp;&nbsp;&nbsp; 
					<input type="radio" id="gender" name="gender" value="F" />
										<spring:message code="platform.text.female"></spring:message>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${is_editable eq true}">
												<input type="radio" id="gender" name="gender" value="M" />
												<spring:message code="platform.text.male"></spring:message>&nbsp;&nbsp;&nbsp; 
							<input type="radio" id="gender" name="gender" value="F" />
												<spring:message code="platform.text.female"></spring:message>
											</c:when>
											<c:otherwise>
												<input type="radio" id="gender" name="gender" value="M"
													disabled="disabled" />
												<spring:message code="platform.text.male"></spring:message>&nbsp;&nbsp;&nbsp; 
							<input type="radio" id="gender" name="gender" value="F"
													disabled="disabled" />
												<spring:message code="platform.text.female"></spring:message>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="data_container_color1" id="div_impliedAge" style="display: none;">
							<div class="data_label" id="label_age">
								<spring:message code="platform.text.impliedAge"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text">
							
							  <input type="text" id="impliedAge" name="impliedAge"
													style="width: 20px; text-align: left; border: 0px"
													maxlength="2" value="${VO_CUSTOMER.impliedAge}"
												readonly="readonly" class="labelTxtBox_color1" /> 
	
							</div>
						</div>
						
						<br />
					</fieldset>
					<br />
				</div>

				<div id="displayIrdInfo_div" style="display: none">
					<fieldset>
						<legend id="label_regcustomer_subheader2">
							<spring:message code="regcustomer.subheader2.text"></spring:message>
						</legend>

						<div class="data_container_color1">
							<div class="data_label" style="" id="label_insRelation">
								<spring:message code="regcustomer.text.rel"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_15">
									<c:choose>
										<c:when test="${custexists eq false}">
											<select id="insRelation" name="insRelation"
												style="width: 130px;">
												<option></option>
												<c:forEach var="relationTypes"
													items="${RELATIONSHIP_TYPE_LIST}">
													<c:if test="${relationTypes != 'Others'}">
														<option value="${relationTypes}">${relationTypes}</option>
													</c:if>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<select id="insRelation" name="insRelation"
														style="width: 130px;">
														<option></option>
														<c:forEach var="relationTypes"
															items="${RELATIONSHIP_TYPE_LIST}">
															<c:if test="${relationTypes != 'Others'}">
																<option value="${relationTypes}">${relationTypes}</option>
															</c:if>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">



															<input type="text" id="insRelation" name="insRelation"
																style="text-align: left; width: 130px; border: 0px"
																class="labelTxtBox_color1"
																value="${VO_CUSTOMER.insRelation}" readonly="readonly" />

														</c:when>
														<c:otherwise>
															<c:set var="relOthers">
																<spring:message code="relationship.9" />
															</c:set>
															<select id="insRelation" name="insRelation"
																style="width: 130px;">
																<option></option>
																<c:forEach var="relationTypes"
																	items="${RELATIONSHIP_TYPE_LIST}">
																	<c:choose>
																		<c:when test="${relationTypes eq relOthers}">
																			<c:if
																				test="${relationTypes eq VO_CUSTOMER.insRelation}">
																				<option value="${relationTypes}">${relationTypes}</option>
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			<option value="${relationTypes}">${relationTypes}</option>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</select>

														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>

						<div class="data_container_color2">
							<div class="data_label" id="label_insRelFname">
								<spring:message code="platform.text.firstname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_13">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="insRelFname" name="insRelFname"
												style="width: 110px" maxlength="50" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="insRelFname" name="insRelFname"
														style="width: 110px" maxlength="50" />
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
															<input type="text" id="insRelFname" name="insRelFname"
																value="${VO_CUSTOMER.insRelFname}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color2" maxlength="50"
																readonly="readonly" />

														</c:when>
														<c:otherwise>
															<input type="text" id="insRelFname" name="insRelFname"
																value="${VO_CUSTOMER.insRelFname}" style="width: 110px"
																maxlength="50" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="data_container_color1">
							<div class="data_label" id="label_insRelSurname">
								<spring:message code="platform.text.surname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_14">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="insRelSurname" name="insRelSurname"
												style="width: 110px" maxlength="50" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="insRelSurname" name="insRelSurname"
														style="width: 110px" maxlength="50" />
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
															<input type="text" id="insRelSurname"
																name="insRelSurname"
																value="${VO_CUSTOMER.insRelSurname}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color1" maxlength="50"
																readonly="readonly" />
														</c:when>
														<c:otherwise>


															<input type="text" id="insRelSurname"
																name="insRelSurname"
																value="${VO_CUSTOMER.insRelSurname}"
																style="width: 110px" maxlength="50" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>

						<div class="data_container_color2">
							<div class="data_label" id="label_insRelIrDoB">
								<spring:message code="platform.text.dob"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text" style="text-align: left">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="insRelIrDoB" name="insRelIrDoB"
											style="width: 110px" readonly="readonly" />
										<div class="calendar-icon"
											onclick="javascript:clearDate('insRelIrDoB','insRelAge')"
											id="calBut2"
											title="<spring:message code='tooltip.platform.calendar'/>"></div>
										<div class="clear-icon"
											onclick="javascript: clearDate('insRelIrDoB','insRelAge')"
											title="<spring:message code='tooltip.platform.clear'/>"></div>
										<script>
											Calendar.setup({
												inputField : "insRelIrDoB",
												trigger : "calBut2",
												onSelect : function() {
													this.hide();
													calculateAge('insRelIrDoB',
															'insRelAge');
												},
												dateFormat : "%d/%m/%Y"
											});
										</script>
									</c:when>
									<c:otherwise>
										<%-- <c:choose> --%>

										<%-- <c:otherwise> --%>
										<c:choose>
											<c:when test="${is_editable eq true}">
												<input type="text" id="insRelIrDoB" name="insRelIrDoB"
													value="${VO_CUSTOMER.insRelIrDoB}" style="width: 110px"
													readonly="readonly" />
												<div class="calendar-icon"
													onclick="javascript:clearDate('insRelIrDoB','insRelAge')"
													id="calBut2"
													title="<spring:message code='tooltip.platform.calendar'/>"></div>
												<div class="clear-icon"
													onclick="javascript: clearDate('insRelIrDoB','insRelAge')"
													title="<spring:message code='tooltip.platform.clear'/>"></div>
												<script>
													Calendar
															.setup({
																inputField : "insRelIrDoB",
																trigger : "calBut2",
																onSelect : function() {
																	this.hide();
																	calculateAge(
																			'insRelIrDoB',
																			'insRelAge');
																},
																dateFormat : "%d/%m/%Y"
															});
												</script>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when
														test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
														<input type="text" id="insRelIrDoB" name="insRelIrDoB"
															value="${VO_CUSTOMER.insRelIrDoB}"
															style="width: 110px; text-align: left; border: 0px"
															readonly="readonly" class="labelTxtBox_color2" />
													</c:when>


													<c:otherwise>
														<input type="text" id="insRelIrDoB" name="insRelIrDoB"
															value="${VO_CUSTOMER.insRelIrDoB}" style="width: 110px"
															readonly="readonly" />
														<div class="calendar-icon"
															onclick="javascript:clearDate('insRelIrDoB','insRelAge')"
															id="calBut2"
															title="<spring:message code='tooltip.platform.calendar'/>"></div>
														<div class="clear-icon"
															onclick="javascript: clearDate('insRelIrDoB','insRelAge')"
															title="<spring:message code='tooltip.platform.clear'/>"></div>
														<script>
															Calendar
																	.setup({
																		inputField : "insRelIrDoB",
																		trigger : "calBut2",
																		onSelect : function() {
																			this
																					.hide();
																			calculateAge(
																					'insRelIrDoB',
																					'insRelAge');
																		},
																		dateFormat : "%d/%m/%Y"
																	});
														</script>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
											<%-- </c:choose> --%>
											<%-- </c:otherwise> --%>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_insRelAge">
								<spring:message code="platform.text.age"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text" style="text-align: left">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="insRelAge" name="insRelAge"
											style="width: 20px" maxlength="2" />
									</c:when>
									<c:otherwise>
										<%-- <c:choose> --%>
										<%-- <c:when test="${is_hpCust eq true}">
												<input type="text" id="insRelAge" name="insRelAge"
													style="width: 20px" maxlength="2" />
											</c:when> --%>
										<%-- <c:otherwise> --%>
										<c:choose>
											<c:when test="${is_editable eq true}">
												<input type="text" id="insRelAge" name="insRelAge"
													value="${VO_CUSTOMER.insRelAge}" style="width: 20px"
													maxlength="2" />
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when
														test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
														<input type="text" id="insRelAge" name="insRelAge"
															value="${VO_CUSTOMER.insRelAge}"
															style="width: 20px; text-align: left; border: 0px"
															class="labelTxtBox_color1" maxlength="2"
															readonly="readonly" />
													</c:when>

													<c:otherwise>
														<input type="text" id="insRelAge" name="insRelAge"
															value="${VO_CUSTOMER.insRelAge}" style="width: 20px"
															maxlength="2" />

													</c:otherwise>
												</c:choose>


											</c:otherwise>
										</c:choose>
										<%-- </c:otherwise> --%>
										<%-- </c:choose> --%>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						<div class="data_container_color2">
							<div class="data_label" id="label_gender" style="width: 260px">
								<spring:message code="platform.text.benMsisdn"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								
									
							<input type="radio" id="benInsMsisdnYesNo" name="benInsMsisdnYesNo" value="yes" checked="checked"  onclick="showInsMsisdn()"/>
										<spring:message code="platform.text.benMsisdn.yes"></spring:message>&nbsp;&nbsp;&nbsp; 
							<input type="radio" id="benInsMsisdnYesNo" name="benInsMsisdnYesNo" value="no"  onclick="showInsMsisdn()"/>
										<spring:message code="platform.text.benMsisdn.no"></spring:message>
			

								
							</div>
						</div>
						
						
						<div class="data_container_color1" id="div_insMsisdn">
							<div class="data_label" id="label_insMsisdn">
								<spring:message code="platform.text.msisdn"></spring:message>
								 &nbsp;:&nbsp;  								
							</div>
							<div class="data_text">
								<div class="" id="div_id_14">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="insMsisdn" name="insMsisdn"
												style="width: 110px" maxlength="10" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="insMsisdn" name="insMsisdn"
														style="width: 110px" maxlength="10"
														value="${VO_CUSTOMER.insMsisdn}" />

												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
													           
															<input type="text" id="insMsisdn" name="insMsisdn"
																value="${VO_CUSTOMER.insMsisdn}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color1" maxlength="10"
																readonly="readonly" />
													      	 <script >
													       		if($("#insMsisdn").val().length == 0 )
																{
																	$("input[name=benInsMsisdnYesNo]").val(['no']);
													
																}
													      		$('input[name="benInsMsisdnYesNo"]').attr('disabled', 'disabled');
													      		
																</script>    
														</c:when>
														<c:otherwise>


															<input type="text" id="insMsisdn" name="insMsisdn"
																value="${VO_CUSTOMER.insMsisdn}" style="width: 110px"
																maxlength="10" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="data_label" style="width: 200px;" >
								&nbsp;&nbsp;&nbsp;&nbsp;
								<spring:message code="platform.text.insMsisdnNotes"></spring:message>
								 
								</div>
							</div>
															
						</div>
						<br />
					</fieldset>
					<br />
				</div>

				<div id="displayIrdInfoForIp_div" style="display: none">
					<fieldset>
						<legend id="label_regcustomer_subheader6">
							<spring:message code="regcustomer.subheader6.text"></spring:message>
						</legend>

						<div class="data_container_color1">
							<div class="data_label" id="label_ipNomFirstName">
								<spring:message code="platform.text.firstname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_89">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="ipNomFirstName" name="ipNomFirstName"
												style="width: 110px" maxlength="50" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="ipNomFirstName" name="ipNomFirstName"
														style="width: 110px" maxlength="50"
														value="${VO_CUSTOMER.ipNomFirstName}" />
													
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) 
													               || ( is_xlCust eq false and is_hpCust eq false  and is_ipCust eq true)
													                
													           }">
													           
  
															<input type="text" id="ipNomFirstName" name="ipNomFirstName"
																value="${VO_CUSTOMER.ipNomFirstName}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color1" maxlength="50"
																readonly="readonly" />
														</c:when>
														<c:otherwise>


															<input type="text" id="ipNomFirstName" name="ipNomFirstName"
																value="${VO_CUSTOMER.ipNomFirstName}" style="width: 110px"
																maxlength="50" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						
						<div class="data_container_color2">
							<div class="data_label" id="label_ipNomSurName">
								<spring:message code="platform.text.surname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div class="" id="div_id_14">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="ipNomSurName" name="ipNomSurName"
												style="width: 110px" maxlength="50" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="ipNomSurName" name="ipNomSurName"
														style="width: 110px" maxlength="50"
														value="${VO_CUSTOMER.ipNomSurName}" />

												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true)  
													               || ( is_xlCust eq false and is_hpCust eq false  and is_ipCust eq true)
													           }">
															<input type="text" id="ipNomSurName" name="ipNomSurName"
																value="${VO_CUSTOMER.ipNomSurName}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color2" maxlength="50"
																readonly="readonly" />
														</c:when>
														<c:otherwise>


															<input type="text" id="ipNomSurName" name="ipNomSurName"
																value="${VO_CUSTOMER.ipNomSurName}" style="width: 110px"
																maxlength="50" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_ipNomAge">
								<spring:message code="platform.text.age"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text" style="text-align: left">
								<c:choose>
									<c:when test="${custexists eq false}">
										<input type="text" id="ipNomAge" name="ipNomAge"
											style="width: 20px" maxlength="2" />
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${is_editable eq true}">
											<input type="text" id="ipNomAge" name="ipNomAge"
													value="${VO_CUSTOMER.ipNomAge}" style="width: 20px"
													maxlength="2" />
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when
																										           
													    test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || ( is_xlCust eq true and is_ipCust eq true) 
													               || ( is_xlCust eq false and is_hpCust eq false  and is_ipCust eq true)
													           }">       
													           
														<input type="text" id="ipNomAge" name="ipNomAge"
															value="${VO_CUSTOMER.ipNomAge}"
															style="width: 20px; text-align: left; border: 0px"
															class="labelTxtBox_color1" maxlength="2"
															readonly="readonly" />
													</c:when>

													<c:otherwise>
													
														<input type="text" id="ipNomAge" name="ipNomAge"
															value="${VO_CUSTOMER.ipNomAge}" style="width: 20px"
															maxlength="2" />

													</c:otherwise>
												</c:choose>


											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						
						
						<div class="data_container_color2">
							<div class="data_label" id="label_gender" style="width: 260px">
								<spring:message code="platform.text.benMsisdn"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								
									
							<input type="radio" id="benMsisdnYesNo" name="benMsisdnYesNo" value="yes" checked="checked"  onclick="showIpInsMsisdn()"/>
										<spring:message code="platform.text.benMsisdn.yes"></spring:message>&nbsp;&nbsp;&nbsp; 
							<input type="radio" id="benMsisdnYesNo" name="benMsisdnYesNo" value="no"  onclick="showIpInsMsisdn()"/>
										<spring:message code="platform.text.benMsisdn.no"></spring:message>
			

								
							</div>
						</div>
						
						
						
						
						<div class="data_container_color1" id="div_ipInsMsisdn">
							<div class="data_label" id="label_ipInsMsisdn">
								<spring:message code="platform.text.msisdn"></spring:message>
								&nbsp;:&nbsp; 
							</div>
							<div class="data_text">
								<div class="" id="div_id_14">
									<c:choose>
										<c:when test="${custexists eq false}">
											<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
												style="width: 110px" maxlength="10" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${is_editable eq true}">
													<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
														style="width: 110px" maxlength="10"
														value="${VO_CUSTOMER.ipInsMsisdn}" />

												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when
															 test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || ( is_xlCust eq true and is_ipCust eq true) 
													               || ( is_xlCust eq false and is_hpCust eq false  and is_ipCust eq true)
													           }">  
															<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
																value="${VO_CUSTOMER.ipInsMsisdn}"
																style="width: 110px; text-align: left; border: 0px"
																class="labelTxtBox_color1" maxlength="10"
																readonly="readonly" />
															<script >
																if($("#ipInsMsisdn").val().length == 0 )
																	{
																		$("input[name=benMsisdnYesNo]").val(['no']);
															
																	}
													      		$('input[name="benMsisdnYesNo"]').attr('disabled', 'disabled');
															</script> 
																
														</c:when>
														<c:otherwise>


															<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
																value="${VO_CUSTOMER.ipInsMsisdn}" style="width: 110px"
																maxlength="10" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="data_label" style="width: 200px;" >
								&nbsp;&nbsp;&nbsp;&nbsp;
								<spring:message code="platform.text.insMsisdnNotes"></spring:message>
								 
								</div>
							</div>
							
						</div>
						
						
						
						<br />
					</fieldset>
					<br />
				</div>

				<div id="deduction" style="display: none;">
					<fieldset>
						<legend id="label_regcustomer_subheader2">
							<spring:message code="regcustomer.subheader5.text"></spring:message>
						</legend>


						<div class="data_container_color1">
							<div class="data_label" id="label_deductionMode">
								<spring:message code="regcustomer.text.deductionMode"></spring:message>
								&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
							</div>
							<c:if test="${custexists eq false}">
								<div class="data_text">
									<select id="deductionMode" name="deductionMode"
										style="width: 200px;">
										<!--  <option value="0"></option>  -->
										<option value="1">
											<spring:message code="regcustomer.text.deduction.mode.daily"></spring:message>
										</option>
										<%-- <option value="2">
											<spring:message
												code="regcustomer.text.deduction.mode.monthly"></spring:message>
										</option> --%>
									</select>
								</div>
							</c:if>
							<c:if test="${custexists eq true}">
								<div class="data_text">
									<c:if test="${VO_CUSTOMER.deductionMode=='0'}">
										<div class="data_text">
											<select id="deductionMode" name="deductionMode"
												style="width: 200px;">
												<option value="0"></option>
												<option value="1">
													<spring:message
														code="regcustomer.text.deduction.mode.daily"></spring:message>
												</option>
												<option value="2">
													<spring:message
														code="regcustomer.text.deduction.mode.monthly"></spring:message>
												</option>
											</select>
										</div>

									</c:if>

									<c:if test="${VO_CUSTOMER.deductionMode=='1'}">

										<input type="hidden" id="deductionMode" name="deductionMode"
											value="1" style="width: 20px; text-align: left; border: 0px"
											class="labelTxtBox_color1" readonly="readonly" />

										<spring:message code="regcustomer.text.deduction.mode.daily"></spring:message>


									</c:if>
									<c:if test="${VO_CUSTOMER.deductionMode=='2'}">
										<input type="hidden" id="deductionMode" name="deductionMode"
											value="2" style="width: 20px; text-align: left; border: 0px"
											class="labelTxtBox_color1" readonly="readonly" />
										<spring:message code="regcustomer.text.deduction.mode.monthly"></spring:message>
									</c:if>
								</div>
							</c:if>
						</div>
						</br>
					</fieldset>
				</div>

				<br />




				<c:choose>
					<c:when test="${is_editable eq true}">
						<div style="text-align: center;" id="saveButtons_div">
							<ul class="btn-wrapper">
								<li class="btn-inner" id="saveBtn"><span><spring:message
											code="platform.button.save" /></span></li>
							</ul>
							<ul class="btn-wrapper">
								<li class="btn-inner" id="clearBtn"><span><spring:message
											code="platform.button.clear" /></span></li>
							</ul>
							<ul class="btn-wrapper">
								<li class="btn-inner" id="backBtn"><span><spring:message
											code="platform.button.back" /></span></li>
							</ul>
						</div>
					</c:when>
					<c:otherwise>
						<div style="text-align: center; display: none"
							id="saveButtons_div">
							<ul class="btn-wrapper">
								<li class="btn-inner" id="saveBtn"><span><spring:message
											code="platform.button.save" /></span></li>
							</ul>
							<ul class="btn-wrapper">
								<li class="btn-inner" id="clearBtn"><span><spring:message
											code="platform.button.clear" /></span></li>
							</ul>

							<ul class="btn-wrapper">
								<li class="btn-inner" id="backBtn"><span><spring:message
											code="platform.button.back" /></span></li>
							</ul>

						</div>
						<div style="text-align: center; display: none"
							id="backButtons_div">

							<ul class="btn-wrapper">
								<li class="btn-inner" id="onlyBackBtn"><span><spring:message
											code="platform.button.back" /></span></li>
							</ul>

						</div>

					</c:otherwise>
				</c:choose>

				<br />
			</div>
		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
	<script type="text/javascript">
	function showIpInsMsisdn()
	{
		var radioButtonVal=$('input:radio[name=benMsisdnYesNo]:checked').val() ;
		if (radioButtonVal=='yes') {
			$("#div_ipInsMsisdn").show('slow');
		}else
		{
			$("#div_ipInsMsisdn").hide('slow');
			clearField("ipInsMsisdn");
			
		}	
		
		
		
	}
	function showInsMsisdn()
	{
		var radioButtonVal=$('input:radio[name=benInsMsisdnYesNo]:checked').val() ;
		if (radioButtonVal=='yes') {
			$("#div_insMsisdn").show('slow');
		}else
		{
			$("#div_insMsisdn").hide('slow');
			clearField("insMsisdn");
		}	
		
	}
	
	
	
	

	
		if ('${custexists}' == "true") {
			$('#msisdn').attr('readonly', true);
			$("#search-icon").hide('slow');
			$("#displayProduct_div").show('slow');
			$("#displayIrdInfo_div").show('slow');
			$("#div_impliedAge").show('slow');
			
			

			if ('${is_xlCust}' == "true" && '${is_hpCust}' == "true"
					&& '${is_ipCust}' == "true") {
				//customer subscribe for all
				$("#displayCustInfo_div").show('slow');
				$("#displayProduct_div").show('slow');
				$("#displayIrdInfoForIp_div").show('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');
				loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');

				$("#insRelAge").val('${VO_CUSTOMER.insRelAge}');
				$("#insRelSurname").val("${VO_CUSTOMER.insRelSurname}");
				$("#insRelFname").val("${VO_CUSTOMER.insRelFname}");
				$("#insRelIrDoB").val('${VO_CUSTOMER.insRelIrDoB}');

				$("#ipNomFirstName").val("${VO_CUSTOMER.ipNomFirstName}");
				$("#ipNomSurName").val("${VO_CUSTOMER.ipNomSurName}");
				$("#ipNomAge").val("${VO_CUSTOMER.ipNomAge}");

				$("#displayofferLevel_div").show('slow');

				loadDropDownList('productCoverIdIP',
						'${VO_CUSTOMER.productCoverIdIP}');
				$('#productCoverIdIP').attr("disabled", "disabled");

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				if ('${is_editable}' == "false") {
					//hide the save button 
					$("#saveButtons_div").hide('slow');
					$("#backButtons_div").show('slow');

				}

			} else if ('${is_xlCust}' == "true" && '${is_hpCust}' == "false"
					&& '${is_ipCust}' == "true") {
				//T,F,T
				//customer subscribe Xtra life
				$('input:checkbox[value=3]').attr('disabled', true);
				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfoForIp_div").show('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');
				loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');

				$("#insRelAge").val('${VO_CUSTOMER.insRelAge}');
				$("#insRelSurname").val("${VO_CUSTOMER.insRelSurname}");
				$("#insRelFname").val("${VO_CUSTOMER.insRelFname}");
				$("#insRelIrDoB").val('${VO_CUSTOMER.insRelIrDoB}');

				//For Ip Product Nominee Details
				$("#ipNomFirstName").val("${VO_CUSTOMER.ipNomFirstName}");
				$("#ipNomSurName").val("${VO_CUSTOMER.ipNomSurName}");
				$("#ipNomAge").val("${VO_CUSTOMER.ipNomAge}");

				$("#displayofferLevel_div").show('slow');

				loadDropDownList('productCoverIdIP',
						'${VO_CUSTOMER.productCoverIdIP}');
				$('#productCoverIdIP').attr("disabled", "disabled");

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				if ('${is_editable}' == "false") {
					//hide the save button 
					$("#saveButtons_div").hide('slow');
					$("#backButtons_div").show('slow');

				}

			} else if ('${is_xlCust}' == "true" && '${is_hpCust}' == "true"
					&& '${is_ipCust}' == "false") {

				//T,T,F
				//customer subscribe for xl and HP
				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfoForIp_div").hide('slow');
				$("#saveButtons_div").show('slow');
               
				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');
				loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');

				$("#insRelAge").val('${VO_CUSTOMER.insRelAge}');
				$("#insRelSurname").val("${VO_CUSTOMER.insRelSurname}");
				$("#insRelFname").val("${VO_CUSTOMER.insRelFname}");
				$("#insRelIrDoB").val('${VO_CUSTOMER.insRelIrDoB}');

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				if ('${isPAReg}' == "true") {
					$("input[type=checkbox]")
							.click(
									function() {

										if ($('input:checkbox[value=4]').is(
												':checked') == true) {

											$("#displayofferLevel_div").show(
													'slow');
											$("#displayIrdInfoForIp_div").show(
													'slow');

										} else if ($('input:checkbox[value=4]')
												.is(':checked') == false) {

											$("#displayofferLevel_div").hide(
													'slow');
											$("#displayIrdInfoForIp_div").hide(
													'slow');
										}
										
										if ('${isHPreactive}' == "true") {
											
											if ($('input:checkbox[value=3]').is(
													':checked') == true   && $('input:checkbox[value=4]')
													.is(':checked') == false  ) {
		
												$('input:checkbox[value=4]').attr('disabled', true);
												$("#displayIrdInfoForIp_div").hide('slow');
												resetDropDownList("productCoverIdIP");
												clearField("ipNomFirstName");
												clearField("ipNomSurName");
												clearField("ipNomAge");								
												
												
											} else if ($('input:checkbox[value=3]')
													.is(':checked') == false   && $('input:checkbox[value=4]').is(
													':checked') == true ) {
												$('input:checkbox[value=3]').attr('disabled', true);												
												$("#displayIrdInfoForIp_div").show('slow');
												$("#displayofferLevel_div").show(
												'slow');
														
											}else if ($('input:checkbox[value=3]')
													.is(':checked') == false   && $('input:checkbox[value=4]').is(
													':checked') == false ) {
												
												$('input:checkbox[value=4]').attr('disabled', false);
												$('input:checkbox[value=3]').attr('disabled', false);
												$("#displayIrdInfoForIp_div").hide('slow');
												resetDropDownList("productCoverIdIP");
												clearField("ipNomFirstName");
												clearField("ipNomSurName");
												clearField("ipNomAge");	
														
											}
											
											
											if ($('input:checkbox[value=2]').is(
											':checked') == true) {
				
														$("#displayIrdInfo_div").show('slow');

											} else if ($('input:checkbox[value=2]')
													.is(':checked') == false) {
		
												$("#displayIrdInfo_div").hide('slow');

											}
											
										}
										

									});

				} else {
					//disable IP product 
					$('input:checkbox[value=4]').attr('disabled', true);

				}

				$("input[type=checkbox]")
						.click(
								function() {

									if ($('input:checkbox[value=4]').is(
											':checked') == true) {

										$("#displayofferLevel_div")
												.show('slow');
										$("#displayIrdInfoForIp_div").show(
												'slow');

									} else if ($('input:checkbox[value=4]').is(
											':checked') == false) {

										$("#displayofferLevel_div")
												.hide('slow');
										
										$("#displayIrdInfoForIp_div").hide(
												'slow');
										
										resetDropDownList("productCoverIdIP");
										clearField("ipNomFirstName");
										clearField("ipNomSurName");
										clearField("ipNomAge");
										
									}

								});

				if ('${is_editable}' == "false" && '${isPAReg}' == "false") {
					//hide the save button 
					$("#saveButtons_div").hide('slow');
					$("#backButtons_div").show('slow');

					
					//check for the customer may come for reactivation
					 if('${reactivationMsg}'.length > 0)
						{
							$("#saveButtons_div").show('slow');
							$("#backButtons_div").hide('slow');
						} 
					
					
				}

			} else if ('${is_xlCust}' == "true" && '${is_hpCust}' == "false"
					&& '${is_ipCust}' == "false") {
				//customer subscribe for xl and IP
				//T,F,F
				//customer subscribe for xl and HP
				$("#displayCustInfo_div").show('slow');

				$("#displayProduct_div").show('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');
				loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');

				$("#insRelAge").val('${VO_CUSTOMER.insRelAge}');
				$("#insRelSurname").val("${VO_CUSTOMER.insRelSurname}");
				$("#insRelFname").val("${VO_CUSTOMER.insRelFname}");
				$("#insRelIrDoB").val('${VO_CUSTOMER.insRelIrDoB}');

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				if ('${isPAReg}' == "true") {
					$("input[type=checkbox]")
							.click(
									function() {

										if ($('input:checkbox[value=3]').is(
												':checked') == false
												&& $('input:checkbox[value=4]')
														.is(':checked') == true) {

											$('input:checkbox[value=3]').attr(
													'disabled', true);

											$("#displayofferLevel_div").show(
													'slow');
											$("#displayIrdInfoForIp_div").show(
													'slow');

										} else if ($('input:checkbox[value=3]')
												.is(':checked') == true
												&& $('input:checkbox[value=4]')
														.is(':checked') == false) {
											$('input:checkbox[value=4]').attr(
													'disabled', true);

											$("#displayofferLevel_div").hide(
													'slow');
											
											if('${ipDeactivated}' == "false")
											{
												resetDropDownList("productCoverIdIP");
												clearField("ipNomFirstName");
												clearField("ipNomSurName");
												clearField("ipNomAge");
													
											}	
											

										} else if ($('input:checkbox[value=3]')
												.is(':checked') == false
												&& $('input:checkbox[value=4]')
														.is(':checked') == false) {
											$('input:checkbox[value=4]').attr(
													'disabled', false);
											$('input:checkbox[value=3]').attr(
													'disabled', false);

											$("#displayofferLevel_div").hide(
													'slow');
											resetDropDownList("productCoverIdIP");
											$("#displayIrdInfoForIp_div").hide(
													'slow');
											
											if('${ipDeactivated}' == "false")
											{
												resetDropDownList("productCoverIdIP");
												clearField("ipNomFirstName");
												clearField("ipNomSurName");
												clearField("ipNomAge");
													
											}	

										}
										
										if ('${ipDeactivated}' == "true") {
											
											/* cuustomer deactivated from ip product  */
											
											$('input:checkbox[value=3]').attr(
													'disabled', true);
											
										}

									});
					


				} else {
					//disable IP product 
					$('input:checkbox[value=4]').attr('disabled', true);

				}

			} else if ('${is_xlCust}' == "false" && '${is_hpCust}' == "true"
					&& '${is_ipCust}' == "true") {

				// F,T,T

				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfo_div").hide();
				$("#displayIrdInfoForIp_div").show('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');

				$("#displayofferLevel_div").show('slow');
				loadDropDownList('productCoverIdIP',
						'${VO_CUSTOMER.productCoverIdIP}');
				$('#productCoverIdIP').attr("disabled", "disabled");

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				$("input[type=checkbox]")
						.click(
								function() {
									if ($('input:checkbox[value=2]').is(
											':checked') == true) {

										$("#displayIrdInfo_div").show('slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false) {

										$("#displayIrdInfo_div").hide('slow');
										resetDropDownList("insRelation");
										clearField("insRelFname");
										clearField("insRelSurname");
										clearField("insRelIrDoB");
										clearField("insRelAge");

									} else if ($('input:checkbox[value=4]').is(
											':checked') == true) {

										$("#displayIrdInfoForIp_div").show(
												'slow');
									} else if ($('input:checkbox[value=4]').is(
											':checked') == false) {

										$("#displayIrdInfo_div").hide('slow');
										clearField("ipNomFirstName");
										clearField("ipNomSurName");
										clearField("ipNomAge");

									}

								});

			}/* else if ('${is_xlCust}' == "true" && '${is_hpCust}' == "true"
										&& '${is_ipCust}' == "false") {
										// T,T,F
									
										
									} */
			else if ('${is_xlCust}' == "false" && '${is_hpCust}' == "true"
					&& '${is_ipCust}' == "false") {
				//F,T,F
				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfo_div").hide();
				$("#displayIrdInfoForIp_div").hide('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');

				/*  $("#displayofferLevel_div").show('slow');
				loadDropDownList('productCoverIdIP','${VO_CUSTOMER.productCoverIdIP}'); 
				$('#productCoverIdIP').attr("disabled","disabled");*/

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				$("input[type=checkbox]")
						.click(
								function() {
									if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {

										$("#displayIrdInfo_div").show('slow');

										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == true) {

										$("#displayIrdInfo_div").hide('slow');
										resetDropDownList("insRelation");
										clearField("insRelFname");
										clearField("insRelSurname");
										clearField("insRelIrDoB");
										clearField("insRelAge");
										$("#displayofferLevel_div")
												.show('slow');
										$("#displayIrdInfoForIp_div").show(
												'slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=4]').is(
													':checked') == true) {
										$("#displayIrdInfo_div").show('slow');
										$("#displayIrdInfoForIp_div").show(
												'slow');

										$("#displayofferLevel_div")
												.show('slow');

									}

									else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {

										$("#displayIrdInfo_div").hide('slow');
										$("#displayIrdInfoForIp_div").hide(
												'slow');
										resetDropDownList("insRelation");
										clearField("insRelFname");
										clearField("insRelSurname");
										clearField("insRelIrDoB");
										clearField("insRelAge");
										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");
									}

								});

				if ('${isPAReg}' == "true") {
					$("input[type=checkbox]")
							.click(
									function() {
										if ($('input:checkbox[value=2]').is(
												':checked') == true
												&& $('input:checkbox[value=4]')
														.is(':checked') == false) {

											$("#displayIrdInfo_div").show(
													'slow');

											$("#displayofferLevel_div").hide(
													'slow');
											resetDropDownList("productCoverIdIP");
										} else if ($('input:checkbox[value=2]')
												.is(':checked') == false
												&& $('input:checkbox[value=4]')
														.is(':checked') == true) {

											$("#displayIrdInfo_div").hide(
													'slow');
											resetDropDownList("insRelation");
											clearField("insRelFname");
											clearField("insRelSurname");
											clearField("insRelIrDoB");
											clearField("insRelAge");
											$("#displayofferLevel_div").show(
													'slow');
										} else if ($('input:checkbox[value=2]')
												.is(':checked') == true
												&& $('input:checkbox[value=4]')
														.is(':checked') == true) {
											$("#displayIrdInfo_div").show(
													'slow');
											$("#displayIrdInfoForIp_div").show(
													'slow');

											$("#displayofferLevel_div").show(
													'slow');

										}

										else if ($('input:checkbox[value=2]')
												.is(':checked') == false
												&& $('input:checkbox[value=4]')
														.is(':checked') == false) {

											$("#displayIrdInfo_div").hide(
													'slow');
											$("#displayIrdInfoForIp_div").hide(
													'slow');
											resetDropDownList("insRelation");
											clearField("insRelFname");
											clearField("insRelSurname");
											clearField("insRelIrDoB");
											clearField("insRelAge");

											$("#displayofferLevel_div").hide(
													'slow');
											resetDropDownList("productCoverIdIP");

										}

									});
				} else {
					//disable IP product 
					$('input:checkbox[value=4]').attr('disabled', true);
					$("#displayIrdInfoForIp_div").hide('slow');
					$("input[type=checkbox]")
							.click(
									function() {
										if ($('input:checkbox[value=2]').is(
												':checked') == true) {

											$("#displayIrdInfo_div").show(
													'slow');
										} else if ($('input:checkbox[value=2]')
												.is(':checked') == false) {

											$("#displayIrdInfo_div").hide(
													'slow');
										}
									});

				}

			}/* else if('${is_xlCust}' == "true" && '${is_hpCust}' == "false"
										&& '${is_ipCust}' == "true")
										{
										   T,F,T
										} */
			/* else if('${is_xlCust}' == "true" && '${is_hpCust}' == "false"
				&& '${is_ipCust}' == "true")
				{
				   F,T,T
				} */

			else if ('${is_xlCust}' == "false" && '${is_hpCust}' == "false"
					&& '${is_ipCust}' == "true") {

				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfo_div").hide();
				$("#displayIrdInfoForIp_div").show('slow');
				$("#saveButtons_div").show('slow');

				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');

				$('input:checkbox[value=3]').attr('disabled', true);
				$("#displayofferLevel_div").show('slow');
				loadDropDownList('productCoverIdIP',
						'${VO_CUSTOMER.productCoverIdIP}');
				$('#productCoverIdIP').attr("disabled", "disabled");

				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {
					var isDeductionModeShow = $("#is_DeductionMode").val();

					if (isDeductionModeShow == "true")
						$("#deduction").show();
					else
						$("#deduction").hide();
				}

				//For Ip Product Nominee Details
				$("#ipNomFirstName").val("${VO_CUSTOMER.ipNomFirstName}");
				$("#ipNomSurName").val("${VO_CUSTOMER.ipNomSurName}");
				$("#ipNomAge").val("${VO_CUSTOMER.ipNomAge}");

				$("input[type=checkbox]")
						.click(
								function() {
									if ($('input:checkbox[value=2]').is(
											':checked') == true) {

										$("#displayIrdInfo_div").show('slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false) {

										$("#displayIrdInfo_div").hide('slow');
										resetDropDownList("insRelation");
										clearField("insRelFname");
										clearField("insRelSurname");
										clearField("insRelIrDoB");
										clearField("insRelAge");

									}
								});

			} else {
				$("#displayCustInfo_div").show('slow');
				$("#displayIrdInfo_div").show('slow');
				loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
				loadRadioButton('gender', '${VO_CUSTOMER.gender}');
				loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');
				if ('${VO_CUSTOMER.purchasedProducts}' == '1') {
					$("#deduction").hide();
				} else {

					$("#deduction").show();
				}
				$("input[type=checkbox][id=productId]").click(
						function() {
							if ($(this).attr("checked")) {
								$("#saveButtons_div").show('slow');
							} else if (('${is_editable}' == "false")
									&& !($(this).attr("checked")))
								$("#saveButtons_div").hide('slow');
						});
			}
		} else if ('${custexists}' == "false") {
			
			$('#msisdn').attr('readonly', true);
			$("#search-icon").hide('slow');
			$("#displayProduct_div").show('slow');
			$("#displayCustInfo_div").show('slow');
			$("#displayIrdInfo_div").hide('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			$("#saveButtons_div").show('slow');
			$("#deductionMode").val("1");
			
			
			if ('${is_ipCust}' == "true")
				{
				  $("#displayIrdInfoForIp_div").show('slow');
					$('input:checkbox[value=3]').attr(
						'disabled', true);
					
					$("#ipNomFirstName").val('${VO_CUSTOMER.ipNomFirstName}');
					$("#ipNomSurName").val("${VO_CUSTOMER.ipNomSurName}");
					$("#ipNomAge").val("${VO_CUSTOMER.ipNomAge}");
					$("#ipInsMsisdn").val("${VO_CUSTOMER.ipInsMsisdn}");
				
					
					
				}
			if ('${is_xlCust}' == "true")
			{
			    //$("#displayIrdInfo_div").show('slow');
				
			 	loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');
				$("#insRelAge").val('${VO_CUSTOMER.insRelAge}');
				$("#insRelSurname").val("${VO_CUSTOMER.insRelSurname}");
				$("#insRelFname").val("${VO_CUSTOMER.insRelFname}");
				$("#insRelIrDoB").val('${VO_CUSTOMER.insRelIrDoB}');
			
				
				
			}
			
			loadRadioButton('gender', '${VO_CUSTOMER.gender}');

			if ('${isPAReg}' == "true") {
				//User have access to register IP 

				$("input[type=checkbox]")
						.click(
								function() {

									if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=3]').is(
													':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == true) {

										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").show('slow');
										$("#displayIrdInfoForIp_div").show(
												'slow');

										$('input:checkbox[value=3]').attr(
												'disabled', true);

										$("#displayofferLevel_div")
												.show('slow');

									} else if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=3]').is(
													':checked') == true
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").show('slow');
										$("#displayIrdInfoForIp_div").hide(
												'slow');
										$('input:checkbox[value=4]').attr(
												'disabled', true);
										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");
									} else if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=3]').is(
													':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").show('slow');
										$("#displayIrdInfoForIp_div").hide(
												'slow');
										$('input:checkbox[value=3]').attr(
												'disabled', false);
										$('input:checkbox[value=4]').attr(
												'disabled', false);
										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=3]').is(
													':checked') == true
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").hide('slow');
										$("#displayIrdInfoForIp_div").hide(
												'slow');
										$('input:checkbox[value=4]').attr(
												'disabled', true);
										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=3]').is(
													':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == true) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").hide('slow');
										$("#displayIrdInfoForIp_div").show(
												'slow');
										$('input:checkbox[value=3]').attr(
												'disabled', true);

										$("#displayofferLevel_div")
												.show('slow');
									}

									else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=3]').is(
													':checked') == false
											&& $('input:checkbox[value=4]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');

										$('input:checkbox[value=3]').attr(
												'disabled', false);
										$('input:checkbox[value=4]').attr(
												'disabled', false);
										$("#displayofferLevel_div")
												.hide('slow');
										resetDropDownList("productCoverIdIP");

										$("#displayIrdInfo_div").hide('slow');
										$("#displayIrdInfoForIp_div").hide(
												'slow');
									}
									
									if ('${ipDeactivated}' == "true") {
										
										/* cuustomer deactivated from ip product  */
										
										$('input:checkbox[value=3]').attr(
												'disabled', true);
										
									}

								});

			} else {
				//User dont have access to reg IP

				$('input:checkbox[value=4]').attr('disabled', true);
				if ('${ipDeactivated}' == "true") {
					
					/* cuustomer deactivated from ip product  */
					
					$('input:checkbox[value=3]').attr(
							'disabled', true);
					
					$("#displayIrdInfoForIp_div").hide(
					'slow');
					
					
				}
				
				
				$("input[type=checkbox]")
						.click(
								function() {
									if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=3]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").show('slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == true
											&& $('input:checkbox[value=3]').is(
													':checked') == true) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").show('slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=3]').is(
													':checked') == true) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").hide('slow');
									} else if ($('input:checkbox[value=2]').is(
											':checked') == false
											&& $('input:checkbox[value=3]').is(
													':checked') == false) {
										$("#displayCustInfo_div").show('slow');
										$("#displayIrdInfo_div").hide('slow');
									}
									
									if ('${ipDeactivated}' == "true") {
										
										/* cuustomer deactivated from ip product  */
										
										$('input:checkbox[value=3]').attr(
												'disabled', true);
										
									}
									
								});

			}

		}

		if ('${ipDeactivated}' == "true") {
			
			/* cuustomer deactivated from ip product  */
			
			$('input:checkbox[value=3]').attr(
					'disabled', true);
			$("#displayIrdInfoForIp_div").hide(
			'slow');
			
			
		}
		
		
		 if('${isXLreactive}' == "true")
		{
		
			$("#displayIrdInfo_div").hide('slow');
				$("input[type=checkbox]")
				.click(
						function() {
							
							
							if ($('input:checkbox[value=2]').is(':checked') == false
							) {
								$("#displayIrdInfo_div").hide('slow');
								
							}
							
							if ($('input:checkbox[value=2]').is(':checked') == true
							) {
								
								$("#displayIrdInfo_div").show('slow');
								
							}
						}
						);
		}	
		
	
	</script>
</body>
</html>