package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
  *   特殊存库映射
 *
 */
public class PlatSpecialWhs {
	
	private Integer stId;
	private String platCode; //平台编码
	private String invtyCode; //存货编码
	private String whsCode; //仓库编码
	private BigDecimal subQtyAll;//总数量
	
	private String whsNm; //仓库名称
	private String invtyNm; //存货名称
	private String ecName; //平台名称
	
	public Integer getStId() {
		return stId;
	}
	public void setStId(Integer stId) {
		this.stId = stId;
	}
	public String getPlatCode() {
		return platCode;
	}
	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}
	public String getInvtyCode() {
		return invtyCode;
	}
	public void setInvtyCode(String invtyCode) {
		this.invtyCode = invtyCode;
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
	public String getWhsNm() {
		return whsNm;
	}
	public void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}
	public String getInvtyNm() {
		return invtyNm;
	}
	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
	}
	
	
	
	
}
