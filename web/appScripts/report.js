
/*
Script To validate user inputs for generating customer report
*/
function validateGenCustReport() {
	
	// Reset the error list and unmark the errors if any.
	resetErrors();
	unMarkError("repStyle");
	if (!validateRadio("repStyle")) {
		setError("repStyle", 6, "Report Style");
	}
	else {
		
		if (get_radio_value("repStyle") == 1) {
			// unmark the field.
			unMarkError("reportRange");
			if (!validateDropDown(document.getElementById("reportRange"))) {
				setError("reportRange", 0, "Report Range");
			}
		} 
		else if (get_radio_value("repStyle") == 2) {
			
			// unmark the field.
			unMarkError("regMode");
			if (!validateRadio("regMode")) {
				setError("regMode", 6, "Registration Mode");
			}
			unMarkError("regType");
			if (!validateDropDown(document.getElementById("regType"))) {
				setError("regType", 1, "Product Type");
			}
			unMarkError("confStat");
			if (!validateRadio("confStat")) {
				setError("confStat", 6, "Confirmation Status");
			}
		
			unMarkError("filterOption");
			if (!validateRadio("filterOption")) {
				setError("filterOption", 6, "Filter Option");
			} 
			else {
				
				if ($("input[name='filterOption']:checked").val() == 0) {

					// Validation for From Date
					unMarkError("fromDate");
					var isDateReady = true;
					if (!validateTextBox("fromDate")) {
						setError("fromDate", 0, "From Date");
						isDateReady = false;
					}

					// Validation for To Date.
					unMarkError("toDate");
					if (!validateTextBox("toDate")) {
						setError("toDate", 0, "To Date");
						isDateReady = false;
					}
					if (isDateReady) {
						if (!compareDate('fromDate', 'toDate'))
							setError("fromDate", 11, "From");
					}
				} else if ($("input[name='filterOption']:checked").val() == 1) {
					unMarkError("quickSelectOption");
					if (!validateRadio("quickSelectOption")) {
						setError("quickSelectOption", 6, "Quick Selection");
					}
				}
			}
		}
		else if (get_radio_value("repStyle") == 3) {
			// unmark the field.
			unMarkError("customSelectOption");
			if (!validateRadio("customSelectOption")) {
				setError("customSelectOption", 6, "Custom");
			}
		}
		
	}
	
	if (showValidationErrors("validationMessages_parent"))
		return true;
		
};

/*
	Script To validate user inputs for generating financial report
*/
validateGenFinReport=function(){
	// Reset the error list and unmark the errors if any.
	resetErrors();
	var artifact=new Array();
	artifact[0]="Total no. of Active Customers ";
	artifact[1]="Free Sum Assured ";
	artifact[2]="Paid Sum Assured ";
	artifact[3]="Total Sum Assured ";
	
	for(var i=1;i<=4;i++)
	{
		// unmark the field.
		unMarkError("txt_"+i);
		if(!validateTextBox("txt_"+i)){
			setError("txt_"+i,0,"Report Artifact - "+artifact[(i-1)]);
			$("#reportData").hide('slow');
		} else if(!validateTextForPercent("txt_"+i)){
			setError("txt_"+i,14,"Report Artifact - "+artifact[(i-1)]);
			$("#reportData").hide('slow');
		}
	}
	if(showValidationErrors("validationMessages_parent"))
	return true;
};

