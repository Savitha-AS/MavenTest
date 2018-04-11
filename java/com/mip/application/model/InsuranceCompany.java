package com.mip.application.model;

import java.util.Date;

import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>InsuranceCompany.java</code> contains MO of Insurance Company module.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class InsuranceCompany extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8793465685017748588L;

	private int insCompId;
	private String insCompName;
	private String insCompPhone;
	private String insCompBranchName;
	private String insCompAddrs1;
	private String insCompAddrs2;
	private String insCompCity;
	private String insCompState;
	private String insCompCountry;
	private int insCompZip;
	private String insCompPocName;
	private String insCompPocMsisdn;
	private UserDetails createdBy;
	private Date createdDate;
	private String createdDateStr;		// for display in UI.
	private UserDetails modifiedBy;
	private Date modifiedDate;
	private String modifiedDateStr;		// for display in UI.

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InsuranceCompany [insCompId=");
		builder.append(insCompId);
		builder.append(", insCompName=");
		builder.append(insCompName);
		builder.append(", insCompPhone=");
		builder.append(insCompPhone);
		builder.append(", insCompBranchName=");
		builder.append(insCompBranchName);
		builder.append(", insCompAddrs1=");
		builder.append(insCompAddrs1);
		builder.append(", insCompAddrs2=");
		builder.append(insCompAddrs2);
		builder.append(", insCompCity=");
		builder.append(insCompCity);
		builder.append(", insCompState=");
		builder.append(insCompState);
		builder.append(", insCompCountry=");
		builder.append(insCompCountry);
		builder.append(", insCompZip=");
		builder.append(insCompZip);
		builder.append(", insCompPocName=");
		builder.append(insCompPocName);
		builder.append(", insCompPocMsisdn=");
		builder.append(insCompPocMsisdn);
		// builder.append(", createdBy=");
		// builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", createdDateStr=");
		builder.append(createdDateStr);
		// builder.append(", modifiedBy=");
		// builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", modifiedDateStr=");
		builder.append(modifiedDateStr);
		builder.append("]");
		return builder.toString();
	}

	public int getInsCompId() {
		return insCompId;
	}

	public void setInsCompId(int insCompId) {
		this.insCompId = insCompId;
	}

	public String getInsCompName() {
		return insCompName;
	}

	public void setInsCompName(String insCompName) {
		this.insCompName = insCompName;
	}

	public String getInsCompPhone() {
		return insCompPhone;
	}

	public void setInsCompPhone(String insCompPhone) {
		this.insCompPhone = insCompPhone;
	}

	public String getInsCompBranchName() {
		return insCompBranchName;
	}

	public void setInsCompBranchName(String insCompBranchName) {
		this.insCompBranchName = insCompBranchName;
	}

	public String getInsCompAddrs1() {
		return insCompAddrs1;
	}

	public void setInsCompAddrs1(String insCompAddrs1) {
		this.insCompAddrs1 = insCompAddrs1;
	}

	public String getInsCompAddrs2() {
		return insCompAddrs2;
	}

	public void setInsCompAddrs2(String insCompAddrs2) {
		this.insCompAddrs2 = insCompAddrs2;
	}

	public String getInsCompCity() {
		return insCompCity;
	}

	public void setInsCompCity(String insCompCity) {
		this.insCompCity = insCompCity;
	}

	public String getInsCompState() {
		return insCompState;
	}

	public void setInsCompState(String insCompState) {
		this.insCompState = insCompState;
	}

	public String getInsCompCountry() {
		return insCompCountry;
	}

	public void setInsCompCountry(String insCompCountry) {
		this.insCompCountry = insCompCountry;
	}

	public int getInsCompZip() {
		return insCompZip;
	}

	public void setInsCompZip(int insCompZip) {
		this.insCompZip = insCompZip;
	}

	public String getInsCompPocName() {
		return insCompPocName;
	}

	public void setInsCompPocName(String insCompPocName) {
		this.insCompPocName = insCompPocName;
	}

	public String getInsCompPocMsisdn() {
		return insCompPocMsisdn;
	}

	public void setInsCompPocMsisdn(String insCompPocMsisdn) {
		this.insCompPocMsisdn = insCompPocMsisdn;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
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
		
		// Generate a String format of the Created Date, for display in UI.
		setModifiedDateStr(DateUtil.toDateTimeMeridianString(this.modifiedDate));
	}

	public String getCreatedDateStr() {
		return createdDateStr;
	}

	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	public String getModifiedDateStr() {
		return modifiedDateStr;
	}

	public void setModifiedDateStr(String modifiedDateStr) {
		this.modifiedDateStr = modifiedDateStr;
	}
}
