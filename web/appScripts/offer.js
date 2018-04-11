var totalOfferRows = 0;
/* Assign Offer Use Case*/
function validateAssignAll()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();
	// Validation for Offer drop down list.
	// unmark the drop down.
	unMarkError("offerSelected");
	if(!validateDropDown(document.getElementById("offerSelected")))
	{
		setError ("offerSelected" , 1 , "Offer" );			
	}
	
	// Validation for Customers Radio button.
	// unmark the Customers Radio button.
	unMarkError("customers");
	if(!validateRadio("customers"))
	{
		setError ("customers" , 6 , "Customers" );			
	}
	if(showValidationErrors("validationMessages_parent"))
	return true;
	
}

function validateAssignOffer()
{	
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	// Validation for Offer drop down list.
	// unmark the drop down.
	unMarkError("offerSelected");
	if(!validateDropDown(document.getElementById("offerSelected")))
	{
		setError ("offerSelected" , 1 , "Offer" );			
	}
	
	// Validation for Customers Radio button.
	// unmark the Customers Radio button.
	unMarkError("customers");
	if(!validateRadio("customers"))
	{
		setError ("customers" , 6 , "Customers" );			
	}
	else if(get_radio_value("customers") == "selectedCustomers")
	{
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
			
	}
	else {
		unMarkError("fromDate");
		unMarkError("toDate");
	}
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

/* Create Offer Use Case*/

function showOfferPane(divName,index){
// Reset the error list and unmark the errors if any.
	resetErrors();
	unMarkError("label_offerRange");
	
	if(totalOfferRows == 0)
		totalOfferRows = 1;
	
	for(var i =1; i<=index;i++)
	{
		if(!validateTextBox("txt_"+i+"2"))
		{
			setError("label_offerRange",0,"Offered Cover: "+i+"");
		}
		else if(!validateTextForNumbersOnly("txt_"+i+"2")){
			setError ("label_offerRange", 3 , "Offered Cover: "+i+"" );	
		}
		if(!validateTextForCurrencyOnly("txt_"+i+"3")){
			setError ("label_offerRange", 14 , "Offered Cover Charged: "+i+"" );	
		}
	}
	if(showValidationErrors("validationMessages_parent"))
			return true;
	
	$("#"+divName+"").show('slow');
	$("#divAddRemBtn"+index+"").hide('slow');
	totalOfferRows++;
}
function hideOfferPane(divName,index)
{
	$("#"+divName+index+"").hide('slow');
	$("#divAddRemBtn"+(index-1)+"").show('slow');
	$("#txt_"+index+"2").val('');
	$("#txt_"+index+"3").val('');
	totalOfferRows--;
}

// Called when save button is clicked for Additional Offer
function validateCreateOffer()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	// Validation for Offer name.
	// unmark the field.
	unMarkError("offerName");
	if(!validateTextBox("offerName"))
	{
		setError("offerName",0,"Offer Name");
	}
	else if(validateTextForCharactersOnly("offerName"))
	{
		setError("offerName",4,"Offer Name");
	}
		
	// Validation for Offer type.
	// unmark the field.
	unMarkError("offerType");
	if(!validateDropDown(document.getElementById("offerType")))
	{
		setError("offerType",0,"Offer Type");
	}
	
	// Validation for Per Day Deduction Value.
	// unmark the field.
	unMarkError("perDayDeduction");
	if(!validateTextBox("perDayDeduction"))
	{
		setError ( "perDayDeduction" , 0 , "Per Day Deduction" );	
	}
	else if(!validateTextForDecimalPlaces("perDayDeduction")) {
		setError ( "perDayDeduction" , 14 , "Per Day Deduction" );
	}
	else if($("#perDayDeduction").val()==0) {
		setError ( "perDayDeduction" , 14 , "Per Day Deduction" );
	}
	
	// Validation for Offer Range.
	// unmark the field.
	unMarkError("label_offerRange");
	for(var i =1; i<=totalOfferRows;i++)
	{
		if(!validateTextBox("txt_"+i+"2"))
		{
			setError("label_offerRange",0,"Offered Cover: "+i+"");
		}
		else if(!validateTextForNumbersOnly("txt_"+i+"2"))
		{
			setError ("label_offerRange", 3 , "Offered Cover: "+i+"" );	
		}
		
		if(!validateTextForCurrencyOnly("txt_"+i+"3")){
			setError ("label_offerRange", 14 , "Offered Cover Charges: "+i+"" );	
		}
	}
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

function validateCreateMulOffer(numOfRows)
{
	if (isNaN(numOfRows))
		numOfRows=1;
	
	// Reset the error list and unmark the errors if any.
	resetErrors();
	// Validation for Offer name.
	// unmark the field.
	unMarkError("offerName");
	
	if(!validateTextBox("offerName"))
	{
		setError ( "offerName" , 0 , "Offer Name" );			
	}
	else if(validateTextForCharactersOnly("offerName"))
		setError("offerName",4,"Offer Name");
	
	// Validation for Offer drop down list.
	// unmark the drop down.
	unMarkError("offerType");
	if(!validateDropDown(document.getElementById("offerType")))
	{
		setError ("offerType" , 1 , "Offer Type" );			
	}
	
	// Validation for Per Day Deduction Value.
	// unmark the field.
	unMarkError("perDayDeduction");
	if(!validateTextBox("perDayDeduction"))
	{
		setError ( "perDayDeduction" , 0 , "Per Day Deduction" );	
	}
	else if(!validateTextForDecimalPlaces("perDayDeduction")) {
		setError ( "perDayDeduction" , 14 , "Per Day Deduction" );
	}
	else if($("#perDayDeduction").val()==0) {
		setError ( "perDayDeduction" , 14 , "Per Day Deduction" );
	}
	
	// Validation for Multiple Value.
	// unmark the field.
	unMarkError("multiValue");
	if(!validateTextBox("multiValue"))
	{
		setError ( "multiValue" , 0 , "Multiple Value" );	
	}
	else if(!validateTextForNumbersOnly("multiValue"))
	{
		setError ("multiValue" , 3 , "Multiple Value");			
	}
	else if($("#multiValue").val() == "0" || $("#multiValue").val() == "1")
	{
		setError ("multiValue" , 12 , "Multiple Value");
	}
	
	//Validating Offer Range Table
	unMarkError("mulOfferRange");
	for(var i = 1; i <= numOfRows;i++)	
	{
		if(!validateTextForCurrencyOnly("mtxt_"+i+"3")){
			// The following validation is to allow a value of '0' for the first 
			// Offered Cover Charges.
			if(i == 1){
				var objValue = stringTrim($("#"+"mtxt_"+i+"3").val());	
				if(objValue == 0){
					//unMarkError("label_offerRange");
					continue;
				}
			}			
			setError ("label_mulOfferRange", 14 , "Offered Cover Charges: "+i+"" );	
		}	
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
	
}

function validateEditOffer(){
// Reset the error list and unmark the errors if any.
	resetErrors();
//Validating Offer Range Table
unMarkError("label_offerRange");
	for(var i =1; i<9;i++)
	{
		if(!validateTextForCurrencyOnly("txt_"+i+"3")){
			setError ("label_offerRange", 14 , "Offered Cover Charges: "+i+"" );
		}
	}
	if(showValidationErrors("validationMessages_parent"))
			return true;
}

function validateRevokeOffer()
{
	resetErrors();
	unMarkError("offers");
	if(!validateDropDown(document.getElementById("offers")))
	{
		setError("label_offers",0,"Offer Name");
	}
	if(showValidationErrors("validationMessages_parent"))
		return true;
}


// Validate Revoke Offer based on MSISDN

var g_valuesNotValid = [];
var g_valuesNotExisting = [];
var g_valuesNotAssignedToOffer = [];
var temp = [];

function validateRevokeOfferBasedOnMSISDN() {
	resetErrors();
	unMarkError("label_textarea_msisdn");
	
	// Removing the extra characters from textArea
	var msisdn = removeExtraChar($("#msisdnCSV").val());

	if(!validateRadio("selectedRadio")) {
		setError ("label_radio_type" , 6 , "Customers" );
	}
	else if($("#selectedRadio").val() == "basedOnMsisdn") {
		if(!(msisdn.length > 0)) {
			// check if textArea is empty.
			setError("label_textarea_msisdn", 0, "MSISDNS");
		}
		else if (validateCSV(msisdn)) {
			// check if textArea is in valid CSV format.
			setError("label_textarea_msisdn", 34, msisdn);
		}
		else {
			temp.length = 0;
			g_valuesNotValid.length = 0;
			
			var elements = msisdn.split(",");
			for(var i=0; i<elements.length; i++) {
				if (!checkValidMSISDN(elements[i])) {
					g_valuesNotValid.push(elements[i]);
				}else {
					temp.push(elements[i]);
				}
			}
			if(g_valuesNotValid.length > 0)
				setMSISDNErrors(g_valuesNotValid, "label_textarea_msisdn", 3);
		}
	}
	else {
		unMarkError("label_radio_type");
		unMarkError("label_textarea_msisdn");
	}
	if(showValidationErrors("validationMessages_parent"))
		return true;
}


/**
 * DWR Call to check the MSISDN registered and MSISDN assigned to any offer.  
 */
function checkForValidMSISDN() {
	g_valuesNotExisting.length = 0;
	g_valuesNotAssignedToOffer.length = 0;

	if (temp.length > 0) {
		customerId = "0";
		validateOfferMSISDN.checkIfValidMSISDN(temp.toString(), customerId,
				loadMSISDNCheck);
	}
}

function formMSISDNErrorIfExists(validMSISDN) {
	var existingMSISDN = [];
	var assignedMSISDN = [];

	var returnMSISDN = validMSISDN.split("|");
	
	if(returnMSISDN[0].indexOf(",")!= -1){
		existingMSISDN = returnMSISDN[0].split(",");		
	}
	else {
		existingMSISDN.push(returnMSISDN[0]);
	}
	g_valuesNotExisting = filterArray(temp, existingMSISDN);
	setMSISDNErrors(g_valuesNotExisting, "label_textarea_msisdn", 31);
	
	
	if(returnMSISDN[1].indexOf(",")!= -1){
		assignedMSISDN = returnMSISDN[1].split(",");		
	}
	else {
		assignedMSISDN.push(returnMSISDN[1]);
	}
	g_valuesNotAssignedToOffer = filterArray(filterArray(temp, g_valuesNotExisting), assignedMSISDN);
	setMSISDNErrors(g_valuesNotAssignedToOffer, "label_textarea_msisdn", 32);
}

//function to create msisdn errors
function setMSISDNErrors(array, errorLabel, errorCode) {
	if(array.length > 0) {
		setError(errorLabel, errorCode, array.join(", "));
	}
}

function filterArray(sourceArray, targetArray) {
	var seen = [], diff = [];

	for ( var i = 0; i < targetArray.length; i++)
		seen[targetArray[i]] = true;
	for ( var i = 0; i < sourceArray.length; i++)
		if (!seen[sourceArray[i]])
			diff.push(sourceArray[i]);
	return diff;
}

