<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
	<script src="${pageContext.request.contextPath}/appScripts/report.js" type="text/javascript"></script>
	 <script type="text/javascript">
        function getLastMonths(n) {
            //Creating array of months
            var monthName = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
            
            var months = new Array();
            //Get the current date
             var today = new Date();
          //  var today = new Date("Wed Jan 17 2017 11:13:00");
            
            var day = today.getDate();
            //Get the full year value
            var year = today.getFullYear();
            //get the previous month value  with respect to current month
            
            var month='';
            
            if(day <= 15){
            	month = today.getMonth();
            }else{
            	month = today.getMonth()+1;	
            }

			if(month <= 11){
				month;	
			}else{
				month = 0;
				year = year+1;
			}

            var i = 0;
            //Pushing the last 6 months to array
            while (i < n) {
                
                if(day <= 15){
                	if(month == 12){
                		month = 0;
                		year++;
                	}
            	
                	months.push(monthName[month] + " " + year);
                	if (month == 0) {
                        month = 11;
                        year--;
                    } else {
                        month--;
                    }
                }else{
                	months.push(monthName[month] + " " + year);
                	if (month == 0) {
                        month = 11;
                        year--;
                    }  else {
                        month--;
                    }
                }
                
                
                i++; 
            }
            //returning month array
            return months;
        }
        
        function AssignPreviosMonthOptions() {
            //Retreiving previous 6 month value
            var optionValues = getLastMonths(6);
            var dropDown = document.getElementById('monthSel');
            //Looping through the values and assinging the values to dropdown list
            
            for (var i = 1; i <= optionValues.length; i++) {
                var key = i-1;
                var value = optionValues[i-1];
                /* if(i = 0){
                	dropDown.options[i] = new Option(0,"");
                } */
                dropDown.options[i] = new Option(value, key);
            }
        }
        //Calling the function on page load
        $(document).ready(function(){
            window.onload = AssignPreviosMonthOptions;
        });
    </script>
	
	
	<script>
	var reportType="";
			$(function() {
				 $('input[name="reportType"]').click(function() {
					 reportType = $(this).val();
					if($(this).val() == 0){	
						$("#reportFilterDivForBoth").show('slow');
						$("#reportFilterDivForQuarter").hide();
						
					}else{	
						$("#reportFilterDivForBoth").hide();
						$("#quickSelectionDivForMonth").hide();
						$("#monthDiv").hide();
						$("#reportFilterDivForQuarter").show('slow');
					}
					$("#rolesDiv").show('slow');
					
				}); 
				 
				$('input[name="filterOption"]').click(function() {
					if(reportType == 0 && $(this).val() == 0) {
						$("#quickSelectionDivForMonth").show('slow');
						$("#monthDiv").hide();
						
						
					}else if(reportType == 0 && $(this).val() == 1){
						$("#quickSelectionDivForMonth").hide();
						$("#monthDiv").show('slow');
						
						
					}else if(reportType == 1 && $(this).val() == 0){
						$("#quickSelectionDivForMonth").hide();
						$("#monthDiv").hide();
						
					}
					else {
						// nothing to do
					}
					$("input[name=filterOption]").removeAttr("disabled");
										
				});
				
				$("#downloadReportLink").click(function() {
					if(!validateDeductionReport())
					{
						 $("input[name=filterOption]").removeAttr("disabled"); 
						
						document.getElementById("generateDeductionReportForm").action="${pageContext.request.contextPath}/customerReport.controller.downloadDeductionReport.task";
						document.getElementById("generateDeductionReportForm").submit();						
					}				
				});
			});		
	</script>
