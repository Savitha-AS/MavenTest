var totalBusRuleRows = 1;
var isSHIFTKeyPressed = false;			
var allowedLength="300";
countChars = function()
{
   var len=document.getElementById("smsTemplate").value.length;
   var str=document.getElementById("smsTemplate").value;
   if(len<=allowedLength)
   {					   
	 document.getElementById("txtLen1").value=allowedLength-len;
   }
   else
   {
	  document.getElementById("smsTemplate").value=str.substr(0,str.length-1);	  
	  return false;
   } 
 };	

processOnBlur=function(){
	 var txtArea = document.getElementById("smsTemplate");
	 var txtValue = txtArea.value;				 
	 var txtLen = txtValue.length;
	 if(txtLen >= allowedLength){
		var validString = txtValue.substring(0,allowedLength);
		txtArea.value = validString;
		document.getElementById("txtLen1").value = 0; 
		return false;
	}
};

processMouseDown=function(e) {
	if (navigator.appName == 'Netscape' && 
	(e.which == 3 || e.which == 2)){
		jAlert("Right Click is not allowed");
	return false;
	}
	else if (navigator.appName == 'Microsoft Internet Explorer' && 
	(event.button == 2 || event.button == 3)) {
		jAlert("Right Click is not allowed");
	return false;
	}
	return true;
};

processKeyEvent = function(eventType, event)
{
	// MSIE hack
	if (window.event)
	{
		event = window.event;
	}
	if(eventType == "onkeyup"){
		if(event.keyCode == 16){
			isSHIFTKeyPressed = false;
			return true;
		}
	}
	//Check if shift key is pressed
	if(isSHIFTKeyPressed){
		//Check for < > keys pressing
		if(event.keyCode == 188 || event.keyCode == 190){
			window.event.returnValue=false;// Suppress < > key press
			return false;
		}	
	}

	if(event.keyCode == 16){
		isSHIFTKeyPressed = true;
		return true;
	}
	var str=document.getElementById("smsTemplate").value;	
	if(str.length > allowedLength){
		if(event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){
			;// Do not suppress key press
		}
		else{
			window.event.returnValue=false;// Suppress key press
		}
	}
};

processKeyDown = function(event)
{
	processKeyEvent("onkeydown", event);
	countChars();
};

processKeyUp = function(event)
{
	processKeyEvent("onkeyup", event);
	processOnBlur();
	countChars();
};

processOnPaste = function(event)
{
	var str=document.getElementById("smsTemplate").value;
	if(str.length >= allowedLength)
		window.clipboardData.clearData();	
	document.getElementById("smsTemplate").focus();
};

submitTemplate = function()
{ 
	var validRegExp =/[<][0-9a-zA-Z]*[>]/g;
	var invalidRegExp = /(<)|(>)/g;
	var contentStr = stringTrim($("#smsTemplate").val());
	resetErrors();	
	if(contentStr.length == 0){
		unMarkError("smsTemplate");
		setError("smsTemplate",0,"Content");
	}
	else
	{
		var res = contentStr.replace(validRegExp,"(..)");	
		var res1 = res.match(invalidRegExp);	
		var isPlaceHolderMatch = false;			
		//If valid check for any Invalid place holders.
		// Reset the error list and unmark the errors if any.
		resetErrors();		
		unMarkError("smsTemplate");
		if(res1 != null){
			setError("smsTemplate",18,"Content");
		}else if(res != null){		
			//Iterate and check to validate place holders//
			var myselect=document.getElementById("smsPlaceHolder");		
			res = contentStr.match(validRegExp);
			if(res != null && myselect != null){
				for (var i=0; i<res.length; i++){	
					for(var j=1; j<myselect.options.length; j++){		
						 if(res[i] == "<"+(myselect.options[j].value.split("#"))[0]+">"){
							//Unmatched place holder found.
							isPlaceHolderMatch = true;
							break;
						 }
					}
					if(!isPlaceHolderMatch){
						var errrorStr = res[i].replace(/(<)|(>)/g,function(thematch){if (thematch=="<") return "&lt;"; else return "&gt;";});
						setError("smsTemplate",18,errrorStr+" in Content");
					}
					isPlaceHolderMatch = false;
				}	
			}
		}		
	}
	
	// Validation to ensure that, all place holders in the drop down list 
	//	exists in the Template.
	unMarkError("smsTemplate");
	var smsPlaceHolder = document.getElementById("smsPlaceHolder");
	if(smsPlaceHolder){
	for(var j=1; j<smsPlaceHolder.options.length; j++){		
		 var occurrenceCount = countInstances(contentStr,"<"+(smsPlaceHolder.options[j].value.split("#"))[0]+">");
		 
		 switch(occurrenceCount){
		 	case 0 : 	setError("smsTemplate",27,"Content");
		 				break;
		 	
		 	case 1 : 	continue;
		 }
	}}
	
	
	//Validating the SMS Validity field.
	unMarkError("smsValidity");
	if(!checkForDigits("smsValidity")){
		setError("smsValidity",3,"SMS Validity");
	}
	
	if(showValidationErrors("validationMessages_parent"))
		return true;
};
			
