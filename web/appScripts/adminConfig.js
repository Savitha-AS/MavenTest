
function validateAdminConfig(){
	
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	// Validation for Default Password.
	// unmark the Default Password field.
	unMarkError("defaultPwd");
	if(!validateTextBox("defaultPwd"))
	{
		setError ("defaultPwd" , 0 , "Default Password" );			
	}
	// Implementation of SOXC576.
	else if($("#defaultPwd").val().length < 8){
		setError("defaultPwd",17,"Default Password");
	}
	else if(!validateUserPassword("defaultPwd")){
		setError("defaultPwd",28,"Default Password");
	}
	
	// Validation for User Login Id Prefix.
	// unmark the User Login Id Prefix field.
	unMarkError("userLoginPrefix");
	if(!validateTextBox("userLoginPrefix"))
	{
		setError ( "userLoginPrefix" , 0 , "Prefix for User Login Id" );			
	}
	else if(!isAlpha("userLoginPrefix"))
	{
		setError("userLoginPrefix", 21, "Prefix for User Login Id");
	}
	else if(!checkForUpperCase("userLoginPrefix"))
	{
		setError("userLoginPrefix", 30, "Prefix for User Login Id");
	}
		
	// Validation for Password History Limit.
	// unmark the Password History Limit field.
	unMarkError("pwdHistoryLimit");
	if(!validateTextBox("pwdHistoryLimit"))
	{
		setError ( "pwdHistoryLimit" , 0 , "Maximum Password History Limit" );			
	}
	else if(!checkForDigits("pwdHistoryLimit"))
	{
		setError("pwdHistoryLimit", 3, "Maximum Password History Limit");
	}
	
	// Validation for Login Attempts.
	// unmark the Login Attempts field.
	unMarkError("maxLoginAttempts");
	if(!validateTextBox("maxLoginAttempts"))
	{
		setError ( "maxLoginAttempts" , 0 , "Maximum Login Attempts" );			
	}
	else if(!checkForDigits("maxLoginAttempts"))
	{
		setError("maxLoginAttempts", 3, "Maximum Login Attempts");
	}

	// Validation for Idle Status.
	// unmark the Idle Status field.
	unMarkError("maxIdleCount");
	if(!validateTextBox("maxIdleCount"))
	{
		setError ( "maxIdleCount" , 0 , "Idle Status" );			
	}
	else if(!checkForDigits("maxIdleCount"))
	{
		setError("maxIdleCount", 3, "Idle Status");
	}	
	
	// Validation for Commission Percentage.
	// unmark the Commission Percentage field.
	unMarkError("commissionPercent");
	if(!validateTextBox("commissionPercent"))
	{
		setError ( "commissionPercent" , 0 , "Commission Percentage" );			
	}
	else if(!validateTextForPremiumPerOnly("commissionPercent"))
	{
		setError("commissionPercent", 14, "Commission Percentage");
	}
	
	// Validation for WS URL.
	// unmark the WS URL field.
	unMarkError("registerCustomerWSURL");
	if(!validateTextBox("registerCustomerWSURL"))
	{
		setError ( "registerCustomerWSURL" , 0 , "Register Customer web-service URL" );			
	}
	else if(!checkTextAreaValLength($("#registerCustomerWSURL").val(),250)){
		setError ( "registerCustomerWSURL" , 25 , "Register Customer web-service URL" );	
	}

	// Validation for Announcement Message.
	// unmark the Announcement Message field.
	unMarkError("announcementMessage");
	if(!validateTextBox("announcementMessage"))
	{
		setError ( "announcementMessage" , 0 , "Announcement message" );			
	}
	else if(!checkTextAreaValLength($("#announcementMessage").val(),250)){
		setError ( "announcementMessage" , 25 , "Announcement message" );	
	}
	
	// Validation for Offer Subscription Last Day.
	// unmark the Offer Subscription Last Day field.
	unMarkError("offerSubscriptionLastDay");
	if(!validateTextBox("offerSubscriptionLastDay"))
	{
		setError ( "offerSubscriptionLastDay" , 0 , "Offer Subscription Last Day" );			
	}
	else if(!checkForDigits("offerSubscriptionLastDay"))
	{
		setError ( "offerSubscriptionLastDay" , 3 , "Offer Subscription Last Day" );			
	}
	
	// Validation for MSISDN Codes.
	// unmark the MSISDN Codes field.
	var msisdnCodes = removeExtraChar($("#msisdnCodes").val());
	$("#msisdnCodes").val(msisdnCodes);
	unMarkError("msisdnCodes");
	if(!validateTextBox("msisdnCodes"))
	{
		setError ( "msisdnCodes" , 0 , "MSISDN Codes" );
	}
	else if(!validateMsisdnCodes(msisdnCodes)) 
	{
		setError("msisdnCodes", 38, "MSISDN Codes");
	}
	
	// Validation for Unsubscribe Offer web-service URL.
	// unmark the Unsubscribe Offer web-service URL field.
	/*unMarkError("offerUnsubscribeWSURL");
	if(!validateTextBox("offerUnsubscribeWSURL"))
	{
		setError ( "offerUnsubscribeWSURL" , 0 , "Unsubscribe Offer web-service URL" );			
	}
	else if(!checkTextAreaValLength($("#offerUnsubscribeWSURL").val(),250)){
		setError ( "offerUnsubscribeWSURL" , 25 , "Unsubscribe Offer web-service URL" );	
	}*/

	// Validation for Remove Customer Registration web-service URL.
	// unmark the Remove Customer Registration web-service URL field.
	/*unMarkError("removeCustomerRegistrationWSURL");
	if(!validateTextBox("removeCustomerRegistrationWSURL"))
	{
		setError ( "removeCustomerRegistrationWSURL" , 0 , "Remove Customer Registration web-service URL" );			
	}
	else if(!checkTextAreaValLength($("#removeCustomerRegistrationWSURL").val(),250)){
		setError ( "removeCustomerRegistrationWSURL" , 25 , "Remove Customer Registration web-service URL" );	
	}*/

	if(showValidationErrors("validationMessages_parent")){
		var wsURLObjValue = stringTrim($("#registerCustomerWSURL").val());
		document.getElementById("registerCustomerWSURL").value = wsURLObjValue;
		
		var announcementObjValue = stringTrim($("#announcementMessage").val());
		document.getElementById("announcementMessage").value = announcementObjValue;
		
		return true;
	}
}
	
function updateCustomerStatistics(customerStats) {
	
		$("#xlRegByUserConfirmed").html(formatNumber(customerStats.xlRegByUserConfirmed));
		$("#xlRegByUserPending").html(formatNumber(customerStats.xlRegByUserPending));
		$("#hospRegByUserConfirmed").html(formatNumber(customerStats.hospRegByUserConfirmed));
		$("#hospRegByUserPending").html(formatNumber(customerStats.hospRegByUserPending));
		$("#freeModelRegByUserConfirmed").html(formatNumber(customerStats.freeModelRegByUserConfirmed));
		$("#freeModelRegByUserPending").html(formatNumber(customerStats.freeModelRegByUserPending));
		$("#lastUpdateTimeStamp").html(customerStats.lastUpdateTimeStamp);
}