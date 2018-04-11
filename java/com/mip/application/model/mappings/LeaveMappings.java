package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.UserDetails;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.view.UserLeaveVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class LeaveMappings {
	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LeaveMappings.class);
	
	
	public static UserLeaveDetails mapUserLeaveVOToUserLeaveDetails(
			UserLeaveVO userLeaveVO,UserDetails userDetails)
			throws MISPException {
		
		Object[] params = { userLeaveVO, userDetails };
		logger.entering("mapUserLeaveVOToUserLeaveDetails", params);
		UserLeaveDetails userLeaveDetails=new UserLeaveDetails();
		Date currentDate = new Date();
		UserDetails user = new UserDetails();
		try{
			user.setUserId(userLeaveVO.getUserId());
			userLeaveDetails.setUser(user);
			userLeaveDetails.setReason(userLeaveVO.getReason());
			userLeaveDetails.setCreatedDate(currentDate);
			userLeaveDetails.setCreatedBy(userDetails);
			userLeaveDetails.setLeaveDate(userLeaveVO.getLeaveDate());
			
		}catch (Exception e) 
		{
			logger.error("Exception in mapping UserLeaveVO to UserLeaveDetails", e);
			
			throw new MISPException(e);
		}
		
		logger.exiting("mapUserLeaveVOToUserLeaveDetails", userLeaveDetails);
		
		return userLeaveDetails;
	
	
	
	}
	}


