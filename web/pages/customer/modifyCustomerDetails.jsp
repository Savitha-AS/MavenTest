<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
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
<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
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
		$("#backBtn").click(function() {
			window.location = "customer.page.searchCustomer.task";
		});

		$("body").keypress(function(e) {
			if (e.keyCode == '13') {
				$("#backBtn").click();
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
	}
</script>
<style type="text/css">
.data_label {
	width: 190px;
}
</style>
</head>
<body onload="keepSubMenuSelected()">
	<form method="post" id="updateCustomerFrm">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>

		<!-- View Content -->
		<div class="content-container">
			<div id="box" style="width: 100%;">
				<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
				<input type="hidden" id="pageId" value="17" />
				<div class="pagetitle">
					<h3>
						<spring:message code="modcustomer.header.text" />
					</h3>
				</div>
				<div style="text-align: right; margin-right: 5px;">
					<spring:message code="platform.text.mandatory" />
				</div>
				<br />
				<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
				
					<div id="message_div" style="text-align: center; color: red;">
						<spring:message code="platform.code.deregcust"></spring:message>
					</div>

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
								<c:when test="${is_editable eq true}">
									<input type="text" id="fname" name="fname"
										style="width: 110px;" value="${VO_CUSTOMER.fname}"
										maxlength="100" />
								</c:when>
								<c:otherwise>
									<input type="text" id="fname" name="fname"
										value="${VO_CUSTOMER.fname}"
										style="text-align: left; width: 110px; border: 0px"
										maxlength="100" readonly="readonly" class="labelTxtBox_color1" />
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
									<c:when test="${is_editable eq true}">
										<input type="text" id="sname" name="sname"
											style="width: 110px" value="${VO_CUSTOMER.sname}"
											maxlength="50" />
									</c:when>
									<c:otherwise>
										<input type="text" id="sname" name="sname"
											value="${VO_CUSTOMER.sname}"
											style="text-align: left; width: 110px; border: 0px"
											maxlength="50" readonly="readonly" class="labelTxtBox_color2" />
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
								<c:choose>
									<c:when test="${is_editable eq true}">
										<input type="text" id="msisdn" name="msisdn"
											value="${VO_CUSTOMER.msisdn}" maxlength="10" size="10" />
									</c:when>
									<c:otherwise>
										<input type="text" id="msisdn" name="msisdn"
											value="${VO_CUSTOMER.msisdn}" maxlength="10" size="10"
											readonly="readonly" class="labelTxtBox_color1"
											style="text-align: left; width: 110px; border: 0px" />
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
								<c:when test="${is_editable eq true}">
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
										value="${VO_CUSTOMER.dob}" readonly="readonly"
										class="labelTxtBox_color2"
										style="text-align: left; width: 110px; border: 0px" />
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
								<c:when test="${is_editable eq true}">
									<input type="text" id="age" name="age" style="width: 20px"
										value="${VO_CUSTOMER.age}" maxlength="2" />
								</c:when>
								<c:otherwise>
									<input type="text" id="age" name="age"
										value="${VO_CUSTOMER.age}" maxlength="2" readonly="readonly"
										class="labelTxtBox_color1"
										style="text-align: left; width: 110px; border: 0px" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="data_container_color2">
						<div class="data_label">
							<spring:message code="platform.text.gender"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<c:choose>
								<c:when test="${is_editable eq true}">
									<input type="radio" name="gender" id="gender" value="M" /> &nbsp;<spring:message
										code="platform.text.male"></spring:message>
			 		&nbsp;&nbsp;
					<input type="radio" name="gender" id="gender" value="F" /> &nbsp;<spring:message
										code="platform.text.female"></spring:message>
								</c:when>
								<c:otherwise>
									<input type="radio" name="gender" id="gender" value="M"
										disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.male"></spring:message>
			 		&nbsp;&nbsp;
					<input type="radio" name="gender" id="gender" value="F"
										disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.female"></spring:message>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div id="implied_age_div"class="data_container_color1">
						<div class="data_label" id="label_age">
							<spring:message code="platform.text.impliedAge"></spring:message>
							&nbsp;:
						</div>
						<div class="data_text">
							<c:choose>
								<c:when test="${is_editable eq true}">
									<input type="text" id="age" name="age" style="width: 20px"
										value="${VO_CUSTOMER.impliedAge}" maxlength="2" />
								</c:when>
								<c:otherwise>
									<input type="text" id="age" name="age"
										value="${VO_CUSTOMER.impliedAge}" maxlength="2" readonly="readonly"
										class="labelTxtBox_color1"
										style="text-align: left; width: 110px; border: 0px" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<br />
				</fieldset>

				<div id="insuredRelativeDetails_div" style="display: none">
					<br/>
					<fieldset>
						<legend id="label_regcustomer_subheader2">
							<spring:message code="regcustomer.subheader2.text"></spring:message>
						</legend>

						<div class="data_container_color1">
							<div class="data_label" id="label_selectRelation">
								<spring:message code="regcustomer.text.rel"></spring:message>
								&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<div id="div_id_15">
									<c:choose>
										<c:when test="${is_editable eq true}">
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
															<c:if test="${relationTypes eq VO_CUSTOMER.insRelation}">
																<option value="${relationTypes}">${relationTypes}</option>
															</c:if>
														</c:when>
														<c:otherwise>
															<option value="${relationTypes}">${relationTypes}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<input type="text" id="insRelation" name="insRelation"
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
										<c:when test="${is_editable eq true}">
											<input type="text" id="insRelFname" name="insRelFname"
												style="width: 110px;" value="${VO_CUSTOMER.insRelFname}"
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
										<c:when test="${is_editable eq true}">
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
									<c:when test="${is_editable eq true}">
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
													calculateAge('insRelIrDoB',
															'insRelAge');
												},
												dateFormat : "%d/%m/%Y"
											});
										</script>
									</c:when>
									<c:otherwise>
										<input type="text" id="insRelIrDoB" name="insRelIrDoB"
											value="${VO_CUSTOMER.insRelIrDoB}" readonly="readonly"
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
									<c:when test="${is_editable eq true}">
										<input type="text" id="insRelAge" name="insRelAge"
											style="width: 20px" value="${VO_CUSTOMER.insRelAge}"
											maxlength="2" />
									</c:when>
									<c:otherwise>
										<input type="text" id="insRelAge" readonly="readonly"
											name="insRelAge"
											style="text-align: left; width: 20px; border: 0px"
											class="labelTxtBox_color1" value="${VO_CUSTOMER.insRelAge}"
											maxlength="2"
											onfocus="javascript:calculateAge('insRelIrDoB','insRelAge');" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<div class="data_container_color2">
							<div class="data_label" id="label_insMsisdn">
								<spring:message code="platform.text.msisdn"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text" style="text-align: left;">
								<c:choose>
									<c:when test="${is_editable eq true}">
										<input type="text" id="insMsisdn" name="insMsisdn"
											value="${VO_CUSTOMER.insMsisdn}" maxlength="10" size="10" />
									</c:when>
									<c:otherwise>
										<input type="text" id="insMsisdn" name="insMsisdn"
											value="${VO_CUSTOMER.insMsisdn}" maxlength="10" size="10"
											readonly="readonly" class="labelTxtBox_color2"
											style="text-align: left; width: 110px; border: 0px" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<br />
					</fieldset>
				</div>
				<br />

				<fieldset>
					<legend id="label_regcustomer_subheader2">
						<spring:message code="regcustomer.subheader5.text"></spring:message>
					</legend>

					<div class="data_container_color1">
						<div class="data_label" id="label_deductionMode">
							<spring:message code="regcustomer.text.deductionMode"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>

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
					<br />
				</fieldset>
				<br />
				<%-- <div id="displayProduct_div">
					<fieldset>
						<legend id="label_regcustomer_subheader1">
							<spring:message code="regcustomer.subheader4.text"></spring:message>
						</legend>
						<div class="data_container_color1">
							<div class="data_label" id="label_productId">
								<spring:message code="products.text.subscribed"></spring:message>
								&nbsp;:&nbsp;
							</div>
							<div class="data_text">
								<div id="div_id_2" style="float: left;">
									<c:forEach var="products" items="${productsList}">
										<c:if
											test="${fn:containsIgnoreCase(VO_CUSTOMER.productId, products.productId)}">
											<input type="checkbox" id="productId" name="productId"
												value="${products.productId}" />&nbsp;${products.productName}&nbsp;&nbsp;													
												</c:if>
									</c:forEach>
								</div>
							</div>
						</div>
						<br />
					</fieldset>
					<br />
				</div> --%>
				
				<div id="displayProduct_div">
					<fieldset>
						<legend id="label_regcustomer_subheader1">
							<spring:message code="regcustomer.subheader4.text"></spring:message>
						</legend>
						<div class="data_container_color1">
							<div class="data_label" id="label_productId">
								<spring:message code="products.text.subscribed"></spring:message>
								&nbsp;:&nbsp;
							</div>
							<div class="data_text">
								<div class="" id="div_id_14">
									<c:choose>
										<c:when test="${is_editable eq true}">
											<input type="text" id="productId" name="productId"
												style="width: 110px;" value="${VO_CUSTOMER.offerName}"
												maxlength="50" />
										</c:when>
										<c:otherwise>
											<input type="text" id="productId" name="productId"
												style="text-align: left; width: 110px; border: 0px"
												class="labelTxtBox_color1" readonly="readonly"
												value="${VO_CUSTOMER.offerName}" maxlength="50" />
										</c:otherwise>
									</c:choose>

								</div>
							</div>
						</div>
						<br />
					</fieldset>
					<br />
				</div>
				<!-- <br /> -->

				<div id="displayofferLevel_div">
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
									style="width: 110px;">
									<option value=""></option>
									<c:forEach var="offers" items="${productCoverList}">
										<option value="${offers.productCoverId}">
											GHC
											<fmt:formatNumber value="${offers.productCover}" />
										</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<br />
					</fieldset>
				</div>
				
				<div id="displayIrdInfoForIp_div" style="display: none">
					<br/>
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
												<c:when test="${is_editable eq true}">
													<input type="text" id="ipNomFname"
														name="ipNomFname" style="width: 110px" maxlength="50" />
												</c:when>
												<c:otherwise>
														
															<input type="text" id="ipNomFname" name="ipNomFname"
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
												<c:when test="${is_editable eq true}">
													<input type="text" id="ipNomSname" name="ipNomSname"
														style="width: 110px" maxlength="50" />
												</c:when>
												<c:otherwise>
														<input type="text" id="ipNomAge" name="ipNomAge"
																style="text-align: left; width: 110px; border: 0px"
																class="labelTxtBox_color2" readonly="readonly"
															value="${VO_CUSTOMER.ipNomSurName}" maxlength="100" />

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
											<c:when test="${is_editable eq true}">
											<input type="text" id="ipNomAge" name="ipNomAge"
														style="width: 110px" maxlength="50" />
												
											</c:when>
													<c:otherwise>
														<input type="text" id="ipNomAge" name="ipNomAge"
																style="text-align: left; width: 110px; border: 0px"
																class="labelTxtBox_color1" readonly="readonly"
															value="${VO_CUSTOMER.ipNomAge}" maxlength="100" />

													</c:otherwise>
										</c:choose>
							</div>
						</div>
						<div class="data_container_color2">
							<div class="data_label" id="label_insIPMsisdn">
								<spring:message code="platform.text.msisdn"></spring:message>
								&nbsp;:
							</div>
							<div class="data_text" style="text-align: left;">
								<c:choose>
									<c:when test="${is_editable eq true}">
										<input type="text" id="insIPMsisdn" name="insIPMsisdn"
											value="${VO_CUSTOMER.ipInsMsisdn}" maxlength="10" size="10" />
									</c:when>
									<c:otherwise>
										<input type="text" id="insIPMsisdn" name="insIPMsisdn"
											value="${VO_CUSTOMER.ipInsMsisdn}" maxlength="10" size="10"
											readonly="readonly" class="labelTxtBox_color2"
											style="text-align: left; width: 110px; border: 0px" />
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<br />
					</fieldset>
					<br/>
				</div>
				
				<div id="displayChangesPerformed_div">
				<fieldset>
						<legend id="label_regcustomer_subheader3">
							<spring:message code="summaryDetailsChanges.subheader1.text"></spring:message>
						</legend>


						<div
							style="overflow-x: auto; height: auto; margin-left: 5px; margin-right: 5px; width: 676px">
							<display:table id="summaryDetailsChangesList"
								name="summaryDetailsChangesList" excludedParams="" 
								style="width:690px; height:100%;" cellspacing="0" cellpadding="0">

								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER" 
									titleKey="summaryDetailsChanges.text.date"
									property="modifiedDate" maxLength="30">
									
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER" 
									titleKey="summaryDetailsChanges.text.modifyBy"
									property="modifiedBy" maxLength="30">
								</display:column>

								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"  
									titleKey="summaryDetailsChanges.text.msisdn"
									property="msisdn" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newFname"
									property="newFname" maxLength="60">
								</display:column>

								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldFname"
									property="oldFname" maxLength="60">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newSname"
									property="newSname" maxLength="30">
								</display:column>
								
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldSname"
									property="oldSname" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newAge"
									property="newAge" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldAge"
									property="oldAge" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newDob"
									property="newDob" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldDob"
									property="oldDob" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newGender"
									property="newGender" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldGender"
									property="oldGender" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIrdFname"
									property="newInsRelFname" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIrdFname"
									property="oldInsRelFname" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIrdSname"
									property="newInsRelSurname" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIrdSname"
									property="oldInsRelSurname" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIrdAge"
									property="newInsRelAge" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIrdAge"
									property="oldInsRelAge" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIrdDob"
									property="newInsRelIrDoB" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIrdDob"
									property="oldInsRelIrDoB" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIrdMsisdn"
									property="newInsMsisdn" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIrdMsisdn"
									property="oldInsMsisdn" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newCustRelationship"
									property="newCustRelationship" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldCustRelationship"
									property="oldCustRelationship" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIPFname"
									property="newIpNomFirstName" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIPFname"
									property="oldIpNomFirstName" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIPSname"
									property="newIpNomSurName" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIPSname"
									property="oldINomSurName" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIPAge"
									property="newIpNomAge" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIPAge"
									property="oldIpNomAge" maxLength="30">
								</display:column>
								
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.newIPMsisdn"
									property="newIpInsMsisdn" maxLength="30">
								</display:column>
								<display:column class="TD_STYLE_ONE" headerClass="TR_PUSH_HEADER"
									titleKey="summaryDetailsChanges.text.oldIPMsisdn"
									property="oldIpInsMsisdn" maxLength="30">
								</display:column>
								
								
								
							</display:table>
						</div>
					</fieldset>
				</div>
				

				<div style="text-align: center;">
					<ul class="btn-wrapper">
						<li class="btn-inner" id="backBtn"><span><spring:message
									code="platform.button.back" /></span></li>
					</ul>
				</div>
				<br />
			</div>
		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
	<script type="text/javascript">
		if ('${VO_CUSTOMER.productId}' == '1,2,3') {
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").hide('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			loadCheckBox("productId", '3');
		} else if ('${VO_CUSTOMER.productId}' == '1,2,4') {
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			loadCheckBox("productId", '4');
		} else if ('${VO_CUSTOMER.productId}' == '1,2') {
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayofferLevel_div").hide('slow');

		} else if ('${VO_CUSTOMER.productId}' == '1,2,3,4') {
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			loadCheckBox("productId", '1');
			loadCheckBox("productId", '2');

			$('input:checkbox[value=3]').attr('disabled', true);

			loadCheckBox("productId", '4');
			$("#insuredRelativeDetails_div").show('slow');
		} else if ('${VO_CUSTOMER.productId}' == '4') {
			$("#displayofferLevel_div").show('slow');
			$("#displayIrdInfoForIp_div").show('slow');
			$("#insuredRelativeDetails_div").hide('slow');
			loadCheckBox("productId", '4');
		} else if ('${VO_CUSTOMER.productId}' == '3') {
			$("#displayofferLevel_div").hide('slow');
			$("#insuredRelativeDetails_div").hide('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '3');
		} else if ('${VO_CUSTOMER.productId}' == '2') {
			$("#displayofferLevel_div").hide('slow');
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '3');
		} else if ('${VO_CUSTOMER.productId}' == '1') {
			$("#displayofferLevel_div").hide('slow');
			$("#insuredRelativeDetails_div").show('slow');
			$("#displayIrdInfoForIp_div").hide('slow');
			loadCheckBox("productId", '3');
		} else {
			loadCheckBox("productId", '${VO_CUSTOMER.productId}');
		}
		
		if('${is_deregisteredcust}' == "true"){
			//$("#implied_age_div").hide('slow');
			$("#message_div").show('slow');
		}else{
			$("#message_div").hide();
		}

		loadRadioButton('gender', '${VO_CUSTOMER.gender}');
		loadDropDownList('insRelation', '${VO_CUSTOMER.insRelation}');
		loadDropDownList('productCoverIdIP', '${VO_CUSTOMER.productCoverIdIP}');
		$('#productCoverIdIP').attr("disabled", "disabled");
	</script>
</body>
</html>

