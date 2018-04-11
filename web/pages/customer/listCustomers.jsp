<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/frameWorkFunctions.tld" prefix="frameWork"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
<script type="text/javascript">
	$(document).ready(function() {
		$("#div_searchResults").show('slow');
	});
</script>
</head>
<body onload="keepSubMenuSelected()">
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
					<spring:message code="searchcustomer.header.text"></spring:message>
				</h3>
			</div>
			<div
				style="text-align: center; overflow-x: auto; height: auto; margin-left: 2px; display: none;"
				id="div_searchResults">
				<%-- <h1>
					<spring:message code="searchcustomer.header.Active"></spring:message>
				</h1> --%>
				<c:if test="${frameWork:isNull(customerDetailsList)}">
				<display:table id="customerDetailsList" name="customerDetailsList"
						requestURI="/customer.controller.searchCustomerDetails.task"
						excludedParams="" pagesize="10" style="width:690px; height:100%;"
						cellspacing="0" cellpadding="0">
						<display:setProperty name="basic.msg.empty_list"
							value="No customer records found.">
						</display:setProperty>
						</display:table>
				</c:if>
				<c:if test="${frameWork:isNotNull(customerDetailsList)}">
					<display:table id="customerDetailsList" name="customerDetailsList"
						requestURI="/customer.controller.searchCustomerDetails.task"
						excludedParams="" pagesize="10" style="width:690px; height:100%;"
						cellspacing="0" cellpadding="0">
						<display:setProperty name="basic.msg.empty_list"
							value="No customer records found.">
						</display:setProperty>
-						
					<c:if test="${ not empty customerDetailsList}">	
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="searchcustomer.text.offername"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">						
									<c:choose>
											<c:when test="${frameWork:isNotNull(customerDetailsList.offerName)}">
												<c:out value="${customerDetailsList.offerName}"/>									
											</c:when>
											<c:otherwise>
												<c:out value="N/A"/>
											</c:otherwise>
										</c:choose>						
								</display:column>
								
								<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="searchcustomer.text.cname"  paramId="custName" 
									paramProperty="custName"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									<a href="customer.controller.getCustomerDetails.task?custId=${customerDetailsList.custId}&offerId=${customerDetailsList.offerId}&status=${customerDetailsList.status}&offerName=${customerDetailsList.offerName}&custName=${customerDetailsList.custName}">${customerDetailsList.custName}</a>
								</display:column>
								
									
			
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="platform.text.msisdn" property="msisdn"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									</display:column>
									
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
									titleKey="searchcustomer.text.status"
									style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
									<c:choose>
											<c:when test="${frameWork:isNotNull(customerDetailsList.status)}">
												<c:out value="${customerDetailsList.status}"/>									
											</c:when>
											<c:otherwise>
												<c:out value="N/A"/>
											</c:otherwise>
										</c:choose>
									</display:column>	
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="searchcustomer.text.confirmedDate"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										<c:choose>
											<c:when
												test="${frameWork:isNotNull(customerDetailsList.confDate)}">
												<c:out value="${customerDetailsList.confDate}"></c:out>
											</c:when>
											<c:otherwise>
												<c:out value="N/A"></c:out>
											</c:otherwise>
										</c:choose>
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="searchcustomer.text.regby"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										<c:choose>
											<c:when
												test="${frameWork:isNotNull(customerDetailsList.regBy)}">
												<c:out value="${customerDetailsList.regBy}"></c:out>
											</c:when>
											<c:otherwise>
												<c:out value="N/A"></c:out>
											</c:otherwise>
										</c:choose>
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="searchcustomer.text.registeredDate"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										<c:choose>
											<c:when
												test="${frameWork:isNotNull(customerDetailsList.regDate)}">
												<c:out value="${customerDetailsList.regDate}"></c:out>
											</c:when>
											<c:otherwise>
												<c:out value="N/A"></c:out>
											</c:otherwise>
										</c:choose>
									</display:column>
									<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
										titleKey="searchcustomer.text.deRegisteredDate"
										style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
										<c:choose>
											<c:when
												test="${frameWork:isNotNull(customerDetailsList.deRegisteredDate)}">
												<c:out value="${customerDetailsList.deRegisteredDate}"></c:out>
											</c:when>
											<c:otherwise>
												<c:out value="N/A"></c:out>
											</c:otherwise>
										</c:choose>
									</display:column>
							</c:if>
					
									
						
						
						

					</display:table>
				</c:if>
			</div>
			<br /> <br />
		</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</body>
</html>