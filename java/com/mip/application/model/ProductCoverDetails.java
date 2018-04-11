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
 * This is a Model class to hold the Offer Cover details.
 * @author THBS
 *
 */
public class ProductCoverDetails extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 6751583649051048741L;

	/**
	 * <code>int</code> represents the productcover id.
	 */
	private int productLevelId;
	
	/**
	 * <code>ProductDetails</code> represents the product details.
	 */
	private ProductDetails productDetails;
	
	/**
	 * <code>float</code> represents the producted cover.
	 */
	private float productCover;
	
	/**
	 * <code>float</code> represents the productcover charges.
	 */
	private float coverCharges;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductCoverDetails [productLevelId=");
		builder.append(productLevelId);
		builder.append(", productCover=");
		builder.append(productCover);
		builder.append(", coverCharges=");
		builder.append(coverCharges);
		builder.append("]");
		return builder.toString();
	}

	public int getProductLevelId() {
		return productLevelId;
	}

	public void setProductLevelId(int productLevelId) {
		this.productLevelId = productLevelId;
	}
	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}

	public float getProductCover() {
		return productCover;
	}

	public void setProductCover(float productCover) {
		this.productCover = productCover;
	}

	public float getCoverCharges() {
		return coverCharges;
	}

	public void setCoverCharges(float coverCharges) {
		this.coverCharges = coverCharges;
	}

}
