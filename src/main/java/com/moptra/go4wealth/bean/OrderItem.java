package com.moptra.go4wealth.bean;
// Generated 1 Sep, 2018 6:25:25 PM by Hibernate Tools 5.1.0.Alpha1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

/**
 * OrderItem generated by hbm2java
 */
@Entity
@Table(name = "order_item", catalog = "go4wealthdb")
public class OrderItem implements java.io.Serializable {

	private Integer orderItemId;
	private Orders orders;
	private Scheme scheme;
	private BigDecimal productprice;
	private String status;
	private Date lastcreate;
	private Date lastupdate;
	private Date timereleased;
	private Date timeshiped;
	private double quantity;
	private BigDecimal taxamount;
	private BigDecimal totaladjustment;
	private String description;
	private String modelPortfolioCategory;
	private String orderCategory;
	private Integer mPBundleId;
	private Integer field1;
	private String field2;
	private String field3;

	public OrderItem() {
	}

	public OrderItem(Orders orders, Scheme scheme, BigDecimal productprice, String status, Date lastcreate,
			Date lastupdate, Date timereleased, Date timeshiped, double quantity, BigDecimal taxamount,
			BigDecimal totaladjustment,String modelPortfolioCategory,String orderCategory,Integer mPBundleId) {
		this.orders = orders;
		this.scheme = scheme;
		this.productprice = productprice;
		this.status = status;
		this.lastcreate = lastcreate;
		this.lastupdate = lastupdate;
		this.timereleased = timereleased;
		this.timeshiped = timeshiped;
		this.quantity = quantity;
		this.taxamount = taxamount;
		this.totaladjustment = totaladjustment;
		this.modelPortfolioCategory = modelPortfolioCategory;
		this.orderCategory = orderCategory;
		this.mPBundleId = mPBundleId;
	}

	public OrderItem(Orders orders, Scheme scheme, BigDecimal productprice, String status, Date lastcreate,
			Date lastupdate, Date timereleased, Date timeshiped, double quantity, BigDecimal taxamount,
			BigDecimal totaladjustment, String description,String modelPortfolioCategory,String orderCategory,Integer mPBundleId, Integer field1, String field2, String field3) {
		this.orders = orders;
		this.scheme = scheme;
		this.productprice = productprice;
		this.status = status;
		this.lastcreate = lastcreate;
		this.lastupdate = lastupdate;
		this.timereleased = timereleased;
		this.timeshiped = timeshiped;
		this.quantity = quantity;
		this.taxamount = taxamount;
		this.totaladjustment = totaladjustment;
		this.description = description;
		this.modelPortfolioCategory = modelPortfolioCategory;
		this.orderCategory = orderCategory;
		this.mPBundleId = mPBundleId;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ORDER_ITEM_ID", unique = true, nullable = false)
	public Integer getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDERS_ID", nullable = false)
	public Orders getOrders() {
		return this.orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEME_ID", nullable = false)
	public Scheme getScheme() {
		return this.scheme;
	}

	public void setScheme(Scheme scheme) {
		this.scheme = scheme;
	}

	@Column(name = "PRODUCTPRICE", nullable = false, precision = 20, scale = 5)
	public BigDecimal getProductprice() {
		return this.productprice;
	}

	public void setProductprice(BigDecimal productprice) {
		this.productprice = productprice;
	}

	@Column(name = "STATUS", nullable = false, length = 3)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTCREATE", nullable = false, length = 19)
	public Date getLastcreate() {
		return this.lastcreate;
	}

	public void setLastcreate(Date lastcreate) {
		this.lastcreate = lastcreate;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTUPDATE", nullable = false, length = 19)
	public Date getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMERELEASED", nullable = false, length = 19)
	public Date getTimereleased() {
		return this.timereleased;
	}

	public void setTimereleased(Date timereleased) {
		this.timereleased = timereleased;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESHIPED", nullable = false, length = 19)
	public Date getTimeshiped() {
		return this.timeshiped;
	}

	public void setTimeshiped(Date timeshiped) {
		this.timeshiped = timeshiped;
	}

	@Column(name = "QUANTITY", nullable = false, precision = 22, scale = 0)
	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "TAXAMOUNT", nullable = false, precision = 20, scale = 5)
	public BigDecimal getTaxamount() {
		return this.taxamount;
	}

	public void setTaxamount(BigDecimal taxamount) {
		this.taxamount = taxamount;
	}

	@Column(name = "TOTALADJUSTMENT", nullable = false, precision = 20, scale = 5)
	public BigDecimal getTotaladjustment() {
		return this.totaladjustment;
	}

	public void setTotaladjustment(BigDecimal totaladjustment) {
		this.totaladjustment = totaladjustment;
	}

	@Column(name = "DESCRIPTION", length = 254)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "MODEL_PORTFOLIO_CATEGORY", length = 50)
	public String getModelPortfolioCategory() {
		return modelPortfolioCategory;
	}
	public void setModelPortfolioCategory(String modelPortfolioCategory) {
		this.modelPortfolioCategory = modelPortfolioCategory;
	}
	
	@Column(name = "ORDER_CATEGORY", length = 50)
	public String getOrderCategory() {
		return orderCategory;
	}
	
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}
	
	@Column(name = "M_P_BUNDLE_ID", length = 50)
	public Integer getmPBundleId() {
		return mPBundleId;
	}
	public void setmPBundleId(Integer mPBundleId) {
		this.mPBundleId = mPBundleId;
	}
	
	@Column(name = "FIELD1")
	public Integer getField1() {
		return this.field1;
	}

	public void setField1(Integer field1) {
		this.field1 = field1;
	}

	@Column(name = "FIELD2", length = 128)
	public String getField2() {
		return this.field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	@Column(name = "FIELD3", length = 254)
	public String getField3() {
		return this.field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

}
