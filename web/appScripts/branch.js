/**
	This method is called on the event of form submit in "Add Branch"/ 
	"Modify Branch Details" use case. All the fields of "Add Branch" and 
	"Modify Branch Details" forms are validated. The error stack is displayed 
	at the top of the screen.
*/
function validateBranchDetails(){
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	//Trim all the fields
	$("#branchName").val(trim($("#branchName").val()));
	$("#branchStreet").val(trim($("#branchStreet").val()));
	$("#branchRegion").val(trim($("#branchRegion").val()));
	$("#branchCity").val(trim($("#branchCity").val()));

	// Validation for Branch name.
	// unmark the branch field.
	unMarkError("branchName");
	if(!validateTextBox("branchName"))
	{
		setError ( "branchName" , 0 , "Branch Name" );			
	}

	// Validation for Street.
	// unmark the street field.
	unMarkError("branchStreet");
	if(!validateTextBox("branchStreet"))
	{
		setError ( "branchStreet" , 0 , "Street" );			
	}

	// Validation for Region.
	// unmark the Region field.
	unMarkError("branchRegion");
	if(!validateTextBox("branchRegion"))
	{
		setError ( "branchRegion" , 0 , "Region" );			
	}

	// Validation for City.
	// unmark the City field.
	unMarkError("branchCity");
	if(!validateTextBox("branchCity"))
	{
		setError ( "branchCity" , 0 , "City" );			
	}
	
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
}


/**
 * Creates a hidden field for Branch Id before saving the form.
 */
function createBranchId(divId, branchId){	
	var divObj = document.getElementById(divId);
	
	var input = document.createElement("INPUT");
	input.setAttribute("type","hidden");
	input.setAttribute("name","branchId");			
	input.setAttribute("id","branchId");
	input.setAttribute("value",branchId);
	divObj.appendChild(input);
}

/**
 * Check if the user entered branch name exists in the database.
 * @return true if it exists and false otherwise.
 */
function checkForExistingBranchName(branchId){ 
	var branchName= $("#branchName").val();
	//check if the branch name is not null and initiate a DWR call.
	if (null != branchName){
		//if branchId is 0 then validation is against the branch name only. 
		//for modify branch details pass the branch id instead of 0.
		if(branchId == null || stringTrim(branchId) == ""){
			branchId = "0";
		}
		validateBranchName.checkBranchName(branchName, branchId, 
				loadBranchNameCheck);
	}
}

/**
 * Check if the selected branch is associated with any users in the database.
 * @return true if it exists and false otherwise.
 */
function checkBeforeBranchDelete(branchId){ 
	
	if(branchId != null){
		validateBranchBeforeDelete.checkBeforeBranchDelete(branchId, 
				loadBranchDelete);
	}
}