function validateDlAgentReport() {
	resetErrors();

	unMarkError("reportType");	
	if (!validateRadio("reportType")) {
		
		setError("reportType", 6, "Report Type");
		
	} else if ( $("input[name='reportType']:checked").val() == 3 ) {
		
		unMarkError("quickSelectOptionForDeducted");
		if (!validateRadio("quickSelectOptionForDeducted")) {
			setError("quickSelectOptionForDeducted", 6, getFieldText("quickSelectOptionForDeducted"));
		}
		
		unMarkError("role");
		if (!validateDropDown(document.getElementById("role"))) {
			setError("role", 6, "Role");
		}
		
	} else {
		
		unMarkError("filterOption");
		if (!validateRadio("filterOption")) {
			
			setError("filterOption", 6, "Filter Option");
			
		}  else {
			if ($("input[name='filterOption']:checked").val() == 0) {
				// Validation for From Date
				unMarkError("fromDate");
				var isDateReady = true;
				if (!validateTextBox("fromDate")) {
					setError("fromDate", 0, "From Date");
					isDateReady = false;
				}

				// Validation for To Date.
				unMarkError("toDate");
				if (!validateTextBox("toDate")) {
					setError("toDate", 0, "To Date");
					isDateReady = false;
				}
				if (isDateReady) {
					if (!compareDate('fromDate', 'toDate'))
						setError("fromDate", 11, "From");
				}
			} else if ($("input[name='filterOption']:checked").val() == 1) {
				 
				unMarkError("quickSelectOption");
				if (!validateRadio("quickSelectOption")) {
					setError("quickSelectOption", 6, "Quick Selection");
				}
			}
			unMarkError("role");
			if (!validateDropDown(document.getElementById("role"))) {
				setError("role", 6, "Role");
			}
		}
		
		
		

	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}



function validateDlWeeklyTigoReport() {
	resetErrors();
	
	// Validation for From Date
	unMarkError("label_dateRangeSelect");
	var isDateReady = true;
	if(!validateTextBox("fromDate"))
	{
		isDateReady = false;
	}

	// Validation for To Date.
	if(!validateTextBox("toDate"))
	{
		isDateReady = false;
	}
	if(isDateReady){
	if(!compareDate('fromDate','toDate'))
		setError ( "label_dateRangeSelect" , 11, "Report Date Range" );
	}
	else {
		setError ( "label_dateRangeSelect" , 0 , "Report Date Range" );
	}
	
	// Validation for Reference Date.
	unMarkError("refDate");
	if(!validateTextBox("refDate"))
	{
		setError ( "refDate" , 0 , "Reference Date" );
	}
	else 
	{
		// To check whether refDate is between fromDate and toDate
		if(isDateReady) {
			if($("#refDate").val()==$("#fromDate").val() || $("#refDate").val()==$("#toDate").val())
			{}
			else 
			{
				if(!compareDate('fromDate', 'refDate')) {
					setError ( "refDate" , 39 , "Reference Date", $("#fromDate").val()+" and "+ $("#toDate").val());
				}
				else if(!compareDate('refDate', 'toDate')) {
					setError ( "refDate" , 39 , "Reference Date", $("#fromDate").val()+" and "+ $("#toDate").val() );
				}
			}
		}
		
	}
	
	// Validation for Registration Type.
	unMarkError("registrationType");
	if(!validateRadio("registrationType"))
	{
		setError ("registrationType", 6, "Registration Type" );
	}
	
	// Validation for Target End Date Registration .
	unMarkError("targetEndDateReg");
	if(!validateTextBox("targetEndDateReg"))
	{
		setError ( "targetEndDateReg" , 0 , "Target End Date Registrations" );	
	}
	else if(!validateTextForNumbersOnly("targetEndDateReg")) {
		setError ( "targetEndDateReg" , 3 , "Target End Date Registrations" );
	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
}

function validateDlRevenueReport() {
	resetErrors();
	
	// Validation for Year/Month.
	var reportYear = document.getElementById('reportYear');
	var reportMonth = document.getElementById('reportMonth');
	var today = new Date();
	unMarkError("label_select");
	if(!validateDropDown(reportYear) || !validateDropDown(reportMonth)) {
		setError ( "label_select" , 6 , "Report Month" );
	}
 	else if ($("#reportYear").val() == today.getFullYear()) {
 		if ($("#reportMonth").val() > (today.getMonth() + 1))
			 setError("label_select", 40, "Report Month",
				"Selected month should not be greater than current month.");
 	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
}

function validateDlCoverageReport() {
	resetErrors();
	
	// Validation for Year/Month.
	var reportYear = document.getElementById('reportYear');
	var reportMonth = document.getElementById('reportMonth');
	var today = new Date();
	unMarkError("label_select");
	if(!validateDropDown(reportYear) || !validateDropDown(reportMonth)) {
		setError ( "label_select" , 6 , "Report Month" );
	}
 	else if ($("#reportYear").val() == today.getFullYear()) {
 		if ($("#reportMonth").val() > (today.getMonth() + 1))
			 setError("label_select", 40, "Report Month",
				"Selected month should not be greater than current month.");
 	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
}


function validateDeductionReport() {
	resetErrors();
	unMarkError("reportType");
	
	if (!validateRadio("reportType")) {
		
		setError("reportType", 6, "Report Type");
		
	}else {
		if(reportType == 0){
			unMarkError("filterOption");
			unMarkError("quickSelectOption");
			unMarkError("monthSel");
			if(!validateRadio("filterOption")){
				setError("filterOption", 6, "Filter Option");
			}else {
				unMarkError("filterOption");
				if ($("input[name='filterOption']:checked").val() == 0) {
					 
					unMarkError("quickSelectOption");
					if (!validateRadio("quickSelectOption")) {
						setError("quickSelectOption", 6, "Quick Selection");
					}
				}else if (!validateDropDown(document.getElementById("monthSel"))) {
					setError("monthSel", 6, "Month Selection");
				}
				
			}
		}else if(reportType == 1){
			if (!validateRadio("quickSelectOptionForQuarter")) {
				setError("quickSelectOptionForQuarter", 6, "Quick Selection");
			}
		}else{
			// to do nothing
		}
		
		
		
		unMarkError("role");
		if (!validateDropDown(document.getElementById("role"))) {
			setError("role", 6, "Role");
		}
	} 

	if (showValidationErrors("validationMessages_parent"))
		return true;
}
