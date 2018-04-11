<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	<jsp:include page="../../includes/calendar.jsp"></jsp:include>
	<jsp:include page="../../includes/global_javascript.jsp"></jsp:include>
	<jsp:include page="../../includes/global_css.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/appScripts/utils.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/appScripts/role.js" type="text/javascript"></script>

	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/validateAddRole.js"></script>
	<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/displayChildMenus.js"></script>
	
	<script>

	
	function showMenu(roleId) {
		
		if(roleId != null || roleId != '') {
			document.getElementById("viewRolesForm").action="${pageContext.request.contextPath}/role.controller.getMenusByRole.task";
			document.getElementById("viewRolesForm").submit();
		
			checkAndDisplayMenus();
		}
	}
	
	function checkAndDisplayMenus(){
		// Check
		
		var parentMenusString = '${roleVO.parentMenus}'.substring(1, '${roleVO.parentMenus}'.length - 1);
		var childMenusString = '${roleVO.childMenus}'.substring(1, '${roleVO.childMenus}'.length - 1);
		
		$('#role').val('${roleVO.roleId}');
		
		$("#menuOptionDiv").show('slow');
		
		
		if(parentMenusString != null || childMenusString != null) {
			var parentMenusArray = new Array();
			var childMenusArray = new Array();
		
			parentMenusArray = parentMenusString.split(', ');
		
			childMenusArray = childMenusString.split(', ');
		
			for(var i=0; i <parentMenusArray.length; i++){
				$("#"+parentMenusArray[i]+"").attr('checked', 'checked');
			}
			for(var j=0; j < childMenusArray.length; j++){
				$("#"+childMenusArray[j]+"").attr('checked', 'checked');
			}			
		
		   if('${roleVO.isIPReg}'=='1'){
				$("#isIPReg_div").show('slow');
				$("input[type=checkbox][id=isIPReg]").attr("checked",true);
			}else {
				if($('input:checkbox[id=3]').is(':checked') && $('input:checkbox[id=16]').is(':checked'))
			    $("#isIPReg_div").show('slow');
				$("input[type=checkbox][id=isIPReg]").attr("checked",false);				
			}
		}

	}
	
	$(function() {
		$("#saveBtn").bind("click",(function() {
			if(!validateModifyRole()){
				checkForExistingRole("");
			}
		}));

		$("body").keypress(function(e) {
			 if (e.keyCode == '13') {
			 	$("#saveBtn").click(); 
				return false;
			 }
	 	});

		$("#clearBtn").click(function() {
			clearField("role");
			$('input:checkbox').removeAttr('checked');
		});
	});	
		
	$(document).ready(function() 
	{
		$("#div_searchResults").show('slow');
	});
	
	/**
	 * Call-back method for <code>checkForExistingMSISDN</code> DWR method.
	 * 
	 * @param msisdnExists boolean result of the DWR method invoked.
	 * 
	 * @see checkForExistingRole()
	 */
	function loadRoleCheck(roleExists){
		if(roleExists){
			setError("roleName", 20, getFieldText('roleName'));
		}
		if(showValidationErrors("validationMessages_parent"))
			return true;
		else{
			confirmSave("", function(result) {
				if(result){
					
					var parentList = "";
					$('input[name=parentMenuCheckBox]').each(function () {
						if(this.checked){
							parentList += this.id + ",";
						}
					});
					
					var childList = "";
					$('input[name=childMenuCheckBox]').each(function () {
						if(this.checked){
							childList += this.id + ",";
						}
					});

					document.getElementById("roleId").value = '${roleVO.roleId}';
					document.getElementById("parentMenuString").value = parentList;
					document.getElementById("childMenuString").value = childList;
					document.getElementById("viewRolesForm").action = "${pageContext.request.contextPath}/role.controller.modifyRoleDetails.task";
					document.getElementById("viewRolesForm").submit();
				}

			});
		}
	}
	 
	 
	 function checkParent(childMenuId, parentMenuId) {
		 
		 parentId = parentMenuId;
		 
		 	if(document.getElementById(childMenuId).checked){   
		 		$("#"+parentMenuId+"").attr('checked', 'checked');
			}
		 	else if(!document.getElementById(childMenuId).checked){   
		 		$("#"+parentMenuId+"").attr('checked', false);
			}
		 	
		 	checkIfAllChildUnchecked(parentMenuId);
	}
	 
	function checkOrUncheckChild(parentMenuId){
		 if(document.getElementById(parentMenuId).checked){
			 displayChildMenus.getChildMenusForParentMenuId(parentMenuId, checkAllChild);
			 $("#isIPReg_div").show('slow');
		}
		 else if(!document.getElementById(parentMenuId).checked){   
			 displayChildMenus.getChildMenusForParentMenuId(parentMenuId, uncheckAllChild);
			 $("#isIPReg_div").hide('slow');
		 }
	}
	
	function checkAllChild(childRoleVO) {
		checkChilds(childRoleVO);
	}
	
	function uncheckAllChild(childRoleVO) {
		uncheckChilds(childRoleVO);
	}
	
	function checkIfAllChildUnchecked(parentMenuId){
		displayChildMenus.getChildMenusForParentMenuId(parentMenuId, checkForChildWithSameParent);
	}
	
	function checkForChildWithSameParent(childRoleVO){
		checkOrUncheckParent(parentId, childRoleVO);
	}
	
	function checkDefault() {
		$("#"+1+"").attr('checked', 'checked');
		$("#"+10+"").attr('checked', 'checked');
		$("#"+11+"").attr('checked', 'checked');
		document.getElementById(1).disabled = true;
		document.getElementById(10).disabled = true;
		document.getElementById(11).disabled = true;
	}
	
	</script>
	<link href="${pageContext.request.contextPath}/theme/css/displaytag.css" rel="stylesheet" type="text/css"/>
