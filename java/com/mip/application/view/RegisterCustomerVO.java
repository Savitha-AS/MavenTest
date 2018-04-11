package com.mip.application.view;

import java.io.Serializable;

public class RegisterCustomerVO  implements Serializable{

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6396395108419591643L;
	private String fname;	
	private String sname;
	private String msisdn;
	private String dob;
	private String age;
	private String gender;
	private String insRelation;
	private String insRelFname;
	private String insRelSurname;
	private String insRelIrDoB;
	private String insRelAge;
	
	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}
	/**
	 * @return the sname
	 */
	public String getSname() {
		return sname;
	}
	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @return the insRelation
	 */
	public String getInsRelation() {
		return insRelation;
	}
	/**
	 * @return the insRelFname
	 */
	public String getInsRelFname() {
		return insRelFname;
	}
	/**
	 * @return the insRelSurname
	 */
	public String getInsRelSurname() {
		return insRelSurname;
	}
	/**
	 * @return the insRelIrDoB
	 */
	public String getInsRelIrDoB() {
		return insRelIrDoB;
	}
	/**
	 * @return the insRelAge
	 */
	public String getInsRelAge() {
		return insRelAge;
	}
	/**
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}
	/**
	 * @param sname the sname to set
	 */
	public void setSname(String sname) {
		this.sname = sname;
	}
	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @param insRelation the insRelation to set
	 */
	public void setInsRelation(String insRelation) {
		this.insRelation = insRelation;
	}
	/**
	 * @param insRelFname the insRelFname to set
	 */
	public void setInsRelFname(String insRelFname) {
		this.insRelFname = insRelFname;
	}
	/**
	 * @param insRelSurname the insRelSurname to set
	 */
	public void setInsRelSurname(String insRelSurname) {
		this.insRelSurname = insRelSurname;
	}
	/**
	 * @param insRelIrDoB the insRelIrDoB to set
	 */
	public void setInsRelIrDoB(String insRelIrDoB) {
		this.insRelIrDoB = insRelIrDoB;
	}
	/**
	 * @param insRelAge the insRelAge to set
	 */
	public void setInsRelAge(String insRelAge) {
		this.insRelAge = insRelAge;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterCustomerVO [fname=");
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
		builder.append(", insRelation=");
		builder.append(insRelation);
		builder.append(", insRelFname=");
		builder.append(insRelFname);
		builder.append(", insRelSurname=");
		builder.append(insRelSurname);
		builder.append(", insRelIrDoB=");
		builder.append(insRelIrDoB);
		builder.append(", insRelAge=");
		builder.append(insRelAge);
		builder.append("]");
		return builder.toString();
	}
	
}
