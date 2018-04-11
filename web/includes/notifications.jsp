<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>		
	$(document).ready(function() {
	   	// Hides the slickbox as soon as the DOM is ready
	   	/*
		$('#slickbox').hide();
		*/
		
	   	// Shows the slickbox on clicking the noted link  
	   	/*
		$('#slick-show').click(function() {
		  $('#slickbox').show('slow');
		  return false;
		});
		*/
		
	   	// Hides the slickbox on clicking the noted link  
	   	/*
		$('#slick-hide').click(function() {
		  $('#slickbox').hide('slow');
		  return false;
		});
		*/

		// Toggles the slickbox on clicking the noted link  
		$('#slick-toggle').click(function() {
		  $('#slickbox').toggle('slow');
		  return false;
		});
	});
</script>
<style type="text/css">

#slickbox {
	background-color: #FFFDD1;
    border: 1px solid #E8E49B;
    color: #666666;
    height: auto;
    margin-left: 50px;
    padding: 10px;
    width: 88%;
}	
</style>


<div>	
	<div id="slick-toggle" title="<spring:message code='tooltip.platform.toggle'/>"></div>		
	<div id="slickbox">${param.message}</div>
</div>
<br/>	
<br/>	
