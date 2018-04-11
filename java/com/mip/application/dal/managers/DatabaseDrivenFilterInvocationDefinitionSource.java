package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource;

import com.mip.application.controllers.LoginController;
import com.mip.application.services.UserService;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class DatabaseDrivenFilterInvocationDefinitionSource extends AbstractFilterInvocationDefinitionSource {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
										LoginController.class);
	
	/**
	 * An instance of <code>Map</code> type representing the allRolesAllowedMap.
	 */
	private Map<String, String> allRolesAllowedMap;
	
	/**
	 * Instance of userService.
	 */
	private UserService userService;
	
	/**
	 * Sets the user service.
	 * 
	 * @param userService an instance of <code>UserService</code> 
	 * 		  representing user Service.
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
		setAllRolesAllowedMap(this.userService.getAllRolesAllowed());
	}
	
	/**
	 * Sets the allRolesAllowedMap.
	 * 
	 * @param allRolesAllowedMap an instance of <code>Map</code> 
	 * 		  representing allRolesAllowedMap.
	 */
	public void setAllRolesAllowedMap(Map<String, String> allRolesAllowedMap) {
		this.allRolesAllowedMap = allRolesAllowedMap;
	}
	
	/**
	 * Method to get the Attribute Definitions.
	 */
	public Iterator getConfigAttributeDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method to get the attributes for the given url.
	 * 
	 * @param url an instance of <code>String</code> representing URL.
	 * 
	 * @return an instance of <code>ConfigAttributeDefinition</code> 
	 * 		   representing ConfigAttributeDefinition.
	 */
	@Override
	public ConfigAttributeDefinition lookupAttributes(String url) {
		
		Object params[] = {url};
//		logger.entering("lookupAttributes", params);
		
		ConfigAttributeDefinition configAttr = null;
		
		try {

			configAttr = new ConfigAttributeDefinition();

			List<String> roleNamesList = new ArrayList<String>();
			
			String trimmedUrl = url.trim();
			
			roleNamesList = this.userService.getRolesByURL(allRolesAllowedMap, trimmedUrl);
			
			// If the URL has roles associated !!
			if (roleNamesList.size() != 0) {
				configAttr = new ConfigAttributeDefinition();
				for (String roleName : roleNamesList) {
					configAttr.addConfigAttribute(new SecurityConfig(roleName));
				}
			}
			
		} catch (Exception e) {
			configAttr = null;
		}
		
//		logger.exiting("lookupAttributes", configAttr);
		
		return configAttr;
	}
	
}
	