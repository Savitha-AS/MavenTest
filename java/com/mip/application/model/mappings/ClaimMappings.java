package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.ClaimDetails;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.view.ClaimVO;
import com.mip.application.view.CustomerVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * <code>ClaimMappings.java</code> contains all VO to Model mappings w.r.t Claim
 * use case model.
 * 
 * @author T H B S
 */
public class ClaimMappings {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ClaimMappings.class);

	/**
	 * This method is used to map customer related details to claim details
	 * 
	 * @param customerDetails
	 *            - instance of CustomerDetails
	 * @param insDetails
	 *            - instance of InsuredRelativeDetails
	 * @param userDetails
	 *            - instance of UserDetails
	 * 
	 * @return - instance of ClaimDetails
	 * 
	 * @throws MISPException
	 */
	public static ClaimDetails getClaimDetailsModelForClaim(
			CustomerDetails customerDetails, InsuredRelativeDetails insDetails,
			UserDetails userDetails) throws MISPException {
		logger.entering("getClaimDetailsModelForClaim");

		ClaimDetails claimModel = new ClaimDetails();

		try {
			claimModel.setFname(customerDetails.getFname());

			claimModel.setSname(customerDetails.getSname());

			claimModel.setMsisdn(customerDetails.getMsisdn());

			claimModel.setDob(customerDetails.getDob());

			claimModel.setAge(customerDetails.getAge());

			claimModel.setGender(customerDetails.getGender());
			if (null != insDetails) {
				if (insDetails.getOfferId() == 4) {
					claimModel.setIpNomFirstName(insDetails.getFname());
					claimModel.setIpNomFirstName(insDetails.getSname());
					claimModel.setIpNomAge(insDetails.getAge() + "");
				} else {
					claimModel.setRelation(insDetails.getCustRelationship());

					claimModel.setInsRelFname(insDetails.getFname());

					claimModel.setInsRelSurname(insDetails.getSname());

					claimModel.setInsRelIrDoB(insDetails.getDob());

					claimModel.setInsRelAge(insDetails.getAge());
				}
			}

			claimModel.setClaimedDate(new Date());

			claimModel.setClaimedBy(userDetails);
		} catch (Exception e) {
			logger.error(
					"Exception occured while mapping ClaimVO to ClaimDetails",
					e);

			throw new MISPException(e);
		}

		logger.exiting("getClaimDetailsModelForClaim");
		return claimModel;
	}

	/**
	 * This method returns the customer details model for claim.
	 * 
	 * @param claimVO
	 *            - instance of ClaimVO
	 * @param customerDetails
	 *            - instance of CustomerDetails
	 * @param userDetails
	 *            - instance of UserDetails
	 * @return - instance of CustomerDetails
	 * @throws MISPException
	 */
	public static CustomerDetails getCustomerModelForClaim(ClaimVO claimVO,
			CustomerDetails customerDetails, UserDetails userDetails)
			throws MISPException {
		logger.entering("getCustomerModelForClaim");

		try {

			customerDetails.setFname(claimVO.getFname());
			customerDetails.setSname(claimVO.getSname());
			customerDetails.setMsisdn(claimVO.getMsisdn());
			customerDetails.setDob(DateUtil.toDate(claimVO.getDob()));
			customerDetails.setAge(TypeUtil.toInt(claimVO.getAge()));
			customerDetails.setGender(claimVO.getGender());
			customerDetails.setModifiedBy(userDetails);
			customerDetails.setModifiedDate(new Date());
			customerDetails.setImpliedAge(TypeUtil.toInt(claimVO.getAge()));

		} catch (Exception e) {
			logger.error(
					"Exception in getCustomerModelForClaim mappping class", e);

			throw new MISPException(e);
		}

		logger.exiting("getCustomerModelForClaim", customerDetails);
		return customerDetails;
	}

	/**
	 * This method returns the insured relative model for claim.
	 * 
	 * @param claimVO
	 * @param insuredRelativeModel
	 * @return
	 * @throws MISPException
	 */
	public static InsuredRelativeDetails getInsuranceModelForClaim(
			ClaimVO claimVO, InsuredRelativeDetails insuredRelativeModel)
			throws MISPException {
		logger.entering("getInsuranceModelForClaim");

		try {
			if(insuredRelativeModel!=null)
				{
				insuredRelativeModel.setFname(claimVO.getInsRelFname());
				insuredRelativeModel.setSname(claimVO.getInsRelSurname());
				insuredRelativeModel.setCustRelationship(claimVO.getRelation());
				insuredRelativeModel.setDob(DateUtil.toDate(claimVO
						.getInsRelIrDoB()));
				insuredRelativeModel.setAge(TypeUtil.toInt(claimVO.getInsRelAge()));
				}
			

		} catch (Exception e) {
			logger.error(
					"Exception in getInsuranceModelForClaim mappping class", e);

			throw new MISPException(e);
		}

		logger.exiting("getInsuranceModelForClaim", insuredRelativeModel);
		return insuredRelativeModel;
	}
	/**
	 * This method is used to get the Insurance details model 
	 * to be updated in the platform while customer modification
	 * @param insModel - instance of Insurance details model
	 * @param customerVO - instance of Customer VO
	 * @param userDetails - instance of user details
	 * @return InsuredRelativeDetails
	 */
	
	public static InsuredRelativeDetails getInsModelToUpdate(InsuredRelativeDetails insModel, 
			ClaimVO claimVO, UserDetails userDetails) throws MISPException{
		
		logger.entering("getInsModelToUpdate", insModel,claimVO,userDetails);
		try
		{			
			
				insModel.setFname(claimVO.getInsRelFname());
				
				insModel.setSname(claimVO.getInsRelSurname());
				
				insModel.setDob(DateUtil.toDate(claimVO.getInsRelIrDoB()));
				
				insModel.setAge(TypeUtil.toInt(claimVO.getInsRelAge()));
				
				insModel.setCustRelationship(claimVO.getRelation());
				
				insModel.setOfferId(Integer.parseInt(PlatformConstants.PRODUCT_XL));
				
				insModel.setInsMsisdn(claimVO.getInsMsisdn());
		
		}
		catch (Exception e) 
		{
			logger.error("Exception in getInsModelToUpdate mappping class", e);
			
			throw new MISPException(e);
		}
		
		logger.exiting("getInsModelToUpdate", insModel);
		
		return insModel;
	}
	
	/**
	 * This method is used to get the Insurance details model 
	 * to be updated in the platform while customer modification
	 * @param insModel - instance of Insurance details model
	 * @param customerVO - instance of Customer VO
	 * @param userDetails - instance of user details
	 * @return InsuredRelativeDetails
	 */
	
	public static InsuredRelativeDetails getNomineeModelToUpdate(InsuredRelativeDetails insModel, ClaimVO claimVO, UserDetails userDetails) throws MISPException{
		
		logger.entering("getInsModelToUpdate", insModel,claimVO,userDetails);
		try
		{			
				
				insModel.setFname(claimVO.getIpNomFirstName());
				
				insModel.setSname(claimVO.getIpNomSurName());
				
				insModel.setAge(TypeUtil.toInt(claimVO.getIpNomAge()));
				
				insModel.setOfferId(Integer.parseInt(PlatformConstants.PRODUCT_IP));
				
				insModel.setDob(null);
				
				insModel.setCustRelationship(null);
				
				insModel.setInsMsisdn(claimVO.getIpInsMsisdn());
				
		
		}
		catch (Exception e) 
		{
			logger.error("Exception in getInsModelToUpdate mappping class", e);
			
			throw new MISPException(e);
		}
		
		logger.exiting("getInsModelToUpdate", insModel);
		
		return insModel;
	}
	
	
}
