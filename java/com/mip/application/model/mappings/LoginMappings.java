package com.mip.application.model.mappings;

import com.mip.application.model.UserDetails;
import com.mip.application.view.LoginVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <code>LoginMappings.java</code> contains all VO to Model mappings w.r.t 
 * Login use case. 
 * 
 * @author T H B S
 */
public class LoginMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LoginMappings.class);

	/**
	 * Maps LoginVO to UserDetails model.
	 * 
	 * @param loginVO LoginVO command object
	 * @return UserDetails model object
	 * @throws MISPException 
	 */
	public static UserDetails mapLoginVOToUserDetailsModel(LoginVO loginVO) 
		throws MISPException{
		
		Object[] params = {loginVO};
		logger.entering("mapLoginVOToUserDetailsModel", params);
		
		UserDetails userDetails = new UserDetails();
		try{
			userDetails.setUserUid(loginVO.getLoginId());
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"UserDetails from a LoginVO.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapLoginVOToUserDetailsModel",userDetails);
		return userDetails;
	}
}
