<script type='text/javascript'
	src='${pageContext.request.contextPath}/dwr/engine.js'></script>
<script type='text/javascript'
	src='${pageContext.request.contextPath}/dwr/interface/validateSessionTimeout.js'></script>
<script
	src="${pageContext.request.contextPath}/appScripts/sessionManager.js"
	type="text/javascript"></script>
<script	src="${pageContext.request.contextPath}/appScripts/sessionPopup.js"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/theme/css/popup.css"
	type="text/css" media="screen" />

<script type="text/javascript">
	var g_maxInactiveTime =
<%=session.getMaxInactiveInterval()%>
	setTimeout("showTimeoutWarning()", (g_maxInactiveTime - 20) * 1000);
	var ctx = "${pageContext.request.contextPath}";
</script>

<!-- Timeout Pop Up -->
<div id="sessionPopup">
	<h2>Session Timeout Warning</h2>
	<hr />
	<div id="sessionArea"></div>
	<div style="text-align: center;">
		<ul class="btn-wrapper"><li class="btn-inner" 
			id="okBtn"><span>Retain</span></li></ul>
		<ul class="btn-wrapper"><li class="btn-inner" 
			id="cancelBtn"><span>End</span></li></ul>
	</div>
</div>
<div id="sessionBackgroundPopup"></div>
<!-- End of Pop Up code -->