</head>

<body onload="keepSubMenuSelected(); checkDefault();">
<form method="post" id="viewRolesForm">
	<jsp:include page="../../includes/global_header.jsp"></jsp:include>
	<jsp:include page="../../includes/menus.jsp"></jsp:include>

	<!-- View Content -->
	<div class="content-container">
		<div id="box" style="width: 100%;">
			<!-- The value attribute of the following 
				"pageId" field should hold the value of 
				"menuId" coming from database for this page. -->
			<input type="hidden" id="pageId" value="47" />
			<div class="pagetitle">
				<h3>
					<spring:message code="role.view.header.text"></spring:message>
				</h3>
			</div>
	
			<jsp:include page="../../includes/global_validations.jsp"></jsp:include>

			<br />
			<br/>
			<div class="data_container_color2">
			<div class="data_label" id="label_role"><spring:message code="platform.text.role"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
				<div class="data_text">
					<div id="div_id_15" >
						<select id="role" name="role" style="width:250px;" onchange="showMenu(this.value)">
							<option value=""></option>
							<c:forEach var="roleMap" items="${roleMap}">  
						    	<option value="${roleMap.key}">${roleMap.value}</option>
						 	</c:forEach>					
						</select>
					</div>
				</div>
			</div>
			
		<div class="data_container_color1" id="menuOptionDiv" style="height: 750px; display: none;">
		
			<div class="data_label" id="label_roleMenu"><spring:message code="platform.text.menu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			
			<br />
		
			<c:set var="menuMapAdmin" value="${sessionScope['menuTreeAdmin']}"/>
			<c:choose>
			<c:when test="${frameWork:isNotNull(menuMapAdmin)}">
	
				<div id="menuDiv" style="float:left;">
					<c:forEach var="parentMenu" items="${menuMapAdmin[0]}">
			
						<c:set var="parentUrl" value="${parentMenu.menuUrl}"/>
						<c:if test="${parentMenu.menuUrl ne '#'}">
							<c:set var="parentUrl" value="${pageContext.request.contextPath}/${parentUrl}"/>		
						</c:if>
						<br/>
						
						<div style="" class="data_label" id="label_menu">
							<input id="${parentMenu.menuId}" name="parentMenuCheckBox" onchange="checkOrUncheckChild(this.id)" value="${parentUrl}" type="checkbox" />
								<%-- <spring:message code="${parentMenu.menuName}"></spring:message> --%>
								<c:out value="${parentMenu.menuName}"></c:out>
						</div>
											
						<c:if test="${parentMenu.menuUrl eq '#'}">
							<br/>
							<c:forEach var="childMenu" items="${menuMapAdmin[parentMenu.menuId]}">
									<div style="margin-left: 50px;" class="data_label" id="label_subMenu">
										<input id="${childMenu.menuId}" name="childMenuCheckBox" onchange="checkParent(this.id, ${parentMenu.menuId})" value="${pageContext.request.contextPath}/${childMenu.menuUrl}" type="checkbox" />
										<%-- <spring:message code="${childMenu.menuName}"></spring:message> --%>
										<c:out value="${childMenu.menuName}"></c:out>
										<c:if test="${childMenu.menuId eq 16}">
														<div id="isIPReg_div"
															style="display: none; float: right;">
															<input type="checkbox" id="isIPReg"
																name="isIPReg" value="1" />
															<spring:message code="platform.text.access"></spring:message>
														</div>
													</c:if>
										   </div>
									<br/>
							</c:forEach>
						</c:if>
						
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<c:redirect url="${pageContext.request.contextPath}/pages/login.jsp"></c:redirect>
			</c:otherwise>
			</c:choose>
			<br /><br />
		</div>
		
		<div class="data_container_color1" style="display: none;">
			<div class="data_label" id="label_roleId"><spring:message code="platform.text.roleId"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="roleId" name="roleId" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		 <div class="data_container_color2" style="display: none;">
			<div class="data_label" id="label_parentMenuString"><spring:message code="platform.text.parentMenu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="parentMenuString" name="parentMenuString" style="width:110px" maxlength="100"/>
			</div>
		</div>
		
		
		<div class="data_container_color1" style="display: none;">
			<div class="data_label" id="label_childMenuString"><spring:message code="platform.text.childMenu"/>&nbsp;:&nbsp;<span class="mandatoryStar">*</span></div>
			<div class="data_text">
				<input type="text" id="childMenuString" name="childMenuString" style="width:110px" maxlength="100"/>
			</div>
		</div> 
		
			<br/>
			<br/>
			
		<div style="text-align: center;">
					<ul class="btn-wrapper">
						<li class="btn-inner" id="saveBtn"><span><spring:message
									code="platform.button.save" /></span></li>
					</ul>
					<ul class="btn-wrapper">
						<li class="btn-inner" id="clearBtn"><span><spring:message
									code="platform.button.clear" /></span></li>
					</ul>
				</div>
			
		</div>
	</div>
	
	<jsp:include page="../../includes/global_footer.jsp"></jsp:include>
</form>
<script>checkAndDisplayMenus();

$("input[type=checkbox][id=16]").click(function(){
	if($('input:checkbox[id=16]').is(':checked')){		
		$("#isIPReg_div").show('slow');
	}
	else{
		$("input[type=checkbox][id=isIPReg]").attr( "checked",false);		
		$("#isIPReg_div").hide('slow');
	}
});

$("input[type=checkbox][id=3]").click(function(){
	if($('input:checkbox[id=3]').is(':checked') && $('input:checkbox[id=16]').is(':checked')){
		$("#isIPReg_div").show('slow');		
		$("input[type=checkbox][id=isIPReg]").attr( "checked",true);
		
	}
	else{
		$("input[type=checkbox][id=isIPReg]").val('0');		
		$("input[type=checkbox][id=isIPReg]").attr( "checked",false);
		$("#isIPReg_div").hide('slow');
		
	}

});
</script>
</body>
</html>