package com.mip.application.model;


/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * This is a Model class to hold the BusinessRule Definition.
 * @author THBS
 *
 */

public class BusinessRuleDefinition extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 865709596261794937L;

	/**
	 * <code>int</code> represents the id.
	 */
	private int brDefId;
	
	/**
	 * <code>BusinessRuleMaster</code>, represents the business rule.
	 */
	private BusinessRuleMaster businessRuleMaster;
	
	/**
	 * <code>int</code>, represents the business rule range number.
	 */
	private int brRangeNum;
	
	/**
	 * <code>float</code>, represents the business rule range from.
	 */
	private float brRangeFrom;
	
	/**
	 * <code>BusinessRuleMaster</code>, represents the business rule range to.
	 */
	private float brRangeTo;
	
	/**
	 * <code>BusinessRuleMaster</code>, represents the business rule value.
	 */
	private float brRangeVal;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BusinessRuleDefinition [brDefId=");
		builder.append(brDefId);
		builder.append(", brRangeNum=");
		builder.append(brRangeNum);
		builder.append(", brRangeFrom=");
		builder.append(brRangeFrom);
		builder.append(", brRangeTo=");
		builder.append(brRangeTo);
		builder.append(", brRangeVal=");
		builder.append(brRangeVal);
		builder.append("]");
		return builder.toString();
	}

	/**
     * Returns the id.
     * @return a <code>int</code> containing the business rule definiton id.
     */
	public int getBrDefId() {
		return brDefId;
	}
	
	/**
     * Sets the id.
     * @param brDefId,
     *            a <code>int</code> containing the id.
     */
	public void setBrDefId(int brDefId) {
		this.brDefId = brDefId;
	}

	/**
     * Returns the business rule.
     * @return  <code>BusinessRuleMaster</code>, containing the business rule.
     */
	public BusinessRuleMaster getBusinessRuleMaster() {
		return businessRuleMaster;
	}

	/**
     * Sets the business rule.
     * @param brDefId,
     *            a <code>int</code> containing the business rule.
     */
	public void setBusinessRuleMaster(BusinessRuleMaster businessRuleMaster) {
		this.businessRuleMaster = businessRuleMaster;
	}

	/**
     * Returns the business rule range number.
     * @return  <code>int</code>, containing the business rule range number.
     */
	public int getBrRangeNum() {
		return brRangeNum;
	}

	/**
     * Sets the offerId.
     * @param offerId,
     *            a <code>int</code> containing the id.
     */
	public void setBrRangeNum(int brRangeNum) {
		this.brRangeNum = brRangeNum;
	}

	/**
     * Returns the business rule range from value.
     * @return  <code>int</code>, containing the business rule range from value.
     */
	public float getBrRangeFrom() {
		return brRangeFrom;
	}

	/**
     * Sets the index numbering..
     * @param brRangeFrom,
     *            a <code>int</code> containing the index number.
     */
	public void setBrRangeFrom(float brRangeFrom) {
		this.brRangeFrom = brRangeFrom;
	}

	/**
     * Returns the business rule range to value.
     * @return  <code>int</code>, containing the business rule range to value.
     */
	public float getBrRangeTo() {
		return brRangeTo;
	}

	/**
     * Sets the business range to value.
     * @param brRangeFrom,
     *            a <code>int</code> containing the business range to value.
     */
	public void setBrRangeTo(float brRangeTo) {
		this.brRangeTo = brRangeTo;
	}

	/**
     * Returns the business rule range value.
     * @return  <code>int</code>, containing the business rule range value.
     */
	public float getBrRangeVal() {
		return brRangeVal;
	}

	/**
     * Sets the business range from value.
     * @param brRangeFrom,
     *            a <code>int</code> containing the business range from value.
     */
	public void setBrRangeVal(float brRangeVal) {
		this.brRangeVal = brRangeVal;
	}
}
