package com.moptra.go4wealth.bean;
// Generated 4 Aug, 2018 3:40:15 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Ppcpaytran generated by hbm2java
 */
@Entity
@Table(name = "ppcpaytran", catalog = "go4wealthdb")
public class Ppcpaytran implements java.io.Serializable {

	private long transactionId;
	private Ppcpayinst ppcpayinst;
	private int transactiontype;
	private BigDecimal processedamount;
	private int state;
	private Date timecreated;
	private Date timeupdated;
	private String responsecode;
	private String reasoncode;
	private String referencenumber;
	private String trackingid;
	private String field1;
	private String field2;
	private Integer userId;

	public Ppcpaytran() {
	}

	public Ppcpaytran(Ppcpayinst ppcpayinst, int transactiontype, BigDecimal processedamount, int state,
			Date timecreated, Date timeupdated) {
		this.ppcpayinst = ppcpayinst;
		this.transactiontype = transactiontype;
		this.processedamount = processedamount;
		this.state = state;
		this.timecreated = timecreated;
		this.timeupdated = timeupdated;
	}

	public Ppcpaytran(Ppcpayinst ppcpayinst, int transactiontype, BigDecimal processedamount, int state,
			Date timecreated, Date timeupdated, String responsecode, String reasoncode, String referencenumber,
			String trackingid, String field1, String field2, Integer userId) {
		this.ppcpayinst = ppcpayinst;
		this.transactiontype = transactiontype;
		this.processedamount = processedamount;
		this.state = state;
		this.timecreated = timecreated;
		this.timeupdated = timeupdated;
		this.responsecode = responsecode;
		this.reasoncode = reasoncode;
		this.referencenumber = referencenumber;
		this.trackingid = trackingid;
		this.field1 = field1;
		this.field2 = field2;
		this.userId = userId;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "ppcpayinst"))
	@Id
	@GeneratedValue(generator = "generator")

	@Column(name = "TRANSACTION_ID", unique = true, nullable = false)
	public long getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Ppcpayinst getPpcpayinst() {
		return this.ppcpayinst;
	}

	public void setPpcpayinst(Ppcpayinst ppcpayinst) {
		this.ppcpayinst = ppcpayinst;
	}

	@Column(name = "TRANSACTIONTYPE", nullable = false)
	public int getTransactiontype() {
		return this.transactiontype;
	}

	public void setTransactiontype(int transactiontype) {
		this.transactiontype = transactiontype;
	}

	@Column(name = "PROCESSEDAMOUNT", nullable = false, precision = 20, scale = 5)
	public BigDecimal getProcessedamount() {
		return this.processedamount;
	}

	public void setProcessedamount(BigDecimal processedamount) {
		this.processedamount = processedamount;
	}

	@Column(name = "STATE", nullable = false)
	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMECREATED", nullable = false, length = 19)
	public Date getTimecreated() {
		return this.timecreated;
	}

	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEUPDATED", nullable = false, length = 19)
	public Date getTimeupdated() {
		return this.timeupdated;
	}

	public void setTimeupdated(Date timeupdated) {
		this.timeupdated = timeupdated;
	}

	@Column(name = "RESPONSECODE", length = 512)
	public String getResponsecode() {
		return this.responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	@Column(name = "REASONCODE", length = 25)
	public String getReasoncode() {
		return this.reasoncode;
	}

	public void setReasoncode(String reasoncode) {
		this.reasoncode = reasoncode;
	}

	@Column(name = "REFERENCENUMBER", length = 512)
	public String getReferencenumber() {
		return this.referencenumber;
	}

	public void setReferencenumber(String referencenumber) {
		this.referencenumber = referencenumber;
	}

	@Column(name = "TRACKINGID", length = 512)
	public String getTrackingid() {
		return this.trackingid;
	}

	public void setTrackingid(String trackingid) {
		this.trackingid = trackingid;
	}

	@Column(name = "FIELD1", length = 128)
	public String getField1() {
		return this.field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	@Column(name = "FIELD2", length = 254)
	public String getField2() {
		return this.field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	
	@Column(name = "USER_ID",length = 50)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
	

}
