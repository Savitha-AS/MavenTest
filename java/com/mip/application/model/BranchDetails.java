package com.mip.application.model;

import java.util.Date;

import com.mip.framework.utils.DateUtil;

public class BranchDetails extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7205018707042639745L;
	
	private int branchId;
	private String branchCode;
	private String branchName;
	private String branchStreet;
	private String branchRegion;
	private String branchCity;
	private byte active;
	private UserDetails createdBy;
	private Date createdDate;
	private String createdDateStr;		// for display in UI.
	private UserDetails modifiedBy;
	private Date modifiedDate;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BranchDetails [active=");
		builder.append(active);
		builder.append(", branchRegion=");
		builder.append(branchRegion);
		builder.append(", branchCity=");
		builder.append(branchCity);
		builder.append(", branchCode=");
		builder.append(branchCode);
		builder.append(", branchId=");
		builder.append(branchId);
		builder.append(", branchName=");
		builder.append(branchName);
		builder.append(", branchStreet=");
		builder.append(branchStreet);
		//builder.append(", createdBy=");
		//builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", createdDateStr=");
		builder.append(createdDateStr);
		//builder.append(", modifiedBy=");
		//builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append("]");
		return builder.toString();
	}

	public int getBranchId() {
		return this.branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchStreet() {
		return this.branchStreet;
	}

	public void setBranchStreet(String branchStreet) {
		this.branchStreet = branchStreet;
	}

	public String getBranchRegion() {
		return this.branchRegion;
	}

	public void setBranchRegion(String branchRegion) {
		this.branchRegion = branchRegion;
	}

	public String getBranchCity() {
		return this.branchCity;
	}

	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public UserDetails getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		
		// Generate a String format of the Created Date, for display in UI.
		setCreatedDateStr(DateUtil.toDateTimeMeridianString(this.createdDate));
	}

	public UserDetails getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(UserDetails modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedDateStr() {
		return createdDateStr;
	}

	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}
}
