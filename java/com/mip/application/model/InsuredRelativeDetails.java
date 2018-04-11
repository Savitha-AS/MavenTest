package com.mip.application.model;

import java.util.Date;

public class InsuredRelativeDetails extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8886940560244688749L;

	private int insId;
	private String fname;
	private String sname;
	private int age;
	private Date dob;
	//private CustomerDetails customerDetails;
	private String custRelationship;
	private int offerId;
	//private int custId;
	private String insMsisdn;




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InsuredRelativeDetails [insId=");
		builder.append(insId);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", age=");
		builder.append(age);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", custRelationship=");
		builder.append(custRelationship);
		builder.append(", offerId=");
		builder.append(offerId);
		builder.append(", insMsisdn=");
		builder.append(insMsisdn);
		builder.append("]");
		return builder.toString();
	}

	public String getInsMsisdn() {
		return insMsisdn;
	}

	public void setInsMsisdn(String insMsisdn) {
		this.insMsisdn = insMsisdn;
	}

	public int getInsId() {
		return insId;
	}

	public void setInsId(int insId) {
		this.insId = insId;
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
/*
    public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}*/

	public String getCustRelationship() {
		return custRelationship;
	}

	public void setCustRelationship(String custRelationship) {
		this.custRelationship = custRelationship;
	}

	public int getOfferId() {
		return offerId;
	}

	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}

	/*
	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}*/
}
