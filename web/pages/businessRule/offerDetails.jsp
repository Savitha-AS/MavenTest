<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
	<script src="${pageContext.request.contextPath}/appScripts/offer.js" type="text/javascript"></script>
	<script>
	/*	$(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() {
				if(!validateEditOfferForBRActivation()){
					confirmSave("", function(result) {
						if(result){
							var frm = document.getElementById("offerDetailsFrm");
						 	frm.action="${pageContext.request.contextPath}/businessRule.controller.saveOfferDetails.task";
		                 	frm.submit();
						}
					});
				}	
			});
		});

		$(function() {
			$("#clearBtn").click(function() {								
				if($("#txt_13"))
					clearField("txt_13");
				if($("#txt_23"))
					clearField("txt_23");					
				if($("#txt_33"))
					clearField("txt_33");
				if($("#txt_43"))
					clearField("txt_43");
				if($("#txt_53"))
					clearField("txt_53");
				if($("#txt_63"))
					clearField("txt_63");
				if($("#txt_73"))
					clearField("txt_73");
				if($("#txt_83"))
					clearField("txt_83");
			});
		});
		
		function validateEditOfferForBRActivation(){
			var numOfRows="";

			var frm = document.forms["offerDetailsFrm"];
			var cvrCount= parseInt(frm.freeCover.length);
			
			if(isNaN(cvrCount))
				numOfRows=1;
			else
				numOfRows=cvrCount;
			
			
			// Reset the error list and unmark the errors if any.
			resetErrors();
			//Validating Offer Range Table
			unMarkError("label_offerRange");
			for(var i =1; i <= numOfRows ;i++){
				if(!validateTextForCurrencyOnly("txt_"+i+"3")){
					setError ("label_offerRange", 14 , "Offered Cover Changes: "+i+"" );
				}
			}
			if(showValidationErrors("validationMessages_parent"))
					return true;
		}
	</script>
    </head>
    <body onload="keepSubMenuSelected()">
    <form id="offerDetailsFrm" method="post">
     <jsp:include page="../../includes/global_header.jsp"></jsp:include>
	 <jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<input type="hidden" id="pageId" value="21" />	
		<div class="pagetitle">
			<h3><spring:message code="viewoffer.header.text"></spring:message>
			</h3>			
		</div>	
				<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"/></div>
				<br/>
				<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
				
				<div class="data_container_color1">
					<div class="data_label"><spring:message code="offer.text.offername"></spring:message>&nbsp;:</div>
					<div class="data_text">
                        <input type="text" id="offerName" name="offerName" class="labelTxtBox_color1" 
                        style="border:0px; text-align:left;" value="${businessRuleOfferVO.offerName}" readonly="readonly"/>
                        
                         <input type="hidden" id="offerId" name="offerId" value="${businessRuleOfferVO.offerId}" readonly="readonly"/>
                          </div>
				</div>
				
				<div class="data_container_color2">
					<div class="data_label"><spring:message code="offer.text.offertype"></spring:message>&nbsp;:</div>
					<div class="data_text">
                        <input type="text" id="offerType" name="offerType" class="labelTxtBox_color2" 
                        style="border:0px; text-align:left;" value="${businessRuleOfferVO.offerType}" readonly="readonly"/>
                          </div>
				</div>	
				
				<div class="data_container_color1">
					<div class="data_label"><spring:message code="offer.text.multiplecover"></spring:message>&nbsp;:</div>
					<div class="data_text">
                        <input type="text" id="multiValue" name="multiValue" size="1" maxlength="1" class="labelTxtBox_color1" 
                        style="border:0px; text-align:left;" value="${businessRuleOfferVO.multiValue}" readonly="readonly"/>
                    </div>					
				</div>	
				<br/>	
				
				<fieldset>
					<legend id="label_offerRange">											
						<spring:message code="offer.text.offerrange"></spring:message> (<spring:message code="platform.text.currency"></spring:message>)&nbsp;:
						<span class="mandatoryStar">*</span>
					</legend>
					<div id="mainDiv">
						<div id="coverageHead" class="mini_table_color2">
						    <div class="mini_table_td mini_table_th"><spring:message code="offer.text.freecover"></spring:message></div>
							<div class="mini_table_td mini_table_th"><spring:message code="offer.text.offeredcover"></spring:message></div>
							<div class="mini_table_td mini_table_th"><spring:message code="offer.text.amttobepaid"></spring:message></div>
						</div>
						<c:forEach var="busRule" items="${businessRuleOfferVO.busRuleDefList}" varStatus="loopCounter"> 							
							<c:choose>
								<c:when test="${loopCounter.count%2 eq 1}">
									<c:set var="cssClass" value="mini_table_color1"></c:set>
									<c:set var="cssClass2" value="labelTxtBox_color1"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="mini_table_color2"></c:set>	
									<c:set var="cssClass2" value="labelTxtBox_color2"></c:set>							
								</c:otherwise>
							</c:choose>
							<div id="coverage${loopCounter.count}" class="${cssClass}">
							    <div class="mini_table_td">
	                                 <input type="text" id="txt_${loopCounter.count}1" name="freeCover" value="${busRule.brRangeFrom}" 
	                                   class="${cssClass2}" style="border:0px; width:50px" readonly="readonly"  />
	                            </div>								
								<div class="mini_table_td">
		                           <input type="text" name="offeredCover" id="txt_${loopCounter.count}2" style="border:0px; width:50px" 
		                             size="5" class="${cssClass2}"  value="${busRule.brRangeTo}" readonly="readonly"/>
	                            </div>
								<div class="mini_table_td"><input type="text" name="paidAmount" id="txt_${loopCounter.count}3"
                                      style="width:50px" size="5" value="${(busRule.brRangeVal== -1) ? '': busRule.brRangeVal}" />
                                </div>
			 				 </div>
						</c:forEach>
					</div>	
					<br/>
				</fieldset>	
				
				<br/>
			   	<div style="text-align: center;">
			   		<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.savechanges"/></span></li></ul>
					<ul class="btn-wrapper"><li class="btn-inner" id="clearBtn"><span><spring:message code="platform.button.clear"/></span></li></ul>
				</div>
			  	<br/>
			</div>
			</div>
            <jsp:include page="../../includes/global_footer.jsp"></jsp:include>
       </form>
    </body>
</html>



