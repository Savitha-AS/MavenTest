package com.mip.application.model;

import java.util.Date;
import java.util.List;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * This is a Model class to hold the BusinessRule.
 * @author THBS
 *
 */

public class BusinessRuleMaster extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -3386962984775899175L;

	/**
	 * <code>int</code> represents the id.
	 */
	private int brId;
	
	/**
	 * <code>String</code> represents business rule version.
	 */
	private String brVersion;
	
	/**
	 * <code>float</code> represents the premium amount.
	 */
	private float premiumAmtPerc;
	
	/**
	 * <code>float</code> represents the active status.
	 */
	private byte active;
	
	/**
	 * <code>InsuranceCompany</code> represents the insurance company details.
	 */
	private InsuranceCompany insuranceCompany;
	
	/**
	 * <code>Date</code> represents the created date.
	 */
	private Date createdDate;
	
	/**
	 * <code>UserDetails</code> represents the user.
	 */
	private UserDetails createdBy;
	
	/**
	 * <code>List</code> represents the business rule definition.
	 */
	private List<BusinessRuleDefinition> busRuleDef;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessRuleMaster [brId=");
		builder.append(brId);
		builder.append(", brVersion=");
		builder.append(brVersion);
		builder.append(", active=");
		builder.append(active);
		builder.append(",premiumAmtPerc=");
		builder.append(premiumAmtPerc);
		builder.append(", createdDate=");
		builder.append(createdDate);
		//builder.append(", createdBy=");
		//builder.append(createdBy);
		builder.append("]");
		return builder.toString();
	}

	/**
     * Returns the id.
     * @return a <code>int</code> containing the business rule id.
     */
	public int getBrId() {
		return brId;
	}

	/**
     * Sets the business rule id.
     * @param brId,
     *     <code>int</code> containing the business rule id.
     */
	public void setBrId(int brId) {
		this.brId = brId;
	}
	
	/**
     * Returns the business rule version.
     * @return  <code>String</code> containing the business rule version.
     */
	public String getBrVersion() {
		return brVersion;
	}

	/**
     * Sets the business rule version.
     * @param brVersion,
     *     <code>String</code> containing the business rule version.
     */
	public void setBrVersion(String brVersion) {
		this.brVersion = brVersion;
	}

	/**
     * Returns the active status.
     * @return  <code>byte</code> containing the active or inactive status
     */
	public byte getActive() {
		return active;
	}

	/**
     * Sets the business rule status.
     * @param active,
     *     <code>byte</code> containing the business rule status.
     */
	public void setActive(byte active) {
		this.active = active;
	}

	/**
     * Returns the insurance company details.
     * @return  <code>InsuranceCompany</code> containing the insurance company details.
     */
	public InsuranceCompany getInsuranceCompany() {
		return insuranceCompany;
	}

	/**
     * Sets the Insurance Company details.
     * @param insuranceCompany,
     *     <code>InsuranceCompany</code> containing the Insurance Company details.
     */
	public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	/**
     * Returns the business rule created date.
     * @return <code>Date</code> containing the business rule created date.
     */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
     * Sets the business rule created date.
     * @param createdDate,
     *     <code>Date</code> containing the business rule created date.
     */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
     * Returns the business rule created by.
     * @return <code>int</code>, containing the business rule created by userid.
     */
	public UserDetails getCreatedBy() {
		return createdBy;
	}

	/**
     * Sets the offer created by user details.
     * @param createdBy,
     *     <code>int</code> containing the offer created by.
     */
	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}
	
	/**
     * Returns the business rule definitions.
     * @return <code>List</code>, containing the business rule definitions.
     */
	public List<BusinessRuleDefinition> getBusRuleDef() {
		return busRuleDef;
	}
	
	/**
     * Sets the business rule definitions.
     * @param <code>List</code>, containing the business rule definitions.
     */
	public void setBusRuleDef(List<BusinessRuleDefinition> busRuleDef) {
		this.busRuleDef = busRuleDef;
	}
	
	/**
     * Returns the Insurance premium amount.
     * @return <code>float</code>, containing the insurance premium amount.
     */
	public float getPremiumAmtPerc() {
		return premiumAmtPerc;
	}

	/**
     * Sets the Insurance premium amount.
     * @param premiumAmtPerc,
     *     <code>float</code> containing the Insurance premium amount.
     */
	public void setPremiumAmtPerc(float premiumAmtPerc) {
		this.premiumAmtPerc = premiumAmtPerc;
	}

	/** 
	 * Checks if 2 Business Rules are same.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		
		BusinessRuleMaster businessRule1 = this;
		BusinessRuleMaster businessRule2 = (BusinessRuleMaster)arg0;
		
		if(businessRule2 != null){
			if(businessRule1.getBrVersion().equalsIgnoreCase(
					businessRule2.getBrVersion())){
				
				return true;
			}
		}		
		return false;
	}
	
	
}
