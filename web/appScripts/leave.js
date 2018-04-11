/**
	This method is called on the event of form submit in "Apply Leave" use case.
	All the fields of "Apply Leave" form is validated. The error stack is 
	displayed at the top of the screen.
*/

//validations for applying leave
function validateApplyLeave()
{
	//Reset all the errors in the "validations_parent" div.
	resetErrors();
	
	unMarkError("userId");
	if(!validateDropDown(document.getElementById("userId")))
	{
		setError ("userId" , 1 , "Agent Name" );			
	}	
	
	
	unMarkError("reason");
	if(!validateDropDown(document.getElementById("reason")))
	{
		setError ("reason" , 1 , "Reason" );			
	}
	
	
	// Validation for From Date
	// unmark the field.
	unMarkError("fromDate");
	var isDateReady = true;
	if(!validateTextBox("fromDate"))
	{
		setError ( "fromDate" , 0 , "From Date" );	
		isDateReady = false;
	}

	// Validation for To Date.
	// unmark the field.
	unMarkError("toDate");
	if(!validateTextBox("toDate"))
	{
		setError ( "toDate" , 0 , "To Date" );	
		isDateReady = false;
	}
	if(isDateReady){
	if(!compareDate('fromDate','toDate'))
		setError ( "fromDate" , 11 , "From Date" );	
	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
}

function validateViewLeave() {
	resetErrors();
	
	if($("#leaveRange").val()==4) {
		
		// Validation for From Date
		// unmark the field.
		unMarkError("label_dateRange");
		var isDateReady = true;
		if(!validateTextBox("fromDate"))
		{
			isDateReady = false;
		}

		// Validation for To Date.
		// unmark the field.
		unMarkError("label_dateRange");
		if(!validateTextBox("toDate"))
		{
			isDateReady = false;
		}
		if(isDateReady){
		if(!compareDate('fromDate','toDate'))
			setError ( "label_dateRange" , 11 , "Date Range" );	
		}
		else {
			setError ( "label_dateRange" , 0 , "Date Range" );	
		}
	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
}

/**
 * DWR Call to check if leave is already assigned for fromDate - toDate.  
 */
function checkForValidLeaveDates() {
	var userId = $("#userId").val();
	var fromDate = $("#fromDate").val();
	var toDate = $("#toDate").val();
	validateLeaveDates.checkForValidLeaveDates(userId, fromDate, toDate,
			loadLeaveDatesCheck);
}