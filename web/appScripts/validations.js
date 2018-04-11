var one_day = 1000 * 60 * 60 * 24;
var one_month = 1000 * 60 * 60 * 24 * 30;
var one_year = 1000 * 60 * 60 * 24 * 30 * 12;

function displayage(yr, mon, day, unit, decimal, round) {
	today = new Date();
	var pastdate = new Date(yr, mon - 1, day);

	var countunit = unit;
	var decimals = decimal;
	var rounding = round;

	finalunit = (countunit == "days") ? one_day
			: (countunit == "months") ? one_month : one_year;
	decimals = (decimals <= 0) ? 1 : decimals * 10;

	if (unit != "years") {
		if (rounding == "rounddown")
			document.write(Math.floor((today.getTime() - pastdate.getTime())
					/ (finalunit) * decimals)
					/ decimals + " " + countunit);
		else
			document.write(Math.ceil((today.getTime() - pastdate.getTime())
					/ (finalunit) * decimals)
					/ decimals + " " + countunit);
	} else {
		yearspast = today.getFullYear() - yr - 1;
		tail = (today.getMonth() > mon - 1 || today.getMonth() == mon - 1
				&& today.getDate() >= day) ? 1 : 0;
		pastdate.setFullYear(today.getFullYear());
		pastdate2 = new Date(today.getFullYear() - 1, mon - 1, day);
		tail = (tail == 1) ? tail
				+ Math.floor((today.getTime() - pastdate.getTime())
						/ (finalunit) * decimals) / decimals : Math
				.floor((today.getTime() - pastdate2.getTime()) / (finalunit)
						* decimals)
				/ decimals;
		return yearspast + tail;
	}
}


function stringTrim(str) {
   if (str != null)
     return str.replace(/^\s+/g,"").replace( /\s+$/g,"");
   else
     return null;
}

/**
 * Function to validate the text box.
 * 
 * @param objId
 *            which contains the ID of the object to be validated.
 * 
 * @return true if the text box value is not empty.
 */
function validateTextBox(objId)
{
	var val = stringTrim($("#"+objId+"").val());
	if(val == null || val == "" || val == "undefined" || val == undefined)
	{
		return false;
	}
	else
	{
		return true;
	}
}

/**
 * Function to validate the drop down
 * 
 * @param obj
 *            which contains the value of the object to be validated.
 * 
 * @return true if the drop down contains a valid value selected.
 */
function validateDropDown(obj)
{
	if(obj.options.length == 0)
	{
		return false;
	}
	
	if(obj.options.selectedIndex == null || obj.options.selectedIndex == "" || obj.options.selectedIndex == "undefined" || obj.options.selectedIndex == undefined)
	{
		return false;
	}
	else
	{
		return true;
	}
}

/**
 * Function to convert small letters to Capital letters on key press.
 * 
 * @param id
 *            the id of the object whose value will be converted to capital
 *            letters.
 */
function toUpperCase(id)
{
     var y=document.getElementById(id).value;
     document.getElementById(id).value=y.toUpperCase();
}

/**
 * Function to validate time according to 12 / 24 hour format
 * 
 * @param timeStr
 *            the time to be validated
 * 
 * @param isMeridian
 *            12/24 hour to be followed with true indicating 12 hour format
 * 
 * @return true or false according to the validity of the time
 */
function checkTimePeriod(timeStr, isMeridian)
{
     if (timeStr == null)
         return false;

     var regExp;

     // if the meridian is not followed it indicates 24 hour format
     if( isMeridian == false ) // hh:mm 23:00
     {
    	 regExp = /^(([0-1][0-9])|(2[0-3])):([0-5][0-9])$/;
     }
     // else 12 hour format
     else
     {
    	 regExp = /^((0[0-9])|(1[0-2])):([0-5][0-9])$/;
     }
    
     if (!regExp.test(timeStr))
     {
         return false;
     }
     
     return true;
}

/**
 * Function to check the length of the text entered in the text area.
 * 
 * @param textAreaObject
 *            the text area element.
 * @param limit
 *            the maximum length.
 */
function checkTextAreaValLength(textAreaValue, limit)
{	
	return textAreaValue.length < limit;
}

