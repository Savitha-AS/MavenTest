<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">
		$(document).ready(function() 
		{
			
			$("#div_searchResults").show('slow');
			if('${fn:length(claimList)}' > 0 ) {
				
				$("#div_searchResults").css('overflow-x', 'scroll');
			}
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
		<input type="hidden" id="pageId" value="44" />	
		<div class="pagetitle">
			<h3>
					<spring:message code="listclaims.header.text" />
				</h3>			
		</div>
		
		<br/>
		<div style="display: none;" id="div_searchResults">
			<c:if test="${frameWork:isNotNull(claimList)}">
				<display:table id="claimList" name="claimList"
					excludedParams="*" pagesize="10" 	
					style="width:690px; height:100%; overflow-x:scroll;" cellspacing="0"
					cellpadding="0" >
					
					<display:setProperty name="basic.msg.empty_list"
						value="No customer record found.">
					</display:setProperty>
					
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.msisdn" property="msisdn"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="custreport.text.customername" 
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						${frameWork:concat(claimList.fname, claimList.sname)}
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="custreport.text.customerage" property="age"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="custreport.text.insrelname"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						${frameWork:concat(claimList.insRelFname, claimList.insRelSurname)}	
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="custreport.text.insrelage" property="insRelAge"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="regcustomer.text.rel" property="relation"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="claim.text.claimedperson" property="claimedPerson" 
						style="text-align:center; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="platform.text.regdate" property="registeredDate" format="{0,date,yyyy-MM-dd}" 
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="claim.text.claimedby"
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						${claimList.claimedBy.userName}
					</display:column>
					<display:column class="TD_STYLE" headerClass="TR_PUSH_HEADER"
						titleKey="claim.text.claimeddate" property="claimedDate" format="{0,date,yyyy-MM-dd}" 
						style="text-align:left; width :40px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
						
					</display:column>
				</display:table>
				<br/>	
			</c:if>
		</div>
		<br/>
	</div>
	</div>
</body>
</html>