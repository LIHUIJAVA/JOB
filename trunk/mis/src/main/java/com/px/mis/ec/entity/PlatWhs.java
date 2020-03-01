package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
  *   ÆÕÍ¨´æ¿âÓ³Éä
 *
 */
public class PlatWhs {
	
	private Integer stockId;
	private String platCode; //Æ½Ì¨±àÂë
	private String cityCode; //ÇøÓò±àÂë
	private String whsCode; //²Ö¿â±àÂë
	private String invtyCode; //´æ»õ±àÂë
	private BigDecimal subQtyAll;//×ÜÊýÁ¿
	
	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	public String getPlatCode() {
		return platCode;
	}
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getWhsCode() {
		return whsCode;
	}
	public void setWhsCode(String whsCode) {
		this.whsCode = whsCode;
	}
	public BigDecimal getSubQtyAll() {
		return subQtyAll;
	}
	public void setSubQtyAll(BigDecimal subQtyAll) {
		this.subQtyAll = subQtyAll;
	}
	public String getInvtyCode() {
		return invtyCode;
	}
	public void setInvtyCode(String invtyCode) {
		this.invtyCode = invtyCode;
	}
	
	
	
}
