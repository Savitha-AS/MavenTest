package com.mip.application.model;

import java.util.Date;
import java.util.Set;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * This is a Model class to hold the Offer details.
 * @author THBS
 *
 */
public class ProductDetails extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 954907016126241713L;
  
	/**
	 * <code>int</code> represents the productId.
	 */
	private int productId;
	
	/**
	 * <code>String</code> represents the productName.
	 */
	private String productName;
	
	/**
	 * <code>String</code> represents the product description.
	 */
	private String productDescription;
	private String productCode;
	
	/**
	 * <code>Date</code> represents the product created date.
	 */
	private Date productCreatedDate;
	
	/**
	 * <code>UserDetails</code> represents user.
	 */
	private UserDetails productCreatedBy;
	
	/**
	 * <code>Set</code> represents productcover details.
	 */
	private Set<ProductCoverDetails> productCoverDetails;
	
	/**
	 * <code>Integer</code> represents the per day deduction.
	 */
	private Float perDayDeduction;
	
	/**
	 * <code>Byte</code> represents the per day deduction.
	 */
	private Byte active;
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductDetails [productId=");
		builder.append(productId);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", productDescription=");
		builder.append(productDescription);
		builder.append(", productCode=");
		builder.append(productCode);
		builder.append(", productCreatedDate=");
		builder.append(productCreatedDate);
		builder.append(", perDayDeduction=");
		builder.append(perDayDeduction);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Date getProductCreatedDate() {
		return productCreatedDate;
	}

	public void setProductCreatedDate(Date productCreatedDate) {
		this.productCreatedDate = productCreatedDate;
	}

	public UserDetails getProductCreatedBy() {
		return productCreatedBy;
	}

	public void setProductCreatedBy(UserDetails productCreatedBy) {
		this.productCreatedBy = productCreatedBy;
	}

	public Set<ProductCoverDetails> getProductCoverDetails() {
		return productCoverDetails;
	}

	public void setProductCoverDetails(Set<ProductCoverDetails> productCoverDetails) {
		this.productCoverDetails = productCoverDetails;
	}

	public Float getPerDayDeduction() {
		return perDayDeduction;
	}

	public void setPerDayDeduction(Float perDayDeduction) {
		this.perDayDeduction = perDayDeduction;
	}

	public Byte getActive() {
		return active;
	}

	public void setActive(Byte active) {
		this.active = active;
	}

}
