package com.mip.application.model.mappings;

import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <code>UserHashMapping.java</code> contains all VO to Model mappings 
 * pertaining to use cases w.r.t Hashes/ Passwords. 
 * 
 * @author T H B S
 */
public class UserHashMapping {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserHashMapping.class);
	
	/**
	 * Populates UserHash model with the hash string and UserDetails.
	 * 
	 * @param userHashStr 
	 * 				Hash string
	 * @param userDetails
	 * 				UserDetails user_id required for mapping between 2 tables.
	 * @return UserHash model
	 * @throws MISPException
	 */
	public static UserHash mapUserHashDetailsModel(String userHashStr, 
			UserDetails userDetails) throws MISPException{
		
		Object[] params = {userHashStr, userDetails};
		logger.entering("mapUserHashDetailsModel", params);
		
		byte accountLocked = 0;
		int attemptCount = 0;
		byte firstLogin = 1;
		
		UserHash userHash = new UserHash();		
		try{
			userHash.setUserHash(userHashStr);
			userHash.setUserDetails(userDetails);
			userHash.setAccountLocked(accountLocked);
			userHash.setAttemptCount(attemptCount);
			userHash.setFirstLogin(firstLogin);
			
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"UserHash from an existing UserDetails.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapUserHashDetailsModel",userHash);
		return userHash;
	}
}
