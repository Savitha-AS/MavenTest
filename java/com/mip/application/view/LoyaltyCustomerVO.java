package com.mip.application.view;


public class LoyaltyCustomerVO {
	
	
	private Integer lcId;
	private String customerName;
	private String msisdn;
	private String isLoyaltyEligible;
	private String confirmedDate;
	private String isConfirmed;
	private String loyaltyProvidedDate;
	private Integer productId;
	private Integer isDataPackage ;
	private String productName;
	private Integer snId;
	private Integer custId;
	
	
	
	public Integer getLcId() {
		return lcId;
	}
	public void setLcId(Integer lcId) {
		this.lcId = lcId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getConfirmedDate() {
		return confirmedDate;
	}
	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}
	public String getIsConfirmed() {
		return isConfirmed;
	}
	public void setIsConfirmed(String isConfirmed) {
		this.isConfirmed = isConfirmed;
	}
	public String getLoyaltyProvidedDate() {
		return loyaltyProvidedDate;
	}
	public void setLoyaltyProvidedDate(String loyaltyProvidedDate) {
		this.loyaltyProvidedDate = loyaltyProvidedDate;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	
	
	public Integer getIsDataPackage() {
		return isDataPackage;
	}
	public void setIsDataPackage(Integer isDataPackage) {
		this.isDataPackage = isDataPackage;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Integer getSnId() {
		return snId;
	}
	public void setSnId(Integer snId) {
		this.snId = snId;
	}
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public String getIsLoyaltyEligible() {
		return isLoyaltyEligible;
	}
	public void setIsLoyaltyEligible(String isLoyaltyEligible) {
		this.isLoyaltyEligible = isLoyaltyEligible;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoyaltyCustomerVO [lcId=");
		builder.append(lcId);
		builder.append(", customerName=");
		builder.append(customerName);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", isLoyaltyEligible=");
		builder.append(isLoyaltyEligible);
		builder.append(", confirmedDate=");
		builder.append(confirmedDate);
		builder.append(", isConfirmed=");
		builder.append(isConfirmed);
		builder.append(", loyaltyProvidedDate=");
		builder.append(loyaltyProvidedDate);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", isDataPackage=");
		builder.append(isDataPackage);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", snId=");
		builder.append(snId);
		builder.append(", custId=");
		builder.append(custId);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	
}
