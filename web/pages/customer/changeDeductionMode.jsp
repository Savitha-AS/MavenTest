<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

			if (!validateChangeDeductionMode()) {
				checkMSISDNStatusForDeductionMode($("#msisdn").val());

			}

		});
		// alert("done");
	});

	/* function loadOfferBundles() {
		var offer = dwr.util.getValue('offerId');
		if (offer != "" || offer.length != 0)
			validateCustomerMSISDN.retrieveOfferBundlesForOfferId(offer,
					loadBundleCallback);
		else {
			dwr.util.removeAllOptions('offerCoverId');
			$('<option />', {
				value : '',
				text : ''
			}).appendTo($("#offerCoverId"));
		}
	}
 */
	/* function loadBundleCallback(data) {
		var size = data.length;
		if (size > 0) {

			dwr.util.removeAllOptions('offerCoverId');
			$('<option />', {
				value : '0',
				text : ''
			}).appendTo($("#offerCoverId"));
			for ( var i = 0; i < data.length; i++) {
				if ('${custOfferDetails.offerCoverDetails.offerCoverId}' != data[i]['offerCoverId']) {
					var segment = 'Level ' + data[i]['offerCoverId'] + ': '
							+ data[i]['bimaCover'] + ' Bima & '
							+ data[i]['offerCover'] + ' Pona';
					$('<option />', {
						value : data[i]['offerCoverId'],
						text : segment
					}).appendTo($("#offerCoverId"));
				}
			}
		} else {
			dwr.util.removeAllOptions('offerCoverId');
		}
	} */

	/* function loadSelectedOfferCover() {
		var offerCoverId = '${custOfferDetails.offerCoverDetails.offerCoverId}';

		// alert("in loadSelectedOfferCover");

		if (offerCoverId > 0) {

			// alert("loadSelectedOfferCover---coverId >0");

			var segment = 'Level '
					+ offerCoverId
					+ ': '
					+ [ parseInt(
							"${custOfferDetails.offerCoverDetails.bimaCover}",
							10) ]
					+ ' Bima'
					+ ' & '
					+ [ parseInt(
							"${custOfferDetails.offerCoverDetails.offerCover}",
							10) ] + ' Pona';

			/* 			var segment = 'Level ' + offerCoverId + ': '
			 + [ parseInt("${custOfferDetails.offerCoverDetails.offerCover}",10) ]
			 + ' TZS'; */
		/*	$("#selectedOfferCoverId").val(segment);

			$("#msisdn").attr('readonly', true);
			$("#msisdn").attr('class', 'labelTxtBox_color1');
			$("#msisdn").attr('style',
					'text-align: left; width: 100%; border: 0px');

			$("#search-icon").attr('style', 'visibility:hidden');
		}
	} */

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
			document.getElementById("changeCoverForm").action = "${pageContext.request.contextPath}/customer.controller.getDeductionMode.task";
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
		$("#saveBtn")
				.click(
						function() {
							resetErrors();
							unMarkError("deductionMode");
							if (!validateDropDown(document
									.getElementById("deductionMode"))) {
								/* setError("deductionMode", 1,	getFieldText('deductionMode')); */
								setError("deductionMode", 1,	"Deduction Mode");
							}
							if (showValidationErrors("validationMessages_parent"))
								return true;
							else {
								document.getElementById("changeCoverForm").action = "${pageContext.request.contextPath}/customer.controller.changeDeductionMode.task";
								document.getElementById("changeCoverForm")
										.submit();
							}
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
				<input type="hidden" id="pageId" value="48" />

				<div class="pagetitle">
					<h3>
						<spring:message code="changeDeductionMode.header.text" />
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

               
                <c:if test="${frameWork:isNull(VO_CUSTOMER)}">
				<div class="data_container_color1">
					<div class="data_label" id="label_msisdn" style="width: 170px;">
						<spring:message code="platform.text.msisdn" />
						&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
					</div>
					<div class="data_text" style="width: 358px;">
						<div id="div_id_1" style="float: left;">
							<input type="text" id="msisdn" name="msisdn"
								value="${VO_CUSTOMER[0].msisdn}"
								maxlength="10" style="width: 110px;" />
						</div>
						<div id="search-icon"></div>
					</div>
				</div>
				</c:if>

				<div style="display: none;" id="div_searchResults">
					<c:if test="${frameWork:isNotNull(VO_CUSTOMER)}">

                <div class="data_container_color1">
					<div class="data_label" id="label_msisdn" style="width: 170px;">
						<spring:message code="platform.text.msisdn" />
						 &nbsp;:&nbsp;
					</div>
					<div class="data_text" style="width: 358px;">
						<div id="div_id_1" style="float: left;">
							<input type="text" id="msisdn" name="msisdn"
								value="${VO_CUSTOMER[0].msisdn}" style="width: 200px; text-align: left; border: 0px"
								maxlength="10"  class="labelTxtBox_color1" readonly="readonly" />
						</div>
						<!-- <div id="search-icon"></div> -->
					
					      <div id="div_id_1" style="float: left;visibility: hidden;">
							<input type="text" id="custId" name="custId"
								value="${VO_CUSTOMER[0].custId}"  />
						</div>
					
					</div>
				</div>




						<div class="data_container_color2">
							<div class="data_label" id="label_offerId">
								<spring:message code="current.dedution.mode"></spring:message>
								 &nbsp;:&nbsp; 
							</div>
							<div class="data_text">
								<c:if test="${VO_CUSTOMER[0].deductionMode=='1'}">
									<spring:message code="regcustomer.text.deduction.mode.daily"></spring:message>
								</c:if>
								<c:if test="${VO_CUSTOMER[0].deductionMode=='2'}">
									<spring:message code="regcustomer.text.deduction.mode.monthly"></spring:message>
								</c:if>
							</div>
						</div>
						<div class="data_container_color1">
							<div class="data_label" id="label_deductionMode">
								<spring:message code="regcustomer.text.deductionMode"></spring:message>
								&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
							</div>
							<div class="data_text">
								<select id="deductionMode" name="deductionMode"
									style="width: 200px;">
									<option value=""></option>
									<c:if test="${VO_CUSTOMER[0].deductionMode=='1'}">
						
										<option value="2">
											<spring:message code="regcustomer.text.deduction.mode.monthly"></spring:message>
										</option>

									</c:if>

									<c:if test="${VO_CUSTOMER[0].deductionMode=='2'}">

										<option value="1">
											<spring:message
												code="regcustomer.text.deduction.mode.daily"></spring:message>
										</option>

									</c:if>

								</select>
							</div>

						</div>

					<br />
						 <div style="text-align: center;">
								<ul class="btn-wrapper">
									<li class="btn-inner" id="saveBtn"><span><spring:message
												code="platform.button.savechanges" />
									</span>
									</li>
								</ul>
							</div> 
					</c:if>

				</div>
				<br />
			</div>
		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
	<script type="text/javascript">
		
	</script>
</body>
</html>