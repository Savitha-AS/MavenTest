package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.view.CustomerVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.StringUtil;
import com.mip.framework.utils.TypeUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 29/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>CustomerMappings.java</code> contains all the methods pertaining to
 * Customer Management use case model. This is a mapping class for all customer
 * management modules. The methods is used to convert a VO Object to a DAL
 * Object.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class CustomerMappings {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerMappings.class);
	
	/**
	 * This method is used to get the Customer details model to be updated in the platform while modification
	 * @param custModel - instance of customer details model
	 * @param customerVO - instance of Customer VO
	 * @param userDetails - instance of user details
	 * @return CustomerDetails
	 */
	
	public static CustomerDetails getCustModelToUpdate(CustomerDetails custModel, CustomerVO customerVO, UserDetails userDetails) throws MISPException{

		logger.entering("getCustModelToUpdate", custModel,customerVO,userDetails);
		
		try {
			custModel.setFname(customerVO.getFname());
			
			custModel.setSname(customerVO.getSname());
			
			custModel.setDob(DateUtil.toDate(customerVO.getDob()));
			
			custModel.setAge(TypeUtil.toInt(customerVO.getAge()));
			
			custModel.setMsisdn(customerVO.getMsisdn());
			
			custModel.setGender(customerVO.getGender());	
			
			custModel.setDeductionMode(Integer.valueOf(customerVO.getDeductionMode()));
			
			custModel.setModifiedDate(new Date());
			
			custModel.setModifiedBy(userDetails);	
			
			custModel.setImpliedAge(TypeUtil.toInt(customerVO.getAge()));
		}
		catch (Exception e) 
		{
			logger.error("Exception in getCustModelToUpdate mappping class", e);
			
			throw new MISPException(e);
		}
		
		logger.exiting("getCustModelToUpdate", custModel);
		
		return custModel;
	}
	
	/**
	 * This method is used to get the Insurance details model 
	 * to be updated in the platform while customer modification
	 * @param insModel - instance of Insurance details model
	 * @param customerVO - instance of Customer VO
	 * @param userDetails - instance of user details
	 * @return InsuredRelativeDetails
	 */
	
	public static InsuredRelativeDetails getInsModelToUpdate(InsuredRelativeDetails insModel, CustomerVO customerVO, UserDetails userDetails) throws MISPException{
		
		logger.entering("getInsModelToUpdate", insModel,customerVO,userDetails);
		try
		{			
			
				insModel.setFname(customerVO.getInsRelFname());
				
				insModel.setSname(customerVO.getInsRelSurname());
				
				insModel.setDob(DateUtil.toDate(customerVO.getInsRelIrDoB()));
				
				insModel.setAge(TypeUtil.toInt(customerVO.getInsRelAge()));
				
				insModel.setCustRelationship(customerVO.getInsRelation());
				
				insModel.setInsMsisdn(customerVO.getInsMsisdn());
				
				insModel.setOfferId(Integer.parseInt(PlatformConstants.PRODUCT_XL));
		
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
	
	public static InsuredRelativeDetails getNomineeModelToUpdate(InsuredRelativeDetails insModel, CustomerVO customerVO, UserDetails userDetails) throws MISPException{
		
		logger.entering("getInsModelToUpdate", insModel,customerVO,userDetails);
		try
		{			
			if(!StringUtil.isEmpty(customerVO.getIpNomFirstName()))	
					{
						insModel.setFname(customerVO.getIpNomFirstName());
						
						insModel.setSname(customerVO.getIpNomSurName());
						
						insModel.setAge(TypeUtil.toInt(customerVO.getIpNomAge()));
						
						insModel.setInsMsisdn(customerVO.getIpInsMsisdn());
						
						insModel.setOfferId(Integer.parseInt(PlatformConstants.PRODUCT_IP));
						
						insModel.setDob(null);
						
						insModel.setCustRelationship(null);
				
					}
			
				
		
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