getCaret = function(el) 
{
  if (el.selectionStart) {
    return el.selectionStart;
  } else if (document.selection) {
    el.focus();
 
    var r = document.selection.createRange();
    if (r == null) {
      return 0;
    }
 
    var re = el.createTextRange(),rc = re.duplicate();
    re.moveToBookmark(r.getBookmark());
    rc.setEndPoint('EndToStart', re);
 
    return rc.text.length;
  } 
  return 0;
};

/* Add Place Holder */
setPlaceHolder = function()	
{	
	// Reset the error list and unmark the errors if any.
	resetErrors();	
	// unmark the field.
	unMarkError("smsPlaceHolder");
	// unmark the field.
	unMarkError("smsTemplate");
	var textarea = document.getElementById('smsTemplate');
	if(textarea.value.length < allowedLength){
		var placeHolder = document.getElementById('smsPlaceHolder');
		var curPos = getCaret(textarea);
		var front = textarea.value.substring(0,curPos);
		var back = textarea.value.substring(curPos,textarea.value.length);
		if(placeHolder.value == "")
		{
			if(!validateDropDown(document.getElementById("smsPlaceHolder")))
				setError("smsPlaceHolder",0,"Place Holder");
		}
		else
		{
			var placeHolderCode = placeHolder.value.split("#");
			var appendText = "<"+placeHolderCode[0]+">";
			textarea.value = front+appendText+back;	
			countChars();
			setCaretToPos(textarea,(front.length)+(appendText.length));
			placeHolder.selectedIndex=0;
		}
	}
	else
	{		
		setError("smsTemplate",19,"Content");
	}
	if(showValidationErrors("validationMessages_parent"))
		return true;
};


function setSelectionRange(input, selectionStart, selectionEnd) {
   if (input.setSelectionRange) {
   input.focus();
   input.setSelectionRange(selectionStart, selectionEnd);
   }
   else if (input.createTextRange) {
   var range = input.createTextRange();
   range.collapse(true); 
   range.moveEnd('character', selectionEnd);
   range.moveStart('character', selectionStart);
   range.select();
   }
   }
   function setCaretToPos (input, pos) {
   setSelectionRange(input, pos, pos);
   }
   
 function substituteValues(){
     var validRegExp =/[<][0-9a-zA-Z]*[>]/g;
     //var textFromParent = window.opener.document.getElementById('smsTemplate').value;
     //var textFromParent = document.getElementById('smsTemplate').value;
     var textFromParent = stringTrim($("#smsTemplate").val());
 	resetErrors();	
 	if(textFromParent.length == 0){
 		unMarkError("smsTemplate");
 		setError("smsTemplate",0,"Content");
 		if(showValidationErrors("validationMessages_parent"))
 			return true;
 	}
     var templateValueArray = textFromParent.match(validRegExp);
     //var myselect=window.opener.document.getElementById("smsPlaceHolder");
     var myselect=document.getElementById("smsPlaceHolder");
     if(myselect){
	     var parentValueArray = new Array();
	     var res = "";
	     for(var j=1; j<myselect.options.length; j++){
	     	parentValueArray[j] = myselect.options[j].value;
		}
	     if(templateValueArray != null){ 
		     for(var i=0;i<templateValueArray.length;i++){
		     	for(var k=1; k<parentValueArray.length; k++){
		     		res = parentValueArray[k].split("#");
		         	if(templateValueArray[i]== "<"+res[0]+">"){
		         		textFromParent = textFromParent.replace(templateValueArray[i],res[1]);
		         		break;
		         	}
		 		}
		     }
	     }
     }
     return textFromParent;
 }