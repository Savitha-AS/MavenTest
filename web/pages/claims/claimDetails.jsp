<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<script src="${pageContext.request.contextPath}/appScripts/claims.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/appScripts/utils.js"
	type="text/javascript"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/util.js"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/dwr/interface/validateCustomerMSISDN.js"></script>

<script src="${pageContext.request.contextPath}/appScripts/customer.js"
	type="text/javascript"></script>	
<script>
	/* $(function() {
		$( "button, input:button, input:submit").button();
	}); */

	$(function() {
		$("#saveBtn").click(
				function() {

					if ('${claimedPerson}' == 'C') {
						if (!validateModifyBeneficiary(false)) {
							checkForExistingMSISDN('${VO_CUSTOMER.custId}');
						}
					} else if ('${claimedPerson}' == 'I') {
						if (!validateModifyBeneficiary(true)) {
							loadMSISDNCheck(false, false);
						}
					} else {

						/* if (!validateModifyBeneficiary("${is_hpCust}")&& ("${is_ipCust}")) {
							checkForExistingMSISDN('${VO_CUSTOMER.custId}');
						} */
						if (!validateModifyBeneficiary('${is_hpCust}',
								'${is_xlCust}', '${is_ipCust}')) {
							
							resetErrors();
										
							
							var msisdnCodes = $("#msisdnCodes").val();
							
							if('${is_xlCust}' == "true")
							{
							var inMsisdn=$("#insMsisdn").val();;
								if(inMsisdn.length >0)
								{
									
									inMsisdn = inMsisdn.substr(0, 3);
									
									if(msisdnCodes.search(inMsisdn)==-1)
									  setError("insMsisdn", 33, getFieldText("insMsisdn"));
									
									if (showValidationErrors("validationMessages_parent"))
										return true;
								}
								
							}	
							
							if('${is_ipCust}' == "true")
							{
							var ipMsisdn=$("#ipInsMsisdn").val();
								
								if(ipMsisdn.length > 0)
								{
									ipMsisdn = ipMsisdn.substr(0, 3);
									if(msisdnCodes.search(ipMsisdn)==-1)
										 setError("ipInsMsisdn", 33, getFieldText("ipInsMsisdn"));
									if (showValidationErrors("validationMessages_parent"))
										return true;
								}
							}
							
							
							if (showValidationErrors("validationMessages_parent"))
								return true;
							else
								{
								
									checkForExistingMSISDN('${VO_CUSTOMER.custId}');
								}
							
							
						}
					}
				});

		$("body").keypress(function(e) {
			if (e.keyCode == '13') {
				$("#saveBtn").click();
				return false;
			}
		});
	});

	$(function() {
		$("#clearBtn")
				.click(
						function() {
							if ('${claimedPerson}' == 'C') {
								clearField("fname");
								clearField("sname");
								//clearField("msisdn");
								clearField("dob");
								clearField("age");
								resetRadioButtons("gender");
							} else if ('${claimedPerson}' == 'I') {
								resetDropDownList("relation");
								clearField("insRelFname");
								clearField("insRelSurname");
								clearField("insRelIrDoB");
								clearField("insRelAge");
								clearField("insMsisdn");
								clearField("ipNomFirstName");
								clearField("ipNomSurName");
								clearField("ipNomAge");
								clearField("ipInsMsisdn");
							} else {
								clearField("fname");
								clearField("sname");
								//clearField("msisdn");
								clearField("dob");
								clearField("age");
								resetRadioButtons("gender");
								/* if ('${is_hpCust}' == "false" && '${is_ipCust}' == "false") */
								if (('${is_hpCust}' == "true"
										&& '${is_xlCust}' == "true" && '${is_ipCust}' == "true")
										|| ('${is_xlCust}' == "true" && '${is_ipCust}' == "true")
										|| ('${is_xlCust}' == "true" && '${is_hpCust}' == "true")
										|| ('${is_xlCust}' == "true")
										|| ('${is_ipCust}' == "true")) {
									clearField("ipNomFirstName");
									clearField("ipNomSurName");
									clearField("ipNomAge");
									clearField("ipInsMsisdn");
									resetDropDownList("relation");
									clearField("insRelFname");
									clearField("insRelSurname");
									clearField("insRelIrDoB");
									clearField("insRelAge");
									clearField("insMsisdn");

								}
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
					"",
					function(result) {

						if (result) {
							$("input[name='gender']").removeAttr("disabled");
							var snm = '${claimedPerson}' == 'A' ? 1 : 0;
							document.getElementById("modifyClaimDetailsFrm").action = "${pageContext.request.contextPath}/claims.controller.claimInsurance.task?custId=${VO_CUSTOMER.custId}&snm="
									+ snm;
							document.getElementById("modifyClaimDetailsFrm")
									.submit();
						}
					});
		}
	}
</script>
<style type="text/css">
.data_label {
	width: 175px;
}
</style>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="modifyClaimDetailsFrm">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>
      
      


		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
				<c:choose>
					<c:when test="${claimedPerson eq 'A'}">
						<c:set var="pid" value="43" />
					</c:when>
					<c:otherwise>
						<c:set var="pid" value="42" />
					</c:otherwise>
				</c:choose>
				<input type="hidden" id="pageId" value="${pid}" />
				
				<input type="hidden"
					id="msisdnCodes" name="msisdnCodes"
					value="${msisdnCodes}" />
				
				<div class="pagetitle">
					<c:choose>
						<c:when test="${claimedPerson eq 'A'}">
							<h3>
								<spring:message code="modcustomer.header.text" />
							</h3>
						</c:when>
						<c:otherwise>
							<h3>
								<spring:message code="modclaimdetails.header.text" />
							</h3>
						</c:otherwise>
					</c:choose>
				</div>
				<div style="text-align: right; margin-right: 5px;">
					<spring:message code="platform.text.mandatory" />
				</div>
				<br />
				<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

				<div id="displayProduct_div">
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
									<c:forEach var="products" items="${productsList}">
										<c:if test="${products.productId ne 1}">
											<input type="checkbox" id="productId" name="productId"
												value="${products.productId}" disabled="disabled" />&nbsp;${products.productName}&nbsp;&nbsp;
					                      </c:if>
									</c:forEach>
								</div>
							</div>
						</div>
						<br />
					</fieldset>
					<br />
				</div>
				<div id="displayofferLevel_div">
					<fieldset>
						<legend id="label_regcustomer_subheader1">
							<spring:message code="platform.text.offerCoverDetailsIP"></spring:message>
						</legend>

						<div class="data_container_color1">
							<div class="data_label" id="label_productCoverIdIP">
								<spring:message code="platform.text.offerCoverLevel"></spring:message>
								&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<select id="productCoverIdIP" name="productCoverIdIP"
									style="width: 110px;" disabled="disabled">
									<option value=""></option>
									<c:forEach var="offers" items="${productCoverList}">
										<option value="${offers.productCoverId}">GHC
											<fmt:formatNumber value="${offers.productCover}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<br />
					</fieldset>
				</div>
				<br/>
				<fieldset>
					<legend id="label_regcustomer_subheader1">
						<spring:message code="regcustomer.subheader1.text"></spring:message>
					</legend>

					<div class="data_container_color1">
						<div class="data_label" id="label_fname">
							<spring:message code="platform.text.firstname"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<c:choose>
							<c:when test="${claimedPerson ne 'I'}">
									<input type="text" id="fname" name="fname"
										style="width: 110px;" value="${VO_CUSTOMER.fname}"
										maxlength="100" />
								</c:when>
								<c:otherwise>
									<input type="text" id="fname" name="fname"
										value="${VO_CUSTOMER.fname}"
										style="text-align: left; width: 110px; border: 0px"
										class="labelTxtBox_color1" readonly="readonly" maxlength="100" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<div class="data_container_color2">
						<div class="data_label" id="label_sname">
							<spring:message code="platform.text.surname"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<div class="" id="div_id_2">
								<c:choose>
									<c:when test="${claimedPerson ne 'I'}">
										<input type="text" id="sname" name="sname"
											style="width: 110px" value="${VO_CUSTOMER.sname}"
											maxlength="50" />
									</c:when>
									<c:otherwise>
										<input type="text" id="sname" name="sname"
											value="${VO_CUSTOMER.sname}"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly" maxlength="50" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>

					<div class="data_container_color1">
						<div class="data_label" id="label_msisdn">
							<spring:message code="platform.text.msisdn"></spring:message>
							&nbsp;:
						</div>
						<div class="data_text">
							<div class="" id="div_id_3" style="float: left;">
								<input type="text" id="msisdn" name="msisdn"
									value="${VO_CUSTOMER.msisdn}"
									style="text-align: left; width: 110px; border: 0px"
									class="labelTxtBox_color1" readonly="readonly" maxlength="10"
									size="9" />
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
								<c:when test="${claimedPerson ne 'I'}">
									<input type="text" id="dob" name="dob" style="width: 100px"
										value="${VO_CUSTOMER.dob}" readonly="readonly" />
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
									<input type="text" id="dob" name="dob"
										value="${VO_CUSTOMER.dob}"
										style="text-align: left; width: 110px; border: 0px"
										class="labelTxtBox_color2" readonly="readonly" />
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
								<c:when test="${claimedPerson ne 'I'}">
									<input type="text" id="age" name="age" style="width: 20px"
										value="${VO_CUSTOMER.age}" maxlength="2" />
								</c:when>
								<c:otherwise>
									<input type="text" id="age" name="age"
										value="${VO_CUSTOMER.age}" style="width: 20px; border: 0px"
										class="labelTxtBox_color1" readonly="readonly" maxlength="2" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="data_container_color2">
						<div class="data_label" id="label_gender">
							<spring:message code="platform.text.gender"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<c:choose>
								<c:when test="${claimedPerson ne 'I'}">
									<input type="radio" name="gender" id="gender" value="M" /> &nbsp;<spring:message
										code="platform.text.male"></spring:message>
					 		&nbsp;&nbsp;
							<input type="radio" name="gender" id="gender" value="F" /> &nbsp;<spring:message
										code="platform.text.female"></spring:message>
								</c:when>
								<c:otherwise>
									<input type="radio" name="gender" id="gender" value="M"
										class="labelTxtBox_color2" disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.male"></spring:message>
					 		&nbsp;&nbsp;
							<input type="radio" name="gender" id="gender" value="F"
										class="labelTxtBox_color2" disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.female"></spring:message>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<c:if test="${isClaimsManagerLogin == 'true'}">

						<div class="data_container_color1">
							<div class="data_label" id="label_customerID">
								<spring:message code="platform.text.customer.id"></spring:message>
								&nbsp;:&nbsp; 
							</div>
							<div class="data_text">

								<input type="text" id="customerID" name="customerID"
									style="text-align: left; width: 110px; border: 0px"
									class="labelTxtBox_color1" readonly="readonly"
									value="${VO_CUSTOMER.custId}" />
							</div>
						</div>

						<div class="data_container_color2">
							<div class="data_label" id="label_lastModifiedDate">
								<spring:message code="platform.text.last.modified.date"></spring:message>
								&nbsp;:&nbsp; 
							</div>
							<div class="data_text">

								<input type="text" id="lastModifiedDate" name="lastModifiedDate"
									style="text-align: left; width: 110px; border: 0px"
									class="labelTxtBox_color2" readonly="readonly"
									value="${VO_CUSTOMER.modifiedDate}" />
							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_lastModifiedBy">
								<spring:message code="platform.text.last.modified.by"></spring:message>
								&nbsp;:&nbsp; 
							</div>
							<div class="data_text">

								<input type="text" id="lastModifiedBy" name="lastModifiedBy"
									style="text-align: left; width: 110px; border: 0px"
									class="labelTxtBox_color1" readonly="readonly"
									value="${VO_CUSTOMER.modifiedBy}" />
							</div>
						</div>
					</c:if>
					
					<div class="data_container_color1" id="div_impliedAge" >
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
				<div id="insuredRelativeDetails_div">
					<%-- <c:if
						test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }"> --%>
						<fieldset>
							<legend id="label_regcustomer_subheader2">
								<spring:message code="regcustomer.subheader2.text"></spring:message>
							</legend>

							<div class="data_container_color1">
								<div class="data_label" id="label_selectRelation">
									<spring:message code="regcustomer.text.rel"></spring:message>
									:<span class="mandatoryStar">*</span>
								</div>
								<div class="data_text">
									<div id="div_id_15">
										<c:choose>
											<c:when test="${claimedPerson ne 'C'}">

												<select id="relation" name="relation" style="width: 110px;">
													<option></option>
													<c:forEach var="relationTypes"
														items="${RELATIONSHIP_TYPE_LIST}">
														<c:if test="${relationTypes != 'Others'}">
															<option
																<c:if test="${relationTypes eq VO_CUSTOMER.insRelation}">selected</c:if>
																value="${relationTypes}">${relationTypes}</option>

														</c:if>

													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<input type="text" id="relation" name="relation"
													style="text-align: left; width: 130px; border: 0px"
													class="labelTxtBox_color1" readonly="readonly"
													value="${VO_CUSTOMER.insRelation}" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="data_container_color2">
								<div class="data_label" id="label_insRelFname">
									<spring:message code="platform.text.firstname"></spring:message>
									&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
								</div>
								<div class="data_text">
									<div class="" id="div_id_13">
										<c:choose>
											<c:when test="${claimedPerson ne 'C'}">
												<input type="text" id="insRelFname" name="insRelFname"
													style="width: 130px;" value="${VO_CUSTOMER.insRelFname}"
													maxlength="100" />
											</c:when>
											<c:otherwise>
												<input type="text" id="insRelFname" name="insRelFname"
													style="text-align: left; width: 110px; border: 0px"
													class="labelTxtBox_color2" readonly="readonly"
													value="${VO_CUSTOMER.insRelFname}" maxlength="100" />
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="data_container_color1">
								<div class="data_label" id="label_insRelSurname">
									<spring:message code="platform.text.surname"></spring:message>
									&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
								</div>
								<div class="data_text">
									<div class="" id="div_id_14">
										<c:choose>
											<c:when test="${claimedPerson ne 'C'}">
												<input type="text" id="insRelSurname" name="insRelSurname"
													style="width: 110px;" value="${VO_CUSTOMER.insRelSurname}"
													maxlength="50" />
											</c:when>
											<c:otherwise>
												<input type="text" id="insRelSurname" name="insRelSurname"
													style="text-align: left; width: 110px; border: 0px"
													class="labelTxtBox_color1" readonly="readonly"
													value="${VO_CUSTOMER.insRelSurname}" maxlength="50" />
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
										<c:when test="${claimedPerson ne 'C'}">
											<input type="text" id="insRelIrDoB" name="insRelIrDoB"
												value="${VO_CUSTOMER.insRelIrDoB}" style="width: 100px"
												readonly="readonly" />
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
														calculateAge(
																'insRelIrDoB',
																'insRelAge');
													},
													dateFormat : "%d/%m/%Y"
												});
											</script>
										</c:when>
										<c:otherwise>

											<input type="text" id="insRelIrDoB" name="insRelIrDoB"
												value="${VO_CUSTOMER.insRelIrDoB}"
												style="text-align: left; width: 100px; border: 0px"
												class="labelTxtBox_color2" />
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="data_container_color1">
								<div class="data_label" id="label_insRelAge">
									<spring:message code="platform.text.age"></spring:message>
									&nbsp;:
								</div>
								<div class="data_text" style="text-align: left;">
									<c:choose>
										<c:when test="${claimedPerson ne 'C'}">
											<input type="text" id="insRelAge" name="insRelAge"
												style="width: 20px" value="${VO_CUSTOMER.insRelAge}"
												maxlength="2" />
										</c:when>
										<c:otherwise>
											<input type="text" id="insRelAge" name="insRelAge"
												style="text-align: left; width: 20px; border: 0px"
												class="labelTxtBox_color1" value="${VO_CUSTOMER.insRelAge}"
												maxlength="2"
												onfocus="javascript:calculateAge('insRelIrDoB','insRelAge');" />
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
									<c:when test="${claimedPerson ne 'C'}">
										<input type="text" id="insMsisdn" name="insMsisdn"
											style="width: 110px;" value="${VO_CUSTOMER.insMsisdn}"
											maxlength="10" />
									</c:when>
									<c:otherwise>
										<input type="text" id="insMsisdn" name="insMsisdn"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color1" readonly="readonly"
											value="${VO_CUSTOMER.insMsisdn}" maxlength="10" />
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
					<%-- </c:if> --%>
					<!-- </div> -->
				</div>
				<div id="displayIrdInfoForIp_div">
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

								<c:choose>
									<c:when test="${claimedPerson ne 'C'}">
										<input type="text" id="ipNomFirstName" name="ipNomFirstName"
											style="width: 130px;" value="${VO_CUSTOMER.ipNomFirstName}"
											maxlength="100" />
									</c:when>
									<c:otherwise>
										<input type="text" id="ipNomFirstName" name="ipNomFirstName"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color1" readonly="readonly"
											value="${VO_CUSTOMER.ipNomFirstName}" maxlength="100" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="data_container_color2">
							<div class="data_label" id="label_ipNomSurName">
								<spring:message code="platform.text.surname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<c:choose>
									<c:when test="${claimedPerson ne 'C'}">
										<input type="text" id="ipNomSurName" name="ipNomSurName"
											style="width: 130px;" value="${VO_CUSTOMER.ipNomSurName}"
											maxlength="100" />
									</c:when>
									<c:otherwise>
										<input type="text" id="ipNomSurName" name="ipNomSurName"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly"
											value="${VO_CUSTOMER.ipNomSurName}" maxlength="50" />
									</c:otherwise>
								</c:choose>


							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_ipNomAge">
								<spring:message code="platform.text.age"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text" style="text-align: left">

								<c:choose>
									<c:when test="${claimedPerson ne 'C'}">
										<input type="text" id="ipNomAge" name="ipNomAge"
											style="width: 20px;" value="${VO_CUSTOMER.ipNomAge}"
											maxlength="100" />
									</c:when>
									<c:otherwise>
										<input type="text" id="ipNomAge" name="ipNomAge"
											style="text-align: left; width: 20px; border: 0px"
											class="labelTxtBox_color1" readonly="readonly"
											value="${VO_CUSTOMER.ipNomAge}" maxlength="2" />
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
									<c:when test="${claimedPerson ne 'C'}">
										<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
											style="width: 110px;" value="${VO_CUSTOMER.ipInsMsisdn}"
											maxlength="10" />
									</c:when>
									<c:otherwise>
										<input type="text" id="ipInsMsisdn" name="ipInsMsisdn"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color1" readonly="readonly"
											value="${VO_CUSTOMER.ipInsMsisdn}" maxlength="10" />
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
				</div>
				<br />
				<div style="text-align: center;">
					<ul class="btn-wrapper">
						<li class="btn-inner" id="saveBtn"><span><spring:message
									code="platform.button.savechanges" /></span></li>
					</ul>
					<ul class="btn-wrapper">
						<li class="btn-inner" id="clearBtn"><span><spring:message
									code="platform.button.clear" /></span></li>
					</ul>
				</div>
				<input type="hidden" id="claimedPerson" name="claimedPerson"
					value="${claimedPerson}" /> <br /> <input type="hidden"
					id="claimedPerson" name="claimedPerson" value="${claimedPerson}" />
				<br />
			</div>
		</div>
		<!-- </div> -->
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
	
	if ("${claimedPerson}" =="C") {
		$('input[name="benMsisdnYesNo"]').attr('disabled', 'disabled');
		$('input[name="benInsMsisdnYesNo"]').attr('disabled', 'disabled');
	}
	
	
	
	
	
		if ('${VO_CUSTOMER.purchasedProducts}' == '1,2,3') {
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").hide('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			loadCheckBox("productId", '3');
		} else if ('${VO_CUSTOMER.purchasedProducts}' == '1,2,4') {
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			loadCheckBox("productId", '4');
		} else if ('${VO_CUSTOMER.purchasedProducts}' == '1,2') {
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").hide('slow');

		} else if ('${VO_CUSTOMER.purchasedProducts}' == '1,2,3,4') {
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			$("#insuredRelativeDetails_div").show('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');

			$('input:checkbox[value=3]').attr('disabled', true);

			loadCheckBox("productId", '4');
			$("#insuredRelativeDetails_div").show('slow');
		} else if ('${VO_CUSTOMER.purchasedProducts}' == '4') {
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			$("#insuredRelativeDetails_div").hide('slow');
			loadCheckBox("productId", '4');
		} else if ('${VO_CUSTOMER.purchasedProducts}' == '3') {
			$("#displayofferLevel_div").hide('slow');
			$("#insuredRelativeDetails_div").hide('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '3');
		} else if ('${VO_CUSTOMER.purchasedProducts}' == '3,4') {
			$("#displayofferLevel_div").show('slow');
			$("#insuredRelativeDetails_div").hide('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			loadCheckBox("productId", '4');
		}

		else {
			loadCheckBox("productId", '${VO_CUSTOMER.purchasedProducts}');
		}
		loadRadioButton('gender', '${VO_CUSTOMER.gender}');
		loadCheckBox('productId', '${VO_CUSTOMER.purchasedProducts}');
		loadDropDownList('productCoverIdIP', '${VO_CUSTOMER.productCoverIdIP}');

		if (('${is_hpCust}' == "true" && '${is_xlCust}' == "true" && '${is_ipCust}' == "true")
				|| ('${is_xlCust}' == "true" && '${is_ipCust}' == "true")
				|| ('${is_xlCust}' == "true" && '${is_hpCust}' == "true")
				|| ('${is_xlCust}' == "true")) {
			loadDropDownList('relation', '${VO_CUSTOMER.insRelation}');

		}
		if ('${is_ipCust}' == "true") {
			$("#displayIrdInfoForIp_div").show('slow');
		} else {
			$("#displayIrdInfoForIp_div").hide('slow');
		}
		/*if('${VO_CUSTOMER.productId}' == '4'){
				$("#displayofferLevel_div").show('slow');
		 }else if('${VO_CUSTOMER.productId}' != '4'){
			 $("#displayofferLevel_div").hide('slow');
			
		 } */

		if ('${is_xlCust}' == "true" && '${is_hpCust}' == "false"
				&& '${is_ipCust}' == "true") {
			$('input:checkbox[value=3]').attr('checked', false);
			/*  $('input:checkbox[value=3]').attr('disabled', true); */

		}
		 
		 
	</script>
</body>
</html>

