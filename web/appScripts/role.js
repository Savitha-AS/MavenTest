/**
	This method is called on the event of form submit in "Add Role" use case.
	All the fields of "Add Role" form is validated. The error stack is 
	displayed at the top of the screen.
*/
function validateAddRole()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();

	//This removes additional spaces in the text.
	$("#roleName").val(trim($("#roleName").val()));
	$("#roleDescription").val(trim($("#roleDescription").val()));
	
	// Validation for Role's name.
	// unmark the first name field.
	unMarkError("roleName");
	if(!validateTextBox("roleName"))
	{
		setError ( "roleName" , 0 ,"Role Name");			
	}
	else if(validateName("roleName"))
	{
		setError ( "roleName" , 7 , "Role Name");
	}
	else if(!checkForRoleNameLength("roleName", 4))
	{
		setError ( "roleName" , 44 , "Role Name : ");
	}

/*	// Validation for Role's description.
	// unmark the surname field.
	unMarkError("roleDescription");
	if(!validateTextBox("roleDescription"))
	{
		setError ( "roleDescription" , 0 , "Role Description");			
	}*/
	
	unMarkError("roleMenu");
	if(!validateCheckBox(document.getElementsByName("parentMenuCheckBox")) && 
			!validateCheckBox(document.getElementsByName("childMenuCheckBox")))
	{
		setError("label_roleMenu", 43, getFieldText('roleMenu'));
	 // setError ( "parentMenuString" , 43 , "Menu : ");	
	}
}

function validateModifyRole()
{
	//Reset the error list and unmark the errors if any.
	resetErrors();
	$("#role").val(trim($("#role").val()));
	unMarkError("role");
	if(!validateDropDown(document.getElementById("role"))){
		setError("role", 0, "Role");
	}
	
	unMarkError("roleMenu");
	if(!validateCheckBox(document.getElementsByName("parentMenuCheckBox")) && 
			!validateCheckBox(document.getElementsByName("childMenuCheckBox")))
	{
		setError("label_roleMenu", 43, "Role Menu");
		// setError ( "parentMenuString" , 43 , "Menu : ");	
	}
}

/**
 * Check if the user entered MSISDN exists in the database.
 * @return true if it exists and false otherwise.
 */
function checkForExistingRole(roleId) 
{ 
	var roleName= $("#roleName").val();
	//check if the role name is not null and initiate a DWR call.
	
	//if roleID is 0 then validation is against the role name only. 
	//for modify role details pass the role id instead of 0.
	if(roleId == null || stringTrim(roleId) == ""){
		roleId = "0";
	}
	
	validateAddRole.checkIfRoleExists(roleName, roleId, loadRoleCheck);
	
}

function checkChilds(childRoleVO)
{
	if(childRoleVO.childMenuString != null) {		
		var childSplit = childRoleVO.childMenuString.split(',');
		
		for(var i = 0; i < childSplit.length; i++){
			$("#"+childSplit[i]+"").attr('checked', 'checked');
		}
	}
}

function uncheckChilds(childRoleVO)
{
	if(childRoleVO.childMenuString != null) {		
		var childSplit = childRoleVO.childMenuString.split(',');
		
		for(var i = 0; i < childSplit.length; i++){
			$("#"+childSplit[i]+"").attr('checked', false);
		}
	}
}

function checkOrUncheckParent(parentMenuId, childRoleVO){
	if(childRoleVO.childMenuString != null){
		var childSplit = childRoleVO.childMenuString.split(',');
		var checkedChildCount = 0;
		for(var i = 0; i < childSplit.length; i++){
			if(document.getElementById(childSplit[i]).checked) {
				checkedChildCount++;
			}
		}
		if(checkedChildCount > 0){
			$("#"+parentMenuId+"").attr('checked', 'checked');
		}
		if(checkedChildCount == 0){
			$("#"+parentMenuId+"").attr('checked', false);
		}
	}
}