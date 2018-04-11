// The error array
var errors = new Array();
// Error count
var errorCount = 0;

/**
 * The var temp = {} is equivalent to creating an object
 * i.e var temp = new Object();
 * temp.abc = 123;
 * 
 * In the object the abc property will contain the value 123.
 * you can access the value as temp["abc"] which will return 123.
 * 
 * if var temp = {abc:123};
 * This is equivalent to the above code.
 */
var errorMessages = 
{
	fieldCannotBeEmpty:"Field should not be empty.",
	invalidDDSelection:"Please choose a value from the Drop down.",
	exists:"The value already exists. Please provide a new value.",
	invalidNumber:"Enter a valid number.",
	invalidCustId:"Enter a valid Customer ID",
	invalidText:"Enter a valid text.",
	alphaNumericOnly:"Please enter only alphanumeric character.",
	radioCheck: "Please select an option.",
	invalidName: "Names can contain only alphabets, one single quote, space and hyphen.",
	dobOrAgeEmpty: "Either Age or Date of Birth required to be filled.",
	noSearchCriteria: "Atleast one search criteria is required.",
	invalidAgeRange: "Age should be between 18 and 69.",
	invalidFromDate: "From Date cannot be greater than To Date.",
	invalidMultipleRange:"Enter a Value between 2 and 9.",
	incompleteOfferSpec:"Incomplete Offer Specification.",
	invalidPattern: "Invalid Value.",
	checkNewPassword: "cannot be same as old password.",
	checkConfirmPassword: "New password and confirm password should be same.",
	checkNewPasswordLength: "must be of atleast 8 characters.",
	doNotMatch:"Invalid Place Holders.",
	inValidLength:"Invalid Length.",
	valueExists:"Entered value already exists. Please enter a new value.",
	alphaOnly:"Please enter only alphabets.",
	wrongPassword:"Entered value is wrong. Retry again with proper values.",
	pwdExistsInHistory:"Entered value already exists in the password history. Retry again with proper values.",
	dataLength: "Please provide atleast 3 characters.",
	textlength250: "Entered text length can not exceed 250 characters.",
	selectedBRActive: "Selected Business Rule is same as the Active Business Rule.",
	invalidSMSContent:"SMS Content should contain all the place holders in the list provided.",
	passwordRules: "must match the following criteria, <ul> <li>A minimum of 8 characters length.</li> <li>At least one upper case alphabet.</li> <li>At least one lower case alphabet.</li> <li>At least one number.</li> <li>At least one special character.</li></ul>",
	deleteBranch: "The selected Branch cannot be de-activated. <valueToReplace> user(s) are associated with this branch.",
	userIdPrefixCase: "must be in UPPER case only.",
	numberNotRegistered: "Mobile numbers are not registered.",
	deregisteredCustomer: "This customer has already been de-registered.",
	invalidMSISDNOprCode:"The operator code of the entered value is invalid.",
	invalidCSV: "Please enter valid comma separated mobile numbers.",
	unconfirmedCustomer: "This customer has not confirmed the registration.",
	valueDoesNotExist: "Entered value does not exist.",
	leaveAlreadyExist: "The leave record already exist for the date(s) : <valueToReplace>",
	invalidMsisdnCode: "Please enter valid comma separated codes.",
	invalidDateRange: "Date should be between <valueToReplace>.",
	unsubscribedCustomer: "This customer has not subscribed to offer.",
	invalidHospAgeRange: "Age should be between 18 and 59 for Hospitalization product.",
	customMessage: "<valueToReplace>",
	noCheckBoxSelected: "Please select a CheckBox",
	invalidRoleNameLength : "must be of atleast 4 characters.",
	invalidIPAgeRange: "Age should be between 18 and 59 for Income Protection product.",
	ageEmpty: "Age required to be filled.",
};

/**
 * Function to retrieve value from the errorMessages.
 * 
 * @param mkey an instance of <tt>String</tt>
 * for which the value needs to be fetched.
 * 
 * @return the value for the key provided.
 */
function getValue( key ) 
{
	return errorMessages[ key ];
}

function getValue( key, valueToReplace)
{
	var errorMessage = errorMessages[ key ];
	
	errorMessage = errorMessage.replace("<valueToReplace>",valueToReplace);
	
	return errorMessage;
}


/**
 * An array which contains the indexing for the values
 * to be fetched from the errorMessages.
 */
