package com.mip.framework.controllers;

import static com.mip.framework.constants.MAVPaths.VIEW_GLOBAL_ERROR;
import static com.mip.framework.constants.MAVPaths.VIEW_GLOBAL_SUCCESS;
import static com.mip.framework.constants.MAVPaths.VIEW_GLOBAL_INFO;
import static com.mip.framework.constants.PlatformConstants.ERROR_MESSAGE;
import static com.mip.framework.constants.PlatformConstants.SUCCESS_MESSAGE_KEY;
import static com.mip.framework.constants.PlatformConstants.SUCCESS_MESSAGE_ARGS;
import static com.mip.framework.constants.PlatformConstants.INFO_MESSAGE_KEY;
import static com.mip.framework.constants.PlatformConstants.INFO_MESSAGE_ARGS;
import static com.mip.framework.constants.PlatformConstants.TICKET_NUMBER;
import static com.mip.framework.constants.PlatformConstants.GO_BACK_LINK;
import static com.mip.framework.constants.PlatformConstants.ERROR_MODEL_MAP;
import static com.mip.framework.constants.PlatformConstants.SUCCESS_MODEL_MAP;
import static com.mip.framework.constants.PlatformConstants.INFO_MODEL_MAP;

import java.util.HashMap;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.mip.framework.utils.LogUtil;
import com.mip.framework.utils.StringUtil;

/**
 * <p>
 * <code>BasePlatformController.java</code> contains all the common methods 
 * pertaining to all use cases. This controller extends the 
 * <code>MultiActionController</code> class of spring framework.
 * </p>
 * 
 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController
 * 
 * @author T H B S
 *
 */
public class BasePlatformController extends MultiActionController
{

	/**
	 * Redirects to the success page post successful completion of the request.
	 * 
	 * @param successMessageKey Success message displayed on the page.
	 * 
	 * @return Global Success Page.
	 */
	public ModelAndView success(String successMessageKey)
	{
		return this.success(successMessageKey, null, null);
	}
	
	/**
	 * Redirects to the success page post successful completion of the request.
	 * 
	 * @param successMessageKey Success message displayed on the page.
	 * 
	 * @param goBackLink Back link to which user can navigate post successful
	 * operation.
	 * 
	 * @return Global Success Page.
	 */
	public ModelAndView success(String successMessageKey, String goBackLink)
	{
		return this.success(successMessageKey, null, goBackLink);
	}
	
	/**
	 * Redirects to the success page post successful completion of the request.
	 * 
	 * @param successMessageKey Success message displayed on the page.
	 * 
	 * @param successMessageArgs Arguments in CSV format to Message NLS.
	 * 
	 * @param goBackLink Back link to which user can navigate post successful
	 * operation.
	 * 
	 * @return Global Success Page.
	 */
	public ModelAndView success(String successMessageKey, 
			String successMessageArgs, String goBackLink)
	{
		//ModelMap object holding the objects.
		ModelMap successModelMap = new ModelMap();
		//Map containing the key value pair.
		HashMap<String,String> successInfoMap = new HashMap<String,String>();
		
		//Set the success message to be shown in resultant page.
		if(!StringUtil.isEmpty(successMessageKey))
			successInfoMap.put(SUCCESS_MESSAGE_KEY, successMessageKey);
		else
			successInfoMap.put(SUCCESS_MESSAGE_KEY, "Operation completed " +
					"successfully.");

		//Set the link to which the user can navigate after successful operation.
		if(!StringUtil.isEmpty(successMessageArgs))
			successInfoMap.put(SUCCESS_MESSAGE_ARGS, successMessageArgs);
		
		//Set the link to which the user can navigate after successful operation.
		if(!StringUtil.isEmpty(goBackLink)){		
			successInfoMap.put(GO_BACK_LINK, goBackLink);
		}
		
		successModelMap.addAllAttributes(successInfoMap);	
		
		//Redirect to the success page.
		return new ModelAndView(VIEW_GLOBAL_SUCCESS, SUCCESS_MODEL_MAP, 
							successModelMap);
	}
	


	/**
	 * Redirects to the info page.
	 * 
	 * @param infoMessageKey Success message displayed on the page.
	 * 
	 * @return Global Info Page.
	 */
	public ModelAndView info(String infoMessageKey)
	{
		return this.success(infoMessageKey, null, null);
	}
	
	/**
	 * Redirects to the info page.
	 * 
	 * @param infoMessageKey Success message displayed on the page.
	 * 
	 * @param goBackLink Back link to which user can navigate.
	 * 
	 * @return Global Info Page.
	 */
	public ModelAndView info(String infoMessageKey, String goBackLink)
	{
		return this.success(infoMessageKey, null, goBackLink);
	}
	
