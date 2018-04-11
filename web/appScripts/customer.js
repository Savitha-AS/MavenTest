function validateAddCustomer(validateBeneficiaryDetails) {
	// Reset the error list and unmark the errors if any.
	resetErrors();

	// This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));

	$("#insRelFname").val(trim($("#insRelFname").val()));
	$("#insRelSurname").val(trim($("#insRelSurname").val()));

	if ((stringTrim($("#dob").val()) != '')) {
		calculateAge("dob", "age");
	}

	if ((stringTrim($("#insRelIrDoB").val()) != '')) {
		calculateAge("insRelIrDoB", "insRelAge");
	}

	// Validation for first name.
	// unmark the field.
	unMarkError("fname");
	var fnameValue = stringTrim($("#fname").val());
	if (fnameValue.length > 0) {
		if (fnameValue.length < 3) {
			setError("fname", 24, "First Name");
		} else if (validateName("fname")) {
			setError("fname", 7, "First Name");
		}
	}else if (!validateTextBox("fname")) {
		setError("fname", 0, "First Name");
	}
	

	// Validation for surname.
	// unmark the field.
	unMarkError("sname");
	var snameValue = stringTrim($("#sname").val());
	if (snameValue.length > 0) {
		if (snameValue.length < 3) {
			setError("sname", 24, "Surname");
		} else if (validateName("sname")) {
			setError("sname", 7, "Surname");
		}
	}else if (!validateTextBox("sname")) {
		setError("sname", 0, "Surname");
	}
	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}
	// Validation for age and dob.
	// unmark the age field.
	unMarkError("age");
	if ((stringTrim($("#dob").val()) == '')
			&& (stringTrim($("#age").val()) == '')) {
		setError("age", 8, "Customer");
	} else if ($("#age").val() < 18 || $("#age").val() > 69) {
		setError("age", 10, "Customer");
	} else if (!validateTextForNumbersOnly("age")) {
		setError("age", 3, "Customer Age");
	}

	// Validation for gender.
	// unmark the gender field.
	unMarkError("gender");
	if (!validateRadio("gender")) {
		setError("gender", 6, "Gender");
	}
	/* Validating Relationship details for the customer */

	if (validateBeneficiaryDetails == "true") {
		// Validation for Relationship drop down list.
		// unmark the drop down.
		unMarkError("insRelation");
		if (!validateDropDown(document.getElementById("insRelation"))) {
			setError("insRelation", 1, "Relationship Name");
		}
		// Validation for ins first name.
		// unmark the field.
		unMarkError("insRelFname");
		var insRelFnameValue = stringTrim($("#insRelFname").val());
		if (insRelFnameValue.length > 0) {
			if (insRelFnameValue.length < 3) {
				setError("insRelFname", 24, "Insured Relative First Name");
			} else if (validateName("insRelFname")) {
				setError("insRelFname", 7, "Insured Relative First Name");
			}
		}else if (!validateTextBox("insRelFname")) {
			setError("insRelFname", 0, "Insured Relative First Name");
		}
		
		// Validation for ins surname.
		// unmark the field.
		unMarkError("insRelSurname");
		var insRelSurnameValue = stringTrim($("#insRelSurname").val());
		if (insRelSurnameValue.length > 0) {
			if (insRelSurnameValue.length < 3) {
				setError("insRelSurname", 24, "Insured Relative Surname");
			} else if (validateName("insRelSurname")) {
				setError("insRelSurname", 7, "Insured Relative Surname");
			}
		}else if (!validateTextBox("insRelSurname")) {
			setError("insRelSurname", 0, "Insured Relative Surname");
		}
		// Validation for age and dob.
		// unmark the age field.
		unMarkError("insRelAge");
		if ((stringTrim($("#insRelIrDoB").val()) == '')
				&& (stringTrim($("#insRelAge").val()) == '')) {
			setError("insRelAge", 8, "Insured Relative");
		} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
			setError("insRelAge", 10, "Insured Relative");
		} else if (!validateTextForNumbersOnly("insRelAge")) {
			setError("insRelAge", 3, "Insured Relative Age");
		}
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
	else {
		if (validateBeneficiaryDetails == "true")
			calculateAge("insRelIrDoB", "insRelAge");

		calculateAge("dob", "age");
	}
}

function clearDate(dateObjId, ageObjId) {
	$("#" + dateObjId + "").val('');
	$("#" + ageObjId + "").val('');
	$("#" + ageObjId + "").attr("readonly", "");
}

function validateSearchCustomer() {

	// Reset the error list and unmark the errors if any.
	resetErrors();
	var searchCriteria = true;
	
	// This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));

	if (!validateTextBox("fname") && !validateTextBox("sname")
			&& !validateTextBox("msisdn") && !validateTextBox("custId")) {
		searchCriteria = false;
	}
	if (!searchCriteria) {
		$("#div_searchResults").hide('slow');
		setError("searchBtn", 9, "Search Criteria");
	}

	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	var msisdnValue = stringTrim($("#msisdn").val());
	if (msisdnValue.length > 0) {
		if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}
	}

	// Validation for first name.
	// unmark the field.
	unMarkError("fname");
	var fnameValue = stringTrim($("#fname").val());
	if (fnameValue.length > 0) {
		if (fnameValue.length < 3) {
			setError("fname", 24, "First Name");
		} else if (validateName("fname")) {
			setError("fname", 7, "First Name");
		}
	}

	// Validation for first name.
	// unmark the field.
	unMarkError("sname");
	var snameValue = stringTrim($("#sname").val());
	if (snameValue.length > 0) {
		if (snameValue.length < 3) {
			setError("sname", 24, "Surname");
		} else if (validateName("sname")) {
			setError("sname", 7, "Surname");
		}
	}
	
	//Validations for Cust ID
	//unmark the field
	unMarkError("custId");
	var custIdValue = stringTrim($("#custId").val());
	if (custIdValue.length > 0) {
		if (!checkForDigits("custId")) {
			setError("custId", 47, "Customer ID ");
		}
	}
	
	if (showValidationErrors("validationMessages_parent"))
		return true;
}

/**
 * Check if the entered MSISDN exists in the database.
 * 
 * @return true if it exists and false otherwise.
 */
function checkForExistingMSISDN(customerId) {
	var msisdn = $("#msisdn").val();
	// check if the MSISDN is not null and initiate a DWR call.
	if (null != msisdn) {
		// if customerId is 0 then validation is against the MSISDN only.
		// for modify customer details pass the customer id instead of 0.
		if (customerId == null || stringTrim(customerId) == "") {
			customerId = "0";
		}
		validateCustomerMSISDN.checkIfMSISDNExists(msisdn, customerId,
				loadMSISDNCheck);
	}
}

function checkMSISDNStatus(msisdn) {

	validateCustomerMSISDN.checkMSISDNStatus(msisdn, loadMSISDNCheck);
}

function checkMSISDNStatusForCoverHistory(msisdn)
{
	validateCustomerMSISDN.checkMSISDNStatusCoverHistory(msisdn, loadMSISDNCheckForExist);
	
}

function validateDeRegisterCustomer() {
	resetErrors();
	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}

function validateChangeDeductionMode() {

	resetErrors();
	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}


function validateCoverHistory() {

	resetErrors();
	// Validation for msisdn.
	
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}



function checkMSISDNStatusForDeductionMode() {
	var msisdn = $("#msisdn").val();
	// check if the MSISDN is not null and initiate a DWR call.
	if (null != msisdn) {
		validateCustomerMSISDN
				.checkMSISDNExist(msisdn, loadMSISDNCheckForExist);
	}
	if (showValidationErrors("validationMessages_parent"))
		return true;

	return false;
}

