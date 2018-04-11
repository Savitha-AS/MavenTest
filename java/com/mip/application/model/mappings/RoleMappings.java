package com.mip.application.model.mappings;

import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.view.RoleVO;
import com.mip.application.view.UserVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/**
 * <code>UserMappings.java</code> contains all VO to Model mappings w.r.t 
 * User use case model. 
 * 
 * @author T H B S
 */
public class RoleMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			RoleMappings.class);
	
	/**
	 * Maps RoleVO to RoleModel.
	 * 
	 * @param roleVO roleVO command object
	 * 
	 * @return RoleDetails model object
	 * 
	 * @throws MISPException
	 */
	public static RoleMaster mapRoleVOToRoleModel(RoleVO roleVO,
			RoleMaster roleMaster) throws MISPException
	{	
		
		Object[] params = {roleVO};
		logger.entering("mapRoleVOToRoleModel", params);	
		
		try{			
			if(roleVO != null){
				roleMaster.setRoleName(roleVO.getRoleName());
				roleMaster.setRoleDescription(roleVO.getRoleDescription());
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"Role Master from a RoleVO.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapRoleVOToRoleModel");
		return roleMaster;
	}

	/**
	 * Maps UserVO to UserHash model.
	 * 
	 * @param userVO UserVO command object
	 * @return UserHash model object
	 * @throws MISPException
	 */
	public static UserHash mapUserVOToUserHashModel(UserVO userVO) 
		throws MISPException{		
		
		Object[] params = {userVO};
		logger.entering("mapUserVOToUserHashModel", params);
		
		UserHash userHash = new UserHash();
		
		try{
			if(userVO != null){
				
				UserDetails userDetails = new UserDetails();
				userDetails.setUserId(TypeUtil.toInt(userVO.getUserId()));
				
				userHash.setUserDetails(userDetails);
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"UserHash from a UserVO.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapUserVOToUserHashModel",userHash);
		return userHash;
	}
	
}
