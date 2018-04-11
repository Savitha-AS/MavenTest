package com.mip.application.model;

import java.io.Serializable;
//ASSIGN_OFFER(:custMsisdn,:productIds,:regBy,:regChannel,:productCoverLevelId)")
import java.sql.Date;

public class AssignOfferRequest implements Serializable {

	private static final long serialVersionUID = 1042864505748593356L;

	private String msisdn;
	private String productIds;
	private int regBy;
	private int regChnId;
	private String productCoverLevelId;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public int getRegBy() {
		return regBy;
	}

	public void setRegBy(int regBy) {
		this.regBy = regBy;
	}

	public int getRegChnId() {
		return regChnId;
	}

	public void setRegChnId(int regChnId) {
		this.regChnId = regChnId;
	}

	public String getProductCoverLevelId() {
		return productCoverLevelId;
	}

	public void setProductCoverLevelId(String productCoverLevelId) {
		this.productCoverLevelId = productCoverLevelId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssignOfferRequest [msisdn=");
		builder.append(msisdn);
		builder.append(", productIds=");
		builder.append(productIds);
		builder.append(", regBy=");
		builder.append(regBy);
		builder.append(", regChnId=");
		builder.append(regChnId);
		builder.append(", productCoverLevelId=");
		builder.append(productCoverLevelId);
		builder.append("]");
		return builder.toString();
	}

}