function validateDeRegisterProductSelected() {
	resetErrors();
	// Validation for deRegisterOption.
	// unmark the msisdn field.
	unMarkError("productId");
	if (!validateCheckBox("productId")) {
		setError("productId", 6, "De-Registration Option");
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}


function validateLoyaltyProductSelected() {
	resetErrors();
	
/*	unMarkError("productId");
	if (!validateCheckBox("productId")) {
		setError("productId", 6, "Products");
	}
	
	
	unMarkError("XLPackage");
	if(($('input:checkbox[value=2]').is(
	':disabled') === false) && ($('input:checkbox[value=2]').is(
	':checked') === true) )
		{
   		if(!validateDropDown(document.getElementById("pakageXL")))
			{
			setError("XLPackage", 1, "XL Package");
			}
		
		}
	
	unMarkError("HPPackage");
	if(($('input:checkbox[value=3]').is(
	':disabled') === false) && ($('input:checkbox[value=3]').is(
	':checked') === true) )
		{
		if(!validateDropDown(document.getElementById("pakageHP")))
			{
			setError("HPPackage", 1, "HP Package");
			}
		
		}
*/
	
	unMarkError("productId");
	if (!validateRadio("productId")) {
		setError("productId", 6, "Products");
	}

	
unMarkError("XLPackage");
	if(($('input:radio[value=2]').is(
	':disabled') === false) && ($('input:radio[value=2]').is(
	':checked') === true) )
		{
   		if(!validateDropDown(document.getElementById("pakageXL")))
			{
			setError("XLPackage", 1, "XL Package");
			}
		
		}
	
	unMarkError("HPPackage");
	if(($('input:radio[value=3]').is(
	':disabled') === false) && ($('input:radio[value=3]').is(
	':checked') === true) )
		{
		if(!validateDropDown(document.getElementById("pakageHP")))
			{
			setError("HPPackage", 1, "HP Package");
			}
		
		}

	
	if (showValidationErrors("validationMessages_parent"))
		return true;
}

function validateDlSignature() {
	resetErrors();

	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
}

function validateRegisterCustomer() {
	resetErrors();
	// Validation for msisdn.
	// unmark the msisdn field.
	unMarkError("msisdn");
	if (!validateTextBox("msisdn")) {
		setError("msisdn", 0, "Mobile Number");
	} else if (!checkValidNumber("msisdn")) {
		setError("msisdn", 3, "Mobile Number");
	}
	if (showValidationErrors("validationMessages_parent"))
		return true;
}

function validateRegCust(isEditable, custExists, isHpCust, isXlCust, isIPCust,
		isPAReg) {
	// Reset the error list and unmark the errors if any.
	resetErrors();

	// This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));

		
	$("#insRelFname").val(trim($("#insRelFname").val()));
	$("#insRelSurname").val(trim($("#insRelSurname").val()));
	
	$("#ipNomFirstName").val(trim($("#ipNomFirstName").val()));
	$("#ipNomSurName").val(trim($("#ipNomSurName").val()));

	unMarkError("productId");
	if (!validateCheckBox("productId")) {
		setError("productId", 43, getFieldText("productId"));
	}
	
	var insMsisdnRadioButtion=$('input:radio[name=benInsMsisdnYesNo]:checked').val();
	var ipMsisdnRadioButtion=$('input:radio[name=benMsisdnYesNo]:checked').val();
		
	// Validations for New Registration
	if (custExists == "false") {
	
		if ((stringTrim($("#dob").val()) != '')) {
			calculateAge("dob", "age");
		}

		// Validation for msisdn.
		// unmark the msisdn field.
		unMarkError("msisdn");
		if (!validateTextBox("msisdn")) {
			setError("msisdn", 0, "Mobile Number");
		} else if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}

		// Validation for first name.
		// unmark the field.
		unMarkError("fname");
		var fnameValue = stringTrim($("#fname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("fname", 24, "First Name");
			} else if (validateName("fname")) {
				setError("fname", 7, "First Name");
			}
		}else if (!validateTextBox("fname")) {
			setError("fname", 0, "First Name");
		}
		

		// Validation for surname.
		// unmark the field.
		unMarkError("sname");
		var snameValue = stringTrim($("#sname").val());
		if (snameValue.length > 0) {
			if (snameValue.length < 3) {
				setError("sname", 24, "Surname");
			} else if (validateName("sname")) {
				setError("sname", 7, "Surname");
			}
		}else if (!validateTextBox("sname")) {
			setError("sname", 0, "Surname");
		}
		
		

		
		

		// Validation for age and dob.
		// unmark the age field.
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#age").val()) == '')) {
			setError("age", 8, "Customer");
		}
		//If Registration is for IP ,customer max age limit shoukd be 59
		else if ($('input:checkbox[value=4]').is(':checked') == true) {
			if ($("#age").val() < 18 || $("#age").val() > 59) {

				setError("age", 45, "Customer");
			}
		}
		// If Registration is for hospitalization, Customer max age limit should
		// be 59.
		else if ($('input:checkbox[value=3]').is(':checked') == true) {
			if ($("#age").val() < 18 || $("#age").val() > 59) {
				setError("age", 41, "Customer");
			}
		}
		// If Registration is for Free Model or XL, Customer max age limit
		// should be 69.
		else if ($("#age").val() < 18 || $("#age").val() > 69) {
			setError("age", 10, "Customer");
		} 
		
		if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}

		// Validation for gender.
		// unmark the gender field.
		unMarkError("gender");
		if (!validateRadio("gender")) {
			setError("gender", 6, "Gender");
		}

		if ($('input:checkbox[value=4]').is(':checked') == true
				&& !validateDropDown(document
						.getElementById("productCoverIdIP"))) {
			setError("productCoverIdIP", 1, getFieldText('productCoverIdIP'));
		}
		


		if ($('input:checkbox[value=2]').is(':checked') == false
				&& $('input:checkbox[value=3]').is(':checked') == true
				&& $('input:checkbox[value=4]').is(':checked') == true) {
			//hp and ip true
			
			if ($("input[type='checkbox']").is(":checked")
					&& !validateDropDown(document
							.getElementById("deductionMode"))) {
				setError("deductionMode", 1, "Deduction Mode");
			}
			
			// Validation for Nominee first name.
			// unmark the field.
			unMarkError("ipNomFirstName");
			var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
			if (ipNomFirstNameValue.length > 0) {
				if (ipNomFirstNameValue.length < 3) {
					setError("ipNomFirstName", 24, "Nominee First Name");
				} else if (validateName("ipNomFirstName")) {
					setError("ipNomFirstName", 7, "Nominee First Name");
				}
			}else if (!validateTextBox("ipNomFirstName")) {
				setError("ipNomFirstName", 0, "Nominee First Name");
			}
			
			// Validation for Nominee surname.
			// unmark the field.
			unMarkError("ipNomSurName");
			var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
			if (ipNomSurNameValue.length > 0) {
				if (ipNomSurNameValue.length < 3) {
					setError("ipNomSurName", 24, "Nominee Surname");
				} else if (validateName("ipNomSurName")) {
					setError("ipNomSurName", 7, "Nominee Surname");
				}
			}else if (!validateTextBox("ipNomSurName")) {
				setError("ipNomSurName", 0, "Nominee Surname");
			}
			
			// Validation for Nominee age .
			// unmark the age field.
			unMarkError("ipNomAge");
			if (stringTrim($("#ipNomAge").val()) == '') {
				setError("ipNomAge", 0, "Nominee Age");
			}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
				setError("ipNomAge", 45, "Nominee Age");
			} else if (!validateTextForNumbersOnly("ipNomAge")) {
				setError("ipNomAge", 3, "Nominee Age");
			}

			
			unMarkError("ipInsMsisdn");
			var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
			if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
			{
				setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
			}else 	if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
				setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
			}


		} else if ($('input:checkbox[value=2]').is(':checked') == true
				&& $('input:checkbox[value=3]').is(':checked') == false
				&& $('input:checkbox[value=4]').is(':checked') == false) {
			//only xl true
			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	

			// Deduction mode

		}else if ($('input:checkbox[value=2]').is(':checked') == true
				&& $('input:checkbox[value=3]').is(':checked') == false
				&& $('input:checkbox[value=4]').is(':checked') == true) {
			//xl and ip true
			
			
			//for XL Product
			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	

			//For IP Product
			// Validation for Nominee first name.
			// unmark the field.
			unMarkError("ipNomFirstName");
			var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
			if (ipNomFirstNameValue.length > 0) {
				if (ipNomFirstNameValue.length < 3) {
					setError("ipNomFirstName", 24, "Nominee First Name");
				} else if (validateName("ipNomFirstName")) {
					setError("ipNomFirstName", 7, "Nominee First Name");
				}
			}else if (!validateTextBox("ipNomFirstName")) {
				setError("ipNomFirstName", 0, "Nominee First Name");
			}
			
			// Validation for Nominee surname.
			// unmark the field.
			unMarkError("ipNomSurName");
			var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
			if (ipNomSurNameValue.length > 0) {
				if (ipNomSurNameValue.length < 3) {
					setError("ipNomSurName", 24, "Nominee Surname");
				} else if (validateName("ipNomSurName")) {
					setError("ipNomSurName", 7, "Nominee Surname");
				}
			}else if (!validateTextBox("ipNomSurName")) {
				setError("ipNomSurName", 0, "Nominee Surname");
			}
			// Validation for Nominee age .
			// unmark the age field.
			unMarkError("ipNomAge");
			if (stringTrim($("#ipNomAge").val()) == '') {
				setError("ipNomAge", 0, "Nominee Age");
			}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
				setError("ipNomAge", 45, "Nominee Age");
			} else if (!validateTextForNumbersOnly("ipNomAge")) {
				setError("ipNomAge", 3, "Nominee Age");
			}

			unMarkError("ipInsMsisdn");
			var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
			
			if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
			{
				setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
			}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
				setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
			}

		}else if ($('input:checkbox[value=2]').is(':checked') == true
				&& $('input:checkbox[value=3]').is(':checked') == true
				&& $('input:checkbox[value=4]').is(':checked') == false) {			
			//for XL Product and hp
			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}
			
			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	

		}
		else if ( $('input:checkbox[value=4]').is(':checked') == true) {
			//only ip true
			
			// Validation for Nominee first name.
			// unmark the field.
			unMarkError("ipNomFirstName");
			var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
			if (ipNomFirstNameValue.length > 0) {
				if (ipNomFirstNameValue.length < 3) {
					setError("ipNomFirstName", 24, "Nominee First Name");
				} else if (validateName("ipNomFirstName")) {
					setError("ipNomFirstName", 7, "Nominee First Name");
				}
			}else if (!validateTextBox("ipNomFirstName")) {
				setError("ipNomFirstName", 0, "Nominee First Name");
			}
			
			// Validation for Nominee surname.
			// unmark the field.
			unMarkError("ipNomSurName");
			var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
			if (ipNomSurNameValue.length > 0) {
				if (ipNomSurNameValue.length < 3) {
					setError("ipNomSurName", 24, "Nominee Surname");
				} else if (validateName("ipNomSurName")) {
					setError("ipNomSurName", 7, "Nominee Surname");
				}
			}else if (!validateTextBox("ipNomSurName")) {
				setError("ipNomSurName", 0, "Nominee Surname");
			}
			
			// Validation for Nominee age .
			// unmark the age field.
			unMarkError("ipNomAge");
			if (stringTrim($("#ipNomAge").val()) == '') {
				setError("ipNomAge", 0, "Nominee Age");
			}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
				setError("ipNomAge", 45, "Nominee Age");
			} else if (!validateTextForNumbersOnly("ipNomAge")) {
				setError("ipNomAge", 3, "Nominee Age");
			}
			
			unMarkError("ipInsMsisdn");
			var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
			
			if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes" )
			{
				setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
			}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
				setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
			}


		}
		if (showValidationErrors("validationMessages_parent"))
			return true;

	} else if (custExists == "true" && isXlCust == "true" && isHpCust == "true"
			&& isIPCust == "true" && isEditable == "true") {

		// T,T,T

		if ((stringTrim($("#dob").val()) != '')) {
			calculateAge("dob", "age");
		}

		// Validation for msisdn.
		// unmark the msisdn field.
		unMarkError("msisdn");
		if (!validateTextBox("msisdn")) {
			setError("msisdn", 0, "Mobile Number");
		} else if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}

		// Validation for first name.
		// unmark the field.
		unMarkError("fname");
		var fnameValue = stringTrim($("#fname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("fname", 24, "First Name");
			} else if (validateName("fname")) {
				setError("fname", 7, "First Name");
			}
		}else if (!validateTextBox("fname")) {
			setError("fname", 0, "First Name");
		}
		

		// Validation for surname.
		// unmark the field.
		unMarkError("sname");
		var snameValue = stringTrim($("#sname").val());
		if (snameValue.length > 0) {
			if (snameValue.length < 3) {
				setError("sname", 24, "Surname");
			} else if (validateName("sname")) {
				setError("sname", 7, "Surname");
			}
		}else if (!validateTextBox("sname")) {
			setError("sname", 0, "Surname");
		}

		// Validation for age and dob.
		// unmark the age field.
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#impliedAge").val()) == '')) {
			setError("age", 8, "Customer");
		}
		// If Registration is for IP ,customer max age limit shoukd be 59
		else if ($('input:checkbox[value=4]').is(':checked') == true) {
			if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {

				setError("age", 45, "Customer");
			}
		}
		// If Registration is for hospitalization, Customer max age limit should
		// be 59.
		else if ($('input:checkbox[value=3]').is(':checked') == true) {
			if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
				setError("age", 41, "Customer");
			}
		}
		// If Registration is for Free Model or XL, Customer max age limit
		// should be 69.
		else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
			setError("age", 10, "Customer");
		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}

		// Validation for gender.
		// unmark the gender field.
		if(isEditable == "true")
		{
		unMarkError("gender");
		if (!validateRadio("gender")) {
			setError("gender", 6, "Gender");
			
		}
		
		}

		if ((stringTrim($("#insRelIrDoB").val()) != '')) {
			calculateAge("insRelIrDoB", "insRelAge");
		}
		// Validation for Relationship drop down list.
		// unmark the drop down.
		unMarkError("insRelation");
		if (!validateDropDown(document.getElementById("insRelation"))) {
			setError("insRelation", 1, "Relationship Name");
		}
		// Validation for ins first name.
		// unmark the field.
		unMarkError("insRelFname");
		var insRelFnameValue = stringTrim($("#insRelFname").val());
		if (insRelFnameValue.length > 0) {
			if (insRelFnameValue.length < 3) {
				setError("insRelFname", 24, "Insured Relative First Name");
			} else if (validateName("insRelFname")) {
				setError("insRelFname", 7, "Insured Relative First Name");
			}
		}else if (!validateTextBox("insRelFname")) {
			setError("insRelFname", 0, "Insured Relative First Name");
		}
		
		// Validation for ins surname.
		// unmark the field.
		unMarkError("insRelSurname");
		var insRelSurnameValue = stringTrim($("#insRelSurname").val());
		if (insRelSurnameValue.length > 0) {
			if (insRelSurnameValue.length < 3) {
				setError("insRelSurname", 24, "Insured Relative Surname");
			} else if (validateName("insRelSurname")) {
				setError("insRelSurname", 7, "Insured Relative Surname");
			}
		}else if (!validateTextBox("insRelSurname")) {
			setError("insRelSurname", 0, "Insured Relative Surname");
		}
		// Validation for age and dob.
		// unmark the age field.
		unMarkError("insRelAge");
		if ((stringTrim($("#insRelIrDoB").val()) == '')
				&& (stringTrim($("#insRelAge").val()) == '')) {
			setError("insRelAge", 8, "Insured Relative");
		} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
			setError("insRelAge", 10, "Insured Relative");
		} else if (!validateTextForNumbersOnly("insRelAge")) {
			setError("insRelAge", 3, "Insured Relative Age");
		}
		
		unMarkError("insMsisdn");
		var insMsisdn = stringTrim($("#insMsisdn").val());
			
		if ($('input:checkbox[value=2]').is(':checked') == true )
		{
			if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
			{
				setError("insMsisdn", 0, getFieldText("insMsisdn"));
			}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
				setError("insMsisdn", 3, getFieldText("insMsisdn"));
			}
			
		}	
		
		// Validation for Nominee first name.
		// unmark the field.
		unMarkError("ipNomFirstName");
		var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
		if (ipNomFirstNameValue.length > 0) {
			if (ipNomFirstNameValue.length < 3) {
				setError("ipNomFirstName", 24, "Nominee First Name");
			} else if (validateName("ipNomFirstName")) {
				setError("ipNomFirstName", 7, "Nominee First Name");
			}
		}else if (!validateTextBox("ipNomFirstName")) {
			setError("ipNomFirstName", 0, "Nominee First Name");
		}
		
		// Validation for Nominee surname.
		// unmark the field.
		unMarkError("ipNomSurName");
		var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
		if (ipNomSurNameValue.length > 0) {
			if (ipNomSurNameValue.length < 3) {
				setError("ipNomSurName", 24, "Nominee Surname");
			} else if (validateName("ipNomSurName")) {
				setError("ipNomSurName", 7, "Nominee Surname");
			}
		}else if (!validateTextBox("ipNomSurName")) {
			setError("ipNomSurName", 0, "Nominee Surname");
		}
		
		// Validation for Nominee age .
		// unmark the age field.
		unMarkError("ipNomAge");
		if (stringTrim($("#ipNomAge").val()) == '') {
			setError("ipNomAge", 0, "Nominee Age");
		}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
			setError("ipNomAge", 45, "Nominee Age");
		} else if (!validateTextForNumbersOnly("ipNomAge")) {
			setError("ipNomAge", 3, "Nominee Age");
		}

		unMarkError("ipInsMsisdn");
		var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
		
		if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
		{
			setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
		}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
			setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
		}
		

		if (showValidationErrors("validationMessages_parent"))
			return true;

	}

	else if (custExists == "true" && isXlCust == "true" && isHpCust == "false"
			&& isIPCust == "true") {
        //T,F,T
		
		/**
		 * Customer subscribed for xl and IP
		 * 
		 */

		if ((stringTrim($("#dob").val()) != '')) {
			calculateAge("dob", "age");
		}

		// Validation for msisdn.
		// unmark the msisdn field.
		unMarkError("msisdn");
		if (!validateTextBox("msisdn")) {
			setError("msisdn", 0, "Mobile Number");
		} else if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}

		// Validation for first name.
		// unmark the field.
		unMarkError("fname");
		var fnameValue = stringTrim($("#fname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("fname", 24, "First Name");
			} else if (validateName("fname")) {
				setError("fname", 7, "First Name");
			}
		}else if (!validateTextBox("fname")) {
			setError("fname", 0, "First Name");
		}
		

		// Validation for surname.
		// unmark the field.
		unMarkError("sname");
		var snameValue = stringTrim($("#sname").val());
		if (snameValue.length > 0) {
			if (snameValue.length < 3) {
				setError("sname", 24, "Surname");
			} else if (validateName("sname")) {
				setError("sname", 7, "Surname");
			}
		}else if (!validateTextBox("sname")) {
			setError("sname", 0, "Surname");
		}

		// Validation for age and dob.
		// unmark the age field.

		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#impliedAge").val()) == '')) {
			setError("age", 8, "Customer");
		}
		// If customer registers for HP (ie checkbox is not disabled) only
		// then the age is restricted to 59.
		else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
			setError("age", 45, "Customer");

		} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
			setError("age", 10, "Customer");

		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}

	
		//IP Product
		// Validation for Nominee first name.
		// unmark the field.
		unMarkError("ipNomFirstName");
		var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
		if (ipNomFirstNameValue.length > 0) {
			if (ipNomFirstNameValue.length < 3) {
				setError("ipNomFirstName", 24, "Nominee First Name");
			} else if (validateName("ipNomFirstName")) {
				setError("ipNomFirstName", 7, "Nominee First Name");
			}
		}else if (!validateTextBox("ipNomFirstName")) {
			setError("ipNomFirstName", 0, "Nominee First Name");
		}
		
		// Validation for Nominee surname.
		// unmark the field.
		unMarkError("ipNomSurName");
		var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
		if (ipNomSurNameValue.length > 0) {
			if (ipNomSurNameValue.length < 3) {
				setError("ipNomSurName", 24, "Nominee Surname");
			} else if (validateName("ipNomSurName")) {
				setError("ipNomSurName", 7, "Nominee Surname");
			}
		}else if (!validateTextBox("ipNomSurName")) {
			setError("ipNomSurName", 0, "Nominee Surname");
		}
		// Validation for Nominee age .
		// unmark the age field.
		unMarkError("ipNomAge");
		if (stringTrim($("#ipNomAge").val()) == '') {
			setError("ipNomAge", 0, "Nominee Age");
		}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
			setError("ipNomAge", 45, "Nominee Age");
		} else if (!validateTextForNumbersOnly("ipNomAge")) {
			setError("ipNomAge", 3, "Nominee Age");
		}
		
		unMarkError("ipInsMsisdn");
		var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
		
		if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
		{
			setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
		}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
			setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
		}
		
		// Validation for gender.
		// unmark the gender field.
		if(isEditable == "true")
		{
		unMarkError("gender");
		if (!validateRadio("gender")) {
			setError("gender", 6, "Gender");
			
		}
		
		}


			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}
			
			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
			
			
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	
   
	

			if (showValidationErrors("validationMessages_parent"))
				return true;
		
	

	} else if (custExists == "true" && isXlCust == "true" && isHpCust == "true"
			&& isIPCust == "false") {

		//T,T,F
		/**
		 * Customer subscribed for xl and IP
		 * 
		 */
		
		if (isEditable == "true") {

			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			}else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}
			

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			}else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.

			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}
			// If customer registers for HP (ie checkbox is not disabled) only
			// then the age is restricted to 59.
			else if ($('input:checkbox[value=4]').is(':checked') == true
					&& ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59)) {
				setError("age", 45, "Customer");

			} else if ($('input:checkbox[value=3]').is(':checked') == true
					&& ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59)) {
				setError("age", 41, "Customer");

			} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");

			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}

			if ($('input:checkbox[value=4]').is(':checked') == true
					&& !validateDropDown(document
							.getElementById("productCoverIdIP"))) {
				setError("productCoverIdIP", 1,
						getFieldText('productCoverIdIP'));
			}
			
			if ($('input:checkbox[value=4]').is(':checked') == true){
				//IP Product
				// Validation for Nominee first name.
				// unmark the field.
				unMarkError("ipNomFirstName");
				var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
				if (ipNomFirstNameValue.length > 0) {
					if (ipNomFirstNameValue.length < 3) {
						setError("ipNomFirstName", 24, "Nominee First Name");
					} else if (validateName("ipNomFirstName")) {
						setError("ipNomFirstName", 7, "Nominee First Name");
					}
				}else if (!validateTextBox("ipNomFirstName")) {
					setError("ipNomFirstName", 0, "Nominee First Name");
				}
				
				// Validation for Nominee surname.
				// unmark the field.
				unMarkError("ipNomSurName");
				var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
				if (ipNomSurNameValue.length > 0) {
					if (ipNomSurNameValue.length < 3) {
						setError("ipNomSurName", 24, "Nominee Surname");
					} else if (validateName("ipNomSurName")) {
						setError("ipNomSurName", 7, "Nominee Surname");
					}
				}else if (!validateTextBox("ipNomSurName")) {
					setError("ipNomSurName", 0, "Nominee Surname");
				}
				// Validation for Nominee age .
				// unmark the age field.
				unMarkError("ipNomAge");
				if (stringTrim($("#ipNomAge").val()) == '') {
					setError("ipNomAge", 0, "Nominee Age");
				}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
					setError("ipNomAge", 45, "Nominee Age");
				} else if (!validateTextForNumbersOnly("ipNomAge")) {
					setError("ipNomAge", 3, "Nominee Age");
				}
				
				unMarkError("ipInsMsisdn");
				var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
				if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
				{
					setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
				}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes" ) {
					setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
				}
			}
			

			/*
			 * If Hp Customer fills Insured relative details validate insured
			 * relative details
			 */

			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}
			
			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
			
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	
			

			if (showValidationErrors("validationMessages_parent"))
				return true;
		

		} else {
			
			if($("#reactivationMsg").val().length > 0 )
				{				 
				
				 
				 
				}
			else
				{				
				
				// agent do not  have any edit option
				
				if ($('input:checkbox[value=4]').is(':checked') == false) {
					setError("productId", 43, getFieldText("productId"));
				}
				
				if ($('input:checkbox[value=4]').is(':checked') == true
						&& !validateDropDown(document
								.getElementById("productCoverIdIP"))) {
					setError("productCoverIdIP", 1,
							getFieldText('productCoverIdIP'));
					
				}
				
				if ($('input:checkbox[value=4]').is(':checked') == true
						&& ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59)) {
					setError("age", 45, "Customer");

				}
				
				if ($('input:checkbox[value=4]').is(':checked') == true
						&& validateDropDown(document
								.getElementById("productCoverIdIP"))) {
					
					
					unMarkError("ipNomFirstName");
					var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
					if (ipNomFirstNameValue.length > 0) {
						if (ipNomFirstNameValue.length < 3) {
							setError("ipNomFirstName", 24, "Nominee First Name");
						} else if (validateName("ipNomFirstName")) {
							setError("ipNomFirstName", 7, "Nominee First Name");
						}
					}else if (!validateTextBox("ipNomFirstName")) {
						setError("ipNomFirstName", 0, "Nominee First Name");
					}
					
					// Validation for Nominee surname.
					// unmark the field.
					unMarkError("ipNomSurName");
					var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
					if (ipNomSurNameValue.length > 0) {
						if (ipNomSurNameValue.length < 3) {
							setError("ipNomSurName", 24, "Nominee Surname");
						} else if (validateName("ipNomSurName")) {
							setError("ipNomSurName", 7, "Nominee Surname");
						}
					}else if (!validateTextBox("ipNomSurName")) {
						setError("ipNomSurName", 0, "Nominee Surname");
					}
					// Validation for Nominee age .
					// unmark the age field.
					unMarkError("ipNomAge");
					if (stringTrim($("#ipNomAge").val()) == '') {
						setError("ipNomAge", 0, "Nominee Age");
					}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
						setError("ipNomAge", 45, "Nominee Age");
					} else if (!validateTextForNumbersOnly("ipNomAge")) {
						setError("ipNomAge", 3, "Nominee Age");
					}
					
					
					unMarkError("ipInsMsisdn");
					var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
					
					if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
					{
						setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
					}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
						setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
					}
					
				}
				
				}
			
			
			
			
			

			if (showValidationErrors("validationMessages_parent"))
				return true;

		}
	} else if (custExists == "true" && isXlCust == "true"
			&& isHpCust == "false" && isIPCust == "false") {

		// T,F,F
		/**
		 * Customer subscribed for xl
		 * 
		 */

		if (isEditable == "true") {

			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			} else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			} else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.

			if ($('input:checkbox[value=2]').is(':checked') == true
					&& $('input:checkbox[value=3]').is(':checked') == true
					|| $('input:checkbox[value=4]').is(':checked') == true) {

				unMarkError("age");
				if ((stringTrim($("#dob").val()) == '')
						&& (stringTrim($("#impliedAge").val()) == '')) {
					setError("age", 8, "Customer");
				}
				// If customer registers for HP (ie checkbox is not disabled)
				// only
				// then the age is restricted to 59.
				else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
					if($('input:checkbox[value=3]').is(':checked') == true   )
						{
						
						setError("age", 41, "Customer");
						}
					else
						{
						  setError("age", 45, "Customer");
						}

				} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
					setError("age", 10, "Customer");

				} else if (!validateTextForNumbersOnly("age")) {
					setError("age", 3, "Customer Age");
				}

			} else {
				unMarkError("age");
				if ((stringTrim($("#dob").val()) == '')
						&& (stringTrim($("#impliedAge").val()) == '')) {
					setError("age", 8, "Customer");
				} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
					setError("age", 10, "Customer");

				} else if (!validateTextForNumbersOnly("age")) {
					setError("age", 3, "Customer Age");
				}
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}

			if ($('input:checkbox[value=4]').is(':checked') == true
					&& !validateDropDown(document
							.getElementById("productCoverIdIP"))) {
				setError("productCoverIdIP", 1,
						getFieldText('productCoverIdIP'));
			}
			/*
			 * If Hp Customer fills Insured relative details validate insured
			 * relative details
			 */

			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			} else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}

			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			} else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	
			
			if ($('input:checkbox[value=4]').is(':checked') == true) {
				// IP Product
				// Validation for Nominee first name.
				// unmark the field.
				unMarkError("ipNomFirstName");
				var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
				if (ipNomFirstNameValue.length > 0) {
					if (ipNomFirstNameValue.length < 3) {
						setError("ipNomFirstName", 24, "Nominee First Name");
					} else if (validateName("ipNomFirstName")) {
						setError("ipNomFirstName", 7, "Nominee First Name");
					}
				} else if (!validateTextBox("ipNomFirstName")) {
					setError("ipNomFirstName", 0, "Nominee First Name");
				}

				// Validation for Nominee surname.
				// unmark the field.
				unMarkError("ipNomSurName");
				var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
				if (ipNomSurNameValue.length > 0) {
					if (ipNomSurNameValue.length < 3) {
						setError("ipNomSurName", 24, "Nominee Surname");
					} else if (validateName("ipNomSurName")) {
						setError("ipNomSurName", 7, "Nominee Surname");
					}
				} else if (!validateTextBox("ipNomSurName")) {
					setError("ipNomSurName", 0, "Nominee Surname");
				}
				// Validation for Nominee age .
				// unmark the age field.
				unMarkError("ipNomAge");
				if (stringTrim($("#ipNomAge").val()) == '') {
					setError("ipNomAge", 0, "Nominee Age");
				} else if ($("#ipNomAge").val() < 18
						|| $("#ipNomAge").val() > 59) {
					setError("ipNomAge", 45, "Nominee Age");
				} else if (!validateTextForNumbersOnly("ipNomAge")) {
					setError("ipNomAge", 3, "Nominee Age");
				}
				
				unMarkError("ipInsMsisdn");
				var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
				
				if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
				{
					setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
				}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
					setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
				}
			}
			if (showValidationErrors("validationMessages_parent"))
				return true;

		} else {
			// no editable option

			if (isPAReg == "true") {

				if ($('input:checkbox[value=3]').is(':checked') == false
						&& $('input:checkbox[value=4]').is(':checked') == false) {
					setError("productId", 43, getFieldText("productId"));
				}

				if ($('input:checkbox[value=4]').is(':checked') == true
						&& !validateDropDown(document
								.getElementById("productCoverIdIP"))) {
					
					setError("productCoverIdIP", 1,
							getFieldText('productCoverIdIP'));
				}
				
				//hp product 
				if ($('input:checkbox[value=3]').is(':checked') == true) {
					unMarkError("age");
					if ((stringTrim($("#dob").val()) == '')
							&& (stringTrim($("#impliedAge").val()) == '')) {
						setError("age", 8, "Customer");
					} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
						setError("age", 41, "Customer");

					} else if (!validateTextForNumbersOnly("age")) {
						setError("age", 3, "Customer Age");
					}
				}
				
				
				if ($('input:checkbox[value=4]').is(':checked') == true
						&& validateDropDown(document
								.getElementById("productCoverIdIP"))) {
										
					
					unMarkError("age");
					if ((stringTrim($("#dob").val()) == '')
							&& (stringTrim($("#impliedAge").val()) == '')) {
						setError("age", 8, "Customer");
					} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
						setError("age", 45, "Customer");

					} else if (!validateTextForNumbersOnly("age")) {
						setError("age", 3, "Customer Age");
					}
					
					
					// IP Product
					// Validation for Nominee first name.
					// unmark the field.
					unMarkError("ipNomFirstName");
					var ipNomFirstNameValue = stringTrim($("#ipNomFirstName")
							.val());
					if (ipNomFirstNameValue.length > 0) {
						if (ipNomFirstNameValue.length < 3) {
							setError("ipNomFirstName", 24, "Nominee First Name");
						} else if (validateName("ipNomFirstName")) {
							setError("ipNomFirstName", 7, "Nominee First Name");
						}
					} else if (!validateTextBox("ipNomFirstName")) {
						setError("ipNomFirstName", 0, "Nominee First Name");
					}

					// Validation for Nominee surname.
					// unmark the field.
					unMarkError("ipNomSurName");
					var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
					if (ipNomSurNameValue.length > 0) {
						if (ipNomSurNameValue.length < 3) {
							setError("ipNomSurName", 24, "Nominee Surname");
						} else if (validateName("ipNomSurName")) {
							setError("ipNomSurName", 7, "Nominee Surname");
						}
					} else if (!validateTextBox("ipNomSurName")) {
						setError("ipNomSurName", 0, "Nominee Surname");
					}
					// Validation for Nominee age .
					// unmark the age field.
					unMarkError("ipNomAge");
					if (stringTrim($("#ipNomAge").val()) == '') {
						setError("ipNomAge", 0, "Nominee Age");
					} else if ($("#ipNomAge").val() < 18
							|| $("#ipNomAge").val() > 59) {
						setError("ipNomAge", 45, "Nominee Age");
					} else if (!validateTextForNumbersOnly("ipNomAge")) {
						setError("ipNomAge", 3, "Nominee Age");
					}

					unMarkError("ipInsMsisdn");
					var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
					
					if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
					{
						setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
					}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
						setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
					}
				}

			} else {

				if ($('input:checkbox[value=3]').is(':checked') == false) {
					setError("productId", 43, getFieldText("productId"));
				}
				if ($('input:checkbox[value=3]').is(':checked') == true) {
					unMarkError("age");
					if ((stringTrim($("#dob").val()) == '')
							&& (stringTrim($("#impliedAge").val()) == '')) {
						setError("age", 8, "Customer");
					} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
						setError("age", 41, "Customer");

					} else if (!validateTextForNumbersOnly("age")) {
						setError("age", 3, "Customer Age");
					}
				}
				
				
				
			}

			/*if ($('input:checkbox[value=4]').is(':checked') == true
					&& !validateDropDown(document
							.getElementById("productCoverIdIP"))) {
				setError("productCoverIdIP", 1,
						getFieldText('productCoverIdIP'));
			}*/

			if (showValidationErrors("validationMessages_parent"))
				return true;

		}

	} else if (custExists == "true" && isXlCust == "false"
			&& isHpCust == "true" && isIPCust == "true") {

		//F,T,T
		/**
		 * Customer subscribed for HP and IP
		 * 
		 */

		if (isEditable == "true") {

			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			}else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}
			

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			}else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.

			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}
			// If customer registers for HP (ie checkbox is not disabled) only
			// then the age is restricted to 59.
			else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
				setError("age", 45, "Customer");

			} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");

			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}
			
			// Validation for Nominee first name.
			// unmark the field.
			unMarkError("ipNomFirstName");
			var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
			if (ipNomFirstNameValue.length > 0) {
				if (ipNomFirstNameValue.length < 3) {
					setError("ipNomFirstName", 24, "Nominee First Name");
				} else if (validateName("ipNomFirstName")) {
					setError("ipNomFirstName", 7, "Nominee First Name");
				}
			}else if (!validateTextBox("ipNomFirstName")) {
				setError("ipNomFirstName", 0, "Nominee First Name");
			}
			
			// Validation for Nominee surname.
			// unmark the field.
			unMarkError("ipNomSurName");
			var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
			if (ipNomSurNameValue.length > 0) {
				if (ipNomSurNameValue.length < 3) {
					setError("ipNomSurName", 24, "Nominee Surname");
				} else if (validateName("ipNomSurName")) {
					setError("ipNomSurName", 7, "Nominee Surname");
				}
			}else if (!validateTextBox("ipNomSurName")) {
				setError("ipNomSurName", 0, "Nominee Surname");
			}
			// Validation for Nominee age .
			// unmark the age field.
			unMarkError("ipNomAge");
			if (stringTrim($("#ipNomAge").val()) == '') {
				setError("ipNomAge", 0, "Nominee Age");
			}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
				setError("ipNomAge", 45, "Nominee Age");
			} else if (!validateTextForNumbersOnly("ipNomAge")) {
				setError("ipNomAge", 3, "Nominee Age");
			}
			
			unMarkError("ipInsMsisdn");
			var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
			
			if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
			{
				setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
			}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
				setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
			}


			if ($('input:checkbox[value=2]').is(':checked') == true) {

				unMarkError("age");
				if ((stringTrim($("#dob").val()) == '')
						&& (stringTrim($("#impliedAge").val()) == '')) {
					setError("age", 8, "Customer");
				}

				// If Registration is for Free Model or XL, Customer max age limit
				// should be 69.
				else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
					setError("age", 10, "Customer");
				} else if (!validateTextForNumbersOnly("age")) {
					setError("age", 3, "Customer Age");
				}

				if ((stringTrim($("#insRelIrDoB").val()) != '')) {
					calculateAge("insRelIrDoB", "insRelAge");
				}
				// Validation for Relationship drop down list.
				// unmark the drop down.
				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}
				
				unMarkError("insMsisdn");
				var insMsisdn = stringTrim($("#insMsisdn").val());
					
				if ($('input:checkbox[value=2]').is(':checked') == true )
				{
					if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
					{
						setError("insMsisdn", 0, getFieldText("insMsisdn"));
					}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
						setError("insMsisdn", 3, getFieldText("insMsisdn"));
					}
					
				}	
				if (showValidationErrors("validationMessages_parent"))
					return true;
				else {
					if (isEditable == "true")
						calculateAge("insRelIrDoB", "insRelAge");

					calculateAge("dob", "age");
				}
			}
			if (showValidationErrors("validationMessages_parent"))
				return true;
		} else {
          
			if ($('input:checkbox[value=2]').is(':checked') == false) {

				setError("productId", 43, getFieldText("productId"));

			}

			if ($('input:checkbox[value=2]').is(':checked') == true) {

				unMarkError("age");
				if ((stringTrim($("#dob").val()) == '')
						&& (stringTrim($("#impliedAge").val()) == '')) {
					setError("age", 8, "Customer");
				}

				// If Registration is for Free Model or XL, Customer max age
				// limit
				// should be 69.
				else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
					setError("age", 10, "Customer");
				} else if (!validateTextForNumbersOnly("age")) {
					setError("age", 3, "Customer Age");
				}

				if ((stringTrim($("#insRelIrDoB").val()) != '')) {
					calculateAge("insRelIrDoB", "insRelAge");
				}
				// Validation for Relationship drop down list.
				// unmark the drop down.
				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}
				
				unMarkError("insMsisdn");
				var insMsisdn = stringTrim($("#insMsisdn").val());
					
				if ($('input:checkbox[value=2]').is(':checked') == true )
				{
					if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
					{
						setError("insMsisdn", 0, getFieldText("insMsisdn"));
					}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
						setError("insMsisdn", 3, getFieldText("insMsisdn"));
					}
					
				}	
			}

			if (showValidationErrors("validationMessages_parent"))
				return true;
		

		}

	}/*
	 * else if (custExists == "true" && isXlCust == "true" && isHpCust ==
	 * "true" && isIPCust == "false"){
	 * 
	 *  }
	 */

	else if (custExists == "true" && isXlCust == "false" && isHpCust == "true"
			&& isIPCust == "false") {

		//F,T,F

		if (isEditable == "false"  ) {
			
			if(isPAReg == "false")
				{
				  
				   if($('input:checkbox[value=2]').is(':checked') == false)
					   {
					   setError("productId", 43, getFieldText("productId"));
					   }
				}
			else 
				{
				
				if($('input:checkbox[value=2]').is(':checked') == false &&
						$('input:checkbox[value=4]').is(':checked') == false)
				{
					setError("productId", 43, getFieldText("productId"));
				}
				}
		}
		
		
		
		if ((stringTrim($("#dob").val()) != '')) {
			calculateAge("dob", "age");
		}

		// Validation for msisdn.
		// unmark the msisdn field.
		unMarkError("msisdn");
		if (!validateTextBox("msisdn")) {
			setError("msisdn", 0, "Mobile Number");
		} else if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}

		// Validation for first name.
		// unmark the field.
		unMarkError("fname");
		var fnameValue = stringTrim($("#fname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("fname", 24, "First Name");
			} else if (validateName("fname")) {
				setError("fname", 7, "First Name");
			}
		}else if (!validateTextBox("fname")) {
			setError("fname", 0, "First Name");
		}
		

		// Validation for surname.
		// unmark the field.
		unMarkError("sname");
		var snameValue = stringTrim($("#sname").val());
		if (snameValue.length > 0) {
			if (snameValue.length < 3) {
				setError("sname", 24, "Surname");
			} else if (validateName("sname")) {
				setError("sname", 7, "Surname");
			}
		}else if (!validateTextBox("sname")) {
			setError("sname", 0, "Surname");
		}

		// Validation for age and dob.
		// unmark the age field.
      
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#impliedAge").val()) == '')) {
			setError("age", 8, "Customer");
		}
		// If customer registers for HP (ie checkbox is not disabled) only
		// then the age is restricted to 59.
		else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
			setError("age", 41, "Customer");

		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}

		// Validation for gender.
		// unmark the gender field.
		
		
		if(isEditable == "true")
		{
		unMarkError("gender");
		if (!validateRadio("gender")) {
			setError("gender", 6, "Gender");
			
		}
		}
			if ($('input:checkbox[value=4]').is(':checked') == true){
			// Validation for Nominee first name.
			// unmark the field.
			unMarkError("ipNomFirstName");
			var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
			if (ipNomFirstNameValue.length > 0) {
				if (ipNomFirstNameValue.length < 3) {
					setError("ipNomFirstName", 24, "Nominee First Name");
				} else if (validateName("ipNomFirstName")) {
					setError("ipNomFirstName", 7, "Nominee First Name");
				}
			}else if (!validateTextBox("ipNomFirstName")) {
				setError("ipNomFirstName", 0, "Nominee First Name");
			}
			
			// Validation for Nominee surname.
			// unmark the field.
			unMarkError("ipNomSurName");
			var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
			if (ipNomSurNameValue.length > 0) {
				if (ipNomSurNameValue.length < 3) {
					setError("ipNomSurName", 24, "Nominee Surname");
				} else if (validateName("ipNomSurName")) {
					setError("ipNomSurName", 7, "Nominee Surname");
				}
			}else if (!validateTextBox("ipNomSurName")) {
				setError("ipNomSurName", 0, "Nominee Surname");
			}
			// Validation for Nominee age .
			// unmark the age field.
			unMarkError("ipNomAge");
			if (stringTrim($("#ipNomAge").val()) == '') {
				setError("ipNomAge", 0, "Nominee Age");
			}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
				setError("ipNomAge", 45, "Nominee Age");
			} else if (!validateTextForNumbersOnly("ipNomAge")) {
				setError("ipNomAge", 3, "Nominee Age");
			}
			
			unMarkError("ipInsMsisdn");
			var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
			
			if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
			{
				setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
			}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
				setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
			}
			
			}			
			
		

		if ($('input:checkbox[value=4]').is(':checked') == true
				&& !validateDropDown(document
						.getElementById("productCoverIdIP"))) {
			setError("productCoverIdIP", 1, getFieldText('productCoverIdIP'));
		}

		if ($('input:checkbox[value=2]').is(':checked') == true) {

			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}

			// If Registration is for Free Model or XL, Customer max age limit
			// should be 69.
			else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");
			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	
			
			if (showValidationErrors("validationMessages_parent"))
				return true;
			else {
				if (isEditable == "true")
					calculateAge("insRelIrDoB", "insRelAge");

				calculateAge("dob", "age");
			}
		}
		if (showValidationErrors("validationMessages_parent"))
			return true;
		else {
			if (isEditable == "true")
				calculateAge("insRelIrDoB", "insRelAge");

			calculateAge("dob", "age");
		}

	}/*else if (custExists == "true" && isXlCust == "true"
	 		&& isHpCust == "false"
				&& isIPCust == "true") {
				T,F,T
		 } */
	/*else if (custExists == "true" && isXlCust == "true"
		&& isHpCust == "false"
		&& isIPCust == "true") {
		//F,T,T
	} */

	else if (custExists == "true" && isXlCust == "false" && isHpCust == "false"
			&& isIPCust == "true") {

		//F,F,T
        
		if (isEditable == "false") {

			if ($('input:checkbox[value=2]').is(':checked') == false) {
				
				setError("productId", 43, getFieldText("productId"));
			}
		}
		

		/*if (isEditable == "false") {
			setError("productId", 43, getFieldText("productId"));
		}*/

		if ((stringTrim($("#dob").val()) != '')) {
			calculateAge("dob", "age");
		}

		// Validation for msisdn.
		// unmark the msisdn field.
		unMarkError("msisdn");
		if (!validateTextBox("msisdn")) {
			setError("msisdn", 0, "Mobile Number");
		} else if (!checkValidNumber("msisdn")) {
			setError("msisdn", 3, "Mobile Number");
		}

		// Validation for first name.
		// unmark the field.
		unMarkError("fname");
		var fnameValue = stringTrim($("#fname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("fname", 24, "First Name");
			} else if (validateName("fname")) {
				setError("fname", 7, "First Name");
			}
		}else if (!validateTextBox("fname")) {
			setError("fname", 0, "First Name");
		}
		

		// Validation for surname.
		// unmark the field.
		unMarkError("sname");
		var snameValue = stringTrim($("#sname").val());
		if (snameValue.length > 0) {
			if (snameValue.length < 3) {
				setError("sname", 24, "Surname");
			} else if (validateName("sname")) {
				setError("sname", 7, "Surname");
			}
		}else if (!validateTextBox("sname")) {
			setError("sname", 0, "Surname");
		}
		// Validation for age and dob.
		// unmark the age field.

		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#impliedAge").val()) == '')) {
			setError("age", 8, "Customer");
		}
		// If customer registers for HP (ie checkbox is not disabled) only
		// then the age is restricted to 59.
		else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
			setError("age", 45, "Customer");

		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}
		
		// Validation for Nominee first name.
		// unmark the field.
		unMarkError("ipNomFirstName");
		var ipNomFirstNameValue = stringTrim($("#ipNomFirstName").val());
		if (ipNomFirstNameValue.length > 0) {
			if (ipNomFirstNameValue.length < 3) {
				setError("ipNomFirstName", 24, "Nominee First Name");
			} else if (validateName("ipNomFirstName")) {
				setError("ipNomFirstName", 7, "Nominee First Name");
			}
		}else if (!validateTextBox("ipNomFirstName")) {
			setError("ipNomFirstName", 0, "Nominee First Name");
		}
		
		// Validation for Nominee surname.
		// unmark the field.
		unMarkError("ipNomSurName");
		var ipNomSurNameValue = stringTrim($("#ipNomSurName").val());
		if (ipNomSurNameValue.length > 0) {
			if (ipNomSurNameValue.length < 3) {
				setError("ipNomSurName", 24, "Nominee Surname");
			} else if (validateName("ipNomSurName")) {
				setError("ipNomSurName", 7, "Nominee Surname");
			}
		}else if (!validateTextBox("ipNomSurName")) {
			setError("ipNomSurName", 0, "Nominee Surname");
		}
		// Validation for Nominee age .
		// unmark the age field.
		unMarkError("ipNomAge");
		if (stringTrim($("#ipNomAge").val()) == '') {
			setError("ipNomAge", 0, "Nominee Age");
		}else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
			setError("ipNomAge", 45, "Nominee Age");
		} else if (!validateTextForNumbersOnly("ipNomAge")) {
			setError("ipNomAge", 3, "Nominee Age");
		}

		unMarkError("ipInsMsisdn");
		var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
		
		if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
		{
			setError("ipInsMsisdn", 0, getFieldText("ipInsMsisdn"));
		}else if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
			setError("ipInsMsisdn", 3, getFieldText("ipInsMsisdn"));
		}

		// Validation for gender.
		// unmark the gender field.
		if(isEditable == "true")
		{
		unMarkError("gender");
		if (!validateRadio("gender")) {
			setError("gender", 6, "Gender");
			
		}
		
		}

		if ($('input:checkbox[value=2]').is(':checked') == true) {

			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}

			// If Registration is for Free Model or XL, Customer max age limit
			// should be 69.
			else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");
			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			unMarkError("insMsisdn");
			var insMsisdn = stringTrim($("#insMsisdn").val());
				
			if ($('input:checkbox[value=2]').is(':checked') == true )
			{
				if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
				{
					setError("insMsisdn", 0, getFieldText("insMsisdn"));
				}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
					setError("insMsisdn", 3, getFieldText("insMsisdn"));
				}
				
			}	
			
			if (showValidationErrors("validationMessages_parent"))
				return true;
			else {
				if (isEditable == "true")
					calculateAge("insRelIrDoB", "insRelAge");

				calculateAge("dob", "age");
			}
		}
		if (showValidationErrors("validationMessages_parent"))
			return true;
		else {
			if (isEditable == "true")
				calculateAge("insRelIrDoB", "insRelAge");

			calculateAge("dob", "age");
		}

	}

	else if (custExists == "true" && isHpCust == "false" && isXlCust == "false") {

		/*
		 * if Existing customer is Not Hp and is not in xl Customer
		 */

		if (isEditable == "true") {

			var type = $("#deductionMode").attr("type");

			if ($("input[type='checkbox']").is(":checked")
					&& !validateDropDown(document
							.getElementById("deductionMode"))) {
				setError("deductionMode", 1, "Deduction Mode");
			}
			/*if (type != "hidden") {
				if (!validateDropDown(document.getElementById("deductionMode"))) {
					setError("deductionMode", 1, "Deduction Mode");
				}

			}*/
			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			}else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}
			

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			}else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.
			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}
			// If customer registers for HP (ie checkbox is not disabled) only
			// then the age is restricted to 59.
			else if (!($('input:checkbox[value=3]').is(':disabled'))
					&& $('input:checkbox[value=3]').is(':checked')) {
				if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
					setError("age", 41, "Customer");

				}
			} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");

			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}
			/*
			 * If Hp Customer fills Insured relative details validate insured
			 * relative details
			 */
			if ((validateDropDown(document.getElementById("insRelation"))
					|| validateTextBox("insRelFname")
					|| validateTextBox("insRelSurname")
					|| validateTextBox("insRelIrDob") || validateTextBox("insRelAge"))) {

				if ((stringTrim($("#insRelIrDoB").val()) != '')) {
					calculateAge("insRelIrDoB", "insRelAge");
				}
				// Validation for Relationship drop down list.
				// unmark the drop down.
				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}
				
				unMarkError("insMsisdn");
				var insMsisdn = stringTrim($("#insMsisdn").val());
					
				if ($('input:checkbox[value=2]').is(':checked') == true )
				{
					if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
					{
						setError("insMsisdn", 0, getFieldText("insMsisdn"));
					}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
						setError("insMsisdn", 3, getFieldText("insMsisdn"));
					}
					
				}	

				if (showValidationErrors("validationMessages_parent"))
					return true;
				else {
					if (isEditable == "true")
						calculateAge("insRelIrDoB", "insRelAge");

					calculateAge("dob", "age");
				}
			}
		} else if (isEditable == "false"
				&& !($('input:checkbox[value=3]').is(':disabled'))
				&& $('input:checkbox[value=3]').is(':checked')) {
			// Case: Not editable, Free/XL Customer with age above 59 tries to
			// register for HP
			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}
			if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
				setError("age", 41, "Customer");

			}
		}

	}

	else if (custExists == "true" && isHpCust == "false") {
		/*
		 * if Existing customer is Not Hp (only) Customer
		 */

		if (isEditable == "true") {

			var type = $("#deductionMode").attr("type");

			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			}else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}
			

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			}else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.
			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}
			// If customer registers for HP (ie checkbox is not disabled) only
			// then the age is restricted to 59.
			else if (!($('input:checkbox[value=3]').is(':disabled'))
					&& $('input:checkbox[value=3]').is(':checked')) {
				if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
					setError("age", 41, "Customer");

				}
			} else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");

			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}
			/*
			 * If Hp Customer fills Insured relative details validate insured
			 * relative details
			 */
			if ((validateDropDown(document.getElementById("insRelation"))
					|| validateTextBox("insRelFname")
					|| validateTextBox("insRelSurname")
					|| validateTextBox("insRelIrDob") || validateTextBox("insRelAge"))) {

				if ((stringTrim($("#insRelIrDoB").val()) != '')) {
					calculateAge("insRelIrDoB", "insRelAge");
				}
				// Validation for Relationship drop down list.
				// unmark the drop down.
				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}

				if (showValidationErrors("validationMessages_parent"))
					return true;
				else {
					if (isEditable == "true")
						calculateAge("insRelIrDoB", "insRelAge");

					calculateAge("dob", "age");
				}
			}
		} else if (isEditable == "false"
				&& !($('input:checkbox[value=3]').is(':disabled'))
				&& $('input:checkbox[value=3]').is(':checked')) {
			// Case: Not editable, Free/XL Customer with age above 59 tries to
			// register for HP
			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}
			if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
				setError("age", 41, "Customer");

			}
		}
	}

	else if (custExists == "true" && isHpCust == "true") {

		// isHpCust: true if customer is only Hospitalization Customer
		if (isEditable == "true") {

			var type = $("#deductionMode").attr("type");

			if ((stringTrim($("#dob").val()) != '')) {
				calculateAge("dob", "age");
			}

			// Validation for msisdn.
			// unmark the msisdn field.
			unMarkError("msisdn");
			if (!validateTextBox("msisdn")) {
				setError("msisdn", 0, "Mobile Number");
			} else if (!checkValidNumber("msisdn")) {
				setError("msisdn", 3, "Mobile Number");
			}

			// Validation for first name.
			// unmark the field.
			unMarkError("fname");
			var fnameValue = stringTrim($("#fname").val());
			if (fnameValue.length > 0) {
				if (fnameValue.length < 3) {
					setError("fname", 24, "First Name");
				} else if (validateName("fname")) {
					setError("fname", 7, "First Name");
				}
			}else if (!validateTextBox("fname")) {
				setError("fname", 0, "First Name");
			}
			

			// Validation for surname.
			// unmark the field.
			unMarkError("sname");
			var snameValue = stringTrim($("#sname").val());
			if (snameValue.length > 0) {
				if (snameValue.length < 3) {
					setError("sname", 24, "Surname");
				} else if (validateName("sname")) {
					setError("sname", 7, "Surname");
				}
			}else if (!validateTextBox("sname")) {
				setError("sname", 0, "Surname");
			}

			// Validation for age and dob.
			// unmark the age field.
			unMarkError("age");
			if ((stringTrim($("#dob").val()) == '')
					&& (stringTrim($("#impliedAge").val()) == '')) {
				setError("age", 8, "Customer");
			}

			// Existing HP customer if registers for free/XL then the age max
			// limit is 69
			/*
			 * if($('input:checkbox[value=3]').is(':checked')){
			 * if($("#impliedAge").val() < 18 || $("#impliedAge").val() > 59) {
			 * setError("age", 41, "Customer"); } }
			 */
			else if ($("#impliedAge").val() < 18 || $("#impliedAge").val() > 69) {
				setError("age", 10, "Customer");

			} else if (!validateTextForNumbersOnly("age")) {
				setError("age", 3, "Customer Age");
			}

			// Validation for gender.
			// unmark the gender field.
			unMarkError("gender");
			if (!validateRadio("gender")) {
				setError("gender", 6, "Gender");
			}

			/*
			 * If Hp Customer fills Insured relative details validate insured
			 * relative details
			 */

			if ($('input:checkbox[value=2]').is(':checked') == true
					&& $('input:checkbox[value=3]').is(':checked') == true) {
				/*if ($("input[type='checkbox']").is(":checked")
						&& !validateDropDown(document
								.getElementById("deductionMode"))) {
					setError("deductionMode", 1, "Deduction Mode");
				}*/

				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}

			}

			if ((validateDropDown(document.getElementById("insRelation"))
					|| validateTextBox("insRelFname")
					|| validateTextBox("insRelSurname")
					|| validateTextBox("insRelIrDob") || validateTextBox("insRelAge"))) {

				if ((stringTrim($("#insRelIrDoB").val()) != '')) {
					calculateAge("insRelIrDoB", "insRelAge");
				}
				// Validation for Relationship drop down list.
				// unmark the drop down.
				unMarkError("insRelation");
				if (!validateDropDown(document.getElementById("insRelation"))) {
					setError("insRelation", 1, "Relationship Name");
				}
				// Validation for ins first name.
				// unmark the field.
				unMarkError("insRelFname");
				var insRelFnameValue = stringTrim($("#insRelFname").val());
				if (insRelFnameValue.length > 0) {
					if (insRelFnameValue.length < 3) {
						setError("insRelFname", 24, "Insured Relative First Name");
					} else if (validateName("insRelFname")) {
						setError("insRelFname", 7, "Insured Relative First Name");
					}
				}else if (!validateTextBox("insRelFname")) {
					setError("insRelFname", 0, "Insured Relative First Name");
				}
				
				// Validation for ins surname.
				// unmark the field.
				unMarkError("insRelSurname");
				var insRelSurnameValue = stringTrim($("#insRelSurname").val());
				if (insRelSurnameValue.length > 0) {
					if (insRelSurnameValue.length < 3) {
						setError("insRelSurname", 24, "Insured Relative Surname");
					} else if (validateName("insRelSurname")) {
						setError("insRelSurname", 7, "Insured Relative Surname");
					}
				}else if (!validateTextBox("insRelSurname")) {
					setError("insRelSurname", 0, "Insured Relative Surname");
				}
				// Validation for age and dob.
				// unmark the age field.
				unMarkError("insRelAge");
				if ((stringTrim($("#insRelIrDoB").val()) == '')
						&& (stringTrim($("#insRelAge").val()) == '')) {
					setError("insRelAge", 8, "Insured Relative");
				} else if ($("#insRelAge").val() < 18
						|| $("#insRelAge").val() > 69) {
					setError("insRelAge", 10, "Insured Relative");
				} else if (!validateTextForNumbersOnly("insRelAge")) {
					setError("insRelAge", 3, "Insured Relative Age");
				}

				if (showValidationErrors("validationMessages_parent"))
					return true;
				else {
					if (isEditable == "true")
						calculateAge("insRelIrDoB", "insRelAge");

					calculateAge("dob", "age");
				}
			}
		}
		/*
		 * If isEditable(past 30 mins since registration) is false and insured
		 * relatives is filled Or XL is checked then register the customer for
		 * freemodel or freeModel+Xl
		 */
		else if (((validateDropDown(document.getElementById("insRelation"))
				|| validateTextBox("insRelFname")
				|| validateTextBox("insRelSurname")
				|| validateTextBox("insRelIrDob") || validateTextBox("insRelAge")) && isHpCust)
				|| ($('input:checkbox[value=2]').is(':checked'))) {

			if ((stringTrim($("#insRelIrDoB").val()) != '')) {
				calculateAge("insRelIrDoB", "insRelAge");
			}
			// Validation for Relationship drop down list.
			// unmark the drop down.
			unMarkError("insRelation");
			if (!validateDropDown(document.getElementById("insRelation"))) {
				setError("insRelation", 1, "Relationship Name");
			}
			// Validation for ins first name.
			// unmark the field.
			unMarkError("insRelFname");
			var insRelFnameValue = stringTrim($("#insRelFname").val());
			if (insRelFnameValue.length > 0) {
				if (insRelFnameValue.length < 3) {
					setError("insRelFname", 24, "Insured Relative First Name");
				} else if (validateName("insRelFname")) {
					setError("insRelFname", 7, "Insured Relative First Name");
				}
			}else if (!validateTextBox("insRelFname")) {
				setError("insRelFname", 0, "Insured Relative First Name");
			}
			
			// Validation for ins surname.
			// unmark the field.
			unMarkError("insRelSurname");
			var insRelSurnameValue = stringTrim($("#insRelSurname").val());
			if (insRelSurnameValue.length > 0) {
				if (insRelSurnameValue.length < 3) {
					setError("insRelSurname", 24, "Insured Relative Surname");
				} else if (validateName("insRelSurname")) {
					setError("insRelSurname", 7, "Insured Relative Surname");
				}
			}else if (!validateTextBox("insRelSurname")) {
				setError("insRelSurname", 0, "Insured Relative Surname");
			}
			// Validation for age and dob.
			// unmark the age field.
			unMarkError("insRelAge");
			if ((stringTrim($("#insRelIrDoB").val()) == '')
					&& (stringTrim($("#insRelAge").val()) == '')) {
				setError("insRelAge", 8, "Insured Relative");
			} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
				setError("insRelAge", 10, "Insured Relative");
			} else if (!validateTextForNumbersOnly("insRelAge")) {
				setError("insRelAge", 3, "Insured Relative Age");
			}

			if (showValidationErrors("validationMessages_parent"))
				return true;
			else {
				if (isEditable == "true")
					calculateAge("insRelIrDoB", "insRelAge");

				calculateAge("dob", "age");
			}
		}
	}
}