/**
 * Function to check if the entered value is a valid telephone number.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkValidNumber(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());	
	var regex = /^[0-9]?[1-9][0-9]*$/;
	
	if(objValue.length == 10)		
	{
		if(!regex.test(objValue))
			return false;
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * Function to check if the entered value contains only numbers.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkForDigits(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());	
	
	var regex = /^[0-9]*$/;
	
	if(!regex.test(objValue))
	return false;
	
	return true;
}
/**
 * Function to check if the entered value's length is valid.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkForLength(objId,length)
{
	var objValue = stringTrim($("#"+objId+"").val());	
	
	if(objValue.length == 10)
	return true;
	else
	return false;
}

/**
 * Function to check if the entered value's length is valid.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkForNameLength(objId,length)
{
	var objValue = stringTrim($("#"+objId+"").val());	
	
	if(objValue.length >= length)
	return true;
	else
	return false;
}

/**
 * Function to check if the entered value is a valid text.
 * 
 * @param obj -
 *            Object Id.
 * 
 * @return - True if its invalid. False if its valid.
 */
function validateTextForCharactersOnly(objId) 
{	
    var objValue = stringTrim($("#"+objId+"").val()) || "";
	var regex = /^[a-zA-Z ]*$/;
	
	if (!regex.test( objValue ) ) 
	{
		return true;
	}
	return false;
}

/**
 * Function to check if the entered value has valid text with only one quote.
 * 
 * @param obj -
 *            Object Id.
 * 
 * @return - True if its invalid. False if its valid.
 */
function validateName(objId) 
{	
    var objValue = stringTrim($("#"+objId+"").val()) || "";
    var regex = /^[a-zA-Z]+\s?[a-zA-Z ]*\s?'?-?\s?[a-zA-Z ]+$/;
    
	// var regex = /^[a-zA-Z]+\s?[a-zA-Z ]*\s?'?-?\s?[a-zA-Z ]+$/;
	if (!regex.test( objValue ) ) 
	{
		return true;
	}
	return false;
}

function validateDecimalNumbers(obj) 
{
	var val = obj;
	var temp = '';
	var retValue = true;
	for(var i=0;i <val.length; i++)
	{
		temp = val.substring(i,i+1);
		if(temp == '.') 
		{
			retValue = false;
			break;
		}
	}
	return retValue;
}

// validate alphanumeric field
function isAlphaNumeric(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());
	var alphaNumericRegex = /^[0-9a-zA-Z]*$/;
	if(alphaNumericRegex.test(objValue))
	{	
		return true;
	}
	else
	{
		return false;
	}
}

function validateUserPassword(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());
	var regEx=/(?=^.{8,300}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-!@#$%^&()_+}{:;'\/>.<,])(?!.*\s).*$/;
	
	if(regEx.test(objValue))
		return true;
	else
		return false;
}

// validate alphabet field
function isAlpha(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());
	var alphaNumericRegex = /^[a-zA-Z]*$/;
	if(alphaNumericRegex.test(objValue))
	{	
		return true;
	}
	else
	{
		return false;
	}
}
/**
 * Check if the entered date is greater than current date.
 * 
 * @param dateObj
 * 
 * @return <b>TRUE</b>. If past date.
 */
function isPastDate(dateObj)
{
	if(dateObj.value == null || dateObj.value == "" || dateObj.value == "undefined" || dateObj == undefined)
	{
		return false;
	}
	var userDate = new Date(dateObj.value);
	var currentDate = new Date();
		
	var usrSelectedDate = userDate.getDate();
	var usrSelectedMonth = userDate.getMonth();
	var usrSelectedYear = userDate.getYear();
	
	var curDate = currentDate.getDate();
	var curMonth = currentDate.getMonth();
	var curYear = currentDate.getYear();
	
	if(usrSelectedYear < curYear){
		return true;
	}
	else if(usrSelectedYear > curYear){
		return false;
	}
	else{
		if(usrSelectedMonth < curMonth){
			return true;
		}
		else if(usrSelectedMonth > curMonth){
			return false;
		}
		else{
			if(usrSelectedDate <  curDate){return true;}
			else{return false;}
		}
	}
}

/**
 * Checks if atleast one radio button is selected from the group.
 * 
 * @radioObjId : Object ID.
 */
function validateRadio(radioObjId) 
{
	return $('input[name='+radioObjId+']:radio').is(":checked");
}

/**
 * Checks if atleast one checkbox is selected from the group.
 * 
 * @checkboxObjId : Object ID.
 */
function validateCheckBox(checkboxObjId) 
{
	return $('input[name='+checkboxObjId+']:checkbox').is(":checked");
}

/**
 * Calculates the age based on the inpute date.
 * 
 * @Param: dateObjId - Object ID of date field.
 * @Param: ageObjId - Age object ID.
 */
