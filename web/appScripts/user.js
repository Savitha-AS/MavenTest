/**
	This method is called on the event of form submit in "Add User" use case.
	All the fields of "Add User" form is validated. The error stack is 
	displayed at the top of the screen.
*/
function validateAddUser()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();

	//This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));
	
	// Validation for User's first name.
	// unmark the first name field.
	unMarkError("fname");
	if(!validateTextBox("fname"))
	{
		setError ( "fname" , 0 , "First Name" );			
	}
	else if(validateName("fname"))
	{
		setError ( "fname" , 7 , "First Name" );
	}

	// Validation for User's surname.
	// unmark the surname field.
	unMarkError("sname");
	if(!validateTextBox("sname"))
	{
		setError ( "sname" , 0 , "Surname" );			
	}
	else if(validateName("sname"))
	{
		setError ( "sname" , 7 , "Surname" );
	}

	// Validation for User's msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if(!validateTextBox("msisdn"))
	{
		setError ( "msisdn" , 0 , "Mobile Number" );			
	}
	else if(!checkValidNumber("msisdn"))
	{
		setError("msisdn", 3, "Mobile Number");
	}
	else if($("#msisdn").val().length < 10)
	{
		setError("msisdn", 3, "Mobile Number");
	}
	
	// Validation for User's gender.
	// unmark the gender field.
	unMarkError("gender");
	if(!validateRadio("gender"))
	{
		setError ("gender" , 6 , "Gender" );			
	}

	// Validation for User's role.
	// unmark the role field.
	unMarkError("role");
	if(!validateDropDown(document.getElementById("role")))
	{
		setError ("role" , 1 , "User Role" );			
	}
	
	//Calculate age.
	if(!stringTrim($("#dob").val()) == '')
	{
		calculateAge("dob","age");
	}
	
	// Validation for User's age and dob.
	// unmark the age field.
	unMarkError("age");
	if((stringTrim($("#dob").val()) == '') && (stringTrim($("#age").val()) == ''))
	{
		/** 
		 * The following section of code has been commented out as part of 
		 * 	NEW_CR_007 : "Age/DOB is not mandatory"
		 **/	
		//setError("age", 8, "User");
	}	
	else if($("#age").val() < 18 || $("#age").val() > 69)
	{
		setError("age", 10, "User");
	}
	else if(!validateTextForNumbersOnly("age"))
	{
		setError("age", 3, "User Age");
	}
	
	unMarkError("branch");
	if(!validateTextBox("branch"))
	{
		setError ( "branch" , 0 , "Branch" );		
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
	else
		calculateAge("dob","age");
}

/**
	This function is invoked when clear/delete button of User Date of Birth field is clicked.
	Date of Birth and Age field values are cleared and Age field is made editable.
*/
function clearDate(dateObjId, ageObjId)
{
	$("#"+dateObjId+"").val('');
	$("#"+ageObjId+"").val('');
	$("#"+ageObjId+"").attr("readonly","");
}

