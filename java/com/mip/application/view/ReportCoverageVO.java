package com.mip.application.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ReportCoverageVO implements Serializable {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 7095484955925506029L;

	private int reportId;
	private int branchId;
	private BigInteger zeroInactive;
	private int cover1;
	private BigInteger count1;
	private BigDecimal percent1;
	private int cover2;
	private BigInteger count2;
	private BigDecimal percent2;
	private int cover3;
	private BigInteger count3;
	private BigDecimal percent3;
	private int cover4;
	private BigInteger count4;
	private BigDecimal percent4;
	private int cover5;
	private BigInteger count5;
	private BigDecimal percent5;
	private int cover6;
	private BigInteger count6;
	private BigDecimal percent6;
	private int cover7;
	private BigInteger count7;
	private BigDecimal percent7;
	private int cover8;
	private BigInteger count8;
	private BigDecimal percent8;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportCoverageVO [reportId=");
		builder.append(reportId);
		builder.append(", branchId=");
		builder.append(branchId);
		builder.append(", zeroInactive=");
		builder.append(zeroInactive);
		builder.append(", cover1=");
		builder.append(cover1);
		builder.append(", count1=");
		builder.append(count1);
		builder.append(", percent1=");
		builder.append(percent1);
		builder.append(", cover2=");
		builder.append(cover2);
		builder.append(", count2=");
		builder.append(count2);
		builder.append(", percent2=");
		builder.append(percent2);
		builder.append(", cover3=");
		builder.append(cover3);
		builder.append(", count3=");
		builder.append(count3);
		builder.append(", percent3=");
		builder.append(percent3);
		builder.append(", cover4=");
		builder.append(cover4);
		builder.append(", count4=");
		builder.append(count4);
		builder.append(", percent4=");
		builder.append(percent4);
		builder.append(", cover5=");
		builder.append(cover5);
		builder.append(", count5=");
		builder.append(count5);
		builder.append(", percent5=");
		builder.append(percent5);
		builder.append(", cover6=");
		builder.append(cover6);
		builder.append(", count6=");
		builder.append(count6);
		builder.append(", percent6=");
		builder.append(percent6);
		builder.append(", cover7=");
		builder.append(cover7);
		builder.append(", count7=");
		builder.append(count7);
		builder.append(", percent7=");
		builder.append(percent7);
		builder.append(", cover8=");
		builder.append(cover8);
		builder.append(", count8=");
		builder.append(count8);
		builder.append(", percent8=");
		builder.append(percent8);
		builder.append("]");
		return builder.toString();
	}
	
	public BigInteger sumOfCount() {
		BigInteger coverSum = new BigInteger("0"); 
		if (this.cover1 != -1) {coverSum = coverSum.add(this.count1);}
		if (this.cover2 != -1) {coverSum = coverSum.add(this.count2);}
		if (this.cover3 != -1) {coverSum = coverSum.add(this.count3);}
		if (this.cover4 != -1) {coverSum = coverSum.add(this.count4);}
		if (this.cover5 != -1) {coverSum = coverSum.add(this.count5);}
		if (this.cover6 != -1) {coverSum = coverSum.add(this.count6);}
		if (this.cover7 != -1) {coverSum = coverSum.add(this.count7);}
		if (this.cover8 != -1) {coverSum = coverSum.add(this.count8);}
		return coverSum;
	}
	
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public BigInteger getZeroInactive() {
		return zeroInactive;
	}
	public void setZeroInactive(BigInteger zeroInactive) {
		this.zeroInactive = zeroInactive;
	}
	public int getCover1() {
		return cover1;
	}
	public void setCover1(int cover1) {
		this.cover1 = cover1;
	}
	public BigInteger getCount1() {
		return count1;
	}
	public void setCount1(BigInteger count1) {
		this.count1 = count1;
	}
	public BigDecimal getPercent1() {
		return percent1;
	}
	public void setPercent1(BigDecimal percent1) {
		this.percent1 = percent1;
	}
	public int getCover2() {
		return cover2;
	}
	public void setCover2(int cover2) {
		this.cover2 = cover2;
	}
	public BigInteger getCount2() {
		return count2;
	}
	public void setCount2(BigInteger count2) {
		this.count2 = count2;
	}
	public BigDecimal getPercent2() {
		return percent2;
	}
	public void setPercent2(BigDecimal percent2) {
		this.percent2 = percent2;
	}
	public int getCover3() {
		return cover3;
	}
	public void setCover3(int cover3) {
		this.cover3 = cover3;
	}
	public BigInteger getCount3() {
		return count3;
	}
	public void setCount3(BigInteger count3) {
		this.count3 = count3;
	}
	public BigDecimal getPercent3() {
		return percent3;
	}
	public void setPercent3(BigDecimal percent3) {
		this.percent3 = percent3;
	}
	public int getCover4() {
		return cover4;
	}
	public void setCover4(int cover4) {
		this.cover4 = cover4;
	}
	public BigInteger getCount4() {
		return count4;
	}
	public void setCount4(BigInteger count4) {
		this.count4 = count4;
	}
	public BigDecimal getPercent4() {
		return percent4;
	}
	public void setPercent4(BigDecimal percent4) {
		this.percent4 = percent4;
	}
	public int getCover5() {
		return cover5;
	}
	public void setCover5(int cover5) {
		this.cover5 = cover5;
	}
	public BigInteger getCount5() {
		return count5;
	}
	public void setCount5(BigInteger count5) {
		this.count5 = count5;
	}
	public BigDecimal getPercent5() {
		return percent5;
	}
	public void setPercent5(BigDecimal percent5) {
		this.percent5 = percent5;
	}
	public int getCover6() {
		return cover6;
	}
	public void setCover6(int cover6) {
		this.cover6 = cover6;
	}
	public BigInteger getCount6() {
		return count6;
	}
	public void setCount6(BigInteger count6) {
		this.count6 = count6;
	}
	public BigDecimal getPercent6() {
		return percent6;
	}
	public void setPercent6(BigDecimal percent6) {
		this.percent6 = percent6;
	}
	public int getCover7() {
		return cover7;
	}
	public void setCover7(int cover7) {
		this.cover7 = cover7;
	}
	public BigInteger getCount7() {
		return count7;
	}
	public void setCount7(BigInteger count7) {
		this.count7 = count7;
	}
	public BigDecimal getPercent7() {
		return percent7;
	}
	public void setPercent7(BigDecimal percent7) {
		this.percent7 = percent7;
	}
	public int getCover8() {
		return cover8;
	}
	public void setCover8(int cover8) {
		this.cover8 = cover8;
	}
	public BigInteger getCount8() {
		return count8;
	}
	public void setCount8(BigInteger count8) {
		this.count8 = count8;
	}
	public BigDecimal getPercent8() {
		return percent8;
	}
	public void setPercent8(BigDecimal percent8) {
		this.percent8 = percent8;
	}
}
