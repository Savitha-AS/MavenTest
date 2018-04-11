package com.mip.application.view.mappings;

import com.mip.application.model.BranchDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.view.UserVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/**
 * <code>UserDetailsM2VMappings.java</code> contains all Model to VO 
 * mappings w.r.t User use case model. 
 * 
 * @author T H B S
 */
public class UserDetailsM2VMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserDetailsM2VMappings.class);
	
	/**
	 * Maps UserDetails model to UserVO.
	 * 
	 * @param userDetails UserDetails model object.
	 * @return UserVO command object.
	 * @throws MISPException
	 */
	public static UserVO mapUserModelToUserVO(UserDetails userDetails)
		throws MISPException{
		
		logger.entering("mapUserModelToUserVO");
		
		UserVO userVO = new UserVO();
		try{
			if(userDetails !=null){
				userVO.setFname(userDetails.getFname());
				userVO.setSname(userDetails.getSname());
				userVO.setAge(Integer.toString(userDetails.getAge()));
				userVO.setMsisdn(userDetails.getMsisdn());
				userVO.setEmail(userDetails.getEmailId());
				userVO.setRole(userDetails.getRoleMaster().getRoleName());
				userVO.setRoleId(""+userDetails.getRoleMaster().getRoleId());
				userVO.setGender(userDetails.getGender());
				userVO.setDob(DateUtil.toDateString(userDetails.getDob()));
				userVO.setUserId(Integer.toString(userDetails.getUserId()));
				userVO.setUserUid(userDetails.getUserUid());
				userVO.setCreatedBy(""+userDetails.getCreatedBy());
				userVO.setCreatedDate(DateUtil
						.toDateString(userDetails.getCreatedDate()));
				
				BranchDetails branchDetails = userDetails.getBranchDetails();
				if(branchDetails != null){
					userVO.setBranch(branchDetails.getBranchName());
					userVO.setBranchId(""+branchDetails.getBranchId());
				}				
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"UserVO from a UserDetails.", e);
			throw new MISPException(e);
		}
		logger.exiting("mapUserModelToUserVO");
		return userVO;
	}
	
}
