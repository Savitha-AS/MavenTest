<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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
	
		/* $(function() {
			$( "button, input:button, input:submit").button();
		}); */
		
		$(function() {
			$("#saveBtn").click(function() {				
			if($("#offerType").val() == "1")
				{
					//Validate create Additional Offer
					if(!validateCreateOffer()){
						confirmSave("", function(result) {
							if(result){
								//var frm = document.forms[0];
				                //frm.action="${pageContext.request.contextPath}/offers.controller.saveOfferDetails.task";
			                    //frm.submit();
			                    document.getElementById("createOffers").action="${pageContext.request.contextPath}/offers.controller.saveOfferDetails.task";
								document.getElementById("createOffers").submit();
							}
						});
					}				
				}
			if($("#offerType").val() == "2")
				{
				  var frm = document.forms["createOffers"];
				  //Validate create Multiple Offer
				  
				  if(!validateCreateMulOffer(parseInt(frm.freeCover.length))){
					  	confirmSave("", function(result) {
							if(result){
								frm.action="${pageContext.request.contextPath}/offers.controller.saveOfferDetails.task";
			                    frm.submit();
							}
						});
				}
						
				}
			});
		});

		function checkOffer()
			{					
				if($("#offerType").val() == "2")
				{
					//Multiple Cover						
					//$("#div_offerTableHeading").show('slow');
					$("#div_offerTable").hide('slow');
					$("#divMulOfferTable").show('slow');
					$("#div_multiValue").show('slow');
					$("#brLinked").show('slow');
					$("#freeMulCoverHead").show('slow');

					var counter = document.getElementsByName("freeCover");
					
					for(var i =1; i<counter.length;i++){
						document.getElementById("mtxt_"+i+"2").readonly=true;
						$("#mtxt_"+i+"2").val('');
						$("#mtxt_"+i+"1").show('slow');
						$("#mtxt_"+i+"3").val('');
					}
					$("#saveButtonDiv").show('slow');
					$("#perDayDeduction").focus();					
				}
				else if($("#offerType").val() == "1")
				{
					//Additional Cover
					totalOfferRows = 1;
					//$("#div_offerTableHeading").show('slow');
					$("#div_offerTable").show('slow');
					$("#multiValue").val('');
					$("#div_multiValue").hide('slow');
					$("#brLinked").hide('slow');
					$("#freeCoverHead").hide('slow');
					for(var i =1; i<9;i++){
						$("#txt_"+i+"1").hide('slow');
						document.getElementById("txt_"+i+"2").readonly=false;
						$("#txt_"+i+"3").val('');
						$("#txt_"+i+"2").val('');
					}
					$("#saveButtonDiv").show('slow');						
					$("#divMulOfferTable").hide('slow');
					$("#perDayDeduction").focus();
				}
				else{
					$("#multiValue").val('');
					$("#div_multiValue").hide('slow');
					$("#brLinked").hide('slow');
					$("#coverage1").show('slow');
					$("#divAddRemBtn1").show('slow');						
					for(var i =2; i<9;i++){	
						$("#coverage"+i+"").hide('slow');
					}
					//$("#div_offerTableHeading").hide('slow');
					$("#div_offerTable").hide('slow');
					$("#divMulOfferTable").hide('slow');
					$("#saveButtonDiv").hide('slow');
				}
			}
		function setOfferedCover()
		{
			var b = 0;
			var c = 0;
			var d = 0;
			var counter = document.getElementsByName("freeCover");
			for(var i =1; i<=counter.length;i++){
				b = $("#mtxt_"+i+"1").val();
				c = $("#multiValue").val();
				d = b*c;
				$("#mtxt_"+i+"2").val(d);
			}
		}
		
	</script> 
</head>

