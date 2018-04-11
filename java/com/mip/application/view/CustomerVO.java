package com.mip.application.view;

import java.io.Serializable;

public class CustomerVO implements Serializable,Cloneable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2529624154468301491L;

	private String custId;
	private String fname;	
	private String sname;	
	private String msisdn;	
	private String dob;
	private String regBy;
	private String regDate;
	private String productIds;
	private String age;	
	private String gender;	
	private String insRelation;	
	private String insRelFname;	
	private String insRelSurname;	
	private String insRelIrDoB;	
	private String insRelAge;	
	private String status;
	private String modifiedBy;	
	private String modifiedDate;
	private String modifiedDateStr;		// for display in UI.
	private String createdBy;	
	private String createdDate;
	private String createdDateStr;		// for display in UI.
	private String name;
	private Byte confirmed;
	private String confirmedDate;
	private String regCommunicationChannel;
	private String insId;
	private String purchasedProducts;
	private String productId;
	private String deRegisterOption;
	private String deRegisterEffectiveFrom;
	private String deductionMode;
	private String confDate;
	private String prevMonthUsage;
	private String coverFree;
	private String chargesXL;
	private String coverXL;
	private String chargesHP;
	private String coverHP;
	private String month;
	private String year;
	private String confirmation;
	private String freeAndxlCover;
	private String offerId;
	private String custName;
	private String regProductLevel;
	private String earnedCover;
	private String deductedOfferAmount;
	private String isConfirmed;
	
	private String deRegBy;
	private String deRegDate;
	private String dateOfCustomerRemoval;
	
	private String XLConfirmed;
	private String XLRegBy;
	private String XLRegDate;
	private String XLConfDate;
	private String XLOfferId;
	
	private String HPConfirmed;
	private String HPRegBy;
	private String HPRegDate;
	private String HPConfDate;
	private String HPOfferId;
	
	private String productCoverIdIP;
	private String IPConfirmed;
	
	private String IPConfDate;
	private String IPRegBy;
	private String IPRegDate;
	private String IPOfferId;
	private String productCover;
	private String coverIP;
	private String chargesIP;
	
	
	//For IP Product Beneficiary Details
	private String ipNomFirstName;
	private String ipNomSurName;
	private String ipNomAge;

	private String insOfferId;
	/*private String DeregistrationDate;*/
	private String impliedAge;
	private String insMsisdn;
	private String ipInsMsisdn;

	private String deactivatedProducts;
	private String productRegistered;
	private String productActiveRegister;
	private String isReactive;
	private String isCustExist;

	
	private String XLDeregisteredDate;
	private String HPDeregisteredDate;
	private String IPDeregisteredDate;
	private String custProductCancellation;
	
	private String deRegisteredDate;
	
	
	private Boolean isActive;
	private Byte deactivated;
	private String OfferName;
	private String pakageHP;
	private String pakageXL;
	
	private Integer isData;
	private Integer lcId;
	private Integer snId;
		
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
		} 
	

	public String getCustProductCancellation() {
		return custProductCancellation;
	}

	public void setCustProductCancellation(String custProductCancellation) {
		this.custProductCancellation = custProductCancellation;
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
		if(dob == null)
			dob = "";
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

	public String getInsRelation() {
		return insRelation;
	}

	public void setInsRelation(String insRelation) {
		this.insRelation = insRelation;
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
		if (insRelIrDoB == null)
			insRelIrDoB = "";
		this.insRelIrDoB = insRelIrDoB;
	}

	public String getInsRelAge() {
		return insRelAge;
	}

	public void setInsRelAge(String insRelAge) {
		this.insRelAge = insRelAge;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getModifiedDateStr() {
		return modifiedDateStr;
	}

	public void setModifiedDateStr(String modifiedDateStr) {
		this.modifiedDateStr = modifiedDateStr;
	}

	public String getName() {
		return getFname()+" "+getSname();
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public Byte getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Byte confirmed) {
		this.confirmed = confirmed;
	}

	public String getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public String getRegCommunicationChannel() {
		return regCommunicationChannel;
	}

	public void setRegCommunicationChannel(String regCommunicationChannel) {
		this.regCommunicationChannel = regCommunicationChannel;
	}

	public String getInsId() {
		return insId;
	}

	public void setInsId(String insId) {
		this.insId = insId;
	}

	public String getPurchasedProducts() {
		return purchasedProducts;
	}

	public void setPurchasedProducts(String purchasedProducts) {
		this.purchasedProducts = purchasedProducts;
	}

	/**
	 * @return the deRegisterOption
	 */
	public String getDeRegisterOption() {
		return deRegisterOption;
	}

	/**
	 * @param deRegisterOption
	 *            the deRegisterOption to set
	 */
	public void setDeRegisterOption(String deRegisterOption) {
		this.deRegisterOption = deRegisterOption;
	}

	/**
	 * @return the deRegisterEffectiveFrom
	 */
	public String getDeRegisterEffectiveFrom() {
		return deRegisterEffectiveFrom;
	}

	/**
	 * @param deRegisterEffectiveFrom
	 *            the deRegisterEffectiveFrom to set
	 */
	public void setDeRegisterEffectiveFrom(String deRegisterEffectiveFrom) {
		this.deRegisterEffectiveFrom = deRegisterEffectiveFrom;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedDateStr() {
		return createdDateStr;
	}

	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	public String getDeductionMode() {
		return deductionMode;
	}

	public void setDeductionMode(String deductionMode) {
		this.deductionMode = deductionMode;
	}

	public String getPrevMonthUsage() {
		return prevMonthUsage;
	}

	public void setPrevMonthUsage(String prevMonthUsage) {
		this.prevMonthUsage = prevMonthUsage;
	}

	public String getCoverFree() {
		return coverFree;
	}

	public void setCoverFree(String coverFree) {
		this.coverFree = coverFree;
	}

	public String getChargesXL() {
		return chargesXL;
	}

	public void setChargesXL(String chargesXL) {
		this.chargesXL = chargesXL;
	}

	public String getCoverXL() {
		return coverXL;
	}

	public void setCoverXL(String coverXL) {
		this.coverXL = coverXL;
	}

	public String getChargesHP() {
		return chargesHP;
	}

	public void setChargesHP(String chargesHP) {
		this.chargesHP = chargesHP;
	}

	public String getCoverHP() {
		return coverHP;
	}

	public void setCoverHP(String coverHP) {
		this.coverHP = coverHP;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getRegProductLevel() {
		return regProductLevel;
	}

	public void setRegProductLevel(String regProductLevel) {
		this.regProductLevel = regProductLevel;
	}

	public String getEarnedCover() {
		return earnedCover;
	}

	public void setEarnedCover(String earnedCover) {
		this.earnedCover = earnedCover;
	}

	public String getDeRegBy() {
		return deRegBy;
	}

	public void setDeRegBy(String deRegBy) {
		this.deRegBy = deRegBy;
	}

	public String getDeRegDate() {
		return deRegDate;
	}

	public void setDeRegDate(String deRegDate) {
		this.deRegDate = deRegDate;
	}

	public String getDateOfCustomerRemoval() {
		return dateOfCustomerRemoval;
	}

	public void setDateOfCustomerRemoval(String dateOfCustomerRemoval) {
		this.dateOfCustomerRemoval = dateOfCustomerRemoval;
	}

	public String getDeductedOfferAmount() {
		return deductedOfferAmount;
	}

	public void setDeductedOfferAmount(String deductedOfferAmount) {
		this.deductedOfferAmount = deductedOfferAmount;
	}

	public String getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(String isConfirmed) {
		this.isConfirmed = isConfirmed;
	}
	
	public String getFreeAndxlCover() {
		return freeAndxlCover;
	}

	public void setFreeAndxlCover(String freeAndxlCover) {
		this.freeAndxlCover = freeAndxlCover;
	}

	public String getRegBy() {
		return regBy;
	}

	public void setRegBy(String regBy) {
		this.regBy = regBy;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	public String getXLConfirmed() {
		return XLConfirmed;
	}

	public void setXLConfirmed(String xLConfirmed) {
		XLConfirmed = xLConfirmed;
	}

	public String getXLRegBy() {
		return XLRegBy;
	}

	public void setXLRegBy(String xLRegBy) {
		XLRegBy = xLRegBy;
	}

	public String getXLRegDate() {
		return XLRegDate;
	}

	public void setXLRegDate(String xLRegDate) {
		XLRegDate = xLRegDate;
	}

	public String getXLOfferId() {
		return XLOfferId;
	}

	public void setXLOfferId(String xLOfferId) {
		XLOfferId = xLOfferId;
	}

	public String getHPConfirmed() {
		return HPConfirmed;
	}

	public void setHPConfirmed(String hPConfirmed) {
		HPConfirmed = hPConfirmed;
	}

	public String getHPRegBy() {
		return HPRegBy;
	}

	public void setHPRegBy(String hPRegBy) {
		HPRegBy = hPRegBy;
	}

	public String getHPRegDate() {
		return HPRegDate;
	}

	public void setHPRegDate(String hPRegDate) {
		HPRegDate = hPRegDate;
	}

	public String getHPOfferId() {
		return HPOfferId;
	}

	public void setHPOfferId(String hPOfferId) {
		HPOfferId = hPOfferId;
	}

	public String getConfDate() {
		return confDate;
	}

	public void setConfDate(String confDate) {
		this.confDate = confDate;
	}

	public String getXLConfDate() {
		return XLConfDate;
	}

	public void setXLConfDate(String xLConfDate) {
		XLConfDate = xLConfDate;
	}

	public String getHPConfDate() {
		return HPConfDate;
	}

	public void setHPConfDate(String hPConfDate) {
		HPConfDate = hPConfDate;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getProductCoverIdIP() {
		return productCoverIdIP;
	}

	public void setProductCoverIdIP(String productCoverIdIP) {
		this.productCoverIdIP = productCoverIdIP;
	}
	public String getIPConfirmed() {
		return IPConfirmed;
	}

	public void setIPConfirmed(String iPConfirmed) {
		IPConfirmed = iPConfirmed;
	}

	public String getIPConfDate() {
		return IPConfDate;
	}

	public void setIPConfDate(String iPConfDate) {
		IPConfDate = iPConfDate;
	}

	public String getIPRegBy() {
		return IPRegBy;
	}

	public void setIPRegBy(String iPRegBy) {
		IPRegBy = iPRegBy;
	}

	public String getIPRegDate() {
		return IPRegDate;
	}

	public void setIPRegDate(String iPRegDate) {
		IPRegDate = iPRegDate;
	}

	public String getIPOfferId() {
		return IPOfferId;
	}

	public void setIPOfferId(String iPOfferId) {
		IPOfferId = iPOfferId;
	}

	public String getProductCover() {
		return productCover;
	}

	public void setProductCover(String productCover) {
		this.productCover = productCover;
	}

	public String getCoverIP() {
		return coverIP;
	}

	public void setCoverIP(String coverIP) {
		this.coverIP = coverIP;
	}

	public String getChargesIP() {
		return chargesIP;
	}

	public void setChargesIP(String chargesIP) {
		this.chargesIP = chargesIP;
	}

	
	public String getInsOfferId() {
		return insOfferId;
	}

	public void setInsOfferId(String insOfferId) {
		this.insOfferId = insOfferId;
	}
/*
	public String getDeregistrationDate() {
		return DeregistrationDate;
	}

	public void setDeregistrationDate(String deregistrationDate) {
		DeregistrationDate = deregistrationDate;
	}
*/

	public String getXLDeregisteredDate() {
		return XLDeregisteredDate;
	}

	public void setXLDeregisteredDate(String xLDeregisteredDate) {
		XLDeregisteredDate = xLDeregisteredDate;
	}

	public String getHPDeregisteredDate() {
		return HPDeregisteredDate;
	}

	public void setHPDeregisteredDate(String hPDeregisteredDate) {
		HPDeregisteredDate = hPDeregisteredDate;
	}

	public String getIPDeregisteredDate() {
		return IPDeregisteredDate;
	}

	public void setIPDeregisteredDate(String iPDeregisteredDate) {
		IPDeregisteredDate = iPDeregisteredDate;
	}
/*
	public String getXLDeregBy() {
		return XLDeregBy;
	}

	public void setXLDeregBy(String xLDeregBy) {
		XLDeregBy = xLDeregBy;
	}

	public String getHPDeregBy() {
		return HPDeregBy;
	}

	public void setHPDeregBy(String hPDeregBy) {
		HPDeregBy = hPDeregBy;
	}

	public String getIPDeregBy() {
		return IPDeregBy;
	}

	public void setIPDeregBy(String iPDeregBy) {
		IPDeregBy = iPDeregBy;
	}
*/

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public String getImpliedAge() {
		return impliedAge;
	}


	public void setImpliedAge(String impliedAge) {
		this.impliedAge = impliedAge;
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


	public String getDeactivatedProducts() {
		return deactivatedProducts;
	}


	public void setDeactivatedProducts(String deactivatedProducts) {
		this.deactivatedProducts = deactivatedProducts;
	}


	public String getProductRegistered() {
		return productRegistered;
	}


	public void setProductRegistered(String productRegistered) {
		this.productRegistered = productRegistered;
	}


	public String getProductActiveRegister() {
		return productActiveRegister;
	}


	public void setProductActiveRegister(String productActiveRegister) {
		this.productActiveRegister = productActiveRegister;
	}


	public String getIsReactive() {
		return isReactive;
	}

	public void setIsReactive(String isReactive) {
		this.isReactive = isReactive;
	}

	public String getIsCustExist() {
		return isCustExist;
	}

	public void setIsCustExist(String isCustExist) {
		this.isCustExist = isCustExist;
	}


	public Integer getIsData() {
		return isData;
	}


	public void setIsData(Integer isData) {
		this.isData = isData;
	}


	public Integer getLcId() {
		return lcId;
	}


	public void setLcId(Integer lcId) {
		this.lcId = lcId;
	}

	

	public Integer getSnId() {
		return snId;
	}


	public void setSnId(Integer snId) {
		this.snId = snId;
	}

	
	

	public Byte getDeactivated() {
		return deactivated;
	}


	public void setDeactivated(Byte deactivated) {
		this.deactivated = deactivated;
	}


	public String getOfferName() {
		return OfferName;
	}


	public void setOfferName(String offerName) {
		OfferName = offerName;
	}
	
	

	public String getDeRegisteredDate() {
		return deRegisteredDate;
	}


	public void setDeRegisteredDate(String deRegisteredDate) {
		this.deRegisteredDate = deRegisteredDate;
	}




	public String getPakageHP() {
		return pakageHP;
	}


	public void setPakageHP(String pakageHP) {
		this.pakageHP = pakageHP;
	}


	public String getPakageXL() {
		return pakageXL;
	}


	public void setPakageXL(String pakageXL) {
		this.pakageXL = pakageXL;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerVO [custId=");
		builder.append(custId);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", regBy=");
		builder.append(regBy);
		builder.append(", regDate=");
		builder.append(regDate);
		builder.append(", productIds=");
		builder.append(productIds);
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
		builder.append(", status=");
		builder.append(status);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", modifiedDateStr=");
		builder.append(modifiedDateStr);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", createdDateStr=");
		builder.append(createdDateStr);
		builder.append(", name=");
		builder.append(name);
		builder.append(", confirmed=");
		builder.append(confirmed);
		builder.append(", confirmedDate=");
		builder.append(confirmedDate);
		builder.append(", regCommunicationChannel=");
		builder.append(regCommunicationChannel);
		builder.append(", insId=");
		builder.append(insId);
		builder.append(", purchasedProducts=");
		builder.append(purchasedProducts);
		builder.append(", productId=");
		builder.append(productId);
		builder.append(", deRegisterOption=");
		builder.append(deRegisterOption);
		builder.append(", deRegisterEffectiveFrom=");
		builder.append(deRegisterEffectiveFrom);
		builder.append(", deductionMode=");
		builder.append(deductionMode);
		builder.append(", confDate=");
		builder.append(confDate);
		builder.append(", prevMonthUsage=");
		builder.append(prevMonthUsage);
		builder.append(", coverFree=");
		builder.append(coverFree);
		builder.append(", chargesXL=");
		builder.append(chargesXL);
		builder.append(", coverXL=");
		builder.append(coverXL);
		builder.append(", chargesHP=");
		builder.append(chargesHP);
		builder.append(", coverHP=");
		builder.append(coverHP);
		builder.append(", month=");
		builder.append(month);
		builder.append(", year=");
		builder.append(year);
		builder.append(", confirmation=");
		builder.append(confirmation);
		builder.append(", freeAndxlCover=");
		builder.append(freeAndxlCover);
		builder.append(", offerId=");
		builder.append(offerId);
		builder.append(", custName=");
		builder.append(custName);
		builder.append(", regProductLevel=");
		builder.append(regProductLevel);
		builder.append(", earnedCover=");
		builder.append(earnedCover);
		builder.append(", deductedOfferAmount=");
		builder.append(deductedOfferAmount);
		builder.append(", isConfirmed=");
		builder.append(isConfirmed);
		builder.append(", deRegBy=");
		builder.append(deRegBy);
		builder.append(", deRegDate=");
		builder.append(deRegDate);
		builder.append(", dateOfCustomerRemoval=");
		builder.append(dateOfCustomerRemoval);
		builder.append(", XLConfirmed=");
		builder.append(XLConfirmed);
		builder.append(", XLRegBy=");
		builder.append(XLRegBy);
		builder.append(", XLRegDate=");
		builder.append(XLRegDate);
		builder.append(", XLConfDate=");
		builder.append(XLConfDate);
		builder.append(", XLOfferId=");
		builder.append(XLOfferId);
		builder.append(", HPConfirmed=");
		builder.append(HPConfirmed);
		builder.append(", HPRegBy=");
		builder.append(HPRegBy);
		builder.append(", HPRegDate=");
		builder.append(HPRegDate);
		builder.append(", HPConfDate=");
		builder.append(HPConfDate);
		builder.append(", HPOfferId=");
		builder.append(HPOfferId);
		builder.append(", productCoverIdIP=");
		builder.append(productCoverIdIP);
		builder.append(", IPConfirmed=");
		builder.append(IPConfirmed);
		builder.append(", IPConfDate=");
		builder.append(IPConfDate);
		builder.append(", IPRegBy=");
		builder.append(IPRegBy);
		builder.append(", IPRegDate=");
		builder.append(IPRegDate);
		builder.append(", IPOfferId=");
		builder.append(IPOfferId);
		builder.append(", productCover=");
		builder.append(productCover);
		builder.append(", coverIP=");
		builder.append(coverIP);
		builder.append(", chargesIP=");
		builder.append(chargesIP);
		builder.append(", ipNomFirstName=");
		builder.append(ipNomFirstName);
		builder.append(", ipNomSurName=");
		builder.append(ipNomSurName);
		builder.append(", ipNomAge=");
		builder.append(ipNomAge);
		builder.append(", insOfferId=");
		builder.append(insOfferId);
		builder.append(", impliedAge=");
		builder.append(impliedAge);
		builder.append(", insMsisdn=");
		builder.append(insMsisdn);
		builder.append(", ipInsMsisdn=");
		builder.append(ipInsMsisdn);
		builder.append(", deactivatedProducts=");
		builder.append(deactivatedProducts);
		builder.append(", productRegistered=");
		builder.append(productRegistered);
		builder.append(", productActiveRegister=");
		builder.append(productActiveRegister);
		builder.append(", isReactive=");
		builder.append(isReactive);
		builder.append(", isCustExist=");
		builder.append(isCustExist);
		builder.append(", XLDeregisteredDate=");
		builder.append(XLDeregisteredDate);
		builder.append(", HPDeregisteredDate=");
		builder.append(HPDeregisteredDate);
		builder.append(", IPDeregisteredDate=");
		builder.append(IPDeregisteredDate);
		builder.append(", custProductCancellation=");
		builder.append(custProductCancellation);
		builder.append(", deRegisteredDate=");
		builder.append(deRegisteredDate);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append(", deactivated=");
		builder.append(deactivated);
		builder.append(", OfferName=");
		builder.append(OfferName);
		builder.append(", pakageHP=");
		builder.append(pakageHP);
		builder.append(", pakageXL=");
		builder.append(pakageXL);
		builder.append(", isData=");
		builder.append(isData);
		builder.append(", lcId=");
		builder.append(lcId);
		builder.append(", snId=");
		builder.append(snId);
		builder.append("]");
		return builder.toString();
	}
	

}
