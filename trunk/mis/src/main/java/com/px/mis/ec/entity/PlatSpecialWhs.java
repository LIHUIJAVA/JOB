package com.px.mis.ec.entity;

import java.math.BigDecimal;

/**
  *   ������ӳ��
 *
 */
public class PlatSpecialWhs {
	
	private Integer stId;
	private String platCode; //ƽ̨����
	private String invtyCode; //�������
	private String whsCode; //�ֿ����
	private BigDecimal subQtyAll;//������
	
	private String whsNm; //�ֿ�����
	private String invtyNm; //�������
	private String ecName; //ƽ̨����
	
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
