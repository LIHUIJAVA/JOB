package com.px.mis.sell.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �ɹ��ִ�����ѯ
 */
public class ProcurementNowStokReport {
	@JsonProperty("�ֿ����")
	private String whsEncd; // �ֿ����,
	@JsonProperty("�������")
	private String invtyEncd; // �������,
	@JsonProperty("����")
	private String batNum; // ����,
	@JsonProperty("���")
	private String bxQty; // ���,
	@JsonProperty("�������")
	private BigDecimal intoWhsQty; // �������,
	@JsonProperty("�����������")
	private BigDecimal intoCannibQty; // �����������,
	@JsonProperty("����������")
	private BigDecimal sellOutWhsQty; // ����������,
	@JsonProperty("������������")
	private BigDecimal outCannibQty; // ������������,
	@JsonProperty("�ִ���")
	private BigDecimal nowStok; // invty_tab.now_stok �ִ���,
	@JsonProperty("������")
	private BigDecimal avalQty; // invty_tab.aval_qty ������,
	@JsonProperty("��������")
	private String prdcDt; // ��������,
	@JsonProperty("������")
	private String baoZhQi; // ������,
	@JsonProperty("ʧЧ����")
	private String invldtnDt; // ʧЧ����,
	@JsonProperty("�ο��ɱ�")
	private BigDecimal refCost; // invty_doc.ref_cost �ο��ɱ�,
	@JsonProperty("�ֿ�")
	private String whsNm; // whs_doc.whs_nm �ֿ�,
	@JsonProperty("���")
	private String invtyNm; // invty_doc.invty_nm ���,
	@JsonProperty("���")
	private String spcModel; // invty_doc.spc_model ���,
	@JsonProperty("������λ")
	private String measrCorpNm; // measr_corp_doc.measr_corp_nm ������λ

	public final String getWhsEncd() {
		return whsEncd;
	}

	public final void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public final String getInvtyEncd() {
		return invtyEncd;
	}

	public final void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public final String getBatNum() {
		return batNum;
	}

	public final void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public final String getBxQty() {
		return bxQty;
	}

	public final void setBxQty(String bxQty) {
		this.bxQty = bxQty;
	}

	public final BigDecimal getIntoWhsQty() {
		return intoWhsQty;
	}

	public final void setIntoWhsQty(BigDecimal intoWhsQty) {
		this.intoWhsQty = intoWhsQty;
	}

	public final BigDecimal getIntoCannibQty() {
		return intoCannibQty;
	}

	public final void setIntoCannibQty(BigDecimal intoCannibQty) {
		this.intoCannibQty = intoCannibQty;
	}

	public final BigDecimal getSellOutWhsQty() {
		return sellOutWhsQty;
	}

	public final void setSellOutWhsQty(BigDecimal sellOutWhsQty) {
		this.sellOutWhsQty = sellOutWhsQty;
	}

	public final BigDecimal getOutCannibQty() {
		return outCannibQty;
	}

	public final void setOutCannibQty(BigDecimal outCannibQty) {
		this.outCannibQty = outCannibQty;
	}

	public final BigDecimal getNowStok() {
		return nowStok;
	}

	public final void setNowStok(BigDecimal nowStok) {
		this.nowStok = nowStok;
	}

	public final BigDecimal getAvalQty() {
		return avalQty;
	}

	public final void setAvalQty(BigDecimal avalQty) {
		this.avalQty = avalQty;
	}

	public final String getPrdcDt() {
		return prdcDt;
	}

	public final void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}

	public final String getBaoZhQi() {
		return baoZhQi;
	}

	public final void setBaoZhQi(String baoZhQi) {
		this.baoZhQi = baoZhQi;
	}

	public final String getInvldtnDt() {
		return invldtnDt;
	}

	public final void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}

	public final BigDecimal getRefCost() {
		return refCost;
	}

	public final void setRefCost(BigDecimal refCost) {
		this.refCost = refCost;
	}

	public final String getWhsNm() {
		return whsNm;
	}

	public final void setWhsNm(String whsNm) {
		this.whsNm = whsNm;
	}

	public final String getInvtyNm() {
		return invtyNm;
	}

	public final void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public final String getSpcModel() {
		return spcModel;
	}

	public final void setSpcModel(String spcModel) {
		this.spcModel = spcModel;
	}

	public final String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public final void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

}