/**
	This method is called on the event of form submit in "Search User" use case.
	All the fields of "Search User" form is validated. The error stack is diplayed at the top of the screen.
*/
function validateSearchUser()
{
	var noCriteria = true;
	// Reset the error list and unmark the errors if any.
	resetErrors();

	//This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));
	
	// Validation for User's first name.
	// unmark the first name field.
	unMarkError("fname");
	if(stringTrim($("#fname").val()) != '')
	{
		if(validateName("fname"))
		{
			setError ( "fname" , 7 , "First Name" );
		}
		noCriteria= false;
	}

	// Validation for User's surname.
	// unmark the surname field.
	unMarkError("sname");
	if(stringTrim($("#sname").val()) != '')
	{
		if(validateName("sname"))
		{
			setError ( "sname" , 7 , "Surname" );
		}
		noCriteria = false;
	}

	// Validation for User's msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if(stringTrim($("#msisdn").val()) != '')
	{
		if(!checkValidNumber("msisdn"))
		{
			setError("msisdn", 3, "Mobile Number");
		}
		else if($("#msisdn").val().length < 10)
		{
			setError("msisdn", 3, "Mobile Number");
		}
		noCriteria = false;
	}
	
	//Validation for User Id.
	// unmark the User Id field.
	unMarkError("userUid");
	if(stringTrim($("#userUid").val()) != '')
	{
		var userId = $("#userUid").val();
		var userUid = userId.substring(2);
		
		if(!checkForDigit(userUid))
		{
			setError("userUid", 3, "User Id");
		}
		noCriteria = false;
	}
	
	// Validation for User's role.
	// unmark the role field.
	unMarkError("role");
	if(validateDropDown(document.getElementById("role")))
	{
		noCriteria = false;
	}
	
	if(noCriteria == true)
		setError("searchBtn", 9, "Search User");
		
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

/**
	This method is called on the event of form submit in "Reset User Password" use case.
	All the fields of "Reset User Password" form is validated. The error stack is diplayed at the top of the screen.
*/
function validateResetUserPassword()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();

	// Validation for User's first name.
	// unmark the first name field.
	unMarkError("userUid");
	if(!validateTextBox("userUid"))
	{
		setError ( "userUid" , 0 , "User ID" );
	}
	else if(!isAlphaNumeric("userUid"))
	{
		setError("userUid", 5, "User ID");
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

/**
	This method is called on the event of form submit in "Modify User Details" use case.
	All the fields of "Modify User Details" form is validated. The error stack is diplayed at the top of the screen.
*/
function validateModifyUserDetails()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();

	//This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));
	
	// Validation for User's first name.
	// unmark the first name field.
	unMarkError("fname");
	if(!validateTextBox("fname"))
	{
		setError ( "fname" , 0 , "First Name" );			
	}
	else if(validateName("fname"))
	{
		setError ( "fname" , 7 , "First Name" );
	}

	// Validation for User's surname.
	// unmark the surname field.
	unMarkError("sname");
	if(!validateTextBox("sname"))
	{
		setError ( "sname" , 0 , "Surname" );			
	}
	else if(validateName("sname"))
	{
		setError ( "sname" , 7 , "Surname" );
	}
	
	// Validation for User's msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if(!validateTextBox("msisdn"))
	{
		setError ( "msisdn" , 0 , "Mobile Number" );			
	}
	else if(!checkValidNumber("msisdn"))
	{
		setError("msisdn", 3, "Mobile Number");
	}
	else if($("#msisdn").val().length < 10)
	{
		setError("msisdn", 3, "Mobile Number");
	}

	// Validation for User's gender.
	// unmark the gender field.
	unMarkError("gender");
	if(!validateRadio("gender"))
	{
		setError ("gender" , 6 , "Gender" );			
	}

	// Validation for User's role.
	// unmark the role field.
	unMarkError("role");
	if(!validateDropDown(document.getElementById("role")))
	{
		setError ("role" , 1 , "User Role" );
	}
	
	// Validation for User's age and dob.
	// unmark the age field.
	unMarkError("age");
	if((stringTrim($("#dob").val()) == '') && (stringTrim($("#age").val()) == ''))
	{
		/** 
		 * The following section of code has been commented out as part of 
		 * 	NEW_CR_007 : "Age/DOB is not mandatory"
		 **/
		//setError("age", 8, "User");
	}
	else if($("#age").val() < 18 || $("#age").val() > 69)
	{
		setError("age", 10, "User");
	}
	else if(!validateTextForNumbersOnly("age"))
	{
		setError("age", 3, "User Age");
	}
	
	unMarkError("branch");
	if(!validateTextBox("branch"))
	{
		setError ( "branch" , 0 , "Branch" );		
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
	else
		calculateAge("dob","age");
}

/**
 * This method validates the <code>ChangePassword.jsp</code> screen before 
 * submitting the form.
 * 
 * @return
 * 			<b>FALSE</b> upon successful validation and <b>TRUE</b> if failure.
 */
function validateChangePassword()
{
	//Reset all the errors in the "validations_parent" div.
	resetErrors();
	
	//Validation for Current Password.
	//UnMark error on Current Password field before validating.
	unMarkError("currentHash");
	if(!validateTextBox("currentHash"))
	{
		setError("currentHash",0,"Current Password");
	}
	
	//Validation for New Password.
	//UnMark error on New Password field before validating.
	unMarkError("newHash");
	if(!validateTextBox("newHash"))
	{
		setError("newHash",0,"New Password");
	}
	else if($("#newHash").val() == $("#currentHash").val())
	{
		setError("newHash",15,"New Password");
	}
	// Implementation of SOXC576.
	else if($("#newHash").val().length < 8){
		setError("newHash",17,"New Password");
	}
	else if(!validateUserPassword("newHash")){
		setError("newHash",28,"New Password");
	}
	
	//Validation for Confirm New Password.
	//UnMark error on Confirm New Password field before validating.
	unMarkError("confirmHash");
	if(!validateTextBox("confirmHash"))
	{
		setError("confirmHash",0,"Confirm New Password");
	}
	else if($("#newHash").val() != $("#confirmHash").val())
	{
		setError("confirmHash",16,"Confirm New Password");
	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
	
}

/**
 * Creates a hidden field for User Id before saving the form.
 *
 */
function createUserId(divId, userId){	
	var divObj = document.getElementById(divId);
	
	var input = document.createElement("INPUT");
	input.setAttribute("type","hidden");
	input.setAttribute("name","userId");			
	input.setAttribute("id","userId");
	input.setAttribute("value",userId);
	divObj.appendChild(input);
}

/**
 * Check if the user entered MSISDN exists in the database.
 * @return true if it exists and false otherwise.
 */
function checkForExistingMSISDN(userId) 
{ 
	var msisdn= $("#msisdn").val();
	//check if the MSISDN is not null and initiate a DWR call.
	if (null != msisdn){
		//if userID is 0 then validation is against the MSISDN only. 
		//for modify user details pass the user id instead of 0.
		if(userId == null || stringTrim(userId) == ""){
			userId = "0";
		}
		validateMSISDN.checkMSISDN(msisdn, userId, loadMSISDNCheck);
	}
}