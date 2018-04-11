<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">
		$(document).ready(function() 
		{
			$("#div_searchResults").show('slow');
		});
	</script>
	
</head>
<body onload="keepSubMenuSelected()">
	<form id="listOffers" method="post">
		<jsp:include page="../../includes/global_header.jsp"></jsp:include>
		<jsp:include page="../../includes/menus.jsp"></jsp:include>

		<!-- View Content -->	
		<div class="content-container">
		<div id="box" style="width: 100%;">	
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="24" />	
			<div class="pagetitle">
				<h3><spring:message code="listoffer.header.text"></spring:message> 
				</h3>			
			</div>	
			
			<div style="display: none;" id="div_searchResults">
				<display:table id="listOffers" name="requestScope.productsList" pagesize="10"
					requestURI="" cellspacing="0" cellpadding="0" class="table"
					style="width: 690px;">

					<display:setProperty name="basic.msg.empty_list"
							value="No offer records found.">
					</display:setProperty>

					<display:column titleKey='offer.text.offername' paramId="offerId" paramProperty="offerId" class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="35">
						<a href="offers.controller.viewOffer.task?offerId=${listOffers.offerId}&offerName=${listOffers.offerName}&offerType=${listOffers.offerType.offerTypeName}">${listOffers.offerName}</a>
					</display:column>
					
					<display:column property="offerType.offerTypeName" titleKey='offer.text.offertype' class="TD_STYLE" headerClass="TR_PUSH_HEADER" maxLength="20"/>
				</display:table>
			</div>
			<br/>
		</div>
		</div>
		<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
	</form>
</body>
</html>