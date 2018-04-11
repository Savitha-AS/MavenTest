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
<script>
	/* $(function() {
		$( "button, input:button, input:submit").button();
	}); */

	$(function() {
		$("#onlyBackBtn")
				.click(
						function() {
							document.getElementById("modifyClaimDetailsFrm").action = "${pageContext.request.contextPath}/claims.page.searchCustomer.task";
							document.getElementById("modifyClaimDetailsFrm")
									.submit();
						});
	});
	
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
							<input type="text" id="fname" name="fname"
										value="${VO_CUSTOMER.fname}"
										style="text-align: left; width: 110px; border: 0px"
										class="labelTxtBox_color1" readonly="readonly" maxlength="100" />
						</div>
					</div>

					<div class="data_container_color2">
						<div class="data_label" id="label_sname">
							<spring:message code="platform.text.surname"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<div class="" id="div_id_2">
								<input type="text" id="sname" name="sname"
											value="${VO_CUSTOMER.sname}"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly" maxlength="50" />
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
							<input type="text" id="dob" name="dob"
										value="${VO_CUSTOMER.dob}"
										style="text-align: left; width: 110px; border: 0px"
										class="labelTxtBox_color2" readonly="readonly" />

						</div>
					</div>
					<div class="data_container_color1">
						<div class="data_label" id="label_age">
							<spring:message code="platform.text.age"></spring:message>
							&nbsp;:
						</div>
						<div class="data_text">
							<input type="text" id="age" name="age"
										value="${VO_CUSTOMER.age}" style="width: 20px; border: 0px"
										class="labelTxtBox_color1" readonly="readonly" maxlength="2" />
						</div>
					</div>
					<div class="data_container_color2">
						<div class="data_label">
							<spring:message code="platform.text.gender"></spring:message>
							&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
						</div>
						<div class="data_text">
							<input type="radio" name="gender" id="gender" value="M"
										class="labelTxtBox_color2" disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.male"></spring:message>
					 		&nbsp;&nbsp;
							<input type="radio" name="gender" id="gender" value="F"
										class="labelTxtBox_color2" disabled="disabled" /> &nbsp;<spring:message
										code="platform.text.female"></spring:message>
						</div>
					</div>
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
									style="text-align: left; width: 300px; border: 0px"
									class="labelTxtBox_color1" readonly="readonly"
									value="${VO_CUSTOMER.modifiedBy}" />
							</div>
						</div>
					<br />
				</fieldset>
				<br />
				<div id="insuredRelativeDetails_div">
					<c:if
						test="${ ( is_hpCust eq true and is_xlCust eq true and is_ipCust eq true)
													               || (is_xlCust eq true and is_ipCust eq true) || 
													               (is_xlCust eq true and is_hpCust eq true)|| (is_xlCust eq true)
													           }">
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
										<input type="text" id="relation" name="relation"
													style="text-align: left; width: 130px; border: 0px"
													class="labelTxtBox_color1" readonly="readonly"
													value="${VO_CUSTOMER.insRelation}" />
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
										<input type="text" id="insRelFname" name="insRelFname"
													style="text-align: left; width: 110px; border: 0px"
													class="labelTxtBox_color2" readonly="readonly"
													value="${VO_CUSTOMER.insRelFname}" maxlength="100" />
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
										<input type="text" id="insRelSurname" name="insRelSurname"
													style="text-align: left; width: 110px; border: 0px"
													class="labelTxtBox_color1" readonly="readonly"
													value="${VO_CUSTOMER.insRelSurname}" maxlength="50" />
									</div>
								</div>
							</div>
							<div class="data_container_color2">
								<div class="data_label" id="label_insRelIrDoB">
									<spring:message code="platform.text.dob"></spring:message>
									&nbsp;:
								</div>
								<div class="data_text" style="text-align: left">
										<input type="text" id="insRelIrDoB" name="insRelIrDoB"
												value="${VO_CUSTOMER.insRelIrDoB}"
												style="text-align: left; width: 100px; border: 0px"
												class="labelTxtBox_color2" />
								</div>
							</div>
							<div class="data_container_color1">
								<div class="data_label" id="label_insRelAge">
									<spring:message code="platform.text.age"></spring:message>
									&nbsp;:
								</div>
								<div class="data_text" style="text-align: left;">
									<input type="text" id="insRelAge" name="insRelAge"
												style="text-align: left; width: 20px; border: 0px"
												class="labelTxtBox_color1" value="${VO_CUSTOMER.insRelAge}"
												maxlength="2"
												onfocus="javascript:calculateAge('insRelIrDoB','insRelAge');" />
								</div>
							</div>
							<br />
						</fieldset>
						<br />
					</c:if>
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

									<input type="text" id="ipNomFirstName" name="ipNomFirstName"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly"
											value="${VO_CUSTOMER.ipNomFirstName}" maxlength="100" />
							</div>
						</div>
						<div class="data_container_color2">
							<div class="data_label" id="label_ipNomSurName">
								<spring:message code="platform.text.surname"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<input type="text" id="ipNomSurName" name="ipNomSurName"
											style="text-align: left; width: 110px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly"
											value="${VO_CUSTOMER.ipNomSurName}" maxlength="50" />

							</div>
						</div>

						<div class="data_container_color1">
							<div class="data_label" id="label_ipNomAge">
								<spring:message code="platform.text.age"></spring:message>
								&nbsp;:&nbsp; <span class="mandatoryStar">*</span>
							</div>
							<div class="data_text" style="text-align: left">
								<input type="text" id="ipNomAge" name="ipNomAge"
											style="text-align: left; width: 20px; border: 0px"
											class="labelTxtBox_color2" readonly="readonly"
											value="${VO_CUSTOMER.ipNomAge}" maxlength="2" />

							</div>
						</div>
						<br />
					</fieldset>
				</div>
				<br />
				<div style="text-align: center;"
							id="backButtons_div">

							<ul class="btn-wrapper">
								<li class="btn-inner" id="onlyBackBtn"><span><spring:message
											code="platform.button.back" /></span></li>
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

