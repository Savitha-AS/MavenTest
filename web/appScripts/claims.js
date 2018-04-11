function validateSearchBeneficiary() {
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

function validateClaimBeneficiary() {
	resetErrors();
	// Validation for gender.
	// unmark the gender field.
	unMarkError("claimedPerson");
	if (!validateRadio("claimedPerson")) {
		setError("claimedPerson", 6, "Person to be claimed");
	}
	if (showValidationErrors("validationMessages_parent"))
		return true;
}

function validateModifyBeneficiary(isHpCustomer, isXlCustomer, isIpCustomer) {
	// Reset the error list and unmark the errors if any.
	resetErrors();

	//This removes additional spaces in the text.
	$("#fname").val(trim($("#fname").val()));
	$("#sname").val(trim($("#sname").val()));

	if ((stringTrim($("#dob").val()) != '')) {
		calculateAge("dob", "age");
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
	if (isXlCustomer == "true") {
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#age").val()) == '')) {
			setError("age", 8, "Customer");
		} else if ($("#age").val() < 18 || $("#age").val() > 69) {
			setError("age", 10, "Customer");
		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}
	}
	if (isIpCustomer == "true" ) {
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#age").val()) == '')) {
			setError("age", 8, "Customer");
		} else if ($("#age").val() < 18 || $("#age").val() > 59) {
			setError("age", 45, "Customer");
		} else if (!validateTextForNumbersOnly("age")) {
			setError("age", 3, "Customer Age");
		}
	}
	if (isHpCustomer == "true" ) {
		unMarkError("age");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#age").val()) == '')) {
			setError("age", 8, "Customer");
		} else if ($("#age").val() < 18 || $("#age").val() > 59) {
			setError("age", 41, "Customer");
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
	/* Validating Relationship details for the customer */
	
	var insMsisdnRadioButtion=$('input:radio[name=benInsMsisdnYesNo]:checked').val();
	var ipMsisdnRadioButtion=$('input:radio[name=benMsisdnYesNo]:checked').val();

	
	if (isXlCustomer == "true") {
		$("#insRelFname").val(trim($("#insRelFname").val()));
		$("#insRelSurname").val(trim($("#insRelSurname").val()));

		if ((stringTrim($("#insRelIrDoB").val()) != '')) {
			calculateAge("insRelIrDoB", "insRelAge");
		}

		if ((stringTrim($("#insRelIrDoB").val()) != '')) {
			calculateAge("insRelIrDoB", "insRelAge");
		}

		// Validation for Relationship drop down list.
		// unmark the drop down.
		unMarkError("relation");
		if (!validateDropDown(document.getElementById("relation"))) {
			setError("relation", 1, "Relationship Name");
		}
		// Validation for first name.
		// unmark the field.
	
		unMarkError("insRelFname");
		var fnameValue = stringTrim($("#insRelFname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("insRelFname", 24, "Insured Relative First Name");
			} else if (validateName("insRelFname")) {
				setError("insRelFname", 7, "Insured Relative First Name");
			}
		}else if (!validateTextBox("insRelFname")) {
			setError("insRelFname", 0, "Insured Relative First Name");
		}

		// Validation for surname.
		// unmark the field.
		unMarkError("insRelSurname");
		var fnameValue = stringTrim($("#insRelSurname").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
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
			setError("insRelAge", 8, "Insured Relative Age");
		} else if ($("#insRelAge").val() < 18 || $("#insRelAge").val() > 69) {
			setError("insRelAge", 10, "Insured Relative Age");
		} else if (!validateTextForNumbersOnly("insRelAge")) {
			setError("insRelAge", 3, "Insured Relative Age");
		}
		
		unMarkError("insMsisdn");
		var insMsisdn = stringTrim($("#insMsisdn").val());
			
		if( insMsisdn.length==0 && insMsisdnRadioButtion=="yes")
		{
			setError("insMsisdn", 0, "Nominee MSISDN");
		}else if (!checkValidNumber("insMsisdn")  && insMsisdnRadioButtion=="yes") {
			setError("insMsisdn", 3, "Nominee MSISDN");
		}
		
	}

	if (isIpCustomer == "true") {
		$("#ipNomFirstName").val(trim($("#ipNomFirstName").val()));
		$("#ipNomSurName").val(trim($("#ipNomSurName").val()));

		// Validation for first name.
		// unmark the field.
	
		unMarkError("ipNomFirstName");
		var fnameValue = stringTrim($("#ipNomFirstName").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("ipNomFirstName", 24, "Nominee First Name");
			} else if (validateName("ipNomFirstName")) {
				setError("ipNomFirstName", 7, "Nominee First Name");
			}
		}else if (!validateTextBox("ipNomFirstName")) {
			setError("ipNomFirstName", 0, "Nominee First Name");
		}

		// Validation for surname.
		// unmark the field.
		unMarkError("ipNomSurName");
		var fnameValue = stringTrim($("#ipNomSurName").val());
		if (fnameValue.length > 0) {
			if (fnameValue.length < 3) {
				setError("ipNomSurName", 24, "Nominee Surname");
			} else if (validateName("ipNomSurName")) {
				setError("ipNomSurName", 7, "Nominee Surname");
			}
		}else if (!validateTextBox("ipNomSurName")) {
			setError("ipNomSurName", 0, "Nominee Surname");
		}

		// Validation for age and dob.
		// unmark the age field.
	
		unMarkError("ipNomAge");
		if ((stringTrim($("#dob").val()) == '')
				&& (stringTrim($("#ipNomAge").val()) == '')) {
			setError("ipNomAge", 8, "Nominee Age");
		} else if ($("#ipNomAge").val() < 18 || $("#ipNomAge").val() > 59) {
			setError("ipNomAge", 45, "Nominee Age");
		} else if (!validateTextForNumbersOnly("age")) {
			setError("ipNomAge", 3, "Nominee Age");
		}
		
		unMarkError("ipInsMsisdn");
		var ipInsMsisdn = stringTrim($("#ipInsMsisdn").val());
		if( ipInsMsisdn.length==0 && ipMsisdnRadioButtion=="yes")
		{
			
			setError("ipInsMsisdn", 0, "Nominee MSISDN");
		}else 	if (!checkValidNumber("ipInsMsisdn")  && ipMsisdnRadioButtion=="yes") {
			
			setError("ipInsMsisdn", 3, "Nominee MSISDN");
		}
	}

	if (showValidationErrors("validationMessages_parent"))
		return true;
	else {
		if (isHpCustomer != "true" && isIpCustomer != "true")
			calculateAge("insRelIrDoB", "insRelAge");

		calculateAge("dob", "age");
	}
}

function clearDate(dateObjId, ageObjId) {
	$("#" + dateObjId + "").val('');
	$("#" + ageObjId + "").val('');
	$("#" + ageObjId + "").attr("readonly", "");
}

/**
 * Check if the entered MSISDN exists in the database.
 * @return true if it exists and false otherwise.
 */
function checkForExistingMSISDN(customerId) {
	var msisdn = $("#msisdn").val();
	//check if the MSISDN is not null and initiate a DWR call.
	if (null != msisdn || stringTrim(msisdn) == "") {
		//if customerId is 0 then validation is against the MSISDN only. 
		//for modify customer details pass the customer id instead of 0.
		if (customerId == null || stringTrim(customerId) == "") {
			customerId = "0";
		}
		validateCustomerMSISDN.checkIfMSISDNExists(msisdn, customerId,
				loadMSISDNCheck);
	}
}
