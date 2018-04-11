
// The method loads a Radio Button with a pre-populated value.
function loadRadioButton(objectName, selectedValue){			
	var radioButtons = document.getElementsByName(objectName);
	
	if(radioButtons != null){
		for(var radioCount = 0; radioCount < radioButtons.length; radioCount++){
			if(radioButtons[radioCount].value == selectedValue){
				radioButtons[radioCount].checked = true;
			}
		}
	}
}

// The method loads a Drop Down List with a pre-populated value. 
function loadDropDownList(objectId, selectedValue){
	
	var selectObj = document.getElementById(objectId);
	selectObj.value = selectedValue;
}


//The method gets the value of a selected Radio Button.
function getRadioButtonValue(objectName){	
	
	var selectedValue = null;
	var radioButtons = document.getElementsByName(objectName);
	
	if(radioButtons != null){
		for(var radioCount = 0; radioCount < radioButtons.length; radioCount++){
			if(radioButtons[radioCount].checked == true){				
				selectedValue = radioButtons[radioCount].value;
			}
		}
	}	
	
	return selectedValue;
}

// The method resets the drop down list with the default one.
function resetDropDownList(objectId){
	var selectObj = document.getElementById(objectId);
	selectObj.selectedIndex = 0;	
}

//The method un-selects all the radio buttons identified by the 
// radio button group name.
function resetRadioButtons(objectName){
	var radioButtons = document.getElementsByName(objectName);
	
	if(radioButtons != null){
		for(var radioCount = 0; radioCount < radioButtons.length; radioCount++){
			radioButtons[radioCount].checked = false;
		}
	}		
}
/**
 * Used for thousand digit separator
 * @param value
 * @returns {String}
 */
function formatNumber(value) {
    var number = new String(value);
    number = number.split("").reverse();

    var output = "";
    for ( var i = 0; i <= number.length-1; i++ ){
        output = number[i] + output;
        if ((i+1) % 3 == 0 && (number.length-1) !== i)output = ',' + output;
    }
    return output;
}

//The method loads a Check box with a pre-populated value.
function loadCheckBox(objectId, selectedValue){	
	var productValue=selectedValue.split(",");
	$("input[type=checkbox][id="+ objectId+ "]").each(function () {
		for(var i=0;i<4;i++)	
		if(this.value == productValue[i]){
				this.checked = true;
				this.disabled = true;
		}
	});
}

//The method resets the drop down list with the default one.
function resetCheckBox(objectId){
	$("input[type=checkbox][id="+ objectId+ "]").removeAttr('checked');
}


//The method resets the drop down list with the default one.
function resetCheckedBox(objectId,selectedValue){
	var productValue=selectedValue.split(",");
	var i=0;
	$("input[type=checkbox][id="+ objectId+ "]").each(function (){
			if(this.value != productValue[i]){
					this.checked = false;					
			}else if(this.value == productValue[i]){
				this.checked = true;
				
		}
	});
}
