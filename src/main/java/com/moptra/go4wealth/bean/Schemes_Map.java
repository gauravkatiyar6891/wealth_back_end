package com.moptra.go4wealth.bean;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schemes_map", catalog = "go4wealthdb")
public class Schemes_Map {

	private Integer mapId;
	private String bseSchemeCode;
	private String amfiIsin;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	
	
	public Schemes_Map() {
		
	}
	
	
	public Schemes_Map(Integer mapId, String bseSchemeCode, String amfiIsin, String field1, String field2,
			String field3, String field4, String field5) {
		
		this.mapId = mapId;
		this.bseSchemeCode = bseSchemeCode;
		this.amfiIsin = amfiIsin;
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
		this.field5 = field5;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "MAP_ID", unique = true, nullable = false)
	public Integer getMapId() {
		return mapId;
	}
	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}
	
	@Column(name = "BSE_SCHEME_CODE")
	public String getBseSchemeCode() {
		return bseSchemeCode;
	}
	public void setBseSchemeCode(String bseSchemeCode) {
		this.bseSchemeCode = bseSchemeCode;
	}
	
	@Column(name = "AMFI_ISIN")
	public String getAmfiIsin() {
		return amfiIsin;
	}
	public void setAmfiIsin(String amfiIsin) {
		this.amfiIsin = amfiIsin;
	}
	
	@Column(name = "FIELD1")
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	@Column(name = "FIELD2")
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	@Column(name = "FIELD3")
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	
	@Column(name = "FIELD4")
	public String getField4() {
		return field4;
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}
	
	@Column(name = "FIELD5")
	public String getField5() {
		return field5;
	}
	public void setField5(String field5) {
		this.field5 = field5;
	}
	
	

}
