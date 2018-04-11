package com.mip.application.model;

import java.math.BigDecimal;

public class LoyalCustomerDetails extends BaseModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1567742106407457845L;
	private int lcId;
	private int custId;
	private int snId;
	private String msisdn;
	private int productId;
	private int productLevelId;
	private int isData;
	private BigDecimal deductedAmount;
	private int isProcessed;
	private int isLoyaltyProvided;
	private int attemptCount;
	private String createdDate;
	private String loyaltyProvidedDate;
	private int isActive;

	public int getLcId() {
		return lcId;
	}

	public void setLcId(int lcId) {
		this.lcId = lcId;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public int getSnId() {
		return snId;
	}

	public void setSnId(int snId) {
		this.snId = snId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getProductLevelId() {
		return productLevelId;
	}

	public void setProductLevelId(int productLevelId) {
		this.productLevelId = productLevelId;
	}

	public int getIsData() {
		return isData;
	}

	public void setIsData(int isData) {
		this.isData = isData;
	}

	public BigDecimal getDeductedAmount() {
		return deductedAmount;
	}

	public void setDeductedAmount(BigDecimal deductedAmount) {
		this.deductedAmount = deductedAmount;
	}

	public int getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(int isProcessed) {
		this.isProcessed = isProcessed;
	}

	public int getIsLoyaltyProvided() {
		return isLoyaltyProvided;
	}

	public void setIsLoyaltyProvided(int isLoyaltyProvided) {
		this.isLoyaltyProvided = isLoyaltyProvided;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLoyaltyProvidedDate() {
		return loyaltyProvidedDate;
	}

	public void setLoyaltyProvidedDate(String loyaltyProvidedDate) {
		this.loyaltyProvidedDate = loyaltyProvidedDate;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoyalCustomerDetails [lcId=");
		builder.append(lcId);
		builder.append(", custId=");
		builder.append(custId);
		builder.append(", snId=");
		builder.append(snId);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", productLevelId=");
		builder.append(productLevelId);
		builder.append(", isData=");
		builder.append(isData);
		builder.append(", deductedAmount=");
		builder.append(deductedAmount);
		builder.append(", isProcessed=");
		builder.append(isProcessed);
		builder.append(", isLoyaltyProvided=");
		builder.append(isLoyaltyProvided);
		builder.append(", attemptCount=");
		builder.append(attemptCount);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", loyaltyProvidedDate=");
		builder.append(loyaltyProvidedDate);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append("]");
		return builder.toString();
	}

}