var errorMessageIndex = new Array
(
        [0,"fieldCannotBeEmpty"],      
        [1,"invalidDDSelection"],
        [2,"exists"],
	    [3,"invalidNumber"],
	    [4,"invalidText"],
	    [5,"alphaNumericOnly"],
		[6,"radioCheck"],
		[7,"invalidName"],
		[8,"dobOrAgeEmpty"],
		[9,"noSearchCriteria"],
		[10,"invalidAgeRange"],
		[11,"invalidFromDate"],
		[12,"invalidMultipleRange"],
		[13,"incompleteOfferSpec"],
		[14,"invalidPattern"],
		[15,"checkNewPassword"],
		[16,"checkConfirmPassword"],
		[17,"checkNewPasswordLength"],
		[18,"doNotMatch"],
		[19,"inValidLength"],
		[20,"valueExists"],
		[21,"alphaOnly"],
		[22,"wrongPassword"],
		[23,"pwdExistsInHistory"],
		[24,"dataLength"],
		[25,"textlength250"],
		[26,"selectedBRActive"],
		[27,"invalidSMSContent"],
		[28,"passwordRules"],
		[29,"deleteBranch"],
		[30,"userIdPrefixCase"],
		[31,"numberNotRegistered"],
		[32,"deregisteredCustomer"],
		[33,"invalidMSISDNOprCode"],
		[34,"invalidCSV"],
		[35,"unconfirmedCustomer"],
		[36,"valueDoesNotExist"],
		[37,"leaveAlreadyExist"],
		[38,"invalidMsisdnCode"],
		[39,"invalidDateRange"],
		[40,"unsubscribedCustomer"],
		[41,"invalidHospAgeRange"],
		[42,"customMessage"],
        [43,"noCheckBoxSelected"],
        [44,"invalidRoleNameLength"],
        [45,"invalidIPAgeRange"],
        [46,"ageEmpty"],
        [47,"invalidCustId"]
        
        
);

/**
 * This method will mark the error field.
 * 
 * @param id the id of the error field to be highlighted as Error.
 * 
 */
function markError( id ) 
{
	var errorField;
    
	// get the access to the element.// 
	if(id.match('label_') == "label_")
	{
		errorField = document.getElementById(id);
	}
	else
	{
		errorField = document.getElementById('label_'+id);
	}
	
	if ( errorField != null && errorField != undefined && errorField != "undefined" )
    {
    	errorField.style.color = "red";
	}
}

/**
 * This method will un-mark the error field.
 * 
 * @param id the id of the field whose field should be unmarked.
 * 
 */
function unMarkError( id ) 
{
	var errorField;
    
	// get the access to the element.
	errorField = document.getElementById('label_'+id);
	if ( errorField != null && errorField != undefined && errorField != "undefined" )
    {
    	errorField.style.color = "";
	}
}

/**
 * This method will un-mark all the error fields present in the error array at that point of time..
 * 
 */
function unMarkErrorAll() 
{
	if(errors.length > 0)
    {
	    for (var i = 0; i < errors.length ; i++)
	    {
	        var errorArray = errors[i];
	        var errDesc = "" + errorArray[0];
	        unMarkError( errDesc );
	    }
	    
	 } 
}

/**
 * This method will show the error fields
 * 
 * @param error_object_id the id of the div/span or any object for that matter
 * which should be shown to user in case of errors.
 * 
 */
function showValidationErrorDetails(error_object_id) 
{
    //var obj = document.getElementById(error_object_id);
    
    $(document).ready(function(){

		$('#'+error_object_id).show('slow');
	
    }); 
    /*try
    {
    	bookmarkscroll.scrollTo(error_object_id);
    }
    catch(err) 
    {}*/
   // obj.style.display = "block";
}

/**
 * This method will hide the error fields
 * 
 * @param error_object_id the id of the div/span or any object for that matter
 * which should be hidden.
 * 
 */
function hideValidationErrorDetails(error_object_id) 
{
    var obj = document.getElementById(error_object_id);
    obj.style.display = "none";
}

function containsElement(elementArray, element)
	{
		for (var i = 0; i < elementArray.length; i++) 
		{
			if (elementArray[i] == element) 
			{
				return true;
			}
		}
		return false;
	}
	
/**
 * Method to add the error to the error list.
 * 
 * @param fieldName the field name
 * 
 * @param errorMessage
 * @return
 */
function addValidationError( fieldName, errorMessage )
{
	if(!containsElement(errors,fieldName))
    {
		errors[errorCount] = new Array(fieldName,errorMessage);
		errorCount++;
    }
}

/**
 * This method will add the error to the error list.
 * 
 * @param objectId the object Id which has to be marked.
 * 
 * @param errorMessageId the error message id.
 * 
 * @param description the description for the error message
 */
function setError ( objectId , errorMessageId , description ) 
{
	//alert("setError(3) - objectId : "+objectId);
	
	// get the field id instead of name
    var name = document.getElementById(objectId).id;
    
    // Build the error message to be displayed.
    var val = description + " : " + getValue( errorMessageIndex[errorMessageId][1] );
    
    addValidationError( name, val ); 
}

/**
 * This method will add the error to the error list.
 * 
 * @param objectId the object Id which has to be marked.
 * 
 * @param errorMessageId the error message id.
 * 
 * @param description the description for the error message.
 * 
 * @param valueToReplace the value to be replaced in the error message.
 * 
 */
function setError ( objectId , errorMessageId , description, valueToReplace) 
{
	//alert("setError(4) - objectId : "+objectId);
	
	// get the field id instead of name
    var name = document.getElementById(objectId).id;
    
    // Build the error message to be displayed.
   var val = description + " : " + getValue( errorMessageIndex[errorMessageId][1], valueToReplace);
       
    addValidationError( name, val ); 
}