function calculateAge(dateObjId, ageObjId)
{
	if(stringTrim($("#"+dateObjId+"").val()) != '')
	{
		var inDate = stringTrim($("#"+dateObjId+"").val());
		var dat = inDate.split("/");
		var age = displayage(dat[2], dat[1], dat[0], 'years', 0, 'rounddown');		
		$("#"+ageObjId+"").val(age);
		
		document.getElementById(ageObjId).readonly='readonly';
		document.getElementById(dateObjId).focus();
	}
}

/*----------------------------------------------*/

function getDateObject(dateString,dateSeperator)
{
	// This function return a date object after accepting
	// a date string and date separator as arguments
	var curValue=dateString;
	var sepChar=dateSeperator;
	var curPos=0;
	var cDate,cMonth,cYear;

	// extract day portion
	curPos=dateString.indexOf(sepChar);
	cDate=dateString.substring(0,curPos);
	
	// extract month portion
	endPos=dateString.indexOf(sepChar,curPos+1); 
	cMonth=dateString.substring(curPos+1,endPos);
	
	// extract year portion
	curPos=endPos;
	endPos=curPos+5; 
	cYear=curValue.substring(curPos+1,endPos);

	// Added : 28-02-2012
	// months are 0 based (0-11 for Jan-Dec)
	cMonth = cMonth-1;
	
	// Create Date Object
	dtObject=new Date(cYear,cMonth,cDate);
	return dtObject;
}


function compareDate(fromDate,toDate)
{
	var startDate = getDateObject(document.getElementById(fromDate).value,"/");
	var endDate = getDateObject(document.getElementById(toDate).value,"/");
	
	if(startDate > endDate)
		return false;
		
	return true;
}


/**
 * Function to check if the entered value is a valid number (Ranging from 0 to
 * 9).
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function validateTextForNumbersOnly(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());		
	var regex = new RegExp('^[0-9]*$');
	
	if(!regex.test(objValue))
	return false;

	return true;
}

/**
 * Function to check if the entered value is a valid floating point number 
 * (Two places after decimal point)
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function validateTextForDecimalPlaces(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());		
	var regex = /^0?\d(.\d\d?)?$/;

	return regex.test(objValue);
}


/**
 * Returns value for the radio button selected.
 * 
 * @radioObjId : Object ID.
 */
function get_radio_value(radioObjId)
{
// return $('#'+radioObjId+'').val();
return $('input[@name='+radioObjId+']:checked').val();
   
}

/**
 * Clears the value in the field specified.
 * 
 * @Param: ObjId - Object ID of the field.
 */
function clearField(objId)
{
	$("#"+objId).val('');
}


/**
 * Gets a confirmation from the user before saving any changes in the record.
 * 
 * Use the same function for other operations as well, such as delete. Send a
 * separate message in such cases.
 * 
 * @param message
 * @returns
 */
function confirmSave(message,callBack){
	var defaultMessage = "Are you sure, you want to save the record?";
	
	if(message == null || stringTrim(message) == ""){
		message = defaultMessage;
	}
	
	$.alerts.okButton = ' Yes ';
	$.alerts.cancelButton = ' No ';
	
	jConfirm(message, null, callBack);
}

/**
 * Function to check if the entered value is a valid currency value
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function validateTextForCurrencyOnly(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());		
	if(objValue == "" || objValue == 0) return false;	
	var regex = /^\d?\d?\d?\d?\d(\.[0-9]?[0-9]?[0-9]?[0-9]?)?$/;	
	if(!regex.test(objValue))
		return false;
	return true;
}

/**
 * Function to check if the entered value is a valid Insurance Premium value
 * 
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function validateTextForPremiumPerOnly(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());
	if(objValue == "" || objValue == 0) return false;	
	var regex = /^\d?\d?(\.[0-9]?[0-9]?[0-9]?[0-9]?)?$/;	
	if(!regex.test(objValue))
		return false;
	return true;
}

/**
 * Function to bring focus to the first text box in the form
 * 
 * @param.
 * 
 * @return.
 */
function focusFirstTextBox()
{
	$("input[type='text']:enabled:first").focus();
	
	/*
	 * $(document).bind("contextmenu",function(e){ return false; });
	 */	
}

