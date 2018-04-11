package com.mip.application.model;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerSubscription extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5163769654191591358L;

	private int snId;
	private CustomerDetails customerDetails;
	private ProductDetails productDetails;
	private BigDecimal earnedCover;
	private BigDecimal coverCharges;
	private BigDecimal prevMonthUsage;
	private UserDetails regBy;
	private CommunicationChannel regCommChannel;
	private Date regDate;
	private Byte confirmed;
	private CommunicationChannel confCommChannel;
	private Date confDate;
	private Byte isDeactivated;
	
	private Integer totalCustomerCount;
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerSubscription [snId=");
		builder.append(snId);
		builder.append(", earnedCover=");
		builder.append(earnedCover);
		builder.append(", coverCharges=");
		builder.append(coverCharges);
		builder.append(", prevMonthUsage=");
		builder.append(prevMonthUsage);
		builder.append(", regBy=");
		builder.append(regBy);
		builder.append(", confirmed=");
		builder.append(confirmed);
		builder.append(", regDate=");
		builder.append(regDate);
		builder.append(", confDate=");
		builder.append(confDate);
		builder.append("]");
		return builder.toString();
	}

	public int getSnId() {
		return snId;
	}

	public void setSnId(int snId) {
		this.snId = snId;
	}

	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public Integer getTotalCustomerCount() {
		return totalCustomerCount;
	}

	public void setTotalCustomerCount(Integer totalCustomerCount) {
		this.totalCustomerCount = totalCustomerCount;
	}

	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}

	public BigDecimal getEarnedCover() {
		return earnedCover;
	}

	public void setEarnedCover(BigDecimal earnedCover) {
		this.earnedCover = earnedCover;
	}

	public BigDecimal getCoverCharges() {
		return coverCharges;
	}

	public void setCoverCharges(BigDecimal coverCharges) {
		this.coverCharges = coverCharges;
	}

	public BigDecimal getPrevMonthUsage() {
		return prevMonthUsage;
	}

	public void setPrevMonthUsage(BigDecimal prevMonthUsage) {
		this.prevMonthUsage = prevMonthUsage;
	}

	public UserDetails getRegBy() {
		return regBy;
	}

	public void setRegBy(UserDetails regBy) {
		this.regBy = regBy;
	}

	public CommunicationChannel getRegCommChannel() {
		return regCommChannel;
	}

	public void setRegCommChannel(CommunicationChannel regCommChannel) {
		this.regCommChannel = regCommChannel;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Byte getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Byte confirmed) {
		this.confirmed = confirmed;
	}

	public CommunicationChannel getConfCommChannel() {
		return confCommChannel;
	}

	public void setConfCommChannel(CommunicationChannel confCommChannel) {
		this.confCommChannel = confCommChannel;
	}

	public Date getConfDate() {
		return confDate;
	}

	public void setConfDate(Date confDate) {
		this.confDate = confDate;
	}

	public Byte getIsDeactivated() {
		return isDeactivated;
	}

	public void setIsDeactivated(Byte isDeactivated) {
		this.isDeactivated = isDeactivated;
	}	
	
}
