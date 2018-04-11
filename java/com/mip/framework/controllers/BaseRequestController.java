package com.mip.framework.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.controllers.LoginController;
import com.mip.framework.utils.StringUtil;

/**
 * This controller currently serves the page requests from the platform.
 * This controller acts as a gateway for all the page requests. Based on the 
 * module name and task name the request view is loaded.
 * 
 * @author T H B S
 * 
 * @see com.mip.framework.handlers.PlatformRequestHandler
 *
 */
public class BaseRequestController extends BasePlatformController
{
	/**
	 * This method handles all the page requests from the platform.
	 * Module name and the Task name are passed in the request. Based on the
	 * module name and task name the requested page is loaded.
	 * 
	 * @param request an instance of <code>HttpServletRequest</code>
	 * 
	 * @param response an instance of <code>HttpServletResponse</code>
	 * 
	 * @return the actual page request from the platform.
	 */
	public ModelAndView handlePageRequest
					(HttpServletRequest request, HttpServletResponse response)
	{
		String moduleName= "";
		String taskName="";	
		
		moduleName = request.getParameter("mn");
		taskName = request.getParameter("tn");
		
		if(StringUtil.isEmpty(moduleName) || StringUtil.isEmpty(taskName))
			super.info("Oops!!! A possible request has been disappeared from" +
					" reaching the controller..["+moduleName+","+taskName+"]");
		
		/*
		 * For pages like changePassword.jsp, home.jsp etc., task name will be 
		 * platform. This implementation will serve the requests of all JSPs
		 * which are placed at /pages/* hierarchy.
		 */
		if(null!= moduleName){
			if(moduleName.equalsIgnoreCase("platform"))
				return new ModelAndView(taskName);
		}
		else
			return new LoginController().getStatistics(request, response);
		
		return new ModelAndView(moduleName+"/"+taskName);
	}
}