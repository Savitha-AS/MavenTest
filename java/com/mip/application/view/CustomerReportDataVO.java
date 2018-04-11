package com.mip.application.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.mip.application.model.UserDetails;

public class CustomerReportDataVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2252097351562418095L;
	
	private String custName;	
	private String custAge;
	private String irdName;
	private String irdAge;
	private String custRelationship;
	private String msisdn;
	private int custId;
	private String status;
	private UserDetails createdBy;
	private String createdByAll;
	private Date createdDate;
	private String createdDateAll;
	private String confDateAll;
	private UserDetails modifiedBy;
	private Date modifiedDate;
	private String productName;
	private String productNameAll;
	private BigDecimal earnedCover;
	private String earnedCoverAll;
	private BigDecimal coverCharges;
	private String coverChargesAll;
	private BigDecimal totalCover;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportCustomerVO [custName=");
		builder.append(custName);
		builder.append(", custAge=");
		builder.append(custAge);
		builder.append(", irdName=");
		builder.append(irdName);
		builder.append(", irdAge=");
		builder.append(irdAge);
		builder.append(", custRelationship=");
		builder.append(custRelationship);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", custId=");
		builder.append(custId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdByAll=");
		builder.append(createdByAll);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", createdDateAll=");
		builder.append(createdDateAll);
		builder.append(", confDateAll=");
		builder.append(confDateAll);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", productName=");
		builder.append(productName);
		builder.append(", productNameAll=");
		builder.append(productNameAll);
		builder.append(", earnedCover=");
		builder.append(earnedCover);
		builder.append(", earnedCoverAll=");
		builder.append(earnedCoverAll);
		builder.append(", coverCharges=");
		builder.append(coverCharges);
		builder.append(", coverChargesAll=");
		builder.append(coverChargesAll);
		builder.append(", totalCover=");
		builder.append(totalCover);
		builder.append("]");
		return builder.toString();
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAge() {
		return custAge;
	}

	public void setCustAge(String custAge) {
		this.custAge = custAge;
	}

	public String getIrdName() {
		return irdName;
	}

	public void setIrdName(String irdName) {
		this.irdName = irdName;
	}

	public String getIrdAge() {
		return irdAge;
	}

	public void setIrdAge(String irdAge) {
		this.irdAge = irdAge;
	}

	public String getCustRelationship() {
		return custRelationship;
	}

	public void setCustRelationship(String custRelationship) {
		this.custRelationship = custRelationship;
	}

	public String getMsisdn() {
		return msisdn;
	}
	
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	
	public int getCustId() {
		return custId;
	}
	
	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}
		
	public String getCreatedByAll() {
		return createdByAll;
	}
	
	public void setCreatedByAll(String createdByAll) {
		this.createdByAll = createdByAll;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getCreatedDateAll() {
		return createdDateAll;
	}
	
	public void setCreatedDateAll(String createdDateAll) {
		this.createdDateAll = createdDateAll;
	}
	
	public String getConfDateAll() {
		return confDateAll;
	}
	
	public void setConfDateAll(String confDateAll) {
		this.confDateAll = confDateAll;
	}
	
	public UserDetails getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(UserDetails modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductNameAll() {
		return productNameAll;
	}
	
	public void setProductNameAll(String productNameAll) {
		this.productNameAll = productNameAll;
	}
	
	public BigDecimal getEarnedCover() {
		return earnedCover;
	}
	
	public void setEarnedCover(BigDecimal earnedCover) {
		this.earnedCover = earnedCover;
	}
	
	public String getEarnedCoverAll() {
		return earnedCoverAll;
	}
	
	public void setEarnedCoverAll(String earnedCoverAll) {
		this.earnedCoverAll = earnedCoverAll;
	}
	
	public BigDecimal getCoverCharges() {
		return coverCharges;
	}
	
	public void setCoverCharges(BigDecimal coverCharges) {
		this.coverCharges = coverCharges;
	}

	public String getCoverChargesAll() {
		return coverChargesAll;
	}
	
	public void setCoverChargesAll(String coverChargesAll) {
		this.coverChargesAll = coverChargesAll;
	}
	
	public BigDecimal getTotalCover() {
		return totalCover;
	}

	public void setTotalCover(BigDecimal totalCover) {
		this.totalCover = totalCover;
	}

}
