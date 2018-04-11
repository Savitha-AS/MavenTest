package com.mip.application.view;

import java.io.Serializable;

public class ProductCoverDetailsVO implements Serializable{
	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 4166256903602160832L;
	
	private String productCoverId;
	private String productCover;
	private String productCoverIP;
	private String productCoverCharges;
	
	
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductCoverDetailsVO [productCoverId=");
		builder.append(productCoverId);
		builder.append(", productCover=");
		builder.append(productCover);
		builder.append(", productCoverIP=");
		builder.append(productCoverIP);
		builder.append(", productCoverCharges=");
		builder.append(productCoverCharges);
		builder.append("]");
		return builder.toString();
	}
	public String getProductCoverCharges() {
		return productCoverCharges;
	}
	public void setProductCoverCharges(String productCoverCharges) {
		this.productCoverCharges = productCoverCharges;
	}
	public String getProductCoverIP() {
		return productCoverIP;
	}
	public void setProductCoverIP(String productCoverIP) {
		this.productCoverIP = productCoverIP;
	}
	public String getProductCoverId() {
		return productCoverId;
	}
	public void setProductCoverId(String productCoverId) {
		this.productCoverId = productCoverId;
	}
	public String getProductCover() {
		return productCover;
	}
	public void setProductCover(String productCover) {
		this.productCover = productCover;
	}
	
	


}
