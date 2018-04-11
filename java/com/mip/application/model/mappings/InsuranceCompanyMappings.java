package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.UserDetails;
import com.mip.application.view.InsuranceCompanyVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 01/05/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>InsuranceCompanyMappings.java</code> contains the mapping of the VO
 * objects to the MO objects.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class InsuranceCompanyMappings {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			InsuranceCompanyMappings.class);

	/**
	 * This method is used to map a insurance company VO object to a insurance
	 * company DAO object.
	 * @param insuranceCompanyVO - instance of InsuranceCompanyVO class {@link InsuranceCompanyVO}
	 * @param userDetails - instance of UserDetails class {@link UserDetails}
	 * @return insuranceCompany - instance of InsuranceCompany class {@link InsuranceCompany}
	 * @throws MISPException
	 */
	public static InsuranceCompany mapInsCompanyVOToInsCompanyModel(
			InsuranceCompanyVO insuranceCompanyVO, UserDetails userDetails)
			throws MISPException {

		Object[] params = { insuranceCompanyVO, userDetails };
		
		logger.entering("mapInsCompanyVOToInsCompanyModel", params);

		InsuranceCompany insuranceCompany = new InsuranceCompany();

		try {
			if (insuranceCompanyVO != null) {
				insuranceCompany.setInsCompName(insuranceCompanyVO
						.getCompanyName());
				insuranceCompany.setInsCompPhone(insuranceCompanyVO
						.getCompanyNumber());
				insuranceCompany.setInsCompBranchName(insuranceCompanyVO
						.getBranchName());
				insuranceCompany.setInsCompAddrs1(insuranceCompanyVO
						.getAddressLine1());
				insuranceCompany.setInsCompAddrs2(insuranceCompanyVO
						.getAddressLine2());
				insuranceCompany.setInsCompCity(insuranceCompanyVO.getCity());
				insuranceCompany.setInsCompState(insuranceCompanyVO.getState());
				insuranceCompany.setInsCompCountry(insuranceCompanyVO
						.getCountry());
				insuranceCompany.setInsCompZip(TypeUtil
						.toInt(insuranceCompanyVO.getZipCode()));
				insuranceCompany.setInsCompPocName(insuranceCompanyVO
						.getPrimaryContact());
				insuranceCompany.setInsCompPocMsisdn(insuranceCompanyVO
						.getPrimaryContactMobile());

				UserDetails createdByUser = new UserDetails();
				createdByUser.setUserId(userDetails.getUserId());
				insuranceCompany.setCreatedBy(createdByUser);
				
				insuranceCompany.setCreatedDate(new Date());

				UserDetails modifiedByUser = new UserDetails();
				modifiedByUser.setUserId(userDetails.getUserId());
				insuranceCompany.setModifiedBy(modifiedByUser);
				
				insuranceCompany.setModifiedDate(new Date());
			}
		} catch (Exception e) {
			
			logger.error("An exception has occured while populating a "
				+ "insuranceCompany  from a InsuranceCompanyVO.", e);
			
			throw new MISPException(e);
		}

		logger.exiting("mapInsCompanyVOToInsCompanyModel", insuranceCompany);

		return insuranceCompany;
	}
	
	/**
	 * This method is used to map the VO objects to model objects to persist in database 
	 * @param insuranceCompanyVO - Instance of <code>InsuranceCompanyVO.java</code> 
	 * @param userDetails - Instance of <code>UserDetails.java</code>
	 * @return insuranceCompany - Instance of <code>InsuranceCompany.java</code>
	 * @throws MISPException
	 */
	public static InsuranceCompany mapToInsCompanyModelToModify(InsuranceCompany insuranceCompany, InsuranceCompanyVO insuranceCompanyVO,UserDetails userDetails) throws MISPException{
		
		logger.entering("mapToInsCompanyModelToModify", insuranceCompanyVO,userDetails);

		try {
			
			if (insuranceCompanyVO != null) {
				
				insuranceCompany.setInsCompName(insuranceCompanyVO.getCompanyName());
				
				insuranceCompany.setInsCompPhone(insuranceCompanyVO.getCompanyNumber());
				
				insuranceCompany.setInsCompBranchName(insuranceCompanyVO.getBranchName());
				
				insuranceCompany.setInsCompAddrs1(insuranceCompanyVO.getAddressLine1());
				
				insuranceCompany.setInsCompAddrs2(insuranceCompanyVO.getAddressLine2());
				
				insuranceCompany.setInsCompCity(insuranceCompanyVO.getCity());
				
				insuranceCompany.setInsCompState(insuranceCompanyVO.getState());
				
				insuranceCompany.setInsCompCountry(insuranceCompanyVO.getCountry());
				
				insuranceCompany.setInsCompZip(TypeUtil.toInt(insuranceCompanyVO.getZipCode()));
				
				insuranceCompany.setInsCompPocName(insuranceCompanyVO.getPrimaryContact());
				
				insuranceCompany.setInsCompPocMsisdn(insuranceCompanyVO.getPrimaryContactMobile());	

				UserDetails modifiedBy = new UserDetails();
				modifiedBy.setUserId(userDetails.getUserId());
				
				insuranceCompany.setModifiedBy(modifiedBy);
				
				insuranceCompany.setModifiedDate(new Date());
			}
		} catch (Exception exception) {
			
			logger.error("Unknown exception has occured while mapping insuranceCompany Model to InsuranceCompany VO.", exception);
			
			throw new MISPException(exception);
		}

		logger.exiting("mapToInsCompanyModelToModify", insuranceCompany);

		return insuranceCompany;		
	}
}
