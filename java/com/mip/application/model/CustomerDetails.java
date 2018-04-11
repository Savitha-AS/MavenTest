package com.mip.application.model;

import java.util.Date;
import java.util.Set;

import com.mip.framework.utils.DateUtil;

public class CustomerDetails extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -1649440687276945665L;

	private int custId;
	private String fname;
	private String sname;
	private int age;
	private Date dob;
	private int impliedAge;
	private String gender;
	private String msisdn;
	private int deductionMode;
	private UserDetails modifiedBy;
	private Date modifiedDate;
	private String modifiedDateStr;		// for display in UI.
	private Set<InsuredRelativeDetails> insuredRelatives;

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerDetails [custId=");
		builder.append(custId);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", age=");
		builder.append(age);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", impliedAge=");
		builder.append(impliedAge);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", deductionMode=");
		builder.append(deductionMode);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", modifiedDateStr=");
		builder.append(modifiedDateStr);
		builder.append(", insuredRelatives=");
		builder.append(insuredRelatives);
		builder.append("]");
		return builder.toString();
	}

	public int getImpliedAge() {
		return impliedAge;
	}

	public void setImpliedAge(int impliedAge) {
		this.impliedAge = impliedAge;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

	public Set<InsuredRelativeDetails> getInsuredRelatives() {
		return insuredRelatives;
	}

	public void setInsuredRelatives(Set<InsuredRelativeDetails> insuredRelatives) {
		this.insuredRelatives = insuredRelatives;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
		
		// Generate a String format of the Created Date, for display in UI.
		setModifiedDateStr(DateUtil.toDateTimeMeridianString(this.modifiedDate));
	}

	public String getModifiedDateStr() {
		return modifiedDateStr;
	}

	public void setModifiedDateStr(String modifiedDateStr) {
		this.modifiedDateStr = modifiedDateStr;
	}

	public int getDeductionMode() {
		return deductionMode;
	}

	public void setDeductionMode(int deductionMode) {
		this.deductionMode = deductionMode;
	}
	
	

}