	/**
	 * Redirects to the info page.
	 * 
	 * @param infoMessageKey Info message displayed on the page.
	 * 
	 * @param infoMessageArgs Arguments in CSV format to Message NLS.
	 * 
	 * @param goBackLink Back link to which user can navigate.
	 * 
	 * @return Global Info Page.
	 */
	public ModelAndView info(String infoMessageKey, 
			String infoMessageArgs, String goBackLink)
	{
		//ModelMap object holding the objects.
		ModelMap infoModelMap = new ModelMap();
		//Map containing the key value pair.
		HashMap<String,String> infoInfoMap = new HashMap<String,String>();
		
		//Set the info message to be shown in resultant page.
		if(!StringUtil.isEmpty(infoMessageKey))
			infoInfoMap.put(INFO_MESSAGE_KEY, infoMessageKey);

		//Set the link to which the user can navigate after successful operation.
		if(!StringUtil.isEmpty(infoMessageArgs))
			infoInfoMap.put(INFO_MESSAGE_ARGS, infoMessageArgs);
		
		//Set the link to which the user can navigate after successful operation.
		if(!StringUtil.isEmpty(goBackLink)){		
			infoInfoMap.put(GO_BACK_LINK, goBackLink);
		}
		
		infoModelMap.addAllAttributes(infoInfoMap);	
		
		//Redirect to the info page.
		return new ModelAndView(VIEW_GLOBAL_INFO, INFO_MODEL_MAP, 
							infoModelMap);
	}
	
	/**
	 * Redirects the request to global error page with error message
	 * and the generated ticket number. 
	 * 
	 * @param errorMessage Error message to be displayed on the error page.
	 * 
	 * @return Global Error Page.
	 */
	public ModelAndView error(String errorMessage)
	{
		//ModelMap object holding the error detail objects.
		ModelMap errorModelMap = new ModelMap();
		//Map containing the key value pair of the error details.
		HashMap<String,String> errorInfoMap = new HashMap<String,String>();
		//Generate the error ticket number.
		String ticketNum = LogUtil.getIncidentNumber();
		
		//Set the error message to be shown in the error page.
		if(StringUtil.isEmpty(errorMessage))
			errorMessage = "An unexpected error has occured " +
					"while processing your request.";
		
		errorInfoMap.put(ERROR_MESSAGE, errorMessage);
				
		//Set the ticket number to be shown.
		errorInfoMap.put(TICKET_NUMBER, ticketNum);
		
		errorModelMap.addAllAttributes(errorInfoMap);		
		
		logger.error(errorMessage);
		logger.error("Error ticket number : ["+ticketNum+"]");
		
		//Redirect to the error page.
		return new ModelAndView(VIEW_GLOBAL_ERROR, ERROR_MODEL_MAP, 
							errorModelMap);
	}
	
	/**
	 * Redirects the request to the global error page with appropriate error
	 * message, the generated ticket number and the link to which user can
	 * navigate in case of exception.
	 * 
	 * @param errorMessage Error message to be displayed on the screen.
	 * 
	 * @param goBackLink Link where the user can navigate.
	 * 
	 * @return Global Error Page.
	 */
	public ModelAndView error(String errorMessage, String goBackLink)
	{
		//ModelMap object holding the error detail objects.
		ModelMap errorModelMap = new ModelMap();
		//Map containing the key value pair of the error details.
		HashMap<String,String> errorInfoMap = new HashMap<String,String>();
		//Generate the error ticket number.
		String ticketNum = LogUtil.getIncidentNumber();
		
		//Set the error message to be shown in the error page.
		if(StringUtil.isEmpty(errorMessage))
			errorMessage= "An unexpected error has occured " +
					"while processing your request.";
		
		errorInfoMap.put(ERROR_MESSAGE, errorMessage);
				
		//Set the link where the user can navigate to in case of error.
		if(!StringUtil.isEmpty(goBackLink))
			errorInfoMap.put(GO_BACK_LINK, goBackLink);
		
		//Set the ticket number to be shown.
		errorInfoMap.put(TICKET_NUMBER, ticketNum);
		
		errorModelMap.addAllAttributes(errorInfoMap);
		
		logger.error(errorMessage);
		logger.error("Error ticket number : ["+ticketNum+"]");
		
		//Redirect to the error page.
		return new ModelAndView(VIEW_GLOBAL_ERROR, ERROR_MODEL_MAP, 
							errorModelMap);
	}
	
	/**
	 * Redirects the request to global error page.
	 * 
	 * @return Global Error Page.
	 */
	public ModelAndView error()
	{
		//ModelMap object holding the error detail objects.
		ModelMap errorModelMap = new ModelMap();
		//Map containing the key value pair of the error details.
		HashMap<String,String> errorInfoMap = new HashMap<String,String>();		
		//Generate the error ticket number.
		String ticketNum = LogUtil.getIncidentNumber();
		
		//Set the error message to be shown in the error page.
		errorInfoMap.put(ERROR_MESSAGE, "An unexpected error has occured " +
					"while processing your request.");
		
		//Set the ticket number to be shown.
		errorInfoMap.put(TICKET_NUMBER, ticketNum);
		
		logger.error("Error ticket number : ["+ticketNum+"]");
		
		errorModelMap.addAllAttributes(errorInfoMap);
		
		//Redirect to the error page.
		return new ModelAndView(VIEW_GLOBAL_ERROR, ERROR_MODEL_MAP, 
							errorModelMap);
	}
}
