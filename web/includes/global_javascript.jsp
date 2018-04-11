
<script src="${pageContext.request.contextPath}/theme/uiScripts/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/theme/uiScripts/buildMenu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/theme/uiScripts/jquery-ui.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/theme/uiScripts/jquery.alerts.js" type="text/javascript"></script>
<!--<script src="${pageContext.request.contextPath}/theme/uiScripts/round-corner.js" type="text/javascript"></script> -->

<script src="${pageContext.request.contextPath}/appScripts/validateManager.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/appScripts/validations.js" type="text/javascript"></script>

<link rel="shortcut icon" type="image/vnd.microsoft.icon" 
      href="${pageContext.request.contextPath}/theme/theme_gh/images/favicon.ico" />

<title>Mobile Insurance Platform, TIGO Ghana</title>

<script>
	function keepSubMenuSelected(){
		var pageId = document.getElementById("pageId");
		if(pageId){
			var childMenu = document.getElementById("submenu"+pageId.value);
						
			if(childMenu){
				var parentMenu = childMenu.parentNode;
				if(parentMenu)
					parentMenu.style.display="block";
				
				var anchorNode = childMenu.children[0];		
				if(anchorNode)
					anchorNode.className = 'selectedSubMenu'; 
			}
		}
	}
	function keepMenuSelected(){
		var pageId = document.getElementById("pageId");
		if(pageId){
			var menu = document.getElementById("menu"+pageId.value);
			
			if(menu){
				var anchorNode = menu.children[0];		
				if(anchorNode)
					anchorNode.className = 'selectedMenu'; 
			}
		}
	}
</script>