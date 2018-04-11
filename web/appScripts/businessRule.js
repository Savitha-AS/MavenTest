var totalBusRuleRows = 1;

/* Create Business Rule Use Case*/

function showNextCoverRange(divName,index)
{
	// Reset the error list and unmark the errors if any.
	resetErrors();	
	unMarkError("BusRuleRange");
	for(var i =2; i<=index;i++)
	{
		if(!validateTextForCurrencyOnly("txt_"+i+"3")){
			setError ("label_busRuleRange", 3 , "Usage (From)"+i+"" );
		}
		if(!validateTextForCurrencyOnly("txt_"+i+"4")){
			setError ("label_busRuleRange", 3 , "Usage (To)"+i+"" );	
		}
		if(!validateTextForCurrencyOnly("txt_"+i+"5")){
			setError ("label_busRuleRange", 3 , "Assured Cover"+i+"" );
		}
	}
	if(showValidationErrors("validationMessages_parent"))
			return true;	
	
	$("#"+divName+(index+1)+"").show('slow');
	$("#txt_"+(index+1)+"3").val($("#txt_"+(index)+"4").val());
	totalBusRuleRows++;
	$("#divAddRemBtn"+index+"").hide('slow');
}

function hideCurrentCoverRange(divName,index)
{
	$("#"+divName+index+"").hide('slow');
	totalBusRuleRows--;
	$("#txt_"+(index)+"3").val('');
	$("#txt_"+(index)+"4").val('');
	$("#txt_"+(index)+"5").val('');
	$("#divAddRemBtn"+(index-1)+"").show('slow');
}

// Called when save button is clicked to Create a Business Rule
function validateCreateBR()
{
	// Reset the error list and unmark the errors if any.
	resetErrors();
	
	// Validation for Insurance Company drop down
	// unmark the field.
	unMarkError("selectInsCmpny");
	if(!validateDropDown(document.getElementById("selectInsCmpny")))
		setError("label_selectInsCmpny",0,"Insurance Company");
		
	
	// Validation for Insurance Premium
	// unmark the field.
	unMarkError("premiumAmt");
	if(!validateTextForPremiumPerOnly("premiumAmt"))
		setError("label_premiumAmt",14,"% of Premium Amount");
		
	// Validation for Business Rule Ranges.
	// unmark the field.
	unMarkError("busRuleRange");
	
	//	-- Business rule range 1 validation.
	if(!validateTextForCurrencyOnly("txt_14")){
		setError ("label_busRuleRange", 14 , "Range 1 [To]" );	
	}
	
	if(stringTrim($("#txt_15").val()) != 0 || 
			stringTrim($("#txt_15").val()) == "")
	{
		var objValue = stringTrim($("#txt_15").val());		
		var regex = /^\d?\d?\d?\d?\d(\.[0-9]?[0-9]?[0-9]?[0-9]?)?$/;
		
		if(!regex.test(objValue) || objValue == "")
			setError ("label_busRuleRange", 14 , "Range 1 [Assured Cover]" );		
	}
	//----
	
	for(var i =2; i<=totalBusRuleRows;i++)
	{
		if(!validateTextForCurrencyOnly("txt_"+i+"3")){
			setError ("label_busRuleRange", 14 , "Range "+i+" [From]" );	
		}
		if(!validateTextForCurrencyOnly("txt_"+i+"4")){
			setError ("label_busRuleRange", 14 , "Range "+i+" [To]" );	
		}
		if(!validateTextForCurrencyOnly("txt_"+i+"5")){
			setError ("label_busRuleRange", 14 , "Range "+i+" [Assured Cover]" );
		}
	}
	
	if(showValidationErrors("validationMessages_parent"))
			return true;
}


/**
 * Check if selected Business Rule for Activation is same as the 
 * active Business Rule in DB.
 * @return true if it is same and false otherwise.
 */
function checkForSelectedBR() { 
	var selectBR= getRadioButtonValue('selectBR');	
	validateSelectedBR.isSelectedBRActive(selectBR, loadSelectedBRCheck);
}