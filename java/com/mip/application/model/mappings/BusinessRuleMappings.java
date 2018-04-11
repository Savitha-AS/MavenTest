package com.mip.application.model.mappings;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mip.application.model.BusinessRuleDefinition;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.InsuranceCompany;
import com.mip.application.view.BusinessRuleVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */


/**
 * This class contains the methods for mapping BusinessRuleVo to
 *  BusinessRuleMaster Model.
 * 
 * @author THBS
 *
 */

public class BusinessRuleMappings {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleMappings.class);
	
	public static BusinessRuleMaster mapBusinessRuleVOToBusinessRuleModel(
			BusinessRuleVO businessRuleVO) throws MISPException{
		
		logger.entering("mapBusinessRuleVOToBusinessRuleModel", businessRuleVO);

		BusinessRuleMaster busRuleMaster = new BusinessRuleMaster();
		
		try{
			
			if (businessRuleVO != null) {
				
				busRuleMaster.setActive((byte) 0);
				busRuleMaster.setPremiumAmtPerc(businessRuleVO.getPremiumAmtPerc());
				
				InsuranceCompany inCompany = new InsuranceCompany();
				inCompany.setInsCompId(businessRuleVO.getInsurer());
				
				busRuleMaster.setInsuranceCompany(inCompany);
				busRuleMaster.setCreatedDate(new Date());
			
				
				List<BusinessRuleDefinition> busRuleDefList = BusinessRuleMappings
						.mapBusinessRuleVOToBusinessRuleDefinition(businessRuleVO,
								busRuleMaster);
				
				busRuleMaster.setBusRuleDef(busRuleDefList);
			}
			
		} catch (Exception exception) {
			logger.error(
					"Exception occurred while Mapping BusinessRuleVO to BusinessRuleMaster Model.",
					exception);
			
			throw new MISPException("Exception occurred while Mapping BusinessRuleVO to BusinessRuleMaster Model", 
					exception);
			
		}

		logger.exiting("mapBusinessRuleVOToBusinessRuleModel",busRuleMaster);
		return busRuleMaster;
	}

	public static List<BusinessRuleDefinition> mapBusinessRuleVOToBusinessRuleDefinition(
			BusinessRuleVO businessRuleVO, BusinessRuleMaster businessRuleMaster) throws MISPException {
		
		logger.entering("mapBusinessRuleVOToBusinessRuleDefinition", 
				businessRuleVO, businessRuleMaster);

		List<BusinessRuleDefinition> busRuleDefList = new ArrayList<BusinessRuleDefinition>();
		
		try{
			
			if (businessRuleVO != null) {
			
			String[] from = businessRuleVO.getFrom();
		
			for (int counter = 0; from != null && counter < from.length; counter++) {
				
				if(from[counter] != null && 
						from[counter].trim().length() !=0){
					
					BusinessRuleDefinition busRuleDef = new BusinessRuleDefinition();
										
					busRuleDef.setBrRangeFrom(TypeUtil.toFloat(from[counter]));
					
					busRuleDef.setBrRangeTo(TypeUtil.toFloat(
							businessRuleVO.getTo()[counter]));
					
					busRuleDef.setBrRangeVal(TypeUtil.toFloat(
							businessRuleVO.getAssuredCover()[counter]));
					
					busRuleDef.setBusinessRuleMaster(businessRuleMaster);
					
					busRuleDefList.add(busRuleDef);
										
				}
			
			}
		  }
			
		}  catch (Exception exception) {
			logger.error(
					"Exception occurred while Mapping BusinessRuleVO to BusinessRuleMaster Model.",
					exception);
			
			throw new MISPException("Exception occurred while Mapping BusinessRuleVO to BusinessRuleMaster Model", 
					exception);
			
		}
		
		logger.exiting("mapBusinessRuleVOToBusinessRuleDefinition",busRuleDefList);
		return busRuleDefList;
	}
}
