package com.mip.application.model;

import java.util.Date;

public class ClaimDetails extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 3299823234612987442L;

	private int claimId;
	private String fname;
	private String sname;
	private String msisdn;
	private Date dob;
	private int age;
	private String gender;
	private String relation;
	private String insRelFname;
	private String insRelSurname;
	private Date insRelIrDoB;
	private int insRelAge;
	private String claimedPerson;
	private Date registeredDate;
	private UserDetails claimedBy;
	private Date claimedDate;
	// For IP Product Beneficiary Details
	private String ipNomFirstName;
	private String ipNomSurName;
	private String ipNomAge;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClaimDetails [claimId=");
		builder.append(claimId);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", age=");
		builder.append(age);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", relation=");
		builder.append(relation);
		builder.append(", insRelFname=");
		builder.append(insRelFname);
		builder.append(", insRelSurname=");
		builder.append(insRelSurname);
		builder.append(", insRelIrDoB=");
		builder.append(insRelIrDoB);
		builder.append(", insRelAge=");
		builder.append(insRelAge);
		builder.append(", claimedPerson=");
		builder.append(claimedPerson);
		builder.append(", registeredDate=");
		builder.append(registeredDate);
		builder.append(", claimedBy=");
		builder.append(claimedBy);
		builder.append(", claimedDate=");
		builder.append(claimedDate);
		builder.append(", ipNomFirstName=");
		builder.append(ipNomFirstName);
		builder.append(", ipNomSurName=");
		builder.append(ipNomSurName);
		builder.append(", ipNomAge=");
		builder.append(ipNomAge);
		builder.append("]");
		return builder.toString();
	}

	public String getIpNomFirstName() {
		return ipNomFirstName;
	}

	public void setIpNomFirstName(String ipNomFirstName) {
		this.ipNomFirstName = ipNomFirstName;
	}

	public String getIpNomSurName() {
		return ipNomSurName;
	}

	public void setIpNomSurName(String ipNomSurName) {
		this.ipNomSurName = ipNomSurName;
	}

	public String getIpNomAge() {
		return ipNomAge;
	}

	public void setIpNomAge(String ipNomAge) {
		this.ipNomAge = ipNomAge;
	}

	public int getClaimId() {
		return claimId;
	}

	public void setClaimId(int claimId) {
		this.claimId = claimId;
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

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getInsRelFname() {
		return insRelFname;
	}

	public void setInsRelFname(String insRelFname) {
		this.insRelFname = insRelFname;
	}

	public String getInsRelSurname() {
		return insRelSurname;
	}

	public void setInsRelSurname(String insRelSurname) {
		this.insRelSurname = insRelSurname;
	}

	public Date getInsRelIrDoB() {
		return insRelIrDoB;
	}

	public void setInsRelIrDoB(Date insRelIrDoB) {
		this.insRelIrDoB = insRelIrDoB;
	}

	public int getInsRelAge() {
		return insRelAge;
	}

	public void setInsRelAge(int insRelAge) {
		this.insRelAge = insRelAge;
	}

	public String getClaimedPerson() {
		return claimedPerson;
	}

	public void setClaimedPerson(String claimedPerson) {
		this.claimedPerson = claimedPerson;
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public UserDetails getClaimedBy() {
		return claimedBy;
	}

	public void setClaimedBy(UserDetails claimedBy) {
		this.claimedBy = claimedBy;
	}

	public Date getClaimedDate() {
		return claimedDate;
	}

	public void setClaimedDate(Date claimedDate) {
		this.claimedDate = claimedDate;
	}
}