/**
 * Function to clear the place holder for errors.
 * 
 * @param errorBoxId the id of the place holder.
 * 
 */
function clearErrors(errorBoxId)
{
	document.getElementById(errorBoxId).innerHTML = "";
}

/**
 * The method to show errors
 * 
 * @param errorBoxId the id of the place holder for errors.
 * 
 */
function showValidationErrors_old(errorBoxId)
{
	var errorBoxIdChild1 = document.getElementById(errorBoxId).childNodes[0].getAttribute("id");
	var errorBoxIdChild = document.getElementById(errorBoxIdChild1).childNodes[1].getAttribute("id");
	
	clearErrors(errorBoxIdChild);	
	hideValidationErrorDetails(errorBoxId);
	
	var errContent = document.getElementById(errorBoxIdChild);
	
	if(errors.length > 0)
    {
	    for (var i = 0; i < errors.length ; i++)
	    {
	        var errorArray = errors[i];
	        var errDesc = "" + errorArray[0];
			var errCode = errorArray[1];
			
			var tempDiv = document.createElement( "DIV" );
	    	
			var newobj = document.createElement( "LI" );
			newobj.style.padding = "5px 15px";
			newobj.style.margin = "0 0 0 20px";
			//newobj.setAttribute("onclick","javascript:bookmarkscroll.scrollTo("+errDesc+");");						
			//newobj.style.cursor="hand";
			newobj.innerHTML = errCode;
			//newobj.innerHTML = "<strong>" + errCode + "</strong>";
			tempDiv.appendChild(newobj);
			
			errContent.innerHTML = errContent.innerHTML + tempDiv.innerHTML ;
	        
	        markError( errDesc );
	    }
	    
	    showValidationErrorDetails(errorBoxId);
	    
	    return true;
    } 
    else
    {
    	return false;
    }
}

/**
 * The method to show errors
 * 
 * @param errorBoxId the id of the place holder for errors.
 * 
 */
function  showValidationErrors(errorBoxId)
{
	//var errorBoxIdChild = document.getElementById(errorBoxId).childNodes[1].getAttribute("id");
	//var errorBoxIdChild = $("#"+errorBoxId).find(".error-div-body").attr("id");
	
	//Fix to extend support for Mozilla Firefox and other browsers.
	var errorBoxIdChild = $("#"+errorBoxId + ">.error-div-body").attr("id");
	
	clearErrors(errorBoxIdChild);
	
	hideValidationErrorDetails(errorBoxId);
	
	var errContent = document.getElementById(errorBoxIdChild);
	
	if(errors.length > 0)
    {
	    for (var i = 0; i < errors.length ; i++)
	    {
	        var errorArray = errors[i];
	        var errDesc = "" + errorArray[0];
			var errCode = errorArray[1];
			
			var tempDiv = document.createElement( "DIV" );
	    	
			var newobj = document.createElement( "LI" );
			newobj.style.padding = "5px 5px";
			newobj.style.margin = "0 0 0 25px";
			//newobj.setAttribute("onclick","javascript:bookmarkscroll.scrollTo("+errDesc+");");						
			//newobj.style.cursor="hand";
			newobj.innerHTML = errCode;
			tempDiv.appendChild(newobj);
			
			errContent.innerHTML = errContent.innerHTML + tempDiv.innerHTML ;
	        
	        markError( errDesc );
	    }
	    
	    showValidationErrorDetails(errorBoxId);
	    
	    return true;
    } 
    else
    {
    	return false;
    }
}

/**
 * Function to reset the error array.
 * 
 */
function resetErrors()
{
	unMarkErrorAll();
	errors = null;
	errors = new Array();
	errorCount = 0;
}

/**
 * Stores the javascript application messages
 */
var applicationMessages = 
{
	en : 
	{
		staticTextTo:"To",
		staticTextFrom:"From",
		staticTextQuickSelection:"Quick Selection"
	},
	sw_TZ : 
	{
		staticTextTo:"Kwenda",
		staticTextFrom:"Kutoka",
		staticTextQuickSelection:"Chaguo la haraka"
	}
};
/**
 * Returns the message for particular key
 * @param key
 * @returns Corresponding message for the key
 */
function getMessage( key ) 
{	
	return applicationMessages[ locale ][ key ];
}

/**
 * Remove the mandatory star from the field text
 */
function getFieldText( objectId ) {
	var mandatoryStar;
	var errorLabelText;
	if(objectId.indexOf('label_') == -1) {
		mandatoryStar = $("#label_"+ objectId +" .mandatoryStar").text();
		errorLabelText = $("#label_"+objectId).text();
	}
	else {
		mandatoryStar = $("#"+ objectId +" .mandatoryStar").text();
		errorLabelText = $("#"+objectId).text();
	}
	var starIndex = errorLabelText.indexOf(mandatoryStar);
	if(mandatoryStar.length > 0)
		errorLabelText = errorLabelText.replace(errorLabelText.charAt(starIndex), "");
	/*var colonIndex = errorLabelText.indexOf(":");
	if(colonIndex.length == 0)
		errorLabelText = errorLabelText + " : ";*/
	return errorLabelText;
}