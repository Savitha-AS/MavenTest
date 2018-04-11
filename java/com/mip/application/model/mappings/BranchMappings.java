package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.BranchDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.view.BranchVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/**
 * <code>BranchMappings.java</code> contains all VO to Model mappings w.r.t 
 * Branch use case model. 
 * 
 * @author T H B S
 */
public class BranchMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BranchMappings.class);
	
	/**
	 * Maps BranchVO to BranchDetails model.
	 * 
	 * @param branchVO BranchVO command object
	 * @param loginUserId 
	 * 			required for CreatedBy and ModifiedBy fields.
	 * @return BranchDetails model object
	 * @throws MISPException
	 */
	public static BranchDetails mapBranchVOToBranchDetailsModel(BranchVO branchVO, 
			UserDetails loginUserDetails, BranchDetails branchDetails, 
			boolean deleteFlag)	throws MISPException{	
		
		Object[] params = {branchVO, loginUserDetails, deleteFlag};
		logger.entering("mapBranchVOToBranchDetailsModel", params);	
		
		
		Date currentDate = new Date();
		byte activeFlag = 1;
		
		try{			
			if(branchVO != null){
				if(deleteFlag){
					// For DELETE/INACTIVATE operation.
					activeFlag = 0;
					branchDetails.setActive(activeFlag);
				}
				if(branchVO.getBranchId() == null){
					// For INSERT operation.
					branchDetails.setCreatedBy(loginUserDetails);
					branchDetails.setCreatedDate(currentDate);	
					branchDetails.setBranchCode(branchVO.getBranchCode());
					branchDetails.setActive(activeFlag);				
				}else{
					// For UPDATE operation.
					branchDetails.setBranchId(TypeUtil.toInt(branchVO.getBranchId()));
				}
				branchDetails.setBranchName(branchVO.getBranchName());
				branchDetails.setBranchStreet(branchVO.getBranchStreet());
				branchDetails.setBranchRegion(branchVO.getBranchRegion());
				branchDetails.setBranchCity(branchVO.getBranchCity());
				branchDetails.setModifiedBy(loginUserDetails);
				branchDetails.setModifiedDate(currentDate);
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"BranchDetails from a BranchVO.", e);
			throw new MISPException(e);
		}

		logger.exiting("mapBranchVOToBranchDetailsModel");
		return branchDetails;
	}

	
}