</head>
<body onload="keepSubMenuSelected()">
<form method="post" id="generateDeductionReportForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="50" />
			<div class="pagetitle">
				<h3>
					<spring:message code="deductionsreport.header.text" /> 
				</h3>
			</div>

		<div style="text-align: right; margin-right: 5px;">
			<spring:message code="platform.text.mandatory" />
		</div>
		<br />
		<jsp:include page="../../includes/global_validations.jsp"></jsp:include>
		
		<div class="data_container_color1" id="reportTypeDiv">
			<div class="data_label" id="label_reportType">
				<spring:message code="dedreport.text.reportType" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width:auto;">
				<input type="radio" id="reportType" name="reportType" value="0" /><spring:message code="firstMonth.report.text.reportType" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="reportType" name="reportType" value="1" /><spring:message code="firstQuarter.report.text.reportType" />&nbsp;&nbsp;&nbsp;
				<%-- <spring:message code="firstMonth.report.text.reportType" /> --%>
			</div>
		</div>		

		<div class="data_container_color2" id="reportFilterDivForBoth" style="display: none;">
			<div class="data_label" id="label_filterOption">
				<spring:message code="dedreport.text.filter" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="filterOption" name="filterOption" value="0" /><label id="quick"><spring:message code="dedreport.text.quickselect" />&nbsp;&nbsp;&nbsp; 
				<input type="radio" id="filterOption" name="filterOption" value="1" /><spring:message code="dedreport.text.byMonth" />&nbsp;&nbsp;&nbsp;</label>
			</div>
		</div>
		
		
		
		<div class="data_container_color2" id="reportFilterDivForQuarter" style="display: none;">
			<div class="data_label" id="label_filterOption">
				<spring:message code="dedreport.text.quickselect" />&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="quickSelectOptionForQuarter" name="quickSelectOptionForQuarter" value="0" /><spring:message code="dedreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOptionForQuarter" name="quickSelectOptionForQuarter" value="1" /><spring:message code="dedreport.text.premonth" />&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
		<div class="data_container_color1" id="quickSelectionDivForMonth" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="0" /><spring:message code="dedreport.text.month" />&nbsp;&nbsp;&nbsp; 
				<%-- <input type="radio" id="quickSelectOption" name="quickSelectOption" value="0" /><spring:message code="dedreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="1" /><spring:message code="dedreport.text.premonth" />&nbsp;&nbsp;&nbsp; --%>
			</div>
		</div>
		<%-- 
		<div class="data_container_color1" id="quickSelectionDivForQuarter" style="display: none;">
			<div class="data_label">&nbsp;&nbsp;</div>
			<div class="data_text" style="width: auto">
				<input type="radio" id="quickSelectOption" name="quickSelectOption" value="0" /><spring:message code="dedreport.text.currmonth" />&nbsp;&nbsp;&nbsp;
			</div>
		</div> --%>
		
		<div class="data_container_color1" id="monthDiv" style="display: none;">
			<div class="data_label" id="label_role"><%-- <spring:message code="report.text.role">
				</spring:message> &nbsp;:&nbsp;<span class="mandatoryStar">*</span>--%> &nbsp;&nbsp;
			</div>
			<div class="data_text">
				<div class="" id="div_id_15">
					<select id="monthSel" name="monthSel" style="width: 200px;">
						<option></option>
						<!-- <option value="-1"></option> -->
						<%-- <c:forEach var="selMonth" items="${selMonth}">
					    	<option value="${roleMap.key}">${roleMap.value}</option>
					 	</c:forEach> --%>
					</select>
				</div>
			</div>
		</div>
		
		<div class="data_container_color2" id="rolesDiv" style="display: none;">
			<div class="data_label" id="label_role"><spring:message code="report.text.role">
				</spring:message>&nbsp;:&nbsp;<span class="mandatoryStar">*</span>
			</div>
			<div class="data_text">
				<div class="" id="div_id_15">
					<select id="role" name="role" style="width: 200px;">
						<option></option>
						<option value="0">All</option>
						<c:forEach var="roleMap" items="${roleMap}">
					    	<option value="${roleMap.key}">${roleMap.value}</option>
					 	</c:forEach>
					</select>
				</div>
			</div>
		</div>
		
		
		<br/>
		<div style="text-align:center;">
			<ul class="btn-wrapper">
				<li class="btn-inner" id="downloadReportLink">
					<span>
						<spring:message code="report.button.downloadreport" />
					</span>
				</li>
			</ul>
		</div>
		
	</div>
	</div>
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
</body>
</html>