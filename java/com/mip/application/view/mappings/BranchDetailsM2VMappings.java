package com.mip.application.view.mappings;

import com.mip.application.model.BranchDetails;
import com.mip.application.view.BranchVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <code>BranchDetailsM2VMappings.java</code> contains all Model to VO 
 * mappings w.r.t Branch use case model. 
 * 
 * @author T H B S
 */
public class BranchDetailsM2VMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BranchDetailsM2VMappings.class);
	
	/**
	 * Maps BranchDetails model to BranchVO.
	 * 
	 * @param branchDetails BranchDetails model object.
	 * @return BranchVO command object.
	 * @throws MISPException
	 */
	public static BranchVO mapBranchModelToBranchVO(BranchDetails branchDetails)
		throws MISPException{
		
		logger.entering("mapBranchModelToBranchVO");
		
		BranchVO branchVO = new BranchVO();
		try{
			if(branchDetails !=null){
				branchVO.setBranchId(String.valueOf(branchDetails.getBranchId()));
				branchVO.setBranchName(branchDetails.getBranchName());
				branchVO.setBranchStreet(branchDetails.getBranchStreet());
				branchVO.setBranchRegion(branchDetails.getBranchRegion());
				branchVO.setBranchCity(branchDetails.getBranchCity());
				branchVO.setBranchCode(branchDetails.getBranchCode());
			}
		}catch(Exception e){
			logger.error("An exception has occured while populating a " +
					"BranchVO from a BranchDetails.", e);
			throw new MISPException(e);
		}
		logger.exiting("mapBranchModelToBranchVO");
		return branchVO;
	}
	
}