/**
 * Function to check if the entered value is a valid Insurance Premium value
 * 
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function validateTextForPercent(objId)
{
	var objValue = stringTrim($("#"+objId+"").val());
	if(objValue == "") return false;	
	var regex = /^\d?\d?\d?(\.[0-9]+[0-9]?[0-9]?)?$/;	
	if(!regex.test(objValue))
	return false;
	return true;
}

/**
 * Counts the no. of occurrences of <code>word</code> in the
 * <code>string</code>.
 * 
 * @param string
 *            The text to be searched in.
 * @param word
 *            The text to be searched in <code>string</code>.
 * @return No. of occurrences
 */
function countInstances(string, word) {
	var substrings = string.split(word);
	return substrings.length - 1;
}

/**
 * Check if the input text contains only upper case.
 * 
 * @param objId
 *            Object ID.
 * 
 * @return true if only upper case, false otherwise.
 */
function checkForUpperCase(objId)
{
	var count=0;
	var objValue = stringTrim($("#"+objId+"").val());
	for (var i=0; i<objValue.length ; i++)
	{
		if(objValue.charCodeAt(i) >= 97 && 
				objValue.charCodeAt(i) <= 122)
			count++;
	}
	
	if(count > 0)
		return false;
	else
		return true;
}

// Does Left Trim and Right Trim for the text.
function trim(inputString) {
   	// Removes leading and trailing spaces from the passed string. Also removes
   	// consecutive spaces and replaces it with one space. If something besides
   	// a string is passed in (null, custom object, etc.) then return the input.
   	   	
   	var retValue = inputString;
   	
   	// Check for spaces at the beginning of the string
	var ch = retValue.substring(0,1);	
   	while (ch == " ") { 	
      	retValue = retValue.substring(1,retValue.length);
      	ch = retValue.substring(0,1);
   	}
   	
   	// Check for spaces at the end of the string
	ch = retValue.substring(retValue.length-1,retValue.length);
   	while (ch == " ") { 	
      	retValue = retValue.substring(0,retValue.length-1);
      	ch = retValue.substring(retValue.length-1,retValue.length);
   	}
   	
   	// Note that there are two spaces in the string - look for multiple spaces
	// within the string
   	// Again, there are two spaces in each of the strings
   	while (retValue.indexOf("  ") != -1) { 
     	 retValue = retValue.substring(0,retValue.indexOf("  ")) + retValue.substring(retValue.indexOf("  ")+1,retValue.length); 
   	}
   	
   	// Return the trimmed string back to the user
   	return retValue; 
} 

/**
 * Function to check if the entered value is a valid telephone number.
 * 
 * @param objValue -
 *            Telephone Number.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkValidMSISDN(objValue)
{
	var regex = /^[0-9]?[1-9][0-9]*$/;
	
	if(objValue.length == 10)       
	{
		if(!regex.test(objValue))
			return false;
		return true;
	}
	else
	{
		return false;
	}
}

/**
 * This method checks for valid CSV format.
 * 
 * @param objValue - Object Value
 * @returns - True if its valid. False if its invalid.
 */
function validateCSV(objValue)
{
	regex = /,,/;
	return regex.test(objValue);
}

/**
 * This method checks for valid Msisdn Codes in CSV format.
 * 
 * @param objValue - Object Value
 * @returns - true if valid, false otherwise.
 */
function validateMsisdnCodes(objVal) 
{
	regex = /^\d{3}(,\d{3})*$/;
	return regex.test(objVal);
}

/**
 * This method removes whitespace character, newline character, null character
 * 	from the original input.
 * 
 * @return newly formated string
 */
function removeExtraChar(objVal){
	// removing space around comma
	pattern1 = /[\s\r\n]*,[\r\n\\s]*/g;
	objVal = objVal.replace(pattern1, ",");
	
	//removing comma from the start/end
	pattern2 = /^,*|,*$/g;
	objVal = objVal.replace(pattern2, "");
	
	// removing space/newline character from the start/end
	pattern3 = /^\s*|\s*$|^\n*|\n*$/g;
	objVal = objVal.replace(pattern3, "");
	
	// removing null character
	pattern = /\0/g;
	objVal = objVal.replace(pattern, "");
	
	return objVal;
}

/**
 * Function to check if the entered value's length is valid.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkForRoleNameLength(objId,length)
{
	var objValue = stringTrim($("#"+objId+"").val());	
	
	if(objValue.length >= length)
	return true;
	else
	return false;
}
/**
 * Function to check if the entered value contains only numbers.
 * 
 * @param objId -
 *            Object ID.
 * 
 * @return - True if its valid. False if its invalid.
 */
function checkForDigit(objId)
{	
	var regex = /^[0-9]*$/;
	
	if(!regex.test(objId))
	return false;
	
	return true;
}
