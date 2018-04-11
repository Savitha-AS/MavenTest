/**
	This method is called on the event of form submit in "Register Insurance Provider" use case.
	All the fields of "Register Insurance Provider" form is validated. The error stack is diplayed at the top of the screen.
*/
function validateRegisterInsuranceProvider()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();

	// Validation for Insurance Provider Company's name.
	// unmark the Company name field.
	unMarkError("companyName");
	if(!validateTextBox("companyName"))
	{
		setError ( "companyName" , 0 , "Company Name" );			
	}
	
	
	// Validation for Insurance Provider Company's contact number.
	// unmark the Company contact number.
	unMarkError("companyNumber");
	if(!validateTextBox("companyNumber"))
	{
		setError ( "companyNumber" , 0 , "Company Contact Number" );			
	}
	else if(!checkForDigits("companyNumber"))
	{
		setError("companyNumber", 3, "Company Contact Number");
	}
	else if(!checkForLength("companyNumber",10))
	{
		setError("companyNumber", 19, "Company Contact Number");
	}
	// Validation for Insurance Provider's branch name.
	// unmark the Company's branch name.
	unMarkError("branchName");
	if(!validateTextBox("branchName"))
	{
		setError ( "branchName" , 0 , "Branch Name" );			
	}
	else if(validateName("branchName"))
	{
		setError ( "branchName" , 7 , "Branch Name" );			
	}
	
	// Validation for Insurance Provider's address.
	// unmark the Company's address.
	unMarkError("addressLine1");
	if(!validateTextBox("addressLine1"))
	{
		setError ( "addressLine1" , 0 , "Address Line 1" );			
	}
	
	// Validation for Insurance Provider's city.
	// unmark the Company's city.
	unMarkError("city");
	if(!validateTextBox("city"))
	{
		setError ( "city" , 0 , "City" );			
	}
	else if(validateTextForCharactersOnly("city"))
	{
		setError ( "city" , 4 , "City" );
	}
	
	// Validation for Insurance Provider's state.
	// unmark the Company's state.
	unMarkError("state");
	if(!validateTextBox("state"))
	{
		setError ( "state" , 0 , "State" );			
	}
	else if(validateTextForCharactersOnly("state"))
	{
		setError ( "state" , 4 , "State" );
	}
	
	// Validation for Insurance Provider's country.
	// unmark the Company's country.
	unMarkError("country");
	if(!validateTextBox("country"))
	{
		setError ( "country" , 0 , "Country" );			
	}
	else if(validateTextForCharactersOnly("country"))
	{
		setError ( "country" , 4 , "Country" );
	}
	
	// Validation for Insurance Provider Company's zip code.
	// unmark the Company zip code.
	unMarkError("zipCode");
	if(!validateTextBox("zipCode"))
	{
		setError ( "zipCode" , 0 , "Zip Code" );			
	}
	else if(!checkForDigits("zipCode"))
	{
		setError("zipCode", 3, "Zip Code");
	}
	
	// Validation for Insurance Provider's primary contact person name.
	// unmark the Company's primary contact person name.
	unMarkError("primaryContact");
	if(!validateTextBox("primaryContact"))
	{
		setError ( "primaryContact" , 0 , "Primary Contact" );			
	}
	else if(validateName("primaryContact"))
	{
		setError ( "primaryContact" , 7 , "Primary Contact" );			
	}
	
	// Validation for Insurance Provider Company's primary contact number.
	// unmark the Company primary contact number.
	unMarkError("primaryContactMobile");
	if(!validateTextBox("primaryContactMobile"))
	{
		setError ( "primaryContactMobile" , 0 , "Primary Contact Person Number" );			
	}
	else if(!checkForDigits("primaryContactMobile"))
	{
		setError("primaryContactMobile", 3, "Primary Contact Person Number");
	}
	else if(!checkForLength("primaryContactMobile",10))
	{
		setError("primaryContactMobile", 19, "Primary Contact Person Number");
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

/**
	This method is called on the event of form submit in "Modify Insurance Provider Details" use case.
	All the fields of "Modify Insurance Provider" form is validated. The error stack is diplayed at the top of the screen.
*/
function validateModifyInsuranceProvider()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	// Validation for Insurance Provider Company's contact number.
	// unmark the Company contact number.
	unMarkError("companyNumber");
	if(!validateTextBox("companyNumber"))
	{
		setError ( "companyNumber" , 0 , "Company Contact Number" );			
	}
	else if(!checkForDigits("companyNumber"))
	{
		setError("companyNumber", 3, "Company Contact Number");
	}
	else if(!checkForLength("companyNumber",10))
	{
		setError("companyNumber", 19, "Company Contact Number");
	}
	
	// Validation for Insurance Provider's branch name.
	// unmark the Company's branch name.
	unMarkError("branchName");
	if(!validateTextBox("branchName"))
	{
		setError ( "branchName" , 0 , "Branch Name" );			
	}
	else if(validateName("branchName"))
	{
		setError ( "branchName" , 7 , "Branch Name" );			
	}
	
	// Validation for Insurance Provider's address.
	// unmark the Company's address.
	unMarkError("addressLine1");
	if(!validateTextBox("addressLine1"))
	{
		setError ( "addressLine1" , 0 , "Address Line 1" );			
	}
	
	// Validation for Insurance Provider's city.
	// unmark the Company's city.
	unMarkError("city");
	if(!validateTextBox("city"))
	{
		setError ( "city" , 0 , "City" );			
	}
	else if(validateTextForCharactersOnly("city"))
	{
		setError ( "city" , 4 , "City" );
	}
	
	// Validation for Insurance Provider's state.
	// unmark the Company's state.
	unMarkError("state");
	if(!validateTextBox("state"))
	{
		setError ( "state" , 0 , "State" );			
	}
	else if(validateTextForCharactersOnly("state"))
	{
		setError ( "state" , 4 , "State" );
	}
	
	// Validation for Insurance Provider's country.
	// unmark the Company's country.
	unMarkError("country");
	if(!validateTextBox("country"))
	{
		setError ( "country" , 0 , "Country" );			
	}
	else if(validateTextForCharactersOnly("country"))
	{
		setError ( "country" , 4 , "Country" );
	}
	
	// Validation for Insurance Provider Company's zip code.
	// unmark the Company zip code.
	unMarkError("zipCode");
	if(!validateTextBox("zipCode"))
	{
		setError ( "zipCode" , 0 , "Zip Code" );			
	}
	else if(!checkForDigits("zipCode"))
	{
		setError("zipCode", 3, "Zip Code");
	}
	
	// Validation for Insurance Provider's primary contact person name.
	// unmark the Company's primary contact person name.
	unMarkError("primaryContact");
	if(!validateTextBox("primaryContact"))
	{
		setError ( "primaryContact" , 0 , "Primary Contact" );			
	}
	else if(validateName("primaryContact"))
	{
		setError ( "primaryContact" , 7 , "Primary Contact" );			
	}
	
	// Validation for Insurance Provider Company's primary contact number.
	// unmark the Company primary contact number.
	unMarkError("primaryContactMobile");
	if(!validateTextBox("primaryContactMobile"))
	{
		setError ( "primaryContactMobile" , 0 , "Company Contact Person Number" );			
	}
	else if(!checkForDigits("primaryContactMobile"))
	{
		setError("primaryContactMobile", 3, "Company Contact Person Number");
	}
	else if(!checkForLength("primaryContactMobile",10))
	{
		setError("primaryContactMobile", 19, "Company Contact Person Number");
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

/**
 * Check if the user entered Insurance company name exists in the database.
 * @return true if it exists and false otherwise.
 */
function checkForExistingInsCmp() 
{ 
	var companyName= $("#companyName").val();
	//check if the Company name is not null and initiate a DWR call.
	if (null != companyName){
		validateInsCompName.checkIfInsCompExists(companyName, loadInsCompCheck);
	}
}