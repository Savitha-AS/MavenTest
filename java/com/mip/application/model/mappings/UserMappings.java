package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.BranchDetails;
import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.view.UserVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * <code>UserMappings.java</code> contains all VO to Model mappings w.r.t 
 * User use case model. 
 * 
 * @author T H B S
 */
public class UserMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserMappings.class);
	
	/**
	 * Maps UserVO to UserDetails model.
	 * 
	 * @param userVO UserVO command object
	 * @param loginUserId 
	 * 			required for CreatedBy and ModifiedBy fields.
	 * @return UserDetails model object
	 * @throws MISPException
	 */
	public static UserDetails mapUserVOToUserDetailsModel(UserVO userVO, 
			int loginUserId, UserDetails userDetails) throws MISPException{	
		
		Object[] params = {userVO, loginUserId};
		logger.entering("mapUserVOToUserDetailsModel", params);	
		
		
		Date currentDate = new Date();
		byte activeFlag = 1;
		
		try{			
			if(userVO != null){
				
				if(userVO.getUserId() == null){
					// For INSERT operation.
					userDetails.setCreatedBy(loginUserId);
					userDetails.setCreatedDate(currentDate);	
					userDetails.setUserUid(userVO.getUserUid());
					userDetails.setActive(activeFlag);				
				}else{
					// For UPDATE operation.
					userDetails.setUserId(TypeUtil.toInt(userVO.getUserId()));
				}
				userDetails.setFname(userVO.getFname());
				userDetails.setSname(userVO.getSname());
				userDetails.setMsisdn(userVO.getMsisdn());
				userDetails.setEmailId(userVO.getEmail());
				userDetails.setDob(DateUtil.toDate(userVO.getDob()));
				userDetails.setAge(TypeUtil.toInt(userVO.getAge()));
				userDetails.setGender(userVO.getGender());
				userDetails.setModifiedBy(loginUserId);
				userDetails.setModifiedDate(currentDate);
				
				BranchDetails branchDetails = new BranchDetails();
				branchDetails.setBranchId(TypeUtil.toInt(userVO.getBranch()));
				userDetails.setBranchDetails(branchDetails);				
				
				/**
				 * Sets the roleMaster to null in case the search is not based   
				 * on role in Search User/Reset Password use cases.
				 * 
				 * Sets the roleMaster to selected role in Add User use cases.
				 */
				RoleMaster roleMaster = null;
				if(userVO.getRole() != null) {
					roleMaster = new RoleMaster();
					roleMaster.setRoleId(TypeUtil.toInt(userVO.getRole()));
				}
				userDetails.setRoleMaster(roleMaster);
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"UserDetails from a UserVO.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapUserVOToUserDetailsModel");
		return userDetails;
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
