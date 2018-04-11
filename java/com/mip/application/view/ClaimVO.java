package com.mip.application.view;

public class ClaimVO implements java.io.Serializable {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 860222787126637387L;

	private int custId;
	private String fname;
	private String sname;
	private String msisdn;
	private String dob;
	private String age;
	private String gender;
	private String relation;
	private String insRelFname;
	private String insRelSurname;
	private String insRelIrDoB;
	private String insRelAge;
	private String claimedPerson;
	//For IP Product Beneficiary Details
	private String ipNomFirstName;
	private String ipNomSurName;
	private String ipNomAge;
	private String insMsisdn;
	private String ipInsMsisdn;
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClaimVO [custId=");
		builder.append(custId);
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
		builder.append(", ipNomFirstName=");
		builder.append(ipNomFirstName);
		builder.append(", ipNomSurName=");
		builder.append(ipNomSurName);
		builder.append(", ipNomAge=");
		builder.append(ipNomAge);
		builder.append(", insMsisdn=");
		builder.append(insMsisdn);
		builder.append(", ipInsMsisdn=");
		builder.append(ipInsMsisdn);
		builder.append("]");
		return builder.toString();
	}
	public String getInsMsisdn() {
		return insMsisdn;
	}
	public void setInsMsisdn(String insMsisdn) {
		this.insMsisdn = insMsisdn;
	}
	public String getIpInsMsisdn() {
		return ipInsMsisdn;
	}
	public void setIpInsMsisdn(String ipInsMsisdn) {
		this.ipInsMsisdn = ipInsMsisdn;
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
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
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
	public String getInsRelIrDoB() {
		return insRelIrDoB;
	}
	public void setInsRelIrDoB(String insRelIrDoB) {
		this.insRelIrDoB = insRelIrDoB;
	}
	public String getInsRelAge() {
		return insRelAge;
	}
	public void setInsRelAge(String insRelAge) {
		this.insRelAge = insRelAge;
	}
	public String getClaimedPerson() {
		return claimedPerson;
	}
	public void setClaimedPerson(String claimedPerson) {
		this.claimedPerson = claimedPerson;
	}
}