<body onload="keepSubMenuSelected()">
  <form id="createOffers" method="post">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>
	
	<!-- View Content -->	
	<div class="content-container">
	<div id="box" style="width: 100%;">	
		<!-- The value attribute of the following 
			"pageId" field should hold the value of 
			"menuId" coming from database for this page. -->
		<input type="hidden" id="pageId" value="23" />	
		<div class="pagetitle">
			<h3><spring:message code="createoffer.header.text"></spring:message>
			</h3>			
		</div>	
		
		<div style="text-align: right; margin-right: 5px;"><spring:message code="platform.text.mandatory"></spring:message></div>
		<br/>
			
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
						
		<div class="data_container_color1">
			<div class="data_label" id="label_offerName"><spring:message code="offer.text.offername"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="offerName" name="offerName" style="width:110px" maxlength="50"/>
			</div>
		</div>
		
		<div class="data_container_color2">
			<div class="data_label" id = "label_offerType"><spring:message code="offer.text.offertype"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<select id="offerType" name="offerType" onchange="javascript: checkOffer();" style="width:140px">
				  <option value="0"></option>
				  <c:forEach var="offerType" items="${offerTypeList}">  
				    <option value="${offerType.offerTypeId}">${offerType.offerTypeName}</option>
				  </c:forEach>
				</select>
			</div>
		</div>	
		
		<div class="data_container_color1" id="div_perDayDeduction" >
			<div class="data_label" id="label_perDayDeduction"><spring:message code="createoffer.text.perDayDeduction"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="perDayDeduction" name="perDayDeduction" size="4" maxlength="4" style="width: 25px;"/>
			</div>
		</div>
		
		<div class="data_container_color2" id="div_multiValue" style="display:none">
			<div class="data_label" id="label_multiValue"><spring:message code="createoffer.text.multiplevalue"></spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="multiValue" name="multiValue" size="1" maxlength="1" style="width: 15px;" onchange="javascript: setOfferedCover();"/>
			</div>
		</div>
						
		<div class="data_container_color1" id="brLinked" style="display:none">
			<div class="data_label"><spring:message code="createoffer.text.BRlinked"></spring:message>&nbsp;:</div>
			<div class="data_text">
				<input type="text" id="txt_brLinked" name="brLinked" value="${activeBusinessRule.brVersion}" class="labelTxtBox_color1" style="border:0px; text-align:left;" readonly="readonly"  />
				<input type="hidden" id="busRuleId" name="brId"  value="${activeBusinessRule.brId}" />
			</div>
		</div>	
		<br/>
		
		<div id="divMulOfferTable" style="display:none">	
			<fieldset>
				<legend id="label_mulOfferRange">											
					<spring:message code="offer.text.offerrange"></spring:message> (<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>)&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
				</legend>
				<div>
					<div id="divMulCoverageHead" class="mini_table_color2">
						<div class="mini_table_td mini_table_th" id="freeMulCoverHead"><spring:message code="offer.text.freecover"></spring:message></div>
						<div class="mini_table_td mini_table_th"><spring:message code="offer.text.offeredcover"></spring:message></div>
						<div class="mini_table_td mini_table_th"><spring:message code="offer.text.amttobepaid"></spring:message></div>
					</div>
					<c:forEach var="busRuleDefinition" items="${activeBusinessRule.busRuleDef}" varStatus="loopCounter1"> 								
						<c:choose>
							<c:when test="${loopCounter1.count%2 eq 1}">
								<c:set var="cssClass1" value="mini_table_color1"></c:set>
								<c:set var="cssClass2" value="labelTxtBox_color1"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass1" value="mini_table_color2"></c:set>	
								<c:set var="cssClass2" value="labelTxtBox_color2"></c:set>							
							</c:otherwise>
						</c:choose>				
						<div id="divMulCoverage${loopCounter.count}" class="${cssClass1}" style="display:block;">
							<div class="mini_table_td">
								<input type="text" id="mtxt_${loopCounter1.count}1" name="freeCover" size="5" value="${busRuleDefinition.brRangeVal}"  class="${cssClass2}" style="border:0px; width:50px" readonly="readonly"  />
							</div>
							<div class="mini_table_td">
								<input type="text" id="mtxt_${loopCounter1.count}2" name="offeredCover" size="5"  readonly="readonly" maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="mtxt_${loopCounter1.count}3" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
						</div>					
					</c:forEach>
				</div>	
				<br/>
			</fieldset>	
		</div> 
		
		<div id="div_offerTable" style="display:none">
			<fieldset>
				<legend id="label_offerRange">											
					<spring:message code="offer.text.offerrange"></spring:message> (<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>)&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
				</legend>
				<div id="mainDiv">
						<div id="coverageHead" class="mini_table_color2">
							<div class="mini_table_td mini_table_th" id="freeCoverHead"><spring:message code="offer.text.freecover"></spring:message></div>
							<div class="mini_table_td mini_table_th"><spring:message code="offer.text.offeredcover"></spring:message></div>
							<div class="mini_table_td mini_table_th"><spring:message code="offer.text.amttobepaid"></spring:message></div>
						</div>
						<div id="coverage1" class="mini_table_color1" style="display:block;">
							<div class="mini_table_td">
								<input type="text" id="txt_12" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_13" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn1">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage2',1)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
							</div>
						</div>
						<div id="coverage2" class="mini_table_color2" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_22" name="offeredCover" size="5"   maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_23" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn2">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage3',2)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',2)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
						<div id="coverage3" class="mini_table_color1" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_32" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_33" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;"  id="divAddRemBtn3">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage4',3)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',3)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
						<div id="coverage4" class="mini_table_color2" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_42" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_43" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn4">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage5',4)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',4)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
						<div id="coverage5" class="mini_table_color1" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_52" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_53" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn5">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage6',5)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',5)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
						<div id="coverage6" class="mini_table_color2" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_62" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_63" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn6">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage7',6)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',6)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
						<div id="coverage7" class="mini_table_color1" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_72" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_73" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn7">
								<div class="add-icon" onclick="javascript:showOfferPane('coverage8',7)" title="<spring:message code='tooltip.platform.add.new.row'/>"></div>
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',7)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>													
							</div>
						</div>
						<div id="coverage8" class="mini_table_color2" style="display:none;">
							<div class="mini_table_td">
								<input type="text" id="txt_82" name="offeredCover" size="5"  maxlength="5"/>
							</div>
							<div class="mini_table_td">
								<input type="text" id="txt_83" name="paidAmount" size="5" maxlength="3"/>&nbsp;<span class="mini_table_th"><spring:message code="platform.text.currency" /></span>
							</div>
							<div class="mini_table_td" style="display:block;" id="divAddRemBtn2">&nbsp;
								<div class="delete-icon" onclick="javascript:hideOfferPane('coverage',8)" title="<spring:message code='tooltip.platform.delete.current.row'/>"></div>
							</div>
						</div>
				</div>
				<br/>
			</fieldset>
		</div>
		<br/>
		<div style="text-align:center; display:none;" id="saveButtonDiv">
			<ul class="btn-wrapper"><li class="btn-inner" id="saveBtn"><span><spring:message code="platform.button.save"/></span></li></ul>
		</div>
		<br/>
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>