package com.mip.application.model;

public class RegisterCustomerRequest {

	// customer Details
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private int age;

	private String gender;
	private int isConfirmed;
	private String msisdn;
	private int dedcutionType;

	// customer insured relative Details for XL
	private String insRelFirstName;
	private String insRelLastName;
	private String insRelDateOfBirth;
	private int insRelAge;
	private String insRelRelationship;
	private String insRelMsisdn;

	// customer insured relative Details for IP
	private String ipNomFirstName;
	private String ipNomSurName;
	private int ipNomAge;
	private String ipNomMsisdn;
	private int productCoverId;

	// registered channel & registered by
	private int registrationChannelId;
	private int registeredBy;

	// products registered by customer
	private String productsSelected;

	
	
	public String getInsRelMsisdn() {
		return insRelMsisdn;
	}

	public void setInsRelMsisdn(String insRelMsisdn) {
		this.insRelMsisdn = insRelMsisdn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public int getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(int isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public int getDedcutionType() {
		return dedcutionType;
	}

	public void setDedcutionType(int dedcutionType) {
		this.dedcutionType = dedcutionType;
	}

	public String getInsRelFirstName() {
		return insRelFirstName;
	}

	public void setInsRelFirstName(String insRelFirstName) {
		this.insRelFirstName = insRelFirstName;
	}

	public String getInsRelLastName() {
		return insRelLastName;
	}

	public void setInsRelLastName(String insRelLastName) {
		this.insRelLastName = insRelLastName;
	}

	public String getInsRelDateOfBirth() {
		return insRelDateOfBirth;
	}

	public void setInsRelDateOfBirth(String insRelDateOfBirth) {
		this.insRelDateOfBirth = insRelDateOfBirth;
	}

	public int getInsRelAge() {
		return insRelAge;
	}

	public void setInsRelAge(int insRelAge) {
		this.insRelAge = insRelAge;
	}

	public String getInsRelRelationship() {
		return insRelRelationship;
	}

	public void setInsRelRelationship(String insRelRelationship) {
		this.insRelRelationship = insRelRelationship;
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

	public int getIpNomAge() {
		return ipNomAge;
	}

	public void setIpNomAge(int ipNomAge) {
		this.ipNomAge = ipNomAge;
	}

	public int getProductCoverId() {
		return productCoverId;
	}

	public void setProductCoverId(int productCoverId) {
		this.productCoverId = productCoverId;
	}

	public int getRegistrationChannelId() {
		return registrationChannelId;
	}

	public void setRegistrationChannel(int registrationChannelId) {
		this.registrationChannelId = registrationChannelId;
	}

	public int getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(int registeredBy) {
		this.registeredBy = registeredBy;
	}

	public String getProductsSelected() {
		return productsSelected;
	}

	public void setProductsSelected(String productsSelected) {
		this.productsSelected = productsSelected;
	}
	
	

	

	public String getIpNomMsisdn() {
		return ipNomMsisdn;
	}

	public void setIpNomMsisdn(String ipNomMsisdn) {
		this.ipNomMsisdn = ipNomMsisdn;
	}

	public void setRegistrationChannelId(int registrationChannelId) {
		this.registrationChannelId = registrationChannelId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterCustomerRequest [firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", dateOfBirth=");
		builder.append(dateOfBirth);
		builder.append(", age=");
		builder.append(age);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", isConfirmed=");
		builder.append(isConfirmed);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", dedcutionType=");
		builder.append(dedcutionType);
		builder.append(", insRelFirstName=");
		builder.append(insRelFirstName);
		builder.append(", insRelLastName=");
		builder.append(insRelLastName);
		builder.append(", insRelDateOfBirth=");
		builder.append(insRelDateOfBirth);
		builder.append(", insRelAge=");
		builder.append(insRelAge);
		builder.append(", insRelRelationship=");
		builder.append(insRelRelationship);
		builder.append(", insRelMsisdn=");
		builder.append(insRelMsisdn);
		builder.append(", ipNomFirstName=");
		builder.append(ipNomFirstName);
		builder.append(", ipNomSurName=");
		builder.append(ipNomSurName);
		builder.append(", ipNomAge=");
		builder.append(ipNomAge);
		builder.append(", ipNomMsisdn=");
		builder.append(ipNomMsisdn);
		builder.append(", productCoverId=");
		builder.append(productCoverId);
		builder.append(", registrationChannelId=");
		builder.append(registrationChannelId);
		builder.append(", registeredBy=");
		builder.append(registeredBy);
		builder.append(", productsSelected=");
		builder.append(productsSelected);
		builder.append("]");
		return builder.toString();
	}

	

	

}
